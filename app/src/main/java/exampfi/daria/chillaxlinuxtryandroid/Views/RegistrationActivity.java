/**
 * Created by Ira on 11/8/2016.
 */

package exampfi.daria.chillaxlinuxtryandroid.Views;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import exampfi.daria.chillaxlinuxtryandroid.ChillaxEnum;
import exampfi.daria.chillaxlinuxtryandroid.ChillaxRestClient;
import exampfi.daria.chillaxlinuxtryandroid.Models.User;
import exampfi.daria.chillaxlinuxtryandroid.MultiSharedPreferences;
import exampfi.daria.chillaxlinuxtryandroid.R;

public class RegistrationActivity extends Activity {

    private AutoCompleteTextView mFirstNameView;
    private AutoCompleteTextView mLastNameView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mBirthdayView;
    String firstname, lastname, login;
    private DatePickerDialog mFromDatePickerDialog;
    private SimpleDateFormat mDateFormatter;


    String mHttpResponseString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration_web);


        mFirstNameView = (AutoCompleteTextView) findViewById(R.id.viewFirstName);
        mLastNameView = (AutoCompleteTextView) findViewById(R.id.viewLastName);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.viewEmail);
        mPasswordView = (EditText) findViewById(R.id.viewPassword);
        mBirthdayView = (EditText) findViewById(R.id.viewBirthday);


        // Set date picker dialog

        mDateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, - 1 * ChillaxEnum.MIN_LEGAL_AGE.getValue());

        mFromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar setDate = Calendar.getInstance();
                setDate.set(year, month, day);
                mBirthdayView.setText(mDateFormatter.format(setDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


        mBirthdayView.setKeyListener(null);
        mBirthdayView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus)
                    ShowDatePickerDialog();

            }
        });
        mBirthdayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDatePickerDialog();
            }
        });


        //Set autocomplete fields (first name, last name, email)
        setAutoCompleteViewFromCash(mEmailView, getString(R.string.preferences_file_key), getString(R.string.USER_EMAIL_KEY));
        setAutoCompleteViewFromCash(mFirstNameView, getString(R.string.preferences_file_key), getString(R.string.FIRST_NAME_KEY));
        setAutoCompleteViewFromCash(mLastNameView, getString(R.string.preferences_file_key), getString(R.string.LAST_NAME_KEY));





    }

    public void ShowDatePickerDialog()
    {
        String birthdayString = mBirthdayView.getText().toString();
        if(!birthdayString.isEmpty())
        {
            try
            {
                Calendar currentDate = Calendar.getInstance();
                currentDate.setTime(mDateFormatter.parse(birthdayString));

                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);

                mFromDatePickerDialog.updateDate(year, month, day);
            } catch(ParseException ex){}
        }
        mFromDatePickerDialog.show();
    }

    public boolean attemptRegister()
    {
        boolean dataIsValid = false;

        if(mFirstNameView.getText().toString().isEmpty())
        {
            mFirstNameView.requestFocus();
            mFirstNameView.setError(getString(R.string.first_name_required));
        }

        else if(mLastNameView.getText().toString().isEmpty())
        {
            mLastNameView.requestFocus();
            mLastNameView.setError(getString(R.string.last_name_required));
        }

        else if(!EmailIsValid(mEmailView.getText()))
        {
            mEmailView.requestFocus();
            mEmailView.setError(getString(R.string.invalid_email));
        }

        else if(mPasswordView.getText().toString().isEmpty())
        {
            mPasswordView.requestFocus();
            mPasswordView.setError(getString(R.string.invalid_password));
        }

        else if(mBirthdayView.getText().toString().isEmpty())
        {
            mBirthdayView.requestFocus();
            mBirthdayView.setError(getString(R.string.birthday_required));
        }

        else
        {
            Date birthday = new Date();
            Date today = new Date();
            try
            {
                birthday =  mDateFormatter.parse(mBirthdayView.getText().toString());

            } catch(ParseException ex){}


            if (getDiffYears(birthday, today) < ChillaxEnum.MIN_LEGAL_AGE.getValue())
            {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.illegal_age_notification, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            else{dataIsValid = true;}
        }



        return dataIsValid;
    }

    public void onRegisterClick(View view)
    {
        cashRegistrationData();
        firstname=mFirstNameView.getText().toString();
        lastname=mLastNameView.getText().toString();
        login=firstname+" "+lastname;
        SavePreferences("LOGIN",login);
        if(attemptRegister())
        {
            registerUser();
        }
        else
        {
            //Reset autocomplete fields (first name, last name, email)
            setAutoCompleteViewFromCash(mEmailView, getString(R.string.preferences_file_key), getString(R.string.USER_EMAIL_KEY));
            setAutoCompleteViewFromCash(mFirstNameView, getString(R.string.preferences_file_key), getString(R.string.FIRST_NAME_KEY));
            setAutoCompleteViewFromCash(mLastNameView, getString(R.string.preferences_file_key), getString(R.string.LAST_NAME_KEY));
        }
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar firstDate = getCalendar(first);
        Calendar lastDate = getCalendar(last);
        int diff = lastDate.get(Calendar.YEAR) - firstDate.get(Calendar.YEAR);
        if (firstDate.get(Calendar.MONTH) > lastDate.get(Calendar.MONTH) ||
                (firstDate.get(Calendar.MONTH) == lastDate.get(Calendar.MONTH) && firstDate.get(Calendar.DATE) > lastDate.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTime(date);
        return calendar;
    }

    public boolean EmailIsValid(CharSequence target)
    {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void cashRegistrationData()
    {
        cashViewMultiValue(mEmailView, getString(R.string.preferences_file_key), getString(R.string.USER_EMAIL_KEY));
        cashViewMultiValue(mFirstNameView, getString(R.string.preferences_file_key), getString(R.string.FIRST_NAME_KEY));
        cashViewMultiValue(mLastNameView, getString(R.string.preferences_file_key), getString(R.string.LAST_NAME_KEY));

    }

    public void setAutoCompleteViewFromCash(AutoCompleteTextView view, String fileKey, String key)
    {
        MultiSharedPreferences settings = new MultiSharedPreferences(this, fileKey, key);

        List<String> cashedValueList = settings.getValueList();

        if(!(cashedValueList == null))
        {
            String[] cashedValueArr = new String[cashedValueList.size()];
            cashedValueArr = cashedValueList.toArray(cashedValueArr);
            ArrayAdapter<String> mailAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, cashedValueArr);
            view.setAdapter(mailAdapter);
        }
    }

    public void cashViewMultiValue(EditText view, String fileKey, String key)
    {
        String value = view.getText().toString();

        if(!value.isEmpty())
        {
            MultiSharedPreferences settings = new MultiSharedPreferences(this, fileKey, key);
            settings.addValue(value);
        }
    }

    private void SavePreferences(String key, String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public void registerUser() {

        //Read user parameters
        String firstName, lastName, birthDateString, email, passwordHash;

        firstName = mFirstNameView.getText().toString();
        lastName = mLastNameView.getText().toString();
        birthDateString = mBirthdayView.getText().toString();
        email = mEmailView.getText().toString();
        passwordHash = mPasswordView.getText().toString();

        Date birthDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            birthDate = dateFormat.parse(birthDateString);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        birthDateString = dateFormat.format(birthDate);

        //Make HTTP request
        JSONObject userParams = new JSONObject();

        try {
            userParams.put("firstName", firstName);
            userParams.put("lastName", lastName);
            userParams.put("birthDate", birthDateString);
            userParams.put("email", email);
            userParams.put("passwordHash", passwordHash);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        ChillaxRestClient restClient = new ChillaxRestClient(this);

        restClient.registerRequest(userParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                mHttpResponseString = responseString;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                mHttpResponseString = responseString;

            }

            @Override
            public void onFinish() {

                super.onFinish();
                onRegisterRequestResult();
            }

        });
    }

    public void onRegisterRequestResult()
    {
        if (mHttpResponseString.equals("true"))
        {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        if (mHttpResponseString.equals("false"))
        {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.used_email, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
