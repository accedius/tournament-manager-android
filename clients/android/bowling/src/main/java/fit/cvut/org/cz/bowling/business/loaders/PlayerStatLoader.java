package fit.cvut.org.cz.bowling.business.loaders;

import android.content.Context;
import android.util.Log;

import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.bowling.business.serialization.PlayerStatSerializer;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.serialization.entities.ServerCommunicationItem;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;

public class PlayerStatLoader {

    public static void importPlayerStat(Context context, ServerCommunicationItem playerStat, Player importedPlayer, Participant addedParticipant) {
        Log.d("IMPORT", "PLAYER_STAT: " + playerStat.syncData);
        PlayerStat importedPlayerStat = PlayerStatSerializer.getInstance(context).deserialize(playerStat);
        importedPlayerStat.setPlayerId(importedPlayer.getId());
        importedPlayerStat.setParticipantId(addedParticipant.getId());
        IPlayerStatManager manager = ManagerFactory.getInstance(context).getEntityManager(PlayerStat.class);
        manager.insert(importedPlayerStat);
    }
}
