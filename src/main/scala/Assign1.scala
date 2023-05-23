
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.typesafe.config.ConfigFactory


object Assign1 extends App{
  case class Info(message: String)

  case class Warn(message: String)

  case class Error(message: String)

  class SimpleActorLogger extends Actor with ActorLogging {


    override def receive: Receive = {

      case Info(message) => log.info(message)
      case Warn(message) => log.warning(message)
      case Error(message) => throw new Exception(sender().toString + "error in this actor : message " + message)
      case message => log.info(message.toString)

    }

  }

  val configString =
    """
      |  akka {
      |    loglevel = "INFO"
      |    loggers = ["akka.event.slf4j.Slf4jLogger"]
      |  }
   """.stripMargin

  val config = ConfigFactory.parseString(configString)
  val system = ActorSystem("LoggingDemo", ConfigFactory.load(config))
  val actor = system.actorOf(Props[SimpleActorLogger])

  for (_ <- 1 to 20) actor ! Info("Let's test this!")


  val defaultConfigFileSystem = ActorSystem("DefaultConfigFileDemo")
  val defaultConfigActor = defaultConfigFileSystem.actorOf(Props[SimpleActorLogger])
  defaultConfigActor ! Warn("Know me??")
}
