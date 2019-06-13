using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using SqlReflectTest.Model;
using SqlReflect;
using System.Collections;

namespace SqlReflectTest
{
    public class AbstractRegionDataMapperTest
    {

        protected static readonly string NORTHWIND = @"
                    Server=(LocalDB)\MSSQLLocalDB;
                    Integrated Security=true;
                    AttachDbFileName=" +
                        Environment.CurrentDirectory +
                        "\\data\\NORTHWND.MDF";

        readonly IDataMapper regions;

        public AbstractRegionDataMapperTest(IDataMapper regions)
        {
            this.regions = regions;
        }

        internal void TestRegionInsertAndDelete()
        {
            //
            // Create and Insert new Region

            Region r = new Region()
            {
                RegionID = 12,
                RegionDescription = "Western                                           ",
            };
            object id = regions.Insert(r);
            //
            // Get the new region object from database
            //
            Region actual = (Region)regions.GetById(12);
            Assert.AreEqual(r.RegionID, actual.RegionID);
            Assert.AreEqual(r.RegionDescription, actual.RegionDescription);

            // Delete the created region from database
            //
            regions.Delete(actual);
            object res = regions.GetById(12);
            actual = res != null ? (Region)res : default(Region);
            Assert.IsNull(actual);
            
        }

        internal void TestRegionUpdate()
        {
            Region original = (Region)regions.GetById(1);
            Region modified = new Region()
            {
                RegionID = original.RegionID,
                RegionDescription = "Western                                           ",
            };

            regions.Update(modified);
            Region actual = (Region)regions.GetById(1);
            Assert.AreEqual(modified.RegionDescription, actual.RegionDescription);
            Assert.AreEqual(modified.RegionID, actual.RegionID);
            regions.Update(original);

            actual = (Region)regions.GetById(1);
            Assert.AreEqual(1, actual.RegionID);
            Assert.AreEqual("Eastern                                           ", actual.RegionDescription);
        }
    

        public void TestRegionGetAll()
        {
            IEnumerable res = regions.GetAll();
            int count = 0;
            foreach (object p in res)
            {
                Console.WriteLine(p);
                count++;
            }
            Assert.AreEqual(4, count);
        }

        public void TestRegionGetById()
        {
            Region r = (Region)regions.GetById(1);
            Assert.AreEqual(1, r.RegionID);
            Assert.AreEqual("Eastern",r.RegionDescription.Trim());
        }

    }
}