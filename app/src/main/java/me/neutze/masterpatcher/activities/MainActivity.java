package me.neutze.masterpatcher.activities;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.neutze.masterpatcher.R;
import me.neutze.masterpatcher.adapters.ApplicationsAdapter;
import me.neutze.masterpatcher.models.APKItem;
import me.neutze.masterpatcher.utils.RootUtils;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.main_recyclerView)
    RecyclerView mRecyclerView;

    PackageManager packetManager;
    private List<APKItem> applicationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        packetManager = getPackageManager();

        if (RootUtils.isSUavailable()) {
            new APKItemsLoader().execute();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showApplicationDialog(APKItem apkItem) {


        /**FragmentManager mFragmentManager = getSupportFragmentManager();
         ApplicationDialog mApplicationDialog = ApplicationDialog.newInstance(this, apkItem);
         mApplicationDialog.show(mFragmentManager, getApplicationContext().getResources().getString(R.string.dialog_application));
         ?**/
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
            dialog = ProgressDialog.show(MainActivity.this, "", "Loading. Please wait...", true);
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
