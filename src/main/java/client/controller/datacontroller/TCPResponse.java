package client.controller.datacontroller;

public class TCPResponse {
    // 0 : game update
    private int mode;
    private String massage;

    public TCPResponse() {
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
