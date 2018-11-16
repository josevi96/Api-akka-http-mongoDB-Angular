
import akka.actor.{Actor, ActorSystem, Props}
import akka.stream.ActorMaterializer
import reactivemongo.api.{MongoConnection, MongoDriver}
import akka.pattern.pipe

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success, Try}

  object Main extends App {
    implicit val system: ActorSystem = ActorSystem("todoApi")
    implicit val executor: ExecutionContext = system.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val host = "0.0.0.0"
    val port = 9002

    val mongoUri = "mongodb://127.0.0.1:27017"

    val driver = MongoDriver()
    val parsedURI = MongoConnection.parseURI(mongoUri)
    implicit val connection: Try[MongoConnection] = parsedURI.map(driver.connection(_))

    val db = new mongoDB
    val actor =system.actorOf(Props[ChangeActor])
    val router = new TodoRouter(actor)
    val server = new Server(router,host,port)

    //Estados del actor, se podria poner en otro archivo:
    sealed trait ActorState
    case class AllTodo() extends ActorState
    case class ToDoItem(id: String) extends ActorState
    case class UnDoneItem (id:String) extends ActorState
    case class DoneTodo() extends ActorState
    case class PendingTodo() extends ActorState
    case class SearchingId(id: String) extends ActorState
    case class GetState() extends ActorState


    class ChangeActor extends Actor {

      def receive: Receive = mreceive(new InMemoryTodoRepository(List()))

      def mreceive(todoList: InMemoryTodoRepository): Receive = {

        case AllTodo =>
          val a = db.allTodo() pipeTo sender()

        case DoneTodo => db.doneTodo() pipeTo sender()


        case PendingTodo => db.pendingTodo() pipeTo sender()

        case SearchingId(id) => db.searchId(id) pipeTo sender()

        case ToDoItem(id : String ) => db.toDone(id) pipeTo sender()

        case UnDoneItem(id : String) => db.toPending(id) pipeTo sender()

        case _ =>  throw new Exception("kha pachao?")
      }
    }


   val binding =  server.bind()
   binding.onComplete{
    case scala.util.Success(_) => println("Success!")
    case scala.util.Failure(error: Error) => println(s"Failed :  ${error.getMessage}")
  }



    Await.result(binding,3.seconds)
  }

