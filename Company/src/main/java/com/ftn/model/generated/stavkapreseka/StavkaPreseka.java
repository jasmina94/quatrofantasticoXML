//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.06.25 at 12:30:27 PM CEST 
//


package com.ftn.model.generated.stavkapreseka;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import rs.ac.uns.ftn.tipovi.TPodaciPlacanje;
import rs.ac.uns.ftn.tipovi.TPravnoLice;


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
 *         &lt;element name="podaci_o_duzniku" type="{http://www.ftn.uns.ac.rs/tipovi}TPravnoLice"/&gt;
 *         &lt;element name="podaci_o_poveriocu" type="{http://www.ftn.uns.ac.rs/tipovi}TPravnoLice"/&gt;
 *         &lt;element name="podaci_o_uplati"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="svrha_placanja"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="255"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="iznos" type="{http://www.ftn.uns.ac.rs/tipovi}TIznos"/&gt;
 *                   &lt;element name="podaci_o_zaduzenju" type="{http://www.ftn.uns.ac.rs/tipovi}TPodaciPlacanje"/&gt;
 *                   &lt;element name="podaci_o_odobrenju" type="{http://www.ftn.uns.ac.rs/tipovi}TPodaciPlacanje"/&gt;
 *                   &lt;element name="datum_naloga" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *                   &lt;element name="datum_valute" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *                 &lt;/sequence&gt;
 *                 &lt;attribute name="smer"&gt;
 *                   &lt;simpleType&gt;
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                       &lt;enumeration value="K"/&gt;
 *                       &lt;enumeration value="T"/&gt;
 *                       &lt;length value="1"/&gt;
 *                     &lt;/restriction&gt;
 *                   &lt;/simpleType&gt;
 *                 &lt;/attribute&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
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
    "podaciODuzniku",
    "podaciOPoveriocu",
    "podaciOUplati"
})
@XmlRootElement(name = "stavka_preseka")
public class StavkaPreseka {

    @XmlElement(name = "podaci_o_duzniku", required = true)
    protected TPravnoLice podaciODuzniku;
    @XmlElement(name = "podaci_o_poveriocu", required = true)
    protected TPravnoLice podaciOPoveriocu;
    @XmlElement(name = "podaci_o_uplati", required = true)
    protected PodaciOUplati podaciOUplati;

    /**
     * Gets the value of the podaciODuzniku property.
     * 
     * @return
     *     possible object is
     *     {@link TPravnoLice }
     *     
     */
    public TPravnoLice getPodaciODuzniku() {
        return podaciODuzniku;
    }

    /**
     * Sets the value of the podaciODuzniku property.
     * 
     * @param value
     *     allowed object is
     *     {@link TPravnoLice }
     *     
     */
    public void setPodaciODuzniku(TPravnoLice value) {
        this.podaciODuzniku = value;
    }

    /**
     * Gets the value of the podaciOPoveriocu property.
     * 
     * @return
     *     possible object is
     *     {@link TPravnoLice }
     *     
     */
    public TPravnoLice getPodaciOPoveriocu() {
        return podaciOPoveriocu;
    }

    /**
     * Sets the value of the podaciOPoveriocu property.
     * 
     * @param value
     *     allowed object is
     *     {@link TPravnoLice }
     *     
     */
    public void setPodaciOPoveriocu(TPravnoLice value) {
        this.podaciOPoveriocu = value;
    }

    /**
     * Gets the value of the podaciOUplati property.
     * 
     * @return
     *     possible object is
     *     {@link PodaciOUplati }
     *     
     */
    public PodaciOUplati getPodaciOUplati() {
        return podaciOUplati;
    }

