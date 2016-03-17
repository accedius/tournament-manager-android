using SQLite.Net.Attributes;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using TournamentManager.Library.Models;

namespace TournamentManager.Core.Models
{
    public class TMModule : BaseEntity
    {
        public string Name { get; set; }
        public string ModuleNamespaceName { get; set; }

        public bool IsEnabled { get; set; }

        
        protected ModuleBase moduleClassInstance;

        [Ignore]
        public ModuleBase ModuleClassInstance
        {
            get
            {
                if(moduleClassInstance == null)
                {
                    var type = Assembly.Load(new AssemblyName(this.ModuleNamespaceName)).GetTypes().First(t => t.Name == "ThisModule");
                    moduleClassInstance = Activator.CreateInstance(type) as ModuleBase;
                }
                return moduleClassInstance;
            }
            set
            {
                this.ModuleNamespaceName = value.GetType().Namespace;
                this.Name = value.Name;
                this.moduleClassInstance = value;
            }
        }

        public TMModule()
        {
           this.ModuleNamespaceName = "Empty";
        }


    }
}
