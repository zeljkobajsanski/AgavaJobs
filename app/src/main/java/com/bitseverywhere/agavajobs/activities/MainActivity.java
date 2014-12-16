package com.bitseverywhere.agavajobs.activities;

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

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        onSectionAttached(position);
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = PregledPoslovaFragment.newInstance(PregledPoslovaFragment.PREMIJUM_POSLOVI);
                break;
            case 1:
                fragment = PregledPoslovaFragment.newInstance(PregledPoslovaFragment.HOT_POSLOVI);
                break;
            case 2:
                fragment = PregledPoslovaFragment.newInstance(PregledPoslovaFragment.STANDARDNI_POSLOVI);
                break;
            case 4:
                fragment = BiografijaFragment.newInstance(5);
                break;
        }
        mActiveFragment = (IFragment)fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("default")
                .commit();

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_premijum);
                break;
            case 1:
                mTitle = getString(R.string.title_hot);
                break;
            case 2:
                mTitle = getString(R.string.title_standardni);
                break;
            case 3:
                mTitle = getString(R.string.title_moji);
                break;
            case 4:
                mTitle = getString(R.string.title_biografija);
                break;
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            if (mActiveFragment != null) {
                mActiveFragment.refresh();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void izabranPosao(int id) {
        Fragment detaljiFragment = DetaljiPoslaFragment.newInstance(id);
        mActiveFragment = (IFragment)detaljiFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, detaljiFragment)
                .addToBackStack("default")
                .commit();
        restoreActionBar();
    }

    public IFragment getActiveFragment() {
        return mActiveFragment;
    }
}
