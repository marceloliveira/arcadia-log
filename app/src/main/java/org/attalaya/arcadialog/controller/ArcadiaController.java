package org.attalaya.arcadialog.controller;

import android.content.Context;

import org.attalaya.arcadialog.model.Campaign;
import org.attalaya.arcadialog.model.CampaignPlayer;
import org.attalaya.arcadialog.model.Player;

import java.util.ArrayList;
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

    public RealmResults<Campaign> getCampaigns() {
        return realm.allObjects(Campaign.class);
    }

    public void createCampaign(List<CampaignPlayer> players, int campaignType) {
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
        campaign.setCampaign(campaignType);
        campaign.setPlayers(campaignPlayers);
        realm.copyToRealm(campaign);
        realm.commitTransaction();
    }
}
