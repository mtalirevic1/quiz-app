package ba.unsa.etf.rma.aktivnosti;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.BlueListAdapter;
import ba.unsa.etf.rma.klase.Pitanje;

public class DodajPitanjeAkt extends AppCompatActivity {

    private EditText etNaziv;
    private TextView odgovoriText;
    private ListView lvOdgovori;
    private EditText etOdgovor;
    private Button btnDodajOdgovor;
    private Button btnDodajPitanje;
    private Button btnDodajTacan;

    private ArrayList<String> odgovori;

    private BlueListAdapter adapterListe;

    private String tacan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodaj_pitanje_akt);

        etNaziv = (EditText) findViewById(R.id.etNaziv);
        odgovoriText = (TextView) findViewById(R.id.odgovoriText);
        lvOdgovori = (ListView) findViewById(R.id.lvOdgovori);
        etOdgovor = (EditText) findViewById(R.id.etOdgovor);
        btnDodajOdgovor = (Button) findViewById(R.id.btnDodajOdgovor);
        btnDodajPitanje = (Button) findViewById(R.id.btnDodajPitanje);
        btnDodajTacan = (Button) findViewById(R.id.btnDodajTacan);

        tacan = "";
        odgovori = new ArrayList<>();
        Resources res = getResources();
        adapterListe = new BlueListAdapter(this, odgovori, res);
        lvOdgovori.setAdapter(adapterListe);

        btnDodajOdgovor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etOdgovor.getText().toString();
                if (!s.equals("") && !imaOdgovor(s)) {
                    odgovori.add(s);
                    adapterListe.notifyDataSetChanged();
                }
            }
        });

        btnDodajTacan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etOdgovor.getText().toString();
                if (!s.equals("") && tacan.equals("") && !imaOdgovor(s)) {
                    odgovori.add(s);
                    tacan = s;
                    adapterListe.setTacan(s);
                    adapterListe.notifyDataSetChanged();
                }
            }
        });

        btnDodajPitanje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imaNaziv()) {
                    etNaziv.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    etNaziv.setBackgroundColor(getResources().getColor(R.color.colorLabel1));
                }
                if (!imaOdgovora()) {
                    odgovoriText.setTextColor(getResources().getColor(R.color.red));
                } else {
                    odgovoriText.setTextColor(getResources().getColor(R.color.colorLabel1));
                }

                if (imaOdgovora() && imaNaziv() && !tacan.equals("")) {
                    Intent returnIntent = new Intent();
                    Pitanje pitanje = new Pitanje(etNaziv.getText().toString(), etNaziv.getText().toString(), new ArrayList<String>(odgovori), tacan);
                    returnIntent.putExtra("pitanje", pitanje);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }

    public Boolean imaOdgovora() {
        if (odgovori.size() <= 0) return false;
        return true;
    }

    public Boolean imaOdgovor(String s) {
        for (String odg : odgovori) {
            if (odg.equals(s)) return true;
        }
        return false;
    }

    public Boolean imaNaziv() {
        if (etNaziv.getText().toString().equals("")) return false;
        return true;
    }

    public void onListItemClick(int mPosition) {
        String s = odgovori.get(mPosition);
        if (s.equals(tacan)) {
            tacan = "";
            adapterListe.setTacan("");
        }
        odgovori.remove(mPosition);
        adapterListe.notifyDataSetChanged();
    }
}
