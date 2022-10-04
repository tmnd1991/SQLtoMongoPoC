import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;

public class ColumnAndExpression {
    public final Column column;
    public final Expression expression;

    public ColumnAndExpression(Column column, Expression expression) {
        this.column = column;
        this.expression = expression;
    }

    public static ColumnAndExpression extractColumnAndExpression(BinaryExpression binaryExpression) {
        if (binaryExpression.getLeftExpression() instanceof Column) {
            return new ColumnAndExpression(
                    (Column) binaryExpression.getLeftExpression(),
                    binaryExpression.getRightExpression());
        } else if (binaryExpression.getRightExpression() instanceof Column) {
            return new ColumnAndExpression((Column) binaryExpression.getRightExpression(),
                    binaryExpression.getLeftExpression()
            );
        } else {
            throw new UnsupportedExpression("Cannot extract any column from [" + binaryExpression + "]");
        }
    }
}