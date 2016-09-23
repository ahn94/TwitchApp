import Domain.TwitchApi;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Singleton;

/**
 * Created by Austin on 9/23/2016.
 */
@Module
public class DaggerModule {

    @Provides
    @Singleton
    public OkHttpClient OkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder().addHeader("Client-ID", "aaexx7qumq1mqaam3eqyu12ss0a38q0").build();
                    return chain.proceed(request);
                })
                .build();
    }

    @Provides
    @Singleton
    public Retrofit getRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.twitch.tv/kraken/")
                .build();
    }

    @Provides
    @Singleton
    public TwitchApi getTwitchApi(Retrofit retrofit) {
        return retrofit.create(TwitchApi.class);
    }
}
