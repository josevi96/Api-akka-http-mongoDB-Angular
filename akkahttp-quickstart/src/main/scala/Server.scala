import akka.actor.ActorSystem
import akka.http.javadsl.ServerBinding
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import scala.concurrent.{Future,ExecutionContext}

class Server(router: Router,host: String,port: Int)(implicit system: ActorSystem, ex: ExecutionContext, mat: ActorMaterializer){

  def bind(): Future[Http.ServerBinding] = Http().bindAndHandle(router.route,host,port)


}


