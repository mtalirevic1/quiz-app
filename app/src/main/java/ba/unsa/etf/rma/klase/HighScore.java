package ba.unsa.etf.rma.klase;

import android.os.Parcel;
import android.os.Parcelable;

public class HighScore implements Parcelable, Comparable {
    private double procenatTacnih;
    private String imeIgraca;
    private String imeKviza;

    public HighScore(double procenatTacnih, String imeIgraca, String imeKviza) {
        this.procenatTacnih = procenatTacnih;
        this.imeIgraca = imeIgraca;
        this.imeKviza = imeKviza;
    }

    public HighScore() {
    }

    public double getProcenatTacnih() {
        return procenatTacnih;
    }

    public void setProcenatTacnih(double procenatTacnih) {
        this.procenatTacnih = procenatTacnih;
    }

    public String getImeIgraca() {
        return imeIgraca;
    }

    public void setImeIgraca(String imeIgraca) {
        this.imeIgraca = imeIgraca;
    }

    public String getImeKviza() {
        return imeKviza;
    }

    public void setImeKviza(String imeKviza) {
        this.imeKviza = imeKviza;
    }



    protected HighScore(Parcel in) {
        procenatTacnih = in.readDouble();
        imeIgraca = in.readString();
        imeKviza = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(procenatTacnih);
        dest.writeString(imeIgraca);
        dest.writeString(imeKviza);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HighScore> CREATOR = new Parcelable.Creator<HighScore>() {
        @Override
        public HighScore createFromParcel(Parcel in) {
            return new HighScore(in);
        }

        @Override
        public HighScore[] newArray(int size) {
            return new HighScore[size];
        }
    };

    @Override
    public int compareTo(Object o) {
        HighScore highScore=(HighScore) o;
        if(highScore.getProcenatTacnih()<this.getProcenatTacnih()){
            return -1;
        }
        return 1;
    }
}
