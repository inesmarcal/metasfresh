package de.metas.manufacturing.order;

import de.metas.handlingunits.HuId;
import de.metas.product.ProductId;
import de.metas.quantity.Quantity;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.adempiere.warehouse.LocatorId;
import org.eevolution.api.PPOrderBOMLineId;
import org.eevolution.api.PPOrderId;

@Value
@Builder
public class PPOrderIssueScheduleCreateRequest
{
	@NonNull PPOrderId ppOrderId;
	@NonNull PPOrderBOMLineId ppOrderBOMLineId;

	int seqNo;

	@NonNull ProductId productId;
	@NonNull Quantity qtyToIssue;
	@NonNull HuId issueFromHUId;
	@NonNull LocatorId issueFromLocatorId;
}
