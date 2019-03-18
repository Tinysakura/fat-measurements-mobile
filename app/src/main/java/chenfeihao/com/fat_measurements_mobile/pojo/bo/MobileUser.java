package chenfeihao.com.fat_measurements_mobile.pojo.bo;

import chenfeihao.com.fat_measurements_mobile.pojo.dto.UserDto;

/**
 * 添加了一个用户信息有效时间的标识
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/18
 */
public class MobileUser extends UserDto {
    private Long validTime;

    public MobileUser(UserDto userDto, Long validTime) {
        this.setId(userDto.getId());
        this.setUserName(userDto.getUserName());
        this.setUserPassword(userDto.getUserPassword());
        this.setUserHeadPortrait(userDto.getUserHeadPortrait());
        this.validTime = validTime;
    }

    public MobileUser() {
    }

    public Long getValidTime() {
        return validTime;
    }

    public void setValidTime(Long validTime) {
        this.validTime = validTime;
    }
}
