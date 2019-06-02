package ba.unsa.etf.rma.fragmenti;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.HighScore;
import ba.unsa.etf.rma.klase.RangListaAdapter;


public class RangLista extends Fragment {

    private ArrayList<HighScore> highScores;
    private RangListaAdapter rangListaAdapter;

    private TextView tekstRangliste;
    private ListView ranglista;

    private OnFragmentInteractionListener mListener;

    public RangLista() {
        // Required empty public constructor
    }

    public static RangLista newInstance(ArrayList<HighScore> highScores) {
        RangLista fragment = new RangLista();
        Bundle args = new Bundle();
        args.putParcelableArrayList("highScores", highScores);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            highScores=getArguments().getParcelableArrayList("highScores");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rang_lista, container, false);

        tekstRangliste=(TextView) view.findViewById(R.id.tekstRangliste);
        ranglista=(ListView) view.findViewById(R.id.ranglista);

        Collections.sort(highScores);

        rangListaAdapter = new RangListaAdapter(getActivity(), highScores, getResources());
        ranglista.setAdapter(rangListaAdapter);
        rangListaAdapter.notifyDataSetChanged();
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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
