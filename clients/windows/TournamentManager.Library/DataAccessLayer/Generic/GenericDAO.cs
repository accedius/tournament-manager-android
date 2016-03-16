using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TournamentManager.Library.DataAccessLayer.Generic
{
    public abstract class GenericDAO<T> : IGenericDAO<T> where T : class
    {
        protected SQLite.Net.SQLiteConnection _db = default(SQLite.Net.SQLiteConnection);

        public GenericDAO(SQLite.Net.SQLiteConnection db)
        {
            this._db = db;
        }

        public void Add(T entity)
        {
            throw new NotImplementedException();
        }

        public void Delete(long id)
        {
            throw new NotImplementedException();
        }

        public void Edit(T entity)
        {
            throw new NotImplementedException();
        }

        public IEnumerable<T> GetAll()
        {
            throw new NotImplementedException();
        }

        public T GetById()
        {
            throw new NotImplementedException();
        }


    }
}
