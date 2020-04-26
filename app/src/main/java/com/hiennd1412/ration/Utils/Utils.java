package com.hiennd1412.ration.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import com.hiennd1412.ration.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

public class Utils {

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Checks if the device has Internet connection.
     *
     * @return <code>true</code> if the phone is connected to the Internet.
     */
    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        Network[] networks = cm.getAllNetworks();
        for(Network network: networks) {
            NetworkInfo networkInfo = cm.getNetworkInfo(network);
            if(networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        }

        return false;
    }

    public static SharedPreferences getCommonSharepreference(Context context) {
        return context.getSharedPreferences(context.getResources().getString(R.string.common_preference),MODE_PRIVATE);
    }

    public static int getIntFromJSonObject(JSONObject jsonObjectData, String key) {
        try {
            return jsonObjectData.getInt(key);
        }
        catch (JSONException exception) {
            return 0;
        }
    }
    public static float getFloatFromJSonObject(JSONObject jsonObjectData, String key) {
        try {
            return (float) jsonObjectData.getDouble(key);
        }
        catch (JSONException exception) {
            return 0;
        }
    }

    public static String getStringFromJSonObject(JSONObject jsonObjectData, String key) {
        try {
            return jsonObjectData.getString(key);
        }
        catch (JSONException exception) {
            return "N/A";
        }
    }

    public static Boolean isEmailValid(String emailToValidate) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+";
        if(emailToValidate.matches(emailPattern)) {
            return true;
        }
        return false;
    }

    public static Boolean isUsernameValid(String usernameToValid) {
        String usernamePattern = "[a-zA-Z ]+";
        if(usernameToValid.matches(usernamePattern)) {
            return true;
        }
        return false;
    }

    public static String formatDate(String dateToFormat) {
        String s = "";
        SimpleDateFormat oldStringDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        oldStringDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        SimpleDateFormat newStringDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone localTimeZone = TimeZone.getDefault();

        System.out.println("TimeZone   " + localTimeZone.getDisplayName(false, TimeZone.SHORT) + " Timezon id :: " + localTimeZone.getID());
        newStringDateFormat.setTimeZone(localTimeZone);

        try {
            Date d = oldStringDateFormat.parse(dateToFormat);
            s = newStringDateFormat.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String formatDate(Date dateToFormat) {
        if (dateToFormat == null)
            return "";
        SimpleDateFormat newStringDateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        TimeZone localTimeZone = TimeZone.getDefault();

        System.out.println("TimeZone   " + localTimeZone.getDisplayName(false, TimeZone.SHORT) + " Timezon id :: " + localTimeZone.getID());
        newStringDateFormat.setTimeZone(localTimeZone);

        return newStringDateFormat.format(dateToFormat);
    }

    public static String formatDateToSendToServer(Date dateToFormat) {
        if (dateToFormat == null)
            return "";
        SimpleDateFormat newStringDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        TimeZone localTimeZone = TimeZone.getDefault();

        System.out.println("TimeZone   " + localTimeZone.getDisplayName(false, TimeZone.SHORT) + " Timezon id :: " + localTimeZone.getID());
        newStringDateFormat.setTimeZone(localTimeZone);

        return newStringDateFormat.format(dateToFormat);
    }



    public static String random() {
        String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random RANDOM = new Random();
        int stringLength = 12;
        StringBuilder sb = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }
        return sb.toString();

    }
}
