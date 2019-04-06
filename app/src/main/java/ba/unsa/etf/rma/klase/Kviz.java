package ba.unsa.etf.rma.klase;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Kviz implements Parcelable {

    private String naziv;
    private Kategorija kategorija;
    private ArrayList<Pitanje> pitanja;

    public Kviz(String naziv, Kategorija kategorija, ArrayList<Pitanje> pitanja) {
        this.naziv = naziv;
        this.kategorija = kategorija;
        this.pitanja = pitanja;
    }

    public Kviz() {
        pitanja = new ArrayList<>();
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

    public void dodajPitanje(Pitanje pitanje) {
        pitanja.add(pitanje);
    }

    @Override
    public String toString() {
        return naziv;
    }

    protected Kviz(Parcel in) {
        naziv = in.readString();
        kategorija = (Kategorija) in.readValue(Kategorija.class.getClassLoader());
        if (in.readByte() == 0x01) {
            pitanja = new ArrayList<Pitanje>();
            in.readList(pitanja, Pitanje.class.getClassLoader());
        } else {
            pitanja = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(naziv);
        dest.writeValue(kategorija);
        if (pitanja == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(pitanja);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Kviz> CREATOR = new Parcelable.Creator<Kviz>() {
        @Override
        public Kviz createFromParcel(Parcel in) {
            return new Kviz(in);
        }

        @Override
        public Kviz[] newArray(int size) {
            return new Kviz[size];
        }
    };
}
