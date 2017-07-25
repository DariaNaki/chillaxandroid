package exampfi.daria.chillaxlinuxtryandroid;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ira on 11/13/2016.
 */

public class MultiSharedPreferences {

    Context mContext;
    String mFileKey;
    String mKey;

    public MultiSharedPreferences(Context context, String fileKey, String key)
    {
        super();
        mContext = context;
        mFileKey = fileKey;
        mKey = key;
    }

    public void saveValueList(List<String> valueList)
    {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = mContext.getSharedPreferences(mFileKey, Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonValeList = gson.toJson(valueList);

        editor.putString(mKey, jsonValeList);
        editor.commit();
    }
    
    public ArrayList<String> getValueList()
    {
        SharedPreferences settings;
        List<String> valueList;

        settings = mContext.getSharedPreferences(mFileKey, Context.MODE_PRIVATE);

        if(settings.contains(mKey))
        {
            String jsonValeList = settings.getString(mKey, null);

            Gson gson = new Gson();
            String [] valueArray = gson.fromJson(jsonValeList, String[].class);
            //valueList = Arrays.asList(valueArray);
            valueList = new ArrayList<String>(Arrays.asList(valueArray));
        }
        else
            return  null;

        return (ArrayList<String>) valueList;
    }

    public void addValue (String value)
    {
        List<String> valueList = getValueList();
        if(valueList == null)
            valueList = new ArrayList<String>();

        if(valueList.contains(value))
            return;

        if(valueList.size() == ChillaxEnum.MAX_USER_CASHED_COUNT.getValue())
            valueList.remove(0);

        valueList.add(value);

        saveValueList(valueList);

    }
}
