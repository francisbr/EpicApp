package francis.epicapp.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import francis.epicapp.InternetStatusListener;
import francis.epicapp.R;
import francis.epicapp.fragments.HoraireFragment;
import francis.epicapp.fragments.ListVideoFragment;
import francis.epicapp.fragments.NoInternetFragment;
import francis.epicapp.fragments.StreamFragment;
import francis.epicapp.fragments.SearchFragment;
import francis.epicapp.fragments.playlists.PlaylistsFragment;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    // Make sure to be using android.support.v7.app.ActionBarDrawerToggle version.
    // The android.support.v4.app.ActionBarDrawerToggle has been deprecated.
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String projectToken = "92122f5b3896932def144c890cad4582"; // e.g.: "1ef7e30d2a58d27f4b90c42e31d6d7ad"
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);

        try {
            JSONObject props = new JSONObject();
            props.put("Gender", "Female");
            props.put("Logged in", false);
            mixpanel.track("MainActivity - onCreate called", props);
        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }

        MixpanelAPI.People people = mixpanel.getPeople();

        people.identify(mixpanel.getDistinctId());
        people.initPushHandling("724877184015");
        Log.d("id?", people.getDistinctId());

        /** Init le menu **/
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        setupDrawerContent(nvDrawer);
        /** **/
    }


    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });

        Fragment frag;
        if (InternetStatusListener.isOnline(getApplicationContext())) {
            //Setting up openning fragment!
            frag = new ListVideoFragment();

            //---------------------------------
        } else {
            frag = new NoInternetFragment();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, frag).commit();
        navigationView.setCheckedItem(R.id.seeAllVid);
    }

    /**
     * Gere les clicks dans le slider menu
     *
     * @param menuItem
     */
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch (menuItem.getItemId()) {
            case R.id.seeAllVid:
                if (InternetStatusListener.isOnline(getApplicationContext())) {
                    fragmentClass = ListVideoFragment.class;
                } else
                    fragmentClass = NoInternetFragment.class;
                break;
            case R.id.seePlaylists:
                if (InternetStatusListener.isOnline(getApplicationContext())) {
                    fragmentClass = PlaylistsFragment.class;
                } else
                    fragmentClass = NoInternetFragment.class;
                break;
            case R.id.seeSearch:
                if (InternetStatusListener.isOnline(getApplicationContext())) {
                    fragmentClass = SearchFragment.class;
                } else
                    fragmentClass = NoInternetFragment.class;
                break;

            case R.id.seeHoraireTwitch:
                fragmentClass = HoraireFragment.class;
                break;
            case R.id.watchStream:
                if (InternetStatusListener.isOnline(getApplicationContext())) {
                    fragmentClass = StreamFragment.class;
                } else
                    fragmentClass = NoInternetFragment.class;
                break;
            default:
                fragmentClass = HoraireFragment.class;
                break;
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();


        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

}
