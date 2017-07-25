package exampfi.daria.chillaxlinuxtryandroid.Adapters;

/**
 * Created by daria on 12/11/16.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import exampfi.daria.chillaxlinuxtryandroid.Models.Friend;
import exampfi.daria.chillaxlinuxtryandroid.R;

public class FriendAdapter extends BaseAdapter{

    Context ctx;
    LayoutInflater lInflater;
    List<Friend> friends=null;
    ArrayList<Friend> filteredFriends;



    public FriendAdapter(Context context,ArrayList<Friend> objects){
        ctx=context;
        friends=objects;
        this.filteredFriends=new ArrayList<Friend>();
        this.filteredFriends.addAll(objects);
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return (friends.get(position));
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }



    public Friend getFriend(int position) {
        return ((Friend) getItem(position));
    }


    public class ViewHolder {
        TextView name;
        TextView status;
    }



    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = lInflater.inflate(R.layout.item_of_friend_list, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.friend_login);
            holder.status = (TextView) view.findViewById(R.id.friend_status);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(friends.get(position).getName());
        holder.status.setText(friends.get(position).getStatus());

        // Listen for ListView Item Click
        /*view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(ctx, FriendPageActivity.class);
                // Pass all data rank
                intent.putExtra("name",
                        (friends.get(position).getName()));
                // Pass all data country
                intent.putExtra("status",
                        (friends.get(position).getStatus()));

                // Start SingleItemView Class
                ctx.startActivity(intent);
            }
        });*/

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        friends.clear();
        if (charText.length() == 0) {
            friends.addAll(filteredFriends);
        } else {
            for (Friend fp : filteredFriends) {
                if (fp.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    friends.add(fp);
                }
            }
        }
        notifyDataSetChanged();
    }

    }


