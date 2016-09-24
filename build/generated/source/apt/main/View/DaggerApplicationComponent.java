package View;

import Domain.TwitchApi;
import dagger.MembersInjector;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DaggerApplicationComponent implements ApplicationComponent {
  private Provider<OkHttpClient> OkHttpClientProvider;

  private Provider<Retrofit> getRetrofitProvider;

  private Provider<TwitchApi> getTwitchApiProvider;

  private MembersInjector<AppController> appControllerMembersInjector;

  private DaggerApplicationComponent(Builder builder) {
    assert builder != null;
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static ApplicationComponent create() {
    return builder().build();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {

    this.OkHttpClientProvider =
        DoubleCheck.provider(DaggerModule_OkHttpClientFactory.create(builder.daggerModule));

    this.getRetrofitProvider =
        DoubleCheck.provider(
            DaggerModule_GetRetrofitFactory.create(builder.daggerModule, OkHttpClientProvider));

    this.getTwitchApiProvider =
        DoubleCheck.provider(
            DaggerModule_GetTwitchApiFactory.create(builder.daggerModule, getRetrofitProvider));

    this.appControllerMembersInjector = AppController_MembersInjector.create(getTwitchApiProvider);
  }

  @Override
  public void inject(AppController controller) {
    appControllerMembersInjector.injectMembers(controller);
  }

  public static final class Builder {
    private DaggerModule daggerModule;

    private Builder() {}

    public ApplicationComponent build() {
      if (daggerModule == null) {
        this.daggerModule = new DaggerModule();
      }
      return new DaggerApplicationComponent(this);
    }

    public Builder daggerModule(DaggerModule daggerModule) {
      this.daggerModule = Preconditions.checkNotNull(daggerModule);
      return this;
    }
  }
}
