import com.mongodb.client.model.Filters;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;
import org.bson.BsonBoolean;
import org.bson.BsonInt64;
import org.bson.BsonNull;
import org.bson.conversions.Bson;

public class ExpressionVisitorToMongoFilter implements ExpressionVisitor {

    private Bson filter = null;

    public Bson getFilter() {
        if (filter == null) {
            throw new RuntimeException("getting null, too early");
        } else {
            return filter;
        }
    }

    @Override
    public void visit(OrExpression orExpression) {
        ExpressionVisitorToMongoFilter v1 = new ExpressionVisitorToMongoFilter();
        orExpression.getLeftExpression().accept(v1);
        ExpressionVisitorToMongoFilter v2 = new ExpressionVisitorToMongoFilter();
        orExpression.getRightExpression().accept(v2);
        filter = Filters.or(v1.filter, v2.filter);
    }

    @Override
    public void visit(Between between) {
        throw new UnsupportedExpression("Unsupported expression " + between);
    }

    @Override
    public void visit(AndExpression andExpression) {
        ExpressionVisitorToMongoFilter v1 = new ExpressionVisitorToMongoFilter();
        andExpression.getLeftExpression().accept(v1);
        ExpressionVisitorToMongoFilter v2 = new ExpressionVisitorToMongoFilter();
        andExpression.getRightExpression().accept(v2);
        filter = Filters.and(v1.filter, v2.filter);
    }


    @Override
    public void visit(EqualsTo equalsTo) {
        ExpressionToMongoValueVisitor v = new ExpressionToMongoValueVisitor();
        ColumnAndExpression ce = ColumnAndExpression.extractColumnAndExpression(equalsTo);
        ce.expression.accept(v);
        filter = Filters.eq(ce.column.getColumnName(), v.getValue());
    }

    @Override
    public void visit(GreaterThan greaterThan) {
        ExpressionToMongoValueVisitor v = new ExpressionToMongoValueVisitor();
        ColumnAndExpression ce = ColumnAndExpression.extractColumnAndExpression(greaterThan);
        ce.expression.accept(v);
        filter = Filters.gt(ce.column.getColumnName(), v.getValue());
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
        ExpressionToMongoValueVisitor v = new ExpressionToMongoValueVisitor();
        ColumnAndExpression ce = ColumnAndExpression.extractColumnAndExpression(greaterThanEquals);
        ce.expression.accept(v);
        filter = Filters.gte(ce.column.getColumnName(), v.getValue());
    }

    private Column extractColumn(InExpression expression) {
        if (expression.getLeftExpression() instanceof Column) {
            return (Column) expression.getLeftExpression();
        } else if (expression.getRightExpression() instanceof Column) {
            return (Column) expression.getRightExpression();
        } else {
            throw new UnsupportedExpression("expression " + expression + " did not contain any column");
        }
    }

    private ItemsList extractItemList(InExpression expression) {
        if (expression.getLeftItemsList() != null) {
            return expression.getLeftItemsList();
        } else if (expression.getRightItemsList() != null) {
            return expression.getRightItemsList();
        } else {
            throw new UnsupportedExpression("both left and right itemList of " + expression + " were null");
        }
    }
    @Override
    public void visit(InExpression inExpression) {
        Column c = extractColumn(inExpression);
        ItemsList itemList = extractItemList(inExpression);
        ExpressionListToMongoValuesVisitor v = new ExpressionListToMongoValuesVisitor();
        itemList.accept(v);
        filter = Filters.in(c.getColumnName(),v.getValues());
    }


    @Override
    public void visit(IsNullExpression isNullExpression) {
        if (isNullExpression.getLeftExpression() instanceof Column) {
            final String columnName = ((Column) isNullExpression.getLeftExpression()).getColumnName();
            filter = Filters.or(
                    Filters.exists(columnName),
                    Filters.eq(columnName, BsonNull.VALUE)
            );
        } else {
            throw new UnsupportedExpression("Left expression of isNull should be a column, found " + isNullExpression.getLeftExpression());
        }
    }

    @Override
    public void visit(IsBooleanExpression isBooleanExpression) {
        if (isBooleanExpression.getLeftExpression() instanceof Column) {
            Column column = (Column) isBooleanExpression.getLeftExpression();
            if (isBooleanExpression.isNot()) {
                filter = Filters.or(
                        Filters.eq(column.getColumnName(), BsonBoolean.FALSE),
                        Filters.exists(column.getColumnName(), false)
                );
            } else {
                filter = Filters.eq(column.getColumnName(), BsonBoolean.TRUE);
            }
        } else {
            throw new UnsupportedExpression("isBoolean left expression must be a column, instead we got " + isBooleanExpression.getLeftExpression());
        }
    }

    @Override
    public void visit(LikeExpression likeExpression) {
        throw new UnsupportedExpression("Unsupported expression " + likeExpression);
    }

    @Override
    public void visit(MinorThan minorThan) {
        ExpressionToMongoValueVisitor v = new ExpressionToMongoValueVisitor();
        ColumnAndExpression ce = ColumnAndExpression.extractColumnAndExpression(minorThan);
        ce.expression.accept(v);
        filter = Filters.lt(ce.column.getColumnName(), v.getValue());
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
        ExpressionToMongoValueVisitor v = new ExpressionToMongoValueVisitor();
        ColumnAndExpression ce = ColumnAndExpression.extractColumnAndExpression(minorThanEquals);
        ce.expression.accept(v);
        filter = Filters.lte(ce.column.getColumnName(), v.getValue());
    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
        ExpressionToMongoValueVisitor v = new ExpressionToMongoValueVisitor();
        ColumnAndExpression ce = ColumnAndExpression.extractColumnAndExpression(notEqualsTo);
        ce.expression.accept(v);
        filter = Filters.ne(ce.column.getColumnName(), v.getValue());
    }

    @Override
    public void visit(Parenthesis parenthesis) {
        parenthesis.getExpression().accept(this);
    }

    @Override
    public void visit(NotExpression aThis) {
        ExpressionVisitorToMongoFilter v = new ExpressionVisitorToMongoFilter();
        aThis.getExpression().accept(v);
        filter = Filters.not(v.filter);
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
    public void visit(NullValue nullValue) {
        filter = BsonNull.VALUE.asDocument();
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
    public void visit(DoubleValue doubleValue) {
        throw new UnsupportedExpression("Unsupported expression " + doubleValue);
    }

    @Override
    public void visit(LongValue longValue) {
        filter = new BsonInt64(longValue.getValue()).asDocument();
    }

    @Override
    public void visit(HexValue hexValue) {
        throw new UnsupportedExpression("Unsupported expression " + hexValue);
    }

    @Override
    public void visit(DateValue dateValue) {
        throw new UnsupportedExpression("Unsupported expression " + dateValue);
    }

    @Override
    public void visit(TimeValue timeValue) {
        throw new UnsupportedExpression("Unsupported expression " + timeValue);
    }

    @Override
    public void visit(TimestampValue timestampValue) {
        throw new UnsupportedExpression("Unsupported expression " + timestampValue);
    }

    @Override
    public void visit(StringValue stringValue) {
        throw new UnsupportedExpression("Unsupported expression " + stringValue);
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
