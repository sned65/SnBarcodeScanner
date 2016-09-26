package sne.bcs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class EgaisActivity extends AppCompatActivity
{

    public static final String EGAIS_URI = "egais_uri";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egais);

        String egais_uri = getIntent().getStringExtra(EGAIS_URI);
        WebView web = (WebView) findViewById(R.id.egaisPage);
        web.loadUrl(egais_uri);
    }

    public static boolean isEgais(String url)
    {
        return url !=  null && url.contains("egais.ru");
    }
}
