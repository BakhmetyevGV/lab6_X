import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.List;

public class Client {
    private static final List<ACL> ACLS = ZooDefs.Ids.OPEN_ACL_UNSAFE;

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        //ActorSystem sys = ActorSystem.create("noname");
        //ActorRef httpActor = sys.actorOf(Props.create(HttpActor.class));
        //Http http = Http.get(sys);
        int serverPort = Integer.parseInt(args[0]);

        Watcher connectionWatcher = we -> {
            if (we.getState() == Watcher.Event.KeeperState.SyncConnected) {
                System.out.println("Connected to Zookeeper in " + Thread.currentThread().getName());
            }
        };

        Watcher clientWatcher = we -> {
//            if(we.getType() == Watcher.Event.EventType.NodeCreated)
//                System.out.println("Node Created in clientQueue");
//            else
//                System.out.println("Something else");

            System.out.println("type: " + we.getType());
            System.out.println("state: " + we.getState());
        };

        int mode = (serverPort == 8094) ? 0 : 1;

        ZookeeperService zookeeperService = new ZookeeperService(1);

        zookeeperService.createClientNode();
        zookeeperService.createServerNode();
        //zookeeperService.watchClient();

        ZooKeeper zk = zookeeperService.zk;

    }
}
