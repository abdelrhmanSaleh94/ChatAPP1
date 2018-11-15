package com.example.abdelrahmansaleh.chatapp.Model;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class SendNotifications {
    public SendNotifications(String message,String heading ,String notificationKey){
        //notificationKey= "0bf189a3-09f1-4c1d-9d73-070cdececdc2";
        try {
            JSONObject notificationContent = new JSONObject(
                    "{'contents':{'en':'" + message + "'},"+
                            "'include_player_ids':['" + notificationKey + "']," +
                            "'headings':{'en': '" + heading + "'}}");
            OneSignal.postNotification( notificationContent,null );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
