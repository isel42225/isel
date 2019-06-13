using System.Text;

namespace SI2App
{
    public class Clauses
    {
        private StringBuilder Clause { get; set; }

        public Clauses()
        {
            this.Clause = new StringBuilder();
        }

        public Clauses Equals<T>(T property, string name) { this.Clause.Append($"{name} = {property} "); return this; }

        public Clauses BiggerThan<T>(T property, string name) { this.Clause.Append($"{name} > {property} "); return this; }

        public Clauses BiggerOrEqualThan<T>(T property, string name) { this.Clause.Append($"{name} >= {property} "); return this; }

        public Clauses LesserThan<T>(T property, string name) { this.Clause.Append($"{name} < {property} "); return this; }

        public Clauses LesserOrEqualThan<T>(T property, string name) { this.Clause.Append($"{name} <= {property} "); return this; }

        public Clauses Like<T>(T property, string name) { this.Clause.Append($"{name} like '%{property}%' "); return this; }

        public Clauses And() { this.Clause.Append("AND "); return this; }

        public Clauses Or() { this.Clause.Append("OR "); return this; }

        public string GetWhereClause() => $"where {this.Clause.ToString()}";
    }
}
