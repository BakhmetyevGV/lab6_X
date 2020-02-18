import akka.actor.ActorRef;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Server extends AllDirectives {
    private static final int CLIENT_MODE = 0;
    private static final int SERVER_MODE = 1;

    private Http http;
    private ActorRef httpActor;
    private int port;
    private ZookeeperService zookeeperService;
    private ZooKeeper zk;

    private Watcher watcher;
    private String nodePath;
    private String dataNode;

    public Server(final Http http, int port, ActorRef httpActor) throws IOException, KeeperException, InterruptedException {
        this.http = http;
        this.port = port;
        this.httpActor = httpActor;
        this.zookeeperService = new ZookeeperService(httpActor);
        this.zk = this.zookeeperService.zk;

        switch (port) {
            case 8094:
                nodePath = "/serverQueue";
                dataNode = "/serverQueue/msg";

                initWatcher(CLIENT_MODE);

                /*...*/
                break;
            case 8095:
                nodePath = "/clientQueue";
                dataNode = "/clientQueue/msg";

                initWatcher(SERVER_MODE);
                /*...*/
                break;
            default:
                System.out.println("Fuck this shit im out");
                System.exit(-1);
        }

    }

    private void initWatcher(int mode) {
        watcher = we -> {
            if (we.getType() == Watcher.Event.EventType.NodeCreated) {
                try {
                    byte[] data = zk.getData(dataNode, true, zk.exists(dataNode, true)); //TODO
                    zk.delete(nodePath, zk.exists(nodePath, false).getVersion());

                    if (mode == CLIENT_MODE) {
                        System.out.println(new String(data, "UTF-8"));
                    } else if (mode == SERVER_MODE) {
                        handleHttp(new String(data, "UTF-8"));
                    }


                } catch (KeeperException | InterruptedException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            try {
                watchNodes();
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        };
    }

    public Route createRoute() {
        return get(() ->
                parameter("url", url -> {
                    return completeWithFuture(http.singleRequest(HttpRequest.create(url)));
                }));
    }

    private void watchNodes() throws KeeperException, InterruptedException {
        zk.exists(nodePath, watcher);
    }

    private void handleHttp(String data) {

    }

}
