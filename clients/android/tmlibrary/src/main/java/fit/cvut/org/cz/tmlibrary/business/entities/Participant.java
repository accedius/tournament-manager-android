package fit.cvut.org.cz.tmlibrary.business.entities;

import java.util.ArrayList;

/**
 * Created by Vaclav on 20. 4. 2016.
 */
public class Participant {
    long id, matchId, teamId;
    String teamName;
    String role;
    ArrayList<Player> players;
}
