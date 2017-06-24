package rs.ac.uns.ftn.model.database;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by zlatan on 6/24/17.
 */

@Entity
@NoArgsConstructor
@Data
public class Mt103Model {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String idPoruke;

    @Column(length = 8)
    private String SWIFTBankeDuznika;

    @Column(length = 8)
    private String SWIFTBankePoverioca;

    @Column(length = 18)
    private String racunBankeDuznika;

    @Column(length = 18)
    private String racunBankePoverioca;

    @Column
    private String duznik;

    @Column
    private String poverilac;

    @Column
    private String svrhaPlacanja;

    @Column
    private Date datumValute;

    @Column
    private Date datumNaloga;

    @Column(length = 18)
    private String racunDuznika;

    @Column
    private int modelZaduzenja;

    @Column
    private String pozivNaBrojZaduzenja;

    @Column
    private String racunPoverioca;

    @Column
    private int modelOdobrenja;

    @Column
    private String pozivNaBrojOdobrenja;

    @Column
    private Double iznos;

    @Column
    private String sifraValute;

}