package org.attalaya.arcadialog.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Marcel on 01/12/2015.
 */
public class CampaignScenario extends RealmObject {

    @PrimaryKey
    private long id;

    private int order;
    private Scenario scenario;
    private CampaignPlayer winner;
    private RealmList<CampaignPlayer> leastDeaths;
    private RealmList<CampaignPlayer> mostCoins;
    private RealmList<CampaignPlayer> reward;
    private RealmList<CampaignPlayer> title;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
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

    public RealmList<CampaignPlayer> getTitle() {
        return title;
    }

    public void setTitle(RealmList<CampaignPlayer> title) {
        this.title = title;
    }
}
