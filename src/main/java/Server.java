import akka.actor.ActorRef;
import akka.dispatch.sysmsg.Watch;
import akka.http.javadsl.Http;
import akka.http.javadsl.server.AllDirectives;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.nio.file.WatchEvent;

public class Server extends AllDirectives {
    private Http http;
    private ActorRef httpActor;

    private int port;
    private ZookeeperService zookeeperService;
    private ZooKeeper zk;

    private Watcher watcher;
    private String NODE_PATH;

    public Server(final Http http, int port, ActorRef httpActor) throws IOException {
        this.http = http;
        this.port = port;
        this.httpActor = httpActor;
        this.zookeeperService = new ZookeeperService(httpActor);
        this.zk = this.zookeeperService.zk;

        switch (port) {
            case 8094:
                NODE_PATH = "/serverQueue";

                watcher = we -> {
                    if (we.getType() == Watcher.Event.EventType.NodeCreated) {
                        try {
                            String dataNode = NODE_PATH + "/msg";
                            zk.getData(dataNode, true, zk.exists(dataNode, true)); //TODO
                        } catch (KeeperException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        watchNodes();
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                };
                /*...*/
                break;
            case 8095:
                NODE_PATH = "/clientQueue";

                /*...*/
                break;
            default:
                NODE_PATH = "crap";
                System.out.println("Fuck this shit im out");
                System.exit(-1);
        }

    }
    private void watchNodes() throws KeeperException, InterruptedException {
        zk.exists(NODE_PATH, watcher);
    }

    private void poseAsClient() throws KeeperException, InterruptedException {
        zookeeperService.createServerNode(watcher);
    }
}
