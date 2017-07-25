package exampfi.daria.chillaxlinuxtryandroid;


import android.content.Context;
import android.content.res.AssetManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;

import cz.msebera.android.httpclient.conn.ssl.X509HostnameVerifier;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by Ira on 12/21/2016.
 */

public class ChillaxRestClient {

    public static final String BASE_URL = "https://192.168.1.2:44334/";
    public static final String REGISTER_URL = "api/Admin/";
    public static final String LOGIN_URL = "api/jwt";

    private AsyncHttpClient restClient = new AsyncHttpClient();
    private Context context;

    public ChillaxRestClient(Context context) {

        this.context = context;

        try {
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);

            HostnameVerifier hvf = MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            MySSLSocketFactory socketFactory = new MySSLSocketFactory(keyStore);
            socketFactory.setHostnameVerifier((X509HostnameVerifier) hvf);

            restClient.setSSLSocketFactory(socketFactory);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerRequest(JSONObject userParams, TextHttpResponseHandler responseHandler) {

        StringEntity stringEntity = new StringEntity(userParams.toString(), "UTF-8");
        stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        restClient.post(context, BASE_URL + REGISTER_URL, stringEntity, "application/json", responseHandler);
    }

    public void loginRequest(RequestParams params, JsonHttpResponseHandler responseHandler) {

        restClient.post(context, BASE_URL + LOGIN_URL, params, responseHandler);
    }

}
