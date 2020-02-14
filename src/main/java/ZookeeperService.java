import akka.actor.ActorRef;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperService {
    private static final String ZOOKEEPER_CONNECT_STRING = "127.0.0.1:2181";
    private static final String ROOT_PATH = "/servers";
    private static final String NODES_PATH = "/servers/s";

    private ZooKeeper zooKeeper;
    private ActorRef actorRef;
}
