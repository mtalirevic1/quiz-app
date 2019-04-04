package ba.unsa.etf.rma.aktivnosti;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.DodajKvizAdapter;
import ba.unsa.etf.rma.klase.Pitanje;

public class DodajKvizAkt extends AppCompatActivity {
    private ListView lvDodanaPitanja;
    private ListView lvMogucaPitanja;
    private Spinner spKategorije;
    private EditText etNaziv;
    private Button btnDodajKviz;
    private DodajKvizAdapter adapter;
    private ArrayList<String> kategorije;
    private ArrayList<Pitanje> mogucaPitanja;
    private ArrayList<Pitanje> pitanjaKviza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodaj_kviz_akt);
        etNaziv=(EditText) findViewById(R.id.etNaziv);
        lvDodanaPitanja=(ListView) findViewById(R.id.lvDodanaPitanja);
        lvMogucaPitanja=(ListView) findViewById(R.id.lvMogucaPitanja);
        spKategorije=(Spinner) findViewById(R.id.spKategorije);
        btnDodajKviz=(Button) findViewById(R.id.btnDodajKviz);

        Resources res=getResources();
        mogucaPitanja=new ArrayList<>();
        pitanjaKviza=new ArrayList<>();
        adapter =new DodajKvizAdapter(this,pitanjaKviza,res);
    }
}
