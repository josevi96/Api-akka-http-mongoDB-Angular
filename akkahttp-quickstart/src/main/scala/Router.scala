import Main.{AllTodo, DoneTodo, PendingTodo, SearchingId, ToDoItem, UnDoneItem}
import akka.actor._
import akka.http.scaladsl.server.{Directives, Route}
import akka.pattern.ask
import akka.util.Timeout
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


trait Router {
  def route: Route
}
class TodoRouter(actor: ActorRef) extends Router with Directives with TodoDirective {
  implicit val timeout: Timeout = 5.minutes
  implicit val sender = actor
  override def route: Route = pathPrefix("todos") {
    pathEndOrSingleSlash {
      get {
        println("por aqui pasa")
        val request = (actor ? AllTodo).mapTo[List[Todo]]
        onSuccess(request)(complete(_))
      }
    } ~
      path("done") {
        get {
          val request = (actor ? DoneTodo).mapTo[List[Todo]]
          onSuccess(request)(complete(_))
        }
      } ~
      path("pending") {
        get {
          val request = (actor ? PendingTodo).mapTo[List[Todo]]
          onSuccess(request)(complete(_))
        }
      } ~
      path(s"toDone" / Segment) { id =>
        get {
          val request = (actor ? ToDoItem(id)).mapTo[List[Todo]]
          onSuccess(request)(complete(_))
        }
      } ~
      path("toUndone" / Segment) { id =>
        get {
          val request = (actor ? UnDoneItem(id)).mapTo[List[Todo]]
          onSuccess(request)(complete(_))
        }
      } ~
      path(s"search" / Segment) { id =>
        get {
          val request = (actor ? SearchingId(id)).mapTo[Todo]
          onSuccess(request)(complete(_))
        }
      }

  }
}