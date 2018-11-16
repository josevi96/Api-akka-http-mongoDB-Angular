import reactivemongo.api.{Cursor, DefaultDB, MongoConnection, MongoDriver}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, Macros, document}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class mongoDB(implicit connection: Try[MongoConnection], executionContext: ExecutionContext) {

    val futureConnection = Future.fromTry(connection)

    val db1: Future[DefaultDB] = futureConnection.flatMap(_.database("Todo"))

    val todoCollection: Future[BSONCollection] = db1.map(_.collection("myTodos"))

    implicit val personReader: BSONDocumentReader[Todo] = Macros.reader[Todo]

    implicit val personWriter: BSONDocumentWriter[Todo] = Macros.writer[Todo]

    def pendingTodo(): Future[List[Todo]] = {
      todoCollection.flatMap(_.find(document("done" -> false)).cursor[Todo]().collect[List](-1, Cursor.FailOnError[List[Todo]]()))
    }

    def doneTodo(): Future[List[Todo]] = {
      todoCollection.flatMap(_.find(document("done" -> true)).cursor[Todo]().collect[List](-1, Cursor.FailOnError[List[Todo]]()))
    }

    def allTodo(): Future[List[Todo]] = {
      todoCollection.flatMap(_.find(document()).cursor[Todo]().collect[List](-1, Cursor.FailOnError[List[Todo]]()))
    }

    def searchId(id: String): Future[List[Todo]] = {
      todoCollection.flatMap(_.find(document("id" -> id)).cursor[Todo]().collect[List](-1, Cursor.FailOnError[List[Todo]]()))
    }

    def toDone(todoID: String): Future[List[Todo]] = {
      val selector = BSONDocument("id" -> todoID)
      val modifier = BSONDocument("$set" -> BSONDocument("done" -> true))
      todoCollection.flatMap(_.update(selector, modifier).map(_.n))
      searchId(todoID)
    }

    def toPending(todoID: String): Future[List[Todo]] = {
      val selector = BSONDocument("id" -> todoID)
      val modifier = BSONDocument("$set" -> BSONDocument("done" -> false))
      todoCollection.flatMap(_.update(selector, modifier).map(_.n))
      searchId(todoID)
    }
  }

