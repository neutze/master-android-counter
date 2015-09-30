package me.neutze.masterpatcher.utils.dialogs;

/**
 * Created by H1GHWAvE on 29/09/15.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.neutze.masterpatcher.R;
import me.neutze.masterpatcher.models.APKItem;

public class ApplicationDialog extends DialogFragment {

    private APKItem application;

    public ApplicationDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = getArguments().getParcelable(getActivity().getResources().getString(R.string.parcelable_application));
    }

    public static ApplicationDialog newInstance(Context context, APKItem application) {
        ApplicationDialog mApplicationDialog = new ApplicationDialog();
        Bundle args = new Bundle();
        args.putParcelable(context.getResources().getString(R.string.parcelable_application), application);
        mApplicationDialog.setArguments(args);
        return mApplicationDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TODO: dialog
        View view = inflater.inflate(R.layout.dialog_application, container);
        String inputFile = getActivity().getResources().getString(R.string.app_folder) + application.name + "/" + getActivity().getResources().getString(R.string.base_apk);
        String outputFolder = "/storage/sdcard0/Download/";
        getDialog().setTitle(application.name);

        //new ZipUtils.ZipAsyncTask().execute(inputFile, outputFolder);

        return view;
    }


}