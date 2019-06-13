using Microsoft.VisualStudio.TestTools.UnitTesting;
using SqlReflect;
using SqlReflectTest.Model;

namespace SqlReflectTest
{

    [TestClass]
    public class CategoryEmitDataMapperTest : AbstractCategoryDataMapperTest
    {
        public CategoryEmitDataMapperTest() : base(EmitDataMapper.Build(typeof(Category), NORTHWIND, false))
        {

        }

        [TestMethod]
        public void TestCategoryGetAllEmit()
        {
            base.TestCategoryGetAll();
        }

        [TestMethod]
        public void TestCategoryGetByIdEmit()
        {
            base.TestCategoryGetById();
        }
        [TestMethod]
        public void TestCategoryInsertAndDeleteEmit()
        {
            base.TestCategoryInsertAndDelete();
        }

        [TestMethod]
        public void TestCategoryUpdateEmit()
        {
            base.TestCategoryUpdate();
        }


    }
}
