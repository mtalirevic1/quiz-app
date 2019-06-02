package ba.unsa.etf.rma.fragmenti;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.aktivnosti.IgrajKvizAkt;
import ba.unsa.etf.rma.klase.IgrajKvizAdapter;
import ba.unsa.etf.rma.klase.Kviz;
import ba.unsa.etf.rma.klase.Pitanje;



public class PitanjeFrag extends Fragment {

    private OnFragmentInteractionListener mListener;

    private TextView tekstPitanja;
    private ListView odgovoriPitanja;
    private IgrajKvizAdapter igrajKvizAdapter;

    private Kviz kviz;
    private ArrayList<String> odgovori;
    private ArrayList<Pitanje> pitanja;
    private String tacan;

    private Handler handler;

    public PitanjeFrag() {
    }

    public static PitanjeFrag newInstance(Kviz kviz) {
        PitanjeFrag fragment = new PitanjeFrag();
        Bundle args = new Bundle();
        args.putParcelable("kviz", kviz);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            kviz = getArguments().getParcelable("kviz");
            odgovori = new ArrayList<>();
            pitanja = kviz.getPitanja();
        }
        tacan = "";
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pitanje, container, false);
        odgovoriPitanja = (ListView) view.findViewById(R.id.odgovoriPitanja);
        tekstPitanja = (TextView) view.findViewById(R.id.tekstPitanja);

        Resources res = getResources();
        Collections.shuffle(kviz.getPitanja());
        if (pitanja.size() > 0) {
            odgovori.addAll(pitanja.get(0).getOdgovori());
            tacan = pitanja.get(0).getTacan();
            tekstPitanja.setText(pitanja.get(0).getTextPitanja());
            pitanja.remove(0);
        }

        igrajKvizAdapter = new IgrajKvizAdapter(getActivity(), odgovori, res);
        odgovoriPitanja.setAdapter(igrajKvizAdapter);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void naClick(final int position) {
        FragmentManager fm = getFragmentManager();
        final InformacijeFrag f = (InformacijeFrag)fm.findFragmentByTag("infoFrag");
        Boolean t;
        if (odgovori.get(position).equals(tacan)) {
            odgovoriPitanja.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.zelena));
            t=true;
        } else {
            odgovoriPitanja.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.crvena));
            t=false;
        }
        final Boolean isCorrect=t;
        if (pitanja.size() == 0) {
            f.updateStats(isCorrect);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    odgovoriPitanja.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.bijela));
                    odgovori.clear();
                    igrajKvizAdapter.notifyDataSetChanged();
                    tekstPitanja.setText(getString(R.string.kviz_zavrsen));
                    ((IgrajKvizAkt)getActivity()).unesiHighscore(f.getProcenatTacnih());
                }
            }, 2000);

        } else {
            f.updateStats(isCorrect);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    odgovoriPitanja.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.bijela));
                    odgovori.clear();
                    odgovori.addAll(pitanja.get(0).getOdgovori());
                    tacan = pitanja.get(0).getTacan();
                    igrajKvizAdapter.notifyDataSetChanged();
                    tekstPitanja.setText(pitanja.get(0).getTextPitanja());


                    pitanja.remove(0);
                }
            }, 2000);
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
