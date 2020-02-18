import akka.actor.ActorRef;
import akka.dispatch.sysmsg.Watch;
import akka.http.javadsl.Http;
import akka.http.javadsl.server.AllDirectives;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.nio.file.WatchEvent;

public class Server extends AllDirectives {
    private Http http;
    private ActorRef httpActor;

    private int port;
    private ZookeeperService zookeeperService;
    private ZooKeeper zk;

    private Watcher watcher;

    public Server(final Http http, int port, ActorRef httpActor) throws IOException {
        this.http = http;
        this.port = port;
        this.httpActor = httpActor;
        this.zookeeperService = new ZookeeperService(httpActor);
        this.zk = this.zookeeperService.zk;

        switch (port){
            case 8094:
                watcher = we ->{
                    if(we.getType() == Watcher.Event.EventType.NodeCreated){
                        zk.getData("/serverQueue/msg", true, zk.exists("/serverQueue/msg", true));
                    }
                };
                /*...*/
                break;
            case 8095:
                /*...*/
                break;
            default:
                System.out.println("Fuck this shit im out");
                System.exit(-1);
        }

    }



    private void poseAsClient(){
        Watcher serverWatcher = we-> {
            if(we.getType() == Watcher.Event.EventType.NodeCreated){
                /*...*/
                zk.exists("/serverQueue", serverWatcher)
            }


        }

        zookeeperService.createServerNode(serverWatcher);
    }
}
