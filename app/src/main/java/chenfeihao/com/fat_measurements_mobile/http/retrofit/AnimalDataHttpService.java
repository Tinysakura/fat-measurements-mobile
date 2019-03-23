package chenfeihao.com.fat_measurements_mobile.http.retrofit;

import chenfeihao.com.fat_measurements_mobile.http.common.ResponseView;
import chenfeihao.com.fat_measurements_mobile.pojo.dto.AnimalDataDto;
import chenfeihao.com.fat_measurements_mobile.pojo.dto.AnimalResultDto;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/17
 */
public interface AnimalDataHttpService {
    @GET("/a/query/animal/data")
    Observable<ResponseView> getAnimalDataById(@Query(value = "id") Long id) throws Exception;

    @POST("/a/submit/form/animal/data")
    Observable<ResponseView<AnimalDataDto>> saveAnimalDataForm(@Body RequestBody Body) throws Exception;

}
