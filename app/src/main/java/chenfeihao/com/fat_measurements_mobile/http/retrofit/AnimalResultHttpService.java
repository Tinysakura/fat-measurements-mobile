package chenfeihao.com.fat_measurements_mobile.http.retrofit;

import chenfeihao.com.fat_measurements_mobile.http.common.ResponseView;
import chenfeihao.com.fat_measurements_mobile.pojo.dto.AnimalResultDto;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/17
 */
public interface AnimalResultHttpService {
    @GET("/a/data/measure")
    Observable<ResponseView<AnimalResultDto>> measure(@Query("animalDataId") Long animalDataId) throws Exception;

    @GET("/a/query/appoint/result")
    Observable<ResponseView<AnimalResultDto>> queryByAnimalDataId(@Query("animalDataId") Long animalDataId) throws Exception;
}
