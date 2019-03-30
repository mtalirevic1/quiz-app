package ba.unsa.etf.rma.klase;

import java.util.ArrayList;

public class Kviz {

    private String naziv;
    private String kategorija;
    private ArrayList<String> pitanjaKviza;
    private static ArrayList<String> mogucaPitanja=new ArrayList<>();

    public Kviz(String naziv, String kategorija, ArrayList<String> pitanjaKviza) {
        this.naziv = naziv;
        this.kategorija = kategorija;
        this.pitanjaKviza = pitanjaKviza;
    }

    public Kviz(){
        pitanjaKviza=new ArrayList<>();
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getKategorija() {
        return kategorija;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public ArrayList<String> getPitanjaKviza() {
        return pitanjaKviza;
    }

    public void setPitanjaKviza(ArrayList<String> pitanjaKviza) {
        this.pitanjaKviza = pitanjaKviza;
    }

    public static ArrayList<String> getMogucaPitanja() {
        return mogucaPitanja;
    }

    public static void setMogucaPitanja(ArrayList<String> mogucaPitanja) {
        Kviz.mogucaPitanja = mogucaPitanja;
    }

    public static void dodajMogucePitanje(String pitanje ){
        mogucaPitanja.add(pitanje);
    }

    public void dodajPitanjeKviza(String pitanje ){
        pitanjaKviza.add(pitanje);
    }
}
