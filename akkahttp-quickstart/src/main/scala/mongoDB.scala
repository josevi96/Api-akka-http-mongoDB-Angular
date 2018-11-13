import reactivemongo.api.{Cursor, DefaultDB, MongoConnection, MongoDriver}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, Macros, document}

import scala.concurrent.Future

class mongoDB {
    val mongoUri = "mongodb://127.0.0.1:27017"

    import scala.concurrent.ExecutionContext.Implicits.global

    val driver = MongoDriver();
    val parsedURI = MongoConnection.parseURI(mongoUri)
    val connection = parsedURI.map(driver.connection(_))

    def futureConnection = Future.fromTry(connection)

    def db1: Future[DefaultDB] = futureConnection.flatMap(_.database("Todo"))

    def todoCollection: Future[BSONCollection] = db1.map(_.collection("myTodos"))

    implicit def personReader: BSONDocumentReader[Todo] = Macros.reader[Todo]

    implicit def personWriter: BSONDocumentWriter[Todo] = Macros.writer[Todo]

    def pendingTodo(): Future[List[Todo]] = {
      todoCollection.flatMap(_.find(document("done" -> false)).cursor[Todo]().collect[List](-1, Cursor.FailOnError[List[Todo]]()))
    }

    def doneTodo(): Future[List[Todo]] = {
      todoCollection.flatMap(_.find(document("done" -> true)).cursor[Todo]().collect[List](-1, Cursor.FailOnError[List[Todo]]()))
    }

    def allTodo(): Future[List[Todo]] = {
      val a = todoCollection.flatMap(_.find(document()).cursor[Todo]().collect[List](-1, Cursor.FailOnError[List[Todo]]()))
      println(a)
      a
    }

    def searchId(id: String): Future[List[Todo]] = {
      todoCollection.flatMap(_.find(document("id" -> id)).cursor[Todo]().collect[List](-1, Cursor.FailOnError[List[Todo]]()))
    }

    def toDone(todoID: String): Future[Int] = {
      val selector = document(
        "id" -> todoID
      )
      val modifier = BSONDocument(
        "$set" -> BSONDocument(
          "done" -> true
        )
      )
      todoCollection.flatMap(_.update(selector, modifier).map(_.n))
    }

    def toPending(todoID: String): Future[Int] = {
      val selector = document(
        "id" -> todoID
      )
      val modifier = BSONDocument(
        "$set" -> BSONDocument(
          "done" -> false
        )
      )
      todoCollection.flatMap(_.update(selector, modifier).map(_.n))
    }
  }

