package org.attalaya.arcadialog.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.attalaya.arcadialog.R;
import org.attalaya.arcadialog.controller.ArcadiaController;
import org.attalaya.arcadialog.model.CampaignPlayer;
import org.attalaya.arcadialog.model.Player;
import org.attalaya.arcadialog.model.PlayerHero;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmBaseAdapter;
import io.realm.RealmList;

public class NewCampaignActivity extends AppCompatActivity implements View.OnClickListener, Dialog.OnClickListener {

    private ArcadiaController controller;
    private List<CampaignPlayer> players;
    private AlertDialog dialog;
    private CampaignPlayerAdapter playerArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_campaign);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        controller = ArcadiaController.getInstance(this);
        players = new ArrayList<>();

        ListView list = (ListView) findViewById(R.id.listView);
        list.addHeaderView(getLayoutInflater().inflate(R.layout.campaign_type_layout, null));
        list.setHeaderDividersEnabled(true);
        playerArrayAdapter = new CampaignPlayerAdapter(this, players);
        list.setAdapter(playerArrayAdapter);

        ((Spinner) findViewById(R.id.campaignTypeSpinner)).setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.campaign_array)));

        findViewById(R.id.createCampaignButton).setOnClickListener(this);
        findViewById(R.id.addPlayer).setOnClickListener(this);
        findViewById(R.id.cancelNewCampaignButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createCampaignButton : {
                controller.createCampaign(players,((Spinner)findViewById(R.id.campaignTypeSpinner)).getSelectedItemPosition());
                finish();
                break;
            }
            case R.id.addPlayer: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.addPlayer).setView(R.layout.add_player_layout).setPositiveButton("Add",this).setNegativeButton("Cancel",this);
                dialog = builder.create();
                dialog.show();
                ArrayAdapter<String> playerNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, controller.getPlayerNames());
                ((AutoCompleteTextView)dialog.findViewById(R.id.playerName)).setAdapter(playerNameAdapter);
                ArrayAdapter<String> guildTypeArray = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.guild_array));
                ((Spinner) dialog.findViewById(R.id.guildSpinner)).setAdapter(guildTypeArray);
                ArrayAdapter<String> heroArray = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.hero_array));
                ((Spinner) dialog.findViewById(R.id.heroSpinner1)).setAdapter(heroArray);
                ((Spinner) dialog.findViewById(R.id.heroSpinner2)).setAdapter(heroArray);
                ((Spinner) dialog.findViewById(R.id.heroSpinner3)).setAdapter(heroArray);
                break;
            }
            case R.id.cancelNewCampaignButton: this.finish(); break;
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case DialogInterface.BUTTON_POSITIVE:
                Player player = new Player();
                player.setName(((EditText) dialog.findViewById(R.id.playerName)).getText().toString());
                CampaignPlayer campaignPlayer = new CampaignPlayer();
                campaignPlayer.setPlayer(player);
                campaignPlayer.setGuild(((Spinner) dialog.findViewById(R.id.guildSpinner)).getSelectedItemPosition());
                RealmList<PlayerHero> heroes = new RealmList<>();
                PlayerHero hero = new PlayerHero();
                hero.setHero(((Spinner) dialog.findViewById(R.id.heroSpinner1)).getSelectedItemPosition());
                heroes.add(hero);
                hero = new PlayerHero();
                hero.setHero(((Spinner) dialog.findViewById(R.id.heroSpinner2)).getSelectedItemPosition());
                heroes.add(hero);
                hero = new PlayerHero();
                hero.setHero(((Spinner) dialog.findViewById(R.id.heroSpinner3)).getSelectedItemPosition());
                heroes.add(hero);
                campaignPlayer.setHeroes(heroes);
                players.add(campaignPlayer);
                playerArrayAdapter.notifyDataSetChanged();
                break;

        }
    }

    private class CampaignPlayerAdapter extends ArrayAdapter<CampaignPlayer> {

        public CampaignPlayerAdapter(Context context, List<CampaignPlayer> list) {
            super(context, -1, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CampaignPlayer campaignPlayer = getItem(position);
            if (convertView==null) {
                convertView = getLayoutInflater().inflate(R.layout.campaign_player_layout, parent, false);
            }
            ((TextView) convertView.findViewById(R.id.campaignPlayerName)).setText(campaignPlayer.getPlayer().getName());
            ((TextView) convertView.findViewById(R.id.campaignPlayerGuild)).setText(getResources().getStringArray(R.array.guild_array)[campaignPlayer.getGuild()]);
            ((TextView) convertView.findViewById(R.id.campaignPlayerHeroes)).setText("");
            convertView.setBackgroundColor(getResources().getIntArray(R.array.guild_colors_array)[campaignPlayer.getGuild()]);
            return convertView;
        }
    }
}
