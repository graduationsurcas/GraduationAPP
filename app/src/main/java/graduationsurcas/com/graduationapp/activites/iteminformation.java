package graduationsurcas.com.graduationapp.activites;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import extra.GeneralFunction;
import extra.PassInfo;
import extra.activityUrlLink;
import graduationsurcas.com.graduationapp.R;

public class iteminformation extends ActionBarActivity {

    Context context = this;
    
    private String itemid;
    private String itemname;
    private Toolbar toolbar;
    private LinearLayout progressbar;

    boolean loadingFinished = true;
    boolean redirect = false;
    private WebView iteminffowebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iteminformation);


        PassInfo info = (PassInfo)getIntent().getSerializableExtra("infoobject");

        toolbar = (Toolbar) findViewById(R.id.iteminfotoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(info.getTitle());
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary));


        progressbar =  (LinearLayout)findViewById(R.id.iteminfoprogressbar);


        iteminffowebview = (WebView) findViewById(R.id.iteminfowebview);
        iteminffowebview.setVisibility(View.INVISIBLE);
        iteminffowebview.getSettings().setJavaScriptEnabled(true);
        iteminffowebview.setWebViewClient(new ItemInfoWebViewClient());
        iteminffowebview.setWebChromeClient(new ItemInfoWebChromeClient());

        iteminffowebview.loadUrl(activityUrlLink.getItemInfoPage(info.getId(), context));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_iteminformation, menu);
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

    private class ItemInfoWebChromeClient extends WebChromeClient {
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

    private class ItemInfoWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!loadingFinished) {
                redirect = true;
            }
            loadingFinished = false;

            String[] sendData = activityUrlLink.getLinkContents(url);
            if(sendData[0].trim().equalsIgnoreCase("map")){
                GeneralFunction.openPlaceLocationOnMap(context, sendData[1], sendData[2]);
            }
            else if (sendData[0].trim().equalsIgnoreCase("openplace")){
                Intent intent = new Intent(context, placeinformation.class);
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
                iteminffowebview.setVisibility(View.VISIBLE);
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

}
