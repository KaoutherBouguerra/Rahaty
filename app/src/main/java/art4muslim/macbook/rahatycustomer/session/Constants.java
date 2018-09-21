package art4muslim.macbook.rahatycustomer.session;

/**
 * Created by macbook on 17/10/2017.
 */

public class Constants {

    private static final String baseUrl="https://rahaty.app/api/v1/client/"; // DEV
    public static final String baseUrlImages="https://rahaty.app/"; // DEV
    public static final String graphBaseUrl="http://graph.facebook.com/";
    public static final String image_baseUrl="http://bl.box2home.xyz/web/Clients/";

    public static final String SIGNUP_URL = baseUrl+"register";
    public static final String RESET_PASSWORD_URL = baseUrl+"reset-password";
    public static final String VERIFY_URL = baseUrl+"verify";

    public static final String LOGIN_URL=baseUrl+"login";
    public static final String GET_PRODUCTS_URL=baseUrl+"products";
    public static final String PROFILE_URL=baseUrl+"profile";
    public static final String GET_NOTIFICATION_URL=baseUrl+"notifications?";
    public static final String GET_PENDING_ORDERS_URL=baseUrl+"pending-orders?";
    public static final String GET_ORDERS_URL=baseUrl+"orders";
    public static final String GET_SETTINGS_URL="https://rahaty.app/api/v1/settings";
    public static final String GET_DETAILS_ORDERS_URL=baseUrl+"show-order?";
    public static final String CONTACT_US_URL=baseUrl+"contact-us";
    public static final String EDIT_ORDER_URL=baseUrl+"edit-order";
    public static final String REGISTER_TOKEN_URL=baseUrl+"register-token";
    public static final String CONFIRM_ORDER_URL=baseUrl+"confirm-order";
    public static final String CANCEL_ORDER_URL=baseUrl+"cancel-order";
    public static final String DELETE_ORDER_URL=baseUrl+"delete-order";
    public static final String OFFER_URL=baseUrl+"offer";
    public static final String NEW_ORDERS_URL=baseUrl+"new-order";
    public static final String GET_VEHICULES_URL=baseUrl+"chauffeur/getVehicules";
    public static final String ZONE_INFLUENCE_URL=baseUrl+"getZoneInfluence";
    public static final String GET_CATEGORIES="http://rahaty.art4muslim.net/api/v1/categories";


    //debug, release
    public static String androiStudioMode="release";

    public static final String KEY_EMAIL = "mail";
    public static final String KEY_EMAIL_CHANGE = "email";
    public static final String KEY_USERNAME= "username";
    public static final String KEY_ID= "id";
    public static final String KEY_ID_CONNECTED= "idConnected";
    public static final String KEY_DISTANCE= "distance";
    public static final String KEY_DRIVER_TYPE= "driverType";
    public static final String KEY_VEHICULE= "vehicule";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_CONFIRM_PASSWORD = "conf_password";
    public static final String KEY_FIRST_NAME = "firstname";
    public static final String KEY_NAME_SOS = "nomSociete";
    public static final String KEY_LAST_NAME = "lastname";
    public static final String KEY_PHONE = "phone";
    public static final String SAV_PHONE = "+33160191911";
    public static final String STATUS_ENELEVEMENT = "ENLEVEMENT";
    public static final String STATUS_DELEVERING = "LIVRAISON";
    public static final String STATUS_DEPOSING = "DECHARGEMENT";
    public static final String KEY_API_TOKEN = "api_token";
    public static final String arabic = "ar";
    public static final String english = "en";



}
