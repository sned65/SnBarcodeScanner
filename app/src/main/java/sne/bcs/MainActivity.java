package sne.bcs;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import sne.bcs.util.AboutDialogFragment;
import sne.bcs.util.BarcodeUtils;
import sne.bcs.util.CameraUtils;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getName();

    // intent request code to handle barcode capture
    public static final int RC_BARCODE_CAPTURE = 9010;

    public static final String DATA_BARCODE = "barcode";

    private TextView _bcData;
    private Barcode _barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button captureBarcodeBtn = (Button) findViewById(R.id.btnBarcode);
        if (CameraUtils.hasCamera(getApplicationContext()))
        {
            Log.d(TAG, "onCreate() hasCamera");
            captureBarcodeBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    action_captureBC();
                }
            });
        }
        else
        {
            Log.d(TAG, "onCreate() disable captureBarcodeBtn");
            captureBarcodeBtn.setEnabled(false);
        }

        _bcData = (TextView) findViewById(R.id.bcData);
        _bcData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                action_processBarcode();
            }
        });
    }

    // ----------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Log.i(TAG, "onOptionsItemSelected(" + item.getItemId() + ") called");
        switch (item.getItemId())
        {
//            case R.id.action_settings:
//                //Log.i(TAG, "User chose the \"Settings\" item, show the app settings UI...");
//                Intent settings = new Intent(this, SettingsActivity.class);
//                startActivity(settings);
//                return true;
            case R.id.menu_about:
            {
                AboutDialogFragment about = new AboutDialogFragment();
                Bundle args = new Bundle();
                args.putString(AboutDialogFragment.ARG_TITLE, getResources().getString(R.string.app_name));
                about.setArguments(args);
                about.show(getFragmentManager(), "about");
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Launches "Capture Barcode" activity.
     * Called when the button "Capture Barcode" has been clicked.
     */
    private void action_captureBC()
    {
        Log.v(TAG, "<Capture BC> button clicked");
        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
        startActivityForResult(intent, RC_BARCODE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.i(TAG, "requestCode = " + requestCode + ", resultCode = " + resultCode);
        if (resultCode != RESULT_OK)
        {
            Log.e(TAG, "resultCode = "+resultCode);
            Toast.makeText(this, "resultCode = "+resultCode,
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (requestCode != RC_BARCODE_CAPTURE)
        {
            Log.e(TAG, "requestCode = "+requestCode);
            Toast.makeText(this, "requestCode = "+requestCode,
                    Toast.LENGTH_LONG).show();
            return;
        }

        _barcode = data.getParcelableExtra(DATA_BARCODE);

        // process EGAIS
        String url = extractUrl();
        if (EgaisActivity.isEgais(url))
        {
            Intent egais = new Intent(this, EgaisActivity.class);
            egais.putExtra(EgaisActivity.EGAIS_URI, url);
            startActivity(egais);
            return;
        }

//        // process ERIS
//        String eris_text = extractEris();
//        Log.i(TAG, "eris_text = "+eris_text);
//        if (ErisActivity.isEris(eris_text))
//        {
//            Intent eris = new Intent(this, ErisActivity.class);
//            eris.putExtra(ErisActivity.ERIS_DATA, eris_text);
//            startActivity(eris);
//            return;
//        }

        // show other barcodes
        String bc_data = BarcodeUtils.barcodeData(_barcode);
        Log.i(TAG, "bc = "+bc_data);
        _bcData.setText(bc_data);
    }

    // ----------------------------------------------------------------------
    private String extractUrl()
    {
        if (_barcode == null || _barcode.valueFormat != Barcode.URL) return null;

        Barcode.UrlBookmark info = _barcode.url;
        String url = info.url;
        if (url == null || url.isEmpty())
        {
            url = _barcode.displayValue;
        }
        return url;
    }

    // ----------------------------------------------------------------------
    private String extractEris()
    {
        if (_barcode == null || _barcode.format != Barcode.PDF417 || _barcode.valueFormat != Barcode.TEXT) return null;

        String eris = _barcode.displayValue;
        return eris;
    }

    // ----------------------------------------------------------------------
    private void action_processBarcode()
    {
        if (_barcode == null) return;

        if (_barcode.valueFormat == Barcode.URL)
        {
            String url = extractUrl();
            if (url == null) return;

            Uri uri = Uri.parse(url);
            Log.i(TAG, "URI = " + uri);
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
            return;
        }
    }
}
