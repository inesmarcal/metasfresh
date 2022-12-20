/*
 * Selling Partner API for Orders
 * The Selling Partner API for Orders helps you programmatically retrieve order information. These APIs let you develop fast, flexible, custom applications in areas like order synchronization, order research, and demand-based decision support tools.
 *
 * OpenAPI spec version: v0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.adekia.exchange.amazonsp.client.orders.api;

import com.adekia.exchange.amazonsp.client.orders.model.UpdateShipmentStatusErrorResponse;
import com.adekia.exchange.amazonsp.client.orders.model.UpdateShipmentStatusRequest;
import org.junit.Test;
import org.junit.Ignore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for ShipmentApi
 */
@Ignore
public class ShipmentApiTest {

    private final ShipmentApi api = new ShipmentApi();

    
    /**
     * 
     *
     * Update the shipment status.  **Usage Plans:**  | Plan type | Rate (requests per second) | Burst | | ---- | ---- | ---- | |Default| 5 | 15 | |Selling partner specific| Variable | Variable |  The x-amzn-RateLimit-Limit response header returns the usage plan rate limits that were applied to the requested operation. Rate limits for some selling partners will vary from the default rate and burst shown in the table above. For more information, see \&quot;Usage Plans and Rate Limits\&quot; in the Selling Partner API documentation.
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void updateShipmentStatusTest() throws Exception {
        String orderId = null;
        UpdateShipmentStatusRequest payload = null;
        api.updateShipmentStatus(orderId, payload);

        // TODO: test validations
    }
    
}
