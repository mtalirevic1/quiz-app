package ba.unsa.etf.rma.klase;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Konekcija {

    private Context context;

    public Konekcija(Context context) {
        this.context=context;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
