package ba.unsa.etf.rma.aktivnosti;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.fragmenti.InformacijeFrag;
import ba.unsa.etf.rma.fragmenti.PitanjeFrag;
import ba.unsa.etf.rma.klase.Kviz;

public class IgrajKvizAkt extends AppCompatActivity implements InformacijeFrag.OnFragmentInteractionListener, PitanjeFrag.OnFragmentInteractionListener {

    private Kviz kviz;
    private String pitanjeFragTag;
    private String infoFragTag;

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

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
