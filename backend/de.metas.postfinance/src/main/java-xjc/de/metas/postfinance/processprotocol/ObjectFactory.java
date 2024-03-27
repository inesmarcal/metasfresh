//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.7 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2024.03.26 at 03:00:37 PM EET 
//


package de.metas.postfinance.processprotocol;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.metas.postfinance.processprotocol package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.metas.postfinance.processprotocol
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Envelope }
     * 
     */
    public Envelope createEnvelope() {
        return new Envelope();
    }

    /**
     * Create an instance of {@link Envelope.Body }
     * 
     */
    public Envelope.Body createEnvelopeBody() {
        return new Envelope.Body();
    }

    /**
     * Create an instance of {@link Envelope.Body.DeliveryDate }
     * 
     */
    public Envelope.Body.DeliveryDate createEnvelopeBodyDeliveryDate() {
        return new Envelope.Body.DeliveryDate();
    }

    /**
     * Create an instance of {@link Envelope.Body.DeliveryDate.OKResult }
     * 
     */
    public Envelope.Body.DeliveryDate.OKResult createEnvelopeBodyDeliveryDateOKResult() {
        return new Envelope.Body.DeliveryDate.OKResult();
    }

    /**
     * Create an instance of {@link Envelope.Header }
     * 
     */
    public Envelope.Header createEnvelopeHeader() {
        return new Envelope.Header();
    }

    /**
     * Create an instance of {@link BillType }
     * 
     */
    public BillType createBillType() {
        return new BillType();
    }

    /**
     * Create an instance of {@link RejectedType }
     * 
     */
    public RejectedType createRejectedType() {
        return new RejectedType();
    }

    /**
     * Create an instance of {@link Envelope.Body.RejectedBills }
     * 
     */
    public Envelope.Body.RejectedBills createEnvelopeBodyRejectedBills() {
        return new Envelope.Body.RejectedBills();
    }

    /**
     * Create an instance of {@link Envelope.Body.DeliveryDate.NOKResult }
     * 
     */
    public Envelope.Body.DeliveryDate.NOKResult createEnvelopeBodyDeliveryDateNOKResult() {
        return new Envelope.Body.DeliveryDate.NOKResult();
    }

    /**
     * Create an instance of {@link Envelope.Body.DeliveryDate.OKResult.Bill }
     * 
     */
    public Envelope.Body.DeliveryDate.OKResult.Bill createEnvelopeBodyDeliveryDateOKResultBill() {
        return new Envelope.Body.DeliveryDate.OKResult.Bill();
    }

}
