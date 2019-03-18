package chenfeihao.com.fat_measurements_mobile.http.retrofit;

import chenfeihao.com.fat_measurements_mobile.http.common.ResponseView;
import chenfeihao.com.fat_measurements_mobile.pojo.dto.UserDto;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/17
 */
public interface UserHttpService {
    @POST("/a/register")
    Observable<ResponseView<UserDto>> rigister(@Body UserDto userDto);

    @GET("/a/login")
    Observable<ResponseView<ResponseView<UserDto>>> login(@Query("userName") String userName, @Query("userPassword") String userPassword);

    @Multipart
    @POST("/a/set/headportrait")
    Observable<ResponseView<String>>  uploadHeadPortrait(@Part MultipartBody.Part headPortrait);

    @GET("/a/get/headportrait")
    Observable<ResponseView<String>> getUserHeadPortrait(@Query("userName") String userName);
}
