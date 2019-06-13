using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Configuration;

namespace EF_Dal
{
    class App
    {
        public static void Main(string[] args)
        {
            var connectionString = ConfigurationManager.ConnectionStrings["cs"].ConnectionString;
            try
            {
                using (var context = new cs())
                {
                    var confs = context.Conferences.AsEnumerable().Count();
                    Console.WriteLine($"Confs = {confs}");
                    Console.ReadKey();
                }
            }catch(Exception e)
            {
                Console.WriteLine($"Error : {e.Message}");
                Console.ReadKey();
            }
        }
    }
}
