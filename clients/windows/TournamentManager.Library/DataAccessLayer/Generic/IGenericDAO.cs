using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TournamentManager.Library.DataAccessLayer.Generic
{
    public interface IGenericDAO<T> where T : class
    {
        
        void Add(T entity);
        void Edit(T entity);
        void Delete(long id);
        T GetById();
        IEnumerable<T> GetAll();

    }
}
