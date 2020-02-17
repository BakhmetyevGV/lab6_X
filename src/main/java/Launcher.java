import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.Http;

public class Launcher {
    public static void main(String[] args){
        ActorSystem sys = ActorSystem.create("noname");
        ActorRef actor = sys.actorOf(Props.create(Actor.class));

        Http http = Http.get(sys);

        String server = "127.0.0.1:2181";

    }
}



