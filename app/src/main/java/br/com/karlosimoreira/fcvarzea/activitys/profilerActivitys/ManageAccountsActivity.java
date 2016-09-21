package br.com.karlosimoreira.fcvarzea.activitys.profilerActivitys;

import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import br.com.karlosimoreira.fcvarzea.R;
import br.com.karlosimoreira.fcvarzea.domain.util.SlidingTabLayout;
import br.com.karlosimoreira.fcvarzea.adapter.TabsAdapter;
import br.com.karlosimoreira.fcvarzea.fragments.profilerFragments.LinkAccountsFragment;

public class ManageAccountsActivity extends AppCompatActivity implements  LinkAccountsFragment.OnFragmentInteractionListener{


    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_accounts);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.colorIcons));
        setSupportActionBar(toolbar);

        viewPager = (ViewPager)findViewById(R.id.vp_tabs);
        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(),this));

        slidingTabLayout = (SlidingTabLayout)findViewById(R.id.stl_tabs);
        slidingTabLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
        slidingTabLayout.setSelectedIndicatorColors(R.color.colorAccent);
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init(){
        toolbar.setTitle( getResources().getString(R.string.title_activity_manage_accounts) );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
