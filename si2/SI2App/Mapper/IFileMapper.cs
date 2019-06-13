namespace SI2App.Mapper
{
    using SI2App.Model;
    using System.Collections.Generic;
    using System;

    public interface IFileMapper : IMapper<File, Tuple<int, int?>, List<File>>
    {
    }
}
