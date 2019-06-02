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
import ba.unsa.etf.rma.aktivnosti.IgrajKvizAkt;

public class RangListaAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater = null;
    public Resources res;

    private HighScore highScore=null;

    public RangListaAdapter(Activity activity, ArrayList podaci, Resources res) {
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

        public TextView redni;
        public TextView ime;
        public TextView procenat;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        RangListaAdapter.ViewHolder holder;

        if (convertView == null) {

            view = inflater.inflate(R.layout.element_rangliste, null);

            holder = new RangListaAdapter.ViewHolder();
            holder.redni = (TextView) view.findViewById(R.id.redni);
            holder.ime = (TextView) view.findViewById(R.id.ime);
            holder.procenat = (TextView) view.findViewById(R.id.procenat);

            view.setTag(holder);
        } else {
            holder = (RangListaAdapter.ViewHolder) view.getTag();
        }

        highScore=(HighScore)data.get(position);
        holder.procenat.setText(String.format( "%.2f", highScore.getProcenatTacnih() )+"%");
        holder.ime.setText(highScore.getImeIgraca());
        holder.redni.setText(position+1+"");

       // view.setOnClickListener(new RangListaAdapter.OnItemClickListener(position));

        return view;
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {

        }
    }
}
