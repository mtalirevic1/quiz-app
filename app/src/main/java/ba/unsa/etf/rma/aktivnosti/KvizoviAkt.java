package ba.unsa.etf.rma.aktivnosti;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import ba.unsa.etf.rma.klase.Kviz;
import ba.unsa.etf.rma.klase.KvizoviAktAdapter;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.kvizovi_akt);

        lvKvizovi = (ListView) findViewById(R.id.lvKvizovi);

        kategorije = new ArrayList<>();
        filtriranaLista = new ArrayList<>();
        pitanja = new ArrayList<>();
        filtriranaLista.add(new Kviz("Dodaj Kviz", new Kategorija("Nista", ""), new ArrayList<Pitanje>(pitanja)));
        spPostojeceKategorije = (Spinner) findViewById(R.id.spPostojeceKategorije);
        adapterSp = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategorije);
        adapterSp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPostojeceKategorije.setAdapter(adapterSp);

        kategorije.add(new Kategorija("Svi", "-1"));
        adapterSp.notifyDataSetChanged();

        Resources res = getResources();
        unosi = new ArrayList<>();
        adapter = new KvizoviAktAdapter(this, filtriranaLista, res);

        spPostojeceKategorije.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Kategorija kategorija = kategorije.get(position);
                resetujListu(filtriranaLista);
                if (kategorija.getNaziv().equals("Svi")) {
                    for (Kviz k : unosi) {
                        filtriranaLista.add(filtriranaLista.size() - 1, k);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    for (Kviz k : unosi) {
                        if (k.getKategorija().getNaziv().equals(kategorija.getNaziv())) {
                            filtriranaLista.add(filtriranaLista.size() - 1, k);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
        lvKvizovi.setAdapter(adapter);

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

        setData();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
       /* while (lista.size()>1) {
            lista.remove(0);
        }*/

        for (int i = 0; i < lista.size(); i++) {
            if (!lista.get(i).getNaziv().equals("Dodaj Kviz")) {
                lista.remove(i);
                i--;
            }
        }
    }

    public void onLongItemClick(int mPosition) {
        //todo vezi sa IgrajKvizAkt
        if (mPosition == filtriranaLista.size() - 1) {
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
        if (mPosition != filtriranaLista.size() - 1) {
          /*  //updateuj kviz
            Kviz temp = (Kviz) filtriranaLista.get(mPosition);
            intent.putExtra("kviz", new Kviz(temp.getNaziv(), temp.getKategorija(), temp.getPitanja()));
            novi = false;*/
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
            intent.putExtra("kviz", kviz);
            startActivityForResult(intent, 5);
        } else {
            //dodaj kviz
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

    public void ucitajKvizoveFirestore() {
        try {
            JSONObject jo = new JSONObject();
            JSONArray fields = new JSONArray();

            JSONObject idKat = new JSONObject();
            idKat.put("fieldPath", "idKategorije");

            JSONObject naz = new JSONObject();
            naz.put("fieldPath", "naziv");

            JSONObject pit = new JSONObject();
            naz.put("fieldPath", "pitanja");

            fields.put(naz);
            fields.put(idKat);
            fields.put(pit);

            JSONObject select = new JSONObject();
            select.put("fields", fields);

            JSONArray from = new JSONArray();
            JSONObject col = new JSONObject();
            col.put("collectionId", "Kvizovi");
            from.put(col);

            JSONObject structuredQuery = new JSONObject();
            structuredQuery.put("select", select);
            structuredQuery.put("limit", 1000);
            structuredQuery.put("from", from);

            jo.put("structuredQuery", structuredQuery);
            TaskPost task = new TaskPost("query", "POST", false, jo.toString(), getResources());
            task.execute();
        } catch (JSONException e) {

        }
    }

    public void ucitajKvizoveKategorijeFirestore() {

    }

    class TaskPost extends BazaTask {

        public TaskPost(String kolekcija, String method, Boolean output, String document, Resources res) {
            super(kolekcija, method, output, document, res);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                String odgovor = this.getOdgovor();

                ArrayList<Kviz> kvizovi = new ArrayList<>();


                JSONObject object = new JSONObject(odgovor);
                JSONArray documents = object.getJSONArray("documents");


                for (int i = 0; i < documents.length(); i++) {

                    Kviz kviz = new Kviz();

                    JSONObject doc = documents.getJSONObject(i);

                    JSONObject fields = new JSONObject(doc.getString("fields"));

                    JSONObject naziv = new JSONObject(fields.getString("naziv"));
                    kviz.setNaziv(naziv.getString("stringValue"));

                    JSONObject idKategorije = new JSONObject(fields.getString("idKategorije"));
                    String kat = idKategorije.getString("stringValue");
                    for (Kategorija k : kategorije) {
                        if (kat.equals(k.getId())) {
                            kviz.setKategorija(k);
                            break;
                        }
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

                for(Kviz k: kvizovi){
                    filtriranaLista.add(filtriranaLista.size()-1,k);
                }

            } catch (JSONException e) {

            }
        }
    }


    public void setData() {
        odgovori = new ArrayList<>();
        odgovori.add("da");
        odgovori.add("ne");

        pitanja = new ArrayList<>();
        pitanja.add(new Pitanje("P1", "Sta je sta", odgovori, "da"));
        pitanja.add(new Pitanje("P2", "Sta je nesto", odgovori, "ne"));
        pitanja.add(new Pitanje("P3", "Sta je ista", odgovori, "da"));


        unosi.add(0, new Kviz("Kviz 1", new Kategorija("Neka", ""), new ArrayList<Pitanje>(pitanja)));
        filtriranaLista.add(0, new Kviz("Kviz 1", new Kategorija("Neka", ""), new ArrayList<Pitanje>(pitanja)));

        unosi.add(0, new Kviz("Kviz 2", new Kategorija("Neka2", ""), new ArrayList<Pitanje>(pitanja)));
        filtriranaLista.add(0, new Kviz("Kviz 2", new Kategorija("Neka2", ""), new ArrayList<Pitanje>(pitanja)));

        unosi.add(0, new Kviz("Kviz 3", new Kategorija("Neka3", ""), new ArrayList<Pitanje>(pitanja)));
        filtriranaLista.add(0, new Kviz("Kviz 3", new Kategorija("Neka3", ""), new ArrayList<Pitanje>(pitanja)));

        kategorije.add(new Kategorija("Neka", "Neka"));
        kategorije.add(new Kategorija("Neka2", "Neka2"));
        kategorije.add(new Kategorija("Neka3", "Neka3"));

        adapter.notifyDataSetChanged();
        adapterSp.notifyDataSetChanged();

    }
}
