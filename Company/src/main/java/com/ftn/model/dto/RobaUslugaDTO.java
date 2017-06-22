package com.ftn.model.dto;

import com.ftn.model.RobaUsluga;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by JELENA on 22.6.2017.
 */
@Data
@NoArgsConstructor
public class RobaUslugaDTO  {

    private long id;

    @NotNull
    private String naziv;

    @NotNull
    private BigDecimal cena;

    @NotNull
    private String jedinicaMere;

    @NotNull
    private BigDecimal procenatRabata;


    public RobaUslugaDTO(RobaUsluga robaUsluga) {
        this.id = robaUsluga.getId();
        this.naziv = robaUsluga.getNaziv();
        this.cena = robaUsluga.getCena();
        this.jedinicaMere = robaUsluga.getJedinicaMere();
        this.procenatRabata = robaUsluga.getProcenatRabata();

    }

    public RobaUsluga construct() {
        final RobaUsluga robaUsluga = new RobaUsluga();
        robaUsluga.setNaziv(naziv);
        robaUsluga.setCena(cena);
        robaUsluga.setJedinicaMere(jedinicaMere);
        robaUsluga.setProcenatRabata(procenatRabata);

        return robaUsluga;
    }
}
