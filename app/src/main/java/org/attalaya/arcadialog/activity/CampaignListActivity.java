package org.attalaya.arcadialog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.attalaya.arcadialog.R;
import org.attalaya.arcadialog.controller.ArcadiaController;
import org.attalaya.arcadialog.model.Campaign;
import org.attalaya.arcadialog.model.CampaignPlayer;
import org.attalaya.arcadialog.model.PlayerHero;

import io.realm.RealmBaseAdapter;

public class CampaignListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private ArcadiaController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_list);

        controller = ArcadiaController.getInstance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView campaignList = (ListView) findViewById(R.id.campaignListView);
        campaignList.setOnItemClickListener(this);
        campaignList.setOnItemLongClickListener(this);
        campaignList.setAdapter(new RealmBaseAdapter<Campaign>(this, controller.getCampaigns(), true) {
            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                int n = 0;
                Campaign campaign = realmResults.get(i);
                LinearLayout layout = view==null?new LinearLayout(this.context):(LinearLayout)view;
                layout.setPadding(16, 16, 16, 16);
                layout.setOrientation(LinearLayout.VERTICAL);
                TextView campaignType = view==null?new TextView(this.context):(TextView)layout.getChildAt(n++);
                campaignType.setTextAppearance(this.context, android.R.style.TextAppearance_DeviceDefault_Large);
                campaignType.setText(campaign.getCampaignType().getName());
                if (view==null) layout.addView(campaignType);
                for (CampaignPlayer campaignPlayer: campaign.getPlayers()) {
                    TextView player = view==null?new TextView(this.context):(TextView)layout.getChildAt(n++);
                    player.setText(campaignPlayer.getPlayer().getName());
                    player.setTextAppearance(this.context, android.R.style.TextAppearance_DeviceDefault_Medium);
                    player.setTextColor(getResources().getIntArray(R.array.guild_colors_array)[campaignPlayer.getGuild()]);
                    if (view==null) layout.addView(player);
                    TextView heroes = view==null?new TextView(this.context):(TextView)layout.getChildAt(n++);
                    String heroList = "";
                    for (PlayerHero hero: campaignPlayer.getHeroes()) {
                        if (!heroList.equals("")) {
                            heroList += ", ";
                        }
                        heroList += hero.getHero().getName();
                    }
                    heroes.setText(heroList);
                    if (view==null) layout.addView(heroes);
                }
                layout.setTag(campaign);
                return layout;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, NewCampaignActivity.class);
        startActivity(intent);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.campaign, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_current) {
            // Handle the camera action
        } else if (id == R.id.nav_finished) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, CampaignActivity.class);
        intent.putExtra("campaignId",((Campaign)view.getTag()).getCampaignId());
        startActivity(intent);
    }
}
