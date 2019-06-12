package ba.unsa.etf.rma.klase;

import android.content.res.Resources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Tasks {

    public static void azurirajKategorijeBaze(Resources res) {
        class TaskKat extends BazaTask {

            public TaskKat(String kolekcija, String method, Boolean output, String document, Resources res) {
                super(kolekcija, method, output, document, res);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    String odgovor = this.getOdgovor();

                    JSONObject object = new JSONObject(odgovor);
                    JSONArray documents = object.getJSONArray("documents");


                    for (int i = 0; i < documents.length(); i++) {

                        JSONObject doc = documents.getJSONObject(i);

                        JSONObject fields = new JSONObject(doc.getString("fields"));

                        JSONObject naziv = new JSONObject(fields.getString("naziv"));
                        String naz=naziv.getString("stringValue");

                        JSONObject idIkonice = new JSONObject(fields.getString("idIkonice"));
                        String icon=idIkonice.getString("integerValue");

                    }
                } catch (JSONException e) {

                }

            }
        }
        TaskKat task = new TaskKat("Kategorije", "GET", false, "", res);
        task.execute();
    }

    public static void azurirajKvizoveBaze(Resources res){
        class TaskPost extends BazaTask {

            public TaskPost(String kolekcija, String method, Boolean output, String document, Resources res) {
                super(kolekcija, method, output, document, res);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    String odgovor =this.getOdgovor();

                    JSONObject object = new JSONObject(odgovor);
                    JSONArray documents = object.getJSONArray("documents");

                    for (int i = 0; i < documents.length(); i++) {

                        JSONObject doc = documents.getJSONObject(i);
                        JSONObject fields;

                        fields = new JSONObject(doc.getString("fields"));

                        JSONObject naziv = new JSONObject(fields.getString("naziv"));
                        String naz=naziv.getString("stringValue");

                        JSONObject idKategorije = new JSONObject(fields.getString("idKategorije"));
                        String kat = idKategorije.getString("stringValue");

                        JSONObject pit = new JSONObject(fields.getString("pitanja"));
                        JSONObject arrayValue = new JSONObject(pit.getString("arrayValue"));
                        JSONArray values = arrayValue.getJSONArray("values");
                        String[] pitanja=new String[values.length()];
                        for (int j = 0; j < values.length(); j++) {
                            JSONObject pita = values.getJSONObject(j);
                            pitanja[i]=pita.getString("stringValue");
                        }
                        String pitanjaString=StrOps.ArrayToString(pitanja);
                    }



                } catch (JSONException e) {

                }
            }
        }
    }
}
