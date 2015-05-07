package extra;

/**
 * Created by Gheith on 07/05/2015.
 */
public interface sharedPreferencesKey {

    public static final String PREFERENCES_FILE_NAME ="graduationsurcasoman";
    //status of user if sign in or not {boolean : true/false}
    public static final String PREFERENCES_SIGNIN ="signin";
    //user info {String}
    public static final String PREFERENCES_USER_NAME ="username";

    //user location coordinates {float number}
    public static final String PREFERENCES_LOCATION_LATITUDE ="locationlang";
    public static final String PREFERENCES_LOCATION_LONGITUDE ="locationlong";

    //update user location every time{LONG in millisecond formate}
    public static final String PREFERENCES_LOCATION_UBDATE_PERIOD_TIME ="updateuserlocationperiodtime";
    public static final String PREFERENCES_LOCATION_LAST_UBDATE_TIME ="updateuserlocationtime";

    //ask about location before{boolean true/false}
    public static final String PREFERENCES_LOCATION_ASKED ="getlocationfirsttime";


    //place order by{string}
    public static final String PREFERENCES_PLACE_ORDER_BY ="placeorderby";



}
