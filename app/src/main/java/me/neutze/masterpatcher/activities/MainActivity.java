package me.neutze.masterpatcher.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
import me.neutze.masterpatcher.models.Application;
import me.neutze.masterpatcher.utils.RootUtils;
import me.neutze.masterpatcher.utils.dialogs.ApplicationDialog;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.main_recyclerView)
    RecyclerView mRecyclerView;

    private List<Application> applicationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (RootUtils.isSUavailable()) {
            setApplicationsList();
        }
    }

    private void setApplicationsList() {
        applicationsList = Application.getApplications(getApplicationContext());
        ApplicationsAdapter mApplicationsAdapter = new ApplicationsAdapter(applicationsList, getApplicationContext());
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);

        mApplicationsAdapter.setOnItemClickListener(new ApplicationsAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                showApplicationDialog(applicationsList.get(position));
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mApplicationsAdapter);
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

    private void showApplicationDialog(Application application) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        ApplicationDialog mApplicationDialog = ApplicationDialog.newInstance(this, application);
        mApplicationDialog.show(mFragmentManager, getApplicationContext().getResources().getString(R.string.dialog_application));
    }

}
