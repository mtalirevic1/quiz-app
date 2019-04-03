package ba.unsa.etf.rma.aktivnosti;

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

public class KvizoviAkt extends AppCompatActivity {

    private Spinner spPostojeceKategorije;
    private ListView lvKvizovi;
    private KvizoviAktAdapter adapter;
    private ArrayList<Kviz> unosi;
    private ArrayList<Kviz> filtriranaLista;
    private ArrayAdapter<String> adapterSp;
    private ArrayList<String> kategorije;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kvizovi_akt);

        lvKvizovi = (ListView) findViewById(R.id.lvKvizovi);

        kategorije=new ArrayList<>();
        filtriranaLista=new ArrayList<>();
        spPostojeceKategorije=(Spinner) findViewById(R.id.spPostojeceKategorije);
        adapterSp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,kategorije);
        adapterSp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPostojeceKategorije.setAdapter(adapterSp);

        kategorije.add("Svi");
        adapterSp.notifyDataSetChanged();

        Resources res=getResources();
        unosi=new ArrayList<>();
        adapter =new KvizoviAktAdapter(this,filtriranaLista,res);

        spPostojeceKategorije.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String kategorija= kategorije.get(position);
                resetujListu(filtriranaLista);
                if(kategorija.equals("Svi")){
                    for(Kviz k: unosi){
                        filtriranaLista.add(filtriranaLista.size()-1,k);
                        adapter.notifyDataSetChanged();
                    }
                } else{
                    for(Kviz k: unosi){
                        if(k.getKategorija().getNaziv().equals(kategorija)){
                            filtriranaLista.add(filtriranaLista.size()-1,k);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });

        lvKvizovi.setAdapter(adapter);
        filtriranaLista.add(new Kviz("Dodaj Kviz",new Kategorija("Nista","0"),null));
        adapter.notifyDataSetChanged();

        unosi.add(0,new Kviz("Kviz 1",new Kategorija("Neka","1"),null));
        filtriranaLista.add(0,new Kviz("Kviz 1",new Kategorija("Neka","1"),null));
        adapter.notifyDataSetChanged();

        unosi.add(0,new Kviz("Kviz 2",new Kategorija("Neka2","1"),null));
        filtriranaLista.add(0,new Kviz("Kviz 2",new Kategorija("Neka2","1"),null));
        adapter.notifyDataSetChanged();

        unosi.add(0,new Kviz("Kviz 3",new Kategorija("Neka3","1"),null));
        filtriranaLista.add(0,new Kviz("Kviz 3",new Kategorija("Neka3","1"),null));
        adapter.notifyDataSetChanged();

        kategorije.add("Neka");
        kategorije.add("Neka2");
        kategorije.add("Neka3");
        adapterSp.notifyDataSetChanged();
    }

    public void resetujListu(ArrayList<Kviz> lista)
    {
        int i=0;
        while(i!=lista.size()-1){
            lista.remove(i);
        }
    }

    public void onItemClick(int mPosition)
    {
        if(mPosition!=filtriranaLista.size()-1){
            //updateuj kviz
            Kviz tempValues = ( Kviz ) filtriranaLista.get(mPosition);
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
