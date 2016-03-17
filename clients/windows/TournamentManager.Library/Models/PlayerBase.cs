using SQLite.Net.Attributes;

namespace TournamentManager.Library.Models
{
    public abstract class PlayerBase : BaseEntity 
    {

        public string Name { get; set; }

        public string Email { get; set; }

        public string Note { get; set; }

    }

}
