/*
 * #%L
 * de-metas-camel-sap-file-import
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

package de.metas.camel.externalsystems.sap.conversionRate;

import de.metas.camel.externalsystems.common.ProcessorHelper;
import de.metas.camel.externalsystems.sap.model.conversionRate.ConversionRateRow;
import de.metas.common.rest_api.v2.conversionRate.JsonCurrencyRateCreateRequest;
import lombok.NonNull;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import static de.metas.camel.externalsystems.sap.SAPConstants.ROUTE_PROPERTY_CONVERSION_RATE_ROUTE_CONTEXT;

public class ConversionRateUpsertProcessor implements Processor
{
	@Override
	public void process(final Exchange exchange) throws Exception
	{
		final ConversionRateRow conversionRateRow = exchange.getIn().getBody(ConversionRateRow.class);

		processConversionRateRow(exchange, conversionRateRow);
	}

	public static void processLastConversionRateGroup(@NonNull final Exchange exchange)
	{
		final ConversionRateContext conversionRateContext = ProcessorHelper.getPropertyOrThrowError(exchange, ROUTE_PROPERTY_CONVERSION_RATE_ROUTE_CONTEXT, ConversionRateContext.class);

		if (conversionRateContext.getJsonCurrencyRateCreateRequestBuilder() == null)
		{
			exchange.getIn().setBody(null);
			return;
		}

		exchange.getIn().setBody(conversionRateContext.getJsonCurrencyRateCreateRequestBuilder().build());
	}

	private void processConversionRateRow(@NonNull final Exchange exchange, @NonNull final ConversionRateRow conversionRateRow)
	{
		final ConversionRateContext conversionRateContext = ProcessorHelper.getPropertyOrThrowError(exchange, ROUTE_PROPERTY_CONVERSION_RATE_ROUTE_CONTEXT, ConversionRateContext.class);

		final JsonCurrencyRateCreateRequestBuilder jsonCurrencyRateCreateRequestBuilder = conversionRateContext.getJsonCurrencyRateCreateRequestBuilder();

		if (jsonCurrencyRateCreateRequestBuilder == null)
		{
			conversionRateContext.initConversionRateRequestBuilderFor(conversionRateRow);

			exchange.getIn().setBody(null);
			return;
		}

		final boolean added = jsonCurrencyRateCreateRequestBuilder.addConversionRateRow(conversionRateRow);

		if (added)
		{
			exchange.getIn().setBody(null);
			return;
		}

		final JsonCurrencyRateCreateRequest upsertCreditLimitRequest = conversionRateContext.getJsonCurrencyRateCreateRequestBuilder().build();
		exchange.getIn().setBody(upsertCreditLimitRequest);

		conversionRateContext.initConversionRateRequestBuilderFor(conversionRateRow);
	}
}
