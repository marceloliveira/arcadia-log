package org.attalaya.arcadialog.controller;

import android.content.Context;

import org.attalaya.arcadialog.R;
import org.attalaya.arcadialog.model.Campaign;
import org.attalaya.arcadialog.model.CampaignPlayer;
import org.attalaya.arcadialog.model.CampaignScenario;
import org.attalaya.arcadialog.model.CampaignType;
import org.attalaya.arcadialog.model.Hero;
import org.attalaya.arcadialog.model.Player;
import org.attalaya.arcadialog.model.Scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ArcadiaController {

    private static ArcadiaController instance;
    private Realm realm;
    private Context context;

    public ArcadiaController(Context context) {
        this.context = context;
        this.realm = Realm.getInstance(this.context);
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
        CampaignType ct = realm.allObjects(CampaignType.class).first();
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
        RealmResults<Player> result = realm.allObjects(Player.class);
        for (Player p: result) {
            playerNames.add(p.getName());
        }
        return playerNames;
    }

    public Campaign getCampaign(int campaignId) {
        return realm.allObjects(Campaign.class).where().equalTo("campaignId",campaignId).findFirst();
    }

    public RealmResults<Campaign> getCampaigns() {
        return realm.allObjects(Campaign.class);
    }

    public void createCampaign(List<CampaignPlayer> players, CampaignType campaignType) {
        realm.beginTransaction();
        RealmList<CampaignPlayer> campaignPlayers = new RealmList<>();
        for (CampaignPlayer p: players) {
            RealmQuery<Player> player = realm.allObjects(Player.class).where().equalTo("name", p.getPlayer().getName());
            if (player.count() > 0) {
                p.setPlayer(player.findFirst());
            }
            campaignPlayers.add(p);
        }
        Campaign campaign = new Campaign();
        campaign.setCampaignId(realm.allObjects(Campaign.class).size());
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
        return realm.allObjects(CampaignType.class);
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
        RealmQuery<Scenario> scenarios = realm.allObjects(Scenario.class).where().equalTo("position",position).equalTo("campaignType.name", ct.getName());
        for (CampaignScenario cs: campaign.getScenarios()) {
            scenarios = scenarios.notEqualTo("name",cs.getScenario().getName());
        }
        return scenarios.findAll();
    }

    public RealmResults<CampaignScenario> getCampaignScenarios(Campaign campaign) {
        return campaign.getScenarios().where().findAllSorted("order");
    }

    public RealmResults<Hero> getHeroes() {
        return realm.allObjects(Hero.class);
    }
}
