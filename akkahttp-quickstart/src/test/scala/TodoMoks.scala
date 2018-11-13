import scala.concurrent.Future
trait TodoMoks {

     class FailingRepository extends TodoRepository{
       override def all(): List[Todo] = throw new Exception("el moquito de todos ha fallao loko")

       override def done(): List[Todo] = throw new Exception("el moquito de done ha fallao loko")

       override def pending(): List[Todo] =throw new Exception("el moquito de pending ha fallao loko")
     }
}
