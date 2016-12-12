package fit.cvut.org.cz.tmlibrary.business.interfaces;

import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by kevin on 4.12.2016.
 */
abstract public class ManagerFactory {
    public SportDBHelper sportDBHelper;
    public ICompetitionManager competitionManager;
    public ITournamentManager tournamentManager;
    public ITeamManager teamManager;
    public ICorePlayerManager corePlayerManager;
    //IStatisticManager statisticsManager;
    public IParticipantManager participantManager;
    public IParticipantStatManager participantStatManager;
    public IPlayerStatManager playerStatManager;
    public IMatchManager matchManager;
}
