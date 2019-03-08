package fit.cvut.org.cz.squash.presentation;

import android.app.Application;

/**
 * Created by kevin on 15.10.2016.
 */
public class SquashPackage extends Application {

    public static final String BADMINTON_NAME = "Badminton";
    public static final String BEACH_NAME = "Beach";
    public static final String SQUASH_NAME = "Squash";
    public static final String TENNIS_NAME = "Tennis";
    public static final String VOLLEYBALL_NAME = "Volleyball";

    private static String sportContext;

    public static String getSportContext() {
        return sportContext;
    }

    public void setSportContext(String sportContext) {
        this.sportContext = sportContext;
    }
}
