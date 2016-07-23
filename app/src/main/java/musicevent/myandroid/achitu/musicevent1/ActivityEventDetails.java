package musicevent.myandroid.achitu.musicevent1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import Model.Event;

public class ActivityEventDetails extends AppCompatActivity {

    private Event event;
    private TextView headLiner;
    private TextView venue;
    private TextView when;
    private TextView where;
    private NetworkImageView bandImage;
    ImageLoader imageLoader= AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_event_details);

        event=(Event)getIntent().getSerializableExtra("eventObj");

        headLiner=(TextView)findViewById(R.id.detsHeadLinerId);
        venue=(TextView)findViewById(R.id.detsVenueId);
        where=(TextView)findViewById(R.id.detsWhereId);
        when=(TextView)findViewById(R.id.detsWhenId);
        bandImage=(NetworkImageView)findViewById(R.id.detsBandImageId);

        headLiner.setText(event.getHeadLiner());
        bandImage.setImageUrl(event.getUrl(), imageLoader);
        venue.setText("Venue: " + event.getVenueName());
        where.setText("Where: " + event.getLine1() + " ," +  event.getCity() + " ,"
                + event.getStateCode() + " ," + event.getPostalCode() + " ," + event.getCountryCode());
        when.setText("When: "+event.getStartDate()+"\n Time zone: "+event.getTimeZone());

        //Log.v("Eventobj:" , event.getHeadLiner());

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_event_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.websiteId) {

            String url= event.getWebsite();
            if(!url.equals("")){
                Intent i= new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
            }else{
                Toast.makeText(getApplicationContext(), "Website not available!", Toast.LENGTH_LONG).show();
            }

        }
            return super.onOptionsItemSelected(item);
        }
    }