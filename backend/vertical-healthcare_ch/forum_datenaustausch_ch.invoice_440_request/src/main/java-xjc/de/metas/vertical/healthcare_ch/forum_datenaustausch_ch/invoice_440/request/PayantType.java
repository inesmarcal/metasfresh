//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.12.09 at 05:47:04 PM CET 
//


package de.metas.vertical.healthcare_ch.forum_datenaustausch_ch.invoice_440.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.Duration;


/**
 * <p>Java class for payantType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="payantType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="biller" type="{http://www.forum-datenaustausch.ch/invoice}billerAddressType"/&gt;
 *         &lt;element name="provider" type="{http://www.forum-datenaustausch.ch/invoice}providerAddressType"/&gt;
 *         &lt;element name="insurance" type="{http://www.forum-datenaustausch.ch/invoice}insuranceAddressType"/&gt;
 *         &lt;element name="patient" type="{http://www.forum-datenaustausch.ch/invoice}patientAddressType"/&gt;
 *         &lt;element name="insured" type="{http://www.forum-datenaustausch.ch/invoice}patientAddressType" minOccurs="0"/&gt;
 *         &lt;element name="guarantor" type="{http://www.forum-datenaustausch.ch/invoice}guarantorAddressType"/&gt;
 *         &lt;element name="referrer" type="{http://www.forum-datenaustausch.ch/invoice}referrerAddressType" minOccurs="0"/&gt;
 *         &lt;element name="employer" type="{http://www.forum-datenaustausch.ch/invoice}employerAddressType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="payment_period" type="{http://www.w3.org/2001/XMLSchema}duration" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "payantType", namespace = "http://www.forum-datenaustausch.ch/invoice", propOrder = {
    "biller",
    "provider",
    "insurance",
    "patient",
    "insured",
    "guarantor",
    "referrer",
    "employer"
})
public class PayantType {

    @XmlElement(namespace = "http://www.forum-datenaustausch.ch/invoice", required = true)
    protected BillerAddressType biller;
    @XmlElement(namespace = "http://www.forum-datenaustausch.ch/invoice", required = true)
    protected ProviderAddressType provider;
    @XmlElement(namespace = "http://www.forum-datenaustausch.ch/invoice", required = true)
    protected InsuranceAddressType insurance;
    @XmlElement(namespace = "http://www.forum-datenaustausch.ch/invoice", required = true)
    protected PatientAddressType patient;
    @XmlElement(namespace = "http://www.forum-datenaustausch.ch/invoice")
    protected PatientAddressType insured;
    @XmlElement(namespace = "http://www.forum-datenaustausch.ch/invoice", required = true)
    protected GuarantorAddressType guarantor;
    @XmlElement(namespace = "http://www.forum-datenaustausch.ch/invoice")
    protected ReferrerAddressType referrer;
    @XmlElement(namespace = "http://www.forum-datenaustausch.ch/invoice")
    protected EmployerAddressType employer;
    @XmlAttribute(name = "payment_period")
    protected Duration paymentPeriod;

    /**
     * Gets the value of the biller property.
     * 
     * @return
     *     possible object is
     *     {@link BillerAddressType }
     *     
     */
    public BillerAddressType getBiller() {
        return biller;
    }

    /**
     * Sets the value of the biller property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillerAddressType }
     *     
     */
    public void setBiller(BillerAddressType value) {
        this.biller = value;
    }

    /**
     * Gets the value of the provider property.
     * 
     * @return
     *     possible object is
     *     {@link ProviderAddressType }
     *     
     */
    public ProviderAddressType getProvider() {
        return provider;
    }

    /**
     * Sets the value of the provider property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProviderAddressType }
     *     
     */
    public void setProvider(ProviderAddressType value) {
        this.provider = value;
    }

    /**
     * Gets the value of the insurance property.
     * 
     * @return
     *     possible object is
     *     {@link InsuranceAddressType }
     *     
     */
    public InsuranceAddressType getInsurance() {
        return insurance;
    }

    /**
     * Sets the value of the insurance property.
     * 
     * @param value
     *     allowed object is
     *     {@link InsuranceAddressType }
     *     
     */
    public void setInsurance(InsuranceAddressType value) {
        this.insurance = value;
    }

    /**
     * Gets the value of the patient property.
     * 
     * @return
     *     possible object is
     *     {@link PatientAddressType }
     *     
     */
    public PatientAddressType getPatient() {
        return patient;
    }

    /**
     * Sets the value of the patient property.
     * 
     * @param value
     *     allowed object is
     *     {@link PatientAddressType }
     *     
     */
    public void setPatient(PatientAddressType value) {
        this.patient = value;
    }

    /**
     * Gets the value of the insured property.
     * 
     * @return
     *     possible object is
     *     {@link PatientAddressType }
     *     
     */
    public PatientAddressType getInsured() {
        return insured;
    }

    /**
     * Sets the value of the insured property.
     * 
     * @param value
     *     allowed object is
     *     {@link PatientAddressType }
     *     
     */
    public void setInsured(PatientAddressType value) {
        this.insured = value;
    }

    /**
     * Gets the value of the guarantor property.
     * 
     * @return
     *     possible object is
     *     {@link GuarantorAddressType }
     *     
     */
    public GuarantorAddressType getGuarantor() {
        return guarantor;
    }

    /**
     * Sets the value of the guarantor property.
     * 
     * @param value
     *     allowed object is
     *     {@link GuarantorAddressType }
     *     
     */
    public void setGuarantor(GuarantorAddressType value) {
        this.guarantor = value;
    }

    /**
     * Gets the value of the referrer property.
     * 
     * @return
     *     possible object is
     *     {@link ReferrerAddressType }
     *     
     */
    public ReferrerAddressType getReferrer() {
        return referrer;
    }

    /**
     * Sets the value of the referrer property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReferrerAddressType }
     *     
     */
    public void setReferrer(ReferrerAddressType value) {
        this.referrer = value;
    }

    /**
     * Gets the value of the employer property.
     * 
     * @return
     *     possible object is
     *     {@link EmployerAddressType }
     *     
     */
    public EmployerAddressType getEmployer() {
        return employer;
    }

    /**
     * Sets the value of the employer property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmployerAddressType }
     *     
     */
    public void setEmployer(EmployerAddressType value) {
        this.employer = value;
    }

    /**
     * Gets the value of the paymentPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getPaymentPeriod() {
        return paymentPeriod;
    }

    /**
     * Sets the value of the paymentPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setPaymentPeriod(Duration value) {
        this.paymentPeriod = value;
    }

}
