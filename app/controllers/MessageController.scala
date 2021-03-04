package controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}
import repositories.MessageRepository
import serializers._
import models._
import play.api.i18n.{Langs, Messages, MessagesApi}

import javax.inject.{Inject, Singleton}

case class MessageData(val content: String)

@Singleton
class MessageController @Inject() (
                                    repository: MessageRepository,
                                    serializer: JsonMessageSerializer,
                                    langs: Langs,
                                    messagesApi: MessagesApi,
                                    cc: ControllerComponents
                                  ) extends AbstractController(cc) {

  def index() = Action {
    val messages = repository.all()
    Ok(serializer.serialize(messages))
  }

  def store() = Action { implicit request =>
    val form = Form(
      mapping(
        "content" -> nonEmptyText(maxLength = 100)
      )(MessageData.apply)(MessageData.unapply)
    )
    form.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(JsObject(Seq(
          "error" -> JsArray(formWithErrors.errors.toList.map { error =>
            JsObject(Seq(
              "field" -> JsString(error.key),
              "message" -> JsString(messagesApi(error.message, error.args:_*)(langs.availables.head))
            ))
          })
        )))
      },
      messageData => {
        val message = new Message(messageData.content)
        val storedMessage = repository.add(message)
        Created(serializer.serialize(storedMessage))
      }
    )
  }

}
