package org.attalaya.arcadialog.view;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.attalaya.arcadialog.R;
import org.attalaya.arcadialog.controller.ArcadiaController;
import org.attalaya.arcadialog.model.CampaignScenario;
import org.attalaya.arcadialog.model.Scenario;

public class CampaignScenarioFragment extends Fragment implements View.OnClickListener, AlertDialog.OnClickListener, AdapterView.OnItemClickListener {

    private static final String ARG_CAMPAIGN_ID = "campaign_id";

    private long mCampaignId;
    private ArcadiaController controller;
    private Button newScenario;
    private AlertDialog dialog;
    private ArrayAdapter<CampaignScenario> campaignScenarioArrayAdapter;


    public CampaignScenarioFragment() {
    }

    public static CampaignScenarioFragment newInstance(long campaignId) {
        CampaignScenarioFragment fragment = new CampaignScenarioFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CAMPAIGN_ID, campaignId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCampaignId = getArguments().getLong(ARG_CAMPAIGN_ID);
            controller = ArcadiaController.getInstance(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_campaign_scenario, container, false);


        ListView scenarioList = (ListView) rootView.findViewById(R.id.scenarioListView);
        campaignScenarioArrayAdapter = new ArrayAdapter<CampaignScenario>(getActivity(), R.layout.campaign_scenario_list_item, controller.getCampaignScenarios(controller.getCampaign(mCampaignId))){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView==null) convertView = getActivity().getLayoutInflater().inflate(R.layout.campaign_scenario_list_item,null);
                CampaignScenario s = getItem(position);
                ((TextView)convertView.findViewById(R.id.scenarioName)).setText(s.getScenario().getName());
                ((TextView)convertView.findViewById(R.id.winnerName)).setText(s.getWinner()==null?"Winner":s.getWinner().getPlayer().getName());
                return convertView;
            }
        };
        scenarioList.setAdapter(campaignScenarioArrayAdapter);
        scenarioList.setOnItemClickListener(this);
        if (controller.getCampaign(mCampaignId).getScenarios().size()<6) {
            newScenario = new Button(getActivity());
            newScenario.setText(R.string.addScenario);
            newScenario.setOnClickListener(this);
            scenarioList.addFooterView(newScenario);
        }

        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view==newScenario) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setTitle(R.string.addScenario).setNegativeButton("Cancel",this).setAdapter(new ArrayAdapter<Scenario>(this.getActivity(),android.R.layout.simple_list_item_1,controller.getScenarios(controller.getCampaign(mCampaignId))){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView==null) convertView = getActivity().getLayoutInflater().inflate(R.layout.scenario_list_item,null);
                    Scenario s = getItem(position);
                    ((TextView)convertView.findViewById(R.id.tierText)).setText(s.getPosition()==0?"Outer":s.getPosition()==1?"Inner":"Final");
                    ((TextView)convertView.findViewById(R.id.scenarioName)).setText(s.getName());
                    ((TextView)convertView.findViewById(R.id.difficultyText)).setText(s.getDifficulty()==0?"Easy":s.getPosition()==1?"Medium":"Hard");
                    return convertView;
                }
            },this);
            dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i>=0) {
            controller.addScenario(controller.getCampaign(mCampaignId), (Scenario) dialog.getListView().getItemAtPosition(i));
            campaignScenarioArrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this.getActivity(), EditScenarioActivity.class);
        intent.putExtra("campaignId",mCampaignId);
        intent.putExtra("campaignScenarioId",i);
        startActivity(intent);
    }
}
