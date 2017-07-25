package exampfi.daria.chillaxlinuxtryandroid.Models;

/**
 * Created by daria on 12/11/16.
 */

public class Friend {

    String name;
    String status;

    public Friend(){

    }

    public Friend(String _name, String _status){
        name=_name;
        status=_status;

    }

    public String getName()
    {
        return this.name;
    }

    public String getStatus()
    {
        return this.status;
    }
    /*@Override
    public final String toString(){
        return name;
    }*/

    public void setName(String _name){
        name=_name;
    }

    public void setStatus(String _status){
        status=_status;
    }
}
