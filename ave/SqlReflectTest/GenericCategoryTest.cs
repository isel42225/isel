using Microsoft.VisualStudio.TestTools.UnitTesting;
using SqlReflect;
using SqlReflectTest.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SqlReflectTest
{
    [TestClass]
    public class GenericCategoryTest
    {

        protected static readonly string NORTHWIND = @"
                    Server=(LocalDB)\MSSQLLocalDB;
                    Integrated Security=true;
                    AttachDbFileName=" +
                        Environment.CurrentDirectory +
                        "\\data\\NORTHWND.MDF";

        private AbstractDataMapper<int, Category> categories;



        public GenericCategoryTest()
        {
            categories = new ReflectDataMapper<int, Category>(typeof(Category), NORTHWIND);
        }

        [TestMethod]
        public void TestGenericCategoryGetAll()
        {
            IEnumerable<Category> res = categories.GetAll();
            int count = 0;
            foreach (object p in res)
            {
                Console.WriteLine(p);
                count++;
            }
            Assert.AreEqual(8, count);
        }

        [TestMethod]
        public void TestGenericCategoryGetById()
        {
            Category c = categories.GetById(3);
            Assert.AreEqual("Confections", c.CategoryName);
            Assert.AreEqual("Desserts, candies, and sweet breads", c.Description);
        }

        [TestMethod]
        public void TestGenericCategoryInsertAndDelete()
        {
            //
            // Create and Insert new Category
            // 
            Category c = new Category()
            {
                CategoryName = "Fish",
                Description = "Live under water!"
            };

            object id = categories.Insert(c);

            //Get the new category object from database

            Category actual = (Category)categories.GetById(id);
            Assert.AreEqual(c.CategoryName, actual.CategoryName);
            Assert.AreEqual(c.Description, actual.Description);
            //
            //// Delete the created category from database
            ////
            categories.Delete(actual);
            object res = categories.GetById(id);
            actual = res != null ? (Category)res : default(Category);
            Assert.IsNull(actual.CategoryName);
            Assert.IsNull(actual.Description);
        }

        [TestMethod]
        public void TestGenericCategoryUpdate()
        {
            Category original = categories.GetById(3);
            Category modified = new Category()
            {
                CategoryID = original.CategoryID,
                CategoryName = "Mushrooms",
                Description = "Agaricus bisporus"
            };
            categories.Update(modified);
            Category actual = categories.GetById(3);
            Assert.AreEqual(modified.CategoryName, actual.CategoryName);
            Assert.AreEqual(modified.Description, actual.Description);
            categories.Update(original);
            actual = categories.GetById(3);
            Assert.AreEqual("Confections", actual.CategoryName);
            Assert.AreEqual("Desserts, candies, and sweet breads", actual.Description);
        }
    }
}
