package ba.unsa.etf.rma.klase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KvizoviDBOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "kvizoviBaza.db" ;

    public static final String KVIZOVI_TABLE = "Kvizovi" ;
    public static final String KVIZ_ID = "id" ;
    public static final String KVIZ_NAZIV = "naziv" ;
    public static final String KVIZ_KATEGORIJA_ID = "kategorija_id" ;
    public static final String KVIZ_PITANJA = "pitanja" ;

    public static final String PITANJA_TABLE = "Pitanja" ;
    public static final String PITANJE_NAZIV = "naziv" ;
    public static final String PITANJE_INDEX_TACNOG = "indeks" ;
    public static final String PITANJE_ODGOVORI = "odgovori" ;

    public static final String KATEGORIJE_TABLE= "Kategorije" ;
    public static final String KATEGORIJA_NAZIV= "naziv" ;
    public static final String KATEGORIJA_ID= "id" ;

    public static final String RANGLISTE_TABLE = "Rangliste" ;
    public static final String RANGLISTA_ID = "id" ;
    public static final String RANGLISTA_NAZIV_KVIZA = "naziv_kviza" ;
    public static final String RANGLISTA_PROCENAT = "procenat" ;
    public static final String RANGLISTA_IME_IGRACA = "ime_igraca" ;

    private static final String CREATE_KVIZOVI = "create table " +
            KVIZOVI_TABLE + " (" + KVIZ_ID +
            " integer primary key, " +
            KVIZ_NAZIV + " text not null, " +
            KVIZ_KATEGORIJA_ID + " integer not null, "+
            KVIZ_PITANJA +" text not null);" ;

    private static final String CREATE_PITANJA="create table " +
            PITANJA_TABLE + " (" + PITANJE_NAZIV +" text primary key, "+
            PITANJE_INDEX_TACNOG + " integer not null, " +
            PITANJE_ODGOVORI + " text not null);";

    private static final String CREATE_KATEGORIJE="create table "+
            KATEGORIJE_TABLE+" ("+KATEGORIJA_ID+" integer primary key, "+
            KATEGORIJA_NAZIV +" text not null);";

    private static final String CREATE_RANGLISTE="create table "+
            RANGLISTE_TABLE +" ("+RANGLISTA_ID+" text primary key, "+ RANGLISTA_NAZIV_KVIZA +" text not null, "+
            RANGLISTA_IME_IGRACA+" text not null, "+
            RANGLISTA_PROCENAT+" real not null);";


    public KvizoviDBOpenHelper (Context context , String name ,
                                SQLiteDatabase.CursorFactory factory , int version ) {
        super ( context , name , factory , version );
    }
    //Poziva se kada ne postoji baza
    @Override
    public void onCreate ( SQLiteDatabase db ) {
        db.execSQL ( CREATE_KVIZOVI );
        db.execSQL ( CREATE_PITANJA );
        db.execSQL ( CREATE_KATEGORIJE );
        db.execSQL ( CREATE_RANGLISTE );
    }

    @Override
    public void onUpgrade ( SQLiteDatabase db , int oldVersion , int newVersion ) {
    }

}
