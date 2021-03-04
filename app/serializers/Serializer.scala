package serializers

import play.api.libs.json._

trait Serializer[T, V] {

  def serialize(value: T): V

}

trait JsonSerializer[T] extends Serializer[T, JsValue] {

  def serialize(array: List[T]): JsValue = {
    JsArray(array.map { value =>
      serialize(value)
    })
  }

}
