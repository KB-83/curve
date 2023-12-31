package server.controller.datacontroller;

import java.net.Socket;

public class UDPRequest {
    private String userName;
    //0 = join
    // 1 = start with an opponent
    private int requestNum;
    private String opponentName;

    public UDPRequest() {

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
