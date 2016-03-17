using System.Collections.Generic;
using TournamentManager.Library.DataAccessLayer.Competition;

namespace TournamentManager.Library.Models
{
    public abstract class ModuleBase
    {
        public abstract string Name { get; }

        public abstract ICompetitionGenericDAOOut<CompetitionBase> CompetitionDAO
        {
            get;
        }

        public abstract void Initialize();

        public IEnumerable<CompetitionBase> GetAllCompetitions()
        {
            return CompetitionDAO.GetAll() as IEnumerable<CompetitionBase>;
        }

    }
}
