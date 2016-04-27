package fit.cvut.org.cz.squash.business.interfaces;

import android.content.Context;

/**
 * Created by Vaclav on 27. 4. 2016.
 */
public interface IParticipantManager {

    long getTeamIdForMatchParticipant(Context context, long matchId, String role);
}
