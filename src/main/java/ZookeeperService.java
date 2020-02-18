import akka.actor.ActorRef;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.List;

public class ZookeeperService {
    private static final String ZOOKEEPER_SERVER = "127.0.0.1:2181";
    private static final int SESSION_TIMEOUT = 5000;
    private static final List<ACL> ACLS = ZooDefs.Ids.OPEN_ACL_UNSAFE;

    public ZooKeeper zk;
    private ActorRef httpActor;

    public ZookeeperService(ActorRef httpActor) throws IOException {
        this.zk = new ZooKeeper(ZOOKEEPER_SERVER, SESSION_TIMEOUT, null);
        this.httpActor = httpActor;
    }
}
