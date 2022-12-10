//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.12.09 at 05:38:01 PM CET 
//


package de.metas.vertical.pharma.vendor.gateway.msv3.schema.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * nur die fachliche Anfrage
 * 
 *         Die Struktur (Aufträge und Positionen) bezüglich Mengen und Reihenfolgen muss zwischen Anfrage und Antwort identisch
 *         sein
 *       
 * 
 * <p>Java class for Bestellung complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Bestellung"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Auftraege" type="{urn:msv3:v1}BestellungAuftrag" maxOccurs="1000"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="Id" use="required" type="{urn:msv3:v1}uuid" /&gt;
 *       &lt;attribute name="BestellSupportId" use="required"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *             &lt;minInclusive value="1"/&gt;
 *             &lt;maxInclusive value="999999"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bestellung", propOrder = {
    "auftraege"
})
public class Bestellung {

    @XmlElement(name = "Auftraege", required = true)
    protected List<BestellungAuftrag> auftraege;
    @XmlAttribute(name = "Id", required = true)
    protected String id;
    @XmlAttribute(name = "BestellSupportId", required = true)
    protected int bestellSupportId;

    /**
     * Gets the value of the auftraege property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the auftraege property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuftraege().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BestellungAuftrag }
     * 
     * 
     */
    public List<BestellungAuftrag> getAuftraege() {
        if (auftraege == null) {
            auftraege = new ArrayList<BestellungAuftrag>();
        }
        return this.auftraege;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the bestellSupportId property.
     * 
     */
    public int getBestellSupportId() {
        return bestellSupportId;
    }

    /**
     * Sets the value of the bestellSupportId property.
     * 
     */
    public void setBestellSupportId(int value) {
        this.bestellSupportId = value;
    }

}
