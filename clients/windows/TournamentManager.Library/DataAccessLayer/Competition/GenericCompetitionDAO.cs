using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TournamentManager.Library.DataAccessLayer.Generic;
using TournamentManager.Library.Models;

namespace TournamentManager.Library.DataAccessLayer
{
    public abstract class GenericCompetitionDAO<T> : GenericDAO<T> where T : BaseCompetition
    {
        public GenericCompetitionDAO(SQLite.Net.SQLiteConnection db) : base(db) { }
        
    }
}
