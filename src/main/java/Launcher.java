import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.List;

public class Launcher {
    private static final String ZOOKEEPER_SERVER = "127.0.0.1:2181";
    private static final int SESSION_TIMEOUT = 3000;
    private static final List<ACL> ACLS = ZooDefs.Ids.OPEN_ACL_UNSAFE;

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        //ActorSystem sys = ActorSystem.create("noname");
        //ActorRef actor = sys.actorOf(Props.create(Actor.class));
        //Http http = Http.get(sys);

        Object lock = new Object();
        Watcher connectionWatcher = we -> {
            if (we.getState() == Watcher.Event.KeeperState.SyncConnected) {
                System.out.println("Connected to Zookeeper in " + Thread.currentThread().getName());
                synchronized (lock) {
                    lock.notifyAll();
                }
            }
        };

        ZooKeeper zooKeeper = null;
        synchronized (lock) {
            zooKeeper = new ZooKeeper(ZOOKEEPER_SERVER, SESSION_TIMEOUT, connectionWatcher);
            lock.wait();
        }

        // Создание нового узла
        String znodePath = "/clientQ";
        if (zooKeeper.exists(znodePath, false) == null) {
            zooKeeper.create(znodePath, "data".getBytes(), ACLS, CreateMode.PERSISTENT_SEQUENTIAL);
        }

//        String znodePath2 = "/servers/s";
//        byte[] cmd = "GET http://localhost:8094/?url=https://www.google.com".getBytes();
//
//        if (zooKeeper.exists(znodePath2, false) == null) {
//            zooKeeper.create(znodePath2, cmd, ACLS, CreateMode.PERSISTENT_SEQUENTIAL);
//        }

        byte[] data = zooKeeper.getData(znodePath, null, null);
        System.out.println("Result: " + new String(data, "UTF-8"));

    }
}



