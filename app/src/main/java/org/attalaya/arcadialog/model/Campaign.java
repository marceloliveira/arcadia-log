package org.attalaya.arcadialog.model;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Campaign extends RealmObject{

    private int campaign;
    private RealmList<CampaignPlayer> players;

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
