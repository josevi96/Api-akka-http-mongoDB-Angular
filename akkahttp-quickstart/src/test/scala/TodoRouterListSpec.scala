import Main.{ChangeActor, system}
import akka.actor.Props
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}



class TodoRouterListSpec extends WordSpec with Matchers with ScalatestRouteTest with TodoMoks {
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._  //Para que convierta el Get en una Seq[Todo]
  import io.circe.generic.auto._

  private val doneTodo =  Todo("2","hacer la compra,patatas", "lechugas etc..",true)
  private val pendingTodo =  Todo("1", "matar a juan","juan hijo de puta te voy a matar", false)
  private val todos = List(doneTodo,pendingTodo)
  private val actor  =system.actorOf(Props[ChangeActor])

  "TodoRouter" should{

    "return all the todos" in{
      val repository = new InMemoryTodoRepository(todos)
      val router = new TodoRouter(actor)

      Get("/todos") ~> router.route ~> check{
        status shouldBe StatusCodes.OK
        val response = responseAs[List[Todo]]
        response shouldBe todos
      }
    }

    "return all the pending todos" in{
      val repository = new InMemoryTodoRepository(todos)
      val router = new TodoRouter(actor)

      Get("/todos/pending") ~> router.route ~> check{
        status shouldBe StatusCodes.OK
        val response = responseAs[List[Todo]]
        response shouldBe List(pendingTodo)
      }
    }


    "return all the  done todos" in{
      val repository = new InMemoryTodoRepository(todos)
      val router = new TodoRouter(actor)

      Get("/todos/done") ~> router.route ~> check{
        status shouldBe StatusCodes.OK
        val response = responseAs[List[Todo]]
        response shouldBe List(doneTodo)
      }
    }

    "fallo el moquito de todos hermano" in {
      val repository = new FailingRepository
      val router = new TodoRouter(actor)

      Get("/todos") ~> router.route ~> check {
        status shouldBe StatusCodes.InternalServerError
        val response = responseAs[String]
        response shouldBe ApiError.generic.message
      }
    }

    "fallo el moquito de done hermano" in {
      val repository = new FailingRepository
      val router = new TodoRouter(actor)

      Get("/todos/done") ~> router.route ~> check {
        status shouldBe StatusCodes.InternalServerError
      }
    }

    "fallo el moquito de pending hermano" in {
      val repository = new FailingRepository
      val router = new TodoRouter(actor)

      Get("/todos/pending") ~> router.route ~> check {
        status shouldBe StatusCodes.InternalServerError
      }
    }



  }
}