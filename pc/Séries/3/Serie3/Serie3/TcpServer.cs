using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace Serie3
{
    class TcpServer
    {
        private static readonly JsonSerializer serializer = new JsonSerializer();
        public async Task Start(int port, CancellationToken ct)
        {
            var ipAddress = Dns.GetHostEntry("localhost").AddressList[0];
            var listener = new TcpListener(ipAddress, port);

            try
            {
                listener.Start();
                Console.WriteLine($"listening on {port}");
                for (; !ct.IsCancellationRequested;)
                {
                    Console.WriteLine("accepting...");
                    var client = await listener.AcceptTcpClientAsync();
                    Console.WriteLine("...client accepted");
                    HandleRequest(client);
                }
            }
            catch (Exception e)
            {
                // await AcceptTcpClientAsync will end up with an exception
                Console.WriteLine($"Exception '{e.Message}' received");
            }
        }

        private void HandleRequest(TcpClient client)
        {
            using (client)
            {
                var stream = client.GetStream();
                var reader = new JsonTextReader(new StreamReader(stream))
                {
                    // To support reading multiple top-level objects
                    SupportMultipleContent = true
                };

                var o = serializer.Deserialize(reader);
                Console.WriteLine(o);
            }
        }
    }
}

