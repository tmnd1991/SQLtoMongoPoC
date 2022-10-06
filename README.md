A minimalistic approach to parse SQL expressions (i.e. where conditions) and transform them into Mongo DB filters.

This project depends only on:

- "com.github.jsqlparser" % "jsqlparser" % "3.2"
- "org.mongodb" % "mongo-java-driver" % "3.12.11"

Some testing against a real DB is definetely due, but I think it's a good starting point and a way to show a simple
approach to solve the problem.