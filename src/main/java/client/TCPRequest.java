package client;

public class TCPRequest extends Request{
    // 0 : send Name
    private int type;
    private String username;

    public TCPRequest() {
    }

    public TCPRequest(int type, String username) {
        this.type = type;
        this.username = username;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
