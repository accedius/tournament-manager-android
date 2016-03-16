using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TournamentManager.Library.Helpers
{
    class TitledList<Ttitle, Titem>
    {
        public List<Titem> Items { get; set; }
        public Ttitle Title { get; set; }

    }
}
