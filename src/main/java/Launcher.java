import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.Http;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.List;

public class Launcher {
    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        //ActorSystem sys = ActorSystem.create("noname");
        //ActorRef actor = sys.actorOf(Props.create(Actor.class));

        //Http http = Http.get(sys);

        String server = "127.0.0.1:2181";
        Object lock = new Object();

        Watcher connectionWatcher = new Watcher() {
            public void process(WatchedEvent we) {
                if (we.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("Connected to Zookeeper in " + Thread.currentThread().getName());
                    synchronized (lock) {
                        lock.notifyAll();
                    }
                }
            }
        };

        int sessionTimeout = 2000;
        ZooKeeper zooKeeper = null;
        synchronized (lock) {
            zooKeeper = new ZooKeeper(server, sessionTimeout, connectionWatcher);
            lock.wait();
        }

        // Создание нового узла
        String znodePath = "/clientQueue";
        List<ACL> acls = ZooDefs.Ids.OPEN_ACL_UNSAFE;
        if (zooKeeper.exists(znodePath, false) == null) {
            zooKeeper.create(znodePath, "data".getBytes(), acls, CreateMode.PERSISTENT_SEQUENTIAL);
        }

        String znodePath2 = "/clientQueue" + "/msg";
        if (zooKeeper.exists(znodePath2, false) == null) {
            zooKeeper.create(znodePath2, "data".getBytes(), acls, CreateMode.PERSISTENT);
        }

        zooKeeper.create("/clientQueue/msg",
                "GET http://localhost:8094/?url=https://www.google.com".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);

        // Получение данных из узла
        byte[] data = zooKeeper.getData("/clientQueue/msg", null, null);
        System.out.println("Result: " + new String(data, "UTF-8"));

    }
}



