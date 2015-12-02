package org.attalaya.arcadialog.model;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Marcel on 28/11/2015.
 */
public class Campaign extends RealmObject{

    @PrimaryKey
    private int id;
    private int campaign;
    private RealmList<CampaignPlayer> players;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCampaign() {
        return campaign;
    }

    public void setCampaign(int campaign) {
        this.campaign = campaign;
    }

    public RealmList<CampaignPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(RealmList<CampaignPlayer> players) {
        this.players = players;
    }

}
