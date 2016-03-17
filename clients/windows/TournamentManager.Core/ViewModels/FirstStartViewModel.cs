using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Template10.Common;
using Template10.Controls;
using Template10.Mvvm;
using Template10.Services.NavigationService;
using TournamentManager.Core.DataAccessLayer;
using TournamentManager.Core.Models;
using TournamentManager.Core.Services;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;

namespace TournamentManager.Core.ViewModels
{
    public class FirstStartViewModel : ViewModelBase
    {
        public FirstStartViewModel()
        {
            if (Windows.ApplicationModel.DesignMode.DesignModeEnabled)
            {
                modules = new List<TMModule> { new TMModule(), new TMModule() };
            }
            else {
                modules = ModuleService.Instance.GetAllAvailableModules() as List<TMModule>;
                selectedModules = ModuleService.Instance.GetAllEnabledModules() as List<TMModule>;

                if (selectedModules == null)
                    selectedModules = new List<TMModule>();
            }
            
        }

        private List<TMModule> modules;
        public List<TMModule> Modules
        {
            get { return modules; }
        }

        private List<TMModule> selectedModules;


        public void Modules_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            ListView listView = (ListView)sender;

            selectedModules.Clear();
            foreach (object module in listView.SelectedItems)
            {
                selectedModules.Add((TMModule)module);
            }

            Debug.WriteLine(selectedModules.Count);
        }

        public void ModulesSubmit_Click(object sender, Windows.UI.Xaml.RoutedEventArgs e)
        {
            ModuleService.Instance.EnableModules(selectedModules);
            NavigationService.Navigate(typeof(Views.MainPage));
        }

    }
}

