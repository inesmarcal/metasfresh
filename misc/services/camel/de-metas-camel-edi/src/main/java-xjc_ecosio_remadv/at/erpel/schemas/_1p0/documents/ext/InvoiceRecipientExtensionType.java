//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.01.06 at 03:38:18 PM CET 
//


package at.erpel.schemas._1p0.documents.ext;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InvoiceRecipientExtensionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InvoiceRecipientExtensionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element ref="{http://erpel.at/schemas/1p0/documents/extensions/edifact}InvoiceRecipientExtension"/&gt;
 *         &lt;element ref="{http://erpel.at/schemas/1p0/documents/ext}ErpelInvoiceRecipientExtension"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InvoiceRecipientExtensionType", propOrder = {
    "invoiceRecipientExtension",
    "erpelInvoiceRecipientExtension"
})
public class InvoiceRecipientExtensionType {

    @XmlElement(name = "InvoiceRecipientExtension", namespace = "http://erpel.at/schemas/1p0/documents/extensions/edifact")
    protected at.erpel.schemas._1p0.documents.extensions.edifact.InvoiceRecipientExtensionType invoiceRecipientExtension;
    @XmlElement(name = "ErpelInvoiceRecipientExtension")
    protected CustomType erpelInvoiceRecipientExtension;

    /**
     * Gets the value of the invoiceRecipientExtension property.
     * 
     * @return
     *     possible object is
     *     {@link at.erpel.schemas._1p0.documents.extensions.edifact.InvoiceRecipientExtensionType }
     *     
     */
    public at.erpel.schemas._1p0.documents.extensions.edifact.InvoiceRecipientExtensionType getInvoiceRecipientExtension() {
        return invoiceRecipientExtension;
    }

    /**
     * Sets the value of the invoiceRecipientExtension property.
     * 
     * @param value
     *     allowed object is
     *     {@link at.erpel.schemas._1p0.documents.extensions.edifact.InvoiceRecipientExtensionType }
     *     
     */
    public void setInvoiceRecipientExtension(at.erpel.schemas._1p0.documents.extensions.edifact.InvoiceRecipientExtensionType value) {
        this.invoiceRecipientExtension = value;
    }

    /**
     * Gets the value of the erpelInvoiceRecipientExtension property.
     * 
     * @return
     *     possible object is
     *     {@link CustomType }
     *     
     */
    public CustomType getErpelInvoiceRecipientExtension() {
        return erpelInvoiceRecipientExtension;
    }

    /**
     * Sets the value of the erpelInvoiceRecipientExtension property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomType }
     *     
     */
    public void setErpelInvoiceRecipientExtension(CustomType value) {
        this.erpelInvoiceRecipientExtension = value;
    }

}
