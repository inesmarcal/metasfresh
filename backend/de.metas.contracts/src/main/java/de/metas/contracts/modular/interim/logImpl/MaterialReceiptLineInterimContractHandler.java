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

package de.metas.contracts.modular.interim.logImpl;

import de.metas.contracts.FlatrateTermId;
import de.metas.contracts.IFlatrateBL;
import de.metas.contracts.modular.IModularContractTypeHandler;
import de.metas.contracts.modular.ModularContractService;
import de.metas.contracts.modular.ModularContract_Constants;
import de.metas.contracts.modular.impl.MaterialReceiptLineHandlerHelper;
import de.metas.contracts.modular.log.LogEntryContractType;
import de.metas.contracts.modular.log.LogEntryCreateRequest;
import de.metas.contracts.modular.log.LogEntryReverseRequest;
import de.metas.inout.IInOutDAO;
import de.metas.inout.InOutId;
import de.metas.lang.SOTrx;
import de.metas.util.Services;
import lombok.NonNull;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.I_M_InOut;
import org.compiere.model.I_M_InOutLine;
import org.compiere.util.TimeUtil;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class MaterialReceiptLineInterimContractHandler implements IModularContractTypeHandler<I_M_InOutLine>
{
	private final MaterialReceiptLineHandlerHelper materialReceiptLineHandlerHelper;

	private final IInOutDAO inoutDao = Services.get(IInOutDAO.class);
	private final IFlatrateBL flatrateBL = Services.get(IFlatrateBL.class);

	public MaterialReceiptLineInterimContractHandler(@NonNull final MaterialReceiptLineHandlerHelper materialReceiptLineHandlerHelper)
	{
		this.materialReceiptLineHandlerHelper = materialReceiptLineHandlerHelper;
	}

	@Override
	@NonNull
	public Class<I_M_InOutLine> getType()
	{
		return I_M_InOutLine.class;
	}

	@Override
	public boolean applies(final @NonNull I_M_InOutLine inOutLineRecord)
	{
		final I_M_InOut inOutRecord = inoutDao.getById(InOutId.ofRepoId(inOutLineRecord.getM_InOut_ID()));
		return SOTrx.ofBoolean(inOutRecord.isSOTrx()).isPurchase();
	}

	@Override
	public boolean applies(@NonNull final LogEntryContractType logEntryContractType)
	{
		return logEntryContractType.isInterimContractType();
	}

	@Override
	public @NonNull Optional<LogEntryCreateRequest> createLogEntryCreateRequest(final @NonNull I_M_InOutLine inOutLineRecord, final @NonNull FlatrateTermId flatrateTermId)
	{
		return materialReceiptLineHandlerHelper.createLogEntryCreateRequest(inOutLineRecord, flatrateTermId, LogEntryContractType.INTERIM);
	}

	@Override
	public @NonNull Optional<LogEntryReverseRequest> createLogEntryReverseRequest(final @NonNull I_M_InOutLine inOutLineRecord, final @NonNull FlatrateTermId flatrateTermId)
	{
		return materialReceiptLineHandlerHelper.createLogEntryReverseRequest(inOutLineRecord, flatrateTermId, LogEntryContractType.INTERIM);
	}

	@Override
	public @NonNull Stream<FlatrateTermId> streamContractIds(@NonNull final I_M_InOutLine inOutLineRecord)
	{
		final I_M_InOut inOutRecord = inoutDao.getById(InOutId.ofRepoId(inOutLineRecord.getM_InOut_ID()));
		final FlatrateTermId modularFlatrateTermId = FlatrateTermId.ofRepoIdOrNull(inOutLineRecord.getC_Flatrate_Term_ID());
		if (inOutRecord.isSOTrx() || modularFlatrateTermId == null || inOutLineRecord.getMovementQty().signum() < 0)
		{
			return Stream.empty();
		}

		return Stream.ofNullable(flatrateBL.getInterimContractIdByModularContractIdAndDate(modularFlatrateTermId, Objects.requireNonNull(TimeUtil.asInstant(inOutRecord.getMovementDate()))));
	}

	@Override
	public void validateDocAction(final @NonNull I_M_InOutLine model, final ModularContractService.@NonNull ModelAction action)
	{
		if (action == ModularContractService.ModelAction.VOIDED)
		{
			throw new AdempiereException(ModularContract_Constants.MSG_ERROR_DOC_ACTION_NOT_ALLOWED);
		}
	}
}
