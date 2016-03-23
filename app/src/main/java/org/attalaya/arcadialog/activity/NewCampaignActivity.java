package org.attalaya.arcadialog.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import org.attalaya.arcadialog.model.CampaignType;
import org.attalaya.arcadialog.model.Hero;
import org.attalaya.arcadialog.model.Player;
import org.attalaya.arcadialog.model.PlayerHero;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.Inflater;

import io.realm.RealmBaseAdapter;
import io.realm.RealmList;

public class NewCampaignActivity extends AppCompatActivity implements View.OnClickListener, Dialog.OnClickListener, AdapterView.OnItemSelectedListener, View.OnTouchListener {

    private ArcadiaController controller;
    private List<CampaignPlayer> players;
    private AlertDialog dialog;
    private CampaignPlayerAdapter playerArrayAdapter;
    private ArrayAdapter<Hero> heroArrayAdapter;
    private HashSet<Hero> unavailableHeroes;
    private HashSet<String> unavailableGuilds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_campaign);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        controller = ArcadiaController.getInstance(this);
        players = new ArrayList<>();
        unavailableHeroes = new HashSet<>();
        unavailableGuilds = new HashSet<>();

        heroArrayAdapter = new ArrayAdapter<Hero>(this,android.R.layout.simple_spinner_dropdown_item) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;

                // If this is the initial dummy entry, make it hidden
                if ((position == 0)||unavailableHeroes.contains(getItem(position))) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                }
                else {
                    // Pass convertView as null to prevent reuse of special case views
                    //v = super.getDropDownView(position, null, parent);
                    v = getLayoutInflater().inflate(android.R.layout.simple_spinner_dropdown_item,null);
                    ((TextView)v).setText(getItem(position).getName());
                }

                // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };

        Hero dummyHero = new Hero();
        dummyHero.setName(getResources().getString(R.string.select_hero));
        heroArrayAdapter.add(dummyHero);
        heroArrayAdapter.addAll(controller.getHeroes());
        heroArrayAdapter.sort(new Comparator<Hero>() {
            @Override
            public int compare(Hero o, Hero t1) {
                if (o.getName().equals(getResources().getString(R.string.select_hero))) return -1;
                if (t1.getName().equals(getResources().getString(R.string.select_hero))) return 1;
                return o.getName().compareTo(t1.getName());
            }
        });

        ListView list = (ListView) findViewById(R.id.listView);
        list.addHeaderView(getLayoutInflater().inflate(R.layout.campaign_type_layout, null));
        list.setHeaderDividersEnabled(true);
        playerArrayAdapter = new CampaignPlayerAdapter(this, players);
        list.setAdapter(playerArrayAdapter);

        ((Spinner) findViewById(R.id.campaignTypeSpinner)).setAdapter(new RealmBaseAdapter<CampaignType>(this, controller.getCampaignTypes(), true) {

            @Override
            public View getDropDownView(int i, View view, ViewGroup viewGroup) {
                if (view==null) {
                    view = View.inflate(context, android.R.layout.simple_spinner_dropdown_item, null);
                }
                ((TextView)view).setText(realmResults.get(i).getName());
                view.setPadding(16,16,16,16);
                return view;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view==null) {
                    view = View.inflate(context, android.R.layout.simple_spinner_dropdown_item, null);
                }
                ((TextView)view).setText(realmResults.get(i).getName());
                view.setPadding(16, 16, 16, 16);
                return view;
            }
        });

        findViewById(R.id.createCampaignButton).setOnClickListener(this);
        findViewById(R.id.addPlayer).setOnClickListener(this);
        findViewById(R.id.cancelNewCampaignButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if ((dialog!=null) && (view == dialog.getButton(AlertDialog.BUTTON_POSITIVE))) {
            EditText playerName = (EditText) dialog.findViewById(R.id.playerName);
            Spinner hero1 = (Spinner) dialog.findViewById(R.id.heroSpinner1);
            Spinner hero2 = (Spinner) dialog.findViewById(R.id.heroSpinner2);
            Spinner hero3 = (Spinner) dialog.findViewById(R.id.heroSpinner3);
            Spinner guild = (Spinner) dialog.findViewById(R.id.guildSpinner);

            if (playerName.getText().toString().equals("")) {
                playerName.setError("Player name can't be empty");
                return;
            }
            if (guild.getSelectedItemPosition()==0) {
                Toast toast = Toast.makeText(this,"Select guild",Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            if ((hero1.getSelectedItemPosition()==0)||(hero2.getSelectedItemPosition()==0)||(hero3.getSelectedItemPosition()==0)) {
                Toast toast = Toast.makeText(this,"Select all 3 heroes",Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            Player player = new Player();
            player.setName(playerName.getText().toString());
            CampaignPlayer campaignPlayer = new CampaignPlayer();
            campaignPlayer.setPlayer(player);
            campaignPlayer.setGuild(((Spinner) dialog.findViewById(R.id.guildSpinner)).getSelectedItemPosition() - 1);
            RealmList<PlayerHero> heroes = new RealmList<>();
            PlayerHero hero = new PlayerHero();
            hero.setHero((Hero) hero1.getSelectedItem());
            heroes.add(hero);
            hero = new PlayerHero();
            hero.setHero((Hero) hero2.getSelectedItem());
            heroes.add(hero);
            hero = new PlayerHero();
            hero.setHero((Hero) hero3.getSelectedItem());
            heroes.add(hero);
            campaignPlayer.setHeroes(heroes);
            players.add(campaignPlayer);
            playerArrayAdapter.notifyDataSetChanged();
            dialog.dismiss();
            return;
        }
        switch (view.getId()) {
            case R.id.createCampaignButton : {
                controller.createCampaign(players,(CampaignType)((Spinner) findViewById(R.id.campaignTypeSpinner)).getSelectedItem());
                finish();
                break;
            }
            case R.id.addPlayer: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.addPlayer).setView(R.layout.add_player_layout).setPositiveButton("Add",null).setNegativeButton("Cancel",this);
                dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);
                ArrayAdapter<String> playerNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, controller.getPlayerNames());
                ((AutoCompleteTextView)dialog.findViewById(R.id.playerName)).setAdapter(playerNameAdapter);
                ArrayAdapter<String> guildTypeArray = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item){
                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View v = null;

                        // If this is the initial dummy entry, make it hidden
                        if ((position == 0)||unavailableGuilds.contains(getItem(position))) {
                            TextView tv = new TextView(getContext());
                            tv.setHeight(0);
                            tv.setVisibility(View.GONE);
                            v = tv;
                        }
                        else {
                            // Pass convertView as null to prevent reuse of special case views
                            v = super.getDropDownView(position, null, parent);
                        }

                        // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                        parent.setVerticalScrollBarEnabled(false);
                        return v;
                    }
                };
                guildTypeArray.add(getString(R.string.select_guild));
                guildTypeArray.addAll(getResources().getStringArray(R.array.guild_array));
                ((Spinner) dialog.findViewById(R.id.guildSpinner)).setAdapter(guildTypeArray);

                Spinner hero1 = (Spinner) dialog.findViewById(R.id.heroSpinner1);
                hero1.setAdapter(heroArrayAdapter);
                hero1.setOnItemSelectedListener(this);
                hero1.setOnTouchListener(this);

                Spinner hero2 = (Spinner) dialog.findViewById(R.id.heroSpinner2);
                hero2.setAdapter(heroArrayAdapter);
                hero2.setOnItemSelectedListener(this);
                hero2.setOnTouchListener(this);

                Spinner hero3 = (Spinner) dialog.findViewById(R.id.heroSpinner3);
                hero3.setAdapter(heroArrayAdapter);
                hero3.setOnItemSelectedListener(this);
                hero3.setOnTouchListener(this);
                break;
            }
            case R.id.cancelNewCampaignButton: {
                this.finish();
                break;
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.heroSpinner1:
            case R.id.heroSpinner2:
            case R.id.heroSpinner3: {
                if (!((Hero)adapterView.getItemAtPosition(i)).getName().equals(getResources().getString(R.string.select_hero))) {
                    unavailableHeroes.add((Hero)adapterView.getItemAtPosition(i));
                }
                break;
            }
            case R.id.guildSpinner: {
                if (!((String)adapterView.getItemAtPosition(i)).equals(getResources().getString(R.string.select_guild))) {
                    unavailableGuilds.add((String)adapterView.getItemAtPosition(i));
                }
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            int id = view.getId();
            switch (id) {
                case R.id.heroSpinner1:
                case R.id.heroSpinner2:
                case R.id.heroSpinner3:
                    unavailableHeroes.remove((Hero)((Spinner) view).getSelectedItem());
                    break;
                case R.id.guildSpinner:
                    unavailableGuilds.remove((String)((Spinner) view).getSelectedItem());
                    break;
            }

        }
        return false;
    }

    private class CampaignPlayerAdapter extends ArrayAdapter<CampaignPlayer> {

        public CampaignPlayerAdapter(Context context, List<CampaignPlayer> list) {
            super(context, -1, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CampaignPlayer campaignPlayer = getItem(position);
            if (convertView==null) {
                convertView = getLayoutInflater().inflate(R.layout.new_campaign_player_layout, parent, false);
            }
            TextView playerName = ((TextView) convertView.findViewById(R.id.campaignPlayerName));
            playerName.setText(campaignPlayer.getPlayer().getName());
            playerName.setTextColor(getResources().getIntArray(R.array.guild_colors_array)[campaignPlayer.getGuild()]);
            String heroList = "";
            for (PlayerHero hero: campaignPlayer.getHeroes()) {
                if (!heroList.equals("")) {
                    heroList += ", ";
                }
                heroList += hero.getHero().getName();
            }
            ((TextView) convertView.findViewById(R.id.campaignPlayerHeroes)).setText(heroList);
            return convertView;
        }
    }
}
