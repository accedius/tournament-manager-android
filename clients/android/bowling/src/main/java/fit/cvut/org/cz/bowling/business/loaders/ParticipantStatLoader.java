package fit.cvut.org.cz.bowling.business.loaders;

import android.content.Context;
import android.util.Log;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantStatManager;
import fit.cvut.org.cz.bowling.business.serialization.ParticipantStatSerializer;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;

public class ParticipantStatLoader {

    public static void importParticipantStat(Context context, ServerCommunicationItem participantStat, Participant addedParticipant) {
        Log.d("IMPORT", "PARTICIPANT_STAT: " + participantStat.syncData);
        ParticipantStat importedParticipantStat = ParticipantStatSerializer.getInstance(context).deserialize(participantStat);
        importedParticipantStat.setParticipantId(addedParticipant.getId());
        IParticipantStatManager manager = ManagerFactory.getInstance(context).getEntityManager(ParticipantStat.class);
        manager.insert(importedParticipantStat);
    }
}
