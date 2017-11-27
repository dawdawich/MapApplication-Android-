package com.example.dawdawich.locator.app;

//get information from https://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/

public class AppConfig {

    // Server user login url
    public static String URL_LOGIN = "http://91.241.176.8:8080/login";
    //public static String URL_LOGIN = "http://192.168.1.252:8080/login";

    // Server user register url
    public static String URL_REGISTER = "http://91.241.176.8:8080/register";
//    public static String URL_REGISTER = "http://192.168.1.252:8080/register";

    public static String URL_GETPOSITION = "http://91.241.176.8:8080/get_position";
    //public static String URL_GETPOSITION = "http://192.168.1.252:8080/get_position";

    public static String URL_UPDATEPOSITION = "http://91.241.176.8:8080/update_position";
    //public static String URL_UPDATEPOSITION = "http://192.168.1.252:8080/update_position";

    public static String URL_GETUSERINFO = "http://91.241.176.8:8080/get_user_info";
    //public static String URL_GETUSERINFO = "http://192.168.1.252:8080/get_user_info";

    public static String URL_SEARCHUSERS = "http://91.241.176.8:8080/search_friends";
    //public static String URL_SEARCHUSERS = "http://192.168.1.252:8080/search_friends";

    public static String URL_SENDINVITE = "http://91.241.176.8:8080/send_invite";
    //public static String URL_SENDINVITE = "http://192.168.1.252:8080/send_invite";

    public static String URL_CONFIRMINVITE = "http://91.241.176.8:8080/confirm_invitation";
    //public static String URL_CONFIRMINVITE = "http://192.168.1.252:8080/confirm_invitation";

    public static String URL_ISUPDATE = "http://91.241.176.8:8080/is_update";

}
