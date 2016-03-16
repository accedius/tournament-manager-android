using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TournamentManager.Core.Models;
using TournamentManager.Core.Services.DataServices;
using TournamentManager.Library.DataAccessLayer;

namespace TournamentManager.Core.DataAccessLayer
{
    class CompetitionDAO : GenericCompetitionDAO<Competition>
    {
        public CompetitionDAO() : base( DataService.Instance.GetDBConnection()) { }
    }
}
