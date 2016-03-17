using System;
using System.Collections.Generic;
using TournamentManager.Library.DataAccessLayer.Generic;
using TournamentManager.Library.Models;

namespace TournamentManager.Library.DataAccessLayer.Competition
{
    public abstract class CompetitionGenericDAO<T, TResult> : GenericDAO<T, TResult>, ICompetitionGenericDAO<T,TResult> where T : CompetitionBase where TResult : CompetitionBase
    {
        public CompetitionGenericDAO(SQLite.Net.SQLiteConnection db) : base(db) { }

    }
}
