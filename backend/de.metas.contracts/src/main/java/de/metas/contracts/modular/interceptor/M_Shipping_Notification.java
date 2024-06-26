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

package de.metas.contracts.modular.interceptor;

import de.metas.contracts.modular.ModelAction;
import de.metas.contracts.modular.ModularContractService;
import de.metas.contracts.modular.log.LogEntryContractType;
import de.metas.shippingnotification.ShippingNotificationId;
import de.metas.shippingnotification.ShippingNotificationService;
import de.metas.shippingnotification.model.I_M_Shipping_Notification;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.adempiere.ad.modelvalidator.annotations.DocValidate;
import org.adempiere.ad.modelvalidator.annotations.Interceptor;
import org.compiere.model.ModelValidator;
import org.springframework.stereotype.Component;

import static de.metas.contracts.modular.ModelAction.COMPLETED;
import static de.metas.contracts.modular.ModelAction.REVERSED;

@Component
@Interceptor(I_M_Shipping_Notification.class)
@RequiredArgsConstructor
public class M_Shipping_Notification
{
	@NonNull
	private final ModularContractService contractService;
	@NonNull
	private final ShippingNotificationService shippingNotificationService;

	@DocValidate(timings = ModelValidator.TIMING_AFTER_COMPLETE)
	public void afterComplete(@NonNull final I_M_Shipping_Notification shippingNotification)
	{
		invokeHandlerForEachLine(shippingNotification, COMPLETED);
	}

	@DocValidate(timings = ModelValidator.TIMING_AFTER_REVERSECORRECT)
	public void afterReverse(@NonNull final I_M_Shipping_Notification shippingNotification)
	{
		invokeHandlerForEachLine(shippingNotification, REVERSED);
	}

	private void invokeHandlerForEachLine(
			@NonNull final I_M_Shipping_Notification record,
			@NonNull final ModelAction modelAction)
	{
		final ShippingNotificationId notificationId = ShippingNotificationId.ofRepoId(record.getM_Shipping_Notification_ID());
		shippingNotificationService.getLines(notificationId)
				.forEach(line -> contractService.invokeWithModel(line, modelAction, LogEntryContractType.MODULAR_CONTRACT));
	}
}
