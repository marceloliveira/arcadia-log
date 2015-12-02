package org.attalaya.arcadialog.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Marcel on 01/12/2015.
 */
public class Scenario extends RealmObject {

    private int scenario;
    private CampaignPlayer winner;
    private RealmList<CampaignPlayer> leastDeaths;
    private RealmList<CampaignPlayer> mostCoins;
    private RealmList<CampaignPlayer> reward;
    private CampaignPlayer title;

    public int getScenario() {
        return scenario;
    }

    public void setScenario(int scenario) {
        this.scenario = scenario;
    }

    public CampaignPlayer getWinner() {
        return winner;
    }

    public void setWinner(CampaignPlayer winner) {
        this.winner = winner;
    }

    public RealmList<CampaignPlayer> getLeastDeaths() {
        return leastDeaths;
    }

    public void setLeastDeaths(RealmList<CampaignPlayer> leastDeaths) {
        this.leastDeaths = leastDeaths;
    }

    public RealmList<CampaignPlayer> getMostCoins() {
        return mostCoins;
    }

    public void setMostCoins(RealmList<CampaignPlayer> mostCoins) {
        this.mostCoins = mostCoins;
    }

    public RealmList<CampaignPlayer> getReward() {
        return reward;
    }

    public void setReward(RealmList<CampaignPlayer> reward) {
        this.reward = reward;
    }

    public CampaignPlayer getTitle() {
        return title;
    }

    public void setTitle(CampaignPlayer title) {
        this.title = title;
    }
}
