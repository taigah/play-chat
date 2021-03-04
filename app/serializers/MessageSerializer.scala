package serializers

import com.google.inject.ImplementedBy
import play.api.libs.json._
import models._

@ImplementedBy(classOf[JsonMessageSerializerImpl])
trait JsonMessageSerializer extends JsonSerializer[Message]

class JsonMessageSerializerImpl extends JsonMessageSerializer {

  def serialize(message: Message): JsValue = {
    JsObject(Seq(
      "content" -> JsString(message.content)
    ))
  }

}
