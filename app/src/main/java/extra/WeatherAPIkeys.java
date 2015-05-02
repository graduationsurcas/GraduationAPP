package extra;

/**
 * Created by Gheith on 02/05/2015.
 */
public interface WeatherAPIkeys {

    public static final String WETHER_API_KEY = "596c95bcfc4acd3c";

    public interface api_xml_key{
        public static final String WETHER_TEMPERATURE = "//current_observation/temp_c";
        public static final String WETHER_CITY_NAME = "//current_observation/display_location/full";
        public static final String WETHER_HUMIDITY = "//current_observation/relative_humidity";
    }

    public interface weather_hashmap_key{
        public static final String TEMPERATURE = "temperature";
        public static final String CITYNAME = "city";
        public static final String HUMIDITY = "humidity";
    }



//    public interface keygroup{}
}
