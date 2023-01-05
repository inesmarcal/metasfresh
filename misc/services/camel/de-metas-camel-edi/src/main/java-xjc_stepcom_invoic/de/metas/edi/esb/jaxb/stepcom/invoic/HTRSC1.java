//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.01.03 at 02:02:15 PM CET 
//


package de.metas.edi.esb.jaxb.stepcom.invoic;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HTRSC1 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HTRSC1"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DOCUMENTID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="TRANPORTTERMSQUAL" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="TRANPORTTERMSCODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TRANPORTTERMSDESC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LOCATIONQUAL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LOCATIONCODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LOCATIONNAME" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LOCATIONDATE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HTRSC1", propOrder = {
    "documentid",
    "tranporttermsqual",
    "tranporttermscode",
    "tranporttermsdesc",
    "locationqual",
    "locationcode",
    "locationname",
    "locationdate"
})
public class HTRSC1 {

    @XmlElement(name = "DOCUMENTID", required = true)
    protected String documentid;
    @XmlElement(name = "TRANPORTTERMSQUAL", required = true)
    protected String tranporttermsqual;
    @XmlElement(name = "TRANPORTTERMSCODE")
    protected String tranporttermscode;
    @XmlElement(name = "TRANPORTTERMSDESC")
    protected String tranporttermsdesc;
    @XmlElement(name = "LOCATIONQUAL")
    protected String locationqual;
    @XmlElement(name = "LOCATIONCODE")
    protected String locationcode;
    @XmlElement(name = "LOCATIONNAME")
    protected String locationname;
    @XmlElement(name = "LOCATIONDATE")
    protected String locationdate;

    /**
     * Gets the value of the documentid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDOCUMENTID() {
        return documentid;
    }

    /**
     * Sets the value of the documentid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDOCUMENTID(String value) {
        this.documentid = value;
    }

    /**
     * Gets the value of the tranporttermsqual property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRANPORTTERMSQUAL() {
        return tranporttermsqual;
    }

    /**
     * Sets the value of the tranporttermsqual property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRANPORTTERMSQUAL(String value) {
        this.tranporttermsqual = value;
    }

    /**
     * Gets the value of the tranporttermscode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRANPORTTERMSCODE() {
        return tranporttermscode;
    }

    /**
     * Sets the value of the tranporttermscode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRANPORTTERMSCODE(String value) {
        this.tranporttermscode = value;
    }

    /**
     * Gets the value of the tranporttermsdesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRANPORTTERMSDESC() {
        return tranporttermsdesc;
    }

    /**
     * Sets the value of the tranporttermsdesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRANPORTTERMSDESC(String value) {
        this.tranporttermsdesc = value;
    }

    /**
     * Gets the value of the locationqual property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLOCATIONQUAL() {
        return locationqual;
    }

    /**
     * Sets the value of the locationqual property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLOCATIONQUAL(String value) {
        this.locationqual = value;
    }

    /**
     * Gets the value of the locationcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLOCATIONCODE() {
        return locationcode;
    }

    /**
     * Sets the value of the locationcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLOCATIONCODE(String value) {
        this.locationcode = value;
    }

    /**
     * Gets the value of the locationname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLOCATIONNAME() {
        return locationname;
    }

    /**
     * Sets the value of the locationname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLOCATIONNAME(String value) {
        this.locationname = value;
    }

    /**
     * Gets the value of the locationdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLOCATIONDATE() {
        return locationdate;
    }

    /**
     * Sets the value of the locationdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLOCATIONDATE(String value) {
        this.locationdate = value;
    }

}
