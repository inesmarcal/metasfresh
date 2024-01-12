package de.metas.ui.web.material.cockpit.rowfactory;

import com.google.common.collect.ImmutableList;
import de.metas.ad_reference.ADReferenceService;
import de.metas.dimension.DimensionSpec;
import de.metas.dimension.DimensionSpecGroup;
import de.metas.material.cockpit.QtyDemandQtySupply;
import de.metas.material.cockpit.model.I_MD_Cockpit;
import de.metas.material.cockpit.model.I_MD_Stock;
import de.metas.material.cockpit.model.I_QtyDemand_QtySupply_V;
import de.metas.material.event.commons.AttributesKey;
import de.metas.material.event.commons.ProductDescriptor;
import de.metas.printing.esb.base.util.Check;
import de.metas.product.IProductBL;
import de.metas.ui.web.material.cockpit.MaterialCockpitDetailsRowAggregation;
import de.metas.ui.web.material.cockpit.MaterialCockpitDetailsRowAggregationIdentifier;
import de.metas.ui.web.material.cockpit.MaterialCockpitRow;
import de.metas.ui.web.material.cockpit.MaterialCockpitRow.MainRowBuilder;
import de.metas.ui.web.material.cockpit.MaterialCockpitRowLookups;
import de.metas.ui.web.window.datatypes.ColorValue;
import de.metas.util.ColorId;
import de.metas.util.IColorRepository;
import de.metas.util.MFColor;
import de.metas.util.Services;
import io.micrometer.core.lang.NonNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.adempiere.warehouse.WarehouseId;
import org.adempiere.warehouse.api.IWarehouseDAO;
import org.compiere.model.I_M_Product;
import org.compiere.model.I_M_Warehouse;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
 * #%L
 * metasfresh-webui-api
 * %%
 * Copyright (C) 2017 metas GmbH
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

@Data
@EqualsAndHashCode(of = "productIdAndDate")
@RequiredArgsConstructor
public class MainRowWithSubRows
{
	@NonNull private final MaterialCockpitRowLookups rowLookups;
	@NonNull private final MainRowBucketId productIdAndDate;

	@NonNull
	private final MainRowBucket mainRow = new MainRowBucket();
	@NonNull
	private final Map<DimensionSpecGroup, DimensionGroupSubRowBucket> dimensionGroupSubRows = new LinkedHashMap<>();
	@NonNull
	private final Map<MaterialCockpitDetailsRowAggregationIdentifier, CountingSubRowBucket> countingSubRows = new LinkedHashMap<>();
	@NonNull
	private final IProductBL productBL = Services.get(IProductBL.class);
	@NonNull
	private final IWarehouseDAO warehouseDAO = Services.get(IWarehouseDAO.class);
	@NonNull
	final ADReferenceService adReferenceService = ADReferenceService.get();

	public static MainRowWithSubRows create(
			@NonNull final MainRowBucketId productIdAndDate,
			@NonNull final HighPriceProvider highPriceProvider,
			@NonNull final MaterialCockpitRowLookups rowLookups)
	{
		return new MainRowWithSubRows(productIdAndDate, highPriceProvider, rowLookups);
	}

	private MainRowWithSubRows(
			@NonNull final MainRowBucketId productIdAndDate,
			@NonNull final HighPriceProvider highPriceProvider,
			@NonNull final MaterialCockpitRowLookups rowLookups)
	{
		this.productIdAndDate = productIdAndDate;
		this.rowLookups = rowLookups;

		final I_M_Product product = productBL.getById(productIdAndDate.getProductId());
		final ColorId colorId = adReferenceService.getColorId(product, I_M_Product.COLUMNNAME_ProcurementStatus, product.getProcurementStatus());
		final String procurementStatus = colorId != null ? toHexString(Services.get(IColorRepository.class).getColorById(colorId)) : null;
		this.mainRow.setProcurementStatus(procurementStatus);

		final HighPriceProvider.HighPriceRequest request = HighPriceProvider.HighPriceRequest.builder()
				.productDescriptor(ProductDescriptor.completeForProductIdAndEmptyAttribute(productIdAndDate.getProductId().getRepoId()))
				.evalDate(productIdAndDate.getDate())
				.build();

		this.mainRow.setHighestPurchasePrice_AtDate(highPriceProvider.getHighestPrice(request).getMaxPurchasePrice());

	}

