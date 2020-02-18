import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.Http;
import akka.http.javadsl.server.AllDirectives;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.List;


public class Launcher {
    private static final String ZOOKEEPER_SERVER = "127.0.0.1:2181";
    private static final int SESSION_TIMEOUT = 5000;
    private static final List<ACL> ACLS = ZooDefs.Ids.OPEN_ACL_UNSAFE;

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        ActorSystem sys = ActorSystem.create("noname");
        //ActorRef httpActor = sys.actorOf(Props.create(HttpActor.class));
        Http http = Http.get(sys);
        int serverPort = Integer.parseInt(args[0]);

        Server server = new Server(http, serverPort);

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

            System.out.println("type: "  + we.getType());
            System.out.println("state: " + we.getState());
        };

        ZooKeeper zk = new ZooKeeper(ZOOKEEPER_SERVER, SESSION_TIMEOUT, connectionWatcher);


//        zk.delete(znodePath + "/msg0000000022",
//                zk.exists(znodePath + "/msg0000000022", false).getVersion());

//        for(String node : zk.getChildren(znodePath, false)){
//            System.out.println(node);
//        }

        String znodePath = "/clientQueue";
        if (zk.exists(znodePath, false) == null) {
            zk.create(znodePath, "data".getBytes(), ACLS, CreateMode.PERSISTENT);
        }

        //zk.exists(znodePath, clientWatcher);

        String znodePath2 = "/clientQueue/msg";
        if (zk.exists(znodePath2, clientWatcher) == null) {
            zk.create(znodePath2, "test1".getBytes(), ACLS, CreateMode.PERSISTENT);
        } else {
            zk.delete(znodePath2, zk.exists(znodePath2, false).getVersion());
            zk.create(znodePath2, "test2".getBytes(), ACLS, CreateMode.PERSISTENT);
        }

//        byte[] data = zk.getData(znodePath, null, null);
//        System.out.println("Result: " + new String(data, "UTF-8"));
//
//        byte[] data2 = zk.getData(znodePath2, null, null);
//        System.out.println("Result2: " + new String(data2, "UTF-8"));
//
//        for(String node : zk.getChildren(znodePath, false)){
//            System.out.println(node);
//        }


        System.in.read();
    }
}



