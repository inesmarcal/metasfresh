//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.12.09 at 06:04:17 PM CET 
//


package de.metas.payment.camt054_001_06;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for POIComponentType1Code.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="POIComponentType1Code"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="SOFT"/&gt;
 *     &lt;enumeration value="EMVK"/&gt;
 *     &lt;enumeration value="EMVO"/&gt;
 *     &lt;enumeration value="MRIT"/&gt;
 *     &lt;enumeration value="CHIT"/&gt;
 *     &lt;enumeration value="SECM"/&gt;
 *     &lt;enumeration value="PEDV"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "POIComponentType1Code")
@XmlEnum
public enum POIComponentType1Code {

    SOFT,
    EMVK,
    EMVO,
    MRIT,
    CHIT,
    SECM,
    PEDV;

    public String value() {
        return name();
    }

    public static POIComponentType1Code fromValue(String v) {
        return valueOf(v);
    }

}
