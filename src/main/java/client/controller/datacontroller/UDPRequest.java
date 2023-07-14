package client.controller.datacontroller;

public class UDPRequest {
    private String userName;
    //0 = join
    //1 = start with an opponent
    //2 answare of other client to joining to game
    //21 yes
    //22 no

    private int requestNum;
    private String opponentName;

    public UDPRequest() {

    }

    public UDPRequest(String userName, int requestNum, String opponentName) {
        this.userName = userName;
        this.requestNum = requestNum;
        this.opponentName = opponentName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRequestNum() {
        return requestNum;
    }

    public void setRequestNum(int requestNum) {
        this.requestNum = requestNum;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

}

