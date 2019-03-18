package chenfeihao.com.fat_measurements_mobile.pojo.dto;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/17
 */
public class UserDto {
    private Long id;
    private String userName;
    private String userPassword;
    private String userHeadPortrait;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserHeadPortrait() {
        return userHeadPortrait;
    }

    public void setUserHeadPortrait(String userHeadPortrait) {
        this.userHeadPortrait = userHeadPortrait;
    }
}