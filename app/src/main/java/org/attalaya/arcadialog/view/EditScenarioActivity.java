package org.attalaya.arcadialog.view;

import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;

import org.attalaya.arcadialog.R;
import org.attalaya.arcadialog.controller.ArcadiaController;
import org.attalaya.arcadialog.model.Campaign;
import org.attalaya.arcadialog.model.CampaignPlayer;
import org.attalaya.arcadialog.model.CampaignScenario;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class EditScenarioActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, Button.OnClickListener {

    ArcadiaController mController;
    Campaign mCampaign;
    CampaignScenario mCampaignScenario;
    List<RadioButton> mWinnerRadio;
    List<CheckBox> mDeathCheck;
    List<CheckBox> mCoinCheck;
    List<CheckBox> mRewardCheck;
    List<CheckBox> mTitleCheck;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_scenario);

        mController = ArcadiaController.getInstance(this);
        mCampaign = mController.getCampaign(getIntent().getLongExtra("campaignId", 0));
        mCampaignScenario = mController.getCampaignScenarios(mCampaign).get(getIntent().getIntExtra("campaignScenarioId",0));

        TableRow headerTR = (TableRow)findViewById(R.id.headerTR);
        for (int i = 0; i < mCampaign.getPlayers().size(); i++) {
            TextView t = new TextView(this);
            t.setWidth(40);
            t.setTextSize(20);
            t.setText(String.valueOf(mCampaign.getPlayers().get(i).getPlayer().getName().charAt(0)));
            t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            headerTR.addView(t);
        }

        mWinnerRadio = new ArrayList<>();
        TableRow winnerTR = (TableRow)findViewById(R.id.winnerTR);
        for (int i = 0; i < mCampaign.getPlayers().size(); i++) {
            CampaignPlayer campaignPlayer = mCampaign.getPlayers().get(i);
            RadioButton r = new RadioButton(this);
            r.setOnCheckedChangeListener(this);
            r.setTag(campaignPlayer);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                r.setButtonTintList(
                        getColorStateList(
                                getResources().getIdentifier(
                                        getResources().getStringArray(R.array.guild_array)[campaignPlayer.getGuild()]+"_color_state_list",
                                        "color",
                                        this.getPackageName())));
            }
            if (mCampaignScenario.getWinner()!=null && mCampaignScenario.getWinner().equals(campaignPlayer)) {
                r.setChecked(true);
            }
            mWinnerRadio.add(r);
            winnerTR.addView(r);
        }

        mDeathCheck = new ArrayList<>();
        TableRow deathTR = (TableRow)findViewById(R.id.leastDeathsTR);
        for (int i = 0; i < mCampaign.getPlayers().size(); i++) {
            CampaignPlayer campaignPlayer = mCampaign.getPlayers().get(i);
            CheckBox c = new CheckBox(this);
            c.setTag(campaignPlayer);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                c.setButtonTintList(
                        getColorStateList(
                                getResources().getIdentifier(
                                        getResources().getStringArray(R.array.guild_array)[campaignPlayer.getGuild()]+"_color_state_list",
                                        "color",
                                        this.getPackageName())));
            }
            if (mCampaignScenario.getLeastDeaths().contains(campaignPlayer)) {
                c.setChecked(true);
            }
            mDeathCheck.add(c);
            deathTR.addView(c);
        }

        mCoinCheck = new ArrayList<>();
        TableRow coinTR = (TableRow)findViewById(R.id.mostCoinsTR);
        for (int i = 0; i < mCampaign.getPlayers().size(); i++) {
            CampaignPlayer campaignPlayer = mCampaign.getPlayers().get(i);
            CheckBox c = new CheckBox(this);
            c.setTag(campaignPlayer);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                c.setButtonTintList(
                        getColorStateList(
                                getResources().getIdentifier(
                                        getResources().getStringArray(R.array.guild_array)[campaignPlayer.getGuild()]+"_color_state_list",
                                        "color",
                                        this.getPackageName())));
            }
            if (mCampaignScenario.getMostCoins().contains(campaignPlayer)) {
                c.setChecked(true);
            }
            mCoinCheck.add(c);
            coinTR.addView(c);
        }

        mRewardCheck = new ArrayList<>();
        TableRow rewardTR = (TableRow)findViewById(R.id.rewardTR);
        for (int i = 0; i < mCampaign.getPlayers().size(); i++) {
            CampaignPlayer campaignPlayer = mCampaign.getPlayers().get(i);
            CheckBox c = new CheckBox(this);
            c.setTag(campaignPlayer);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                c.setButtonTintList(
                        getColorStateList(
                                getResources().getIdentifier(
                                        getResources().getStringArray(R.array.guild_array)[campaignPlayer.getGuild()]+"_color_state_list",
                                        "color",
                                        this.getPackageName())));
            }
            if (mCampaignScenario.getReward().contains(campaignPlayer)) {
                c.setChecked(true);
            }
            mRewardCheck.add(c);
            rewardTR.addView(c);
        }

        mTitleCheck = new ArrayList<>();
        TableRow titleTR = (TableRow)findViewById(R.id.titleTR);
        for (int i = 0; i < mCampaign.getPlayers().size(); i++) {
            CampaignPlayer campaignPlayer = mCampaign.getPlayers().get(i);
            CheckBox c = new CheckBox(this);
            c.setTag(campaignPlayer);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                c.setButtonTintList(
                        getColorStateList(
                                getResources().getIdentifier(
                                        getResources().getStringArray(R.array.guild_array)[campaignPlayer.getGuild()]+"_color_state_list",
                                        "color",
                                        this.getPackageName())));
            }
            if (mCampaignScenario.getTitle().contains(campaignPlayer)) {
                c.setChecked(true);
            }
            mTitleCheck.add(c);
            titleTR.addView(c);
        }

        Button cancelButton = (Button)findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);

        Button saveButton = (Button)findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (mWinnerRadio.contains(buttonView)) {
                for (CompoundButton b : mWinnerRadio) {
                    if (b != buttonView) {
                        b.setChecked(false);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button: this.finish(); break;
            case R.id.save_button:
                CampaignPlayer winner = null;
                for (RadioButton view: mWinnerRadio) {
                    if (view.isChecked())
                        winner = (CampaignPlayer)view.getTag();
                }
                RealmList<CampaignPlayer> deaths = new RealmList<>();
                for (CheckBox view: mDeathCheck) {
                    if (view.isChecked())
                        deaths.add((CampaignPlayer)view.getTag());
                }
                RealmList<CampaignPlayer> coins = new RealmList<>();
                for (CheckBox view: mCoinCheck) {
                    if (view.isChecked())
                        coins.add((CampaignPlayer)view.getTag());
                }
                RealmList<CampaignPlayer> reward = new RealmList<>();
                for (CheckBox view: mRewardCheck) {
                    if (view.isChecked())
                        reward.add((CampaignPlayer)view.getTag());
                }
                RealmList<CampaignPlayer> title = new RealmList<>();
                for (CheckBox view: mTitleCheck) {
                    if (view.isChecked())
                        title.add((CampaignPlayer)view.getTag());
                }
                mController.editCampaignScenario(mCampaignScenario,winner,deaths,coins,reward,title);
                this.finish();
        }
    }
}
