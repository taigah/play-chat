package repositories

import com.google.inject.ImplementedBy
import models._

import javax.inject.Singleton
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

@ImplementedBy(classOf[MemoryMessageRepository])
trait MessageRepository {

  def all(): List[Message]
  def add(message: Message): Message

}

@Singleton
class MemoryMessageRepository extends MessageRepository {

  val store = new mutable.Queue[Message]

  add(new Message("salut"))

  def all(): List[Message] = {
    store.toList
  }

  def add(message: Message): Message = {
    store.enqueue(message)
    while (store.length > 10) {
      store.dequeue()
    }
    message
  }

}
