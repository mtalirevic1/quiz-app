package ba.unsa.etf.rma.klase;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import android.content.res.Resources;

import ba.unsa.etf.rma.R;


public class BazaTask extends AsyncTask<String, Void, Void> {

    private String kolekcija;
    private String method;
    private Boolean output;
    private String document;
    private Resources res;
    private String odgovor;
    private String rezultat;

    public String getOdgovor() {
        return odgovor;
    }

    public String getKolekcija(){
        return kolekcija;
    }

    public String getRezultat(){
        return rezultat;
    }

    public BazaTask(String kolekcija, String method, Boolean output, String document, Resources res) {
        this.kolekcija = kolekcija;
        this.method = method;
        this.output = output;
        this.document = document;
        this.res = res;
        this.odgovor = "";
        this.rezultat="";
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }


    @Override
    protected Void doInBackground(String... strings) {

        GoogleCredential credentials;
        try{
            InputStream stream=res.openRawResource(R.raw.secret);
            credentials=GoogleCredential.fromStream(stream).createScoped(Lists.newArrayList("https://www.googleapis.com/auth/datastore"));
            credentials.refreshToken();
            String TOKEN=credentials.getAccessToken();
            String urlString;
            Boolean query=kolekcija.equals("query");
            if(!query) {
                urlString = "https://firestore.googleapis.com/v1/projects/rmaprojekat-17749/databases/(default)/documents/" + kolekcija + "?access_token=";
            } else{
                urlString = "https://firestore.googleapis.com/v1/projects/rmaprojekat-17749/databases/(default)/documents:runQuery?access_token=";
            }
            URL url=new URL(urlString + URLEncoder.encode(TOKEN,"utf-8"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);

            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");

            if(query){
                conn.setDoInput(true);
            }
            else {
                conn.setDoOutput(output);
            }

            if(output) {
                try(OutputStream os=conn.getOutputStream()){
                    byte[] input = document.getBytes("utf-8");
                    os.write(input,0,input.length);
                }
            }
            int code=conn.getResponseCode();

            InputStream is=conn.getInputStream();

            if(query){
                rezultat=convertStreamToString(is);
                Log.d("REZULTATQUERY",rezultat);
            }

            if(!query) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    Log.d("ODGOVOR", response.toString());
                    odgovor = response.toString();

                }
            }

        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
