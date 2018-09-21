package art4muslim.macbook.rahatycustomer.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


import art4muslim.macbook.rahatycustomer.LoginActivity;
import art4muslim.macbook.rahatycustomer.MainActivity;

import java.util.HashMap;

public class
SessionManager {
    // access token
    public static final String KEY_Acesstoken = "access_token";
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    // Sharedpref file name
    private static final String PREF_NAME = "aymanPref";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    private static final  String Key_UserID="ID";
    private static final  String Key_UserName="NAME";
    private static final  String Key_UserSESSION="SESSION";
    public  static final  String Key_UserPhone="USERPHONE";
    public  static final  String Key_UserPhoneHome="USERPHONEHOME";
    public  static final  String Key_UserIMAGE="USERIMAGE";
    private static final  String Key_UserSTATUS="USERSTATUS";
    private static final  String Key_UserEmail="USEREMAIL";
    private static final  String Key_UserMAPX="Key_UserMAPX";
    private static final  String Key_UserMAPY="Key_UserMAPY";
    private static final  String Key_UserCITY_ID="USERCITYID";
    private static final  String Key_UserCITY_NAME="USERCITYNAME";
    private static final  String Key_LANGUAGE="LANGUAGE";
    private static final  String Key_ServiceID="ID_SERVICE";
    private static final  String Key_cityId="CityId";
    private static final  String Key_Currency="Currency";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */

    public void saveLoginState(boolean isLogin){
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.commit();


    }
    public String getAccessToken() {
        return pref.getString(KEY_Acesstoken, null);
    }

    public void createLoginSession(int id,String name, String phone, String phoneHome,String thumbnail, String status) {
        // Storing login value as TRUE

        // storing access token in pref
        editor.putInt(Key_UserID, id);
        Log.v("id", ""+id);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing phone in pref
        editor.putString(Key_UserPhone, phone);
        Log.v("key phone", phone);

        // Storing email in pref
        editor.putString(Key_UserPhoneHome, phoneHome);
        Log.v("key phoneHome", phoneHome);

        // Storing email in pref
        editor.putString(Key_UserIMAGE, thumbnail);
        Log.v("key thumbnail", thumbnail);

        // Storing status in pref
        editor.putString(Key_UserSTATUS, status);
        Log.v("key status", status);

        // commit changes
        editor.commit();
    }
    // to get user id
    public void saveUserId(int userId){
        editor.putInt(Key_UserID,userId);
        editor.commit();


    }
    // to get user id
    public void saveUserName(String userName){
        editor.putString(Key_UserName,userName);
        editor.commit();


    }
    public void saveAccessToken(String accessToken){
        editor.putString(KEY_Acesstoken,accessToken);
        editor.commit();


    }
    public void saveUserSESSION(String userSession){
        editor.putString(Key_UserSESSION,userSession);
        editor.commit();


    }

    public void saveUser_phone(String user_phone){
        editor.putString(Key_UserPhone,user_phone);
        editor.commit();


    }

    public void saveUser_city_id(String user_city){
        editor.putString(Key_UserCITY_ID,user_city);
        editor.commit();


    }
    public void saveUser_city_name(String user_cityName){
        editor.putString(Key_UserCITY_NAME,user_cityName);
        editor.commit();


    }
    public void saveUserLanguage(String lng){
        editor.putString(Key_LANGUAGE,lng);
        editor.commit();


    }
    public void saveServiceId(int serviceId){
        editor.putInt(Key_ServiceID,serviceId);
        editor.commit();


    }
    public void savecityid(int cityid){
        editor.putInt(Key_cityId,cityid);
        editor.commit();
    }
    public void saveCurrencyCountry(String cityid){
        editor.putString(Key_Currency,cityid);
        editor.commit();
    }

    public  void  SaveDriverIdentityImage(String DriverIdentityImage){
        editor.putString("DriverIdentityImage",DriverIdentityImage);
        editor.commit();

    }
    public void SaveDriverLicence(String DriverLicence){
        editor.putString("DriverLicence",DriverLicence);
        editor.commit();


    }
    public void SaveCarLicensce(String CarLicensce){
        editor.putString("CarLicensce",CarLicensce);
        editor.commit();


    }
    public void SaveDriverPersonalImageID(String DriverPersonalImageID){
        editor.putString("DriverPersonalImageID",DriverPersonalImageID);
        editor.commit();

    }
    public void SaveCarFrontImage(String CarFrontImage){
        editor.putString("CarFrontImage",CarFrontImage);
        editor.commit();


    }
    public void SaveCarBackImage(String CarBackImage){
        editor.putString("CarBackImage",CarBackImage);
        editor.commit();


    }
    /**to retrive
     * Gson gson = new Gson();
     String json = mPrefs.getString("MyObject", "");
     MyObject obj = gson.fromJson(json, MyObject.class);
     * **/
     public  void SaveDriverIbfo(String myobject, String json){
         editor.putString(myobject, json);
         editor.commit();
     }
    public  String getDriverInfo( String myobject){


   return pref.getString(myobject, null);
    }

    public void DeleteImages(){
        editor.remove("DriverIdentityImage");
        editor.remove("DriverLicence");
        editor.remove("CarLicensce");
        editor.remove("DriverPersonalImageID");
        editor.remove("CarFrontImage");
        editor.remove("CarBackImage");
        editor.remove("DriverPersonalImageID");
        editor.commit();
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String > getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        //access token
        user.put(KEY_Acesstoken, pref.getString(KEY_Acesstoken, null));

        //  Log.v("Key_UserID", user.put(Key_UserID,pref.getString(Key_UserID,null)));
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(Key_UserPhone, pref.getString(Key_UserPhone, null));
        user.put(Key_UserPhoneHome, pref.getString(Key_UserPhoneHome, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        //city id

        user.put(Key_cityId, String.valueOf(pref.getInt(Key_cityId,0)));

        return user;
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (this.isLoggedIn()) {
            // user is  logged in redirect him to main Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
          //  i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
          //  System.exit(0);
        }

    }


    public HashMap<String ,Integer> getUserId(){
        HashMap<String, Integer> usrId = new HashMap<String, Integer>();
        // customer or user id
        usrId.put(Key_UserID, pref.getInt(Key_UserID,0));
        return usrId;
    }

    public String getUserName(){
        //  HashMap<String, String> usrId = new HashMap<String, String>();
        // customer or user id
        //  usrId.put(Key_Currency, pref.getString(Key_Currency));

        return pref.getString(Key_UserName,"");
    }  public String getUserSESSION(){
        //  HashMap<String, String> usrId = new HashMap<String, String>();
        // customer or user id
        //  usrId.put(Key_Currency, pref.getString(Key_Currency));

        return pref.getString(Key_UserSESSION,"");
    }

    public String getUserPhone(){
        //  HashMap<String, String> usrId = new HashMap<String, String>();
        // customer or user id
        //  usrId.put(Key_Currency, pref.getString(Key_Currency));

        return pref.getString(Key_UserPhone,"");
    }
    public String getUserCityId(){
        //  HashMap<String, String> usrId = new HashMap<String, String>();
        // customer or user id
        //  usrId.put(Key_Currency, pref.getString(Key_Currency));

        return pref.getString(Key_UserCITY_ID,"");
    }
    public String getKey_UserCITY_NAME(){
        //  HashMap<String, String> usrId = new HashMap<String, String>();
        // customer or user id
        //  usrId.put(Key_Currency, pref.getString(Key_Currency));

        return pref.getString(Key_UserCITY_NAME,"");
    }
    public HashMap<String ,Integer> getServiceId(){
        HashMap<String, Integer> usrId = new HashMap<String, Integer>();
        // customer or user id
        usrId.put(Key_ServiceID, pref.getInt(Key_ServiceID,0));
        return usrId;
    }
    public String getKey_LANGUAGE(){
      //  HashMap<String, String> usrId = new HashMap<String, String>();
        // customer or user id
      //  usrId.put(Key_Currency, pref.getString(Key_Currency));

        return pref.getString(Key_LANGUAGE,null);
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();


        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);

    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


    public void saveUser_email(String user_email) {
        editor.putString(Key_UserEmail,user_email);
        editor.commit();
    }
    public String getUserEmail(){

        return pref.getString(Key_UserEmail,"ar");
    }

    public void saveUser_mapx(String user_mapx) {
        editor.putString(Key_UserMAPX,user_mapx);
        editor.commit();
    }
    public String getUserMapx(){

        return pref.getString(Key_UserMAPX,"0.0");
    }
    public void saveUser_mapy(String user_mapy) {
        editor.putString(Key_UserMAPY,user_mapy);
        editor.commit();
    }
    public String getUserMapy(){

        return pref.getString(Key_UserMAPY,"0.0");
    }

}
