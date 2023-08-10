//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.08.10 at 04:28:05 PM CEST 
//


package at.erpel.schemas._1p0.documents.ext;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ShipperExtensionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ShipperExtensionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element ref="{http://erpel.at/schemas/1p0/documents/extensions/edifact}ShipperExtension"/&gt;
 *         &lt;element ref="{http://erpel.at/schemas/1p0/documents/ext}ErpelShipperExtension"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShipperExtensionType", propOrder = {
    "shipperExtension",
    "erpelShipperExtension"
})
public class ShipperExtensionType {

    @XmlElement(name = "ShipperExtension", namespace = "http://erpel.at/schemas/1p0/documents/extensions/edifact")
    protected at.erpel.schemas._1p0.documents.extensions.edifact.ShipperExtensionType shipperExtension;
    @XmlElement(name = "ErpelShipperExtension")
    protected CustomType erpelShipperExtension;

    /**
     * Gets the value of the shipperExtension property.
     * 
     * @return
     *     possible object is
     *     {@link at.erpel.schemas._1p0.documents.extensions.edifact.ShipperExtensionType }
     *     
     */
    public at.erpel.schemas._1p0.documents.extensions.edifact.ShipperExtensionType getShipperExtension() {
        return shipperExtension;
    }

    /**
     * Sets the value of the shipperExtension property.
     * 
     * @param value
     *     allowed object is
     *     {@link at.erpel.schemas._1p0.documents.extensions.edifact.ShipperExtensionType }
     *     
     */
    public void setShipperExtension(at.erpel.schemas._1p0.documents.extensions.edifact.ShipperExtensionType value) {
        this.shipperExtension = value;
    }

    /**
     * Gets the value of the erpelShipperExtension property.
     * 
     * @return
     *     possible object is
     *     {@link CustomType }
     *     
     */
    public CustomType getErpelShipperExtension() {
        return erpelShipperExtension;
    }

    /**
     * Sets the value of the erpelShipperExtension property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomType }
     *     
     */
    public void setErpelShipperExtension(CustomType value) {
        this.erpelShipperExtension = value;
    }

}
