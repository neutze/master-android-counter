package me.neutze.masterpatcher.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.neutze.masterpatcher.R;
import me.neutze.masterpatcher.models.APKItem;

public class ApplicationActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_basic)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        APKItem apkItem = (APKItem) getIntent().getExtras().get(getApplicationContext().getString(R.string.app_parcel));

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setTitle(apkItem != null ? apkItem.getName() : null);
        }
    }
}
