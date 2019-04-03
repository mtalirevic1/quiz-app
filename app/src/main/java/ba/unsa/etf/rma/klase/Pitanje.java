package ba.unsa.etf.rma.klase;

import java.util.ArrayList;
import java.util.Collections;

public class Pitanje {

    private String naziv;
    private String textPitanja;
    private ArrayList<String> odgovori;
    private String tacan;

    public Pitanje() {
        odgovori=new ArrayList<>();
    }

    public Pitanje(String naziv, String textPitanja, ArrayList<String> odgovori, String tacan) {
        this.naziv = naziv;
        this.textPitanja = textPitanja;
        this.odgovori = odgovori;
        this.tacan = tacan;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getTextPitanja() {
        return textPitanja;
    }

    public void setTextPitanja(String textPitanja) {
        this.textPitanja = textPitanja;
    }

    public ArrayList<String> getOdgovori() {
        return odgovori;
    }

    public void setOdgovori(ArrayList<String> odgovori) {
        this.odgovori = odgovori;
    }

    public String getTacan() {
        return tacan;
    }

    public void setTacan(String tacan) {
        this.tacan = tacan;
    }

    ArrayList<String> dajRandomOdgovore(){
        ArrayList<String> random=new ArrayList<>(odgovori);
        Collections.shuffle(random);
        return random;
    }
}
