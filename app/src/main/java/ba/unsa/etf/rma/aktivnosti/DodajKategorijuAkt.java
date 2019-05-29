package ba.unsa.etf.rma.aktivnosti;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.maltaisn.icondialog.Icon;
import com.maltaisn.icondialog.IconDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.klase.BazaTask;
import ba.unsa.etf.rma.klase.Kategorija;

public class DodajKategorijuAkt extends AppCompatActivity implements IconDialog.Callback {
    private Icon[] selectedIcons;
    private EditText etNaziv;
    private EditText etIkona;
    private Button btnDodajIkonu;
    private Button btnDodajKategoriju;
    private ArrayList<Kategorija> kategorije;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.dodaj_kategoriju_akt);
        Bundle bundle=getIntent().getExtras();
        kategorije=bundle.getParcelableArrayList("kategorije");

        final IconDialog iconDialog = new IconDialog();
        etNaziv = (EditText) findViewById(R.id.etNaziv);
        etIkona = (EditText) findViewById(R.id.etIkona);
        btnDodajIkonu = (Button) findViewById(R.id.btnDodajIkonu);
        btnDodajKategoriju = (Button) findViewById(R.id.btnDodajKategoriju);

        etIkona.setFocusable(false);

        btnDodajIkonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconDialog.setSelectedIcons(selectedIcons);
                iconDialog.show(getSupportFragmentManager(), "icon_dialog");
            }
        });

        btnDodajKategoriju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imaIme()) {
                    etNaziv.setBackgroundColor(getResources().getColor(R.color.crvena));
                } else{
                    etNaziv.setBackgroundColor(getResources().getColor(R.color.colorLabel1));
                }

                if (!imaId()) {
                    etIkona.setBackgroundColor(getResources().getColor(R.color.crvena));
                } else {
                    etIkona.setBackgroundColor(getResources().getColor(R.color.colorLabel1));
                }

                if(imaIme() && imaId() && !imaKategoriju(etIkona.getText().toString())){
                    Intent returnIntent = new Intent();
                    String s=etNaziv.getText().toString();
                    returnIntent.putExtra("ikona",selectedIcons[0].getId());
                    returnIntent.putExtra("kategorija",s);
                    setResult(RESULT_OK, returnIntent);
                    dodajKategorijuFirebase(s,selectedIcons[0].getId()+"");
                    finish();
                }
            }
        });

    }

    @Override
    public void onIconDialogIconsSelected(Icon[] icons) {
        selectedIcons=icons;
        for(int i=0;i<selectedIcons.length;i++) {
            etIkona.setText(""+selectedIcons[i].getId());
        }
    }

    public boolean imaIme() {
        String s = etNaziv.getText().toString();
        if (!s.equals(""))
            return true;
        return false;
    }

    public boolean imaId() {
        String s = etIkona.getText().toString();
        if (!s.equals(""))
            return true;
        return false;
    }

    public boolean imaKategoriju(String s){
        for(Kategorija k: kategorije){
            if(k.getId().equals(s)){
                return true;
            }
        }
        return false;
    }

    public void dodajKategorijuFirebase(String naziv, String idIkone){
        try {
            JSONObject integerValue=new JSONObject();
            integerValue.put("integerValue",idIkone);

            JSONObject stringValue=new JSONObject();
            stringValue.put("stringValue",naziv);

            JSONObject fields=new JSONObject();
            fields.put("idIkonice",integerValue);
            fields.put("naziv",stringValue);

            JSONObject jo=new JSONObject();
            jo.put("fields",fields);

            new BazaTask("Kategorije/"+idIkone, "PATCH", true, jo.toString(), getResources()).execute();
        } catch (JSONException e){

        }
    }

}
