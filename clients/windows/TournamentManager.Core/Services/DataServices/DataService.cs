using SQLite.Net;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TournamentManager.Core.Services.DataServices
{
    public class DataService
    {
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

        private SQLite.Net.SQLiteConnection dbConnection;
        public SQLite.Net.SQLiteConnection GetDBConnection()
        {
            if(dbConnection == null)
            {
                string path = Path.Combine(Windows.Storage.ApplicationData.Current.LocalFolder.Path,
                    "core.sqlite");
                dbConnection =  new SQLiteConnection(new
                    SQLite.Net.Platform.WinRT.SQLitePlatformWinRT(), path);
            }
            return dbConnection;
        }
    }
}
