//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.08.10 at 04:28:05 PM CEST 
//


package at.erpel.schemas._1p0.documents;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IndicatorsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IndicatorsType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ProcessingIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CriticalStockCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndicatorsType", propOrder = {
    "processingIndicator",
    "criticalStockCode"
})
public class IndicatorsType {

    @XmlElement(name = "ProcessingIndicator")
    protected String processingIndicator;
    @XmlElement(name = "CriticalStockCode")
    protected String criticalStockCode;

    /**
     * Gets the value of the processingIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessingIndicator() {
        return processingIndicator;
    }

    /**
     * Sets the value of the processingIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessingIndicator(String value) {
        this.processingIndicator = value;
    }

    /**
     * Gets the value of the criticalStockCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriticalStockCode() {
        return criticalStockCode;
    }

    /**
     * Sets the value of the criticalStockCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriticalStockCode(String value) {
        this.criticalStockCode = value;
    }

}
