import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.Http;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class Launcher {
    public static void main(String[] args){
        ActorSystem sys = ActorSystem.create("noname");
        ActorRef actor = sys.actorOf(Props.create(Actor.class));

        Http http = Http.get(sys);

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

    }
}



