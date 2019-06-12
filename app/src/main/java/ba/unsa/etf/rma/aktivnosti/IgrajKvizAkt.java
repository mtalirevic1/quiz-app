package ba.unsa.etf.rma.aktivnosti;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.AlarmClock;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.fragmenti.InformacijeFrag;
import ba.unsa.etf.rma.fragmenti.PitanjeFrag;
import ba.unsa.etf.rma.fragmenti.RangLista;
import ba.unsa.etf.rma.klase.BazaTask;
import ba.unsa.etf.rma.klase.HighScore;
import ba.unsa.etf.rma.klase.Kviz;

public class IgrajKvizAkt extends AppCompatActivity implements InformacijeFrag.OnFragmentInteractionListener, PitanjeFrag.OnFragmentInteractionListener, RangLista.OnFragmentInteractionListener {

    private Kviz kviz;
    private String pitanjeFragTag;
    private String infoFragTag;
    private String ime;
    private ArrayList<HighScore> highScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.igraj_kviz_akt);
        Bundle bundle = getIntent().getExtras();
        kviz = (Kviz) bundle.getParcelable("kviz");
        InformacijeFrag informacijeFrag =InformacijeFrag.newInstance(kviz);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.informacijePlace, informacijeFrag,"infoFrag");
        transaction.addToBackStack(null);
        transaction.commit();

        PitanjeFrag pitanjeFrag = PitanjeFrag.newInstance(kviz);
        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
        transaction2.replace(R.id.pitanjePlace, pitanjeFrag,"pitFrag");
        transaction2.addToBackStack(null);
        transaction2.commit();
        ime="";
        pitanjeFragTag=pitanjeFrag.getTag();
        infoFragTag=informacijeFrag.getTag();
        highScores=new ArrayList<>();
        setAlarm(kviz);
    }

    public void naClick(int position) {
        PitanjeFrag f = (PitanjeFrag) getSupportFragmentManager().findFragmentByTag(pitanjeFragTag);
        f.naClick(position);
    }

    public void zavrsiAkt(){
        setResult(RESULT_OK);
        finish();
    }

    public void unesiHighscore(final double procenat){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unesite ime");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ime = input.getText().toString();
                ucitajRangListu(ime,kviz.getNaziv(),procenat);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    public void ucitajRangListu(final String imeIgraca,final String nazivKviza,final double procenat){
        class TaskPost extends BazaTask {

            public TaskPost(String kolekcija, String method, Boolean output, String document, Resources res) {
                super(kolekcija, method, output, document, res);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    String odgovor = this.getOdgovor();

                   highScores.clear();


                    JSONObject object = new JSONObject(odgovor);
                    JSONArray documents = object.getJSONArray("documents");


                    for (int i=0; i < documents.length(); i++) {

                        JSONObject doc = documents.getJSONObject(i);

                        JSONObject fields = new JSONObject(doc.getString("fields"));

                        JSONObject naziv = new JSONObject(fields.getString("nazivKviza"));
                        String name=naziv.getString("stringValue");
                        if(!name.equals(kviz.getNaziv())) continue;

                        HighScore highScore=new HighScore();
                        highScore.setImeKviza(name);

                        JSONObject lista= new JSONObject(fields.getString("lista"));
                        JSONObject mapValue=new JSONObject(lista.getString("mapValue"));
                        JSONObject fields2=new JSONObject(mapValue.getString("fields"));
                        JSONObject pozicija= new JSONObject(fields2.getString("pozicija"));
                        JSONObject mapValue2= new JSONObject(pozicija.getString("mapValue"));
                        JSONObject fields3= new JSONObject(mapValue2.getString("fields"));
                        Iterator<String> keys = fields3.keys();
                        if(keys.hasNext()){
                            String key=(String)keys.next();
                            highScore.setImeIgraca(key);
                            JSONObject iV=new JSONObject(fields3.getString(key));
                            highScore.setProcenatTacnih(Double.parseDouble(iV.getString("doubleValue")));
                        }
                        highScores.add(highScore);
                    }
                    HighScore hs=new HighScore(procenat*100,imeIgraca,nazivKviza);
                    highScores.add(hs);
                    RangLista rangLista=RangLista.newInstance(highScores);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.pitanjePlace, rangLista,"rangLista");
                    transaction.addToBackStack(null);
                    transaction.commit();
                    azurirajRangListe(hs);
                }
                catch (JSONException e){

                }
            }
        }
        TaskPost task = new TaskPost("Rangliste", "GET", false, "", getResources());
        task.execute();
    }

    public void azurirajRangListe(HighScore highScore){
        try {
            JSONObject jo = new JSONObject();
            JSONObject fields = new JSONObject();
            JSONObject fields2 = new JSONObject();
            JSONObject fields3 = new JSONObject();
            JSONObject mapValue = new JSONObject();
            JSONObject mapValue2 = new JSONObject();
            JSONObject pozicija = new JSONObject();
            JSONObject lista = new JSONObject();
            JSONObject nazivKviza = new JSONObject();

            JSONObject imeIgraca = new JSONObject();
            imeIgraca.put("doubleValue", highScore.getProcenatTacnih() + "");
            fields3.put(highScore.getImeIgraca(),imeIgraca);
            mapValue2.put("fields",fields3);
            pozicija.put("mapValue",mapValue2);
            fields2.put("pozicija",pozicija);
            mapValue.put("fields",fields2);
            lista.put("mapValue",mapValue);
            fields.put("lista",lista);
            nazivKviza.put("stringValue",kviz.getNaziv());
            fields.put("nazivKviza",nazivKviza);
            jo.put("fields",fields);
            new BazaTask("Rangliste","POST",true,jo.toString(),getResources()).execute();

        }catch (JSONException e){

        }

    }

    public void setAlarm(Kviz k){

        Double minuta= Math.ceil(Integer.valueOf(k.getPitanja().size()).doubleValue()/2);
        Integer sati = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int min=Calendar.getInstance().get(Calendar.MINUTE);
        minuta+=min;
        while(minuta>=60){
            minuta-=60;
            sati++;
        }
        while(sati>=24){
            sati-=24;
        }
        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        i.putExtra(AlarmClock.EXTRA_HOUR,sati);
        i.putExtra(AlarmClock.EXTRA_MINUTES,minuta.intValue());
        i.putExtra(AlarmClock.EXTRA_MESSAGE, "No quiz lasts forever!");
        startActivity(i);
    }

    public void removeAlarm(){
        AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), this.getClass());
        PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        aManager.cancel(pIntent);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
