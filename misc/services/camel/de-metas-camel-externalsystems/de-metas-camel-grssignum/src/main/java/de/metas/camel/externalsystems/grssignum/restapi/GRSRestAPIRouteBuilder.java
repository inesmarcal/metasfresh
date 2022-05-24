/*
 * #%L
 * de-metas-camel-externalsystems
 * %%
 * Copyright (C) 2021 metas GmbH
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

package de.metas.camel.externalsystems.grssignum.restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.metas.camel.externalsystems.common.ErrorBuilderHelper;
import de.metas.camel.externalsystems.common.RestServiceAuthority;
import de.metas.camel.externalsystems.common.RestServiceRoutes;
import de.metas.camel.externalsystems.common.auth.JsonAuthenticateRequest;
import de.metas.camel.externalsystems.common.auth.JsonExpireTokenResponse;
import de.metas.camel.externalsystems.common.auth.TokenCredentials;
import de.metas.common.externalsystem.ExternalSystemConstants;
import de.metas.common.externalsystem.JsonExternalSystemRequest;
import de.metas.common.rest_api.v2.JsonError;
import lombok.NonNull;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.RouteController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static de.metas.camel.externalsystems.common.ExternalSystemCamelConstants.ERROR_WRITE_TO_ADISSUE;
import static de.metas.camel.externalsystems.common.ExternalSystemCamelConstants.HEADER_PINSTANCE_ID;
import static de.metas.camel.externalsystems.common.ExternalSystemCamelConstants.MF_ERROR_ROUTE_ID;
import static de.metas.camel.externalsystems.common.ExternalSystemCamelConstants.REST_API_AUTHENTICATE_TOKEN;
import static de.metas.camel.externalsystems.common.ExternalSystemCamelConstants.REST_API_EXPIRE_TOKEN;
import static de.metas.camel.externalsystems.common.RouteBuilderHelper.setupJacksonDataFormatFor;
import static de.metas.camel.externalsystems.grssignum.GRSSignumConstants.JSON_PROPERTY_FLAG;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;
import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.direct;

@Component
public class GRSRestAPIRouteBuilder extends RouteBuilder
{
	public static final String REST_API_ROUTE_ID = "GRSSignum_RestAPI";

	public static final String ENABLE_RESOURCE_ROUTE_ID = "GRSSignum-enableRestAPI";
	public static final String DISABLE_RESOURCE_ROUTE_ID = "GRSSignum-disableRestAPI";

	public static final String ENABLE_RESOURCE_ROUTE_PROCESSOR_ID = "GRS-enableRestAPIProcessor";
	public static final String DISABLE_RESOURCE_ROUTE_PROCESSOR_ID = "GRS-disableRestAPIProcessor";

	public static final String ENABLE_RESOURCE_ATTACH_AUTHENTICATE_REQ_PROCESSOR_ID = "GRS-ER-AttachAuthenticateReqProcessorId";
	public static final String DISABLE_RESOURCE_ATTACH_AUTHENTICATE_REQ_PROCESSOR_ID = "GRS-DR-AttachAuthenticateReqProcessorId";

	private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

	@NonNull
	private final ProducerTemplate producerTemplate;

	public GRSRestAPIRouteBuilder(
			final @NonNull ProducerTemplate producerTemplate)
	{
		this.producerTemplate = producerTemplate;
	}

	@Override
	public void configure()
	{
		errorHandler(defaultErrorHandler());
		onException(Exception.class)
				.to(direct(MF_ERROR_ROUTE_ID));

		from(direct(ENABLE_RESOURCE_ROUTE_ID))
				.routeId(ENABLE_RESOURCE_ROUTE_ID)
				.log("Route invoked!")
				.process(this::attachAuthenticateRequest).id(ENABLE_RESOURCE_ATTACH_AUTHENTICATE_REQ_PROCESSOR_ID)
				.to(direct(REST_API_AUTHENTICATE_TOKEN))
				.process(this::enableRestAPIProcessor).id(ENABLE_RESOURCE_ROUTE_PROCESSOR_ID)
				.end();

		from(direct(DISABLE_RESOURCE_ROUTE_ID))
				.routeId(DISABLE_RESOURCE_ROUTE_ID)
				.log("Route invoked!")
				.process(this::attachAuthenticateRequest).id(DISABLE_RESOURCE_ATTACH_AUTHENTICATE_REQ_PROCESSOR_ID)
				.to(direct(REST_API_EXPIRE_TOKEN))
				.process(this::disableRestAPIProcessor).id(DISABLE_RESOURCE_ROUTE_PROCESSOR_ID)
				.end();

		rest().path(RestServiceRoutes.GRS.getPath())
				.post()
				.route()
				.routeId(REST_API_ROUTE_ID)
				.autoStartup(false)
				.doTry()
					.process(this::restAPIProcessor)
				    .process(this::prepareSuccessResponse)
				.doCatch(JsonProcessingException.class)
			   		.to(direct(ERROR_WRITE_TO_ADISSUE))
					.process(this::prepareErrorResponse)
					.marshal(setupJacksonDataFormatFor(getContext(), JsonError.class))
				.doCatch(Exception.class)
				    .to(direct(MF_ERROR_ROUTE_ID))
				    .process(this::prepareErrorResponse)
				    .marshal(setupJacksonDataFormatFor(getContext(), JsonError.class))
				.endDoTry()
				.end();
	}

	private void enableRestAPIProcessor(@NonNull final Exchange exchange) throws Exception
	{
		final RouteController routeController = getContext().getRouteController();

		routeController.resumeRoute(REST_API_ROUTE_ID);
	}

	private void attachAuthenticateRequest(@NonNull final Exchange exchange)
	{
		final JsonExternalSystemRequest request = exchange.getIn().getBody(JsonExternalSystemRequest.class);

		final JsonAuthenticateRequest jsonAuthenticateRequest = getJsonAuthenticateRequest(request);

		exchange.getIn().setBody(jsonAuthenticateRequest);
	}

	@NonNull
	private JsonAuthenticateRequest getJsonAuthenticateRequest(@NonNull final JsonExternalSystemRequest request)
	{
		if (request.getAdPInstanceId() == null)
		{
			throw new RuntimeCamelException("Missing pInstance identifier!");
		}

		final String authKey = request.getParameters().get(ExternalSystemConstants.PARAM_CAMEL_HTTP_RESOURCE_AUTH_KEY);

		if (authKey == null)
		{
			throw new RuntimeCamelException("Missing authKey from request!");
		}

		return JsonAuthenticateRequest.builder()
				.grantedAuthority(RestServiceAuthority.GRS.getValue())
				.authKey(authKey)
				.pInstance(request.getAdPInstanceId())
				.orgCode(request.getOrgCode())
				.build();
	}

	private void restAPIProcessor(@NonNull final Exchange exchange) throws JsonProcessingException
	{
		final TokenCredentials credentials = (TokenCredentials)SecurityContextHolder.getContext().getAuthentication().getCredentials();

		if (credentials == null)
		{
			throw new RuntimeCamelException("Missing credentials!");
		}

		exchange.getIn().setHeader(HEADER_PINSTANCE_ID, credentials.getPInstance().getValue());

		final String requestBody = exchange.getIn().getBody(String.class);

		final JsonNode rootJsonNode = objectMapper.readValue(requestBody, JsonNode.class);

		final Integer flag = objectMapper.treeToValue(rootJsonNode.get(JSON_PROPERTY_FLAG), Integer.class);

		final Endpoint endpoint = Endpoint.ofFlag(flag);

		exchange.getIn().setBody(requestBody);

		producerTemplate.send("direct:" + endpoint.getTargetRoute(), exchange);
	}

	private void disableRestAPIProcessor(@NonNull final Exchange exchange) throws Exception
	{
		final JsonExpireTokenResponse response = exchange.getIn().getBody(JsonExpireTokenResponse.class);

		if (response != null && response.getNumberOfAuthenticatedTokens() == 0)
		{
			getContext().getRouteController().suspendRoute(REST_API_ROUTE_ID);
		}
	}

	private void prepareErrorResponse(@NonNull final Exchange exchange)
	{
		final Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

		if (exception == null)
		{
			exchange.getIn().setBody(null);
			exchange.getIn().setHeader(HTTP_RESPONSE_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value());
			return;
		}

		final int httpCode = exception instanceof JsonProcessingException
				? HttpStatus.BAD_REQUEST.value()
				: HttpStatus.INTERNAL_SERVER_ERROR.value();

		final JsonError jsonError = JsonError.ofSingleItem(ErrorBuilderHelper.buildJsonErrorItem(exchange));

		exchange.getIn().setBody(jsonError);
		exchange.getIn().setHeader(HTTP_RESPONSE_CODE, httpCode);
	}

	private void prepareSuccessResponse(@NonNull final Exchange exchange)
	{
		exchange.getIn().setBody(null);
		exchange.getIn().setHeader(HTTP_RESPONSE_CODE, HttpStatus.OK.value()
		);
	}
}