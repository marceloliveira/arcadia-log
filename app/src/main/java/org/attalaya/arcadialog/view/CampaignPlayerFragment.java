package org.attalaya.arcadialog.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.attalaya.arcadialog.R;
import org.attalaya.arcadialog.controller.ArcadiaController;
import org.attalaya.arcadialog.model.Campaign;
import org.attalaya.arcadialog.model.CampaignPlayer;

public class CampaignPlayerFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    private static final String ARG_CAMPAIGN_ID = "campaign_id";
    private static final String ARG_CAMPAIGN_PLAYER_ID = "campaign_player_id";

    private ArcadiaController mController;
    private Campaign mCampaign;
    private CampaignPlayer mCampaignPlayer;

    public CampaignPlayerFragment() {
    }

    public static CampaignPlayerFragment newInstance(long campaignId, int campaignPlayerId) {
        CampaignPlayerFragment fragment = new CampaignPlayerFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CAMPAIGN_ID, campaignId);
        args.putInt(ARG_CAMPAIGN_PLAYER_ID, campaignPlayerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = ArcadiaController.getInstance(getActivity());
        if (getArguments() != null) {
            mCampaign = mController.getCampaign(getArguments().getInt(ARG_CAMPAIGN_ID));
            mCampaignPlayer = mCampaign.getPlayers().get(getArguments().getInt(ARG_CAMPAIGN_PLAYER_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_campaign_player, container, false);

        TextView guildName = (TextView)rootView.findViewById(R.id.guild_name);
        guildName.setText(getResources().getStringArray(R.array.guild_name_array)[mCampaignPlayer.getGuild()]);
        guildName.setTextColor(getResources().getIntArray(R.array.guild_colors_array)[mCampaignPlayer.getGuild()]);

        TextView heroName1 = (TextView)rootView.findViewById(R.id.heroName1);
        heroName1.setText(mCampaignPlayer.getHeroes().get(0).getHero().getName());

        TextView heroName2 = (TextView)rootView.findViewById(R.id.heroName2);
        heroName2.setText(mCampaignPlayer.getHeroes().get(1).getHero().getName());

        TextView heroName3 = (TextView)rootView.findViewById(R.id.heroName3);
        heroName3.setText(mCampaignPlayer.getHeroes().get(2).getHero().getName());

        CheckBox savedCoin = (CheckBox)rootView.findViewById(R.id.saved_coin);
        savedCoin.setChecked(mCampaignPlayer.isSavedCoin());
        savedCoin.setOnCheckedChangeListener(this);

        return rootView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mController.updateCampaignPlayerSavedCoin(mCampaignPlayer,isChecked);
    }
}
