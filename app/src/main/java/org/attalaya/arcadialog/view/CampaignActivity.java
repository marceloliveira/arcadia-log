package org.attalaya.arcadialog.view;

import android.animation.ArgbEvaluator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import org.attalaya.arcadialog.R;
import org.attalaya.arcadialog.controller.ArcadiaController;
import org.attalaya.arcadialog.model.Campaign;

public class CampaignActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private Campaign campaign;
    private ArcadiaController controller;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private CampaignSectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);

        controller = ArcadiaController.getInstance(this);
        campaign = controller.getCampaign(getIntent().getLongExtra("campaignId",0));
        tabColors = new int[campaign.getPlayers().size()+1];
        tabColors[0] = getResources().getColor(R.color.colorPrimaryLight);
        for (int i = 0; i < campaign.getPlayers().size(); i++) {
            tabColors[i+1] = getResources().getIntArray(R.array.guild_colors_array)[campaign.getPlayers().get(i).getGuild()];
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new CampaignSectionsPagerAdapter(getSupportFragmentManager(), campaign);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_campaign, menu);
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

    private int[] tabColors;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int colorFrom = tabColors[position];
        int colorTo = colorFrom;
        if (position<tabColors.length-1) {
            colorTo = tabColors[position+1];
        }
        findViewById(R.id.pager_title_strip).setBackgroundColor((int) new ArgbEvaluator().evaluate(positionOffset,colorFrom,colorTo));
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_campaign, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(((CampaignActivity)getActivity()).mSectionsPagerAdapter.getPageTitle(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class CampaignSectionsPagerAdapter extends FragmentPagerAdapter {

        private Campaign campaign;

        public CampaignSectionsPagerAdapter(FragmentManager fm, Campaign campaign) {
            super(fm);
            this.campaign = campaign;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return CampaignScenarioFragment.newInstance(campaign.getId());
                default:
                    return CampaignPlayerFragment.newInstance(campaign.getId(),position-1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return campaign.getPlayers().size()+1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position>=getCount()) return null;
            switch (position) {
                case 0:
                    return campaign.getCampaignType().getName();
                default:
                    return campaign.getPlayers().get(position-1).getPlayer().getName();
            }
        }
    }
}
