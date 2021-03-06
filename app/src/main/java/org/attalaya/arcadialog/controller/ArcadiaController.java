package org.attalaya.arcadialog.controller;

import android.content.Context;

import org.attalaya.arcadialog.R;
import org.attalaya.arcadialog.model.Campaign;
import org.attalaya.arcadialog.model.CampaignPlayer;
import org.attalaya.arcadialog.model.CampaignScenario;
import org.attalaya.arcadialog.model.CampaignType;
import org.attalaya.arcadialog.model.Hero;
import org.attalaya.arcadialog.model.Item;
import org.attalaya.arcadialog.model.Player;
import org.attalaya.arcadialog.model.PlayerHero;
import org.attalaya.arcadialog.model.Scenario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ArcadiaController {

    private static ArcadiaController instance;
    private Realm realm;
    private Context context;

    public ArcadiaController(Context context) {
        this.context = context;
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this.context).build();
        this.realm = Realm.getInstance(realmConfiguration);
        if (this.realm.isEmpty()) {
            populateRealm();
        }
    }

    private void populateRealm() {
        realm.beginTransaction();
        for (String name: context.getResources().getStringArray(R.array.campaign_array)) {
            CampaignType ct = new CampaignType();
            ct.setName(name);
            realm.copyToRealm(ct);
        }
        for (String name: context.getResources().getStringArray(R.array.hero_array)) {
            Hero h = new Hero();
            h.setName(name);
            realm.copyToRealm(h);
        }
        CampaignType ct = realm.where(CampaignType.class).findFirst();
        for (String name: context.getResources().getStringArray(R.array.base_outer_scenario_array)) {
            Scenario s = new Scenario();
            s.setName(name);
            s.setCampaignType(ct);
            s.setPosition(0);
            realm.copyToRealm(s);
        }
        for (String name: context.getResources().getStringArray(R.array.base_inner_scenario_array)) {
            Scenario s = new Scenario();
            s.setName(name);
            s.setCampaignType(ct);
            s.setPosition(1);
            realm.copyToRealm(s);
        }
        for (String name: context.getResources().getStringArray(R.array.base_final_scenario_array)) {
            Scenario s = new Scenario();
            s.setName(name);
            s.setCampaignType(ct);
            s.setPosition(2);
            realm.copyToRealm(s);
        }
        try {
            realm.createAllFromJson(Item.class, context.getResources().openRawResource(R.raw.items));
        } catch (IOException e) {
            e.printStackTrace();
        }
        realm.commitTransaction();
    }

    public static ArcadiaController getInstance(Context context) {
        if (instance==null) {
            instance = new ArcadiaController(context);
        }
        return instance;
    }

    public List<String> getPlayerNames() {
        ArrayList<String> playerNames = new ArrayList<>();
        RealmResults<Player> result = realm.where(Player.class).findAll();
        for (Player p: result) {
            playerNames.add(p.getName());
        }
        return playerNames;
    }

    public Campaign getCampaign(long campaignId) {
        return realm.where(Campaign.class).equalTo("id",campaignId).findFirst();
    }

    public RealmResults<Campaign> getCampaigns() {
        return realm.where(Campaign.class).findAll();
    }

    public void createCampaign(List<CampaignPlayer> players, CampaignType campaignType) {
        realm.beginTransaction();
        long playerId = realm.where(Player.class).count();
        long playerHeroId = realm.where(PlayerHero.class).count();
        long campaignPlayerId = realm.where(CampaignPlayer.class).count();
        RealmList<CampaignPlayer> campaignPlayers = new RealmList<>();
        for (CampaignPlayer p: players) {
            RealmQuery<Player> player = realm.where(Player.class).equalTo("name", p.getPlayer().getName());
            if (player.count() > 0) {
                p.setPlayer(player.findFirst());
            } else {
                p.getPlayer().setId(playerId++);
            }
            for (PlayerHero h: p.getHeroes()) {
                h.setId(playerHeroId++);
            }
            p.setId(campaignPlayerId++);
            campaignPlayers.add(p);
        }
        Campaign campaign = new Campaign();
        campaign.setId(realm.where(Campaign.class).count());
        campaign.setCampaignType(campaignType);
        campaign.setPlayers(campaignPlayers);
        realm.copyToRealm(campaign);
        realm.commitTransaction();
    }

    public void addScenario(Campaign campaign, Scenario scenario) {
        realm.beginTransaction();
        CampaignScenario cs = new CampaignScenario();
        cs.setScenario(scenario);
        cs.setOrder(campaign.getScenarios().size() + 1);
        campaign.getScenarios().add(cs);
        realm.copyToRealmOrUpdate(campaign);
        realm.commitTransaction();
    }

    public RealmResults<CampaignType> getCampaignTypes() {
        return realm.where(CampaignType.class).findAll();
    }

    public RealmResults<Scenario> getScenarios(Campaign campaign) {
        CampaignType ct = campaign.getCampaignType();
        int q = campaign.getScenarios().size();
        int position = 0;
        if (q==5) {
            position = 2;
        } else if (q>2) {
            position = 1;
        }
        RealmQuery<Scenario> scenarios = realm.where(Scenario.class).equalTo("position",position).equalTo("campaignType.name", ct.getName());
        for (CampaignScenario cs: campaign.getScenarios()) {
            scenarios = scenarios.notEqualTo("name",cs.getScenario().getName());
        }
        return scenarios.findAll();
    }

    public RealmResults<CampaignScenario> getCampaignScenarios(Campaign campaign) {
        return campaign.getScenarios().where().findAllSorted("order");
    }

    public RealmResults<Hero> getHeroes() {
        return realm.where(Hero.class).findAll();
    }

    public void editCampaignScenario(CampaignScenario campaignScenario, CampaignPlayer winner, RealmList<CampaignPlayer> deaths, RealmList<CampaignPlayer> coins, RealmList<CampaignPlayer> reward, RealmList<CampaignPlayer> title) {
        realm.beginTransaction();
        campaignScenario.setWinner(winner);
        campaignScenario.setLeastDeaths(deaths);
        campaignScenario.setMostCoins(coins);
        campaignScenario.setReward(reward);
        campaignScenario.setTitle(title);
        realm.copyToRealmOrUpdate(campaignScenario);
        realm.commitTransaction();
    }

    public void updateCampaignPlayerSavedCoin(CampaignPlayer campaignPlayer, boolean savedCoin) {
        realm.beginTransaction();
        campaignPlayer.setSavedCoin(savedCoin);
        realm.copyToRealmOrUpdate(campaignPlayer);
        realm.commitTransaction();
    }
}
