import akka.actor.ActorRef;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class ZookeeperService {
    private static final String ZOOKEEPER_CONNECT_STRING = "127.0.0.1:2181";
    private static final String ROOT_PATH = "/servers";
    private static final String NODES_PATH = "/servers/s";
    private static final int SESSION_TIMEOUT = 5000;

    private ZooKeeper zooKeeper;
    private ActorRef actorRef;

    private ZooKeeper createZooKeeper() throws IOException {
        return new ZooKeeper(ZOOKEEPER_CONNECT_STRING, SESSION_TIMEOUT, null);
    }
}
