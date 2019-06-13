namespace SI2App.Model
{
    using System;
    using System.IO;
    using System.Threading.Tasks;

    public class File
    {

        public int? Id { get; set; }

        public int ArticleId { get; set; }

        public byte[] SubmittedFile { get; set; }

        public DateTime? InsertionDate { get; set; }

        public async Task AddFile(FileStream stream)
        {
            var size = 0;
            using (stream)
            {
                size = (int)stream.Length;
                var bytesRead = 0;
                while (bytesRead < size) 
                    bytesRead += await stream.ReadAsync(this.SubmittedFile, 0, size);
            }
        }
    }
}
