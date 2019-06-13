namespace SI2App.Concrete.Repositories
{
    using SI2App.Concrete.Mappers;
    using SI2App.Dal;
    using SI2App.Model;
    using System;
    using System.Collections.Generic;

    public class FileRepository : IFileRepository
    {
        private IContext Context { get; set; }
        private FileMapper Mapper { get; set; }

        public FileRepository(IContext context)
        {
            this.Context = context;
            this.Mapper = new FileMapper(context);
        }

        public IEnumerable<File> Find(Clauses clauses) => this.Mapper.ReadWhere(clauses);

        public IEnumerable<File> FindAll() => this.Mapper.ReadAll();

        public File Delete(File entity) => this.Mapper.Delete(entity);

        public File Update(File entity) => throw new InvalidOperationException("Can't Update File");

        public File Create(File entity) => this.Mapper.Create(entity);
    }
}
