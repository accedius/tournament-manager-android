using SQLite.Net;
using System.IO;
using Windows.Storage;

namespace TournamentManager.Library.Services
{
    public abstract class DataServiceBase
    {
        protected string FolderPath { get { return Windows.Storage.ApplicationData.Current.LocalFolder.Path; } }

        protected abstract string DatabaseName { get; }

        protected SQLiteConnection dbConnection;

        public SQLiteConnection DbConnection
        {
            get
            {
                if (dbConnection == null)
                {
                    string path = Path.Combine(FolderPath, DatabaseName);
                    dbConnection = new SQLiteConnection(new SQLite.Net.Platform.WinRT.SQLitePlatformWinRT(), path);
                }

                return dbConnection;
            }
        }

    }
}
