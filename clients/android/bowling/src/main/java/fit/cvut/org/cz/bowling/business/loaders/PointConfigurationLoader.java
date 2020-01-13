package fit.cvut.org.cz.bowling.business.loaders;

import android.content.Context;
import android.util.Log;

import java.util.List;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.PointConfigurationManager;
import fit.cvut.org.cz.bowling.business.serialization.PointConfigurationSerializer;
import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

public class PointConfigurationLoader {

    public static void importPointConfigurations(Context context, List<ServerCommunicationItem> pointConfigurations, Tournament importedTournament) {
        for (ServerCommunicationItem pc : pointConfigurations) {
            Log.d("IMPORT", "Point configuration: " + pc.syncData);
            PointConfiguration importedPointConfiguration = PointConfigurationSerializer.getInstance(context).deserialize(pc);
            importedPointConfiguration.setTournamentId(importedTournament.getId());
            PointConfigurationManager manager = ManagerFactory.getInstance(context).getEntityManager(PointConfiguration.class);
            PointConfiguration existing = manager.getBySidesNumber(importedTournament.getId(), importedPointConfiguration.getSidesNumber());
            if (existing == null) {
                manager.insert(importedPointConfiguration);
            } else {
                importedPointConfiguration.setId(existing.id);
                manager.update(importedPointConfiguration);
            }
        }
    }
}
