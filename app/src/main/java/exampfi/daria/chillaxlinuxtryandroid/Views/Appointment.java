package exampfi.daria.chillaxlinuxtryandroid.Views;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import exampfi.daria.chillaxlinuxtryandroid.Adapters.FriendAdapter;
import exampfi.daria.chillaxlinuxtryandroid.ChillaxEnum;
import exampfi.daria.chillaxlinuxtryandroid.Models.Friend;
import exampfi.daria.chillaxlinuxtryandroid.R;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import android.view.View.OnClickListener;


public class Appointment extends AppCompatActivity implements OnClickListener {

    public static final String SharedPref = "FriendsIWannaSee";
    SharedPreferences settings=null;

    final String LOG_TAG = "AppointmentSQLLogs";
    public ArrayList<Friend> myfriendsApp=new ArrayList<Friend>();
    ListView friendslist;
    FriendAdapter friendAdapterApp;

    private EditText mAppointmentName;
    private RatingBar mForceOfAlcohol;
    private EditText mAppointmentDate;
    private EditText mAppointmentTime;
    private TextView mPlaceAdress;
    private TextView mPlaceName;
    private TextView mForceOfFun;

    private AutoCompleteTextView mResponsibleMail;
    private DatePickerDialog mFromDatePickerDialog;
    private SimpleDateFormat mDateFormatter;

    private ImageButton mCreate;
    private ImageButton mPrint;
    private ImageButton mClear;

    DBHelper dbHelper;
    String AddedTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        mCreate=(ImageButton)findViewById(R.id.create_appointment);
        mCreate.setOnClickListener(this);

        mPrint=(ImageButton)findViewById(R.id.history_of_appointments);
        mPrint.setOnClickListener(this);

        mClear=(ImageButton)findViewById(R.id.clear_db);
        mClear.setOnClickListener(this);

        mAppointmentName=(EditText)findViewById(R.id.appointment_name);
        mAppointmentDate=(EditText)findViewById(R.id.appointment_date);
        mAppointmentTime=(EditText)findViewById(R.id.appointment_time);
        mForceOfAlcohol=(RatingBar)findViewById(R.id.rating_drunk);
        mForceOfFun=(TextView)findViewById(R.id.force_of_fun);
        mPlaceAdress=(TextView)findViewById(R.id.adress_for_appointment);
        mPlaceName=(TextView)findViewById(R.id.place_for_appointment);
        mResponsibleMail=(AutoCompleteTextView)findViewById(R.id.appointment_mail);

        //data for caching selected location
        Context context = this;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String desiredPreference =  sharedPreferences.getString("ADRESS", "DefaultValue");
        String desiredPreference1 = sharedPreferences.getString("PLACE_NAME", "DefaultValue");
        mPlaceAdress.setText(desiredPreference);
        mPlaceName.setText(desiredPreference1);

        //for invited friends
        settings=getSharedPreferences(SharedPref,2);

        //choosing date of appointment
        mDateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, - 1 * ChillaxEnum.MIN_LEGAL_AGE.getValue());

        mFromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar setDate = Calendar.getInstance();
                setDate.set(year, month, day);
                mAppointmentDate.setText(mDateFormatter.format(setDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        mAppointmentDate.setKeyListener(null);
        mAppointmentDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus)
                    ShowDatePickerDialog();

            }
        });
        mAppointmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDatePickerDialog();
            }
        });



        //setting the intensity of a meeting
        mForceOfAlcohol.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                if(rating<=1.0)
                {
                    mForceOfFun.setText("State of mind: Sober god");

                }
                if(rating>1.0 && rating<=2.0)
               {
                   mForceOfFun.setText("State of mind: Intelligent");

               }

                if(rating>2.0 && rating<=3.0) {
                    mForceOfFun.setText("State of mind: Man of Steel");

                }
                if(rating>3.0 && rating<=4.0) {
                    mForceOfFun.setText("State of mind: Neandertal");
                }
            }
        });

        //setting time of a meeting
        mAppointmentTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Appointment.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mAppointmentTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        //data for storing in sqlite
        dbHelper = new DBHelper(this);

    }

    public void onResume()
    {
        super.onResume();
        AddedTask=settings.getString("FriendInvited", "");
        friendslist = (ListView) findViewById(R.id.invited_friends);
        String name = AddedTask;
        Friend fr = new Friend(name, "");
        myfriendsApp.add(fr);
        friendAdapterApp = new FriendAdapter(getApplicationContext(), myfriendsApp);
        friendslist.setAdapter(friendAdapterApp);
        friendAdapterApp.notifyDataSetChanged();
    }

    public void ShowDatePickerDialog()
    {
        String appointmentDateString = mAppointmentDate.getText().toString();
        if(!appointmentDateString.isEmpty())
        {
            try
            {
                Calendar currentDate = Calendar.getInstance();
                currentDate.setTime(mDateFormatter.parse(appointmentDateString));

                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);

                mFromDatePickerDialog.updateDate(year, month, day);
            } catch(ParseException ex){}
        }
        mFromDatePickerDialog.show();
    }


    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "AppointmentDB", null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");

            db.execSQL("create table AppointmentDB ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "email text,"
                    + "date text,"
                    + "time text,"
                    + "adress text"

                    + ");");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public void onClick(View view){
        ContentValues cv = new ContentValues();


        String nameAp=mAppointmentName.getText().toString();
        String emailResp=mResponsibleMail.getText().toString();
        String date=mAppointmentDate.getText().toString();
        String time=mAppointmentTime.getText().toString();
        String place=mPlaceAdress.getText().toString()+mPlaceName.getText().toString();
        String fun=mForceOfFun.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (view.getId()){
            case R.id.create_appointment:
                Log.d(LOG_TAG, "--- Insert in AppointmentDB: ---");
                cv.put("name", nameAp);
                cv.put("email", emailResp);
                cv.put("date",date);
                cv.put("time",time);
                cv.put("adress",place);
                long rowID = db.insert("AppointmentDB", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                break;
            case R.id.history_of_appointments:
               String result="";
                Cursor c1 = db.query("AppointmentDB", null,null,null,null, null, null,null);
                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (c1.moveToFirst()) {
                    do{
                        result+=c1.getString(1)+"   "+c1.getString(2)+"\n"+c1.getString(3)+"   "+c1.getString(4)+"\n"+c1.getString(6)+"\n\n";

                    }while(c1.moveToNext());
                }
                c1.close();
                Intent intent1=new Intent(getApplicationContext(), HistoryOfAppointmentsActivity.class);
                intent1.putExtra("stored", result);
                startActivity(intent1);
                break;
            case R.id.clear_db:
                Log.d(LOG_TAG, "--- Clear AppointmentDB: ---");
                int clearCount = db.delete("AppointmentDB", null, null);
                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                break;
        }
    }


    public void onClickFindFriends(View view){
        Intent intent = new Intent(Appointment.this, FriendsOverviewActivity.class);
        startActivityForResult(intent,1);
        String toastMsg = String.format("Long click to add a friend");
        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
    }
}
