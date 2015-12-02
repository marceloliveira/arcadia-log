package org.attalaya.arcadialog.controller;

import android.content.Context;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by Marcel on 01/12/2015.
 */
public class ArcadiaController {

    private static ArcadiaController instance;
    private Realm realm;
    private Context context;

    public ArcadiaController(Context context) {
        this.context = context;
        this.realm = Realm.getInstance(this.context);
    }

    public static ArcadiaController getInstance(Context context) {
        if (instance==null) {
            instance = new ArcadiaController(context);
        }
        return instance;
    }

    public void createCampaign(ArrayList<String> players) {

    }
}
