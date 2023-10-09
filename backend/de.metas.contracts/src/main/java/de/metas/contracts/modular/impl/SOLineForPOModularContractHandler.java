/*
 * #%L
 * de.metas.contracts
 * %%
 * Copyright (C) 2023 metas GmbH
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

package de.metas.contracts.modular.impl;

import de.metas.bpartner.BPartnerId;
import de.metas.calendar.standard.CalendarId;
import de.metas.calendar.standard.YearId;
import de.metas.contracts.FlatrateTermId;
import de.metas.contracts.FlatrateTermRequest.ModularFlatrateTermQuery;
import de.metas.contracts.IFlatrateBL;
import de.metas.contracts.flatrate.TypeConditions;
import de.metas.contracts.modular.IModularContractTypeHandler;
import de.metas.contracts.modular.ModelAction;
import de.metas.contracts.modular.ModularContractHandlerType;
import de.metas.contracts.modular.ModularContractProvider;
import de.metas.contracts.modular.ModularContract_Constants;
import de.metas.contracts.modular.log.LogEntryContractType;
import de.metas.contracts.modular.log.ModularContractLogService;
import de.metas.lang.SOTrx;
import de.metas.order.IOrderBL;
import de.metas.order.OrderAndLineId;
import de.metas.order.OrderId;
import de.metas.product.ProductId;
import de.metas.util.Services;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.adempiere.exceptions.AdempiereException;
import org.adempiere.util.lang.impl.TableRecordReference;
import org.adempiere.warehouse.WarehouseId;
import org.adempiere.warehouse.api.IWarehouseBL;
import org.compiere.model.I_C_Order;
import org.compiere.model.I_C_OrderLine;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

import static de.metas.contracts.modular.ModularContractHandlerType.SO_LINE_FOR_PO_MODULAR;
import static de.metas.contracts.modular.ModularContract_Constants.MSG_ERROR_PROCESSED_LOGS_CANNOT_BE_RECOMPUTED;

@Component
@RequiredArgsConstructor
public class SOLineForPOModularContractHandler implements IModularContractTypeHandler<I_C_OrderLine>
{
	private final IOrderBL orderBL = Services.get(IOrderBL.class);
	private final IWarehouseBL warehouseBL = Services.get(IWarehouseBL.class);
	private final IFlatrateBL flatrateBL = Services.get(IFlatrateBL.class);

	@NonNull private final ModularContractLogService contractLogService;
	@NonNull private final ModularContractProvider contractProvider;

	@Override
	public @NonNull Class<I_C_OrderLine> getType()
	{
		return I_C_OrderLine.class;
	}

	@Override
	public boolean applies(final @NonNull I_C_OrderLine orderLine)
	{
		final I_C_Order order = orderBL.getById(OrderId.ofRepoId(orderLine.getC_Order_ID()));
		if (SOTrx.ofBoolean(order.isSOTrx()).isPurchase() || orderBL.isProFormaSO(order))
		{
			return false;
		}

		final CalendarId harvestingCalendarId = CalendarId.ofRepoIdOrNull(order.getC_Harvesting_Calendar_ID());
		if (harvestingCalendarId == null)
		{
			return false;
		}

		final YearId harvestingYearId = YearId.ofRepoIdOrNull(order.getHarvesting_Year_ID());
		if (harvestingYearId == null)
		{
			return false;
		}

		final BPartnerId warehousePartnerId = warehouseBL.getBPartnerId(WarehouseId.ofRepoId(order.getM_Warehouse_ID()));

		final ModularFlatrateTermQuery modularFlatrateTermQuery = ModularFlatrateTermQuery.builder()
				.bPartnerId(warehousePartnerId)
				.productId(ProductId.ofRepoId(orderLine.getM_Product_ID()))
				.yearId(harvestingYearId)
				.calendarId(harvestingCalendarId)
				.soTrx(SOTrx.PURCHASE)
				.typeConditions(TypeConditions.MODULAR_CONTRACT)
				.build();

		return flatrateBL.isModularContractInProgress(modularFlatrateTermQuery);
	}

	@Override
	public boolean applies(final @NonNull LogEntryContractType logEntryContractType)
	{
		return logEntryContractType.isModularContractType();
	}

	@Override
	public @NonNull Stream<FlatrateTermId> streamContractIds(@NonNull final I_C_OrderLine orderLine)
	{
		return contractProvider.streamPurchaseContractsForSalesOrderLine(OrderAndLineId.ofRepoIds(orderLine.getC_Order_ID(),
																								  orderLine.getC_OrderLine_ID()));
	}

	@Override
	public void validateAction(final @NonNull I_C_OrderLine model, final @NonNull ModelAction action)
	{
		switch (action)
		{
			case COMPLETED, VOIDED, REACTIVATED ->
			{
			}
			case RECREATE_LOGS -> contractLogService.throwErrorIfProcessedLogsExistForRecord(TableRecordReference.of(model),
																							 MSG_ERROR_PROCESSED_LOGS_CANNOT_BE_RECOMPUTED);
			default -> throw new AdempiereException(ModularContract_Constants.MSG_ERROR_DOC_ACTION_UNSUPPORTED);
		}
	}

	@Override
	public @NonNull ModularContractHandlerType getHandlerType()
	{
		return SO_LINE_FOR_PO_MODULAR;
	}
}
