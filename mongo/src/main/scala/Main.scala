

import reactivemongo.api.{Cursor, DefaultDB, MongoConnection, MongoDriver}

import scala.util.{Failure, Success}
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, Macros, document}
import reactivemongo.api.commands.{MultiBulkWriteResult, WriteResult}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.io.netty.handler.codec.json.JsonObjectDecoder

import scala.concurrent.duration._

case class Todo (id: String, title: String, description: String, done: Boolean)

 object Main extends App {


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

  def pendingTodo() : Future[List[Todo]] ={
    todoCollection.flatMap(_.find(document("done" -> false)).cursor[Todo]().collect[List](-1,Cursor.FailOnError[List[Todo]]()))
  }

  def doneTodo() : Future[List[Todo]] ={
   todoCollection.flatMap(_.find(document("done" -> true)).cursor[Todo]().collect[List](-1,Cursor.FailOnError[List[Todo]]()))tp
  }

  def allTodo() :Future[List[Todo]] = {
   todoCollection.flatMap(_.find(document()).cursor[Todo]().collect[List](-1,Cursor.FailOnError[List[Todo]]()))
  }


   allTodo.onComplete{
     case Success(value) => println(value)
   }
    futureConnection.onComplete{
      case Success(value) =>
      println(value.name)
      value.database("Todo").onComplete{
        case Success(value) => println(value.name)
      }
    }



}

