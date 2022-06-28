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

package com.adekia.exchange.amazonsp.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * Contains the business invoice tax information. Available only in the TR marketplace.
 */
@Schema(description = "Contains the business invoice tax information. Available only in the TR marketplace.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-06-28T13:53:07.940430682+02:00[Europe/Paris]")
public class BuyerTaxInformation {
  @JsonProperty("BuyerLegalCompanyName")
  private String buyerLegalCompanyName = null;

  @JsonProperty("BuyerBusinessAddress")
  private String buyerBusinessAddress = null;

  @JsonProperty("BuyerTaxRegistrationId")
  private String buyerTaxRegistrationId = null;

  @JsonProperty("BuyerTaxOffice")
  private String buyerTaxOffice = null;

  public BuyerTaxInformation buyerLegalCompanyName(String buyerLegalCompanyName) {
    this.buyerLegalCompanyName = buyerLegalCompanyName;
    return this;
  }

   /**
   * Business buyer&#x27;s company legal name.
   * @return buyerLegalCompanyName
  **/
  @Schema(description = "Business buyer's company legal name.")
  public String getBuyerLegalCompanyName() {
    return buyerLegalCompanyName;
  }

  public void setBuyerLegalCompanyName(String buyerLegalCompanyName) {
    this.buyerLegalCompanyName = buyerLegalCompanyName;
  }

  public BuyerTaxInformation buyerBusinessAddress(String buyerBusinessAddress) {
    this.buyerBusinessAddress = buyerBusinessAddress;
    return this;
  }

   /**
   * Business buyer&#x27;s address.
   * @return buyerBusinessAddress
  **/
  @Schema(description = "Business buyer's address.")
  public String getBuyerBusinessAddress() {
    return buyerBusinessAddress;
  }

  public void setBuyerBusinessAddress(String buyerBusinessAddress) {
    this.buyerBusinessAddress = buyerBusinessAddress;
  }

  public BuyerTaxInformation buyerTaxRegistrationId(String buyerTaxRegistrationId) {
    this.buyerTaxRegistrationId = buyerTaxRegistrationId;
    return this;
  }

   /**
   * Business buyer&#x27;s tax registration ID.
   * @return buyerTaxRegistrationId
  **/
  @Schema(description = "Business buyer's tax registration ID.")
  public String getBuyerTaxRegistrationId() {
    return buyerTaxRegistrationId;
  }

  public void setBuyerTaxRegistrationId(String buyerTaxRegistrationId) {
    this.buyerTaxRegistrationId = buyerTaxRegistrationId;
  }

  public BuyerTaxInformation buyerTaxOffice(String buyerTaxOffice) {
    this.buyerTaxOffice = buyerTaxOffice;
    return this;
  }

   /**
   * Business buyer&#x27;s tax office.
   * @return buyerTaxOffice
  **/
  @Schema(description = "Business buyer's tax office.")
  public String getBuyerTaxOffice() {
    return buyerTaxOffice;
  }

  public void setBuyerTaxOffice(String buyerTaxOffice) {
    this.buyerTaxOffice = buyerTaxOffice;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BuyerTaxInformation buyerTaxInformation = (BuyerTaxInformation) o;
    return Objects.equals(this.buyerLegalCompanyName, buyerTaxInformation.buyerLegalCompanyName) &&
        Objects.equals(this.buyerBusinessAddress, buyerTaxInformation.buyerBusinessAddress) &&
        Objects.equals(this.buyerTaxRegistrationId, buyerTaxInformation.buyerTaxRegistrationId) &&
        Objects.equals(this.buyerTaxOffice, buyerTaxInformation.buyerTaxOffice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(buyerLegalCompanyName, buyerBusinessAddress, buyerTaxRegistrationId, buyerTaxOffice);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BuyerTaxInformation {\n");
    
    sb.append("    buyerLegalCompanyName: ").append(toIndentedString(buyerLegalCompanyName)).append("\n");
    sb.append("    buyerBusinessAddress: ").append(toIndentedString(buyerBusinessAddress)).append("\n");
    sb.append("    buyerTaxRegistrationId: ").append(toIndentedString(buyerTaxRegistrationId)).append("\n");
    sb.append("    buyerTaxOffice: ").append(toIndentedString(buyerTaxOffice)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
