package org.attalaya.arcadialog.model;

import io.realm.RealmObject;

/**
 * Created by Marcel on 12/12/2015.
 */
public class Title extends RealmObject {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
