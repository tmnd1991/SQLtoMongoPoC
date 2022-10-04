import net.sf.jsqlparser.expression.Expression
import net.sf.jsqlparser.parser.CCJSqlParserUtil
object Main extends App {


  val expr: Expression = CCJSqlParserUtil.parseCondExpression("a = 3 or country = 'italy' and name = 'antonio'")

  val visitor = new ExpressionVisitorToMongoFilter()
  expr.accept(visitor)

  println(visitor.getFilter)

}
