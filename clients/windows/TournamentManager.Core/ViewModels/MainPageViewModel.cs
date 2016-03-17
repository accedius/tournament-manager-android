using Template10.Mvvm;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Template10.Services.NavigationService;
using Windows.UI.Xaml.Navigation;
using System;
using System.Reflection;
using Windows.UI.Xaml.Data;
using TournamentManager.Core.Models;
using TournamentManager.Library.Helpers;
using TournamentManager.Library.Models;
using TournamentManager.Core.Services;
using TournamentManager.Core.BusinessLogic;

namespace TournamentManager.Core.ViewModels
{
    public class MainPageViewModel : ViewModelBase
    {
        public MainPageViewModel()
        {
            CompetitionsSource.Source = groupedCompetitions = new CompetitionManager().GetAllCompetitions() as List<TitledList<TMModule, CompetitionBase>>;
            CompetitionsSource.IsSourceGrouped = true;
            CompetitionsSource.ItemsPath = new Windows.UI.Xaml.PropertyPath("Items");

            if (Windows.ApplicationModel.DesignMode.DesignModeEnabled)
            {
                Value = "Designtime value";
            }
        }

        string _Value = "Gas";
        public string Value { get { return _Value; } set { Set(ref _Value, value); } }

        private CollectionViewSource competitionsSource = new CollectionViewSource();
        private List<TitledList<TMModule, CompetitionBase>> groupedCompetitions;

        public CollectionViewSource CompetitionsSource
        {
            get
            {
                return competitionsSource;
            }
            set
            {
                competitionsSource = value;
            }
        }



        public override async Task OnNavigatedToAsync(object parameter, NavigationMode mode, IDictionary<string, object> suspensionState)
        {
           
            if (suspensionState.Any())
            {
                Value = suspensionState[nameof(Value)]?.ToString();
            }
            await Task.CompletedTask;
        }

        public override async Task OnNavigatedFromAsync(IDictionary<string, object> suspensionState, bool suspending)
        {
            if (suspending)
            {
                suspensionState[nameof(Value)] = Value;
            }
            await Task.CompletedTask;
        }

        public override async Task OnNavigatingFromAsync(NavigatingEventArgs args)
        {
            args.Cancel = false;
            await Task.CompletedTask;
        }

        public void GotoDetailsPage() =>
            NavigationService.Navigate(typeof(Views.DetailPage), Value);

        public void GotoSettings() =>
            NavigationService.Navigate(typeof(Views.SettingsPage), 0);

        public void GotoPrivacy() =>
            NavigationService.Navigate(typeof(Views.SettingsPage), 1);

        public void GotoAbout() =>
            NavigationService.Navigate(typeof(Views.SettingsPage), 2);

    }
}

