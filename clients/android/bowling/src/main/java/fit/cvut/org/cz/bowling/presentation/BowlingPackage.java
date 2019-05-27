package fit.cvut.org.cz.bowling.presentation;

import android.app.Application;

/**
 * Class for maintaining global application state
 */
public class BowlingPackage extends Application {
    public static final String BOWLING_NAME= "Bowling";

    private static String sportContext;

    public static String getSportContext() {return sportContext;}

    public void setSportContext(String sportContext) {this.sportContext = sportContext;}
}
