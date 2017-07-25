package exampfi.daria.chillaxlinuxtryandroid.Models;

import java.text.DateFormat;

/**
 * Created by daria on 12/12/16.
 */

public class Message {

    public String messageText;


    public int creationTime;


    public String sender;

    public String receiver;


    public Message(){

    }

    public Message(String _messageText/*, int _creationTime, String _sender,String _receiver*/) {
        messageText = _messageText;
       // creationTime = _creationTime;
       // sender= _sender;
        //receiver=_receiver;
    }


    public String getMessageText(){
        return this.messageText;
    }

    //public int getCreationTime(){        return this.creationTime;    }


    //public String getSender(){        return this.sender;    }
    //public String getReceiver(){return this.receiver;}



    public void setMessageText(String _messageText){
        messageText=_messageText;
    }

    //public void setCreationTime(int _creationTime){        creationTime=_creationTime;    }

    /*public void setSender(String _sender){
        sender=_sender;
    }
    public void setReceiver(String _receiver){
        receiver=_receiver;
    }*/

}
