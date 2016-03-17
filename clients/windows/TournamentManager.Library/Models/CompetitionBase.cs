using SQLite.Net.Attributes;
using System;

namespace TournamentManager.Library.Models
{
    public abstract class CompetitionBase : BaseEntity
    {
        public string Name { get; set; }

        public DateTime From { get; set; }
        public DateTime To { get; set; }

    }

}
