public class SecureCopyClient implements Command{
    private final String ip;
    private final String serverFilename;
    private final String clientFilename;

    public SecureCopyClient(String ip, String serverFilename, String clientFilename) {
        this.ip = ip;
        this.serverFilename = serverFilename;
        this.clientFilename = clientFilename;
    }

    @Override
    public void execute() {

    }
}
