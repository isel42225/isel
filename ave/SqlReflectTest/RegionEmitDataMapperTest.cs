using Microsoft.VisualStudio.TestTools.UnitTesting;
using SqlReflect;
using SqlReflectTest.Model;

namespace SqlReflectTest
{
    [TestClass]
    public class RegionEmitDataMapperTest:AbstractRegionDataMapperTest
    {
            public RegionEmitDataMapperTest() : base(EmitDataMapper.Build(typeof(Region),NORTHWIND,false))
            {
            }

            [TestMethod]
            public void TestRegionGetAllEmit()
            {
                base.TestRegionGetAll();
            }

            [TestMethod]
            public void TestRegionGetByIdEmit()
            {
                base.TestRegionGetById();
            }


            [TestMethod]
            public void TestRegionInsertAndDeleteEmit()
            {
                base.TestRegionInsertAndDelete();
            }

            [TestMethod]
            public void TestRegionUpdateEmit()
            {
                base.TestRegionUpdate();
            }
        }
}

