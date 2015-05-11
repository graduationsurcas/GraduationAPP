package extra;

/**
 * Created by Gheith on 07/05/2015.
 */
public interface sharedPreferencesKey {

    public static final String PREFERENCES_FILE_NAME ="graduationsurcasoman";
    //status of user if sign in or not {boolean : true/false}
    public static final String PREFERENCES_SIGNIN ="signin";
    //run application first time
    public static final String PREFERENCES_APP_RUN_FIRST_TIME = "runappfirsttime";
    //user info {String}
    public static final String PREFERENCES_USER_NAME ="username";
    public static final String PREFERENCES_USER_LANGUAGE ="userlang";


    //user location coordinates {double number in string format}
    public static final String PREFERENCES_LOCATION_LATITUDE ="locationlang";
    public static final String PREFERENCES_LOCATION_LONGITUDE ="locationlong";

    //update user location every time{LONG in millisecond formate}
    public static final String PREFERENCES_LOCATION_UBDATE_PERIOD_TIME ="updateuserlocationperiodtime";
    public static final String PREFERENCES_LOCATION_LAST_UBDATE_TIME ="updateuserlocationtime";

    //ask about location before{boolean true/false}
    public static final String PREFERENCES_LOCATION_ASKED ="getlocationfirsttime";


    //place order by{string}
    public static final String PREFERENCES_PLACE_ORDER_BY ="placeorderby";
    //number of item select in each time
    public static final String PREFERENCES_PLACE_SELECT_AMOUNT = "placeselectamount";

    public static final String PREFERENCES_WEATHER_TEMPERATURE = "weathertemperature";
    public static final String PREFERENCES_WEATHER_HUMIDITY = "weatherhumidity";
    public static final String PREFERENCES_WEATHER_PLACE = "weatherplace";
    //update user location every time{LONG in millisecond formate}
    public static final String PREFERENCES_WEATHER_UBDATE_PERIOD_TIME ="updateuserweatherperiodtime";
    public static final String PREFERENCES_WEATHER_LAST_UBDATE_TIME ="updateuserweathertime";

    //item
    public static final String PREFERENCES_ITEM_ORDER_BY ="itemorderby";
    public static final String PREFERENCES_ITEM_SELECT_AMOUNT = "itemselectamount";



}
