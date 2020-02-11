
import akka.actor.ActorRef;
import akka.http.javadsl.Http;
import akka.http.javadsl.server.AllDirectives;


public class Server extends AllDirectives {
    private Http http;
    private ActorRef actorRef;

    public server(final Http http){

    }

}
