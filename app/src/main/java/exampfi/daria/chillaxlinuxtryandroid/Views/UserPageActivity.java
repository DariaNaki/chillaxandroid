package exampfi.daria.chillaxlinuxtryandroid.Views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.AdapterView.OnItemSelectedListener;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import exampfi.daria.chillaxlinuxtryandroid.Models.User;
import exampfi.daria.chillaxlinuxtryandroid.R;


public class UserPageActivity extends Activity {

    private ImageButton mHomeButtonUP;
    private ImageButton mAddFriendButtonUP;
    private ImageButton mFriendButtonUP;
    private ImageButton mChatButtonUP;
    private ImageButton mMapButtonUP;
    private ImageButton mAddPicButton;
    private ImageButton mAppointmentsUP;

    private ImageButton mPostUP;
    private static final int CM_DELETE_ID = 1;

    private EditText mPostText;
    private TextView mPostDate;
    private TextView mPostTime;

    private Spinner mSpinnerStatus;
    private ImageView mAvatar;
    private Bitmap bitmap;
    private ImageView mWallAvatar;
    private  Uri selectedImageUri;

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "UserPageActivity";
    // імена атрибутів для Map
    private final String ATTRIBUTE_NAME_TEXT = "text";
    private final String ATTRIBUTE_NAME_IMAGE = "image";
    //private final String ATTRIBUTE_NAME_DATE = "date";
   // private final String ATTRIBUTE_NAME_TIME = "time";
    private final String[] from = { ATTRIBUTE_NAME_TEXT, ATTRIBUTE_NAME_IMAGE/*,ATTRIBUTE_NAME_DATE, ATTRIBUTE_NAME_TIME*/};
    // масив ID View-компонентів, в які будемо вставляти дані
    private final int[] to = { R.id.wall_post, R.id.wall_pic/*,R.id.date_post,R.id.time_post*/};
    private ListView lvSimple;
    private SimpleAdapter sAdapter;
    private ArrayList<Map<String, Object>> dataList;
    private Map<String, Object> m;
    private int swap=0;

    private TextView mUserLogin;
    String[] statusSpinnerdata = {"Not today", "May go out", "50/50", "Feeling adventurous", "Desperate to vent out"};
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mHomeButtonUP = (ImageButton) findViewById(R.id.user_home_page);
        mAddFriendButtonUP = (ImageButton) findViewById(R.id.user_add_friend);
        mFriendButtonUP = (ImageButton) findViewById(R.id.user_friends);
        mChatButtonUP = (ImageButton) findViewById(R.id.user_messages);
        mMapButtonUP = (ImageButton) findViewById(R.id.user_map);
       // mAddPicButton = (ImageButton) findViewById(R.id.post_picture);
        mAppointmentsUP = (ImageButton) findViewById(R.id.user_appointment);
        mPostUP=(ImageButton)findViewById(R.id.post_button);
        mAvatar=(ImageView)findViewById(R.id.user_av);
      //  mPostDate=(TextView)findViewById(R.id.date_post);
       // mPostTime=(TextView)findViewById(R.id.time_post);

        mWallAvatar=(ImageView)findViewById(R.id.wall_pic);
        mUserLogin=(TextView)findViewById(R.id.user_login);
       // Context context=this;
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        //String desiredPreference =  sharedPreferences.getString("LOGIN", "DefaultValue");
        //mUserLogin.setText(desiredPreference);
        /*SimpleDateFormat dateF = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String date = dateF.format(Calendar.getInstance().getTime());
        String time = timeF.format(Calendar.getInstance().getTime());
        mPostDate.setText(date);
        mPostTime.setText(time);*/

        dataList = new ArrayList<Map<String, Object>>();
        sAdapter = new SimpleAdapter(this, dataList, R.layout.item_wall, from, to);

        lvSimple = (ListView) findViewById(R.id.wall);
        lvSimple.setAdapter(sAdapter);
        lvSimple.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dataList.remove(position);
                sAdapter.notifyDataSetChanged();
                swap = 0;
                return false;
            }
        });

        lvSimple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (swap == 0) {
                    swap = position;
                    Toast.makeText(getApplicationContext(), "Select 1", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> m1, m2;
                    m1 = (Map<String, Object>) parent.getItemAtPosition(swap);
                    m2 = (Map<String, Object>) parent.getItemAtPosition(position);
                    dataList.set(swap, m2);
                    dataList.set(position, m1);
                    sAdapter.notifyDataSetChanged();
                    swap = 0;
                    Toast.makeText(getApplicationContext(), "Swaps", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSpinnerStatus=(Spinner)findViewById(R.id.status_spinner);
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusSpinnerdata);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerStatus.setAdapter(adapterSpinner);
        mSpinnerStatus.setPrompt("Chillax status");
        mSpinnerStatus.setSelection(4);

        mSpinnerStatus.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                Toast.makeText(getBaseContext(), "Welcome to the party!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });



    }

    public void onClickAddFriend(View view)
    {

        String toastMsg = String.format("Friend request was sent");
        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
    }

    public void onClickHome(View view)
    {
        Intent intent = new Intent(UserPageActivity.this, NewsActivity.class);
        startActivity(intent);
    }

    public void onClickFriends(View view)
    {
        Intent intent = new Intent(UserPageActivity.this, FriendsOverviewActivity.class);
        startActivity(intent);
    }

    public void onClickChat(View view)
    {
        Intent intent = new Intent(UserPageActivity.this, FriendsOverviewActivity.class);
        startActivity(intent);
    }

    public void onClickMap(View view)
    {
        Intent intent = new Intent(UserPageActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    public void onClickAppointments(View view)
    {
        Intent intent = new Intent(UserPageActivity.this, Appointment.class);
        startActivity(intent);
    }

    public void onClickPostPic(View view)
    {

        String toastMsg = String.format("Your picture was uploaded");
        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
    }

    public void onClickUploadAvatar(View view)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

    }

    public void onClickSendPost(View view)
    {
        mPostText=(EditText)findViewById(R.id.whats_on_my_mind);

        m = new HashMap<String, Object>();
        m.put(ATTRIBUTE_NAME_TEXT, mPostText.getText());
        if(selectedImageUri!=null)
        {

            m.put(ATTRIBUTE_NAME_IMAGE, R.drawable.winehouse);

        }
        else
            m.put(ATTRIBUTE_NAME_IMAGE, R.drawable.winehouse);


        dataList.add(m);
        sAdapter.notifyDataSetChanged();
        mPostText.setText("");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri

                    Log.i(TAG, "Image Path : " + selectedImageUri);
                    // Set the image in ImageView
                    mAvatar.setImageURI(selectedImageUri);


                }
                else
                {
                    String toastMsg = String.format("Sorry, a failure");
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                }

            }
        }
    }



}