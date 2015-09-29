package me.neutze.masterpatcher.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.neutze.masterpatcher.R;
import me.neutze.masterpatcher.models.Application;

/**
 * Created by H1GHWAvE on 24/09/15.
 */
public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ApplicationViewHolder> {
    private List<Application> applications;
    private Context context;
    private static ClickListener clickListener;

    public ApplicationsAdapter(List<Application> applications, Context context) {
        this.applications = applications;
        this.context = context;
    }

    @Override
    public ApplicationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_application, viewGroup, false);
        return new ApplicationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ApplicationViewHolder mApplicationViewHolder, int i) {
        mApplicationViewHolder.applicationName.setText(applications.get(i).getName());
        if (applications.get(i).hasLicense()) {
            mApplicationViewHolder.licenseVerification.setText(context.getString(R.string.patch));
        } else {
            mApplicationViewHolder.licenseVerification.setText(context.getString(R.string.no_patch));
        }
        mApplicationViewHolder.applicationLogo.setImageDrawable(applications.get(i).getLogo());
    }

    @Override
    public int getItemCount() {
        return applications.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        ApplicationsAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }


    public static class ApplicationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView mCardView;
        TextView applicationName;
        TextView licenseVerification;
        ImageView applicationLogo;

        ApplicationViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.application_card);
            applicationName = (TextView) itemView.findViewById(R.id.app_name);
            licenseVerification = (TextView) itemView.findViewById(R.id.app_license);
            applicationLogo = (ImageView) itemView.findViewById(R.id.app_logo);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }
}


