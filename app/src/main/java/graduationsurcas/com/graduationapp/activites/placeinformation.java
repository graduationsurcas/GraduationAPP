package graduationsurcas.com.graduationapp.activites;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import extra.activityUrlLink;
import graduationsurcas.com.graduationapp.R;

public class placeinformation extends ActionBarActivity {

    private Context context = this;
    private String placeid;
    private Toolbar toolbar;
    private String placename;
    private WebView placeinfowebview;
    private SwipeRefreshLayout swipeLayout;

    boolean loadingFinished = true;
    boolean redirect = false;
    private LinearLayout progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeinformation);
        placeid = getIntent().getExtras().getString("placeid");
        placename = getIntent().getExtras().getString("placename");

        toolbar = (Toolbar) findViewById(R.id.placeinfotoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(placename);
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary));

        progressbar =  (LinearLayout)findViewById(R.id.placeprogressbar);

        placeinfowebview = (WebView) findViewById(R.id.placeinfowebview);
        placeinfowebview.setVisibility(View.INVISIBLE);
        placeinfowebview.getSettings().setJavaScriptEnabled(true);
        placeinfowebview.setWebViewClient(new PlaceInfoWebViewClient());
        placeinfowebview.setWebChromeClient(new PlaceInfoWebChromeClient());

        placeinfowebview.loadUrl(activityUrlLink.getPlaceInfoPage(placeid));



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_placeinformation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class PlaceInfoWebChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {
            super.onProgressChanged(view, progress);
            setProgress(progress * 100);
        }


        @Override
        public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
            Toast.makeText(context, url, Toast.LENGTH_LONG).show();
//            super.onReceivedTouchIconUrl(view, url, precomposed);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            result.confirm();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    private class PlaceInfoWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!loadingFinished) {
                redirect = true;
            }
            loadingFinished = false;

            String[] sendData = activityUrlLink.getLinkContents(url);
            if(sendData[0].trim().equalsIgnoreCase("map")){
                openPlaceLocationOnMap(sendData[1], sendData[2]);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap facIcon) {
            loadingFinished = false;
            //SHOW LOADING IF IT ISNT ALREADY VISIBLE
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(!redirect){
                loadingFinished = true;
            }

            if(loadingFinished && !redirect){
                //HIDE LOADING IT HAS FINISHED
                placeinfowebview.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);

            } else{
                redirect = false;
            }

        }

        //        Toast.makeText(context, url, Toast.LENGTH_SHORT).show();


        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == event.KEYCODE_BACK && placeinfowebview.canGoBack()) {
            placeinfowebview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void openPlaceLocationOnMap(String latitude, String longitude){
        Intent mapa = new Intent(Intent.ACTION_VIEW);
        mapa.setData(Uri.parse("geo:"+latitude+","+longitude));
        Intent choser = Intent.createChooser(mapa, "Lunch Map");
        startActivity(choser);
//        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//        context.startActivity(intent);
    }

}
