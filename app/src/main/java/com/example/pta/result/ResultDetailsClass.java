package com.example.pta.result;

public class ResultDetailsClass {

    int serialNo;
    String playerName,number,time,winAmount;

    public ResultDetailsClass() {
    }

    public ResultDetailsClass(int serialNo, String playerName, String number, String time, String winAmount) {
        this.serialNo = serialNo;
        this.playerName = playerName;
        this.number = number;
        this.time = time;
        this.winAmount = winAmount;
    }

    public int getSerialNo() {
        return serialNo;
    }
    public String getPlayerName() {
        return playerName;
    }
    public String getNumber() {
        return number;
    }
    public String getTime() {
        return time;
    }
    public String getWinAmount() {
        return winAmount;
    }

}
