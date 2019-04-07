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
import android.widget.Toast;

import java.util.ArrayList;

import ba.unsa.etf.rma.R;
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

        setData();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                Bundle bundle = data.getExtras();
                Kviz kviz = (Kviz) bundle.getParcelable("kviz");
                Integer pos = bundle.getInt("p");
                Boolean novi = bundle.getBoolean("novi");

                Kategorija kategorija=kviz.getKategorija();

                if(!imaKategorija(kategorija)){
                    kategorije.add(kategorija);
                }

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

            }
        }
    }

    public Boolean imaKategorija(Kategorija kategorija){
        for(Kategorija k: kategorije){
            if(k.getNaziv().equals(kategorija.getNaziv())){
                return true;
            }
        }
        return false;
    }


    public void resetujListu(ArrayList<Kviz> lista) {
        int i = 0;
        while (i != lista.size() - 1) {
            lista.remove(i);
        }
    }

    public void onItemClick(int mPosition) {
        Intent intent = new Intent(KvizoviAkt.this, DodajKvizAkt.class);

        intent.putExtra("kategorije", kategorije);
        Integer pos = mPosition;
        Boolean novi = false;
        intent.putExtra("p", pos);
        if (mPosition != filtriranaLista.size() - 1) {
            //updateuj kviz
            Kviz temp = (Kviz) filtriranaLista.get(mPosition);
            intent.putExtra("kviz", new Kviz(temp.getNaziv(), temp.getKategorija(), temp.getPitanja()));
            novi = false;
        } else {
            //dodaj kviz
            intent.putExtra("kviz", new Kviz("", new Kategorija("", ""), new ArrayList<Pitanje>()));
            novi = true;
        }
        intent.putExtra("novi", novi);
        startActivityForResult(intent, 1);
    }

    public void setData() {
        odgovori = new ArrayList<>();
        odgovori.add("da");
        odgovori.add("ne");

        pitanja = new ArrayList<>();
        pitanja.add(new Pitanje("P1", "Sta je sta", odgovori, "da"));
        pitanja.add(new Pitanje("P2", "Sta je nesto", odgovori, "ne"));
        pitanja.add(new Pitanje("P3", "Sta je ista", odgovori, "da"));


        filtriranaLista.add(new Kviz("Dodaj Kviz", new Kategorija("Nista", ""), new ArrayList<Pitanje>(pitanja)));

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
