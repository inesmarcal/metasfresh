//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.08.10 at 04:28:05 PM CEST 
//


package at.erpel.schemas._1p0.documents.extensions.edifact;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigInteger;


/**
 * <p>Java class for ConsignmentItemInformationExtensionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConsignmentItemInformationExtensionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://erpel.at/schemas/1p0/documents/extensions/edifact}PackagingDimensions" minOccurs="0"/&gt;
 *         &lt;element name="StackingFactor" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsignmentItemInformationExtensionType", propOrder = {
    "packagingDimensions",
    "stackingFactor"
})
public class ConsignmentItemInformationExtensionType {

    @XmlElement(name = "PackagingDimensions")
    protected DimensionsType packagingDimensions;
    @XmlElement(name = "StackingFactor")
    protected BigInteger stackingFactor;

    /**
     * Packaging dimensions.
     * 
     * @return
     *     possible object is
     *     {@link DimensionsType }
     *     
     */
    public DimensionsType getPackagingDimensions() {
        return packagingDimensions;
    }

    /**
     * Sets the value of the packagingDimensions property.
     * 
     * @param value
     *     allowed object is
     *     {@link DimensionsType }
     *     
     */
    public void setPackagingDimensions(DimensionsType value) {
        this.packagingDimensions = value;
    }

    /**
     * Gets the value of the stackingFactor property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getStackingFactor() {
        return stackingFactor;
    }

    /**
     * Sets the value of the stackingFactor property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setStackingFactor(BigInteger value) {
        this.stackingFactor = value;
    }

}
