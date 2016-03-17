using System;
using TournamentManager.Library.DataAccessLayer.Competition;
using TournamentManager.Library.Models;
using TournamentManager.Modules.Squash.DataAccessLayer;
using TournamentManager.Modules.Squash.Models;

namespace TournamentManager.Modules.Squash
{
    public class ThisModule : ModuleBase
    {
        protected CompetitionDAO competitionDAO = new CompetitionDAO();

        public override ICompetitionGenericDAOOut<CompetitionBase> CompetitionDAO {
            get
            {
                return competitionDAO;
            }
        } 

        public override string Name
        {
            get { return "Squash"; }
        }

        public override void Initialize()
        {
            competitionDAO.Add(new Competition { Name = "FirstTest" });
            competitionDAO.Add(new Competition { Name = "Second" });
        }
    }
}
