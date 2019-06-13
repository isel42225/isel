using SqlReflect;
using SqlReflectTest;
using SqlReflectTest.DataMappers;
using SqlReflectTest.Model;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics;

namespace App
{
    class BenchMarkingMappers
    {
        static readonly string connStr = @"
                    Server=(LocalDB)\MSSQLLocalDB;
                    Integrated Security=true;
                    AttachDbFileName=" +
                        Environment.CurrentDirectory +
                        "\\data\\NORTHWND.MDF";

        static void Main(string[] args)
        {
            //Action emit = new Action(MeasureEmit);
            //Action reflect = new Action(MeasureReflect);
            //Action hardCoded = new Action(MeasureHardCoded);

            ////NBench.Bench(emit, "Emit Mapper");
            //NBench.Bench(reflect, "Reflect Mapper");
            ////NBench.Bench(hardCoded, "HardCoded Mapper");
          
            ReflectDataMapper<int, Employee> dm = new ReflectDataMapper<int, Employee>(typeof(Employee), connStr);

            IEnumerable<Employee> l2 = dm.GetAll();
            
        }


        private static void MeasureEmit()
        {
            DynamicDataMapper customer = EmitDataMapper.Build(typeof(Customer), connStr, false);
            customer.GetAll();
            customer.GetById("ALFKI");
           
        }

        private static void MeasureReflect()
        {
            ReflectDataMapper<string, Customer> customer = new ReflectDataMapper<string,Customer>(typeof(Customer), connStr, false);
            customer.GetAll();
            customer.GetById("ALFKI");

        }

        private static void MeasureHardCoded()
        {
            CustomerDataMapper customer = new CustomerDataMapper(typeof(Customer), connStr, true);
            customer.GetAll();
            customer.GetById("ALFKI");
        }

        //private static void CompareMappers(Type klass)
        //{
        //    Console.WriteLine("############## Reflect WITH Cache");
        //    IDataMapper emps = new ReflectDataMapper(klass, connStr);
        //    for (int i = 0; i < 5; i++)
        //    {
        //        GetAllItens(emps);
        //    }

        //    Console.WriteLine("############## Reflect NO Cache");
        //    emps = new ReflectDataMapper(klass, connStr, false);
        //    for (int i = 0; i < 5; i++)
        //    {
        //        GetAllItens(emps);
        //    }
        //}

        //private static void GetAllItens(IDataMapper data)
        //{
        //    Stopwatch stopwatch = new Stopwatch();
        //    stopwatch.Start();
        //    IEnumerable res = data.GetAll();
        //    stopwatch.Stop();
        //    Console.WriteLine("Time elapsed (us): {0}", stopwatch.Elapsed.TotalMilliseconds * 1000);
        //}
    }
}

