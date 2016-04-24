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
    ArrayList<DStat> getByParticipant(Context context, long participantId, StatsEnum type);
}
