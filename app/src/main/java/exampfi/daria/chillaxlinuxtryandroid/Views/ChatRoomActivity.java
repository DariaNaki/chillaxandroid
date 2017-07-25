package exampfi.daria.chillaxlinuxtryandroid.Views;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import exampfi.daria.chillaxlinuxtryandroid.R;

public class ChatRoomActivity extends AppCompatActivity {

    ArrayAdapter<String> chatAdapter;
    ArrayList<String> chatMessages= new ArrayList<String>();
    final String LOG_TAG = "MessagesSQLLogs";
    private Button sendMessage;
    ListView chat;
    private EditText mToBeMessage;
    private String chatterName;

    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        chatterName = getIntent().getExtras().get("myChatter").toString();
        setTitle(chatterName);
        chat=(ListView)findViewById(R.id.present_chat);
        chatAdapter = new ArrayAdapter<String>(this,R.layout.item_of_chat, chatMessages);
        chat.setAdapter(chatAdapter);

        sendMessage = (Button) findViewById(R.id.send_message);
        mToBeMessage = (EditText) findViewById(R.id.toBeSent);

        dbHelper = new DBHelper(this);
        Context context=this;
        final   SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query("MessagesDB", null,null,null,null, null, null,null);
        // final String Query = ;
        String query = "SELECT * FROM MessagesDB WHERE receiver=" +"'"+ chatterName+ "'";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                if (c.moveToFirst())
                {
                    do {

                        chatMessages.add(c.getString(3));
                        chatAdapter.notifyDataSetChanged();


                    } while (c.moveToNext());
                }
                cursor.moveToNext();
            }
        }


        ArrayAdapter<String> aa=new ArrayAdapter<String>(getApplicationContext(), R.layout.item_of_chat, chatMessages);
        chat.setAdapter(aa);

        mToBeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToBeMessage.setHint("");
            }

        });

        sendMessage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String messageContent = mToBeMessage.getText().toString();
                if(null!=messageContent&&messageContent.length()>0) {

                    chatMessages.add(messageContent);

                    chatAdapter.notifyDataSetChanged();
                    Calendar c = Calendar.getInstance();
                    int seconds = c.get(Calendar.SECOND);
                    ContentValues cv = new ContentValues();

                    String sender = sharedPreferences.getString("LOGIN", "DefaultValue");
                    String receiver = chatterName;
                    String message = messageContent;
                    String time = Integer.toString(seconds);
                    SQLiteDatabase db1 = dbHelper.getWritableDatabase();
                    Log.d(LOG_TAG, "--- Insert in MessagesDB: ---");
                    cv.put("sender", sender);
                    cv.put("receiver", receiver);
                    cv.put("message", message);
                    cv.put("time", time);
                    long rowID = db1.insert("MessagesDB", null, cv);
                    Log.d(LOG_TAG, "row inserted, ID = " + rowID);


                }
            }
        });



    }

    public void sendRequest(){

        String sender;
        String receiver;
        String passwordHash;
        String message;
        Context context=this;
        final   SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sender= sharedPreferences.getString("LOGIN", "DefaultValue");
        receiver=chatterName;

        //AsyncHttpClient
        String postUrl = "https://193.105.219.190:44334/api/jwt";

    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "MessagesDB", null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");

            db.execSQL("create table MessagesDB ("
                    + "id integer primary key autoincrement,"
                    + "sender text,"
                    + "receiver text,"
                    + "message text,"
                    + "time text"
                    + ");");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


}
