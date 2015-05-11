package graduationsurcas.com.graduationapp.activites;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import extra.GeneralFunction;
import extra.PassInfo;
import extra.URLHeaderKeys;
import extra.activityUrlLink;
import extra.sharedPreferencesKey;
import graduationsurcas.com.graduationapp.R;

public class ServiceProvidor extends ActionBarActivity {

    private Context context = this;
    private Toolbar toolbar;
    private WebView mainwebview;
    private LinearLayout progressbar;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor sharedpreferenceseditor;
    private SwipeRefreshLayout swipeLayout;
    boolean loadingFinished = true;
    boolean redirect = false;
    private PassInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_providor);

        info = (PassInfo)getIntent().getSerializableExtra("infoobject");
        SharedPreferencesSetUp();

        toolbar = (Toolbar) findViewById(R.id.serviceprovidertoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(info.getTitle());
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary));
        initializeSwipeLayout();

        progressbar =  (LinearLayout)findViewById(R.id.serviceproviderprogresslayout);

        mainwebview = (WebView) findViewById(R.id.serviceproviderwebview);
        mainwebview.setVisibility(View.INVISIBLE);
        mainwebview.getSettings().setJavaScriptEnabled(true);
        mainwebview.setWebViewClient(new ServicesProviderWebViewClient());
        mainwebview.setWebChromeClient(new ServicesProviderWebChromeClient());

        mainwebview.loadUrl(activityUrlLink.getServicesProvideorInfo(context,
                info.getId(),
                sharedpreferences.getInt(
                        sharedPreferencesKey.PREFERENCES_PLACE_SELECT_AMOUNT,
                        25)
        ));


    }

    private class ServicesProviderWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (!loadingFinished) {
                redirect = true;
            }
            loadingFinished = false;

            try {
                url = java.net.URLDecoder.decode(url, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String[] sendData = activityUrlLink.getLinkContents(url);
            if(sendData[0].trim().equalsIgnoreCase("share")){
                GeneralFunction.shareWithAnotherApp(sendData[1].replaceAll("%20", " "), context);
            }

            if(sendData[0].trim().equalsIgnoreCase("openplace")){
//                Intent intent = new Intent(context, placeinformation.class);
//                intent.putExtra("infoobject", new PassInfo(sendData[1], (sendData[2].replaceAll("%20", " "))));
//                startActivity(intent);
            }
            if(sendData[0].trim().equalsIgnoreCase("map")){
                GeneralFunction.openPlaceLocationOnMap(context, sendData[1], sendData[2]);
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
                mainwebview.setVisibility(View.VISIBLE);
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
    private class ServicesProviderWebChromeClient extends WebChromeClient {
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


    public void SharedPreferencesSetUp(){
        sharedpreferences = getSharedPreferences(sharedPreferencesKey.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        sharedpreferenceseditor = sharedpreferences.edit();
    }

    private void initializeSwipeLayout() {
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.serviceproviderswipe);
        // Set four colors used in progress animation
        swipeLayout.setColorSchemeResources(R.color.primary,
                R.color.primary_dark,
                R.color.primary_text);
        // The function SwipeRefreshLayout.setColorScheme() is deprecated.
        // Make sure you have latest version of android support library

        // Set Refresh Listener
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mainwebview.loadUrl(activityUrlLink.getServicesProvideorInfo(context,
                        info.getId(),
                        sharedpreferences.getInt(
                                sharedPreferencesKey.PREFERENCES_PLACE_SELECT_AMOUNT,
                                25)
                ));
                refreshData();
            }
        });

    }

    private void refreshData() {
        // start progress animation-
        swipeLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // stop progress animation
                swipeLayout.setRefreshing(false);
            }
        }, 1000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_service_providor, menu);
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
}
