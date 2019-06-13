package ba.unsa.etf.rma.aktivnosti;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.BazaTask;
import ba.unsa.etf.rma.klase.Kategorija;
import ba.unsa.etf.rma.klase.Konekcija;
import ba.unsa.etf.rma.klase.Kviz;
import ba.unsa.etf.rma.klase.KvizoviAktAdapter;
import ba.unsa.etf.rma.klase.KvizoviDBOpenHelper;
import ba.unsa.etf.rma.klase.Pitanje;

public class KvizoviAkt extends AppCompatActivity {

    private Spinner spPostojeceKategorije;
    private ListView lvKvizovi;
    private KvizoviAktAdapter adapter;
    private ArrayList<Kviz> unosi;
    private ArrayList<Kviz> filtriranaLista;
    private ArrayAdapter adapterSp;
    private ArrayList<Kategorija> kategorije;

    private ArrayList<Pitanje> pitanja;
    private ArrayList<String> odgovori;

    private SQLiteDatabase db;
    private KvizoviDBOpenHelper dbOpenHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kvizovi_akt);

        lvKvizovi = (ListView) findViewById(R.id.lvKvizovi);

        kategorije = new ArrayList<>();
        filtriranaLista = new ArrayList<>();
        pitanja = new ArrayList<>();
        filtriranaLista.add(new Kviz("Dodaj Kviz", new Kategorija("Nista", ""), new ArrayList<Pitanje>()));
        spPostojeceKategorije = (Spinner) findViewById(R.id.spPostojeceKategorije);
        adapterSp = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategorije);
        adapterSp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPostojeceKategorije.setAdapter(adapterSp);

        kategorije.add(new Kategorija("Svi", "-1"));
        adapterSp.notifyDataSetChanged();

        Resources res = getResources();
        unosi = new ArrayList<>();

        dbOpenHelper=new KvizoviDBOpenHelper(getApplicationContext(),KvizoviDBOpenHelper.DATABASE_NAME,null,1);
        db=dbOpenHelper.getWritableDatabase();

        ucitajSve();

        adapter = new KvizoviAktAdapter(this, filtriranaLista, res);
        lvKvizovi.setAdapter(adapter);

        spPostojeceKategorije.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Kategorija kategorija = kategorije.get(position);
                if (kategorija.getNaziv().equals("Svi")) {
                    ucitajKvizoveFirestore();
                } else {
                    ucitajKvizoveKategorijeFirestore();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });


        lvKvizovi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                naClick(position);
            }
        });

        lvKvizovi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                onLongItemClick(position);
                return true;
            }
        });
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(internetStatusReceiver,intentFilter);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, 8);
        }
    }

    BroadcastReceiver internetStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = Konekcija.dajStatusKonekcije(context);
            Log.e("NETWORK", intent.getAction());
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                if (status == Konekcija.TYPE_NOT_CONNECTED) {
                    //todo
                    Log.e("NETWORK", "NEMA INTERNETA");
                } else {
                    //todo
                    ucitajSve();
                    Log.e("NETWORK", "IMA INTERNETA");

                }
            }
        }
    };

    public void ucitajSve(){
        if(Konekcija.dajStatusKonekcije(getApplicationContext())!=Konekcija.TYPE_NOT_CONNECTED) {
            try {
                ucitajSveFirestore();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.e("NETWORK", "IZ BAZE UCITATI");
            //todo uraditi citanje
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ucitajSve();
        if (requestCode == 1) {
            Bundle bundle = data.getExtras();

            ArrayList<Kategorija> noveK = bundle.getParcelableArrayList("kategorije");

            for (Kategorija k : noveK) {
                if (!imaKategorija(k) && !k.getNaziv().equals("Dodaj kategoriju")) {
                    kategorije.add(k);
                }
            }
            adapterSp.notifyDataSetChanged();

            if (resultCode == RESULT_OK) {


                Kviz kviz = (Kviz) bundle.getParcelable("kviz");
                Integer pos = bundle.getInt("p");
                Boolean novi = bundle.getBoolean("novi");


                if (!novi) {
                    Kviz k = filtriranaLista.get(pos);
                    unosi.remove(k);
                    filtriranaLista.remove(k);
                }
                unosi.add(kviz);
                Kategorija k = (Kategorija) spPostojeceKategorije.getSelectedItem();
                if (k.getNaziv().equals(kviz.getKategorija().getNaziv()) || k.getNaziv().equals("Svi")) {
                    filtriranaLista.add(filtriranaLista.size() - 1, kviz);
                }
                adapter.notifyDataSetChanged();

            }
            if (resultCode == RESULT_CANCELED) {
                adapter.notifyDataSetChanged();
            }
        } else if (requestCode == 5) {
            if (resultCode == RESULT_OK) {

            }
            if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    public Boolean imaKategorija(Kategorija kategorija) {
        for (Kategorija k : kategorije) {
            if (k.getNaziv().equals(kategorija.getNaziv())) {
                return true;
            }
        }
        return false;
    }


    public void resetujListu(ArrayList<Kviz> lista) {
        for (int i = 0; i < lista.size(); i++) {
            if (!lista.get(i).getNaziv().equals("Dodaj Kviz")) {
                lista.remove(i);
                i--;
            }
        }
    }

    public void onLongItemClick(int mPosition) {
        if (mPosition == filtriranaLista.size() - 1) {
            return;
        }
        if(Konekcija.dajStatusKonekcije(getApplicationContext())==Konekcija.TYPE_NOT_CONNECTED){
            Konekcija.nemaKonekcijeToast(getApplicationContext());
            return;
        }
        Boolean novi = false;
        Kviz kviz = filtriranaLista.get(mPosition);
        Integer pos = mPosition;
        Intent intent = new Intent(KvizoviAkt.this, DodajKvizAkt.class);
        intent.putExtra("kategorije", kategorije);
        intent.putExtra("kviz", kviz);
        novi = false;
        intent.putExtra("novi", novi);
        intent.putExtra("kvizovi", unosi);
        intent.putExtra("p", pos);
        startActivityForResult(intent, 1);

    }

    public void naClick(int mPosition) {

        Integer pos = mPosition;
        Boolean novi = false;
        if (filtriranaLista.size()>1 && mPosition != filtriranaLista.size() - 1) {

            Intent intent = new Intent(KvizoviAkt.this, IgrajKvizAkt.class);
            Kviz kviz = filtriranaLista.get(mPosition);
            ArrayList<Pitanje> pitanja = kviz.getPitanja();
            for (int i = 0; i < pitanja.size(); i++) {
                if (pitanja.get(i).getNaziv().equals("Dodaj Pitanje")) {
                    pitanja.remove(i);
                    i--;
                }
            }
            kviz.setPitanja(pitanja);
            if(!imaEvent(kviz)) {
                intent.putExtra("kviz", kviz);
                startActivityForResult(intent, 5);
            }
        } else {
            //dodaj kviz

            if(Konekcija.dajStatusKonekcije(getApplicationContext())==Konekcija.TYPE_NOT_CONNECTED){
                Konekcija.nemaKonekcijeToast(getApplicationContext());
                return;
            }

            Intent intent = new Intent(KvizoviAkt.this, DodajKvizAkt.class);
            intent.putExtra("kategorije", kategorije);
            intent.putExtra("kviz", new Kviz("", new Kategorija("", ""), new ArrayList<Pitanje>()));
            novi = true;
            intent.putExtra("novi", novi);
            intent.putExtra("kvizovi", unosi);
            intent.putExtra("p", pos);
            startActivityForResult(intent, 1);
        }

    }

    public void ucitajKategorijeFirestore(final Boolean ucitajKvizove) {
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

                        Kategorija kategorija = new Kategorija();

                        JSONObject doc = documents.getJSONObject(i);

                        JSONObject fields = new JSONObject(doc.getString("fields"));

                        JSONObject naziv = new JSONObject(fields.getString("naziv"));
                        kategorija.setNaziv(naziv.getString("stringValue"));

                        JSONObject idIkonice = new JSONObject(fields.getString("idIkonice"));
                        kategorija.setId(idIkonice.getString("integerValue"));
                        kategorije.add(kategorija);

                    }
                    adapterSp.notifyDataSetChanged();
                } catch (JSONException e) {

                }

                if (ucitajKvizove) {
                    if(((Kategorija)spPostojeceKategorije.getSelectedItem()).getNaziv().equals("Svi")) {
                        ucitajKvizoveFirestore();
                    }
                    else{
                        ucitajKvizoveKategorijeFirestore();
                    }
                }
            }
        }
        for (int i = 0; i < kategorije.size(); i++) {
            if (!kategorije.get(i).getNaziv().equals("Svi")) {
                kategorije.remove(i);
                i--;
            }
        }
        TaskKat task = new TaskKat("Kategorije", "GET", false, "", getResources());
        task.execute();
    }

    class TaskPost extends BazaTask {

        public TaskPost(String kolekcija, String method, Boolean output, String document, Resources res) {
            super(kolekcija, method, output, document, res);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                resetujListu(filtriranaLista);
                adapter.notifyDataSetChanged();
                String odgovor = "";
                if (getKolekcija().equals("query")) {
                    odgovor = "{ \"documents\": " + getRezultat() + "}";
                } else {
                    odgovor = this.getOdgovor();
                }

                ArrayList<Kviz> kvizovi = new ArrayList<>();


                JSONObject object = new JSONObject(odgovor);
                JSONArray documents = object.getJSONArray("documents");


                for (int i = 0; i < documents.length(); i++) {

                    Kviz kviz = new Kviz();

                    JSONObject doc = documents.getJSONObject(i);
                    JSONObject dok;
                    JSONObject fields;

                    if (getKolekcija().equals("query")) {
                        dok = new JSONObject(doc.getString("document"));
                        fields = new JSONObject(dok.getString("fields"));
                    } else {
                        fields = new JSONObject(doc.getString("fields"));
                    }

                    JSONObject naziv = new JSONObject(fields.getString("naziv"));
                    kviz.setNaziv(naziv.getString("stringValue"));

                    JSONObject idKategorije = new JSONObject(fields.getString("idKategorije"));
                    String kat = idKategorije.getString("stringValue");
                    Boolean ima = false;
                    for (Kategorija k : kategorije) {
                        if (kat.equals(k.getId())) {
                            ima = true;
                            kviz.setKategorija(k);
                            break;
                        }
                    }
                    if (!ima) {
                        kviz.setKategorija(new Kategorija("Nema kategoriju", "-1"));
                    }


                    JSONObject pit = new JSONObject(fields.getString("pitanja"));
                    JSONObject arrayValue = new JSONObject(pit.getString("arrayValue"));
                    JSONArray values = arrayValue.getJSONArray("values");


                    for (int j = 0; j < values.length(); j++) {
                        JSONObject pita = values.getJSONObject(j);
                        for (Pitanje p : pitanja) {
                            if (pita.getString("stringValue").equals(p.getNaziv())) {
                                kviz.getPitanja().add(p);
                                break;
                            }
                        }
                    }
                    kvizovi.add(kviz);
                }

                for (Kviz k : kvizovi) {
                    filtriranaLista.add(filtriranaLista.size() - 1, k);
                }
                adapter.notifyDataSetChanged();

                if (getKolekcija().equals("Kvizovi")) {
                    unosi.clear();
                    unosi.addAll(kvizovi);
                }

            } catch (JSONException e) {

            }
        }
    }

    public void ucitajKvizoveFirestore() {

        TaskPost task = new TaskPost("Kvizovi", "GET", false, "", getResources());
        task.execute();


    }

    public void ucitajSveFirestore() throws JSONException {

        class TaskPit extends BazaTask {

            public TaskPit(String kolekcija, String method, Boolean output, String document, Resources res) {
                super(kolekcija, method, output, document, res);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    pitanja.clear();
                    String odgovor = this.getOdgovor();

                    JSONObject object = new JSONObject(odgovor);
                    JSONArray documents = object.getJSONArray("documents");


                    for (int i = 0; i < documents.length(); i++) {

                        Pitanje pitanje = new Pitanje();

                        JSONObject doc = documents.getJSONObject(i);

                        JSONObject fields = new JSONObject(doc.getString("fields"));

                        JSONObject naziv = new JSONObject(fields.getString("naziv"));
                        pitanje.setNaziv(naziv.getString("stringValue"));
                        pitanje.setTextPitanja(naziv.getString("stringValue"));

                        JSONObject indexTacnog = new JSONObject(fields.getString("indexTacnog"));
                        Integer index = Integer.parseInt(indexTacnog.getString("integerValue"));

                        JSONObject odgovori = new JSONObject(fields.getString("odgovori"));
                        JSONObject arrayValue = new JSONObject(odgovori.getString("arrayValue"));
                        JSONArray values = arrayValue.getJSONArray("values");


                        for (int j = 0; j < values.length(); j++) {
                            JSONObject odg = values.getJSONObject(j);
                            if (index == j) {
                                pitanje.setTacan(odg.getString("stringValue"));
                            }
                            pitanje.getOdgovori().add(odg.getString("stringValue"));
                        }
                        pitanja.add(pitanje);
                    }

                    ucitajKategorijeFirestore(true);

                } catch (JSONException e) {

                }
            }
        }

        TaskPit task = new TaskPit("Pitanja", "GET", false, "", getResources());
        task.execute();
    }


    public void ucitajKvizoveKategorijeFirestore() {
        try {
            JSONObject jo = new JSONObject();
            JSONArray fields = new JSONArray();

            JSONObject idKat = new JSONObject();
            idKat.put("fieldPath", "idKategorije");

            JSONObject naz = new JSONObject();
            naz.put("fieldPath", "naziv");

            JSONObject pit = new JSONObject();
            pit.put("fieldPath", "pitanja");

            fields.put(naz);
            fields.put(idKat);
            fields.put(pit);

            JSONObject select = new JSONObject();
            select.put("fields", fields);

            JSONArray from = new JSONArray();
            JSONObject col = new JSONObject();
            col.put("collectionId", "Kvizovi");
            from.put(col);

            JSONObject field = new JSONObject();
            JSONObject value = new JSONObject();
            value.put("stringValue", ((Kategorija) spPostojeceKategorije.getSelectedItem()).getId());
            field.put("fieldPath", "idKategorije");
            JSONObject fieldFilter = new JSONObject();
            fieldFilter.put("field", field);
            fieldFilter.put("op", "EQUAL");
            fieldFilter.put("value", value);
            JSONObject where = new JSONObject();
            where.put("fieldFilter", fieldFilter);

            JSONObject structuredQuery = new JSONObject();
            structuredQuery.put("where", where);
            structuredQuery.put("select", select);
            structuredQuery.put("from", from);
            structuredQuery.put("limit", 1000);


            jo.put("structuredQuery", structuredQuery);

            TaskPost task = new TaskPost("query", "POST", true, jo.toString(), getResources());
            task.execute();

        } catch (JSONException e) {

        }
    }

    public boolean imaEvent(Kviz k){

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED){
            return false;
        }

        ContentResolver cr=getContentResolver();
        Cursor cursor = cr.query(Uri.parse("content://com.android.calendar/events"), new String[]{ "calendar_id", "title", "description", "dtstart", "dtend", "eventLocation" }, null, null, null);
        cursor.moveToFirst();
        int count=cursor.getCount();
        for (int i = 0; i < count; i++) {
            Long dtstart= cursor.getLong(cursor.getColumnIndex("dtstart"));
            Long dtend=cursor.getLong(cursor.getColumnIndex("dtend"));
            Long systemTime=System.currentTimeMillis();
            Double d=Integer.valueOf(k.getPitanja().size()).doubleValue();
            Long x=Double.valueOf(Math.ceil(d/2)*60000).longValue();
            if(systemTime+x>dtstart && systemTime<dtstart){
                Integer minuta=Long.valueOf((dtstart-systemTime)/60000).intValue();
                napraviAlert("Upozorenje","Imate događaj u kalendaru za "+ minuta.toString()+" minuta!");
                return true;
            }
            else if(systemTime>=dtstart && systemTime<=dtend){
                napraviAlert("Upozorenje","Imate trenutno aktivan događaj u kalendaru!");
                return true;
            }

            cursor.moveToNext();
        }
        cursor.close();
        return false;
    }


   /* public void azurirajSQLBazu(){
        for(int i=0;i<)
        ContentValues vrijednosti = new ContentValues();
        vrijednosti.put("_id", 1); // the execution is different if _id is 2
        vrijednosti.put("columnA", "valueNEW");

        int id = (int) db.insertWithOnConflict(KvizoviDBOpenHelper.KVIZOVI_TABLE, null, vrijednosti, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            yourdb.update("your_table", vrijednosti, "_id=?", new String[] {"1"});  // number 1 is the _id here, update to variable for your code
        }

    }*/

    public void napraviAlert(String naslov,String poruka){
        new AlertDialog.Builder(this)
                .setTitle(naslov)
                .setMessage(poruka)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

}
