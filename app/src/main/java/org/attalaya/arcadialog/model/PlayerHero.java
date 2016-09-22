package org.attalaya.arcadialog.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Marcel on 28/11/2015.
 */
public class PlayerHero extends RealmObject {

    @PrimaryKey
    private long id;

    private Hero hero;
    private RealmList<Item> items;
    private int curse;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public RealmList<Item> getItems() {
        return items;
    }

    public void setItems(RealmList<Item> items) {
        this.items = items;
    }

    public int getCurse() {
        return curse;
    }

    public void setCurse(int curse) {
        this.curse = curse;
    }
}
