package fit.cvut.org.cz.hockey.presentation;

import android.app.Application;

/**
 * Created by kevin on 15.10.2016.
 */
public class HockeyPackage extends Application {

    private String sportContext;

    public String getSportContext() {
        return sportContext;
    }

    public void setSportContext(String sportContext) {
        this.sportContext = sportContext;
    }
}
