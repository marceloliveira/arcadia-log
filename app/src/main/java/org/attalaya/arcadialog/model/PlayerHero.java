package org.attalaya.arcadialog.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Marcel on 28/11/2015.
 */
public class PlayerHero extends RealmObject {

    private Hero hero;
    private RealmList<Item> items;
    private int curse;

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
