using System.Collections.Generic;

namespace TournamentManager.Library.DataAccessLayer.Generic
{
    public interface IGenericDAO< in T, out TResult> : IGenericDAOIn<T>, IGenericDAOOut<TResult>
    {

    }

    public interface IGenericDAOIn< in T>
    {
        void Add(T entity);
        void Edit(T entity);
        void Delete(long id);
    }

    public interface IGenericDAOOut<out TResult>
    {
        int CountAll();
        TResult GetById(long id);
        IEnumerable<TResult> GetAll();
    }

}
