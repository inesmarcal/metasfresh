//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.01.03 at 02:02:16 PM CET 
//


package at.erpel.schemas._1p0.documents;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ListLineItemReductionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListLineItemReductionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element ref="{http://erpel.at/schemas/1p0/documents}ReductionAmount" minOccurs="0"/&gt;
 *         &lt;element ref="{http://erpel.at/schemas/1p0/documents}ReductionRate" minOccurs="0"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListLineItemReductionType", propOrder = {
    "reductionAmount",
    "reductionRate"
})
public class ListLineItemReductionType {

    @XmlElement(name = "ReductionAmount")
    protected BigDecimal reductionAmount;
    @XmlElement(name = "ReductionRate")
    protected BigDecimal reductionRate;

    /**
     * Used to denote details about the reduction amount. Must be a positive value.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getReductionAmount() {
        return reductionAmount;
    }

    /**
     * Sets the value of the reductionAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setReductionAmount(BigDecimal value) {
        this.reductionAmount = value;
    }

    /**
     * Used to denote details about the reduction rate.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getReductionRate() {
        return reductionRate;
    }

    /**
     * Sets the value of the reductionRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setReductionRate(BigDecimal value) {
        this.reductionRate = value;
    }

}
