package ba.unsa.etf.rma.klase;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ba.unsa.etf.rma.R;
import ba.unsa.etf.rma.aktivnosti.DodajKvizAkt;

public class PlusListAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater = null;
    public Resources res;
    Pitanje pitanje = null;

    public PlusListAdapter(Activity activity, ArrayList podaci, Resources res) {
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
        public ImageView image;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        PlusListAdapter.ViewHolder holder;

        if (convertView == null) {

            view = inflater.inflate(R.layout.element_liste, null);

            holder = new PlusListAdapter.ViewHolder();
            holder.text = (TextView) view.findViewById(R.id.text);
            holder.image = (ImageView) view.findViewById(R.id.image);

            view.setTag(holder);
        } else {
            holder = (PlusListAdapter.ViewHolder) view.getTag();
        }

        pitanje = null;
        pitanje = (Pitanje) data.get(position);

        holder.text.setText(pitanje.getTextPitanja());
        holder.image.setImageResource(res.getIdentifier("zeleniplus", "drawable", "ba.unsa.etf.rma"));

        view.setOnClickListener(new PlusListAdapter.OnItemClickListener(position));

        return view;
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            DodajKvizAkt sct = (DodajKvizAkt) activity;
            sct.onMoguceClick(mPosition);
        }
    }
}