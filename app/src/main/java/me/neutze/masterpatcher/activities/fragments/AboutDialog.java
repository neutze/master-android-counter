package me.neutze.masterpatcher.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.neutze.masterpatcher.R;

/**
 * Created by H1GHWAvE on 01/10/15.
 */
public class AboutDialog extends DialogFragment {

    @Bind(R.id.about_dialog_version)
    TextView about_dialog_version;
    @Bind(R.id.about_dialog_build)
    TextView about_dialog_build;
    @Bind(R.id.about_dialog_ok)
    Button about_dialog_ok;

    private String versionName;
    private int versionCode;

    public AboutDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_application, container);
        ButterKnife.bind(this, view);

        versionName = getArguments().getString(getActivity().getString(R.string.about_dialog_version));
        versionCode = getArguments().getInt(getActivity().getString(R.string.about_dialog_build));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        about_dialog_version.setText(versionName);
        about_dialog_build.setText(String.valueOf(versionCode));

        about_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}