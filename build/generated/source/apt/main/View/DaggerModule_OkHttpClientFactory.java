package View;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import okhttp3.OkHttpClient;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DaggerModule_OkHttpClientFactory implements Factory<OkHttpClient> {
  private final DaggerModule module;

  public DaggerModule_OkHttpClientFactory(DaggerModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public OkHttpClient get() {
    return Preconditions.checkNotNull(
        module.OkHttpClient(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<OkHttpClient> create(DaggerModule module) {
    return new DaggerModule_OkHttpClientFactory(module);
  }
}
