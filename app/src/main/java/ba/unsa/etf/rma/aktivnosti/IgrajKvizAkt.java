package ba.unsa.etf.rma.aktivnosti;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.fragmenti.InformacijeFrag;
import ba.unsa.etf.rma.fragmenti.PitanjeFrag;
import ba.unsa.etf.rma.klase.Kviz;

public class IgrajKvizAkt extends AppCompatActivity implements InformacijeFrag.OnFragmentInteractionListener, PitanjeFrag.OnFragmentInteractionListener {

    private Kviz kviz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.igraj_kviz_akt);
        Bundle bundle = getIntent().getExtras();
        kviz = (Kviz) bundle.getParcelable("kviz");

        InformacijeFrag informacijeFrag = new InformacijeFrag();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.informacijePlace, informacijeFrag);
        transaction.addToBackStack(null);
// Commit the transaction
        transaction.commit();

        PitanjeFrag pitanjeFrag=PitanjeFrag.newInstance(kviz);
        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
        transaction2.replace(R.id.pitanjePlace, pitanjeFrag);
        transaction2.addToBackStack(null);
        transaction2.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
