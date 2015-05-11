package graduationsurcas.com.graduationapp.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;


import extra.GPStracker;
import extra.GeneralFunction;
import extra.PassInfo;
import extra.WeatherAPIkeys;
import extra.WeatherAPIkeys.*;
import extra.sharedPreferencesKey;
import graduationsurcas.com.graduationapp.R;

public class Home extends ActionBarActivity {
    double locationlong = 0.0;
    double locationlat = 0.0;

    private Context context = this;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor sharedpreferenceseditor;

    private Toolbar toolbar;
    private TextView temperaturedegree;
    private TextView humiditydegree;
    private TextView cityname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        SharedPreferencesSetUp();

        sharedpreferenceseditor.putString(sharedPreferencesKey.PREFERENCES_USER_LANGUAGE, GeneralFunction.getLanguageCode("Portuguese"));
        sharedpreferenceseditor.commit();

        toolbar = (Toolbar) findViewById(R.id.homeactivitytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setLogo(R.drawable.toolbarappicon);
        toolbar.getBackground().setAlpha(0);


        temperaturedegree = (TextView) findViewById(R.id.temperaturedegree);
        humiditydegree = (TextView) findViewById(R.id.humiditydegree);
        cityname = (TextView) findViewById(R.id.cityname);

        if (sharedpreferences.getBoolean(sharedPreferencesKey.PREFERENCES_APP_RUN_FIRST_TIME, true)) {
            sharedpreferenceseditor.putBoolean(sharedPreferencesKey.PREFERENCES_APP_RUN_FIRST_TIME,
                    false);
            sharedpreferenceseditor.commit();

            //if the app run at the first time
            //select user location
            if (sharedpreferences.getBoolean(sharedPreferencesKey.PREFERENCES_LOCATION_ASKED, true)) {
                sharedpreferenceseditor.putBoolean(sharedPreferencesKey.PREFERENCES_LOCATION_ASKED,
                        false);
                sharedpreferenceseditor.commit();

                //get user location
                GeneralFunction.getLocation(context);
            }

            //get weather info
            Weather weather = new Weather(sharedpreferences.getString(
                    sharedPreferencesKey.PREFERENCES_LOCATION_LATITUDE,
                    ""), sharedpreferences.getString(
                    sharedPreferencesKey.PREFERENCES_LOCATION_LONGITUDE,
                    ""));
            weather.execute();

        }
        //if app open before by user
        //1 hour -> 3600000 milliseconds
        long lastwitherupdate = Long.valueOf(sharedpreferences.getString(sharedPreferencesKey.PREFERENCES_WEATHER_LAST_UBDATE_TIME,
                String.valueOf(System.currentTimeMillis())));
        long updatewithertime = Long.valueOf(sharedpreferences.getString(sharedPreferencesKey.PREFERENCES_WEATHER_UBDATE_PERIOD_TIME,
                "3600000"));

        if ((System.currentTimeMillis() <= (lastwitherupdate + updatewithertime))) {
            Log.d("update weather", "true");
            //get weather
            Weather weather = new Weather(sharedpreferences.getString(
                    sharedPreferencesKey.PREFERENCES_LOCATION_LATITUDE,
                    ""), sharedpreferences.getString(
                    sharedPreferencesKey.PREFERENCES_LOCATION_LONGITUDE,
                    ""));
            weather.execute();
        } else {
            temperaturedegree.setText(
                    sharedpreferences.getString(sharedPreferencesKey.PREFERENCES_WEATHER_TEMPERATURE, "31")
                            + (char) 0x00B0);
            humiditydegree.setText(sharedpreferences.getString(sharedPreferencesKey.PREFERENCES_WEATHER_HUMIDITY, "20%"));
            cityname.setText(sharedpreferences.getString(sharedPreferencesKey.PREFERENCES_WEATHER_PLACE, "Muscat, Oman"));
        }


//        new DownloadImageTask().execute("oman+sur");


    }

