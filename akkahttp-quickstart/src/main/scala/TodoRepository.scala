import scala.concurrent.Future

trait TodoRepository {
  def all():List[Todo]
  def done():List[Todo]
  def pending():List[Todo]

}
case class InMemoryTodoRepository(initialTodos: List[Todo])extends TodoRepository{

  private val todos: List[Todo] =  initialTodos.toList

  override def all(): List[Todo] = todos


  override def done(): List[Todo] = todos.filter(_.done == true)

  override def pending(): List[Todo] = todos.filter(_.done == false)

  def add(todo : Todo): List[Todo] =  todos :+ todo

  def delete(todo : Todo): InMemoryTodoRepository = new InMemoryTodoRepository(todos.filter(!_.id.equals(todo.id)))
}