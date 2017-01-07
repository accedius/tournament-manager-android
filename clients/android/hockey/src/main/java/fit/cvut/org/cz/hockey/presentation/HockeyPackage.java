package fit.cvut.org.cz.hockey.presentation;

import android.app.Application;

/**
 * Created by kevin on 15.10.2016.
 */
public class HockeyPackage extends Application {

    public static final String FLOORBALL_NAME = "Floorball";
    public static final String HOCKEY_NAME = "Hockey";

    private static String sportContext;

    public static String getSportContext() {
        return sportContext;
    }

    public void setSportContext(String sportContext) {
        this.sportContext = sportContext;
    }
}
