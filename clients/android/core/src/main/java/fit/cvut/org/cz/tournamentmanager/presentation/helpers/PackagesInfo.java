package fit.cvut.org.cz.tournamentmanager.presentation.helpers;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;

/**
 * Created by kevin on 28.9.2016.
 */
public class PackagesInfo {
    public static Map<String, ApplicationInfo> getSportContexts(Context context) {
        Map<String, ApplicationInfo> contexts = new TreeMap<>();
        for (ApplicationInfo app : getPackages(context)) {
            for (String sport_context : app.metaData.getString(CrossPackageConstants.CONTEXT_NAMES).split(",")) {
                contexts.put(sport_context, app);
            }
        }
        return contexts;
    }

    private static ArrayList<ApplicationInfo> getPackages(Context context) {
        List<ApplicationInfo> packages = context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        ArrayList<ApplicationInfo> sport_packages = new ArrayList<>();

        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.metaData == null)
                continue;
            if (!packageInfo.metaData.containsKey(CrossPackageConstants.APP_TYPE))
                continue;
            if (packageInfo.metaData.get(CrossPackageConstants.APP_TYPE)
                    .equals(CrossPackageConstants.TM_PACKAGE))
                    sport_packages.add(packageInfo);
        }
        return sport_packages;
    }
}