	@Nullable
	private static String toHexString(@Nullable final MFColor color)
	{
		if (color == null)
		{
			return null;
		}

		final Color awtColor = color.toFlatColor().getFlatColor();
		return ColorValue.toHexString(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
	}

	public void addEmptyAttributesSubrowBucket(@NonNull final DimensionSpecGroup dimensionSpecGroup)
	{
		dimensionGroupSubRows.computeIfAbsent(dimensionSpecGroup, this::newSubRowBucket);
	}

	private DimensionGroupSubRowBucket newSubRowBucket(@NonNull final DimensionSpecGroup dimensionSpecGroup)
	{
		return new DimensionGroupSubRowBucket(rowLookups, dimensionSpecGroup);
	}

	public void addEmptyCountingSubrowBucket(final MaterialCockpitDetailsRowAggregationIdentifier detailsRowAggregationIdentifier)
	{
		countingSubRows.computeIfAbsent(detailsRowAggregationIdentifier, this::newCountingSubRowBucket);
	}

	private CountingSubRowBucket newCountingSubRowBucket(final MaterialCockpitDetailsRowAggregationIdentifier detailsRowAggregationIdentifier)
	{
		return new CountingSubRowBucket(rowLookups,
										detailsRowAggregationIdentifier);
	}

	public void addCockpitRecord(
			@NonNull final I_MD_Cockpit cockpitRecord,
			@NonNull final DimensionSpec dimensionSpec,
			@NonNull final MaterialCockpitDetailsRowAggregation detailsRowAggregation)
	{
		boolean addedToAtLeastOneBucket = false;

		int ppPlantId = 0;
		final WarehouseId warehouseId = WarehouseId.ofRepoIdOrNull(cockpitRecord.getM_Warehouse_ID());
		if (warehouseId != null)
		{
			final I_M_Warehouse warehouse = warehouseDAO.getById(warehouseId);
			ppPlantId = warehouse.getPP_Plant_ID();
		}

		if (cockpitRecord.getQtyStockEstimateCount_AtDate().signum() != 0 || ppPlantId > 0)
		{
			if (detailsRowAggregation.isPlant())
			{
				final MaterialCockpitDetailsRowAggregationIdentifier rowAggregationIdentifier = MaterialCockpitDetailsRowAggregationIdentifier.builder().detailsRowAggregation(detailsRowAggregation)
						.aggregationId(ppPlantId)
						.build();
				addCockpitRecordToCounting(cockpitRecord, rowAggregationIdentifier);
				addedToAtLeastOneBucket = true;
			}
		}
		if (cockpitRecord.getQtyStockEstimateCount_AtDate().signum() != 0 || warehouseId != null)
		{
			if (detailsRowAggregation.isWarehouse())
			{
				final MaterialCockpitDetailsRowAggregationIdentifier rowAggregationIdentifier = MaterialCockpitDetailsRowAggregationIdentifier.builder().detailsRowAggregation(detailsRowAggregation)
						.aggregationId(WarehouseId.toRepoId(warehouseId))
						.build();
				addCockpitRecordToCounting(cockpitRecord, rowAggregationIdentifier);
				addedToAtLeastOneBucket = true;
			}
		}
		if (!addedToAtLeastOneBucket)
		{
			addedToAtLeastOneBucket = addCockpitRecordToDimensionGroups(cockpitRecord, dimensionSpec);
		}

		if (!addedToAtLeastOneBucket)
		{
			// we need at least one subRow-bucket, even if there is no qtyOnHandCount, no plant and our dimensionSpec is empty!
			final DimensionGroupSubRowBucket fallbackBucket = dimensionGroupSubRows.computeIfAbsent(DimensionSpecGroup.OTHER_GROUP, this::newSubRowBucket);
			fallbackBucket.addCockpitRecord(cockpitRecord);
		}
		mainRow.addDataRecord(cockpitRecord);
	}

	private void addCockpitRecordToCounting(@NonNull final I_MD_Cockpit stockEstimate, final MaterialCockpitDetailsRowAggregationIdentifier detailsRowAggregationIdentifier)
	{
		final CountingSubRowBucket countingSubRow = countingSubRows.computeIfAbsent(detailsRowAggregationIdentifier, this::newCountingSubRowBucket);
		countingSubRow.addCockpitRecord(stockEstimate);
	}

	private void addQuantitiesRecordToCounting(@NonNull final I_QtyDemand_QtySupply_V quantitiesRecord, @NonNull final MaterialCockpitDetailsRowAggregationIdentifier detailsRowAggregationIdentifier)
	{
		final CountingSubRowBucket countingSubRow = countingSubRows.computeIfAbsent(detailsRowAggregationIdentifier, this::newCountingSubRowBucket);
		countingSubRow.addQuantitiesRecord(quantitiesRecord);
	}

	/**
	 * @return true if there was at least one {@link DimensionGroupSubRowBucket} to which the given dataRecord could be added.
	 */
	private boolean addCockpitRecordToDimensionGroups(
			@NonNull final I_MD_Cockpit dataRecord,
			@NonNull final DimensionSpec dimensionSpec)
	{
		assertProductIdAndDateOfDataRecord(dataRecord);

		final AttributesKey attributesKey = AttributesKey.ofString(dataRecord.getAttributesKey());
		final List<DimensionGroupSubRowBucket> subRowBuckets = findOrCreateSubRowBucket(attributesKey, dimensionSpec);
		subRowBuckets.forEach(bucket -> bucket.addCockpitRecord(dataRecord));
		return !subRowBuckets.isEmpty();
	}

	/**
	 * @return true if there was at least one {@link DimensionGroupSubRowBucket} to which the given quantitiesRecord could be added.
	 */
	private boolean addQuantitiesRecordToDimensionGroups(
			@NonNull final QtyDemandQtySupply quantitiesRecord,
			@NonNull final DimensionSpec dimensionSpec)
	{
		final List<DimensionGroupSubRowBucket> subRowBuckets = findOrCreateSubRowBucket(quantitiesRecord.getAttributesKey(), dimensionSpec);
		subRowBuckets.forEach(bucket -> bucket.addQuantitiesRecord(quantitiesRecord));
		return !subRowBuckets.isEmpty();
	}

	private void assertProductIdAndDateOfDataRecord(@NonNull final I_MD_Cockpit dataRecord)
	{
		final MainRowBucketId key = MainRowBucketId.createInstanceForCockpitRecord(dataRecord);

		Check.errorUnless(
				productIdAndDate.equals(key),
				"The given parameter 'dataRecord' does not fit into this bucket; our productIdAndDate={}; dataRecord's productIdAndDate={}; fdataRecord={}",
				productIdAndDate, key, dataRecord);
	}

	private List<DimensionGroupSubRowBucket> findOrCreateSubRowBucket(
			@NonNull final AttributesKey dataRecordAttributesKey,
			@NonNull final DimensionSpec dimensionSpec)
	{
		final ImmutableList.Builder<DimensionGroupSubRowBucket> result = ImmutableList.builder();

		DimensionSpecGroup otherGroup = null;
		boolean addedToAnyGroup = false;

		for (final DimensionSpecGroup group : dimensionSpec.retrieveGroups())
		{
			final AttributesKey dimensionAttributesKey = group.getAttributesKey();

			if (DimensionSpecGroup.EMPTY_GROUP.equals(group) && AttributesKey.NONE.equals(dataRecordAttributesKey))
			{
				result.add(dimensionGroupSubRows.computeIfAbsent(group, this::newSubRowBucket));
				addedToAnyGroup = true;
				continue;
			}
			else if (dataRecordAttributesKey.intersects(dimensionAttributesKey))
			{
				result.add(dimensionGroupSubRows.computeIfAbsent(group, this::newSubRowBucket));
				addedToAnyGroup = true;
				continue;
			}

			// while iterating, also look out for "otherGroup"
			if (DimensionSpecGroup.OTHER_GROUP.equals(group))
			{
				otherGroup = group;
			}
		}

		if (!addedToAnyGroup && otherGroup != null)
		{
			result.add(dimensionGroupSubRows.computeIfAbsent(otherGroup, this::newSubRowBucket));
		}
		return result.build();
	}

	public void addStockRecord(
			@NonNull final I_MD_Stock stockRecord,
			@NonNull final DimensionSpec dimensionSpec,
			@NonNull final MaterialCockpitDetailsRowAggregation detailsRowAggregation)
	{
		boolean addedToAtLeastOneBucket = false;
		if (!detailsRowAggregation.isNone())
		{
			addStockRecordToCounting(stockRecord, detailsRowAggregation);
			addedToAtLeastOneBucket = true;
		}
		addedToAtLeastOneBucket = addStockRecordToDimensionGroups(stockRecord, dimensionSpec) || addedToAtLeastOneBucket;

		if (!addedToAtLeastOneBucket)
		{
			// we need at least one subRow-bucket, even if there is no qtyOnHandCount, no plant and our dimensionSpec is empty!
			final DimensionGroupSubRowBucket fallbackBucket = dimensionGroupSubRows.computeIfAbsent(DimensionSpecGroup.OTHER_GROUP, this::newSubRowBucket);
			fallbackBucket.addStockRecord(stockRecord);
		}

		mainRow.addStockRecord(stockRecord);
	}

	public void addQuantitiesRecord(
			@NonNull final QtyDemandQtySupply qtyDemandQtySupply,
			@NonNull final DimensionSpec dimensionSpec,
			@NonNull final MaterialCockpitDetailsRowAggregation detailsRowAggregation)
	{
		boolean addedToAtLeastOneBucket = false;

		int ppPlantId = 0;
		if (qtyDemandQtySupply.getWarehouseId() != null)
		{
			final I_M_Warehouse warehouse = warehouseDAO.getById(qtyDemandQtySupply.getWarehouseId());
			ppPlantId = warehouse.getPP_Plant_ID();
		}

		if (ppPlantId > 0)
		{
			if (detailsRowAggregation.isPlant())
			{
				final MaterialCockpitDetailsRowAggregationIdentifier rowAggregationIdentifier = MaterialCockpitDetailsRowAggregationIdentifier.builder().detailsRowAggregation(detailsRowAggregation)
						.aggregationId(ppPlantId)
						.build();
				addQuantitiesRecordToCounting(quantitiesRecord, rowAggregationIdentifier);
				addedToAtLeastOneBucket = true;

			}
		}
		if(warehouseId!=null)
		{
			if (detailsRowAggregation.isWarehouse())
			{
				final MaterialCockpitDetailsRowAggregationIdentifier rowAggregationIdentifier = MaterialCockpitDetailsRowAggregationIdentifier.builder().detailsRowAggregation(detailsRowAggregation)
						.aggregationId(WarehouseId.toRepoId(warehouseId))
						.build();

				addQuantitiesRecordToCounting(quantitiesRecord, rowAggregationIdentifier);
				addedToAtLeastOneBucket = true;
			}
		}

		if (!addedToAtLeastOneBucket)
		{
			addedToAtLeastOneBucket = addQuantitiesRecordToDimensionGroups(quantitiesRecord, dimensionSpec);
		}

		if (!addedToAtLeastOneBucket)
		{
			final DimensionGroupSubRowBucket fallbackBucket = dimensionGroupSubRows.computeIfAbsent(DimensionSpecGroup.OTHER_GROUP, this::newSubRowBucket);
			fallbackBucket.addQuantitiesRecord(quantitiesRecord);
		}

		mainRow.addQuantitiesRecord(quantitiesRecord);
	}

	private void addStockRecordToCounting(final @NonNull I_MD_Stock stockRecord,
			final @NonNull MaterialCockpitDetailsRowAggregation detailsRowAggregation)
	{

		if (detailsRowAggregation.isNone())
		{
			// nothing to do
			return;
		}
		final WarehouseId warehouseId = WarehouseId.ofRepoId(stockRecord.getM_Warehouse_ID());
		final I_M_Warehouse warehouseRecord = warehouseDAO.getById(warehouseId);
		final int plantId = warehouseRecord.getPP_Plant_ID();

		if (detailsRowAggregation.isPlant())
		{
			final MaterialCockpitDetailsRowAggregationIdentifier rowAggregationIdentifier = MaterialCockpitDetailsRowAggregationIdentifier.builder().detailsRowAggregation(detailsRowAggregation)
					.aggregationId(plantId)
					.build();

			final CountingSubRowBucket countingSubRow = countingSubRows.computeIfAbsent(rowAggregationIdentifier, this::newCountingSubRowBucket);
			countingSubRow.addStockRecord(stockRecord);
		}
		else if (detailsRowAggregation.isWarehouse())
		{
			final MaterialCockpitDetailsRowAggregationIdentifier rowAggregationIdentifier = MaterialCockpitDetailsRowAggregationIdentifier.builder().detailsRowAggregation(detailsRowAggregation)
					.aggregationId(stockRecord.getM_Warehouse_ID())
					.build();

			final CountingSubRowBucket countingSubRow = countingSubRows.computeIfAbsent(rowAggregationIdentifier, this::newCountingSubRowBucket);
			countingSubRow.addStockRecord(stockRecord);
		}
	}

	/**
	 * @return true if there was at least one {@link DimensionGroupSubRowBucket} to which the given dataRecord could be added.
	 */
	private boolean addStockRecordToDimensionGroups(
			@NonNull final I_MD_Stock dataRecord,
			@NonNull final DimensionSpec dimensionSpec)
	{
		final AttributesKey attributesKey = AttributesKey.ofString(dataRecord.getAttributesKey());
		final List<DimensionGroupSubRowBucket> subRowBuckets = findOrCreateSubRowBucket(attributesKey, dimensionSpec);
		subRowBuckets.forEach(bucket -> bucket.addStockRecord(dataRecord));

		return !subRowBuckets.isEmpty();
	}

	public MaterialCockpitRow createMainRowWithSubRows()
	{
		final MainRowBuilder mainRowBuilder = MaterialCockpitRow.mainRowBuilder()
				.lookups(rowLookups)
				.productId(productIdAndDate.getProductId())
				.date(productIdAndDate.getDate())
				.qtyMaterialentnahmeAtDate(mainRow.getQtyMaterialentnahmeAtDate())
				.qtyDemandPPOrderAtDate(mainRow.getQtyDemandPPOrderAtDate())
				.qtyStockCurrentAtDate(mainRow.getQtyStockCurrentAtDate())
				.qtyOnHandStock(mainRow.getQtyOnHand())
				.qtySupplyPPOrderAtDate(mainRow.getQtySupplyPPOrderAtDate())
				.qtySupplyPurchaseOrderAtDate(mainRow.getQtySupplyPurchaseOrderAtDate())
				.qtySupplyPurchaseOrder(mainRow.getQtySupplyPurchaseOrder())
				.qtySupplyDDOrderAtDate(mainRow.getQtySupplyDDOrderAtDate())
				.qtySupplySumAtDate(mainRow.getQtySupplySumAtDate())
				.qtySupplyRequiredAtDate(mainRow.getQtySupplyRequiredAtDate())
				.qtySupplyToScheduleAtDate(mainRow.getQtySupplyToScheduleAtDate())
				.qtyExpectedSurplusAtDate(mainRow.getQtyExpectedSurplusAtDate())
				.qtyDemandSalesOrderAtDate(mainRow.getQtyDemandSalesOrderAtDate())
				.qtyDemandSalesOrder(mainRow.getQtyDemandSalesOrder())
				.qtyDemandDDOrderAtDate(mainRow.getQtyDemandDDOrderAtDate())
				.qtyDemandSumAtDate(mainRow.getQtyDemandSumAtDate())
				.qtyInventoryCountAtDate(mainRow.getQtyInventoryCountAtDate())
				.qtyInventoryTimeAtDate(mainRow.getQtyInventoryTimeAtDate())
				.qtyStockEstimateCountAtDate(mainRow.getQtyStockEstimateCountAtDate())
				.qtyStockEstimateTimeAtDate(mainRow.getQtyStockEstimateTimeAtDate())
				.qtyStockEstimateSeqNoAtDate(mainRow.getQtyStockEstimateSeqNoAtDate())
				.pmmQtyPromisedAtDate(mainRow.getPmmQtyPromisedAtDate())
				.procurementStatus(mainRow.getProcurementStatus())
				.highestPurchasePrice_AtDate(mainRow.getHighestPurchasePrice_AtDate())
				.qtyOrdered_PurchaseOrder_AtDate(mainRow.getQtyOrdered_PurchaseOrder_AtDate())
				.qtyOrdered_SalesOrder_AtDate(mainRow.getQtyOrdered_SalesOrder_AtDate())
				.availableQty_AtDate(mainRow.getAvailableQty_AtDate())
				.remainingStock_AtDate(mainRow.getRemainingStock_AtDate())
				.pmm_QtyPromised_NextDay(mainRow.getPmm_QtyPromised_NextDay())
				.allIncludedCockpitRecordIds(mainRow.getCockpitRecordIds())
				.allIncludedStockRecordIds(mainRow.getStockRecordIds());

		for (final CountingSubRowBucket subRowBucket : countingSubRows.values())
		{
			final MaterialCockpitRow subRow = subRowBucket.createIncludedRow(this);
			final boolean subRowIsEmpty = subRow.getAllIncludedStockRecordIds().isEmpty() && subRow.getAllIncludedCockpitRecordIds().isEmpty();
			if (!subRowIsEmpty)
			{
				mainRowBuilder.includedRow(subRow);
			}
		}

		for (final DimensionGroupSubRowBucket subRowBucket : dimensionGroupSubRows.values())
		{
			final MaterialCockpitRow subRow = subRowBucket.createIncludedRow(this);
			final boolean subRowIsEmpty = subRow.getAllIncludedStockRecordIds().isEmpty() && subRow.getAllIncludedCockpitRecordIds().isEmpty();
			if (!subRowIsEmpty)
			{
				mainRowBuilder.includedRow(subRow);
			}
		}

		return mainRowBuilder.build();

	}
}
