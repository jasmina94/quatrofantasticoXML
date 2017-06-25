package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.exception.ServiceFaultException;
import rs.ac.uns.ftn.model.database.*;
import rs.ac.uns.ftn.model.dto.error.ServiceFault;
import rs.ac.uns.ftn.model.dto.mt102.Mt102;
import rs.ac.uns.ftn.model.dto.mt102body.Mt102Telo;
import rs.ac.uns.ftn.model.dto.mt102header.Mt102Zaglavlje;
import rs.ac.uns.ftn.model.dto.mt103.Mt103;
import rs.ac.uns.ftn.model.dto.nalog_za_prenos.NalogZaPrenos;
import rs.ac.uns.ftn.model.dto.tipovi.*;
import rs.ac.uns.ftn.model.environment.EnvironmentProperties;
import rs.ac.uns.ftn.repository.*;
import rs.ac.uns.ftn.service.ClearingService;
import rs.ac.uns.ftn.service.PlacanjeService;
import rs.ac.uns.ftn.service.RTGSService;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by zlatan on 6/24/17.
 */
@Service
public class PlacanjeServiceImpl implements PlacanjeService {

    @Autowired
    private RacunRepository repozitorijumRacuna;

    @Autowired
    private AnalitikaIzvodaRepository repozitorijumAnalitika;

    @Autowired
    private DnevnoStanjeRacunaRepository repozitorijumDnevnoStanjeRacuna;

    @Autowired
    private BankaRepository repozitorijumBanka;

    @Autowired
    private RTGSService RTGSService;

    @Autowired
    private ClearingService clearingService;

    @Autowired
    private EnvironmentProperties environmentProperties;

    @Autowired
    private Mt102Repository mt102Repository;

    @Autowired
    private PojedinacniNalogZaPlacanjeRepository pojedinacniNalogZaPlacanjeRepository;

    @Override
    public void process(NalogZaPrenos nalogZaPrenos) {
        boolean duznikKodNas = proveriFirmu(nalogZaPrenos.getPodaciOPrenosu().getDuznikUPrenosu().getRacunUcesnika());
        boolean poverilacKodNas = proveriFirmu(nalogZaPrenos.getPodaciOPrenosu().getPoverilacUPrenosu().getRacunUcesnika());

        if(!duznikKodNas) {
            throw new ServiceFaultException("Nije pronadjen", new ServiceFault("404", "Racun duznika nije pronadjen!"));
        }else if(duznikKodNas && poverilacKodNas) {
            unutrasnjiPromet(nalogZaPrenos);
        } else if(duznikKodNas && !poverilacKodNas) {
            medjubankarskiPromet(nalogZaPrenos);
        }
    }

    @Override
    public void send(NalogZaPrenos nalogZaPrenos) {

    }

    private boolean proveriFirmu(String brojRacuna) {

        Racun racun = repozitorijumRacuna.findByBrojRacuna(brojRacuna).get();
        String swiftCode = racun.getBanka().getSWIFTkod();

        if(swiftCode.equals(environmentProperties.getSwiftCode())){
            return true;
        }else {
            return false;
        }
    }

    private void unutrasnjiPromet(NalogZaPrenos nalog) {
        TPrenosUcesnik duznik = nalog.getPodaciOPrenosu().getDuznikUPrenosu();
        TPrenosUcesnik poverilac = nalog.getPodaciOPrenosu().getPoverilacUPrenosu();

        Racun racunDuznika = repozitorijumRacuna.findByBrojRacuna(duznik.getRacunUcesnika()).get();
        Racun racunPoverioca = repozitorijumRacuna.findByBrojRacuna(poverilac.getRacunUcesnika()).get();
        racunDuznika.setSaldo(racunDuznika.getSaldo() - nalog.getPodaciOPrenosu().getIznos().doubleValue());
        racunPoverioca.setSaldo(racunPoverioca.getSaldo() + nalog.getPodaciOPrenosu().getIznos().doubleValue());
        repozitorijumRacuna.save(racunDuznika);
        repozitorijumRacuna.save(racunPoverioca);
        napraviAnalitike(nalog, racunDuznika, racunPoverioca);
    }

