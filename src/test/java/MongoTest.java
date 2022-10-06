import com.mongodb.client.model.Filters;
import org.bson.BsonBoolean;
import org.bson.BsonInt64;
import org.bson.BsonNull;
import org.bson.BsonString;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MongoTest {

    @Test
    public void basicAndOrComposition() {
        Bson filter = Main.toFilter("a = 3 or country = 'italy' and name = 'antonio'");

        Bson expectedValue = Filters.or(
                Filters.eq("a", new BsonInt64(3)),
                Filters.and(
                        Filters.eq("country", new BsonString("italy")),
                        Filters.eq("name", new BsonString("antonio"))
                )
        );

        Assertions.assertEquals(expectedValue, filter);
    }

    @Test
    public void basicAndOrCompositionWithParens() {
        Bson filter = Main.toFilter("(a = 3 or country = 'italy') and name = 'antonio'");

        Bson expectedValue = Filters.and(
                Filters.or(
                        Filters.eq("a", new BsonInt64(3)),
                        Filters.eq("country", new BsonString("italy"))
                ),
                Filters.eq("name", new BsonString("antonio"))
        );

        Assertions.assertEquals(expectedValue, filter);
    }

    @Test
    public void lt() {
        Bson filter = Main.toFilter("a < 3");
        Bson expectedValue = Filters.lt("a", new BsonInt64(3));
        Assertions.assertEquals(expectedValue, filter);
    }

    @Test
    public void gt() {
        Bson filter = Main.toFilter("a > 3");
        Bson expectedValue = Filters.gt("a", new BsonInt64(3));
        Assertions.assertEquals(expectedValue, filter);
    }

    @Test
    public void in() {
        Bson filter = Main.toFilter("a in (3,3,4,5,6)");
        Bson expectedValue = Filters.in("a", new BsonInt64(3), new BsonInt64(3), new BsonInt64(4), new BsonInt64(5), new BsonInt64(6));
        Assertions.assertEquals(expectedValue, filter);
    }

    @Test
    public void isNull() {
        Bson filter = Main.toFilter("a is null");
        Bson expectedValue = Filters.or(Filters.exists("a", false), Filters.eq("a", BsonNull.VALUE));
        Assertions.assertEquals(expectedValue, filter);
    }

    @Test
    public void isNotNull() {
        Bson filter = Main.toFilter("a is not null");
        Bson expectedValue = Filters.or(Filters.exists("a", true), Filters.ne("a", BsonNull.VALUE));
        Assertions.assertEquals(expectedValue, filter);
    }

    @Test
    public void isTrue() {
        Bson filter = Main.toFilter("a is True");
        Bson expectedValue = Filters.eq("a", BsonBoolean.TRUE);
        Assertions.assertEquals(expectedValue, filter);
    }

    @Test
    public void isNotTrue() {
        Bson filter = Main.toFilter("a is not True");
        Bson expectedValue = Filters.or(
                Filters.eq("a", BsonBoolean.FALSE),
                Filters.exists("a", false)
        );
        Assertions.assertEquals(expectedValue, filter);
    }


    @Test
    public void isFalse() {
        Bson filter = Main.toFilter("a is False");
        Bson expectedValue =
                Filters.or(
                        Filters.eq("a", BsonBoolean.FALSE),
                        Filters.exists("a", false)
                );
        Assertions.assertEquals(expectedValue, filter);
    }

    @Test
    public void isNotFalse() {
        Bson filter = Main.toFilter("a is not false");
        Bson expectedValue = Filters.eq("a", BsonBoolean.TRUE);
        Assertions.assertEquals(expectedValue, filter);
    }

    @Test
    public void notAeB() {
        Bson filter = Main.toFilter("not (a = 'b')");
        Bson expectedValue = Filters.not(Filters.eq("a",new BsonString("b")));
        Assertions.assertEquals(expectedValue, filter);
    }

    @Test
    public void AnoteB() {
        Bson filter = Main.toFilter("a != 'b')");
        Bson expectedValue = Filters.ne("a",new BsonString("b"));
        Assertions.assertEquals(expectedValue, filter);
    }
}
