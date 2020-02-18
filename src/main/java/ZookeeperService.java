import akka.actor.ActorRef;
import akka.dispatch.sysmsg.Watch;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.List;

public class ZookeeperService {
    private static final String ZOOKEEPER_SERVER = "127.0.0.1:2181";
    private static final int SESSION_TIMEOUT = 5000;
    private static final List<ACL> ACLS = ZooDefs.Ids.OPEN_ACL_UNSAFE;

    public ZooKeeper zk;
    //private ActorRef httpActor;


    public ZookeeperService() throws IOException {
        this.zk = new ZooKeeper(ZOOKEEPER_SERVER, SESSION_TIMEOUT, this);
        //this.httpActor = httpActor;
    }

    public void createClientNode() throws KeeperException, InterruptedException {
        String znodePath = "/clientQueue";
        if (zk.exists(znodePath, false) == null) {
            zk.create(znodePath, "data".getBytes(), ACLS, CreateMode.PERSISTENT);

            System.out.println("Client Node created");
        }
        //zk.create("/clientNode","data".getBytes(), ACLS, CreateMode.PERSISTENT);
    }

    public void createServerNode() throws KeeperException, InterruptedException {
        String znodePath = "/serverQueue";
        if (zk.exists(znodePath, false) == null) {
            zk.create(znodePath, "data".getBytes(), ACLS, CreateMode.PERSISTENT);

            System.out.println("Server Node created");
        }
    }

    public void msgFromClient() throws KeeperException, InterruptedException {
        zk.create("/clientQueue/msg", "testing..".getBytes(),ACLS,CreateMode.PERSISTENT);
        System.out.println("Sending message from client");
    }

    public void msgFromServer() throws KeeperException, InterruptedException {
        zk.create("/serverQueue/msg", "test completed OK".getBytes(),ACLS,CreateMode.PERSISTENT);
        System.out.println("Sending message from server");
    }
}
