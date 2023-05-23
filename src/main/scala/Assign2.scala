import Assign1.{Error, Info, SimpleActorLogger}
import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, ActorSystem, OneForOneStrategy, Props, SupervisorStrategy}

object Assign2 extends App{
  class SupervisorAct extends Actor {

    override val supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
      case _: NullPointerException => Restart
      case _: IllegalArgumentException => Stop
      case _: RuntimeException => Resume
      case _: Exception => Restart
      case _ => Escalate
    }

    override def receive: Receive = {
      case props: Props =>
        val childRef = context.actorOf(props)
        sender() ! childRef
    }
  }

  val system = ActorSystem("Supervisior")
  val supervisorActor = system.actorOf(Props[SupervisorAct])
  val loggerActor = system.actorOf(Props[SimpleActorLogger])

  loggerActor ! Error("Passing the error message")
  loggerActor ! Info("How are you?")
  loggerActor ! Info("Anuj Saklani")
  loggerActor ! Info("at Tellius")
  loggerActor ! Error("Passing error message 2nd time")
  loggerActor ! Info("Are you fine?")
  loggerActor ! Error("Passing error message 3rd time")
  loggerActor ! Info("Are you fine?")
  loggerActor ! Info("Are You there?")
}
