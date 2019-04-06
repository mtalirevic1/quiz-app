package ba.unsa.etf.rma.aktivnosti;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.DodajKvizAdapter;
import ba.unsa.etf.rma.klase.Kategorija;
import ba.unsa.etf.rma.klase.Kviz;
import ba.unsa.etf.rma.klase.Pitanje;
import ba.unsa.etf.rma.klase.PlusListAdapter;

public class DodajKvizAkt extends AppCompatActivity {
    private ListView lvDodanaPitanja;
    private ListView lvMogucaPitanja;
    private Spinner spKategorije;
    private EditText etNaziv;
    private Button btnDodajKviz;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodaj_kviz_akt);

        Bundle bundle = getIntent().getExtras();
        kviz = (Kviz) bundle.getParcelable("kviz");
        pos = bundle.getInt("p");
        novi = bundle.getBoolean("novi");


        etNaziv = (EditText) findViewById(R.id.etNaziv);
        lvDodanaPitanja = (ListView) findViewById(R.id.lvDodanaPitanja);
        lvMogucaPitanja = (ListView) findViewById(R.id.lvMogucaPitanja);
        spKategorije = (Spinner) findViewById(R.id.spKategorije);
        btnDodajKviz = (Button) findViewById(R.id.btnDodajKviz);
        pitanjaText = (TextView) findViewById(R.id.pitanjaText);

        Resources res = getResources();
        kategorije = bundle.getParcelableArrayList("kategorije");

        for (int i = 0; i < kategorije.size(); i++) {
            if (kategorije.get(i).getNaziv().equals("Svi")) {
                kategorije.remove(i);
                break;
            }
        }

        kategorije.add(new Kategorija("Dodaj kategoriju", "-1"));

        mogucaPitanja = new ArrayList<>();

        pitanjaKviza = new ArrayList<>();
        pitanjaKviza.add(new Pitanje("Dodaj Pitanje", "Dodaj Pitanje", new ArrayList<String>(), ""));
        for (int i = 0; i < kviz.getPitanja().size(); i++) {
            pitanjaKviza.add(pitanjaKviza.size() - 1, kviz.getPitanja().get(i));
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
                    //dodaj kategoriju todo
                    spKategorije.setSelection(0);
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

        if (novi) {
            etNaziv.setText(getString(R.string.etNaziv));
        } else {
            etNaziv.setText(kviz.getNaziv());
        }

        if (!novi) {
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
                if (!imaIme()) {
                    etNaziv.setBackgroundColor(getResources().getColor(R.color.red));
                }
                if (!imaPitanja()) {
                    pitanjaText.setTextColor(getResources().getColor(R.color.red));
                }

                if (imaPitanja() && imaIme()) {
                    kviz.setNaziv(etNaziv.getText().toString());
                    kviz.setKategorija((Kategorija) spKategorije.getSelectedItem());
                    kviz.setPitanja(pitanjaKviza);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("kviz", new Kviz(kviz.getNaziv(), kviz.getKategorija(), kviz.getPitanja()));
                    returnIntent.putExtra("novi", novi);
                    returnIntent.putExtra("p", pos);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

        setData();
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
           /* Intent intent=new Intent(DodajKvizAkt.this, DodajKvizAkt.class);
            intent.putExtra("kategorije",kategorije);
            //DODAJ pitanje todo
            startActivity(intent);*/
        }

    }

    public void onMoguceClick(int mPosition) {
        if (mPosition >= 0 && mPosition < mogucaPitanja.size()) {
            pitanjaKviza.add(pitanjaKviza.size() - 1, mogucaPitanja.get(mPosition));
            mogucaPitanja.remove(mPosition);
            adapterMoguca.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
        }
    }

    public void setData() {
        odg = new ArrayList<>();
        odg.add("da");
        odg.add("ne");
        mogucaPitanja.add(new Pitanje("P14", "Sta je sta", odg, "da"));
        mogucaPitanja.add(new Pitanje("P22", "Sta je ovo", odg, "ne"));
        mogucaPitanja.add(new Pitanje("P23", "Sta je nesto", odg, "ne"));
        adapterMoguca.notifyDataSetChanged();
    }

}
