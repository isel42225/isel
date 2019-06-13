using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using SqlReflect;
using SqlReflectTest.Model;

namespace SqlReflectTest
{
    [TestClass]
    public class ReflectDataMapperTest
    {
        protected static readonly string NORTHWIND = @"
                    Server=(LocalDB)\MSSQLLocalDB;
                    Integrated Security=true;
                    AttachDbFileName=" +
                        Environment.CurrentDirectory +
                        "\\data\\NORTHWND.MDF";


        [TestMethod]
        public void TestGetAll()
        {
            IDataMapper r =
                 new ReflectDataMapper(typeof(Category), NORTHWIND);
            Console.WriteLine(r.GetAll());
        }
    }
}