    private void medjubankarskiPromet(NalogZaPrenos nalog) {
        TPrenosUcesnik duznik = nalog.getPodaciOPrenosu().getDuznikUPrenosu();
        TPrenosUcesnik poverilac = nalog.getPodaciOPrenosu().getPoverilacUPrenosu();

        Racun racunDuznika = repozitorijumRacuna.findByBrojRacuna(duznik.getRacunUcesnika()).get();
        Racun racunPoverioca = repozitorijumRacuna.findByBrojRacuna(poverilac.getRacunUcesnika()).get();

        if(nalog.isHitno() || nalog.getPodaciOPrenosu().getIznos().doubleValue() >= 250000.00){
            // RTGS
            Mt103 mt103 = createMt103(nalog, racunDuznika, racunPoverioca);
            RTGSService.processMT103(mt103);
        }else{
            // Clearing i settlement
            String swiftBankeDuznika = environmentProperties.getSwiftCode();
            String swiftBankePoverioca = "";
            Optional<Banka> bankaPoverioca = repozitorijumBanka.findById(racunPoverioca.getBanka().getId());
            Banka bankaPoveriocaReal = null;
            if(!bankaPoverioca.isPresent()){
                throw new ServiceFaultException("Nije pronadjen", new ServiceFault("404", "Banka poverioca nije pronadjena!"));
            }else{
                bankaPoveriocaReal = bankaPoverioca.get();
                swiftBankePoverioca = bankaPoveriocaReal.getSWIFTkod();
            }
            //Rezervisi sredstva
            racunDuznika.setRezervisanaSredstva(racunDuznika.getRezervisanaSredstva() + nalog.getPodaciOPrenosu().getIznos().doubleValue());
            racunDuznika.setSaldo(racunDuznika.getSaldo() - nalog.getPodaciOPrenosu().getIznos().doubleValue());

            List<Mt102Model> mt102Model = mt102Repository.findBySwiftBankeDuznikaAndSwiftBankePoveriocaAndPoslato(swiftBankeDuznika, swiftBankePoverioca, false);
            if(mt102Model.isEmpty()){
                System.out.println("pravim novi mt102 model");
                kreirajNoviMt102Model(nalog, racunDuznika, racunPoverioca);
                System.out.println("Prosao kliring PRAZAN");
            }else{
                System.out.println("samo dodajem novi nalog u mt102 model");
                PojedinacniNalogZaPlacanje pojedinacniNalogZaPlacanje = kreiranjeNalogaZaPlacanje(nalog);
                pojedinacniNalogZaPlacanje.setMt102Model(mt102Model.get(0));
                mt102Model.get(0).getListaNalogaZaPlacanje().add(pojedinacniNalogZaPlacanje);
                pojedinacniNalogZaPlacanjeRepository.save(pojedinacniNalogZaPlacanje);
                mt102Repository.save(mt102Model.get(0));
                if(mt102Model.get(0).getListaNalogaZaPlacanje().size() >= 2){
                    Mt102 mt102 = createMt102(mt102Model.get(0));
                    clearingService.sendMT102(mt102);
                }
                System.out.println("Prosao kliring IMA VEC");
            }
       }
    }

