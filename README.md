# Api-akka-http-mongoDB-Angular
Lista de tareas gestionada con objetos del tipo Todo(id : String, title : String, description : String, done :Boolean).
La conexión con el back esta configurada para la dirección localhost:9002 

http://localhost:9002/todos  devuelve todos los Todo que haya en la base de datos

http://localhost:9002/todos/pending devuelve todos los Todo con "done" = false (las tareas pendientes de realizar

http://localhost:9002/todos/done devuelve todos los Todo con "done" = true (tareas ya realizadas)

http://localhost:9002/todos/search/${ID} devuelve el Todo con "id" = ID 

http://localhost:9002/todos/toDone/${ID} busca el Todo con "id" = ID, cambia el parametro "done"  a true y devuelve el objeto Todo cambiado

http://localhost:9002/todos/toUndone/${ID} busca el Todo con "id" = ID, cambia el parametro "done"  a false y devuelve el objeto Todo cambiado

el servicio del front esta alojado en la dirección estandar de angular 5 (http://localhost:4200/), se puede lanzar con el comando ng serve.

# akkahttp-quickstart
es el back programado en scala y utiliza la libreria de Akka con un sistema de actores que gestionan junto con reactiveMongo la base de datos Mongodb, una base de datos noSQL.

# mongo
Prueba de conexión aparte para la base de datos con reactiveMongo y el driver de Scala

# angular
Front muy muy basico para controlar la api
