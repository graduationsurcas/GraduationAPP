package extra;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;

import graduationsurcas.com.graduationapp.R;

/**
 * Created by Gheith on 08/05/2015.
 */
public class GeneralFunction {

    public static void getLocation(Context context) {




        SharedPreferences sharedpreferences = context.getSharedPreferences(sharedPreferencesKey.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedpreferenceseditor = sharedpreferences.edit();

        GPStracker gps = new GPStracker(context);
        if (gps.isCanGetLocation()) {
            sharedpreferenceseditor.putString(sharedPreferencesKey.PREFERENCES_LOCATION_LATITUDE,
                    String.valueOf(gps.getLatitude()));
            sharedpreferenceseditor.commit();
            sharedpreferenceseditor.putString(sharedPreferencesKey.PREFERENCES_LOCATION_LONGITUDE,
                    String.valueOf(gps.getLongitude()));
            sharedpreferenceseditor.commit();

            Log.d("location", "lat "+gps.getLatitude());
            Log.d("location", "long "+gps.getLongitude());
            sharedpreferences.getString(sharedPreferencesKey.PREFERENCES_LOCATION_LATITUDE, "0.0");
        } else {
            gps.showSettingAlert();
        }
        if (Double.valueOf(sharedpreferences.getString(sharedPreferencesKey.PREFERENCES_LOCATION_LATITUDE, "0.0"))
                == 0.0
                ||
                Double.valueOf(sharedpreferences.getString(sharedPreferencesKey.PREFERENCES_LOCATION_LONGITUDE, "0.0"))
                        == 0.0) {
            //muscat is the default location
            sharedpreferenceseditor.putString(sharedPreferencesKey.PREFERENCES_LOCATION_LATITUDE,
                    String.valueOf(23.58734));
            sharedpreferenceseditor.commit();
            sharedpreferenceseditor.putString(sharedPreferencesKey.PREFERENCES_LOCATION_LONGITUDE,
                    String.valueOf(58.25188));
            sharedpreferenceseditor.commit();
        }
    }

    public static void openPlaceLocationOnMap(Context context, String latitude, String longitude){
        Intent mapa = new Intent(Intent.ACTION_VIEW);
        mapa.setData(Uri.parse("geo:" + latitude + "," + longitude));
        Intent choser = Intent.createChooser(mapa, "Lunch Map");
        context.startActivity(choser);
//        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//        context.startActivity(intent);
    }

    public static void shareWithAnotherApp(String text, Context context){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent,
                context.getResources().getText(R.string.send_to)));
    }


    public static String getLanguageCode(String language){
        HashMap<String, String> cache = new HashMap<String, String>();
        cache.put("English", "en");
        cache.put("German", "de");
        cache.put("Portuguese", "pt");
        cache.put("Spanish", "es");
        cache.put("Italian", "it");
        cache.put("French", "fr");
        return cache.get(language);
    }


}
