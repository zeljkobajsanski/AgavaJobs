package com.bitseverywhere.agavajobs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.bitseverywhere.agavajobs.R;
import com.bitseverywhere.agavajobs.fragments.BiografijaFragment;
import com.bitseverywhere.agavajobs.fragments.DetaljiPoslaFragment;
import com.bitseverywhere.agavajobs.fragments.HomeFragment;
import com.bitseverywhere.agavajobs.fragments.IFragment;
import com.bitseverywhere.agavajobs.fragments.NavigationDrawerFragment;
import com.bitseverywhere.agavajobs.fragments.PregledPoslovaFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, IMainActivity {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private IFragment mActiveFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private BiografijaFragment mBiografijaFragment;
    private PregledPoslovaFragment mPremijumPoslovi, mHotPoslovi, mStandardniPoslovi;
    private Menu menu;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        userId = intent.getIntExtra("USER_ID", 0);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        Fragment home = HomeFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, home)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        onSectionAttached(position);
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                if (mPremijumPoslovi == null) {
                    mPremijumPoslovi = PregledPoslovaFragment.newInstance(PregledPoslovaFragment.PREMIJUM_POSLOVI);
                }
                fragment = mPremijumPoslovi;
                break;
            case 1:
                if (mHotPoslovi == null) {
                    mHotPoslovi = PregledPoslovaFragment.newInstance(PregledPoslovaFragment.HOT_POSLOVI);
                }
                fragment = mHotPoslovi;
                break;
            case 2:
                if (mStandardniPoslovi == null) {
                    mStandardniPoslovi = PregledPoslovaFragment.newInstance(PregledPoslovaFragment.STANDARDNI_POSLOVI);
                }
                fragment = mStandardniPoslovi;
                break;
            case 4:
                if (mBiografijaFragment == null) {
                    mBiografijaFragment = BiografijaFragment.newInstance(userId);
                }
                fragment = mBiografijaFragment;
                break;
        }
        mActiveFragment = (IFragment)fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void idiNaBiografiju() {
        if (mBiografijaFragment == null) {
            mBiografijaFragment = BiografijaFragment.newInstance(userId);
        }
        mActiveFragment = mBiografijaFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, mBiografijaFragment)
                .addToBackStack(null)
                .commit();
        mNavigationDrawerFragment.closeDrawer();
    }

    public void onSectionAttached(int number) {

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);

            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void izabranPosao(int id) {
        Fragment detaljiFragment = DetaljiPoslaFragment.newInstance(id, userId);
        mActiveFragment = (IFragment)detaljiFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, detaljiFragment)
                .addToBackStack(null)
                .commit();
        restoreActionBar();
    }

    @Override
    public void setActionBarTitle(int title) {
        mTitle = getResources().getString(title);
    }

    @Override
    public void otvoriPrmijumPoslove() {
        onNavigationDrawerItemSelected(0);
    }

    @Override
    public void otvoriHotPoslove() {
        onNavigationDrawerItemSelected(1);
    }

    @Override
    public void otvoriStandardnePoslove() {
        onNavigationDrawerItemSelected(2);
    }

    @Override
    public void otvoriBiografiju() {
        onNavigationDrawerItemSelected(4);
    }

    public IFragment getActiveFragment() {
        return mActiveFragment;
    }


}
