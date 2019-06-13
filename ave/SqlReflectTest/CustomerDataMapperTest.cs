using SqlReflect;
using SqlReflectTest.DataMappers;
using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Collections;

namespace SqlReflectTest
{
    [TestClass]
    public class CustomerDataMapperTest 
    {

        readonly DynamicDataMapper customers;

       
        protected static readonly string NORTHWIND = @"
                    Server=(LocalDB)\MSSQLLocalDB;
                    Integrated Security=true;
                    AttachDbFileName=" +
                        Environment.CurrentDirectory +
                        "\\data\\NORTHWND.MDF";

        

        public CustomerDataMapperTest()
        {
            //customers = new CustomerDataMapper(typeof (Customer), NORTHWIND, false);
            customers = EmitDataMapper.Build(typeof(Customer), NORTHWIND, false);
        }
        

        [TestMethod]
        public  void TestCustomerGetAll()
        {
           
            //int SQLCount = customers.Count();  // impossible to do
             IEnumerable res = customers.GetAll();
            int count = 0;
            foreach (object p in res)
            {
                Console.WriteLine(p);
                count++;
            }
            Assert.AreEqual(91, count);
        }

        [TestMethod]
        public  void TestCustomersGetById()
        {
            Customer cust = (Customer)customers.GetById("ALFKI"); 
            Assert.AreEqual("ALFKI", cust.CustomerID);
            Assert.AreEqual("Alfreds Futterkiste", cust.CompanyName);

        }

        [TestMethod]
        public  void TestCustomersInsertAndDelete()

        {
            Customer insert = new Customer()
            {
                CustomerID = "AAA",   
                CompanyName = "Isel",

            };

            object id = customers.Insert(insert);
            Customer res = (Customer)customers.GetById(id);

            //Assert.AreEqual(id,  res.CustomerID);
            Assert.AreEqual("AAA  ", res.CustomerID);

            customers.Delete(insert);


        }

        [TestMethod]
        public  void TestCustomersUpdate()
        {


            Customer original = (Customer)customers.GetById("ALFKI");
            Customer modified = new Customer()
            {
                CustomerID = original.CustomerID,
                CompanyName = "Isel",
            };

            customers.Update(modified);
            Customer actual = (Customer)customers.GetById("ALFKI");
            Assert.AreEqual(modified.CompanyName, actual.CompanyName);
            Assert.AreEqual(modified.CustomerID, actual.CustomerID);
            customers.Update(original);

            actual = (Customer)customers.GetById("ALFKI");
            Assert.AreEqual("Alfreds Futterkiste", actual.CompanyName);
        }


        }




}

