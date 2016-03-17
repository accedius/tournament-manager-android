using TournamentManager.Library.DataAccessLayer.Generic;
using TournamentManager.Library.Models;

namespace TournamentManager.Library.DataAccessLayer.Competition
{
    public interface ICompetitionGenericDAO <in T, out TResult> : ICompetitionGenericDAOIn<T>, ICompetitionGenericDAOOut<TResult> where TResult : CompetitionBase where T : CompetitionBase
    {
    }

    public interface ICompetitionGenericDAOIn<in T> : IGenericDAOIn<T> where T : CompetitionBase
    {

    }

    public interface ICompetitionGenericDAOOut<out TResult> : IGenericDAOOut<TResult> where TResult : CompetitionBase
    {

    }
}
