package fit.cvut.org.cz.bowling.business.loaders;

import android.content.Context;
import android.util.Log;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IWinConditionManager;
import fit.cvut.org.cz.bowling.business.serialization.WinConditionSerializer;
import fit.cvut.org.cz.bowling.data.entities.WinCondition;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;

public class WinConditionLoader {
    public static void importWinCondition(Context context, ServerCommunicationItem winCondition, Tournament importedTournament) {
        Log.d("IMPORT", "WIN_CONDITION: " + winCondition.syncData);
        WinCondition importedWinCondition = WinConditionSerializer.getInstance(context).deserialize(winCondition);
        importedWinCondition.setTournamentId(importedTournament.getId());
        IWinConditionManager manager = ManagerFactory.getInstance(context).getEntityManager(WinCondition.class);
        manager.insert(importedWinCondition);
    }
}
