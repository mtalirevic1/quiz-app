package ba.unsa.etf.rma.fragmenti;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.aktivnosti.IgrajKvizAkt;
import ba.unsa.etf.rma.klase.BlueListAdapter;
import ba.unsa.etf.rma.klase.IgrajKvizAdapter;
import ba.unsa.etf.rma.klase.Kviz;
import ba.unsa.etf.rma.klase.Pitanje;


public class PitanjeFrag extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    private TextView tekstPitanja;
    private ListView odgovoriPitanja;
    private IgrajKvizAdapter igrajKvizAdapter;

    private Kviz kviz;
    private ArrayList<String> odgovori;
    private String tacan;

    public PitanjeFrag() {
    }

    public static PitanjeFrag newInstance(Kviz kviz) {
        PitanjeFrag fragment = new PitanjeFrag();
        Bundle args = new Bundle();
        args.putParcelable("kviz",kviz);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           /* mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
           kviz=getArguments().getParcelable("kviz");
           odgovori=new ArrayList<>();
           tacan="";
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pitanje, container, false);
        odgovoriPitanja=(ListView) view.findViewById(R.id.odgovoriPitanja);
        tekstPitanja=(TextView) view.findViewById(R.id.tekstPitanja);

        Resources res = getResources();
        Collections.shuffle(kviz.getPitanja());
        if(kviz.getPitanja().size()>0){
            odgovori.addAll(kviz.getPitanja().get(0).getOdgovori());
            tacan=kviz.getPitanja().get(0).getTacan();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
