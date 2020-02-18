import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class test {
    private static final String ZOOKEEPER_SERVER = "127.0.0.1:2181";
    private static final int SESSION_TIMEOUT = 5000;

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zk = new ZooKeeper(ZOOKEEPER_SERVER, SESSION_TIMEOUT, null);

        for(String node : zk.getChildren("/clientQueue", null)){
            System.out.println("cQ: " + node);
        }

        for(String node : zk.getChildren("/serverQueue", null)){
            System.out.println("sQ: " + node);
        }
    }

}