    /**
     * Sets the value of the podaciOUplati property.
     * 
     * @param value
     *     allowed object is
     *     {@link PodaciOUplati }
     *     
     */
    public void setPodaciOUplati(PodaciOUplati value) {
        this.podaciOUplati = value;
    }


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
     *         &lt;element name="svrha_placanja"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="255"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="iznos" type="{http://www.ftn.uns.ac.rs/tipovi}TIznos"/&gt;
     *         &lt;element name="podaci_o_zaduzenju" type="{http://www.ftn.uns.ac.rs/tipovi}TPodaciPlacanje"/&gt;
     *         &lt;element name="podaci_o_odobrenju" type="{http://www.ftn.uns.ac.rs/tipovi}TPodaciPlacanje"/&gt;
     *         &lt;element name="datum_naloga" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
     *         &lt;element name="datum_valute" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
     *       &lt;/sequence&gt;
     *       &lt;attribute name="smer"&gt;
     *         &lt;simpleType&gt;
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *             &lt;enumeration value="K"/&gt;
     *             &lt;enumeration value="T"/&gt;
     *             &lt;length value="1"/&gt;
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
    @XmlType(name = "", propOrder = {
        "svrhaPlacanja",
        "iznos",
        "podaciOZaduzenju",
        "podaciOOdobrenju",
        "datumNaloga",
        "datumValute"
    })
    public static class PodaciOUplati {

        @XmlElement(name = "svrha_placanja", required = true)
        protected String svrhaPlacanja;
        @XmlElement(required = true)
        protected BigDecimal iznos;
        @XmlElement(name = "podaci_o_zaduzenju", required = true)
        protected TPodaciPlacanje podaciOZaduzenju;
        @XmlElement(name = "podaci_o_odobrenju", required = true)
        protected TPodaciPlacanje podaciOOdobrenju;
        @XmlElement(name = "datum_naloga", required = true)
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar datumNaloga;
        @XmlElement(name = "datum_valute", required = true)
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar datumValute;
        @XmlAttribute(name = "smer")
        protected String smer;

        /**
         * Gets the value of the svrhaPlacanja property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSvrhaPlacanja() {
            return svrhaPlacanja;
        }

        /**
         * Sets the value of the svrhaPlacanja property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSvrhaPlacanja(String value) {
            this.svrhaPlacanja = value;
        }

        /**
         * Gets the value of the iznos property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getIznos() {
            return iznos;
        }

        /**
         * Sets the value of the iznos property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setIznos(BigDecimal value) {
            this.iznos = value;
        }

        /**
         * Gets the value of the podaciOZaduzenju property.
         * 
         * @return
         *     possible object is
         *     {@link TPodaciPlacanje }
         *     
         */
        public TPodaciPlacanje getPodaciOZaduzenju() {
            return podaciOZaduzenju;
        }

        /**
         * Sets the value of the podaciOZaduzenju property.
         * 
         * @param value
         *     allowed object is
         *     {@link TPodaciPlacanje }
         *     
         */
        public void setPodaciOZaduzenju(TPodaciPlacanje value) {
            this.podaciOZaduzenju = value;
        }

        /**
         * Gets the value of the podaciOOdobrenju property.
         * 
         * @return
         *     possible object is
         *     {@link TPodaciPlacanje }
         *     
         */
        public TPodaciPlacanje getPodaciOOdobrenju() {
            return podaciOOdobrenju;
        }

        /**
         * Sets the value of the podaciOOdobrenju property.
         * 
         * @param value
         *     allowed object is
         *     {@link TPodaciPlacanje }
         *     
         */
        public void setPodaciOOdobrenju(TPodaciPlacanje value) {
            this.podaciOOdobrenju = value;
        }

        /**
         * Gets the value of the datumNaloga property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDatumNaloga() {
            return datumNaloga;
        }

        /**
         * Sets the value of the datumNaloga property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDatumNaloga(XMLGregorianCalendar value) {
            this.datumNaloga = value;
        }

        /**
         * Gets the value of the datumValute property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDatumValute() {
            return datumValute;
        }

        /**
         * Sets the value of the datumValute property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDatumValute(XMLGregorianCalendar value) {
            this.datumValute = value;
        }

        /**
         * Gets the value of the smer property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSmer() {
            return smer;
        }

        /**
         * Sets the value of the smer property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSmer(String value) {
            this.smer = value;
        }

    }

}
