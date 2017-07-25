package exampfi.daria.chillaxlinuxtryandroid.Views;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import exampfi.daria.chillaxlinuxtryandroid.Adapters.FriendAdapter;
import exampfi.daria.chillaxlinuxtryandroid.App.AppController;
import exampfi.daria.chillaxlinuxtryandroid.Models.Friend;
import exampfi.daria.chillaxlinuxtryandroid.R;

import static exampfi.daria.chillaxlinuxtryandroid.Views.Appointment.SharedPref;


public class FriendsOverviewActivity extends AppCompatActivity {

    public ArrayList<Friend> myfriends=new ArrayList<Friend>();


    FriendAdapter friendAdapter;
    EditText inputSearch;
    ListView friendslist;
    private String mUrl = "https://193.105.219.190:44334/api/Friend/name/status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_overview);
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        fillData();
        friendAdapter = new FriendAdapter(this, myfriends);
        friendslist=(ListView)findViewById(R.id.list_of_friends);
        friendslist.setAdapter(friendAdapter);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                friendAdapter.filter(text);
            }
        });

        friendslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Friend frChat=myfriends.get(position);
                String nameChatter=frChat.getName();
                Intent intent=new Intent(FriendsOverviewActivity.this,ChatRoomActivity.class);
                intent.putExtra("myChatter",nameChatter);
                startActivity(intent);
            }
        });
        friendslist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FriendsOverviewActivity.this);

                builder.setIcon(R.drawable.ic_error);

                builder.setMessage("Invite friend to the appointment ?");

                builder.setTitle("Invite");

                builder.setPositiveButton("Sure, invite!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Friend bf=myfriends.get(position);
                        String TaskName=bf.getName();
                        SharedPreferences settings=null;//declaration

                        settings=getSharedPreferences(SharedPref,2);


                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("FriendInvited", TaskName);
                        editor.commit();
                        finishFromChild(FriendsOverviewActivity.this);
                    }
                });
                builder.setNegativeButton("Gosh, no...", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });


// Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            }
        });
    }

    void fillData() {
        myfriends.add(new Friend("Harry Potter", "BFFs"));
        myfriends.add(new Friend("Draco Malfoy", "Boyfriend"));
        myfriends.add(new Friend("Daeneris", "Sis"));
        myfriends.add(new Friend("Neil Druckmann", "Genius"));
        myfriends.add(new Friend("Peter the Great", "NARNIA!"));
        myfriends.add(new Friend("Dustan", "Abs Prince"));
    }

    void fillDataChillax(){
        JsonArrayRequest movieReq = new JsonArrayRequest(mUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d(TAG, response.toString());


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Friend friend = new Friend();
                                friend.setName(obj.getString("name"));
                                friend.setStatus(obj.getString("status"));



                                // adding movie to movies array
                                myfriends.add(friend);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        friendAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
}
