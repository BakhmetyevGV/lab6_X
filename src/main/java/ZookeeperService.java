import akka.actor.ActorRef;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class ZookeeperService {
    private static final String ZOOKEEPER_CONNECT_STRING = "127.0.0.1:2181";
    private static final String ROOT_PATH = "/servers";
    private static final String NODES_PATH = "/servers/s";
    private static final int SESSION_TIMEOUT = 5000;

    private ZooKeeper zooKeeper;
    private ActorRef actorRef;

    public ZookeeperService(ActorRef actorRef) throws IOException {
        this.actorRef = actorRef;
        this.zooKeeper = new ZooKeeper(ZOOKEEPER_CONNECT_STRING, SESSION_TIMEOUT, null);
    }

    public void createServer(String serverUrl) throws KeeperException, InterruptedException {
        zooKeeper.create(
                NODES_PATH,
                serverUrl.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL
        );
    }

//    private ZooKeeper createZooKeeper(ActorRef configStoreActor) throws IOException {
//
//        return new ZooKeeper(ZOOKEEPER_CONNECT_STRING, SESSION_TIMEOUT, null);
//    }
}
