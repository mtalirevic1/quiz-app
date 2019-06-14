package ba.unsa.etf.rma.klase;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class DBTasks {

    private SQLiteDatabase db;
    private Resources res;

    public DBTasks(SQLiteDatabase db, Resources res) {
        this.db = db;
        this.res = res;
    }

    public void azurirajSveUBazi(){
        azurirajKategorijeBaze();
        azurirajPitanjaBaze();
        azurirajKvizoveBaze();
        azurirajRangListeBaze();
    }


    private void patchKviz(String id, String naziv, String kategorija, String pitanja){
        final String sql="INSERT OR REPLACE INTO "+
                KvizoviDBOpenHelper.KVIZOVI_TABLE +
                "("+ KvizoviDBOpenHelper.KVIZ_ID+
                ", "+ KvizoviDBOpenHelper.KVIZ_NAZIV+
                ", "+KvizoviDBOpenHelper.KVIZ_KATEGORIJA_ID+
                ", "+KvizoviDBOpenHelper.KVIZ_PITANJA+")"+
                "VALUES ("+id+","+naziv+","+kategorija+","+pitanja+");";

        db.execSQL(sql);
    }

    private void patchKategoriju(String id, String naziv){
        final String sql="INSERT OR REPLACE INTO "+
                KvizoviDBOpenHelper.KATEGORIJE_TABLE +
                "("+ KvizoviDBOpenHelper.KATEGORIJA_ID+
                ", "+KvizoviDBOpenHelper.KATEGORIJA_NAZIV+")"+
                "VALUES ("+id+","+naziv+");";

        db.execSQL(sql);
    }

    private void patchPitanje(String naziv, String indeksTacnog, String odgovori){
        final String sql="INSERT OR REPLACE INTO "+
                KvizoviDBOpenHelper.PITANJA_TABLE +
                "("+ KvizoviDBOpenHelper.PITANJE_NAZIV+
                ", "+ KvizoviDBOpenHelper.PITANJE_INDEX_TACNOG+
                ", "+KvizoviDBOpenHelper.PITANJE_ODGOVORI+")"+
                "VALUES ("+naziv+","+indeksTacnog+","+odgovori+");";
        db.execSQL(sql);
    }

    private void patchRanglistu(String id, String nazivKviza, String imeIgraca, String procenatTacnih){
        final String sql="INSERT OR REPLACE INTO "+
                KvizoviDBOpenHelper.RANGLISTE_TABLE +
                "("+ KvizoviDBOpenHelper.RANGLISTA_ID+
                ", "+ KvizoviDBOpenHelper.RANGLISTA_NAZIV_KVIZA+
                ", "+ KvizoviDBOpenHelper.RANGLISTA_IME_IGRACA+
                ", "+KvizoviDBOpenHelper.RANGLISTA_PROCENAT+")"+
                "VALUES ("+id+","+nazivKviza+","+imeIgraca+","+procenatTacnih+");";
        db.execSQL(sql);
    }

    public void azurirajKategorijeBaze() {
        class TaskKat extends BazaTask {

            public TaskKat(String kolekcija, String method, Boolean output, String document, Resources res) {
                super(kolekcija, method, output, document, res);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    String odgovor = this.getOdgovor();

                    JSONObject object = new JSONObject(odgovor);
                    JSONArray documents = object.getJSONArray("documents");


                    for (int i = 0; i < documents.length(); i++) {

                        JSONObject doc = documents.getJSONObject(i);

                        JSONObject fields = new JSONObject(doc.getString("fields"));

                        JSONObject naziv = new JSONObject(fields.getString("naziv"));
                        String naz=naziv.getString("stringValue");

                        JSONObject idIkonice = new JSONObject(fields.getString("idIkonice"));
                        String icon=idIkonice.getString("integerValue");

                        patchKategoriju(icon,convertToSQLString(naz));
                    }
                } catch (JSONException e) {

                }

            }
        }
        TaskKat task = new TaskKat("Kategorije", "GET", false, "", res);
        task.execute();
    }

    public void azurirajKvizoveBaze(){
        class TaskPost extends BazaTask {

            public TaskPost(String kolekcija, String method, Boolean output, String document, Resources res) {
                super(kolekcija, method, output, document, res);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    String odgovor =this.getOdgovor();

                    JSONObject object = new JSONObject(odgovor);
                    JSONArray documents = object.getJSONArray("documents");

                    for (int i = 0; i < documents.length(); i++) {

                        JSONObject doc = documents.getJSONObject(i);
                        JSONObject fields;

                        fields = new JSONObject(doc.getString("fields"));

                        JSONObject naziv = new JSONObject(fields.getString("naziv"));
                        String naz=naziv.getString("stringValue");

                        JSONObject idKategorije = new JSONObject(fields.getString("idKategorije"));
                        String kat = idKategorije.getString("stringValue");

                        JSONObject pit = new JSONObject(fields.getString("pitanja"));
                        JSONObject arrayValue = new JSONObject(pit.getString("arrayValue"));
                        JSONArray values = arrayValue.getJSONArray("values");
                        String[] pitanja=new String[values.length()];
                        for (int j = 0; j < values.length(); j++) {
                            JSONObject pita = values.getJSONObject(j);
                            pitanja[j]=pita.getString("stringValue");
                        }
                        String pitanjaString=arrayToString(pitanja);

                        patchKviz(Integer.valueOf(i).toString(),convertToSQLString(naz),kat,convertToSQLString(pitanjaString));
                    }



                } catch (JSONException e) {

                }
            }

        }

        TaskPost task = new TaskPost("Kvizovi", "GET", false, "", res);
        task.execute();
    }

    public void azurirajPitanjaBaze(){
        class TaskPost extends BazaTask{

            public TaskPost(String kolekcija, String method, Boolean output, String document, Resources res) {
                super(kolekcija, method, output, document, res);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    String odgovor = this.getOdgovor();

                    JSONObject object = new JSONObject(odgovor);
                    JSONArray documents = object.getJSONArray("documents");


                    for (int i = 0; i < documents.length(); i++) {

                        JSONObject doc = documents.getJSONObject(i);

                        JSONObject fields = new JSONObject(doc.getString("fields"));

                        JSONObject naziv = new JSONObject(fields.getString("naziv"));
                        String naz=naziv.getString("stringValue");

                        JSONObject indexTacnog = new JSONObject(fields.getString("indexTacnog"));
                        String indeks=indexTacnog.getString("integerValue");

                        JSONObject odgovori = new JSONObject(fields.getString("odgovori"));
                        JSONObject arrayValue = new JSONObject(odgovori.getString("arrayValue"));
                        JSONArray values = arrayValue.getJSONArray("values");

                        String[] nizOdg=new String[values.length()];
                        for (int j = 0; j < values.length(); j++) {
                            JSONObject odg = values.getJSONObject(j);
                            nizOdg[j]=odg.getString("stringValue");
                        }
                        String stringOdg=arrayToString(nizOdg);

                        patchPitanje(convertToSQLString(naz),indeks,convertToSQLString(stringOdg));
                    }

                }
                catch (JSONException e){

                }
            }
        }

        TaskPost task = new TaskPost("Pitanja", "GET", false, "", res);
        task.execute();
    }

    public void azurirajRangListeBaze(){
        class TaskPost extends BazaTask {

            public TaskPost(String kolekcija, String method, Boolean output, String document, Resources res) {
                super(kolekcija, method, output, document, res);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    String odgovor = this.getOdgovor();
                    JSONObject object = new JSONObject(odgovor);
                    JSONArray documents = object.getJSONArray("documents");
                    HighScore.brojHighScoreova=documents.length();
                    for (int i=0; i < documents.length(); i++) {

                        JSONObject doc = documents.getJSONObject(i);

                        JSONObject fields = new JSONObject(doc.getString("fields"));

                        JSONObject naziv = new JSONObject(fields.getString("nazivKviza"));
                        String name=naziv.getString("stringValue");
                        String naz=name;

                        JSONObject lista= new JSONObject(fields.getString("lista"));
                        JSONObject mapValue=new JSONObject(lista.getString("mapValue"));
                        JSONObject fields2=new JSONObject(mapValue.getString("fields"));
                        JSONObject pozicija= new JSONObject(fields2.getString("pozicija"));
                        JSONObject mapValue2= new JSONObject(pozicija.getString("mapValue"));
                        JSONObject fields3= new JSONObject(mapValue2.getString("fields"));
                        Iterator<String> keys = fields3.keys();
                        String key="-1";
                        String proc="0.00";
                        if(keys.hasNext()){
                            key=(String)keys.next();
                            JSONObject iV=new JSONObject(fields3.getString(key));
                            proc=iV.getString("doubleValue");
                        }
                        patchRanglistu(convertToSQLString(Integer.valueOf(i).toString()),convertToSQLString(naz),convertToSQLString(key),proc);
                    }
                }
                catch (JSONException e){

                }
            }
        }
        TaskPost task = new TaskPost("Rangliste", "GET", false, "", res);
        task.execute();
    }

    public ArrayList<Kviz> ucitajKvizoveBaze(String whereClause, String[] whereArgs){
        ArrayList<Kviz> kvizovi=new ArrayList<>();

        Cursor cursor= db.query(KvizoviDBOpenHelper.KVIZOVI_TABLE,KvizoviDBOpenHelper.KOLONE_REZULTAT_KVIZ,whereClause,whereArgs,null,null,null);
        int indeksId=cursor.getColumnIndexOrThrow(KvizoviDBOpenHelper.KVIZ_ID);
        int indeksNaziv=cursor.getColumnIndexOrThrow(KvizoviDBOpenHelper.KVIZ_NAZIV);
        int indeksKatId=cursor.getColumnIndexOrThrow(KvizoviDBOpenHelper.KVIZ_KATEGORIJA_ID);
        int indeksPit=cursor.getColumnIndexOrThrow(KvizoviDBOpenHelper.KVIZ_PITANJA);

        while(cursor.moveToNext()){
            int katId=cursor.getInt(indeksKatId);
            ArrayList<Kategorija> kategorije=ucitajKategorijeBaze(KvizoviDBOpenHelper.KATEGORIJA_ID+"="+Integer.valueOf(katId).toString(),null);

            String[] niz=stringToArray(cursor.getString(indeksPit));

            StringBuilder where=new StringBuilder();
            int len=niz.length;
            for(int i=0; i<len;i++){
                where.append(KvizoviDBOpenHelper.PITANJE_NAZIV).append("=").append(convertToSQLString(niz[i]));
                if(i!=len-1){
                   where.append(" OR ");
                }
            }

            ArrayList<Pitanje> pitanja=ucitajPitanjaBaze(where.toString(),null);

            kvizovi.add(new Kviz(cursor.getString(indeksNaziv),kategorije.get(0),pitanja));
        }

        cursor.close();

        return kvizovi;
    }

    public ArrayList<Pitanje> ucitajPitanjaBaze(String whereClause, String[] whereArgs){
        ArrayList<Pitanje> pitanja=new ArrayList<>();

        Cursor cursor= db.query(KvizoviDBOpenHelper.PITANJA_TABLE,KvizoviDBOpenHelper.KOLONE_REZULTAT_PITANJE,whereClause,whereArgs,null,null,null);
        int indeksNaziv=cursor.getColumnIndexOrThrow(KvizoviDBOpenHelper.PITANJE_NAZIV);
        int indeksIT=cursor.getColumnIndexOrThrow(KvizoviDBOpenHelper.PITANJE_INDEX_TACNOG);
        int indeksOdg=cursor.getColumnIndexOrThrow(KvizoviDBOpenHelper.PITANJE_ODGOVORI);

        while(cursor.moveToNext()){

            String tacan="";
            int indeksTacnog=cursor.getInt(indeksIT);
            String[] niz=stringToArray(cursor.getString(indeksOdg));
            ArrayList<String> odgovori=new ArrayList<>();
            for(int i=0;i<niz.length;i++){
                if(indeksTacnog==i){
                    tacan=niz[i];
                }
                odgovori.add(niz[i]);
            }
            pitanja.add(new Pitanje(cursor.getString(indeksNaziv),cursor.getString(indeksNaziv),odgovori,tacan));
        }

        cursor.close();

        return pitanja;
    }

    public ArrayList<Kategorija> ucitajKategorijeBaze(String whereClause, String[] whereArgs){
        ArrayList<Kategorija> kategorije=new ArrayList<>();

       Cursor cursor= db.query(KvizoviDBOpenHelper.KATEGORIJE_TABLE,KvizoviDBOpenHelper.KOLONE_REZULTAT_KATEGORIJA,whereClause,whereArgs,null,null,null);
        int indeksId=cursor.getColumnIndexOrThrow(KvizoviDBOpenHelper.KATEGORIJA_ID);
        int indeksNaziv=cursor.getColumnIndexOrThrow(KvizoviDBOpenHelper.KATEGORIJA_NAZIV);

        while(cursor.moveToNext()){
            kategorije.add(new Kategorija(cursor.getString(indeksNaziv),cursor.getString(indeksId)));
        }

        cursor.close();
        return kategorije;
    }



    public ArrayList<HighScore> ucitajRanglisteBaze(String whereClause, String[] whereArgs){
        ArrayList<HighScore> highScores=new ArrayList<>();

        Cursor cursor= db.query(KvizoviDBOpenHelper.RANGLISTE_TABLE,KvizoviDBOpenHelper.KOLONE_REZULTAT_RANGLISTA,whereClause,whereArgs,null,null,null);
        int indeksId=cursor.getColumnIndexOrThrow(KvizoviDBOpenHelper.RANGLISTA_ID);
        int indeksKviz=cursor.getColumnIndexOrThrow(KvizoviDBOpenHelper.RANGLISTA_NAZIV_KVIZA);
        int indeksIgrac=cursor.getColumnIndexOrThrow(KvizoviDBOpenHelper.RANGLISTA_IME_IGRACA);
        int indeksProc=cursor.getColumnIndexOrThrow(KvizoviDBOpenHelper.RANGLISTA_PROCENAT);

        while(cursor.moveToNext()){
            highScores.add(new HighScore(cursor.getDouble(indeksProc),cursor.getString(indeksIgrac),cursor.getString(indeksKviz)));
        }

        cursor.close();

        return highScores;
    }

    private String convertToSQLString(String s){
        return "'"+s+"'";
    }

    private String arrayToString(String[] array) {
        String str = "";
        for (int i = 0; i < array.length; i++) {
            str = str + array[i];
            if (i < array.length - 1) {
                str = str + ",";
            }
        }
        return str;
    }

    private String[] stringToArray(String str) {
        return str.split(",");
    }
}
