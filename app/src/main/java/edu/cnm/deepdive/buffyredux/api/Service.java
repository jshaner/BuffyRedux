package edu.cnm.deepdive.buffyredux.api;

import android.os.Build.VERSION_CODES;
import androidx.annotation.RequiresApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.buffyredux.BuildConfig;
import edu.cnm.deepdive.buffyredux.model.MoviesResponse;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {


  @GET("movie/popular")
  Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

  @GET("movie/top_rated")
  Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

  @GET("movie/keyword_id")
  Call<MoviesResponse> getKeywordId(@Query("api_key") String apiKey);

  @GET("movie/person_id")
  Call<MoviesResponse> getPersonId(@Query("api_key") String apiKey);

  @RequiresApi(api = VERSION_CODES.N)
  static Service getInstance() {
    return InstanceHolder.INSTANCE;
  }

  class InstanceHolder {

    private static final Service INSTANCE;

    static {
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .create();
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
      OkHttpClient client = new OkHttpClient.Builder()
          .addInterceptor(interceptor)
          .readTimeout(30, TimeUnit.SECONDS)
          .build();
      Retrofit retrofit = new Retrofit.Builder()
          .addConverterFactory(GsonConverterFactory.create(gson))
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .client(client)
          .baseUrl(BuildConfig.BASE_URL)
          .build();
      INSTANCE = retrofit.create(Service.class);
    }

  }

}
