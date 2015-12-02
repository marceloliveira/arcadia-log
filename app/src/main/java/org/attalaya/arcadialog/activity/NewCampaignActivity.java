package org.attalaya.arcadialog.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.attalaya.arcadialog.R;
import org.attalaya.arcadialog.controller.ArcadiaController;

public class NewCampaignActivity extends AppCompatActivity implements View.OnClickListener {

    private ArcadiaController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_campaign);
        controller = ArcadiaController.getInstance(this);

        ((Spinner) findViewById(R.id.campaignTypeSpinner)).setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.campaign_array)));

        ArrayAdapter<String> guildTypeArray = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.guild_array));
        ((Spinner)findViewById(R.id.guildSpinner1)).setAdapter(guildTypeArray);
        ((Spinner)findViewById(R.id.guildSpinner2)).setAdapter(guildTypeArray);
        ((Spinner)findViewById(R.id.guildSpinner3)).setAdapter(guildTypeArray);
        ((Spinner)findViewById(R.id.guildSpinner4)).setAdapter(guildTypeArray);

        findViewById(R.id.createCampaignButton).setOnClickListener(this);
        findViewById(R.id.cancelNewCampaignButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createCampaignButton : {
                break;
            }
            case R.id.cancelNewCampaignButton: this.finish(); break;
        }
    }
}
