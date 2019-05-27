package ba.unsa.etf.rma.aktivnosti;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParser;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.common.collect.Lists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.DodajKvizAdapter;
import ba.unsa.etf.rma.klase.Kategorija;
import ba.unsa.etf.rma.klase.Kviz;
import ba.unsa.etf.rma.klase.NeispravnaDatotekaException;
import ba.unsa.etf.rma.klase.Pitanje;
import ba.unsa.etf.rma.klase.PlusListAdapter;

public class DodajKvizAkt extends AppCompatActivity  {
    private ListView lvDodanaPitanja;
    private ListView lvMogucaPitanja;
    private Spinner spKategorije;
    private EditText etNaziv;
    private Button btnDodajKviz;
    private Button btnImportKviz;
    private DodajKvizAdapter adapter;
    private ArrayAdapter adapterSp;
    private TextView pitanjaText;
    private PlusListAdapter adapterMoguca;
    private ArrayList<Kategorija> kategorije;
    private ArrayList<Pitanje> mogucaPitanja;
    private ArrayList<Pitanje> pitanjaKviza;
    private Boolean novi;
    private Integer pos;
    private Kviz kviz;
    private ArrayList<String> odg;
    private ArrayList<Kviz> kvizovi;
    private String prvobitno;




    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodaj_kviz_akt);
        Bundle bundle = getIntent().getExtras();
        kviz = (Kviz) bundle.getParcelable("kviz");

        new KreirajDokumentTask().execute();

        pos = bundle.getInt("p");
        novi = bundle.getBoolean("novi");


        if (novi) prvobitno = "";
        else prvobitno = kviz.getNaziv();

        etNaziv = (EditText) findViewById(R.id.etNaziv);
        lvDodanaPitanja = (ListView) findViewById(R.id.lvDodanaPitanja);
        lvMogucaPitanja = (ListView) findViewById(R.id.lvMogucaPitanja);
        spKategorije = (Spinner) findViewById(R.id.spKategorije);
        btnDodajKviz = (Button) findViewById(R.id.btnDodajKviz);
        btnImportKviz = (Button) findViewById(R.id.btnImportKviz);
        pitanjaText = (TextView) findViewById(R.id.pitanjaText);

        Resources res = getResources();
        kvizovi = bundle.getParcelableArrayList("kvizovi");
        kategorije = bundle.getParcelableArrayList("kategorije");

        for (int i = 0; i < kategorije.size(); i++) {
            if (kategorije.get(i).getNaziv().equals("Svi") || kategorije.get(i).getNaziv().equals("Dodaj kategoriju")) {
                kategorije.remove(i);
                break;
            }
        }

        kategorije.add(new Kategorija("Dodaj kategoriju", "-1"));

        mogucaPitanja = new ArrayList<>();

        pitanjaKviza = new ArrayList<>();
        pitanjaKviza.add(new Pitanje("Dodaj Pitanje", "Dodaj Pitanje", new ArrayList<String>(), ""));
        for (int i = 0; i < kviz.getPitanja().size(); i++) {
            if (!kviz.getPitanja().get(i).getNaziv().equals("Dodaj Pitanje")) {
                pitanjaKviza.add(pitanjaKviza.size() - 1, kviz.getPitanja().get(i));
            }
        }


        adapter = new DodajKvizAdapter(this, pitanjaKviza, res);
        adapterMoguca = new PlusListAdapter(this, mogucaPitanja, res);


        adapterSp = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategorije);
        adapterSp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKategorije.setAdapter(adapterSp);

        spKategorije.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Kategorija kategorija = kategorije.get(position);
                if (kategorija.getNaziv().equals("Dodaj kategoriju")) {
                    spKategorije.setSelection(0);
                    Intent intent = new Intent(DodajKvizAkt.this, DodajKategorijuAkt.class);
                    intent.putExtra("kategorije", kategorije);
                    startActivityForResult(intent, 3);

                } else {
                    kviz.setKategorija(kategorija);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
        lvDodanaPitanja.setAdapter(adapter);
        lvMogucaPitanja.setAdapter(adapterMoguca);

        if (!novi) {
            etNaziv.setText(kviz.getNaziv());
            Kategorija k = kviz.getKategorija();
            for (int i = 0; i < kategorije.size(); i++) {
                if (kategorije.get(i).getNaziv().equals(k.getNaziv())) {
                    spKategorije.setSelection(i);
                    break;
                }
            }
        }

        btnDodajKviz.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!imaIme() || (!novi && imaKviz() && !imaPrvobitno()) || (novi && imaKviz())) {
                    etNaziv.setBackgroundColor(getResources().getColor(R.color.crvena));
                } else {
                    etNaziv.setBackgroundColor(getResources().getColor(R.color.colorLabel1));
                }
                if (!imaPitanja()) {
                    pitanjaText.setTextColor(getResources().getColor(R.color.crvena));
                } else {
                    pitanjaText.setTextColor(getResources().getColor(R.color.colorLabel1));
                }

                if ((imaPitanja() && imaIme() && !novi && (!imaKviz() || imaPrvobitno())) || (imaPitanja() && imaIme() && novi && !imaKviz())) {
                    kviz.setNaziv(etNaziv.getText().toString());
                    kviz.setKategorija((Kategorija) spKategorije.getSelectedItem());
                    kviz.setPitanja(pitanjaKviza);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("kviz", new Kviz(kviz.getNaziv(), kviz.getKategorija(), kviz.getPitanja()));
                    returnIntent.putExtra("novi", novi);
                    returnIntent.putExtra("p", pos);
                    returnIntent.putExtra("kategorije", kategorije);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

        btnImportKviz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/*");
                startActivityForResult(intent, 4);

            }

        });

        setData();
    }

    public boolean imaKviz() {
        String s = etNaziv.getText().toString();
        for (Kviz k : kvizovi) {
            if (k.getNaziv().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean imaKviz(String naziv) {
        for (Kviz k : kvizovi) {
            if (k.getNaziv().equals(naziv)) {
                return true;
            }
        }
        return false;
    }


    public boolean imaPrvobitno() {
        if (prvobitno.equals(etNaziv.getText().toString())) {
            return true;
        }
        return false;
    }

    public boolean imaIme() {
        String s = etNaziv.getText().toString();
        if (!s.equals(""))
            return true;
        return false;
    }

    public boolean imaPitanja() {
        if (pitanjaKviza.size() <= 1) return false;
        return true;
    }

    public void onItemClick(int mPosition) {

        if (mPosition != pitanjaKviza.size() - 1) {
            //prebaci u moguce
            mogucaPitanja.add(mogucaPitanja.size() - 1, pitanjaKviza.get(mPosition));
            pitanjaKviza.remove(mPosition);
            adapterMoguca.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
        } else {
            Intent intent = new Intent(DodajKvizAkt.this, DodajPitanjeAkt.class);
            startActivityForResult(intent, 2);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2) {

            if (resultCode == RESULT_OK) {

                Bundle bundle = data.getExtras();
                Pitanje pitanje = bundle.getParcelable("pitanje");
                pitanjaKviza.add(pitanjaKviza.size() - 1, pitanje);
                adapter.notifyDataSetChanged();
            }
            if (resultCode == RESULT_CANCELED) {

            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Integer id = bundle.getInt("ikona");
                String kat = bundle.getString("kategorija");
                kategorije.add(kategorije.size() - 1, new Kategorija(kat, id.toString()));
                spKategorije.setSelection(kategorije.size() - 2);
                adapterSp.notifyDataSetChanged();
            }
            if (resultCode == RESULT_CANCELED) {

            }
        } else if (requestCode == 4) {
            if (resultCode == RESULT_OK) {

                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                    readFile(uri);
                }

            }
            if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    private void readFile(Uri uri) {
      // File file = new File(uri.toString());
        try {


            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                throw new NeispravnaDatotekaException("Datoteka nije pronađena!");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line="";

            String naziv;
            Kategorija kategorija=new Kategorija();
            Integer brojPitanja=0;
            ArrayList<Pitanje> pitanja = new ArrayList<>();

            try {
                line = reader.readLine();
            } catch (IOException e) {
                throw new NeispravnaDatotekaException("Datoteka kviza kojeg importujete nema ispravan format!");

            }
            String[] red = line.split(",");
            if (red.length != 3) {
                throw new NeispravnaDatotekaException("Datoteka kviza kojeg importujete nema ispravan format!");
            }

            if (imaKviz(red[0])) {

                throw new NeispravnaDatotekaException("Kviz kojeg importujete već postoji!");

            }
            naziv=red[0];

            try {
                brojPitanja = Integer.parseInt(red[2]);
            }catch (NumberFormatException e){
                throw new NeispravnaDatotekaException("Datoteka kviza kojeg importujete nema ispravan format!");
            }
            if (brojPitanja <= 0) {

                throw new NeispravnaDatotekaException("Kviz kojeg importujete ima neispravan broj pitanja!");
            }

            kategorija.setNaziv(red[1]);
            kategorija.setId("42");

            for (int i = 0; i < brojPitanja; i++) {
                try {
                    if ((line = reader.readLine()) == null) {
                        throw new NeispravnaDatotekaException("Kviz kojeg imporujete ima neispravan broj pitanja!");
                    }
                } catch (IOException e) {
                    throw new NeispravnaDatotekaException("Kviz kojeg imporujete ima neispravan broj pitanja!");
                }

                red = line.split(",");

                String nazivPitanja=red[0];
                for(Pitanje p: pitanja){
                    if(p.getNaziv().equals(nazivPitanja)){
                        throw new NeispravnaDatotekaException("Kviz nije ispravan postoje dva pitanja sa istim nazivom!");
                    }
                }

                ArrayList<String> odgovori=new ArrayList<>();
                Integer brojOdgovora=0,indeksTacnog=-1;
                try {
                    brojOdgovora = Integer.parseInt(red[1]);
                }catch (NumberFormatException e){
                    throw new NeispravnaDatotekaException("Datoteka kviza kojeg importujete nema ispravan format!");
                }

                if(red.length-3!=brojOdgovora){
                    throw new NeispravnaDatotekaException("Kviz kojeg importujete ima neispravan broj odgovora!");
                }

                try {
                    indeksTacnog = Integer.parseInt(red[2]);
                }catch (NumberFormatException e){
                    throw new NeispravnaDatotekaException("Datoteka kviza kojeg importujete nema ispravan format!");
                }

                if(indeksTacnog<0 || indeksTacnog>=brojOdgovora){
                    throw new NeispravnaDatotekaException("Kviz kojeg importujete ima neispravan index tačnog odgovora!");
                }

                String tacan="";
                for(int j=0; j<brojOdgovora;j++){
                    String odgovor=red[j+3];

                    for(String o: odgovori){
                        if(o.equals(odgovor)){
                            throw new NeispravnaDatotekaException("Kviz kojeg importujete nije ispravan postoji ponavljanje odgovora!");
                        }
                    }
                    odgovori.add(odgovor);
                    if(j==indeksTacnog){
                        tacan=red[j+3];
                    }
                }
                Pitanje p=new Pitanje(nazivPitanja,nazivPitanja,odgovori,tacan);
                pitanja.add(p);
            }

            try {
                inputStream.close();
            } catch (IOException e) {
                throw new NeispravnaDatotekaException("Nije moguće zatvoriti datoteku!");
            }

            Boolean imaKategorija=false;
            for (int i = 0; i < kategorije.size(); i++) {
                if (kategorije.get(i).getNaziv().equals(kategorija.getNaziv())) {
                    spKategorije.setSelection(i);
                    imaKategorija=true;
                    break;
                }
            }
            if(!imaKategorija){
                kategorije.add(kategorije.size()-1,kategorija);
                spKategorije.setSelection(kategorije.size()-2);
                adapterSp.notifyDataSetChanged();
            }

            etNaziv.setText(naziv);
            for(int i=0;i<pitanjaKviza.size();i++){
                if(!pitanjaKviza.get(i).getNaziv().equals("Dodaj Pitanje")){
                    pitanjaKviza.remove(i);
                    i--;
                }
            }
            for(Pitanje p: pitanja){
                pitanjaKviza.add(pitanjaKviza.size()-1,p);
            }

            adapter.notifyDataSetChanged();

        } catch (NeispravnaDatotekaException e) {
            new AlertDialog.Builder(this)
                    .setTitle("Greška")
                    .setMessage(e.getMessage())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
    }


    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("kategorije", kategorije);
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }

    public void onMoguceClick(int mPosition) {
        if (mPosition >= 0 && mPosition < mogucaPitanja.size()) {
            pitanjaKviza.add(pitanjaKviza.size() - 1, mogucaPitanja.get(mPosition));
            mogucaPitanja.remove(mPosition);
            adapterMoguca.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
        }
    }

    public class KreirajDokumentTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            dodajKvizFirestore(kviz);
            return null;
        }
    }

    private JSONObject pozoviBazu(String kolekcija, String method,Boolean output, String document){
        GoogleCredential credentials;
        try{
            InputStream stream=getResources().openRawResource(R.raw.secret);
            credentials=GoogleCredential.fromStream(stream).createScoped(Lists.newArrayList("https://www.googleapis.com/auth/datastore"));
            credentials.refreshToken();
            String TOKEN=credentials.getAccessToken();

            String urlString = "https://firestore.googleapis.com/v1/projects/rmaprojekat-17749/databases/(default)/documents/"+kolekcija+"?access_token=";
            URL url=new URL(urlString + URLEncoder.encode(TOKEN,"utf-8"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setRequestMethod("POST");
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(output);


            if(output) {

              /*  String doc = "{\n" +
                        "  \"fields\":{\n" +
                        "    \"idKategorije\":{\n" +
                        "      \"stringValue\":\"kategorija1\"\n" +
                        "    },\n" +
                        "    \"naziv\":{\n" +
                        "      \"stringValue\":\"kviz1\"\n" +
                        "      \n" +
                        "    },\n" +
                        "    \"pitanja\":{\n" +
                        "      \"arrayValue\":{\n" +
                        "        \"values\":[\n" +
                        "          {\"stringValue\":\"id1\"},\n" +
                        "          {\"stringValue\":\"id2\"}\n" +
                        "        ]\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}";*/

                try(OutputStream os=conn.getOutputStream()){
                    byte[] input = document.getBytes("utf-8");
                    os.write(input,0,input.length);
                }
            }
                /*JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.getJSONObject(doc);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

              /*  try(OutputStream os=conn.getOutputStream()){
                    byte[] input = doc.getBytes("utf-8");
                    os.write(input,0,input.length);
                }*/

            int code=conn.getResponseCode();

            InputStream is=conn.getInputStream();
            try(BufferedReader br=new BufferedReader(new InputStreamReader(is,"utf-8"))){
                StringBuilder response=new StringBuilder();
                String responseLine=null;
                while((responseLine=br.readLine())!=null){
                    response.append(responseLine.trim());
                }
                Log.d("ODGOVOR",response.toString());


            }

        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void dodajKvizFirestore(Kviz kviz){
        try {
        JSONObject jo=new JSONObject();

        JSONObject fields=new JSONObject();

        JSONObject kategorija=new JSONObject();
        kategorija.put("stringValue",kviz.getKategorija());

        JSONObject naziv=new JSONObject();
        naziv.put("stringValue",kviz.getNaziv());

        fields.put("idKategorije",kategorija);
        fields.put("naziv",naziv);

        JSONObject arrayValue=new JSONObject();

        JSONArray values=new JSONArray();
        JSONObject pit=new JSONObject();
        for(Pitanje p: kviz.getPitanja()){
            pit.put("stringValue",p.getNaziv());
        }
        values.put(pit);
        arrayValue.put("values",values);

        JSONObject pitanja=new JSONObject();
        pitanja.put("arrayValue",arrayValue);

        fields.put("pitanja",pitanja);

        jo.put("fields",fields);

         pozoviBazu("Kvizovi/3","PATCH",true,jo.toString());
             Log.d("KVIZ",jo.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public void dodajMogucePitanjeFirestore(Pitanje pitanje){

    }

    public void dodajPitanjeFirestore(Pitanje pitanje){

    }

    private void setData() {
        odg = new ArrayList<>();
        odg.add("da");
        odg.add("ne");
        mogucaPitanja.add(new Pitanje("P14", "Sta je sta", odg, "da"));
        mogucaPitanja.add(new Pitanje("P22", "Sta je ovo", odg, "ne"));
        mogucaPitanja.add(new Pitanje("P23", "Sta je nesto", odg, "ne"));
        adapterMoguca.notifyDataSetChanged();
    }

}
