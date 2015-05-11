package extra;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Gheith on 01/05/2015.
 */
public class activityUrlLink {


    public static final String MAIN_LINK  = "http://192.168.1.13/graduationsurcas/appactivitesconntents/";
    public static final String PLACES_LIST_URL = MAIN_LINK + "places.php";
    public static final String ITEMS_LIST_URL = MAIN_LINK + "itemlist.php";

    public static String[] getLinkContents(String link){
        link = link.substring(link.indexOf('*')+1, link.length());
       return link.split("~");
    }


    public static String getPlacesListUrl(Context context, String orderby, int amount) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(sharedPreferencesKey.PREFERENCES_FILE_NAME,
                context.MODE_PRIVATE);
        String url = PLACES_LIST_URL;
        if(orderby.equalsIgnoreCase("date")){
            url = url+ "?orderby="+orderby
                    +"&selectamount="+amount;
        }else if(orderby.equalsIgnoreCase("location")){
            GeneralFunction.getLocation(context);
            url = url+ "?orderby="+orderby
                    +"&locationlong="+sharedpreferences.getString(sharedPreferencesKey.PREFERENCES_LOCATION_LONGITUDE, "23.58734")
                    +"&locationlat="+sharedpreferences.getString(sharedPreferencesKey.PREFERENCES_LOCATION_LATITUDE, "58.25188")
                    +"&selectamount="+amount;
        }
        return url+userToken(context);
    }

    public static String getPlaceInfoPage(String placeid, Context context) {
        return MAIN_LINK + "placeinfo.php?id="+placeid
                +userToken(context);
    }

    public static String getItemsListUrl(Context context, String orderby, int amount, long placeid) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(sharedPreferencesKey.PREFERENCES_FILE_NAME,
                context.MODE_PRIVATE);
        String url = ITEMS_LIST_URL;
        if(orderby.equalsIgnoreCase("date")){
            url = url+ "?orderby="+orderby
                    +"&selectamount="+amount;
        }else if(orderby.equalsIgnoreCase("location")){
            GeneralFunction.getLocation(context);
            url = url+ "?orderby="+orderby
                    +"&locationlong="+sharedpreferences.getString(sharedPreferencesKey.PREFERENCES_LOCATION_LONGITUDE, "23.58734")
                    +"&locationlat="+sharedpreferences.getString(sharedPreferencesKey.PREFERENCES_LOCATION_LATITUDE, "58.25188")
                    +"&selectamount="+amount;
        }else if (placeid > -1){
            url = url+ "?placeid="+placeid
                    +"&selectamount="+amount;
        }
        return url+userToken(context);
    }

    public static String getItemInfoPage(String itemid, Context context) {
        return MAIN_LINK + "iteminfo.php?id="+itemid
                +userToken(context);
    }

    public static String getServicesList(Context context, String orderby, int amount) {
        return MAIN_LINK + "serviceslist.php?"
                +userToken(context);
    }

    public static String getServicesProvideorInfo(Context context, String userid, int amount) {
        return MAIN_LINK + "serviceprovider.php?"
                +"id="+userid
                +userToken(context);
    }

    public static String userToken(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(sharedPreferencesKey.PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        String lang = sharedpreferences.getString(sharedPreferencesKey.PREFERENCES_USER_LANGUAGE, "en");
        boolean signin = sharedpreferences.getBoolean(sharedPreferencesKey.PREFERENCES_SIGNIN, false);
        String token = "&lang="+lang
                +"&sign="+((signin==true)?"true" : "false");
        return token;
    }
}
