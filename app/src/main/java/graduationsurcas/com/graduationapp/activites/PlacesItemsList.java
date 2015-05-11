package graduationsurcas.com.graduationapp.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class PlacesItemsList extends ActionBarActivity {

    private Toolbar toolbar;
    private LinearLayout progressbar;
    private WebView itemswebview;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor sharedpreferenceseditor;
    private SwipeRefreshLayout swipeLayout;
    private Context context = this;
    boolean loadingFinished = true;
    boolean redirect = false;
    private PassInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_items_list);
        SharedPreferencesSetUp();

        info = (PassInfo)getIntent().getSerializableExtra("infoobject");

        toolbar = (Toolbar) findViewById(R.id.placeitemslisttoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(info.getTitle());
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary));
        initializeSwipeLayout();

        progressbar =  (LinearLayout)findViewById(R.id.placeitemslistprogresslayout);

        itemswebview = (WebView) findViewById(R.id.placeitemslistwebview);
        itemswebview.setVisibility(View.INVISIBLE);
        itemswebview.getSettings().setJavaScriptEnabled(true);
        itemswebview.setWebViewClient(new CustomeWebViewClient());
        itemswebview.setWebChromeClient(new CustomeWebChromeClient());
        itemswebview.loadUrl(activityUrlLink.getItemsListUrl(context,
                sharedpreferences.getString(
                        sharedPreferencesKey.PREFERENCES_ITEM_ORDER_BY,
                        URLHeaderKeys.GET_KEY.ITEM_ORDER_PY_DATE),
                sharedpreferences.getInt(
                        sharedPreferencesKey.PREFERENCES_ITEM_SELECT_AMOUNT,
                        25), Long.valueOf(info.getId())));

    }

    public void SharedPreferencesSetUp(){
        sharedpreferences = getSharedPreferences(sharedPreferencesKey.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        sharedpreferenceseditor = sharedpreferences.edit();
    }

    private void initializeSwipeLayout() {
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.placeitemslistswipe);
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
                itemswebview.loadUrl(activityUrlLink.getItemsListUrl(context,
                        sharedpreferences.getString(
                                sharedPreferencesKey.PREFERENCES_ITEM_ORDER_BY,
                                URLHeaderKeys.GET_KEY.ITEM_ORDER_PY_DATE),
                        sharedpreferences.getInt(
                                sharedPreferencesKey.PREFERENCES_ITEM_SELECT_AMOUNT,
                                25), Long.valueOf(info.getId())));
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
        }, 4000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_places_items_list, menu);
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


    private class CustomeWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (!loadingFinished) {
                redirect = true;
            }
            loadingFinished = false;

            try {
                url = java.net.URLDecoder.decode(url, "UTF-8");
                Log.d("info object", url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            String[] sendData = activityUrlLink.getLinkContents(url);
            if(sendData[0].trim().equalsIgnoreCase("share")){
                GeneralFunction.shareWithAnotherApp(sendData[1].replaceAll("%20", " "), context);
            }
            if(sendData[0].trim().equalsIgnoreCase("openitem")){
                Intent intent = new Intent(context, iteminformation.class);
                intent.putExtra("infoobject", new PassInfo(sendData[1], (sendData[2].replaceAll("%20", " "))));
                startActivity(intent);
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
                itemswebview.setVisibility(View.VISIBLE);
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
            Toast.makeText(context, description, Toast.LENGTH_SHORT).show();
        }
    }

    private class CustomeWebChromeClient extends WebChromeClient {
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

}
