package com.bond95.litesender;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by bond95 on 7/21/16.
 */
public class DeviceArrayAdapter extends ArrayAdapter<DeviceListItem> {

    private int id;
    private List<DeviceListItem> items;
    private Context c;

    public DeviceArrayAdapter(Context context, int textViewResourceId,
                            List<DeviceListItem> objects) {
        super(context, textViewResourceId, objects);
        c = context;
        id = textViewResourceId;
        items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }

               /* create a new view of my layout and inflate it in the row */
        //convertView = ( RelativeLayout ) inflater.inflate( resource, null );

        final DeviceListItem o = items.get(position);
        if (o != null) {
            TextView t1 = (TextView) v.findViewById(R.id.fileName);

            if(t1!=null)
                t1.setText(o.getName());
        }
        return v;
    }
}
