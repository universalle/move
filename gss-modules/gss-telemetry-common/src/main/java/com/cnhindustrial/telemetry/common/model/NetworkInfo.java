package com.cnhindustrial.telemetry.common.model;

public class NetworkInfo {
    private int rssi;
    private int mnc;
    private String networkStatus;
    private String connection;
    private int mcc;
    private String operatorName;

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getMnc() {
        return mnc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }

    public String getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(String networkStatus) {
        this.networkStatus = networkStatus;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public int getMcc() {
        return mcc;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    @Override
    public String toString() {
        return "NetworkInfo{" +
                "rssi=" + rssi +
                ", mnc=" + mnc +
                ", networkStatus='" + networkStatus + '\'' +
                ", connection='" + connection + '\'' +
                ", mcc=" + mcc +
                ", operatorName='" + operatorName + '\'' +
                '}';
    }
}