    private Mt102 createMt102(Mt102Model mt102Model) {
        Mt102 mt102 = new Mt102();
        Mt102Zaglavlje zaglavlje = new Mt102Zaglavlje();

        TPodaciBanka bankaDuznika = new TPodaciBanka();
        bankaDuznika.setSwiftKod(mt102Model.getSwiftBankeDuznika());
        bankaDuznika.setObracunskiRacun(mt102Model.getRacunBankeDuznika());

        TPodaciBanka bankaPoverioca = new TPodaciBanka();
        bankaPoverioca.setSwiftKod(mt102Model.getSwiftBankePoverioca());
        bankaPoverioca.setObracunskiRacun(mt102Model.getRacunBankePoverioca());

        zaglavlje.setPodaciOBanciDuznika(bankaDuznika);
        zaglavlje.setPodaciOBanciPoverioca(bankaPoverioca);
        zaglavlje.setUkupanIznos(BigDecimal.valueOf(mt102Model.getUkupanIznos()));
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(mt102Model.getDatumValute());
        try {
            zaglavlje.setDatumValute(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        calendar.setTime(mt102Model.getDatumNaloga());
        try {
            zaglavlje.setDatum(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        zaglavlje.setSifraValute(TOznakaValute.fromValue(mt102Model.getSifraValute()));
        zaglavlje.setIdPoruke(mt102Model.getIdPoruke());
        mt102.setMt102Zaglavlje(zaglavlje);

        //Pojedinacni nalozi
        for (PojedinacniNalogZaPlacanje pnzp: mt102Model.getListaNalogaZaPlacanje()){
            Mt102Telo telo = new Mt102Telo();

            TPravnoLice duznik = new TPravnoLice();
            duznik.setNaziv(pnzp.getDuznik());
            duznik.setBrojRacuna(pnzp.getRacunDuznika());
            telo.setPodaciODuzniku(duznik);

            TPravnoLice poverilac = new TPravnoLice();
            poverilac.setNaziv(pnzp.getPoverilac());
            poverilac.setBrojRacuna(pnzp.getRacunPoverioca());
            telo.setPodaciOPoveriocu(poverilac);

            Mt102Telo.Iznos iznos = new Mt102Telo.Iznos();
            iznos.setValue(BigDecimal.valueOf(pnzp.getIznos()));
            iznos.setValuta(TOznakaValute.valueOf(pnzp.getSifraValute()));
            telo.setIznos(iznos);

            TPodaciPlacanje podaciZaduzenje = new TPodaciPlacanje();
            podaciZaduzenje.setModel(BigInteger.valueOf(pnzp.getModelZaduzenja()));
            podaciZaduzenje.setPozivNaBroj(pnzp.getPozivNaBrojZaduzenja());
            telo.setPodaciOZaduzenju(podaciZaduzenje);

            TPodaciPlacanje podaciOdobrenje = new TPodaciPlacanje();
            podaciOdobrenje.setModel(BigInteger.valueOf(pnzp.getModelOdobrenja()));
            podaciOdobrenje.setPozivNaBroj(pnzp.getPozivNaBrojOdobrenja());
            telo.setPodaciOOdobrenju(podaciOdobrenje);

            GregorianCalendar calendarNew = new GregorianCalendar();
            calendarNew.setTime(pnzp.getDatumNaloga());
            try {
                telo.setDatumNaloga(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendarNew));
            } catch (DatatypeConfigurationException e) {
                e.printStackTrace();
            }

            telo.setIdNaloga(pnzp.getIdNaloga());
            telo.setSvrhaPlacanja(pnzp.getSvrhaPlacanja());

            mt102.getMt102Telo().add(telo);
        }

        return mt102;
    }

    private void kreirajNoviMt102Model(NalogZaPrenos nalog, Racun racunDuznika, Racun racunPoverioca) {
        Mt102Model mt102Model = new Mt102Model();
        Optional<Banka> bankaDuznika = repozitorijumBanka.findById(racunDuznika.getBanka().getId());
        Optional<Banka> bankaPoverioca = repozitorijumBanka.findById(racunPoverioca.getBanka().getId());
        if(!bankaPoverioca.isPresent() || !bankaDuznika.isPresent()){
            throw new ServiceFaultException("Nije pronadjen.", new ServiceFault("404", "Banka poverioca ili banka duznika nisu pronadjene!"));
        }

        mt102Model.setIdPoruke(UUID.randomUUID().toString());
        mt102Model.setSwiftBankeDuznika(environmentProperties.getSwiftCode());
        mt102Model.setSwiftBankePoverioca(bankaPoverioca.get().getSWIFTkod());
        mt102Model.setRacunBankeDuznika(bankaDuznika.get().getObracunskiRacun());
        mt102Model.setRacunBankePoverioca(bankaPoverioca.get().getObracunskiRacun());
        mt102Model.setUkupanIznos(mt102Model.getUkupanIznos() + nalog.getPodaciOPrenosu().getIznos().doubleValue());
        mt102Model.setSifraValute(nalog.getPodaciOPrenosu().getOznakaValute().value());
        mt102Model.setDatumValute(nalog.getDatumValute().toGregorianCalendar().getTime());
        mt102Model.setDatumNaloga(nalog.getDatumNaloga().toGregorianCalendar().getTime());
        mt102Model.setPoslato(false);

        List<PojedinacniNalogZaPlacanje> naloziZaPlacanje = new ArrayList<>();
        mt102Model.setListaNalogaZaPlacanje(naloziZaPlacanje);
        //napravi pojedinacni nalog od onog koji dolazi iz firme i kaci ga na model
        PojedinacniNalogZaPlacanje pojedinacniNalogZaPlacanje = kreiranjeNalogaZaPlacanje(nalog);
        mt102Model.getListaNalogaZaPlacanje().add(pojedinacniNalogZaPlacanje);
        mt102Model = mt102Repository.save(mt102Model);
        pojedinacniNalogZaPlacanje.setMt102Model(mt102Model);
        pojedinacniNalogZaPlacanjeRepository.save(pojedinacniNalogZaPlacanje);
    }

    private PojedinacniNalogZaPlacanje kreiranjeNalogaZaPlacanje(NalogZaPrenos nalog) {
        PojedinacniNalogZaPlacanje pnzp = new PojedinacniNalogZaPlacanje();
        pnzp.setIdNaloga(nalog.getIdPoruke());
        pnzp.setDuznik(nalog.getDuznik());
        pnzp.setPoverilac(nalog.getPoverilac());
        pnzp.setSvrhaPlacanja(nalog.getSvrhaPlacanja());
        pnzp.setDatumNaloga(nalog.getDatumNaloga().toGregorianCalendar().getTime());
        pnzp.setRacunDuznika(nalog.getPodaciOPrenosu().getDuznikUPrenosu().getRacunUcesnika());
        pnzp.setRacunPoverioca(nalog.getPodaciOPrenosu().getPoverilacUPrenosu().getRacunUcesnika());
        pnzp.setModelZaduzenja(((int) nalog.getPodaciOPrenosu().getDuznikUPrenosu().getModelPrenosa()));
        pnzp.setModelOdobrenja((int) nalog.getPodaciOPrenosu().getPoverilacUPrenosu().getModelPrenosa());
        pnzp.setPozivNaBrojZaduzenja(nalog.getPodaciOPrenosu().getDuznikUPrenosu().getPozivNaBroj());
        pnzp.setPozivNaBrojOdobrenja(nalog.getPodaciOPrenosu().getPoverilacUPrenosu().getPozivNaBroj());
        pnzp.setIznos(nalog.getPodaciOPrenosu().getIznos().doubleValue());
        pnzp.setSifraValute(nalog.getPodaciOPrenosu().getOznakaValute().value());

        return pnzp;
    }

    private Mt103 createMt103(NalogZaPrenos nalog, Racun racunDuznika, Racun racunPoverioca) {
        Mt103 mt103 = new Mt103();
        mt103.setIdPoruke(UUID.randomUUID().toString());
        Mt103.PodaciODuzniku podaciODuzniku = new Mt103.PodaciODuzniku();
        podaciODuzniku.setNaziv(nalog.getDuznik());
        podaciODuzniku.setBrojRacuna(nalog.getPodaciOPrenosu().getDuznikUPrenosu().getRacunUcesnika());

        Mt103.PodaciOPoveriocu podaciOPoveriocu = new Mt103.PodaciOPoveriocu();
        podaciOPoveriocu.setNaziv(nalog.getPoverilac());
        podaciOPoveriocu.setBrojRacuna(nalog.getPodaciOPrenosu().getPoverilacUPrenosu().getRacunUcesnika());

        Optional<Banka> bankaDuznika = repozitorijumBanka.findById(racunDuznika.getBanka().getId());
        Optional<Banka> bankaPoverioca = repozitorijumBanka.findById(racunPoverioca.getBanka().getId());

        if(!bankaDuznika.isPresent() || !bankaPoverioca.isPresent())
            throw new ServiceFaultException("Nije pronadjen", new ServiceFault("404", "Banka ucesnika prenosa nije pronadjena!"));


        TPodaciBanka podaciBankaDuznika = new TPodaciBanka();
        podaciBankaDuznika.setObracunskiRacun(bankaDuznika.get().getObracunskiRacun());
        podaciBankaDuznika.setSwiftKod(bankaDuznika.get().getSWIFTkod());
        podaciODuzniku.setPodaciOBanci(podaciBankaDuznika);

        mt103.setPodaciODuzniku(podaciODuzniku);

        TPodaciBanka podaciBankaPoverioca = new TPodaciBanka();
        podaciBankaPoverioca.setObracunskiRacun(bankaPoverioca.get().getObracunskiRacun());
        podaciBankaPoverioca.setSwiftKod(bankaPoverioca.get().getSWIFTkod());
        podaciOPoveriocu.setPodaciOBanci(podaciBankaPoverioca);

        mt103.setPodaciOPoveriocu(podaciOPoveriocu);

        Mt103.PodaciOUplati podaciOUplati = new Mt103.PodaciOUplati();
        podaciOUplati.setDatumNaloga(nalog.getDatumNaloga());
        podaciOUplati.setDatumValute(nalog.getDatumValute());
        Mt103.PodaciOUplati.Iznos iznos = new Mt103.PodaciOUplati.Iznos();
        iznos.setValue(nalog.getPodaciOPrenosu().getIznos());
        iznos.setValuta(nalog.getPodaciOPrenosu().getOznakaValute().value());
        podaciOUplati.setIznos(iznos);
        TPodaciPlacanje podaciZaduzenje = new TPodaciPlacanje();
        TPodaciPlacanje podaciOdobrenje = new TPodaciPlacanje();

        podaciZaduzenje.setModel(BigInteger.valueOf(nalog.getPodaciOPrenosu().getDuznikUPrenosu().getModelPrenosa()));
        podaciZaduzenje.setPozivNaBroj(nalog.getPodaciOPrenosu().getDuznikUPrenosu().getPozivNaBroj());

        podaciOdobrenje.setModel(BigInteger.valueOf(nalog.getPodaciOPrenosu().getPoverilacUPrenosu().getModelPrenosa()));
        podaciOdobrenje.setPozivNaBroj(nalog.getPodaciOPrenosu().getPoverilacUPrenosu().getPozivNaBroj());

        podaciOUplati.setPodaciOOdobrenju(podaciOdobrenje);
        podaciOUplati.setPodaciOZaduzenju(podaciZaduzenje);
        podaciOUplati.setSvrhaPlacanja(nalog.getSvrhaPlacanja());


        mt103.setPodaciOUplati(podaciOUplati);

        return mt103;
    }

    private void napraviAnalitike(NalogZaPrenos nalog, Racun racunDuznika, Racun racunPoverioca){
        AnalitikaIzvoda analitikaDuznika = new AnalitikaIzvoda();
        AnalitikaIzvoda analitikaPoverioca = new AnalitikaIzvoda();

        analitikaDuznika.setDatumNaloga(nalog.getDatumNaloga().toGregorianCalendar().getTime());
        analitikaDuznika.setPrimljeno(false);
        analitikaDuznika.setDuznik(nalog.getDuznik());
        analitikaDuznika.setPoverilac(nalog.getPoverilac());
        analitikaDuznika.setDatumValute(nalog.getDatumValute().toGregorianCalendar().getTime());
        analitikaDuznika.setRacunDuznika(nalog.getPodaciOPrenosu().getDuznikUPrenosu().getRacunUcesnika());
        analitikaDuznika.setModelZaduzenja(nalog.getPodaciOPrenosu().getDuznikUPrenosu().getModelPrenosa());
        analitikaDuznika.setPozivNaBrojZaduzenja(nalog.getPodaciOPrenosu().getDuznikUPrenosu().getPozivNaBroj());
        analitikaDuznika.setRacunPoverioca(nalog.getPodaciOPrenosu().getPoverilacUPrenosu().getRacunUcesnika());
        analitikaDuznika.setModelOdobrenja(nalog.getPodaciOPrenosu().getPoverilacUPrenosu().getModelPrenosa());
        analitikaDuznika.setPozivNaBrojZaduzenja(nalog.getPodaciOPrenosu().getDuznikUPrenosu().getPozivNaBroj());
        analitikaDuznika.setPozivNaBrojOdobrenja(nalog.getPodaciOPrenosu().getPoverilacUPrenosu().getPozivNaBroj());
        analitikaDuznika.setIznos(nalog.getPodaciOPrenosu().getIznos());
        analitikaDuznika.setSifraValute(nalog.getPodaciOPrenosu().getOznakaValute().value());
        analitikaDuznika.setSvrhaPlacanja(nalog.getSvrhaPlacanja());
        //analitikaDuznika.setDnevnoStanjeRacuna(repozitorijumDnevnoStanjeRacuna.findByRacun(racunDuznika));

        repozitorijumAnalitika.save(analitikaDuznika);

        analitikaPoverioca.setDatumNaloga(nalog.getDatumNaloga().toGregorianCalendar().getTime());
        analitikaPoverioca.setPrimljeno(true);
        analitikaPoverioca.setDuznik(nalog.getDuznik());
        analitikaPoverioca.setPoverilac(nalog.getPoverilac());
        analitikaPoverioca.setDatumValute(nalog.getDatumValute().toGregorianCalendar().getTime());
        analitikaPoverioca.setRacunDuznika(nalog.getPodaciOPrenosu().getDuznikUPrenosu().getRacunUcesnika());
        analitikaPoverioca.setModelZaduzenja(nalog.getPodaciOPrenosu().getDuznikUPrenosu().getModelPrenosa());
        analitikaPoverioca.setPozivNaBrojZaduzenja(nalog.getPodaciOPrenosu().getDuznikUPrenosu().getPozivNaBroj());
        analitikaPoverioca.setRacunPoverioca(nalog.getPodaciOPrenosu().getPoverilacUPrenosu().getRacunUcesnika());
        analitikaPoverioca.setModelOdobrenja(nalog.getPodaciOPrenosu().getPoverilacUPrenosu().getModelPrenosa());
        analitikaPoverioca.setPozivNaBrojOdobrenja(nalog.getPodaciOPrenosu().getPoverilacUPrenosu().getPozivNaBroj());
        analitikaPoverioca.setIznos(nalog.getPodaciOPrenosu().getIznos());
        analitikaPoverioca.setSifraValute(nalog.getPodaciOPrenosu().getOznakaValute().value());
        analitikaPoverioca.setSvrhaPlacanja(nalog.getSvrhaPlacanja());
        //analitikaPoverioca.setDnevnoStanjeRacuna(repozitorijumDnevnoStanjeRacuna.findByRacun(racunPoverioca));

        repozitorijumAnalitika.save(analitikaPoverioca);

        evidentirajDnevnoStanje(analitikaDuznika, nalog, racunDuznika, true);
        evidentirajDnevnoStanje(analitikaPoverioca, nalog, racunPoverioca, false);

    }

    private void evidentirajDnevnoStanje(AnalitikaIzvoda analitika, NalogZaPrenos nalog, Racun racun, boolean isDuzan){
        //treba evidentirati dnevno stanje duznika

        boolean nasaoDnevnoStanje = false;

        Date datumAnalitike = analitika.getDatumNaloga();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datumAnalitike);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        datumAnalitike = calendar.getTime();

        //uci i pokupiti sva dnevna stanja,
        for (DnevnoStanjeRacuna dsr : racun.getDnevnoStanjeRacuna()) {
            Date tempDatum = dsr.getDatum();
            Calendar tempCal = Calendar.getInstance();
            tempCal.setTime(tempDatum);
            tempCal.set(Calendar.MILLISECOND, 0);
            tempCal.set(Calendar.SECOND, 0);
            tempCal.set(Calendar.MINUTE, 0);
            tempCal.set(Calendar.HOUR, 0);
            tempDatum = tempCal.getTime();

            if (tempDatum.equals(datumAnalitike)){
                //nasao sam dnevno stanje
                dsr.setPredhodnoStanje(dsr.getNovoStanje());
                if(isDuzan) {
                    dsr.setNovoStanje(dsr.getNovoStanje() - nalog.getPodaciOPrenosu().getIznos().doubleValue());
                }else{
                    dsr.setNovoStanje(dsr.getNovoStanje() + nalog.getPodaciOPrenosu().getIznos().doubleValue());
                }

                dsr.getAnalitikeIzvoda().add(analitika);
                analitika.setDnevnoStanjeRacuna(dsr);
                dsr = repozitorijumDnevnoStanjeRacuna.save(dsr);
                racun.getDnevnoStanjeRacuna().add(dsr);
                nasaoDnevnoStanje = true;
                break;
            }
        }

        if(!nasaoDnevnoStanje){
            DnevnoStanjeRacuna dnevnoStanjeRacuna = new DnevnoStanjeRacuna();
            dnevnoStanjeRacuna.setDatum(datumAnalitike);
            dnevnoStanjeRacuna.setRacun(racun);

            if(isDuzan) {
                dnevnoStanjeRacuna.setPredhodnoStanje(racun.getSaldo() + nalog.getPodaciOPrenosu().getIznos().doubleValue());
                dnevnoStanjeRacuna.setNovoStanje(racun.getSaldo());
                dnevnoStanjeRacuna.setPrometNaTeret(1);
            }else{
                dnevnoStanjeRacuna.setPredhodnoStanje(racun.getSaldo() - nalog.getPodaciOPrenosu().getIznos().doubleValue());
                dnevnoStanjeRacuna.setNovoStanje(racun.getSaldo());
                dnevnoStanjeRacuna.setPrometuKorist(1);
            }

            dnevnoStanjeRacuna = repozitorijumDnevnoStanjeRacuna.save(dnevnoStanjeRacuna);
            racun.getDnevnoStanjeRacuna().add(dnevnoStanjeRacuna);
            analitika.setDnevnoStanjeRacuna(dnevnoStanjeRacuna);
        }

        repozitorijumRacuna.save(racun);
        repozitorijumAnalitika.save(analitika);
    }

}
