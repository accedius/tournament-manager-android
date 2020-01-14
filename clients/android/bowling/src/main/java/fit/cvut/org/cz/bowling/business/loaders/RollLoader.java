package fit.cvut.org.cz.bowling.business.loaders;

import android.content.Context;
import android.util.Log;

import java.util.List;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IRollManager;
import fit.cvut.org.cz.bowling.business.serialization.RollSerializer;
import fit.cvut.org.cz.bowling.data.entities.Frame;
import fit.cvut.org.cz.bowling.data.entities.Roll;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;

public class RollLoader {
    public static void importRolls(Context context, List<ServerCommunicationItem> rolls, Frame importedFrame) {
        for (ServerCommunicationItem roll : rolls) {
            Log.d("IMPORT", "Roll: " + roll.syncData);
            Roll importedRoll = RollSerializer.getInstance(context).deserialize(roll);
            importedRoll.setFrameId(importedFrame.getId());
            IRollManager manager = ManagerFactory.getInstance(context).getEntityManager(Roll.class);
            manager.insert(importedRoll);
        }
    }
}
