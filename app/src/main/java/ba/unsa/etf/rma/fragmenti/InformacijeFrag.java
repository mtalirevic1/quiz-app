package ba.unsa.etf.rma.fragmenti;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.aktivnosti.IgrajKvizAkt;
import ba.unsa.etf.rma.klase.Kviz;

public class InformacijeFrag extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private OnFragmentInteractionListener mListener;

    private TextView infNazivKviza;
    private TextView infBrojTacnihPitanja;
    private TextView infBrojPreostalihPitanja;
    private TextView infProcenatTacni;


    private Button btnKraj;

    private Integer brojTacnih;
    private Integer brojPreostalih;
    private Integer brojOdgovorenih;
    private Double procenatTacnih;
    private Integer ukupanBroj;

    private Kviz kviz;

    public Double getProcenatTacnih() {
        return procenatTacnih;
    }

    public InformacijeFrag() {
    }

    public static InformacijeFrag newInstance(Kviz kviz) {
        InformacijeFrag fragment = new InformacijeFrag();
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
        }
        brojTacnih=0;
        brojOdgovorenih=0;
        ukupanBroj=kviz.getPitanja().size();
        brojPreostalih=kviz.getPitanja().size()-1;
        procenatTacnih=0.;
    }

    public void updateStats(Boolean tacan){
        if(brojPreostalih>0)
            brojPreostalih--;
        brojOdgovorenih++;
        if(tacan){
            brojTacnih++;
        }
        if(brojOdgovorenih!=0) {
            procenatTacnih = brojTacnih / (double) brojOdgovorenih;
        } else{
            procenatTacnih=0.;
        }
        postaviText(brojPreostalih,brojTacnih,procenatTacnih);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fragment_informacije, container, false);

         infNazivKviza=(TextView)view.findViewById(R.id.infNazivKviza);
         infBrojTacnihPitanja=(TextView)view.findViewById(R.id.infBrojTacnihPitanja);
         infBrojPreostalihPitanja=(TextView)view.findViewById(R.id.infBrojPreostalihPitanja);
         infProcenatTacni=(TextView)view.findViewById(R.id.infProcenatTacni);
         btnKraj=(Button)view.findViewById(R.id.btnKraj) ;

        btnKraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IgrajKvizAkt)getActivity()).zavrsiAkt();
            }
        });

        infNazivKviza.setText(kviz.getNaziv());
        postaviText(brojPreostalih,brojTacnih,procenatTacnih);

        return view;
    }

    public void postaviText(Integer brojPreostalih, Integer brojTacnih, Double procenatTacnih){
        procenatTacnih*=100;
        infBrojPreostalihPitanja.setText(brojPreostalih.toString());
        infBrojTacnihPitanja.setText(brojTacnih.toString());
        infProcenatTacni.setText(procenatTacnih.toString()+"%");
    }

    public void postaviTextPrazno(){
        infBrojPreostalihPitanja.setText("");
        infBrojTacnihPitanja.setText("");
        infProcenatTacni.setText("");
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
