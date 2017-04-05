package com.example.ZMTCSD.entity;


/**
 * 登陆时的token的实体类
 */
public class UserLoginEntity  {
/**
 * "access_token": "AQAAANCMnd8BFdERjHoAwE_Cl-sBAAAAyuA1GORM_kK-yokao501TgAAAAACAAAAAAAQZgAAAAEAACAA
 * AAAG_cVr_fgag3lB7pXvqBpWaVC4Y5tHIxDuNgkWfhMi4AAAAAAOgAAAAAIAACAAAABxP5rAIPeriss7gtUbrXMpPiKxfVJI7
 * 32uaav2l3fwMcAAAADRE85fSJq9zK3jqR3QvmTNiflW5yDkW4T0eDIcMtIXyL3zvZOxqrfdOVZswhDdwVR6ZonVxnJK6xscan
 * JynZlu7j06rshQUOxcje0sYOzqjIi7tUT6Z3GzN6QfLFjtDTMFwjNIPpyodOBoUe1tk_Nud3tg335OXRIL7VYzlOZQ4WWWUy_
 * XV36B1c6rVY3ilD-QqIZvH-PL4s7_2q3MFSjLNFRcqCSmDTWVljUOh9BAtPNIvPPi8V0ih9w5lWjutz1AAAAAu6HXA0wuBiSe
 * ACwBMW9CghuMvvHHNdjYvfOg1pFRbdkLajW-4R1HFPo7W_o1b_nyuTSus4FX-ELsth_Q-0k76w",
 "token_type": "bearer",
 "expires_in": 1199,
 "refresh_token": "AQAAANCMnd8BFdERjHoAwE_Cl-sBAAAAyuA1GORM_kK-yokao501TgAAAAACAAAAAAAQZgAAAAEAACAAA
 AAjJjgP74CL4Jb83eJ3cWQaPhmp4jRvJgP4BjUZPJYraQAAAAAOgAAAAAIAACAAAAA7EnoP8IHvSpfi2stIwmmGtDuNS15B6S-x
 a-uK73wFE8AAAAAAPy87Zq9bOnpbDI9iE6SlwmuaDVhbnDqcsarKySOWizHr8iFCqkZWVeLYWBQXsjDL_SFBBjqubmuCJS6pR6j
 wOplU6pwQlua86EpVCOAj81DHww2MyLqnS5JKCZR5rFJmmvptXZ2WTQKRUYt6A27c3aOZ-yerxrwRyOVl8QXsJTT6l3T-4zlpFS
 rnrfSa2r9jGRL67rFmU2aDvN7PWJJOY24gpeq0VXhNYs77YDTfrmOHwyjvJqZzUtnJ-6nN4XdAAAAA2p3IfW86JqB1b0-jmmZLy
 5ZirqqRdkNf4724vkp9Hx-zRcQi6S-z6-mu1VWVtb6xTOqcPiQN--wxUIy3mY4X8g"
 */

    private String access_token;
    private String token_type;
    private int expires_in;
    private String refresh_token;

    @Override
    public String toString() {
        return "UserLoginEntity{" +
                "access_token='" + access_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                '}';
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
