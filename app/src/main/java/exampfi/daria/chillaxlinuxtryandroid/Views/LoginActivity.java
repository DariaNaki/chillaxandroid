package exampfi.daria.chillaxlinuxtryandroid.Views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import exampfi.daria.chillaxlinuxtryandroid.App.AppController;
import exampfi.daria.chillaxlinuxtryandroid.ChillaxRestClient;
import exampfi.daria.chillaxlinuxtryandroid.MultiSharedPreferences;
import exampfi.daria.chillaxlinuxtryandroid.R;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    private Button SignInButton;
    private TextView mRegisterView;

    private int mHttpResultStatusCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_log_web);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        SignInButton = (Button) findViewById(R.id.sign_in_button);
        mRegisterView = (TextView) findViewById(R.id.register);

        setAutoCompleteViewFromCash(mEmailView, getString(R.string.preferences_file_key), getString(R.string.USER_EMAIL_KEY));
        Intent intent=new Intent(LoginActivity.this,ChatRoomActivity.class);
        intent.putExtra("sender",mEmailView.getText().toString());

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        SignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                cashLoginData();

                if (attemptLogin()) {

                    loginUser();
                }
                else {
                    setAutoCompleteViewFromCash(mEmailView, getString(R.string.preferences_file_key), getString(R.string.USER_EMAIL_KEY));

                }

            }

        });

        mEmailView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = (String)parent.getItemAtPosition(position);

                setViewTextFromCash(mPasswordView, getString(R.string.preferences_file_key), selection);
            }
        });


    }

    public void onRegisterClick(View view)
    {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);

    }


    boolean attemptLogin() {
        boolean isSuccessful = false;

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {

            focusView.requestFocus();

        } else {

            isSuccessful = true;
        }

        return isSuccessful;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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

    public void cashViewValue(EditText view, String fileKey, String key)
    {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = this.getSharedPreferences(fileKey, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(key, view.getText().toString());
        editor.commit();
    }

    public void setViewTextFromCash(EditText view, String fileKey, String key)
    {
        SharedPreferences settings;
        settings = this.getSharedPreferences(fileKey, Context.MODE_PRIVATE);

        if(settings.contains(key))
        {
            view.setText(settings.getString(key, null));
        }
    }

    public void cashLoginData()
    {
        cashViewMultiValue(mEmailView, getString(R.string.preferences_file_key), getString(R.string.USER_EMAIL_KEY));

    }

    public void loginUser()
    {
        String email = mEmailView.getText().toString();
        String passwordHash = mPasswordView.getText().toString();

        RequestParams loginParams = new RequestParams();
        loginParams.put("Email", email);
        loginParams.put("PasswordHash", passwordHash);

        ChillaxRestClient restClient = new ChillaxRestClient(this);

        restClient.loginRequest(loginParams, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mHttpResultStatusCode = statusCode;
                System.out.println("on failure 1");
                onLoginRequestResult();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("login_success");
                System.out.println(response.toString());
                mHttpResultStatusCode = statusCode;
                System.out.println(statusCode);
                try {
                    AppController.TOKEN = response.getString("access_token");
                    System.out.println(AppController.TOKEN);
                } catch (Exception e) {System.out.println("EXCEPTION " + e.getMessage());}
                onLoginRequestResult();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                System.out.println("login_failure");
                System.out.println(errorResponse.toString());
                mHttpResultStatusCode = statusCode;
                System.out.println(statusCode);
                onLoginRequestResult();
            }
        });

    }

    public void onLoginRequestResult()
    {
        if (mHttpResultStatusCode == 200)
        {
            Intent intent = new Intent(LoginActivity.this, NewsActivity.class);
            startActivity(intent);

            finish();
        } else
        {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.invalid_credentials, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

}

