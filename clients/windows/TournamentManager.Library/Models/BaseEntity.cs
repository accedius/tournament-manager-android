using SQLite.Net.Attributes;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TournamentManager.Library.Models
{
    public abstract class BaseEntity
    {
        [PrimaryKey, AutoIncrement]
        public long Id { get; set; }
    }
}
