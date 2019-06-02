package ba.unsa.etf.rma.aktivnosti;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;


import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.fragmenti.InformacijeFrag;
import ba.unsa.etf.rma.fragmenti.PitanjeFrag;
import ba.unsa.etf.rma.klase.Kviz;

public class IgrajKvizAkt extends AppCompatActivity implements InformacijeFrag.OnFragmentInteractionListener, PitanjeFrag.OnFragmentInteractionListener {

    private Kviz kviz;
    private String pitanjeFragTag;
    private String infoFragTag;
    private String ime;

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
    }

    public void naClick(int position) {
        PitanjeFrag f = (PitanjeFrag) getSupportFragmentManager().findFragmentByTag(pitanjeFragTag);
        f.naClick(position);
    }

    public void zavrsiAkt(){
        setResult(RESULT_OK);
        finish();
    }

    public void unesiHighscore(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unesite ime");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ime = input.getText().toString();
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

    public void prikaziRangListu(){

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
