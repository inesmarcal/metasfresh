/*
 * #%L
 * de.metas.banking.camt53
 * %%
 * Copyright (C) 2023 metas GmbH
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.7 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.06.07 at 02:56:38 PM EEST 
//


package de.metas.banking.camt53.jaxb.camt053_001_04;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for PointOfInteractionComponent1 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PointOfInteractionComponent1"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="POICmpntTp" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}POIComponentType1Code"/&gt;
 *         &lt;element name="ManfctrId" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max35Text" minOccurs="0"/&gt;
 *         &lt;element name="Mdl" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max35Text" minOccurs="0"/&gt;
 *         &lt;element name="VrsnNb" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max16Text" minOccurs="0"/&gt;
 *         &lt;element name="SrlNb" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max35Text" minOccurs="0"/&gt;
 *         &lt;element name="ApprvlNb" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max70Text" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PointOfInteractionComponent1", propOrder = {
    "poiCmpntTp",
    "manfctrId",
    "mdl",
    "vrsnNb",
    "srlNb",
    "apprvlNb"
})
public class PointOfInteractionComponent1 {

    @XmlElement(name = "POICmpntTp", required = true)
    @XmlSchemaType(name = "string")
    protected POIComponentType1Code poiCmpntTp;
    @XmlElement(name = "ManfctrId")
    protected String manfctrId;
    @XmlElement(name = "Mdl")
    protected String mdl;
    @XmlElement(name = "VrsnNb")
    protected String vrsnNb;
    @XmlElement(name = "SrlNb")
    protected String srlNb;
    @XmlElement(name = "ApprvlNb")
    protected List<String> apprvlNb;

    /**
     * Gets the value of the poiCmpntTp property.
     * 
     * @return
     *     possible object is
     *     {@link POIComponentType1Code }
     *     
     */
    public POIComponentType1Code getPOICmpntTp() {
        return poiCmpntTp;
    }

    /**
     * Sets the value of the poiCmpntTp property.
     * 
     * @param value
     *     allowed object is
     *     {@link POIComponentType1Code }
     *     
     */
    public void setPOICmpntTp(POIComponentType1Code value) {
        this.poiCmpntTp = value;
    }

    /**
     * Gets the value of the manfctrId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManfctrId() {
        return manfctrId;
    }

    /**
     * Sets the value of the manfctrId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManfctrId(String value) {
        this.manfctrId = value;
    }

    /**
     * Gets the value of the mdl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMdl() {
        return mdl;
    }

    /**
     * Sets the value of the mdl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMdl(String value) {
        this.mdl = value;
    }

    /**
     * Gets the value of the vrsnNb property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVrsnNb() {
        return vrsnNb;
    }

    /**
     * Sets the value of the vrsnNb property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVrsnNb(String value) {
        this.vrsnNb = value;
    }

    /**
     * Gets the value of the srlNb property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrlNb() {
        return srlNb;
    }

    /**
     * Sets the value of the srlNb property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrlNb(String value) {
        this.srlNb = value;
    }

    /**
     * Gets the value of the apprvlNb property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the apprvlNb property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApprvlNb().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getApprvlNb() {
        if (apprvlNb == null) {
            apprvlNb = new ArrayList<String>();
        }
        return this.apprvlNb;
    }

}
