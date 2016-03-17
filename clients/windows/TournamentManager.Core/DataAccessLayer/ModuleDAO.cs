using TournamentManager.Core.Models;
using TournamentManager.Core.Services;
using TournamentManager.Library.DataAccessLayer.Generic;

namespace TournamentManager.Core.DataAccessLayer
{
    public class ModuleDAO : GenericDAO<TMModule, TMModule>
    {
        public ModuleDAO() : base(DataService.Instance.DbConnection)
        {

        }

        public TMModule GetByClassName(string className)
        {
           return dbConnection.Table<TMModule>().Where(t => t.ModuleNamespaceName == className).FirstOrDefault();
        }
    }
}
