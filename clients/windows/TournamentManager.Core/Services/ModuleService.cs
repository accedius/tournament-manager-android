using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using TournamentManager.Core.DataAccessLayer;
using TournamentManager.Library.Models;
using TournamentManager.Core.Models;
using Windows.Storage;
using System.Diagnostics;

namespace TournamentManager.Core.Services
{
    public class ModuleService
    {

        #region Singleton
        protected static ModuleService instance;
        public static ModuleService Instance {
            get
            {
                if (instance == null)
                    instance = new ModuleService();

                return instance;
            }
        }
        #endregion

        protected string fileName = "Modules.txt";
        protected ModuleDAO moduleDAO;

        protected ModuleService()
        {
            this.moduleDAO = new ModuleDAO();
        }

        protected bool isFirstStart = false;
        public bool IsFirstStart
        {
            get
            {
                return isFirstStart;
            }
        }

        protected List<TMModule> modules = new List<TMModule>();

        public void Initialize()
        {
            LoadModules();

            InitializeAllEnabledModules();
        }

        protected async void LoadModules()
        {
            var file = await Windows.ApplicationModel.Package.Current.InstalledLocation.GetFileAsync(fileName);

            var lines = await FileIO.ReadLinesAsync(file);

            // If there is no enabled module -> first time app is running
            if (lines.Count > moduleDAO.CountAll() || moduleDAO.GetAll().Where(t => t.IsEnabled).Count() == 0)
            {
                this.isFirstStart = true;

                Debug.WriteLine("FirstStart-ModuleService - count:" + moduleDAO.CountAll());

                // for all modules
                foreach (var line in lines)
                {
                    // Load module assembly and locate BaseModule class
                    var type = Assembly.Load(new AssemblyName(Path.GetFileNameWithoutExtension(line))).GetTypes().First(t=>t.Name=="ThisModule");
                  
                    // Create ModuleBase class instance
                    ModuleBase module = Activator.CreateInstance(type) as ModuleBase;
     
                    // Create local entity for module and fill with ModuleBase instance
                    var tmModule = new TMModule();
                    tmModule.ModuleClassInstance = module;

                    //Do not add module to database if already exists
                    var dbModule = moduleDAO.GetByClassName(tmModule.ModuleNamespaceName);

                    if ( dbModule == null)
                    {
                        moduleDAO.Add(tmModule);
                    }
                    else
                    {
                        tmModule = dbModule;
                    }

                    // Add to local list
                    modules.Add(tmModule);
                }

            }
            else
            {
                Debug.WriteLine("NotFirstStart-ModuleService");

                modules = moduleDAO.GetAll().ToList() as List<TMModule>;
            }

        }

        public IEnumerable<TMModule> GetAllAvailableModules()
        {
            return modules;
        }

        public IEnumerable<TMModule> GetAllEnabledModules()
        {
            return modules.Where(t => t.IsEnabled);
        }

        /// <summary>
        /// Run initialization of all enabled modules
        /// </summary>
        protected void InitializeAllEnabledModules()
        {
            foreach (var module in GetAllEnabledModules())
            {
                module.ModuleClassInstance.Initialize();
            }
        }

        /// <summary>
        /// Run initialization of all available modules
        /// </summary>
        protected void InitializeAllAvailableModules()
        {
            foreach (var module in GetAllAvailableModules())
            {
                module.ModuleClassInstance.Initialize();
            }
        }
        

        public void EnableModules(IEnumerable<TMModule> modules)
        {
            foreach(var module in modules)
            {
                EnableModule(module);
            }
        }

        public void EnableModule(TMModule module)
        {
            module.IsEnabled = true;
            moduleDAO.Edit(module);
        }

        public void DisableModules(IEnumerable<TMModule> modules)
        {
            foreach (var module in modules)
            {
                DisableModule(module);
            }
        }

        public void DisableModule(TMModule module)
        {
            module.IsEnabled = false;
            moduleDAO.Edit(module);
        }

    }
}
