package fit.cvut.org.cz.squash.presentation;

import android.app.Application;

/**
 * Created by kevin on 15.10.2016.
 */
public class SquashPackage extends Application {

    private static String sportContext;

    public static String getSportContext() {
        return sportContext;
    }

    public void setSportContext(String sportContext) {
        this.sportContext = sportContext;
    }
}
