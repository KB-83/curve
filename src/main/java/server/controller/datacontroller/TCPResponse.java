package server.controller.datacontroller;

public class TCPResponse {
    // 0 : game update
    //1 : game ended massage = winner
    private int mode;
    private String massage;

    public TCPResponse() {
    }

    public TCPResponse(int mode, String massage) {
        this.mode = mode;
        this.massage = massage;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
}
