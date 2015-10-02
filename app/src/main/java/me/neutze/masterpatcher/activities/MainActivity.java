package me.neutze.masterpatcher.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.neutze.masterpatcher.R;
import me.neutze.masterpatcher.adapters.ApplicationsAdapter;
import me.neutze.masterpatcher.models.APKItem;
import me.neutze.masterpatcher.utils.RootUtils;
import me.neutze.masterpatcher.utils.SharedPrefUtils;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private static final String NAV_ITEM_ID = "navItemId";

    private final Handler mDrawerActionHandler = new Handler();

    @Bind(R.id.main_recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar_basic)
    Toolbar toolbar_main;
    @Bind(R.id.drawer_header_image)
    ImageView drawer_header_image;

    private List<APKItem> applicationsList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mNavItemId;
    private String versionName;
    private int versionCode;
    private AlertDialog alert;

    public static Toolbar getToolbar(Activity activity) {
        return ((MainActivity) activity).toolbar_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initVersion();
        initRecyclerView();
        initToolbar();
        setupDrawerLayout(savedInstanceState);


    }

    private void setupDrawerLayout(Bundle savedInstanceState) {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // load saved navigation state if present
        if (null == savedInstanceState) {
            mNavItemId = R.id.drawer_home;
        } else {
            mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        }

        // listen for navigation events
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        // select the correct nav menu item
        navigationView.getMenu().findItem(mNavItemId).setChecked(true);

        // set up the hamburger icon to open and close the drawer
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar_main, R.string.open,
                R.string.close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigate(mNavItemId);
    }

    private void initRecyclerView() {
        if (versionCode != SharedPrefUtils.getVersion(getApplicationContext())) {
            SharedPrefUtils.clearAllPrefs(getApplicationContext());
            SharedPrefUtils.saveVersion(getApplicationContext(), versionCode);
        }
        if (RootUtils.isSUavailable()) {
            new APKItemsLoader().execute();
        }
    }

    private void initVersion() {
        PackageManager packetManager = getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packetManager.getPackageInfo(getPackageName(), 0);
            versionCode = packageInfo.versionCode;
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void showAboutDialog() {
        Bundle aboutDialogArgs = new Bundle();
        aboutDialogArgs.putString(getApplicationContext().getString(R.string.about_dialog_version), versionName);
        aboutDialogArgs.putInt(getApplicationContext().getString(R.string.about_dialog_build), versionCode);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getApplicationContext().getString(R.string.app_name))
                .setMessage("Author: Johannes Neutze\nVersion: " + versionName + "." + versionCode)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alert.dismiss();
                    }
                });

        alert = builder.create();
        alert.show();

        Button ok_button = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        if (ok_button != null)
            ok_button.setTextColor(getApplicationContext().getResources().getColor(R.color.primary));
    }


    private void showApplicationDialog(APKItem apkItem) {

        Log.e("JOHANNES", "clickediclick");
        /**FragmentManager mFragmentManager = getSupportFragmentManager();
         ApplicationDialog mApplicationDialog = ApplicationDialog.newInstance(this, apkItem);
         mApplicationDialog.show(mFragmentManager, getApplicationContext().getResources().getString(R.string.dialog_application));
         ?**/
    }

    private void navigate(final int drawerItemId) {
        if (drawerItemId == R.id.drawer_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        if (drawerItemId == R.id.drawer_about) {
            showAboutDialog();
        }
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        // update highlighted item in the navigation menu
        menuItem.setChecked(true);
        mNavItemId = menuItem.getItemId();

        // allow some time after closing the drawer before performing real navigation
        // so the user can see what is happening
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mDrawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(menuItem.getItemId());
            }
        }, DRAWER_CLOSE_DELAY_MS);
        return true;
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar_main);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, mNavItemId);
    }


    private class APKItemsLoader extends AsyncTask<Void, List<APKItem>, Void> {
        ProgressDialog dialog;

        @Override
        protected Void doInBackground(Void... params) {
            applicationsList = APKItem.getApplications(getApplicationContext());
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage(getApplicationContext().getString(R.string.app_load));
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ApplicationsAdapter mApplicationsAdapter = new ApplicationsAdapter(applicationsList, getApplicationContext());
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());

            mApplicationsAdapter.setOnItemClickListener(new ApplicationsAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {

                    showApplicationDialog(applicationsList.get(position));
                }
            });

            dialog.cancel();

            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mRecyclerView.setAdapter(mApplicationsAdapter);
            super.onPostExecute(aVoid);
        }
    }
}
