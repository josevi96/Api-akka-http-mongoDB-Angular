import akka.http.scaladsl.model.{StatusCode, StatusCodes}

final case class  ApiError private(statusCode: StatusCode, message: String)


object ApiError{

  private def apply (statusCode: StatusCode, message: String): ApiError =  new ApiError (statusCode: StatusCode, message: String)

  val generic: ApiError = new ApiError(StatusCodes.InternalServerError, "kah pachao?")



}