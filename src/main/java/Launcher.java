import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

public class Launcher {

    private static final String ACTOR_SYSTEM_NAME = "anonymizer-system";
    private static final String HOST_NAME = "localhost";

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        int serverPort = Integer.parseInt(args[0]);

        ActorSystem system = ActorSystem.create(ACTOR_SYSTEM_NAME);
        ActorRef configStoreActor = system.actorOf(Props.create(ConfigStoreActor.class));

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        Server server = new Server(http, serverPort, configStoreActor);

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow =
                server.createRoute().flow(system, materializer);

        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(HOST_NAME, serverPort),
                materializer
        );

        System.out.println("Server started on port: " + serverPort);
        System.in.read();
        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> system.terminate());
    }
}
