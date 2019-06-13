using System;
using System.Collections.Generic;
using SqlReflect.Attributes;


namespace SqlReflectTest.Model
{
    [Table("Region")]
    public class Region
    {
        [PK(false)]
        public int RegionID { get; set; }

        public string RegionDescription { get; set; }
       

        public override string ToString()
        {
            return RegionDescription;
        }

    }

    
}