    public void SharedPreferencesSetUp() {
        sharedpreferences = getSharedPreferences(sharedPreferencesKey.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        sharedpreferenceseditor = sharedpreferences.edit();
    }


    public void openplaceslist(View view) {
        startActivity(new Intent(context, MainActivity.class));
    }

    public void openitemslist(View view) {
        startActivity(new Intent(context, items_list.class));
    }


    public void openplacelist(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void openserviceslist(View view) {
        startActivity(new Intent(this, ServicesList.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
        try{
            startActivity(new Intent(context,Login.class));
        }catch (Exception e){

        }
            return true;
        } else if (id == R.id.user_login) {

            try{
                startActivity(new Intent(context, Login.class));
            }
            catch (Exception ex){}

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    protected class Weather extends AsyncTask<Void, Void, ArrayList<String>> {


        private String lat;
        private String lang;
        private Document weatherdoc;
        ArrayList<String> weather = new ArrayList();
        HashMap<String, String> weatherInfoList = new HashMap<>();

        public Weather(String lat, String lang) {
            this.lat = lat;
            this.lang = lang;
        }

        public Weather() {
        }

        @Override
        protected ArrayList<String> doInBackground(Void... arg0) {
            String qResult = "";
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            String apiUrl = "http://api.wunderground.com/api/"
                    + WeatherAPIkeys.WETHER_API_KEY
                    + "/conditions/q/"
                    + getLat()
                    + ","
                    + getLang()
                    + ".xml";


            HttpGet httpGet = new HttpGet(apiUrl);

            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    InputStream inputStream = entity.getContent();
                    Reader in = new InputStreamReader(inputStream);
                    BufferedReader bufferedreader = new BufferedReader(in);
                    StringBuilder stringBuilder = new StringBuilder();
                    String stringReadLine = null;
                    while ((stringReadLine = bufferedreader.readLine()) != null) {
                        stringBuilder.append(stringReadLine + "\n");

                    }
                    qResult = stringBuilder.toString();

                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            DocumentBuilderFactory dbFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder parser;
            try {
                parser = dbFactory.newDocumentBuilder();
                weatherdoc = parser
                        .parse(new ByteArrayInputStream(qResult.getBytes()));

            } catch (ParserConfigurationException e1) {
                e1.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            sharedpreferenceseditor.putString(sharedPreferencesKey.PREFERENCES_WEATHER_LAST_UBDATE_TIME, String.valueOf(System.currentTimeMillis()));
            sharedpreferenceseditor.putString(sharedPreferencesKey.PREFERENCES_WEATHER_TEMPERATURE,
                    findDetail(api_xml_key.WETHER_TEMPERATURE));
            sharedpreferenceseditor.putString(sharedPreferencesKey.PREFERENCES_WEATHER_HUMIDITY,
                    findDetail(api_xml_key.WETHER_HUMIDITY));
            sharedpreferenceseditor.putString(sharedPreferencesKey.PREFERENCES_WEATHER_PLACE,
                    findDetail(api_xml_key.WETHER_CITY_NAME));
            sharedpreferenceseditor.commit();

            weatherInfoList.put(weather_hashmap_key.TEMPERATURE, findDetail(api_xml_key.WETHER_TEMPERATURE));
            weatherInfoList.put(weather_hashmap_key.CITYNAME, findDetail(api_xml_key.WETHER_CITY_NAME));
            weatherInfoList.put(weather_hashmap_key.HUMIDITY, findDetail(api_xml_key.WETHER_HUMIDITY));

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);

            temperaturedegree.setText(
                    sharedpreferences.getString(sharedPreferencesKey.PREFERENCES_WEATHER_TEMPERATURE, "31")
                            + (char) 0x00B0);
            humiditydegree.setText(sharedpreferences.getString(sharedPreferencesKey.PREFERENCES_WEATHER_HUMIDITY, "20%"));
            cityname.setText(sharedpreferences.getString(sharedPreferencesKey.PREFERENCES_WEATHER_PLACE, "Muscat, Oman"));
            //            Log.d("weather api temp", weather.get(0));
        }

        public String findDetail(String query) {
            XPath xPath = XPathFactory.newInstance().newXPath();
            String rs = "";
            try {
                Node n = (Node) xPath.evaluate(query, getWeatherdoc(), XPathConstants.NODE);
                if (n != null) {
                    rs = n.getTextContent();
                }
            } catch (Exception e) {
                rs = "";
            }
            return rs;
        }


        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public Document getWeatherdoc() {
            return weatherdoc;
        }

        public void setWeatherdoc(Document weatherdoc) {
            this.weatherdoc = weatherdoc;
        }

    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        String qResult = "";

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        public DownloadImageTask() {
        }

        protected Bitmap doInBackground(String... cityname) {


            String apiUrl = getFlickerSearchLink("oman");
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();


            HttpGet httpGet = new HttpGet(apiUrl);

            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    InputStream inputStream = entity.getContent();
                    Reader in = new InputStreamReader(inputStream);
                    BufferedReader bufferedreader = new BufferedReader(in);
                    StringBuilder stringBuilder = new StringBuilder();
                    String stringReadLine = null;
                    while ((stringReadLine = bufferedreader.readLine()) != null) {
                        stringBuilder.append(stringReadLine + "\n");
                        Log.d("photos", stringReadLine);
                    }
                    qResult = stringBuilder.toString();

                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


//            String urldisplay = cityname[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
            return null;
        }

        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
        }

        public String getFlickerSearchLink(String tag) {
//            sur+oman
            return ("https://api.flickr.com/services/rest/?" +
                    "method=flickr.photos.search" +
                    "&api_key=a59cbccd1f44d2c3cf5d5c66416ee8c3" +
                    "&tags=uae" +
                    "&text=uae" +
                    "&privacy_filter=1" +
                    "&safe_search=1&per_page=1" +
                    "&page=1" +
                    "&format=rest" +
                    "&auth_token=72157651857612357-5514d76cc9433844" +
                    "&api_sig=8b741b7d93ce063937ee99a5ba2b0b9c");
        }


    }

    public void QRReader(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureLayout(R.layout.custom_capture_layout);
        integrator.initiateScan();

    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult != null) {
                if (scanResult.getFormatName().trim().equalsIgnoreCase("QR_CODE")) {
                    if (scanResult.getContents() != null) {

                        String[] data = scanResult.getContents().split("~");
                        if (data[0].trim().equalsIgnoreCase("omantourismguide")) {
                            //"omantourismguide~place~itemid~itemname"
                            if (data[1].trim().equalsIgnoreCase("place")) {
                                try {
                                    Intent placeinfo = new Intent(context, placeinformation.class);
                                    placeinfo.putExtra("infoobject", new PassInfo(data[2], (data[3].replaceAll("%20", " "))));
                                    startActivity(placeinfo);
                                } catch (Exception e) {

                                }
                            } else if (data[1].trim().equalsIgnoreCase("item")) {
                                Intent iteminfo = new Intent(context, iteminformation.class);
                                iteminfo.putExtra("infoobject", new PassInfo(data[2], (data[3].replaceAll("%20", " "))));
                                startActivity(iteminfo);
                            }
//                        String destenation = data[1];
//                        String itemid = data[2];
//                        String itemname = data[3];
//                        Log.d("QR item", "destenation = "+destenation);
//                        Log.d("QR item", "id = "+itemid);
//                        Log.d("QR item", "name = "+itemname);
                        }
                    } else {

                    }
                }
//            Log.d("QR type", scanResult.getFormatName());
            }
        } catch (Exception e) {
        }

//        && scanResult.getContents() != null
    }


}
