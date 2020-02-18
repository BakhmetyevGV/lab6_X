import akka.actor.ActorRef;
import akka.dispatch.sysmsg.Watch;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ZookeeperService {
    private static final String ZOOKEEPER_SERVER = "127.0.0.1:2181";
    private static final int SESSION_TIMEOUT = 5000;
    private static final List<ACL> ACLS = ZooDefs.Ids.OPEN_ACL_UNSAFE;
    private static final int CLIENT_MODE = 0;
    private static final int SERVER_MODE = 1;

    public ZooKeeper zk;
    private int mode;

    public ZookeeperService(int mode) throws IOException, KeeperException, InterruptedException {
        this.zk = new ZooKeeper(ZOOKEEPER_SERVER, SESSION_TIMEOUT, null);
        this.mode = mode;

        if (mode == CLIENT_MODE) {
            watchServers1();
        } else {
            watchServers2();
        }

    }

    private void watchServers1() {
        try {
            zk.exists("/serverQueue/msg", watchedEvent -> {
                if (watchedEvent.getType() == Watcher.Event.EventType.NodeCreated) {
                    watchServers1();
                }
            });

            byte[] data = zk.getData("/serverQueue/msg", null, null);
            System.out.println(new String(data, "UTF-8"));
            zk.delete("/serverQueue/msg", zk.exists("/serverQueue/msg", false).getVersion());
        } catch (InterruptedException | UnsupportedEncodingException | KeeperException ex) {
            ex.printStackTrace();
        }
}

    private void watchServers2() {
        try {
            zk.exists("/clientQueue/msg", watchedEvent -> {
                if (watchedEvent.getType() == Watcher.Event.EventType.NodeCreated) {
                    watchServers2();
                }
            });


            byte[] data = zk.getData("/clientQueue/msg", null, null);
            zk.delete("/clientQueue/msg", zk.exists("/clientQueue/msg", false).getVersion());
            zk.create("/serverQueue/msg", "answer to:".getBytes().)

        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
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
        zk.create("/clientQueue/msg", "testing..".getBytes(), ACLS, CreateMode.PERSISTENT);
        System.out.println("Sending message from client");
    }

    public void msgFromServer() throws KeeperException, InterruptedException {
        zk.create("/serverQueue/msg", "test completed OK".getBytes(), ACLS, CreateMode.PERSISTENT);
        System.out.println("Sending message from server");
    }
}

/*
connectionWatcher = we -> {
            if (we.getState() == Watcher.Event.KeeperState.SyncConnected) {
                System.out.println("Connected to Zookeeper in " + Thread.currentThread().getName());
            }
        };
 */
