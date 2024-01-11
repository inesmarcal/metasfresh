package org.eevolution.api.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import de.metas.document.engine.DocStatus;
import de.metas.manufacturing.order.exportaudit.APIExportStatus;
import de.metas.order.OrderLineId;
import de.metas.product.ResourceId;
import de.metas.util.Services;
import lombok.NonNull;
import org.adempiere.ad.dao.IQueryBL;
import org.adempiere.ad.dao.IQueryBuilder;
import org.adempiere.ad.dao.IQueryFilter;
import org.adempiere.model.InterfaceWrapperHelper;
import org.adempiere.warehouse.WarehouseId;
import org.compiere.model.IQuery;
import org.compiere.util.TimeUtil;
import org.eevolution.api.IPPOrderDAO;
import org.eevolution.api.ManufacturingOrderQuery;
import org.eevolution.api.PPOrderId;
import org.eevolution.api.ProductBOMId;
import org.eevolution.api.ProductBOMVersionsId;
import org.eevolution.model.I_PP_Cost_Collector;
import org.eevolution.model.I_PP_Order;
import org.eevolution.model.I_PP_OrderCandidate_PP_Order;
import org.eevolution.model.X_PP_Order;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.adempiere.ad.dao.impl.CompareQueryFilter.Operator.LESS_OR_EQUAL;
import static org.adempiere.model.InterfaceWrapperHelper.loadByRepoIdAwares;
import static org.adempiere.model.InterfaceWrapperHelper.saveRecord;
import static org.compiere.util.TimeUtil.asTimestamp;

public class PPOrderDAO implements IPPOrderDAO
{
	private final IQueryBL queryBL = Services.get(IQueryBL.class);

	@Override
	public I_PP_Order getById(@NonNull final PPOrderId ppOrderId)
	{
		return getById(ppOrderId, I_PP_Order.class);
	}

	@Override
	public <T extends I_PP_Order> T getById(@NonNull final PPOrderId ppOrderId, @NonNull final Class<T> type)
	{
		return InterfaceWrapperHelper.load(ppOrderId, type);
	}

	@Override
	public <T extends I_PP_Order> List<T> getByIds(@NonNull final Set<PPOrderId> orderIds, @NonNull final Class<T> type)
	{
		return loadByRepoIdAwares(orderIds, type);
	}

	@Override
	public List<I_PP_Order> retrieveReleasedManufacturingOrdersForWarehouse(final WarehouseId warehouseId)
	{
		final IQueryBuilder<I_PP_Order> queryBuilder = toSqlQueryBuilder(ManufacturingOrderQuery.builder()
				.warehouseId(warehouseId)
				.onlyCompleted(true)
				.build());

		return queryBuilder
				.orderBy(I_PP_Order.COLUMN_DocumentNo)
				.create()
				.list(I_PP_Order.class);
	}

	@Override
	public List<I_PP_Order> retrieveManufacturingOrders(@NonNull final ManufacturingOrderQuery query)
	{
		return toSqlQueryBuilder(query).create().list();
	}

	@Override
	public Stream<I_PP_Order> streamManufacturingOrders(@NonNull final ManufacturingOrderQuery query)
	{
		return toSqlQueryBuilder(query).create().iterateAndStream();
	}

	private IQueryBuilder<I_PP_Order> toSqlQueryBuilder(final ManufacturingOrderQuery query)
	{
		final IQueryBuilder<I_PP_Order> queryBuilder = queryBL.createQueryBuilder(I_PP_Order.class)
				.addOnlyActiveRecordsFilter();

		// Plant
		if (query.getPlantId() != null)
		{
			queryBuilder.addEqualsFilter(I_PP_Order.COLUMN_S_Resource_ID, query.getPlantId());
		}

		// Warehouse
		if (query.getWarehouseId() != null)
		{
			queryBuilder.addEqualsFilter(I_PP_Order.COLUMNNAME_M_Warehouse_ID, query.getWarehouseId());
		}

		// Responsible
		query.getResponsibleId().appendFilter(queryBuilder, I_PP_Order.COLUMNNAME_AD_User_Responsible_ID);

		// Only Releases Manufacturing orders
		if (query.isOnlyCompleted())
		{
			queryBuilder.addEqualsFilter(I_PP_Order.COLUMN_Processed, true);
			queryBuilder.addEqualsFilter(I_PP_Order.COLUMN_DocStatus, DocStatus.Completed);
		}

		// Export Status
		if (query.getExportStatus() != null)
		{
			queryBuilder.addEqualsFilter(I_PP_Order.COLUMNNAME_ExportStatus, query.getExportStatus().getCode());
		}
		if (query.getCanBeExportedFrom() != null)
		{
			queryBuilder.addCompareFilter(I_PP_Order.COLUMN_CanBeExportedFrom, LESS_OR_EQUAL, asTimestamp(query.getCanBeExportedFrom()));
		}

		//
		// Order BYs
		queryBuilder.orderBy(I_PP_Order.COLUMN_DocumentNo);
		queryBuilder.orderBy(I_PP_Order.COLUMNNAME_PP_Order_ID);

		// Limit
		if (query.getLimit().isLimited())
		{
			queryBuilder.setLimit(query.getLimit());
		}

		//
		return queryBuilder;
	}

	@Override
	public PPOrderId retrievePPOrderIdByOrderLineId(@NonNull final OrderLineId orderLineId)
	{
		return queryBL
				.createQueryBuilder(I_PP_Order.class)
				.addEqualsFilter(I_PP_Order.COLUMN_C_OrderLine_ID, orderLineId)
				.addOnlyActiveRecordsFilter()
				.create()
				.firstIdOnly(PPOrderId::ofRepoIdOrNull);
	}

