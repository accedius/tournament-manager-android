using System.Collections.Generic;
using TournamentManager.Library.DataAccessLayer.Competition;
using TournamentManager.Library.Models;
using TournamentManager.Modules.Squash.Models;
using TournamentManager.Modules.Squash.Services;

namespace TournamentManager.Modules.Squash.DataAccessLayer
{
    public class CompetitionDAO : CompetitionGenericDAO<Competition, Competition>
    {
        public CompetitionDAO() : base(DataService.Instance.DbConnection) { }

    }
}
