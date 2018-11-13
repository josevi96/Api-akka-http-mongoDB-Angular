
import scala.util.{Success,Failure}
import scala.concurrent.{Await, ExecutionContext}
import akka.actor.{Actor, ActorSystem, Props}
import akka.stream.ActorMaterializer
import akka.pattern.ask
import scala.concurrent.duration._
import akka.http.scaladsl.server
import akka.io.Inet.SO.ReceiveBufferSize
import akka.util.Timeout
import reactivemongo.api.MongoConnection

  object Main extends App {
    implicit val system: ActorSystem = ActorSystem("todoApi")
    implicit val executor: ExecutionContext = system.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val host = "0.0.0.0"
    val port = 9002
    val  actor =system.actorOf(Props[ChangeActor])
    val router = new TodoRouter(actor)
    val server = new Server(router,host,port)
    val db = new mongoDB

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
          println("porsfasdfasf")

          db.allTodo().onComplete{
          case Success(a) =>  sender ! a
          case Failure(exception) => throw new Exception("error en all bd")
        }

        case DoneTodo => db.doneTodo().onComplete {
          case Success(a) => sender ! a
          case Failure(exception) => throw new Exception("error en done bd")
        }

        case PendingTodo => db.pendingTodo().onComplete {
          case Success(a) => sender ! a
          case Failure(exception) => throw new Exception("error en pending bd")
        }

        case SearchingId(id) => db.searchId(id).onComplete{
          case Success(a) => sender ! a
          case Failure(exception) => throw new Exception(s"error buscando id : ${id}")
        }


        case ToDoItem(id : String ) => db.toDone(id).onComplete{
          case Success(a) => printf("a")
          case Failure(exception) => throw new Exception(s"error en TodoItem con ${id}")
        }

        case UnDoneItem(id : String)   => db.toDone(id).onComplete{
          case Success(a) => printf("a")
          case Failure(exception) => throw new Exception(s"error en TodoItem con ${id}")
        }

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

