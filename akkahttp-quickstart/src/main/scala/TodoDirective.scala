import scala.concurrent.Future
import akka.http.scaladsl.server.{Directive1, Directives}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._


trait TodoDirective extends Directives {

  def handle[T](f: Future[T])(e: Throwable => ApiError): Directive1[T] = onComplete(f) flatMap{
    case scala.util.Success(t) =>
      provide(t)
    case scala.util.Failure(error) =>
      val apiError = e(error)
      complete(apiError.statusCode, apiError.message)
  }

  def handleWithGeneric[T](f:Future[T]):Directive1[T] = handle[T](f)(_ => ApiError.generic)


}
