import net.sf.jsqlparser.expression.Expression
import net.sf.jsqlparser.parser.CCJSqlParserUtil
object Main extends App {


  def toFilter(sql: String) = {
    val expr: Expression = CCJSqlParserUtil.parseCondExpression(sql)

    val visitor = new ExpressionVisitorToMongoFilter()
    expr.accept(visitor)
    visitor.getFilter
  }


  println(toFilter("a = 3 or country = 'italy' and name = 'antonio'"))

}
