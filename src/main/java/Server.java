import akka.http.javadsl.Http;
import akka.http.javadsl.server.AllDirectives;
import org.apache.zookeeper.ZooKeeper;

public class Server extends AllDirectives {
    private Http http;
    private int port;
    private ZookeeperService zookeeperService;
    private ZooKeeper zk;

    public Server(final Http http, int port) {
        this.http = http;
        this.port = port;

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

    }
}
