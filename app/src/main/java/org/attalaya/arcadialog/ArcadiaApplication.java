package org.attalaya.arcadialog;

import android.app.Application;

import com.facebook.stetho.Stetho;

import io.github.gjyaiya.stetho.realm.RealmInspectorModulesProvider;

/**
 * Created by Marcel on 29/08/2016.
 */
public class ArcadiaApplication extends Application {

    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
    }
}
