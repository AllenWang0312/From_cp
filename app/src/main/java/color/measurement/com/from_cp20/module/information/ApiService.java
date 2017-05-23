package color.measurement.com.from_cp20.module.information;

import color.measurement.com.from_cp20.module.information.javaBean.NewsGson;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wpc on 2017/2/16.
 */

public interface ApiService {


    @GET("social/")
    Observable<NewsGson> getNewsData(@Query("key") String key, @Query("num") String num, @Query("page") int page);
}
