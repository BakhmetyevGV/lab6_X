import akka.actor.ActorRef;
import akka.http.javadsl.Http;
import akka.http.javadsl.server.AllDirectives;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Server extends AllDirectives {
    private Http http;
    private ActorRef httpActor;

    private int port;
    private ZookeeperService zookeeperService;
    private ZooKeeper zk;

    public Server(final Http http, int port, ActorRef httpActor) {
        this.http = http;
        this.port = port;
        this.httpActor = httpActor;
        this.zookeeperService = new ZookeeperService(httpActor);


        switch (port){
            case 8094:
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

            }


        }

        zookeeperService.createServerNode(serverWatcher);
    }
}
