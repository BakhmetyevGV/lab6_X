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

class Server extends AllDirectives {
    private Http http;

    public Server(final Http http, int port) {

    }
}

public class Launcher {
    private static final String ZOOKEEPER_SERVER = "127.0.0.1:2181";
    private static final int SESSION_TIMEOUT = 5000;
    private static final List<ACL> ACLS = ZooDefs.Ids.OPEN_ACL_UNSAFE;

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        //ActorSystem sys = ActorSystem.create("noname");
        //ActorRef actor = sys.actorOf(Props.create(Actor.class));
        //Http http = Http.get(sys);
        //int serverPort = Integer.parseInt(args[0]);

        //Server server = new Server(http, serverPort);

        Object lock = new Object();
        Watcher connectionWatcher = we -> {
            if (we.getState() == Watcher.Event.KeeperState.SyncConnected) {
                System.out.println("Connected to Zookeeper in " + Thread.currentThread().getName());
            }

        };

        ZooKeeper zk = new ZooKeeper(ZOOKEEPER_SERVER, SESSION_TIMEOUT, connectionWatcher);

        String znodePath = "/clientQueue";
        zk.delete(znodePath + "/msg0000000021",
                zk.exists(znodePath + "/msg0000000021", false).getVersion());

        for(String node : zk.getChildren(znodePath, false)){
            System.out.println(node);
        }

        //        if (zooKeeper.exists(znodePath, false) == null) {
//            zooKeeper.create(znodePath, "data".getBytes(), ACLS, CreateMode.PERSISTENT);
//        } else{
//            for(String node : zooKeeper.getChildren(znodePath, false)){
//                zooKeeper.delete(node, zooKeeper.exists(znodePath + node, false).getVersion());
//            }
//            zooKeeper.delete(znodePath, zooKeeper.exists(znodePath, true).getVersion());
//            zooKeeper.create(znodePath, "data".getBytes(), ACLS, CreateMode.PERSISTENT);
//        }

//        Watcher clientWatcher = we -> {
//            System.out.println("Connected to Zookeeper in ALSLSLSLSLSLSl " + Thread.currentThread().getName());
//        };
//
//        String znodePath2 = "/clientQueue/msg";
//        if (zooKeeper.exists(znodePath2, false) == null) {
//            zooKeeper.create(znodePath2, "test1".getBytes(), ACLS, CreateMode.PERSISTENT);
//        } else {
//            zooKeeper.delete(znodePath2, zooKeeper.exists(znodePath2, true).getVersion());
//            zooKeeper.create(znodePath2, "test2".getBytes(), ACLS, CreateMode.PERSISTENT);
//        }
//
//
//        byte[] data = zooKeeper.getData(znodePath, null, null);
//        System.out.println("Result: " + new String(data, "UTF-8"));
//
//        byte[] data2 = zooKeeper.getData(znodePath2, null, null);
//        System.out.println("Result2: " + new String(data2, "UTF-8"));


//        for(String node : zooKeeper.getChildren(znodePath, false)){
//            System.out.println(node);
//        }


        //System.in.read();
    }
}



