package chenfeihao.com.fat_measurements_mobile.http.retrofit;

import chenfeihao.com.fat_measurements_mobile.http.common.ResponseView;
import chenfeihao.com.fat_measurements_mobile.pojo.dto.UserDto;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/17
 */
public interface UserHttpService {
    @POST("/a/register")
    Observable<ResponseView> rigister(@Body UserDto userDto);

    @GET("/a/login")
    Observable<ResponseView> login(@Query("userName") String userName, @Query("userPassword") String userPassword);
}
