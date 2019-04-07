package ba.unsa.etf.rma.klase;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;

public class Pitanje implements Parcelable {

    private String naziv;
    private String textPitanja;
    private ArrayList<String> odgovori;
    private String tacan;

    public Pitanje() {
        odgovori = new ArrayList<>();
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

    ArrayList<String> dajRandomOdgovore() {
        ArrayList<String> random = new ArrayList<>(odgovori);
        Collections.shuffle(random);
        return random;
    }

    @Override
    public String toString() {
        return textPitanja;
    }

    protected Pitanje(Parcel in) {
        naziv = in.readString();
        textPitanja = in.readString();
        if (in.readByte() == 0x01) {
            odgovori = new ArrayList<String>();
            in.readList(odgovori, String.class.getClassLoader());
        } else {
            odgovori = null;
        }
        tacan = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(naziv);
        dest.writeString(textPitanja);
        if (odgovori == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(odgovori);
        }
        dest.writeString(tacan);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Pitanje> CREATOR = new Parcelable.Creator<Pitanje>() {
        @Override
        public Pitanje createFromParcel(Parcel in) {
            return new Pitanje(in);
        }

        @Override
        public Pitanje[] newArray(int size) {
            return new Pitanje[size];
        }
    };
}
