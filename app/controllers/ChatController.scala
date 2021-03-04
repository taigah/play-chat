package controllers

import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.{Inject, Singleton}

@Singleton
class ChatController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  def index() = Action {
    Ok(views.html.chat.render())
  }

}
