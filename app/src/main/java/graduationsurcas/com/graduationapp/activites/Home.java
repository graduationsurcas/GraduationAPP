package graduationsurcas.com.graduationapp.activites;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;


import extra.GPStracker;
import extra.WeatherAPIkeys;
import extra.WeatherAPIkeys.*;
import graduationsurcas.com.graduationapp.R;

public class Home extends ActionBarActivity {

    double locationlong = 0.0;
    double locationlat = 0.0;

    private Context context = this;

    private Toolbar toolbar;
    private TextView temperaturedegree;
    private TextView humiditydegree;
    private TextView cityname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.homeactivitytoolbar);
        setSupportActionBar(toolbar);

        temperaturedegree = (TextView) findViewById(R.id.temperaturedegree);
        humiditydegree = (TextView) findViewById(R.id.humiditydegree);
        cityname = (TextView) findViewById(R.id.cityname);

        //get user location
        getLocation();

        //get weather
        Weather weather = new Weather(String.valueOf(locationlat), String.valueOf(locationlong));
        weather.execute();


//        String tempr = weather.findDetail(WeatherAPIkeys.WETHER_TEMPERATURE);



    }


    public void openplaceslist(View view){
        startActivity(new Intent(context, MainActivity.class));
    }



    public void getLocation(){
        GPStracker gps = new GPStracker(context);
        if(gps.isCanGetLocation()){
            locationlat = gps.getLatitude();
            locationlong = gps.getLongitude();
            Log.d("location", "lat = "+locationlat+", long = "+locationlong);
        }else {
            gps.showSettingAlert();
        }
        if(locationlat == 0.0 || locationlong == 0.0){
            locationlong = 59.47241;
            locationlat = 22.56225;
        }
    }

    public void openplacelist(View view){
        startActivity(new Intent(this, MainActivity.class));
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    protected class Weather extends AsyncTask<Void, Void, ArrayList<String>> {



        private String lat;
        private String lang;
        private Document weatherdoc;
        ArrayList<String>  weather = new ArrayList();
        HashMap<String, String> weatherInfoList = new HashMap<>();

        public Weather(String lat, String lang) {
            this.lat = lat;
            this.lang = lang;
        }

        public Weather() {
        }

        @Override
        protected ArrayList<String> doInBackground(Void ...arg0) {
            String qResult = "";
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            String apiUrl = "http://api.wunderground.com/api/"
                    +WeatherAPIkeys.WETHER_API_KEY
                    +"/conditions/q/"
                    +getLat()
                    +","
                    +getLang()
                    +".xml";

            Log.d("weather api", apiUrl);

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



            weatherInfoList.put(weather_hashmap_key.TEMPERATURE, findDetail(api_xml_key.WETHER_TEMPERATURE));
            weatherInfoList.put(weather_hashmap_key.CITYNAME, findDetail(api_xml_key.WETHER_CITY_NAME));
            weatherInfoList.put(weather_hashmap_key.HUMIDITY, findDetail(api_xml_key.WETHER_HUMIDITY));

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);

            temperaturedegree.setText(weatherInfoList.get(weather_hashmap_key.TEMPERATURE) + (char) 0x00B0 );
            humiditydegree.setText(weatherInfoList.get(weather_hashmap_key.HUMIDITY));
            cityname.setText(weatherInfoList.get(weather_hashmap_key.CITYNAME));
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



}
