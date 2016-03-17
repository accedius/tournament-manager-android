using System.Collections.Generic;

namespace TournamentManager.Library.Helpers
{
    public class TitledList<TTitle, TItem>
    {
        public TitledList()
        {
        }

        public TitledList(TTitle title, IList<TItem> items) : this()
        {
            Title = title;
            Items = items as List<TItem>;
        }

        public List<TItem> Items { get; set; }
        public TTitle Title { get; set; }
    }
}
