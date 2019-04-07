package ba.unsa.etf.rma.klase;

import android.os.Parcel;
import android.os.Parcelable;

public class Kategorija implements Parcelable {
    private String naziv;
    private String id;

    public Kategorija(String naziv, String id) {
        this.naziv = naziv;
        this.id = id;
    }

    public Kategorija() {

    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return naziv;
    }

    protected Kategorija(Parcel in) {
        naziv = in.readString();
        id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(naziv);
        dest.writeString(id);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Kategorija> CREATOR = new Parcelable.Creator<Kategorija>() {
        @Override
        public Kategorija createFromParcel(Parcel in) {
            return new Kategorija(in);
        }

        @Override
        public Kategorija[] newArray(int size) {
            return new Kategorija[size];
        }
    };
}
