package exampfi.daria.chillaxlinuxtryandroid.Models;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import exampfi.daria.chillaxlinuxtryandroid.R;

/**
 * Created by daria on 12/12/16.
 */

public class User {
    public String firstName;

    public String lastName;

    public String birthDate;

    public int sex;

    public int chillaxStatus;

    public User(){

    }

    public User(String _firstName, String _lastName, String _birthdate, int _sex, int _chillaxstatus){
        firstName=_firstName;
        lastName=_lastName;
        birthDate=_birthdate;
        sex=_sex;
        chillaxStatus=_chillaxstatus;
    }
    Context context;
    public User(Context context){
        this.context=context;
    }

    public void Update(){
        TextView txtView = (TextView) ((Activity)context).findViewById(R.id.user_login);
        txtView.setText(firstName.toString()+lastName.toString());
    }

    public String getFirstName(){
        return this.firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public String getBirthDate(){
        return this.birthDate;
    }

    public int getSex(){
        return this.sex;
    }

    public int getChillaxStatus(){
        return this.chillaxStatus;
    }

    public void setFirstName(String _firstName){
        firstName=_firstName;
    }

    public void setLastName(String _lastName){
        lastName=_lastName;
    }

    public void setBirthDate(String _birthdate){
        birthDate=_birthdate;
    }

    public void setSex(int _sex){
        sex=_sex;
    }

    public void setChillaxStatus(int _chillaxstatus){
        chillaxStatus=_chillaxstatus;
    }
}
