import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.server.Directives
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import scala.concurrent.Future

class TodoDirectivesSpec extends WordSpec with Matchers with ScalatestRouteTest with Directives with TodoDirective {

  private val testRoute = pathPrefix("test") {
    path("success") {
      get {
        handleWithGeneric(Future.unit) { _ => complete(StatusCodes.OK) }
      }
    }
  } ~ path("failure") {
    get {
      handleWithGeneric(Future.failed(new Exception("failure"))) { _ => complete(StatusCodes.OK) }
    }
  }

  "TodoDirectives" should {
    "not return an error if the future succeds" in {
      Get("/test/success") ~> testRoute ~> check {
        status shouldBe StatusCodes.OK
      }
    }
  }
}