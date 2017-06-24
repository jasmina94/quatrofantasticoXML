//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.06.24 at 01:57:20 PM CEST 
//


package rs.ac.uns.ftn.mt910;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import rs.ac.uns.ftn.tipovi.TPodaciBanka;
import rs.ac.uns.ftn.tipovi.TPodaciNalog;


/**
 * <p>Java class for mt910 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mt910"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="podaci_o_banci_poverioca" type="{http://www.ftn.uns.ac.rs/tipovi}TPodaciBanka"/&gt;
 *         &lt;element name="podaci_o_nalogu"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;extension base="{http://www.ftn.uns.ac.rs/tipovi}TPodaciNalog"&gt;
 *                 &lt;attribute name="id_poruke_naloga"&gt;
 *                   &lt;simpleType&gt;
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                       &lt;maxLength value="50"/&gt;
 *                     &lt;/restriction&gt;
 *                   &lt;/simpleType&gt;
 *                 &lt;/attribute&gt;
 *               &lt;/extension&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id_poruke"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;maxLength value="50"/&gt;
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
@XmlType(name = "mt910", propOrder = {
    "podaciOBanciPoverioca",
    "podaciONalogu"
})
public class Mt910 {

    @XmlElement(name = "podaci_o_banci_poverioca", required = true)
    protected TPodaciBanka podaciOBanciPoverioca;
    @XmlElement(name = "podaci_o_nalogu", required = true)
    protected PodaciONalogu podaciONalogu;
    @XmlAttribute(name = "id_poruke")
    protected String idPoruke;

    /**
     * Gets the value of the podaciOBanciPoverioca property.
     * 
     * @return
     *     possible object is
     *     {@link TPodaciBanka }
     *     
     */
    public TPodaciBanka getPodaciOBanciPoverioca() {
        return podaciOBanciPoverioca;
    }

    /**
     * Sets the value of the podaciOBanciPoverioca property.
     * 
     * @param value
     *     allowed object is
     *     {@link TPodaciBanka }
     *     
     */
    public void setPodaciOBanciPoverioca(TPodaciBanka value) {
        this.podaciOBanciPoverioca = value;
    }

    /**
     * Gets the value of the podaciONalogu property.
     * 
     * @return
     *     possible object is
     *     {@link Mt910 .PodaciONalogu }
     *     
     */
    public PodaciONalogu getPodaciONalogu() {
        return podaciONalogu;
    }

    /**
     * Sets the value of the podaciONalogu property.
     * 
     * @param value
     *     allowed object is
     *     {@link Mt910 .PodaciONalogu }
     *     
     */
    public void setPodaciONalogu(PodaciONalogu value) {
        this.podaciONalogu = value;
    }

    /**
     * Gets the value of the idPoruke property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdPoruke() {
        return idPoruke;
    }

    /**
     * Sets the value of the idPoruke property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdPoruke(String value) {
        this.idPoruke = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;extension base="{http://www.ftn.uns.ac.rs/tipovi}TPodaciNalog"&gt;
     *       &lt;attribute name="id_poruke_naloga"&gt;
     *         &lt;simpleType&gt;
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *             &lt;maxLength value="50"/&gt;
     *           &lt;/restriction&gt;
     *         &lt;/simpleType&gt;
     *       &lt;/attribute&gt;
     *     &lt;/extension&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class PodaciONalogu
        extends TPodaciNalog
    {

        @XmlAttribute(name = "id_poruke_naloga")
        protected String idPorukeNaloga;

        /**
         * Gets the value of the idPorukeNaloga property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIdPorukeNaloga() {
            return idPorukeNaloga;
        }

        /**
         * Sets the value of the idPorukeNaloga property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIdPorukeNaloga(String value) {
            this.idPorukeNaloga = value;
        }

    }

}