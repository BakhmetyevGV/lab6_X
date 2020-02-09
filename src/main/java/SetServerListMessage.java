
public class SetServerListMessage {

    private String[] serverList;

    public SetServerListMessage(String[] serverList) {
        this.serverList = serverList;
    }

    public String[] getServerList() {
        return serverList;
    }
}