	@Override
	public Stream<I_PP_Order> streamOpenPPOrderIdsOrderedByDatePromised(@Nullable final ResourceId plantId)
	{
		final IQueryBuilder<I_PP_Order> queryBuilder = queryBL
				.createQueryBuilder(I_PP_Order.class)
				.addOnlyActiveRecordsFilter()
				.addInArrayFilter(I_PP_Order.COLUMN_DocStatus, X_PP_Order.DOCSTATUS_InProgress, X_PP_Order.DOCSTATUS_Completed)
				.orderBy(I_PP_Order.COLUMNNAME_DatePromised);

		if (plantId != null)
		{
			queryBuilder.addEqualsFilter(I_PP_Order.COLUMN_S_Resource_ID, plantId);
		}

		return queryBuilder.create().iterateAndStream();
	}

	@Override
	public void changeOrderScheduling(
			@NonNull final PPOrderId orderId,
			@NonNull final Instant scheduledStartDate,
			@NonNull final Instant scheduledFinishDate)
	{
		final I_PP_Order order = getById(orderId);
		order.setDateStartSchedule(TimeUtil.asTimestamp(scheduledStartDate));
		order.setDateFinishSchedule(TimeUtil.asTimestamp(scheduledFinishDate));
		save(order);
	}

	@Override
	public void save(@NonNull final I_PP_Order order)
	{
		saveRecord(order);
	}

	@Override
	public void saveAll(@NonNull final Collection<I_PP_Order> orders)
	{
		orders.forEach(this::save);
	}

	@Override
	public void exportStatusMassUpdate(
			@NonNull final Map<PPOrderId, APIExportStatus> exportStatuses)
	{
		if (exportStatuses.isEmpty())
		{
			return;
		}

		final HashMultimap<APIExportStatus, PPOrderId> orderIdsByExportStatus = HashMultimap.create();
		for (Map.Entry<PPOrderId, APIExportStatus> entry : exportStatuses.entrySet())
		{
			orderIdsByExportStatus.put(entry.getValue(), entry.getKey());
		}

		for (final APIExportStatus exportStatus : orderIdsByExportStatus.keySet())
		{
			final Set<PPOrderId> orderIds = orderIdsByExportStatus.get(exportStatus);

			queryBL.createQueryBuilder(I_PP_Order.class)
					.addInArrayFilter(I_PP_Order.COLUMNNAME_PP_Order_ID, orderIds)
					.create()
					//
					.updateDirectly()
					.addSetColumnValue(I_PP_Order.COLUMNNAME_ExportStatus, exportStatus.getCode())
					.execute();
		}
	}

	@Override
	public IQueryBuilder<I_PP_Order> createQueryForPPOrderSelection(final IQueryFilter<I_PP_Order> userSelectionFilter)
	{
		return queryBL
				.createQueryBuilder(I_PP_Order.class)
				.filter(userSelectionFilter)
				.addNotEqualsFilter(I_PP_Order.COLUMNNAME_DocStatus, X_PP_Order.DOCSTATUS_Closed)
				.addOnlyActiveRecordsFilter()
				.addOnlyContextClient();
	}

	@NonNull
	@Override
	public ImmutableList<I_PP_OrderCandidate_PP_Order> getPPOrderAllocations(@NonNull final PPOrderId ppOrderId)
	{
		return queryBL
				.createQueryBuilder(I_PP_OrderCandidate_PP_Order.class)
				.addOnlyActiveRecordsFilter()
				.addEqualsFilter(I_PP_OrderCandidate_PP_Order.COLUMNNAME_PP_Order_ID, ppOrderId.getRepoId())
				.create()
				.listImmutable(I_PP_OrderCandidate_PP_Order.class);
	}

	@NonNull
	public ImmutableList<I_PP_Order> getByProductBOMId(@NonNull final ProductBOMId productBOMId)
	{
		return queryBL
				.createQueryBuilder(I_PP_Order.class)
				.addOnlyActiveRecordsFilter()
				.addEqualsFilter(I_PP_Order.COLUMNNAME_PP_Product_BOM_ID, productBOMId.getRepoId())
				.create()
				.listImmutable(I_PP_Order.class);
	}

	@NonNull
	public Stream<I_PP_Order> streamDraftedPPOrdersFor(@NonNull final ProductBOMVersionsId bomVersionsId)
	{
		final ManufacturingOrderQuery query = ManufacturingOrderQuery.builder()
				.bomVersionsId(bomVersionsId)
				.onlyDrafted(true)
				.build();

		final IQueryBuilder<I_PP_Order> ppOrderQueryBuilder = toSqlQueryBuilder(query);

		//dev-note: make sure there are no material transactions already created
		final IQuery<I_PP_Cost_Collector> withMaterialTransactionsQuery = queryBL.createQueryBuilder(I_PP_Cost_Collector.class)
				.addInSubQueryFilter(I_PP_Cost_Collector.COLUMNNAME_PP_Order_ID, I_PP_Order.COLUMNNAME_PP_Order_ID, ppOrderQueryBuilder.create())
				.create();

		return ppOrderQueryBuilder
				.addNotInSubQueryFilter(I_PP_Order.COLUMNNAME_PP_Order_ID, I_PP_Cost_Collector.COLUMNNAME_PP_Order_ID, withMaterialTransactionsQuery)
				.create()
				.iterateAndStream();
	}
}
