/*
 * #%L
 * de.metas.shipper.gateway.dhl
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

package de.metas.shipper.gateway.dhl.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import javax.annotation.Nullable;
import java.util.List;

@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = JsonDhlItemResponse.JsonDhlItemResponseBuilder.class)
public class JsonDhlItemResponse
{
	@JsonProperty("shipmentNo")
	@Nullable String shipmentNo;
	@JsonProperty("sstatus")
	@NonNull JsonDhlStatusResponse sstatus;
	@JsonProperty("shipmentRefNo")
	@Nullable String shipmentRefNo;
	@JsonProperty("label")
	@Nullable JsonDhlLabel label;
	@JsonProperty("validationMessages")
	@Nullable List<JsonDhlValidationMessage> validationMessages;

	public JsonDhlItemResponse withNoLabelData()
	{
		if (label == null)
		{
			return this;
		}
		return this.toBuilder()
				.label(label.withNoLabelData())
				.build();
	}
}
