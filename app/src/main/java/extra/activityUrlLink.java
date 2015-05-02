package extra;

/**
 * Created by Gheith on 01/05/2015.
 */
public class activityUrlLink {

    public static final String MAIN_LINK  = "http://192.168.43.19/graduationsurcas/appactivitesconntents/";
    public static final String PLACE_URL = MAIN_LINK + "places.php";

    public static String[] getLinkContents(String link){
        link = link.substring(link.indexOf('*')+1, link.length());
       return link.split("~");
    }

    public static String getPlaceInfoPage(String placeid) {
        return MAIN_LINK + "placeinfo.php?id="+placeid;
    }
}
