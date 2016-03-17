using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using TournamentManager.Library.DataAccessLayer.Competition;
using TournamentManager.Library.Models;

namespace TournamentManager.Library.DataAccessLayer.Generic
{
    public abstract class GenericDAO<T, TResult> :  IGenericDAO<T, TResult> where T : BaseEntity where TResult : BaseEntity
    {
        protected SQLite.Net.SQLiteConnection dbConnection;

        public GenericDAO(SQLite.Net.SQLiteConnection dbConnection)
        {
            this.dbConnection = dbConnection;
            dbConnection.CreateTable<T>();
        }

        public void Add(T entity)
        {
            dbConnection.Insert(entity);
        }

        public void Delete(long id)
        {
            dbConnection.Delete<T>(id);
        }

        public void Edit(T entity)
        {
           if( dbConnection.Update(entity) == 0)
            {
                dbConnection.InsertOrReplace(entity);
                
            }
            dbConnection.Commit();
        }

        public int CountAll()
        {
            return dbConnection.Table<T>().Count();
        }

        public IEnumerable<TResult> GetAll()
        {
            Debug.WriteLine("GetAllFrom - "+typeof(T).FullName);
            return (from t in dbConnection.Table<T>()
                    select t) as IEnumerable<TResult>;
            
        }

        public TResult GetById(long id)
        {
            return dbConnection.Table<T>().FirstOrDefault(t => t.Id == id) as TResult;
        }

    }
}
