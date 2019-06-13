using System;
using System.Data;
using SqlReflect;

namespace SqlReflectTest.DataMappers
{
    public class CustomerDataMapper : DynamicDataMapper
    {

        

        public CustomerDataMapper(Type klass, string connStr, bool withCache) : base(klass, connStr, withCache)
        {
           
        }

        protected override object Load(IDataReader dr)
        {

          
            Customer c = new Customer();

            c.CustomerID = dr.GetValue(0) as string;
            c.CompanyName = dr.GetValue(1) as string;
            c.ContactName = dr.GetValue(2) as string;    //
            c.ContactTitle = dr.GetValue(3) as string;   //
            c.Address = dr.GetValue(4) as string;        //
            c.City = dr.GetValue(5) as string;           //
            c.Region = dr.GetValue(6) as string;         // In case DBNull as operator
            c.PostalCode = dr.GetValue(7) as string;     // puts null in Property
            c.Country = dr.GetValue(8) as string;        //
            c.Phone = dr.GetValue(9) as string;          //
            c.Fax = dr.GetValue(10) as string;           //

            return c;

        }

        protected override string SqlDelete(object target)
        {
            Customer c = (Customer)target;
            return base.deleteStmt + c.CustomerID;
        }

        protected override string SqlInsert(object target)
        {
            Customer c = (Customer) target;
            string aux =  String.Format( "( '{0}' , '{1}', '{2}' , '{3}', '{4}' , '{5}'," +
                 " '{6}' , '{7}', '{8}' , '{9}', '{10}')",
                  c.CustomerID,
                  c.CompanyName,
                  c.ContactName,
                  c.ContactTitle,
                  c.Address,
                  c.City,
                  c.Region,
                  c.PostalCode,
                  c.Country,
                  c.Phone,
                  c.Fax);
            return String.Format(base.insertStmt + aux);
        }

        protected override string SqlUpdate(object target)
        {
            Customer c = (Customer)target;
            string aux = String.Format(" {0} = '{1}', {2} = '{3}', {4} = '{5}'," +
                 " {6} = '{7}', {8} = '{9}', {10} = '{11}',{12} = '{13}', {14} = '{15}', " +
                 "{16} = '{17}', {18} = '{19}',  {20} = '{21}'",
                 "CustomerID", c.CustomerID,
                 "CompanyName", c.CompanyName,
                 "ContactName", c.ContactName,
                 "ContactTitle", c.ContactTitle,
                 "Address", c.Address,
                 "City", c.City,
                 "Region", c.Region,
                 "PostalCode", c.PostalCode,
                 "Country", c.Country,
                 "Phone", c.Phone,
                 "Fax", c.Fax);

            return String.Format(base.updateStmt, aux, "'"+c.CustomerID+"'");
        }
        
    }
}
