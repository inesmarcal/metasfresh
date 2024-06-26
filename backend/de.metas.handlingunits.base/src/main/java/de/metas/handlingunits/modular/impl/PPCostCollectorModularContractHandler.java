/*
 * #%L
 * de.metas.handlingunits.base
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

package de.metas.handlingunits.modular.impl;

import de.metas.contracts.FlatrateTermId;
import de.metas.contracts.modular.IModularContractTypeHandler;
import de.metas.contracts.modular.ModelAction;
import de.metas.contracts.modular.ModularContractHandlerType;
import de.metas.contracts.modular.ModularContract_Constants;
import de.metas.contracts.modular.log.LogEntryContractType;
import de.metas.contracts.modular.log.ModularContractLogService;
import de.metas.util.Services;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.adempiere.exceptions.AdempiereException;
import org.adempiere.util.lang.impl.TableRecordReference;
import org.eevolution.api.IPPOrderBL;
import org.eevolution.api.PPOrderId;
import org.eevolution.model.I_PP_Cost_Collector;
import org.eevolution.model.I_PP_Order;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

import static de.metas.contracts.modular.ModularContractHandlerType.PPCOSTCOLLECTOR_MODULAR;
import static de.metas.contracts.modular.ModularContract_Constants.MSG_ERROR_PROCESSED_LOGS_CANNOT_BE_RECOMPUTED;

@Component
@RequiredArgsConstructor
public class PPCostCollectorModularContractHandler implements IModularContractTypeHandler<I_PP_Cost_Collector>
{
	private final IPPOrderBL ppOrderBL = Services.get(IPPOrderBL.class);

	@NonNull
	private final ModularContractLogService contractLogService;

	@Override
	@NonNull
	public Class<I_PP_Cost_Collector> getType()
	{
		return I_PP_Cost_Collector.class;
	}

	@Override
	public boolean applies(@NonNull final I_PP_Cost_Collector ppCostCollector)
	{
		return ppOrderBL.isModularOrder(PPOrderId.ofRepoId(ppCostCollector.getPP_Order_ID()));
	}

	@Override
	public boolean applies(final @NonNull LogEntryContractType logEntryContractType)
	{
		return logEntryContractType.isModularContractType();
	}

	@Override
	@NonNull
	public Stream<FlatrateTermId> streamContractIds(@NonNull final I_PP_Cost_Collector ppCostCollector)
	{
		return Optional.of(ppCostCollector.getPP_Order_ID())
				.map(PPOrderId::ofRepoId)
				.map(ppOrderBL::getById)
				.map(I_PP_Order::getModular_Flatrate_Term_ID)
				.map(FlatrateTermId::ofRepoIdOrNull)
				.stream();
	}

	@Override
	public void validateAction(@NonNull final I_PP_Cost_Collector ppCostCollector, @NonNull final ModelAction action)
	{
		switch (action)
		{
			case COMPLETED ->
			{
			}
			case RECREATE_LOGS ->
			{
				final TableRecordReference ppOrderReference = TableRecordReference.of(I_PP_Order.Table_Name,
																					  PPOrderId.ofRepoId(ppCostCollector.getPP_Order_ID()));

				contractLogService.throwErrorIfProcessedLogsExistForRecord(ppOrderReference,
																		   MSG_ERROR_PROCESSED_LOGS_CANNOT_BE_RECOMPUTED);
			}
			default -> throw new AdempiereException(ModularContract_Constants.MSG_ERROR_DOC_ACTION_UNSUPPORTED);
		}
	}

	@Override
	public @NonNull ModularContractHandlerType getHandlerType()
	{
		return PPCOSTCOLLECTOR_MODULAR;
	}
}
