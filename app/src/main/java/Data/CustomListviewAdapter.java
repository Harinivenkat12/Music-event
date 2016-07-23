package Data;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import Model.Event;
import musicevent.myandroid.achitu.musicevent1.ActivityEventDetails;
import musicevent.myandroid.achitu.musicevent1.AppController;
import musicevent.myandroid.achitu.musicevent1.R;

/**
 * Created by achitu on 7/21/16.
 */
public class CustomListviewAdapter extends ArrayAdapter<Event> {

    private LayoutInflater inflater;
    private ArrayList<Event> data;
    private Activity mContext;
    private int layoutResourceId;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public CustomListviewAdapter(Activity context, int resource, ArrayList<Event> objects) {
        super(context, resource, objects);
        data = objects;
        mContext = context;
        layoutResourceId = resource;


    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Event getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(Event item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewholder = null;

        if (row == null) {
            inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(layoutResourceId, parent, false);

            viewholder=new ViewHolder();

            viewholder.bandImage=(NetworkImageView)row.findViewById(R.id.bandImageId);
            viewholder.headLiner=(TextView)row.findViewById(R.id.headLinerTextId);
            viewholder.venue=(TextView)row.findViewById(R.id.venueTextId);
            viewholder.where=(TextView)row.findViewById(R.id.whereTextId);
            viewholder.when=(TextView)row.findViewById(R.id.whenTextId);
            row.setTag(viewholder);
        }else {
            viewholder=(ViewHolder)row.getTag();
        }
        viewholder.event=data.get(position);

        viewholder.headLiner.setText("Title: "+viewholder.event.getHeadLiner());
        viewholder.venue.setText("Venue: "+viewholder.event.getVenueName());
        viewholder.when.setText("When: "+viewholder.event.getStartDate()+"\n Time zone: "+viewholder.event.getTimeZone());
        viewholder.where.setText("Where: " + viewholder.event.getLine1() + ", " + viewholder.event.getCity() + ", " + viewholder.event.getCountryCode());
        viewholder.bandImage.setImageUrl(viewholder.event.getUrl(), imageLoader);
        viewholder.website=viewholder.event.getWebsite();

        //Log.v("WHEN: ",viewholder.event.getStartDate()+", Time zone: "+viewholder.event.getTimeZone() );

        final ViewHolder finalViewholder = viewholder;
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(mContext, ActivityEventDetails.class);
                Bundle mBundle= new Bundle();
                mBundle.putSerializable("eventObj", finalViewholder.event);
                i.putExtras(mBundle);
                mContext.startActivity(i);

            }
        });

        return row;
    }

    public class ViewHolder{
        TextView headLiner;
        TextView venue;
        TextView where;
        TextView when;
        String website;
        Event event;
        NetworkImageView bandImage;
    }
}