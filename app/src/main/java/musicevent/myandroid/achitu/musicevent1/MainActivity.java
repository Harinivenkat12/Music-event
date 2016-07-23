package musicevent.myandroid.achitu.musicevent1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import Data.CustomListviewAdapter;
import Model.Event;
import Util.Prefs;

public class MainActivity extends AppCompatActivity {
    private CustomListviewAdapter adapter;
    private ArrayList<Event> events = new ArrayList<>();
    private ListView listView;
    private TextView selectedCity;
    private ProgressDialog progressDialog;
    private String urlLeft = "https://api.ticketfly.com/v2/search-results";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listViewid);
        adapter = new CustomListviewAdapter(MainActivity.this, R.layout.list_row, events);
        listView.setAdapter(adapter);

        selectedCity=(TextView)findViewById(R.id.selectedLocationId);

        Prefs prefs= new Prefs(MainActivity.this);
        String city= prefs.getCity();

        selectedCity.setText("Selected city: "+ city);


        showEvents(city);


    }


    private void getEvents(final String city) {
        //clear data first
        events.clear();

        progressDialog= new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String finalURL;

        try {
            finalURL = String.format("%s?q=%s", urlLeft, URLEncoder.encode(city, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        JsonObjectRequest eventsRequest = new JsonObjectRequest(Request.Method.GET, finalURL, (JSONObject) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                hideProgressdialog();

                try {

                    JSONArray eventsArray = response.getJSONArray("searchResults");

                    for (int i = 0; i < eventsArray.length(); i++) {
                        JSONObject jsonObject = eventsArray.getJSONObject(i);


                        JSONObject linkObj = eventsArray.getJSONObject(i);
                        String link = linkObj.getString("link");

                        //getting title of music events from tf api
                        JSONObject titleObj = eventsArray.getJSONObject(i);
                        String title = titleObj.getString("title");

                        Log.v("Title: ", title);

                        //getting imageURL from tf api
                        JSONObject imageObject = eventsArray.getJSONObject(i);
                        String actualImage = imageObject.getString("imageUrl");

                        //get website link from tf api
                        String websiteURL = "https://api.ticketfly.com/v2";
                        websiteURL = websiteURL + link;
                        Log.v("Website: ", websiteURL);


                        //get address from api
                        JSONObject propertiesObject = jsonObject.getJSONObject("properties");
                        String timeZone = propertiesObject.getString("timeZone");
                        String time = propertiesObject.getString("time");

                        JSONObject addressObject = propertiesObject.getJSONObject("address");
                        String venueName = addressObject.getString("name");
                        String addressLine1 = addressObject.getString("line1");
                        String addressLine2 = addressObject.getString("line2");
                        String city = addressObject.getString("city");
                        String stateCode = addressObject.getString("stateCode");
                        String postalCode = addressObject.getString("postalCode");
                        String countryCode = addressObject.getString("countryCode");

                        Event event = new Event();

                        event.setTimeZone(timeZone);
                        event.setStartDate(time);
                        event.setWebsite(link);
                        event.setCity(city);
                        event.setHeadLiner(venueName);
                        event.setVenueName(venueName);
                        event.setLine1(addressLine1);
                        event.setLine2(addressLine2);
                        event.setStateCode(stateCode);
                        event.setPostalCode(postalCode);
                        event.setCountryCode(countryCode);
                        event.setUrl(actualImage);
                        event.setWebsite(websiteURL);

                        events.add(event);


                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideProgressdialog();

            }
        });
        AppController.getInstance().addToRequestQueue(eventsRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.changeLocationID) {
                showInputdialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showInputdialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Change City");

        final EditText cityInput = new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("Seattle");
        builder.setView(cityInput);//to have the cityinput edittext within the alert dialog builder

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Prefs cityPreference = new Prefs(MainActivity.this);
                cityPreference.setCity(cityInput.getText().toString());
                String newCity = cityPreference.getCity();

                selectedCity.setText("Selected City: " + newCity);
//re-render everything again
                showEvents(newCity);
            }
        });
        builder.show();
    }
    private void showEvents(String newCity) {
        getEvents(newCity);
    }

    private void hideProgressdialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


}
