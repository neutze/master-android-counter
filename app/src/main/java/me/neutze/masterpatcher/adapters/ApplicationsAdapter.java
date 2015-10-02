package me.neutze.masterpatcher.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import me.neutze.masterpatcher.R;
import me.neutze.masterpatcher.activities.ApplicationActivity;
import me.neutze.masterpatcher.models.APKItem;

public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ApplicationViewHolder> {
    private List<APKItem> applications;
    private Context context;

    public ApplicationsAdapter(List<APKItem> applications, Context context) {
        this.applications = applications;
        this.context = context;
    }

    @Override
    public ApplicationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_application, viewGroup, false);
        return new ApplicationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ApplicationViewHolder mApplicationViewHolder, int position) {

        mApplicationViewHolder.applicationName.setText(applications.get(position).getName());

        String permissions = null;

        switch (applications.get(position).getAttributes()) {
            case (1):
                break;
            case (10):

                break;
            case (11):
                break;
            case (100):
                break;
            case (101):
                break;
            case (110):
                break;
            case (111):
                break;
            default:
                break;
        }


        if (applications.get(position).getLvl()) {
            permissions = context.getResources().getString(R.string.lvl);
        }

        if (applications.get(position).getBilling()) {
            if (permissions == null)
                permissions = context.getResources().getString(R.string.billing);
            else
                permissions += "\n" + context.getResources().getString(R.string.billing);
        }
        if (applications.get(position).getAds()) {
            if (permissions == null)
                permissions = context.getResources().getString(R.string.ads);
            else
                permissions += "\n" + context.getResources().getString(R.string.ads);
        }

        if (permissions == null)
            permissions = context.getString(R.string.no_patch);

        mApplicationViewHolder.options.setText(permissions);
        mApplicationViewHolder.applicationLogo.setImageDrawable(applications.get(position).getIcon());

        initLabels();
        initOnClickListener(mApplicationViewHolder, position);
    }

    private void initLabels() {
    }

    private void initOnClickListener(final ApplicationViewHolder mApplicationViewHolder, final int position) {
        mApplicationViewHolder.app_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mApplicationViewHolder.app_options_menu.getVisibility() == View.VISIBLE) {
                    mApplicationViewHolder.app_options_menu.setVisibility(View.GONE);
                    mApplicationViewHolder.app_layout.setBackgroundColor(context.getResources().getColor(R.color.primary_light));
                } else if (mApplicationViewHolder.app_options_menu.getVisibility() == View.GONE) {
                    mApplicationViewHolder.app_options_menu.setVisibility(View.VISIBLE);
                }
            }
        });

        mApplicationViewHolder.application_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ApplicationActivity.class);
                intent.putExtra(context.getString(R.string.app_parcel), applications.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        mApplicationViewHolder.application_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("JOHANNES", "2");
            }
        });

        mApplicationViewHolder.application_license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("JOHANNES", "3");
            }
        });
    }

    @Override
    public int getItemCount() {
        return applications.size();
    }

    public static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView applicationName;
        TextView options;
        ImageView applicationLogo;
        LinearLayout app_options_menu;
        LinearLayout application_info;
        LinearLayout application_permission;
        LinearLayout application_license;
        RelativeLayout app_layout;

        ApplicationViewHolder(View itemView) {
            super(itemView);
            app_layout = (RelativeLayout) itemView.findViewById(R.id.app_layout);
            mCardView = (CardView) itemView.findViewById(R.id.application_card);
            applicationName = (TextView) itemView.findViewById(R.id.app_name);
            options = (TextView) itemView.findViewById(R.id.app_options);
            applicationLogo = (ImageView) itemView.findViewById(R.id.app_logo);
            app_options_menu = (LinearLayout) itemView.findViewById(R.id.app_options_menu);
            application_info = (LinearLayout) itemView.findViewById(R.id.application_info);
            application_permission = (LinearLayout) itemView.findViewById(R.id.application_permission);
            application_license = (LinearLayout) itemView.findViewById(R.id.application_license);
        }
    }
}


