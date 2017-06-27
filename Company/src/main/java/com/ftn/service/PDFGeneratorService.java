package com.ftn.service;

import com.ftn.model.database.AnalitikaIzvoda;
import com.ftn.model.database.DnevnoStanjeRacuna;
import com.ftn.model.generated.faktura.Faktura;

import java.util.ArrayList;


/**
 * Created by Jasmina on 6/27/17.
 */
public interface PDFGeneratorService {

    Faktura generisiFakturaPDF(Faktura faktura);

    void generisiIzvodPDF(DnevnoStanjeRacuna dnevnoStanjeRacuna, ArrayList<AnalitikaIzvoda> analitikaIzvodaList);
}
