using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TournamentManager.Core.Models;
using TournamentManager.Core.Services;
using TournamentManager.Library.Helpers;
using TournamentManager.Library.Models;

namespace TournamentManager.Core.BusinessLogic
{
    class CompetitionManager
    {
        public IList< TitledList<TMModule,CompetitionBase>> GetAllCompetitions()
        {
            var list = new List<TitledList<TMModule, CompetitionBase>>();
           var modules = ModuleService.Instance.GetAllEnabledModules();

            foreach(var module in modules)
            {
                var competitions = module.ModuleClassInstance.GetAllCompetitions().ToList() as IList<CompetitionBase>;

                list.Add(new TitledList<TMModule, CompetitionBase>(module, competitions));
            }

            return list as IList<TitledList<TMModule, CompetitionBase>>;
        }
    }
}
