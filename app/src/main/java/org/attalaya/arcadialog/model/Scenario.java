package org.attalaya.arcadialog.model;

import io.realm.RealmObject;

/**
 * Created by Marcel on 12/12/2015.
 */
public class Scenario extends RealmObject{

    private String name;
    private CampaignType campaignType;
    private int position;
    private int difficulty;

    public CampaignType getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(CampaignType campaignType) {
        this.campaignType = campaignType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
