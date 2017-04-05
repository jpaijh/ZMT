package com.example.ZMTCSD.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 账户的所有的信息
 */
public class MoreUserEntity implements Serializable {
    private int ServerId;
    private long Second;
    private String ServerUrl;
    private String LoginName;
    private UserLoginEntity LoginEntity;
    private List<UserClaimsEntity> ClaimsList;

    public MoreUserEntity() {
    }

    //判断如何是同一个用户
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoreUserEntity that = (MoreUserEntity) o;

        return (ServerUrl.equals(that.ServerUrl) && LoginName.equals(that.LoginName))  ?  true : false;
    }


    @Override
    public String toString() {
        return "MoreUserEntity{" +
                "ServerId='" + ServerId + '\'' +
                ", Second=" + Second +
                ", ServerUrl='" + ServerUrl + '\'' +
                ", LoginName='" + LoginName + '\'' +
                ", LoginEntity=" + LoginEntity +
                ", ClaimsList=" + ClaimsList +
                '}';
    }

    public int getServerId() {
        return ServerId;
    }

    public void setServerId(int serverId) {
        ServerId = serverId;
    }

    public long getSecond() {
        return Second;
    }

    public void setSecond(long second) {
        Second = second;
    }

    public String getServerUrl() {
        return ServerUrl;
    }

    public void setServerUrl(String serverUrl) {
        ServerUrl = serverUrl;
    }

    public String getLoginName() {
        return LoginName;
    }

    public void setLoginName(String loginName) {
        LoginName = loginName;
    }

    public UserLoginEntity getLoginEntity() {
        return LoginEntity;
    }

    public void setLoginEntity(UserLoginEntity loginEntity) {
        LoginEntity = loginEntity;
    }

    public List<UserClaimsEntity> getClaimsList() {
        return ClaimsList;
    }

    public void setClaimsList(List<UserClaimsEntity> claimsList) {
        ClaimsList = claimsList;
    }
}
