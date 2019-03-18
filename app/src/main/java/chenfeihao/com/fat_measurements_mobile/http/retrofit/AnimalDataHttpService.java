package chenfeihao.com.fat_measurements_mobile.http.retrofit;

import chenfeihao.com.fat_measurements_mobile.http.common.ResponseView;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/17
 */
public interface AnimalDataHttpService {
    @GET("/a/query/animal/data")
    Observable<ResponseView> getAnimalDataById(@Query(value = "id") Long id) throws Exception;
}
