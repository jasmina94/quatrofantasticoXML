//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.06.24 at 12:58:02 PM CEST 
//


package rs.ac.uns.ftn.nalog_za_prenos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="nalog_za_prenos" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "nalogZaPrenos"
})
@XmlRootElement(name = "getNalogZaPrenosResponse")
public class GetNalogZaPrenosResponse {

    @XmlElement(name = "nalog_za_prenos", required = true)
    protected String nalogZaPrenos;

    /**
     * Gets the value of the nalogZaPrenos property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNalogZaPrenos() {
        return nalogZaPrenos;
    }

    /**
     * Sets the value of the nalogZaPrenos property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNalogZaPrenos(String value) {
        this.nalogZaPrenos = value;
    }

}
