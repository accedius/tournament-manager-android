using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TournamentManager.Core.Helpers;
using TournamentManager.Library.Models;
using TournamentManager.Library.BusinessLogic;

namespace TournamentManager.Core.BusinessLogic
{
    class CompetitionManager : ICompetitionManager
    {

        public List<TitledList<BaseModule,BaseCompetition>> GetGroupedCompetitions()
        {
            return new List<TitledList<BaseModule, BaseCompetition>>();
        }
    }
}
