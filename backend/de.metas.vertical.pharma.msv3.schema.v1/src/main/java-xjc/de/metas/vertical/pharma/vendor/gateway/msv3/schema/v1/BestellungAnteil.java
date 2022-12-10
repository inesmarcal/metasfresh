//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.12.09 at 05:38:01 PM CET 
//


package de.metas.vertical.pharma.vendor.gateway.msv3.schema.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for BestellungAnteil complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BestellungAnteil"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Menge"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int"&gt;
 *               &lt;minInclusive value="1"/&gt;
 *               &lt;maxInclusive value="9999"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Typ" type="{urn:msv3:v1}BestellungRueckmeldungTyp"/&gt;
 *         &lt;element name="Lieferzeitpunkt" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Tour" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;minLength value="1"/&gt;
 *               &lt;maxLength value="80"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Grund" type="{urn:msv3:v1}BestellungDefektgrund"/&gt;
 *         &lt;element name="TourId" type="{urn:msv3:v1}DruckbareKennung" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BestellungAnteil", propOrder = {
    "menge",
    "typ",
    "lieferzeitpunkt",
    "tour",
    "grund",
    "tourId"
})
public class BestellungAnteil {

    @XmlElement(name = "Menge", required = true, type = Integer.class, nillable = true)
    protected Integer menge;
    @XmlElement(name = "Typ", required = true)
    @XmlSchemaType(name = "string")
    protected BestellungRueckmeldungTyp typ;
    @XmlElement(name = "Lieferzeitpunkt", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lieferzeitpunkt;
    @XmlElement(name = "Tour")
    protected String tour;
    @XmlElement(name = "Grund", required = true)
    @XmlSchemaType(name = "string")
    protected BestellungDefektgrund grund;
    @XmlElement(name = "TourId")
    protected String tourId;

    /**
     * Gets the value of the menge property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMenge() {
        return menge;
    }

    /**
     * Sets the value of the menge property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMenge(Integer value) {
        this.menge = value;
    }

    /**
     * Gets the value of the typ property.
     * 
     * @return
     *     possible object is
     *     {@link BestellungRueckmeldungTyp }
     *     
     */
    public BestellungRueckmeldungTyp getTyp() {
        return typ;
    }

    /**
     * Sets the value of the typ property.
     * 
     * @param value
     *     allowed object is
     *     {@link BestellungRueckmeldungTyp }
     *     
     */
    public void setTyp(BestellungRueckmeldungTyp value) {
        this.typ = value;
    }

    /**
     * Gets the value of the lieferzeitpunkt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLieferzeitpunkt() {
        return lieferzeitpunkt;
    }

    /**
     * Sets the value of the lieferzeitpunkt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLieferzeitpunkt(XMLGregorianCalendar value) {
        this.lieferzeitpunkt = value;
    }

    /**
     * Gets the value of the tour property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTour() {
        return tour;
    }

    /**
     * Sets the value of the tour property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTour(String value) {
        this.tour = value;
    }

    /**
     * Gets the value of the grund property.
     * 
     * @return
     *     possible object is
     *     {@link BestellungDefektgrund }
     *     
     */
    public BestellungDefektgrund getGrund() {
        return grund;
    }

    /**
     * Sets the value of the grund property.
     * 
     * @param value
     *     allowed object is
     *     {@link BestellungDefektgrund }
     *     
     */
    public void setGrund(BestellungDefektgrund value) {
        this.grund = value;
    }

    /**
     * Gets the value of the tourId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTourId() {
        return tourId;
    }

    /**
     * Sets the value of the tourId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTourId(String value) {
        this.tourId = value;
    }

}
