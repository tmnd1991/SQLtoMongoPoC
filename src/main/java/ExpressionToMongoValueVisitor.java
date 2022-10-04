import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;
import org.bson.*;

public class ExpressionToMongoValueVisitor implements ExpressionVisitor {

    private BsonValue value;

    public BsonValue getValue() {
        if (value == null) {
            throw new RuntimeException("getting null, too early");
        } else {
            return value;
        }
    }

    @Override
    public void visit(NullValue nullValue) {
        value = BsonNull.VALUE;
    }

    @Override
    public void visit(DoubleValue doubleValue) {
        value = new BsonDouble(doubleValue.getValue());
    }

    @Override
    public void visit(LongValue longValue) {
        value = new BsonInt64(longValue.getValue());
    }

    @Override
    public void visit(DateValue dateValue) {
        value = new BsonDateTime(dateValue.getValue().getTime());
    }

    @Override
    public void visit(TimeValue timeValue) {
        value = new BsonDateTime(timeValue.getValue().getTime());
    }

    @Override
    public void visit(TimestampValue timestampValue) {
        value = new BsonDateTime(timestampValue.getValue().getTime());
    }

    @Override
    public void visit(Parenthesis parenthesis) {
        // TODO
    }

    @Override
    public void visit(StringValue stringValue) {
        value = new BsonString(stringValue.getNotExcapedValue());
    }

    @Override
    public void visit(OrExpression orExpression) {
        throw new UnsupportedExpression("Unsupported expression " + orExpression);
    }

    @Override
    public void visit(Between between) {
        throw new UnsupportedExpression("Unsupported expression " + between);
    }

    @Override
    public void visit(AndExpression andExpression) {
        throw new UnsupportedExpression("Unsupported expression " + andExpression);
    }

    @Override
    public void visit(EqualsTo equalsTo) {
        throw new UnsupportedExpression("Unsupported expression " + equalsTo);
    }

    @Override
    public void visit(GreaterThan greaterThan) {
        throw new UnsupportedExpression("Unsupported expression " + greaterThan);
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
        throw new UnsupportedExpression("Unsupported expression " + greaterThanEquals);
    }

    @Override
    public void visit(InExpression inExpression) {
        throw new UnsupportedExpression("Unsupported expression " + inExpression);
    }

    @Override
    public void visit(IsNullExpression isNullExpression) {
        throw new UnsupportedExpression("Unsupported expression " + isNullExpression);
    }

    @Override
    public void visit(IsBooleanExpression isBooleanExpression) {
        throw new UnsupportedExpression("Unsupported expression " + isBooleanExpression);
    }

    @Override
    public void visit(LikeExpression likeExpression) {
        throw new UnsupportedExpression("Unsupported expression " + likeExpression);
    }

    @Override
    public void visit(MinorThan minorThan) {
        throw new UnsupportedExpression("Unsupported expression " + minorThan);
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
        throw new UnsupportedExpression("Unsupported expression " + minorThanEquals);
    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
        throw new UnsupportedExpression("Unsupported expression " + notEqualsTo);
    }

    @Override
    public void visit(BitwiseRightShift aThis) {
        throw new UnsupportedExpression("Unsupported expression " + aThis);
    }

    @Override
    public void visit(BitwiseLeftShift aThis) {
        throw new UnsupportedExpression("Unsupported expression " + aThis);
    }

    @Override
    public void visit(Function function) {
        throw new UnsupportedExpression("Unsupported expression " + function);
    }

    @Override
    public void visit(SignedExpression signedExpression) {
        throw new UnsupportedExpression("Unsupported expression " + signedExpression);
    }

    @Override
    public void visit(JdbcParameter jdbcParameter) {
        throw new UnsupportedExpression("Unsupported expression " + jdbcParameter);
    }

    @Override
    public void visit(JdbcNamedParameter jdbcNamedParameter) {
        throw new UnsupportedExpression("Unsupported expression " + jdbcNamedParameter);
    }

    @Override
    public void visit(HexValue hexValue) {
        throw new UnsupportedExpression("Unsupported expression " + hexValue);
    }

    @Override
    public void visit(Addition addition) {
        throw new UnsupportedExpression("Unsupported expression " + addition);
    }

    @Override
    public void visit(Division division) {
        throw new UnsupportedExpression("Unsupported expression " + division);
    }

    @Override
    public void visit(IntegerDivision division) {
        throw new UnsupportedExpression("Unsupported expression " + division);
    }

    @Override
    public void visit(Multiplication multiplication) {
        throw new UnsupportedExpression("Unsupported expression " + multiplication);
    }

    @Override
    public void visit(Subtraction subtraction) {
        throw new UnsupportedExpression("Unsupported expression " + subtraction);
    }

    @Override
    public void visit(FullTextSearch fullTextSearch) {
        throw new UnsupportedExpression("Unsupported expression " + fullTextSearch);
    }

    @Override
    public void visit(Column tableColumn) {
        throw new UnsupportedExpression("Unsupported expression " + tableColumn);
    }

