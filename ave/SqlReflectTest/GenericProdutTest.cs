using System;
using System.Text;
using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using SqlReflect;
using SqlReflectTest.Model;

namespace SqlReflectTest
{
   
    [TestClass]
    public class GenericProdutTest
    {

        protected static readonly string NORTHWIND = @"
                    Server=(LocalDB)\MSSQLLocalDB;
                    Integrated Security=true;
                    AttachDbFileName=" +
                        Environment.CurrentDirectory +
                        "\\data\\NORTHWND.MDF";

        readonly IDataMapper<int,Product> prods;
        readonly IDataMapper<int, Category> categories;
        readonly IDataMapper<int,Supplier> suppliers;

        public GenericProdutTest()
        {
            prods = new ReflectDataMapper<int, Product>(typeof(Product), NORTHWIND);
            categories = new ReflectDataMapper<int, Category>(typeof(Category), NORTHWIND);
            suppliers = new ReflectDataMapper<int, Supplier>(typeof(Supplier), NORTHWIND);
        }



        [TestMethod]
        public void TestGenericProductGetAll()
        {

            IEnumerable<Product> res = prods.GetAll();
            int count = 0;
            foreach (object p in res)
            {
                Console.WriteLine(p);
                count++;
            }
            Assert.AreEqual(77, count);
        }

        [TestMethod]
        public void TestGenericProductGetById()
        {
            Product p = prods.GetById(10);
            Assert.AreEqual("Ikura", p.ProductName);
            Assert.AreEqual("Seafood", p.Category.CategoryName);
            Assert.AreEqual("Tokyo Traders", p.Supplier.CompanyName);
        }

        [TestMethod]
        public void TestGenericProductInsertAndDelete()
        {
            //
            // Create and insert a new product
            //
            Category c = categories.GetById(4);
            Supplier s = suppliers.GetById(17);
            Product p = new Product()
            {
                Category = c,
                Supplier = s,
                ProductName = "Bacalhau",
                ReorderLevel = 23,
                UnitsInStock = 100,
                UnitsOnOrder = 40
            };
            object id = prods.Insert(p);
            //
            // Get the new product object from database
            //
            Product actual = (Product)prods.GetById(id);
            Assert.AreEqual(p.ProductName, actual.ProductName);
            Assert.AreEqual(p.UnitsInStock, actual.UnitsInStock);
            //
            // Delete the created product from database
            //
            prods.Delete(actual);
            actual = (Product)prods.GetById(id);
            Assert.IsNull(actual);
        }
    }
}
