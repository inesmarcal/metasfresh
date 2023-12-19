package de.metas.manufacturing.workflows_api.activity_handlers.issue.json;

import de.metas.workflow.rest_api.controller.v2.json.JsonOpts;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.adempiere.ad.service.IADReferenceDAO;

@Value
@Builder
@Jacksonized
public class JsonRejectReason
{
	@NonNull String key;
	@NonNull String caption;

	public static JsonRejectReason of(@NonNull final ADRefListItem item, @NonNull final JsonOpts jsonOpts)
	{
		return builder()
				.key(item.getValue())
				.caption(item.getName().translate(jsonOpts.getAdLanguage()))
				.build();
	}
}
