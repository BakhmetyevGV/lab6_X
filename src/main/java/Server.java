
import akka.actor.ActorRef;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.http.javadsl.model.Uri;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.japi.Pair;
import akka.pattern.Patterns;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

public class Server extends AllDirectives {

    private static final String URL_PARAM_NAME = "url";
    private static final String COUNT_PARAM_NAME = "count";
    private static final Duration TIMEOUT = Duration.ofMillis(3000);

    private Http http;
    private ActorRef configStoreActor;

    public Server(final Http http, int port, ActorRef configStoreActor) throws InterruptedException, IOException, KeeperException {
        this.http = http;
        this.configStoreActor = configStoreActor;
        initZooKeeper(port);
    }

    private void initZooKeeper(int port) throws IOException, KeeperException, InterruptedException {
        ZookeeperService zookeeperService = new ZookeeperService(configStoreActor);
        zookeeperService.createServer(getServerUrl(port));
    }

    private String getServerUrl(int port) {
        return "http://localhost:" + port;
    }

    public Route createRoute() {
        return get(() ->
                parameter(URL_PARAM_NAME, url ->
                        parameter(COUNT_PARAM_NAME, countParam -> {
                                    int count = Integer.parseInt(countParam);

                                    return count == 0 ?
                                            completeWithFuture(fetch(url)) :
                                            completeWithFuture(redirect(url, count));
                                }
                        )
                )
        );
    }

    private CompletionStage<HttpResponse> fetch(String url) {
        return http.singleRequest(HttpRequest.create(url));
    }

    private CompletionStage<HttpResponse> redirect(String url, int count) {
        return Patterns.ask(configStoreActor, new GetRandomServerMessage(), TIMEOUT)
                .thenCompose(serverUrl -> fetch(createRedirectUrl((String) serverUrl, url, count)));
    }

    private String createRedirectUrl(String serverUrl, String queryUrl, int count) {
        return Uri.create(serverUrl)
                .query(Query.create(
                        Pair.create(URL_PARAM_NAME, queryUrl),
                        Pair.create(COUNT_PARAM_NAME, Integer.toString(count - 1))
                ))
                .toString();
    }
}
