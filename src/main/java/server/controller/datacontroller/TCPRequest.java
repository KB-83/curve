package server.controller.datacontroller;

public class TCPRequest {
    private int type;
    private String massage;

    public TCPRequest() {
    }

    public TCPRequest(int type, String massage) {
        this.type = type;
        this.massage = massage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
}
