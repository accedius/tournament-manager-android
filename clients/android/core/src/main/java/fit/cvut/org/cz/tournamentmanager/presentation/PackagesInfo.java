package fit.cvut.org.cz.tournamentmanager.presentation;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by kevin on 28.9.2016.
 */
public class PackagesInfo {
    private static ArrayList<ApplicationInfo> getPackages(Context context, Resources res) {
        List<ApplicationInfo> packages = context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        ArrayList<ApplicationInfo> sport_packages = new ArrayList<>();

        // TODO r.string.tournament_manager_package -- define as constant somewhere

        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.metaData != null) {
                if (packageInfo.metaData.containsKey("application_type") == true
                        && packageInfo.metaData.get("application_type").equals(res.getString(R.string.tournament_manager_package))) {
                    sport_packages.add(packageInfo);
                }
            }
        }
        return sport_packages;
    }


    public static Map<String, ApplicationInfo> getSportContexts (Context context, Resources res) {
        Map<String, ApplicationInfo> contexts = new TreeMap<>();
        for (ApplicationInfo app : getPackages(context, res)) {
            for (String sport_context : app.metaData.getString("context_names").split(",")) {
                contexts.put(sport_context, app);
            }
        }
        return contexts;
    }
}
