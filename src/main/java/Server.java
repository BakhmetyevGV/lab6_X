import akka.actor.ActorRef;
import akka.http.javadsl.Http;
import akka.http.javadsl.server.AllDirectives;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class Server extends AllDirectives {
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

                initWatcher();
                poseAsClient();
                /*...*/
                break;
            case 8095:
                nodePath = "/clientQueue";
                dataNode = "/clientQueue/msg";

                initWatcher();
                /*...*/
                break;
            default:
                System.out.println("Fuck this shit im out");
                System.exit(-1);
        }

    }
    private void initWatcher(){
        watcher = we -> {
            if (we.getType() == Watcher.Event.EventType.NodeCreated) {
                try {
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
    }

    private void watchNodes() throws KeeperException, InterruptedException {
        zk.exists(nodePath, watcher);
    }

    private void poseAsClient() throws KeeperException, InterruptedException {
        zookeeperService.createServerNode(watcher);
    }

    private void poseAsServer() throws KeeperException, InterruptedException {
        zookeeperService.createClientNode(watcher);
    }
}
