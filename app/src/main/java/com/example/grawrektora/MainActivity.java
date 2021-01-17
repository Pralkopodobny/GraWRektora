package com.example.grawrektora;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private FragmentManager fragmentManager;

    public enum Screen {
        GAME_SCREEN,
        HELP_SCREEN,
        END_SCREEN;
    }

    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // This will display an Up icon (<-), we will replace it with hamburger later
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        fragmentManager = getSupportFragmentManager();
        setupDrawerContent(nvDrawer);
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
        changeScreen(Screen.HELP_SCREEN);
    }
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = GameFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = HelpFragment.class;
                break;
            default:
                fragmentClass = GameFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert fragment != null;
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        menuItem.setChecked(true);
        mDrawer.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeScreen(Screen newScreen){
        try {
            switch (newScreen){
                case GAME_SCREEN: {
                    fragmentManager.beginTransaction().replace(R.id.flContent, (Fragment) GameFragment.class.newInstance()).commit();
                    nvDrawer.setCheckedItem(R.id.nav_first_fragment);
                    break;
                }
                case END_SCREEN:{
                    fragmentManager.beginTransaction().replace(R.id.flContent, (Fragment)EndGameFragment.class.newInstance()).commit();
                    break;
                }
                case HELP_SCREEN:{
                    fragmentManager.beginTransaction().replace(R.id.flContent, (Fragment)HelpFragment.class.newInstance()).commit();
                    nvDrawer.setCheckedItem(R.id.nav_second_fragment);
                    break;
                }
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }

    }
}