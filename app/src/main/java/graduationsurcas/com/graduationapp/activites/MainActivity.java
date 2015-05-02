package graduationsurcas.com.graduationapp.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import extra.activityUrlLink;
import graduationsurcas.com.graduationapp.R;

public class MainActivity extends ActionBarActivity {

    private Context context = this;
    private WebView mainwebview;
    private Toolbar toolbar;
    private ProgressBar progressbar;
    private SwipeRefreshLayout swipeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);

        initializeSwipeLayout();

//        progressbar = (ProgressBar) findViewById(R.id.mainactivityprogress);

        mainwebview = (WebView) findViewById(R.id.mainactivitywebview);
        mainwebview.getSettings().setJavaScriptEnabled(true);
        mainwebview.setWebViewClient(new MainActivityWebViewClient());
        mainwebview.setWebChromeClient(new MainActivityWebChromeClient());
        mainwebview.loadUrl(activityUrlLink.PLACE_URL);


    }

    private class MainActivityWebChromeClient extends WebChromeClient {
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK && mainwebview.canGoBack()) {
            mainwebview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        noinspection SimplifiableIfStatement
//        if (id == R.id.action_openplacelist) {
//
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    private void initializeSwipeLayout() {
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.mainactivityswipe);
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
                mainwebview.loadUrl(activityUrlLink.PLACE_URL);
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


    private class MainActivityWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String[] sendData = activityUrlLink.getLinkContents(url);
            if(sendData[0].trim().equalsIgnoreCase("share")){
                shareWithAnotherApp(sendData[1].replaceAll("%20", " "));
            }
            if(sendData[0].trim().equalsIgnoreCase("openplace")){
                Intent intent = new Intent(context, placeinformation.class);
                intent.putExtra("placeid", sendData[1]);
                intent.putExtra("placename", sendData[2].replaceAll("%20", " "));
                startActivity(intent);
            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
//            titlebar.setText(url);
        }

        //        Toast.makeText(context, url, Toast.LENGTH_SHORT).show();


        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    public void shareWithAnotherApp(String text){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent,
                getResources().getText(R.string.send_to)));
    }

}