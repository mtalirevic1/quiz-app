package ba.unsa.etf.rma.aktivnosti;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

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
    private PlusListAdapter adapterMoguca;
    private ArrayList<Kategorija> kategorije;
    private ArrayList<Pitanje> mogucaPitanja;
    private ArrayList<Pitanje> pitanjaKviza;
    private Boolean novi;

    private Kviz kviz;
    ArrayList<String> odg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodaj_kviz_akt);

        kviz= (Kviz) getIntent().getSerializableExtra("kviz");
        if(kviz.getKategorija()==null){
            novi=true;
            kviz.setPitanja(new ArrayList<Pitanje>());
            kviz.setKategorija(new Kategorija());
        }
        else {
            novi=false;
        }

        etNaziv=(EditText) findViewById(R.id.etNaziv);
        lvDodanaPitanja=(ListView) findViewById(R.id.lvDodanaPitanja);
        lvMogucaPitanja=(ListView) findViewById(R.id.lvMogucaPitanja);
        spKategorije=(Spinner) findViewById(R.id.spKategorije);
        btnDodajKviz=(Button) findViewById(R.id.btnDodajKviz);

        Resources res=getResources();
        kategorije = (ArrayList<Kategorija>) getIntent().getSerializableExtra("kategorije");
        for(int i=0;i<kategorije.size();i++){
            if(kategorije.get(i).getNaziv().equals("Svi")){
                kategorije.remove(i);
                break;
            }
        }
        kategorije.add(new Kategorija("Dodaj kategoriju","-1"));

        mogucaPitanja=new ArrayList<>();
        pitanjaKviza=new ArrayList<>(kviz.getPitanja());


        adapter =new DodajKvizAdapter(this,pitanjaKviza,res);
        adapterMoguca=new PlusListAdapter(this,mogucaPitanja,res);


        adapterSp = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,kategorije);
        adapterSp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKategorije.setAdapter(adapterSp);

        spKategorije.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Kategorija kategorija= kategorije.get(position);
                if(kategorija.getNaziv().equals("Dodaj kategoriju")){
                    //dodaj kategoriju
                    spKategorije.setSelection(0);
                } else{
                    kviz.setKategorija(kategorija);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        lvDodanaPitanja.setAdapter(adapter);
        lvMogucaPitanja.setAdapter(adapterMoguca);

        setData();
    }

    public void onItemClick(int mPosition)
    {
        /*Intent intent=new Intent(KvizoviAkt.this, DodajKvizAkt.class);
        intent.putExtra("kategorije",kategorije);
        if(mPosition!=filtriranaLista.size()-1){
            //updateuj kviz
            Kviz temp = ( Kviz ) filtriranaLista.get(mPosition);
            intent.putExtra("kviz",temp);
        }
        else{
            //dodaj kviz
            intent.putExtra("kviz",new Kviz("",null,null));
        }
        startActivity(intent);*/
    }

    public void onMoguceClick(int mPosition) {

    }

    public void setData(){
        odg=new ArrayList<>();
        odg.add("da"); odg.add("ne");
        mogucaPitanja.add(new Pitanje("P14","Sta je sta",odg,"da"));
        mogucaPitanja.add(new Pitanje("P22","Sta je ovo",odg,"ne"));
        mogucaPitanja.add(new Pitanje("P22","Sta je nesto",odg,"ne"));
    }

}
