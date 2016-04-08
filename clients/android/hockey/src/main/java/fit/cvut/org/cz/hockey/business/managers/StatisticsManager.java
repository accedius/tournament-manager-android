package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.business.entities.AgregatedStatistics;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public class StatisticsManager {

    public ArrayList<AgregatedStatistics> getByCompetitionID( Context context, long compId )
    {
        //TODO a zrusit mock

        ArrayList<AgregatedStatistics> res = new ArrayList<>();
        res.add( new AgregatedStatistics(1, "Petr", 10, 5, 4, 1, 12, 20, 12, 20 ) );
        res.add( new AgregatedStatistics(1, "Pavel", 0, 0, 0, 0, 0, 0, 0, 0 ) );
        res.add( new AgregatedStatistics(1, "Igor", 1, 1, 0, 0, 5, 1, 6, 4 ) );
        return res;
    }
}
