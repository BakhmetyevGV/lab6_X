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

            System.out.println("type: "  + we.getType());
            System.out.println("state: " + we.getState());
        };

        int mode = (serverPort == 8094) ? 0 : 1;

        ZookeeperService zookeeperService = new ZookeeperService(mode);

        if(serverPort == 0){
            ZooKeeper zk = zookeeperService.zk;
            System.out.println("Starting cleanup");
            for(String node : zk.getChildren("/clientQueue", false)){
                System.out.println("client msg " + node);
            }

            for(String node : zk.getChildren("/serverQueue", false)){
                System.out.println("server msg " + node);
            }

            if(zk.exists("/clientQueue/msg", false) != null) {
                zk.delete("/clientQueue/msg", zk.exists("/clientQueue/msg", false).getVersion());
            }

            if(zk.exists("/serverQueue/msg", false) != null) {
                zk.delete("/serverQueue/msg", zk.exists("/serverQueue/msg", false).getVersion());
            }

//            if(zk.exists("/clientQueue", false) != null){
//                zk.delete("/clientQueue", zk.exists("/clientQueue", false).getVersion());
//            }
//
//            if(zk.exists("/serverQueue", false) != null){
//                zk.delete("/serverQueue", zk.exists("/serverQueue", false).getVersion());
//            }


            System.out.println("Finished cleanup");
            System.exit(0);
        }

        zookeeperService.createClientNode();
        zookeeperService.createServerNode();

        ZooKeeper zk = zookeeperService.zk;

        Object lock = new Object();
        if(serverPort == 8094){
            synchronized (lock){
                zk.create("/clientQueue/msg", "msg from client 1".getBytes(), ACLS, CreateMode.PERSISTENT);
                lock.wait();
            }
            zk.create("/clientQueue/msg", "msg from client 1".getBytes(), ACLS, CreateMode.PERSISTENT);
            zk.create("/clientQueue/msg", "msg from client 2".getBytes(), ACLS, CreateMode.PERSISTENT);
            zk.create("/clientQueue/msg", "msg from client 3".getBytes(), ACLS, CreateMode.PERSISTENT);

        }

        //zk.create("/clientQueue/msg", "msg from client queue".getBytes(), ACLS, CreateMode.PERSISTENT);
        //zk.create("/serverQueue/msg", "msg from server queue".getBytes(), ACLS, CreateMode.PERSISTENT);


        //Server server = new Server(serverPort);

//        ZooKeeper zk = zookeeperService.zk;
//        zk.delete("/clientQueue/msg",
//                zk.exists("/clientQueue/msg", false).getVersion());
//
//        zk.delete("/clientQueue", zk.exists("/clientQueue", false).getVersion());
//        zk.delete("/serverQueue", zk.exists("/serverQueue", false).getVersion());

        //zk.exists(znodePath, clientWatcher);

//        String znodePath2 = "/clientQueue/msg";
//        if (zk.exists(znodePath2, clientWatcher) == null) {
//            zk.create(znodePath2, "test1".getBytes(), ACLS, CreateMode.PERSISTENT);
//        } else {
//            zk.delete(znodePath2, zk.exists(znodePath2, false).getVersion());
//            zk.create(znodePath2, "test2".getBytes(), ACLS, CreateMode.PERSISTENT);
//        }



//        byte[] data = zk.getData(znodePath, null, null);
//        System.out.println("Result: " + new String(data, "UTF-8"));
//
//        byte[] data2 = zk.getData(znodePath2, null, null);
//        System.out.println("Result2: " + new String(data2, "UTF-8"));
//
//        for(String node : zk.getChildren(znodePath, false)){
//            System.out.println(node);
//        }

        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}



