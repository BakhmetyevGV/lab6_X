import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Launcher {
    public static void main(String[] args){
        ActorSystem sys = ActorSystem.create("noname");
        ActorRef actor = sys.actorOf(Props.create(Actor.class));
    }
}



