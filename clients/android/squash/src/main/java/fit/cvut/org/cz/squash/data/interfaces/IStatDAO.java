package fit.cvut.org.cz.squash.data.interfaces;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.data.entities.DStat;
import fit.cvut.org.cz.squash.data.entities.StatsEnum;

/**
 * Created by Vaclav on 21. 4. 2016.
 */
public interface IStatDAO {

    void insert(Context context, DStat stat);
    ArrayList<Long> getPlayerIdsForParticipant(Context context, long id);
    void delete(Context context, long participantId, StatsEnum type);
    void update(Context context, DStat stat);
    void deleteByParticipant(Context context, long participantId);
    ArrayList<DStat> getByParticipant(Context context, long participantId, StatsEnum type);
    ArrayList<DStat> getByTournament(Context context, long tournamentId, StatsEnum type);
}
