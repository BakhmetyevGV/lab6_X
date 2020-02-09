import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import java.util.Random;

public class ConfigStoreActor extends AbstractActor {

    private String[] serverList;

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create().match(SetServerListMessage.class, msg -> {
            System.out.println("set config");
            this.serverList = msg.getServerList();
        }).match(GetRandomServerMessage.class, msg ->
                sender().tell(getRandomServer(), self())
        ).build();
    }

    private String getRandomServer() {
        String serverUrl = serverList[new Random().nextInt(serverList.length)];
        System.out.println("redirect to " + serverUrl);

        return serverUrl;
    }
}
