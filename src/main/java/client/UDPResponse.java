package client;

public class UDPResponse {
    //0 answare of login
    //1 playersArrayList
    private int type;
    private String massage;

    public UDPResponse() {
    }

    public UDPResponse(int type, String massage) {
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
