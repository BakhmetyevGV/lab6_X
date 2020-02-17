
import akka.actor.ActorRef;
import akka.http.javadsl.Http;
import akka.http.javadsl.server.AllDirectives;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;


public class Server extends AllDirectives {
    private Http http;
    private ActorRef actorRef;

    public Server(final Http http, int port, ActorRef actorRef) throws InterruptedException, IOException, KeeperException {
        this.http = http;
        this.actorRef = actorRef;
    }

}
