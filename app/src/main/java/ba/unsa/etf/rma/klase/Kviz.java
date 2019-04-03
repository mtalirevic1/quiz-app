package ba.unsa.etf.rma.klase;

import java.util.ArrayList;

public class Kviz {

    private String naziv;
    private Kategorija kategorija;
    private ArrayList<Pitanje> pitanja;

    public Kviz(String naziv, Kategorija kategorija, ArrayList<Pitanje> pitanja) {
        this.naziv = naziv;
        this.kategorija = kategorija;
        this.pitanja = pitanja;
    }

    public Kviz(){
        pitanja=new ArrayList<>();
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Kategorija getKategorija() {
        return kategorija;
    }

    public void setKategorija(Kategorija kategorija) {
        this.kategorija = kategorija;
    }

    public ArrayList<Pitanje> getPitanja() {
        return pitanja;
    }

    public void setPitanja(ArrayList<Pitanje> pitanja) {
        this.pitanja = pitanja;
    }
}
