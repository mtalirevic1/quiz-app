package ba.unsa.etf.rma.aktivnosti;

import android.graphics.drawable.Icon;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.maltaisn.icondialog.IconDialog;

import ba.unsa.etf.rma.R;

public class DodajKategorijuAkt extends AppCompatActivity implements IconDialog.Callback {
    private Icon[] selectedIcons;
    private EditText etNaziv;
    private EditText etIkona;
    private Button btnDodajIkonu;
    private Button btnDodajKategoriju;


    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.dodaj_kategoriju_akt);

        final IconDialog iconDialog = new IconDialog();
        etNaziv = (EditText) findViewById(R.id.etNaziv);
        etIkona = (EditText) findViewById(R.id.etIkona);
        btnDodajIkonu = (Button) findViewById(R.id.btnDodajIkonu);
        btnDodajKategoriju = (Button) findViewById(R.id.btnDodajKategoriju);

        btnDodajIkonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  iconDialog.setSelectedIcons(selectedIcons);
                iconDialog.show(getSupportFragmentManager(), "icon_dialog");
                iconDialog.getIconFilter();
                etIkona.setText(Integer.toString(iconDialog.getId())); // nije jos dobro
            }
        });

    }

    @Override
    public void onIconDialogIconsSelected(com.maltaisn.icondialog.Icon[] icons) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
