package ba.unsa.etf.rma.aktivnosti;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.Kviz;
import ba.unsa.etf.rma.klase.KvizoviAktAdapter;

public class KvizoviAkt extends AppCompatActivity {

    private Spinner spPostojeceKategorije;
    private ListView lvKvizovi;
    private KvizoviAktAdapter adapter;
    private ArrayList<Kviz> unosi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kvizovi_akt);
        lvKvizovi = (ListView) findViewById(R.id.lvKvizovi);
        spPostojeceKategorije=(Spinner) findViewById(R.id.spPostojeceKategorije);
        Resources res=getResources();
        unosi=new ArrayList<>();
        adapter =new KvizoviAktAdapter(this,unosi,res);
        lvKvizovi.setAdapter(adapter);
        unosi.add(new Kviz("Dodaj Kviz","N/A",null));
        adapter.notifyDataSetChanged();

        unosi.add(0,new Kviz("Kviz 1","Neka",null));
        adapter.notifyDataSetChanged();
    }

    public void onItemClick(int mPosition)
    {
        if(mPosition!=unosi.size()-1){
            //updateuj kviz
            Kviz tempValues = ( Kviz ) unosi.get(mPosition);
            Toast.makeText(this,"Update kviz",Toast.LENGTH_LONG).show();
        }
        else{
            //dodaj kviz
            Toast.makeText(this,"Dodaj kviz",Toast.LENGTH_LONG).show();
        }
    }

    /*public void setListData(ArrayList<Kviz> lista)
    {

        int vel=0;
        if(lista!=null){
            vel=lista.size();
        }
        for (int i = 0; i < vel-1; i++) {
            unosi.add( sched );
            adapter.notifyDataSetChanged();
        }

    }*/
}
