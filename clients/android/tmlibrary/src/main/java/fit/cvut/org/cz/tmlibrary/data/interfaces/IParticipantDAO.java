package fit.cvut.org.cz.tmlibrary.data.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.data.entities.DParticipant;

/**
 * Created by atgot_000 on 18. 4. 2016.
 */
public interface IParticipantDAO {

    long insert(Context context, DParticipant participant);
    void update(Context context, DParticipant participant);
    void delete( Context context, long id );

    ArrayList<DParticipant> getParticipantsByMatchId( Context context, long matchId );
    DParticipant getById( Context context, long id );

}
