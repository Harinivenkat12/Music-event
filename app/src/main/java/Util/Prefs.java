package Util;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by achitu on 7/21/16.
 */
public class Prefs {
    SharedPreferences preferences;

    public Prefs(Activity activity){
        preferences= activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public void setCity(String city){
        preferences.edit().putString("city",city).commit();//changes the city that user wants
    }

    public String getCity(){
        return preferences.getString("city", "Baltimore");//if the user has not entered any city, restore to default
    }
}

