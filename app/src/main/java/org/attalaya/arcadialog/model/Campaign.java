package org.attalaya.arcadialog.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Campaign extends RealmObject{

    @PrimaryKey
    private int campaignId;

    private CampaignType campaignType;
    private RealmList<CampaignPlayer> players;
    private RealmList<CampaignScenario> scenarios;

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public CampaignType getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(CampaignType campaignType) {
        this.campaignType = campaignType;
    }

    public RealmList<CampaignPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(RealmList<CampaignPlayer> players) {
        this.players = players;
    }

    public RealmList<CampaignScenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(RealmList<CampaignScenario> scenarios) {
        this.scenarios = scenarios;
    }
}
