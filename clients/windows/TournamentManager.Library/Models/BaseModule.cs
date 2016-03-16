using SQLite.Net.Attributes;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TournamentManager.Library.DataAccessLayer;

namespace TournamentManager.Library.Models
{
    public abstract class BaseModule
    {
        protected string name;
        public string Name { get { return name; } }

        protected GenericCompetitionDAO<BaseCompetition> competitionDAO;

        
        public BaseModule()
        {
            Initialize();
        }


        public abstract void Initialize();

        public IEnumerable<BaseCompetition> GetCompetitions() {
            return competitionDAO.GetAll();
        }
       
    }
}
