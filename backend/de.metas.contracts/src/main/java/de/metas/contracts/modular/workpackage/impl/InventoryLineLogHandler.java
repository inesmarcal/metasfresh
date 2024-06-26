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

package de.metas.contracts.modular.workpackage.impl;

import de.metas.bpartner.BPartnerId;
import de.metas.contracts.IFlatrateDAO;
import de.metas.contracts.model.I_C_Flatrate_Term;
import de.metas.contracts.modular.IModularContractTypeHandler;
import de.metas.contracts.modular.ModularContract_Constants;
import de.metas.contracts.modular.impl.InventoryLineModularContractHandler;
import de.metas.contracts.modular.invgroup.InvoicingGroupId;
import de.metas.contracts.modular.invgroup.interceptor.ModCntrInvoicingGroupRepository;
import de.metas.contracts.modular.log.LogEntryContractType;
import de.metas.contracts.modular.log.LogEntryCreateRequest;
import de.metas.contracts.modular.log.LogEntryDocumentType;
import de.metas.contracts.modular.log.LogEntryReverseRequest;
import de.metas.contracts.modular.workpackage.IModularContractLogHandler;
import de.metas.document.engine.DocStatus;
import de.metas.i18n.AdMessageKey;
import de.metas.i18n.BooleanWithReason;
import de.metas.i18n.ExplainedOptional;
import de.metas.i18n.IMsgBL;
import de.metas.inventory.IInventoryBL;
import de.metas.inventory.InventoryId;
import de.metas.lang.SOTrx;
import de.metas.organization.IOrgDAO;
import de.metas.organization.LocalDateAndOrgId;
import de.metas.organization.OrgId;
import de.metas.product.ProductId;
import de.metas.quantity.Quantity;
import de.metas.util.Services;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.adempiere.exceptions.AdempiereException;
import org.adempiere.util.lang.impl.TableRecordReference;
import org.adempiere.warehouse.WarehouseId;
import org.adempiere.warehouse.api.IWarehouseBL;
import org.compiere.model.I_M_Inventory;
import org.compiere.model.I_M_InventoryLine;
import org.compiere.model.I_M_Warehouse;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class InventoryLineLogHandler implements IModularContractLogHandler<I_M_InventoryLine>
{
	private static final AdMessageKey MSG_DESCRIPTION = AdMessageKey.of("de.metas.contracts.modular.impl.InventoryLineModularContractHandler.Description");
	private final IOrgDAO orgDAO = Services.get(IOrgDAO.class);
	private final IFlatrateDAO flatrateDAO = Services.get(IFlatrateDAO.class);
	private final IInventoryBL inventoryBL = Services.get(IInventoryBL.class);
	private final IWarehouseBL warehouseBL = Services.get(IWarehouseBL.class);
	private final IMsgBL msgBL = Services.get(IMsgBL.class);

	@NonNull
	private final InventoryLineModularContractHandler contractHandler;
	@NonNull
	private final ModCntrInvoicingGroupRepository modCntrInvoicingGroupRepository;

	@Override
	public LogAction getLogAction(@NonNull final IModularContractLogHandler.HandleLogsRequest<I_M_InventoryLine> request)
	{
		return switch (request.getModelAction())
				{
					case COMPLETED -> LogAction.CREATE;
					case REVERSED -> LogAction.REVERSE;
					case RECREATE_LOGS -> LogAction.RECOMPUTE;
					default -> throw new AdempiereException(ModularContract_Constants.MSG_ERROR_DOC_ACTION_UNSUPPORTED);
				};
	}

	@Override
	public BooleanWithReason doesRecordStateRequireLogCreation(@NonNull final I_M_InventoryLine model)
	{
		final DocStatus inventoryDocStatus = inventoryBL.getDocStatus(InventoryId.ofRepoId(model.getM_Inventory_ID()));

		if (!inventoryDocStatus.isCompleted())
		{
			return BooleanWithReason.falseBecause("The M_Inventory.DocStatus is " + inventoryDocStatus);
		}

		return BooleanWithReason.TRUE;
	}

	@Override
	@NonNull
	public ExplainedOptional<LogEntryCreateRequest> createLogEntryCreateRequest(
			@NonNull final CreateLogRequest<I_M_InventoryLine> createLogRequest)
	{
		final I_M_InventoryLine inventoryLine = createLogRequest.getHandleLogsRequest().getModel();

		final I_M_Inventory inventory = inventoryBL.getById(InventoryId.ofRepoId(inventoryLine.getM_Inventory_ID()));

		if (inventoryBL.isReversal(inventory))
		{
			return ExplainedOptional.emptyBecause(MessageFormat.format("InventoryId: {0} | The enqueued inventory is a reversal, the log entries are created only for the reversed inventory",
																	   inventory.getM_Inventory_ID()));
		}

		final Quantity quantity = inventoryBL.getMovementQty(inventoryLine);

		final String description = msgBL.getBaseLanguageMsg(MSG_DESCRIPTION, quantity.abs().toString());

		final I_C_Flatrate_Term modularContractRecord = flatrateDAO.getById(createLogRequest.getContractId());

		final ProductId productId = ProductId.ofRepoId(inventoryLine.getM_Product_ID());

		final LocalDateAndOrgId transactionDate = LocalDateAndOrgId.ofTimestamp(inventory.getMovementDate(),
																				OrgId.ofRepoId(inventoryLine.getAD_Org_ID()),
																				orgDAO::getTimeZone);

		final InvoicingGroupId invoicingGroupId = modCntrInvoicingGroupRepository.getInvoicingGroupIdFor(productId, transactionDate.toInstant(orgDAO::getTimeZone))
				.orElse(null);

		final WarehouseId warehouseId = WarehouseId.ofRepoIdOrNull(inventory.getM_Warehouse_ID());
		final BPartnerId collectionPointBPartnerId = Optional.ofNullable(warehouseId)
				.map(warehouseBL::getById)
				.map(I_M_Warehouse::getC_BPartner_ID)
				.map(BPartnerId::ofRepoId)
				.orElse(null);

		return ExplainedOptional.of(LogEntryCreateRequest.builder()
											.contractId(createLogRequest.getContractId())
											.productId(productId)
											.referencedRecord(TableRecordReference.of(I_M_InventoryLine.Table_Name, inventoryLine.getM_InventoryLine_ID()))
											.invoicingBPartnerId(BPartnerId.ofRepoIdOrNull(modularContractRecord.getBill_BPartner_ID()))
											.collectionPointBPartnerId(collectionPointBPartnerId)
											.warehouseId(warehouseId)
											.documentType(LogEntryDocumentType.INVENTORY)
											.contractType(LogEntryContractType.MODULAR_CONTRACT)
											.soTrx(SOTrx.PURCHASE)
											.quantity(quantity)
											.transactionDate(transactionDate)
											.year(createLogRequest.getModularContractSettings().getYearAndCalendarId().yearId())
											.description(description)
											.modularContractTypeId(createLogRequest.getTypeId())
											.configId(createLogRequest.getConfigId())
											.invoicingGroupId(invoicingGroupId)
											.build());
	}

	@Override
	public @NonNull ExplainedOptional<LogEntryReverseRequest> createLogEntryReverseRequest(@NonNull final IModularContractLogHandler.HandleLogsRequest<I_M_InventoryLine> handleLogsRequest)
	{
		final I_M_Inventory inventory = inventoryBL.getById(InventoryId.ofRepoId(handleLogsRequest.getModel().getM_Inventory_ID()));

		if (inventoryBL.isReversal(inventory))
		{
			return ExplainedOptional.emptyBecause(MessageFormat.format("InventoryId: {0} | The enqueued inventory is a reversal, the log entries are created only for the reversed inventory",
																	   inventory.getM_Inventory_ID()));
		}

		return ExplainedOptional.of(LogEntryReverseRequest.builder()
											.referencedModel(TableRecordReference.of(handleLogsRequest.getModel()))
											.flatrateTermId(handleLogsRequest.getContractId())
											.logEntryContractType(LogEntryContractType.MODULAR_CONTRACT)
											.build());
	}

	@Override
	public @NonNull IModularContractTypeHandler<I_M_InventoryLine> getModularContractTypeHandler()
	{
		return contractHandler;
	}
}
