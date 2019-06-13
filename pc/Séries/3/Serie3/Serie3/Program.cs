using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Serie3
{
    class Program
    {
        static async Task Main(string[] args)
        {
            var server = new TcpServer();
            CancellationTokenSource cts = new CancellationTokenSource();
            await server.Start(8081, cts.Token);
        }
    }
}
