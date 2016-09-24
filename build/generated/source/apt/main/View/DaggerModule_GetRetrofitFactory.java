package View;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DaggerModule_GetRetrofitFactory implements Factory<Retrofit> {
  private final DaggerModule module;

  private final Provider<OkHttpClient> clientProvider;

  public DaggerModule_GetRetrofitFactory(
      DaggerModule module, Provider<OkHttpClient> clientProvider) {
    assert module != null;
    this.module = module;
    assert clientProvider != null;
    this.clientProvider = clientProvider;
  }

  @Override
  public Retrofit get() {
    return Preconditions.checkNotNull(
        module.getRetrofit(clientProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<Retrofit> create(
      DaggerModule module, Provider<OkHttpClient> clientProvider) {
    return new DaggerModule_GetRetrofitFactory(module, clientProvider);
  }
}
