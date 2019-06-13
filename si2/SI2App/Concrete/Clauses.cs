namespace SI2App.Concrete
{
    using System.Text;

    public class Clauses
    {
        private StringBuilder Where { get; set; }

        public Clauses()
        {
            this.Where = new StringBuilder();
        }

        public Clauses And() { this.Where.Append("AND "); return this; }

        public Clauses Or() { this.Where.Append("OR "); return this; }

        public Clauses Equals<T>(T property, string name) { this.Where.Append($"{name} = {property} "); return this; }

        public Clauses Different<T>(T property, string name) { this.Where.Append($"{name} != {property} "); return this; }

        public Clauses BiggerThan<T>(T property, string name) { this.Where.Append($"{name} > {property} "); return this; }

        public Clauses BiggerOrEqualsThan<T>(T property, string name) { this.Where.Append($"{name} >= {property} "); return this; }

        public Clauses LowerThan<T>(T property, string name) { this.Where.Append($"{name} < {property} "); return this; }

        public Clauses LowerOrEqualsThan<T>(T property, string name) { this.Where.Append($"{name} <= {property} "); return this; }

        public string GetWhereClause() => $"where {this.Where.ToString()}";

    }
}
