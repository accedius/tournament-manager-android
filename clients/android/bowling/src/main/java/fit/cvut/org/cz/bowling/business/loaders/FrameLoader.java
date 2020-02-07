package fit.cvut.org.cz.bowling.business.loaders;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IFrameManager;
import fit.cvut.org.cz.bowling.business.serialization.Constants;
import fit.cvut.org.cz.bowling.business.serialization.FrameSerializer;
import fit.cvut.org.cz.bowling.data.entities.Frame;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;

public class FrameLoader {
    public static void importFrames(Context context, List<ServerCommunicationItem> frames, Player importedPlayer, Match importedMatch, Participant addedParticipant) {
        for (ServerCommunicationItem frame : frames) {
            List<ServerCommunicationItem> frameRolls = new ArrayList<>();

            for (ServerCommunicationItem subItem : frame.getSubItems()) {
                if (subItem.getType().equals(Constants.ROLL)) {
                    frameRolls.add(subItem);
                }
            }

            Log.d("IMPORT", "Frame: " + frame.syncData);
            Frame importedFrame = FrameSerializer.getInstance(context).deserialize(frame);
            importedFrame.setPlayerId(importedPlayer.getId());
            importedFrame.setParticipantId(addedParticipant.getId());
            importedFrame.setMatchId(importedMatch.getId());
            IFrameManager manager = ManagerFactory.getInstance(context).getEntityManager(Frame.class);
            manager.insert(importedFrame);

            /* Load Rolls */
            RollLoader.importRolls(context, frameRolls, importedFrame);
        }
    }
}
