using TournamentManager.Library.Services;

namespace TournamentManager.Core.Services
{
    public class DataService : DataServiceBase
    {
        #region Singleton

        private static DataService instance;

        public static DataService Instance
        {
            get
            {
                if (instance == null)
                {
                    instance = new DataService();
                }

                return instance;
            }
        }

        #endregion

        protected override string DatabaseName { get { return "core.sqlite"; } }

    }
}
