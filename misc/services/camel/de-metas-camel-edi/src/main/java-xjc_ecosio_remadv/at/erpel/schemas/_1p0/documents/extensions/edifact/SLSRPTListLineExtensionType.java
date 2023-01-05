//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.01.03 at 02:02:16 PM CET 
//


package at.erpel.schemas._1p0.documents.extensions.edifact;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for SLSRPTListLineExtensionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLSRPTListLineExtensionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice&gt;
 *           &lt;element ref="{http://erpel.at/schemas/1p0/documents/extensions/edifact}ReportingDate" minOccurs="0"/&gt;
 *           &lt;element ref="{http://erpel.at/schemas/1p0/documents/extensions/edifact}ReportingPeriod" minOccurs="0"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="PointOfSales" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SalesDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="DocumentInformation" type="{http://erpel.at/schemas/1p0/documents/extensions/edifact}DocumentReferenceType" minOccurs="0"/&gt;
 *         &lt;element ref="{http://erpel.at/schemas/1p0/documents/extensions/edifact}CurrencyCode" minOccurs="0"/&gt;
 *         &lt;element ref="{http://erpel.at/schemas/1p0/documents/extensions/edifact}AdditionalQuantitiy" minOccurs="0"/&gt;
 *         &lt;element ref="{http://erpel.at/schemas/1p0/documents/extensions/edifact}AdditionalBusinessPartner" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{http://erpel.at/schemas/1p0/documents/extensions/edifact}AdditionalReference" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLSRPTListLineExtensionType", propOrder = {
    "reportingDate",
    "reportingPeriod",
    "pointOfSales",
    "salesDate",
    "documentInformation",
    "currencyCode",
    "additionalQuantitiy",
    "additionalBusinessPartner",
    "additionalReference"
})
public class SLSRPTListLineExtensionType {

    @XmlElement(name = "ReportingDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar reportingDate;
    @XmlElement(name = "ReportingPeriod")
    protected PeriodType reportingPeriod;
    @XmlElement(name = "PointOfSales")
    protected String pointOfSales;
    @XmlElement(name = "SalesDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar salesDate;
    @XmlElement(name = "DocumentInformation")
    protected DocumentReferenceType documentInformation;
    @XmlElement(name = "CurrencyCode")
    protected String currencyCode;
    @XmlElement(name = "AdditionalQuantitiy")
    protected ExtendedQuantityType additionalQuantitiy;
    @XmlElement(name = "AdditionalBusinessPartner")
    protected List<BusinessEntityType> additionalBusinessPartner;
    @XmlElement(name = "AdditionalReference")
    protected List<ReferenceType> additionalReference;

    /**
     * 						
     * 						The date for which the sales report is provided.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReportingDate() {
        return reportingDate;
    }

    /**
     * Sets the value of the reportingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReportingDate(XMLGregorianCalendar value) {
        this.reportingDate = value;
    }

    /**
     * 		
     * 						The period, for which the sales report is provided.
     * 
     * @return
     *     possible object is
     *     {@link PeriodType }
     *     
     */
    public PeriodType getReportingPeriod() {
        return reportingPeriod;
    }

    /**
     * Sets the value of the reportingPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link PeriodType }
     *     
     */
    public void setReportingPeriod(PeriodType value) {
        this.reportingPeriod = value;
    }

    /**
     * Gets the value of the pointOfSales property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPointOfSales() {
        return pointOfSales;
    }

    /**
     * Sets the value of the pointOfSales property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPointOfSales(String value) {
        this.pointOfSales = value;
    }

    /**
     * Gets the value of the salesDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSalesDate() {
        return salesDate;
    }

    /**
     * Sets the value of the salesDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSalesDate(XMLGregorianCalendar value) {
        this.salesDate = value;
    }

    /**
     * Gets the value of the documentInformation property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentReferenceType }
     *     
     */
    public DocumentReferenceType getDocumentInformation() {
        return documentInformation;
    }

    /**
     * Sets the value of the documentInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentReferenceType }
     *     
     */
    public void setDocumentInformation(DocumentReferenceType value) {
        this.documentInformation = value;
    }

    /**
     * The currency in which the monetary values are provided. Codes according to ISO 4217 must be used.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCode(String value) {
        this.currencyCode = value;
    }

    /**
     * Quantity mentioned in the sales report. Please use quantity's qualifier to specify the meaning more detailed.
     * 
     * @return
     *     possible object is
     *     {@link ExtendedQuantityType }
     *     
     */
    public ExtendedQuantityType getAdditionalQuantitiy() {
        return additionalQuantitiy;
    }

    /**
     * Sets the value of the additionalQuantitiy property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtendedQuantityType }
     *     
     */
    public void setAdditionalQuantitiy(ExtendedQuantityType value) {
        this.additionalQuantitiy = value;
    }

    /**
     * DEPRICATED - please Document/Details/ItemList/ListLineItem/ListLineItemExtension/ListLineItemExtension/AdditionalBusinessPartner instead Gets the value of the additionalBusinessPartner property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the additionalBusinessPartner property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdditionalBusinessPartner().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BusinessEntityType }
     * 
     * 
     */
    public List<BusinessEntityType> getAdditionalBusinessPartner() {
        if (additionalBusinessPartner == null) {
            additionalBusinessPartner = new ArrayList<BusinessEntityType>();
        }
        return this.additionalBusinessPartner;
    }

    /**
     * Other references if no dedicated field is available.Gets the value of the additionalReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the additionalReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdditionalReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReferenceType }
     * 
     * 
     */
    public List<ReferenceType> getAdditionalReference() {
        if (additionalReference == null) {
            additionalReference = new ArrayList<ReferenceType>();
        }
        return this.additionalReference;
    }

}
