package View;

import Domain.TwitchApi;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DaggerModule_GetTwitchApiFactory implements Factory<TwitchApi> {
  private final DaggerModule module;

  private final Provider<Retrofit> retrofitProvider;

  public DaggerModule_GetTwitchApiFactory(
      DaggerModule module, Provider<Retrofit> retrofitProvider) {
    assert module != null;
    this.module = module;
    assert retrofitProvider != null;
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public TwitchApi get() {
    return Preconditions.checkNotNull(
        module.getTwitchApi(retrofitProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<TwitchApi> create(
      DaggerModule module, Provider<Retrofit> retrofitProvider) {
    return new DaggerModule_GetTwitchApiFactory(module, retrofitProvider);
  }
}
