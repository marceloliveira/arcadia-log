package org.attalaya.arcadialog.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Marcel on 28/11/2015.
 */
public class CampaignPlayer extends RealmObject {

    @PrimaryKey
    private long id;

    private int guild;
    private Player player;
    private RealmList<PlayerHero> heroes;
    private boolean savedCoin;

    public int getGuild() {
        return guild;
    }

    public void setGuild(int guild) {
        this.guild = guild;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public RealmList<PlayerHero> getHeroes() {
        return heroes;
    }

    public void setHeroes(RealmList<PlayerHero> heroes) {
        this.heroes = heroes;
    }

    public boolean isSavedCoin() {
        return savedCoin;
    }

    public void setSavedCoin(boolean savedCoin) {
        this.savedCoin = savedCoin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
