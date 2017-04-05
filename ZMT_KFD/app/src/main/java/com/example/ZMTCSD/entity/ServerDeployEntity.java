package com.example.ZMTCSD.entity;

import java.io.Serializable;

/**
 * 服务器的配置的 实体类
 */
public class ServerDeployEntity implements Serializable{
    private int ServerId;  //id
    private String  ServerCode;  //简称
    private String ServerName;  //名称
    private String ServerRemark; //备注
    private String ServerAddress; //地址

    public ServerDeployEntity() {
    }

    public ServerDeployEntity(int serverId, String serverCode, String serverName, String serverRemark, String serverAddress) {
        ServerId = serverId;
        ServerCode = serverCode;
        ServerName = serverName;
        ServerRemark = serverRemark;
        ServerAddress = serverAddress;
    }
    public int getServerId() {
        return ServerId;
    }

    public void setServerId(int serverId) {
        ServerId = serverId;
    }

    public String getServerCode() {
        return ServerCode;
    }

    public void setServerCode(String serverCode) {
        ServerCode = serverCode;
    }

    public String getServerName() {
        return ServerName;
    }

    public void setServerName(String serverName) {
        ServerName = serverName;
    }

    public String getServerRemark() {
        return ServerRemark;
    }

    public void setServerRemark(String serverRemark) {
        ServerRemark = serverRemark;
    }

    public String getServerAddress() {
        return ServerAddress;
    }

    public void setServerAddress(String serverAddress) {
        ServerAddress = serverAddress;
    }
}
