import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList;
import net.sf.jsqlparser.statement.select.SubSelect;
import org.bson.BsonValue;

import java.util.List;
import java.util.stream.Collectors;

public class ExpressionListToMongoValuesVisitor implements ItemsListVisitor {

    public List<BsonValue> getValues() {
        return values;
    }

    private List<BsonValue> values;

    @Override
    public void visit(SubSelect subSelect) {
        throw new UnsupportedExpression("Unsupported expression: " + subSelect);
    }

    @Override
    public void visit(ExpressionList expressionList) {
        values = expressionList.getExpressions().stream().map(e -> {
            ExpressionToMongoValueVisitor v = new ExpressionToMongoValueVisitor();
            e.accept(v);
            return v.getValue();
        }).collect(Collectors.toList());
    }

    @Override
    public void visit(NamedExpressionList namedExpressionList) {
        values = namedExpressionList.getExpressions().stream().map(e -> {
            ExpressionToMongoValueVisitor v = new ExpressionToMongoValueVisitor();
            e.accept(v);
            return v.getValue();
        }).collect(Collectors.toList());
    }

    @Override
    public void visit(MultiExpressionList multiExprList) {
        throw new UnsupportedExpression("Unsupported expression: " + multiExprList);
    }
}