    @Override
    public void visit(SubSelect subSelect) {
        throw new UnsupportedExpression("Unsupported expression " + subSelect);
    }

    @Override
    public void visit(CaseExpression caseExpression) {
        throw new UnsupportedExpression("Unsupported expression " + caseExpression);
    }

    @Override
    public void visit(WhenClause whenClause) {
        throw new UnsupportedExpression("Unsupported expression " + whenClause);
    }

    @Override
    public void visit(ExistsExpression existsExpression) {
        throw new UnsupportedExpression("Unsupported expression " + existsExpression);
    }

    @Override
    public void visit(AllComparisonExpression allComparisonExpression) {
        throw new UnsupportedExpression("Unsupported expression " + allComparisonExpression);
    }

    @Override
    public void visit(AnyComparisonExpression anyComparisonExpression) {
        throw new UnsupportedExpression("Unsupported expression " + anyComparisonExpression);
    }

    @Override
    public void visit(Concat concat) {
        throw new UnsupportedExpression("Unsupported expression " + concat);
    }

    @Override
    public void visit(Matches matches) {
        throw new UnsupportedExpression("Unsupported expression " + matches);
    }

    @Override
    public void visit(BitwiseAnd bitwiseAnd) {
        throw new UnsupportedExpression("Unsupported expression " + bitwiseAnd);
    }

    @Override
    public void visit(BitwiseOr bitwiseOr) {
        throw new UnsupportedExpression("Unsupported expression " + bitwiseOr);
    }

    @Override
    public void visit(BitwiseXor bitwiseXor) {
        throw new UnsupportedExpression("Unsupported expression " + bitwiseXor);
    }

    @Override
    public void visit(CastExpression cast) {
        throw new UnsupportedExpression("Unsupported expression " + cast);
    }

    @Override
    public void visit(Modulo modulo) {
        throw new UnsupportedExpression("Unsupported expression " + modulo);
    }

    @Override
    public void visit(AnalyticExpression aexpr) {
        throw new UnsupportedExpression("Unsupported expression " + aexpr);
    }

    @Override
    public void visit(ExtractExpression eexpr) {
        throw new UnsupportedExpression("Unsupported expression " + eexpr);
    }

    @Override
    public void visit(IntervalExpression iexpr) {
        throw new UnsupportedExpression("Unsupported expression " + iexpr);
    }

    @Override
    public void visit(OracleHierarchicalExpression oexpr) {
        throw new UnsupportedExpression("Unsupported expression " + oexpr);
    }

    @Override
    public void visit(RegExpMatchOperator rexpr) {
        throw new UnsupportedExpression("Unsupported expression " + rexpr);
    }

    @Override
    public void visit(JsonExpression jsonExpr) {
        throw new UnsupportedExpression("Unsupported expression " + jsonExpr);

    }

    @Override
    public void visit(JsonOperator jsonExpr) {
        throw new UnsupportedExpression("Unsupported expression " + jsonExpr);
    }

    @Override
    public void visit(RegExpMySQLOperator regExpMySQLOperator) {
        throw new UnsupportedExpression("Unsupported expression " + regExpMySQLOperator);
    }

    @Override
    public void visit(UserVariable var) {
        throw new UnsupportedExpression("Unsupported expression " + var);
    }

    @Override
    public void visit(NumericBind bind) {
        throw new UnsupportedExpression("Unsupported expression " + bind);
    }

    @Override
    public void visit(KeepExpression aexpr) {
        throw new UnsupportedExpression("Unsupported expression " + aexpr);
    }

    @Override
    public void visit(MySQLGroupConcat groupConcat) {
        throw new UnsupportedExpression("Unsupported expression " + groupConcat);
    }

    @Override
    public void visit(ValueListExpression valueList) {
        throw new UnsupportedExpression("Unsupported expression " + valueList);
    }

    @Override
    public void visit(RowConstructor rowConstructor) {
        throw new UnsupportedExpression("Unsupported expression " + rowConstructor);
    }

    @Override
    public void visit(OracleHint hint) {
        throw new UnsupportedExpression("Unsupported expression " + hint);
    }

    @Override
    public void visit(TimeKeyExpression timeKeyExpression) {
        throw new UnsupportedExpression("Unsupported expression " + timeKeyExpression);
    }

    @Override
    public void visit(DateTimeLiteralExpression literal) {
        throw new UnsupportedExpression("Unsupported expression " + literal);
    }

    @Override
    public void visit(NotExpression aThis) {
        throw new UnsupportedExpression("Unsupported expression " + aThis);
    }

    @Override
    public void visit(NextValExpression aThis) {
        throw new UnsupportedExpression("Unsupported expression " + aThis);
    }

    @Override
    public void visit(CollateExpression aThis) {
        throw new UnsupportedExpression("Unsupported expression " + aThis);
    }

    @Override
    public void visit(SimilarToExpression aThis) {
        throw new UnsupportedExpression("Unsupported expression " + aThis);
    }

    @Override
    public void visit(ArrayExpression aThis) {
        throw new UnsupportedExpression("Unsupported expression " + aThis);
    }
}
