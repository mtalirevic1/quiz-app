package ba.unsa.etf.rma.klase;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maltaisn.icondialog.IconView;

import java.util.ArrayList;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.aktivnosti.KvizoviAkt;

public class KvizoviAktAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater = null;
    public Resources res;
    Kviz kviz = null;

    public KvizoviAktAdapter(Activity activity, ArrayList podaci, Resources res) {
        this.activity = activity;
        this.data = podaci;
        this.res = res;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {

        public TextView text;
        public IconView image;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (convertView == null) {

            view = inflater.inflate(R.layout.lista_ikone, null);

            holder = new ViewHolder();
            holder.text = (TextView) view.findViewById(R.id.text);
            holder.image = (IconView) view.findViewById(R.id.image);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        kviz = null;
        kviz = (Kviz) data.get(position);

        if (position != data.size() - 1) {
            holder.text.setText(kviz.getNaziv());
            Integer id=-1;
            try {
                id = Integer.parseInt(kviz.getKategorija().getId());
            } catch (NumberFormatException e) {
                id = -1;
            }

            holder.image.setIcon(id);

        } else {
            holder.text.setText("Dodaj Kviz");
            holder.image.setImageResource(res.getIdentifier("zeleniplus", "drawable", "ba.unsa.etf.rma"));
        }

        return view;
    }

}
