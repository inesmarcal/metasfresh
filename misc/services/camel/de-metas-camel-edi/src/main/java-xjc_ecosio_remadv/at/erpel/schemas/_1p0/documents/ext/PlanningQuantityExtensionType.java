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
 * <p>Java class for PlanningQuantityExtensionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PlanningQuantityExtensionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element ref="{http://erpel.at/schemas/1p0/documents/extensions/edifact}PlanningQuantityExtension"/&gt;
 *         &lt;element ref="{http://erpel.at/schemas/1p0/documents/ext}ErpelPlanningQuantityExtension"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PlanningQuantityExtensionType", propOrder = {
    "planningQuantityExtension",
    "erpelPlanningQuantityExtension"
})
public class PlanningQuantityExtensionType {

    @XmlElement(name = "PlanningQuantityExtension", namespace = "http://erpel.at/schemas/1p0/documents/extensions/edifact")
    protected at.erpel.schemas._1p0.documents.extensions.edifact.PlanningQuantityExtensionType planningQuantityExtension;
    @XmlElement(name = "ErpelPlanningQuantityExtension")
    protected CustomType erpelPlanningQuantityExtension;

    /**
     * Gets the value of the planningQuantityExtension property.
     * 
     * @return
     *     possible object is
     *     {@link at.erpel.schemas._1p0.documents.extensions.edifact.PlanningQuantityExtensionType }
     *     
     */
    public at.erpel.schemas._1p0.documents.extensions.edifact.PlanningQuantityExtensionType getPlanningQuantityExtension() {
        return planningQuantityExtension;
    }

    /**
     * Sets the value of the planningQuantityExtension property.
     * 
     * @param value
     *     allowed object is
     *     {@link at.erpel.schemas._1p0.documents.extensions.edifact.PlanningQuantityExtensionType }
     *     
     */
    public void setPlanningQuantityExtension(at.erpel.schemas._1p0.documents.extensions.edifact.PlanningQuantityExtensionType value) {
        this.planningQuantityExtension = value;
    }

    /**
     * Gets the value of the erpelPlanningQuantityExtension property.
     * 
     * @return
     *     possible object is
     *     {@link CustomType }
     *     
     */
    public CustomType getErpelPlanningQuantityExtension() {
        return erpelPlanningQuantityExtension;
    }

    /**
     * Sets the value of the erpelPlanningQuantityExtension property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomType }
     *     
     */
    public void setErpelPlanningQuantityExtension(CustomType value) {
        this.erpelPlanningQuantityExtension = value;
    }

}
