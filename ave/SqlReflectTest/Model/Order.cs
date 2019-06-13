using SqlReflect.Attributes;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SqlReflectTest.Model
{
    [Table("Order")]
    public struct Order
    {
        [PK]
        public int OrderID { get; set; }
        public string CustomerID { get; set; }
        public int Freight { get; set; }
        public string ShipName { get; set; }


    }
}
