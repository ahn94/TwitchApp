package View;

import Domain.TwitchApi;
import dagger.MembersInjector;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AppController_MembersInjector implements MembersInjector<AppController> {
  private final Provider<TwitchApi> twitchApiProvider;

  public AppController_MembersInjector(Provider<TwitchApi> twitchApiProvider) {
    assert twitchApiProvider != null;
    this.twitchApiProvider = twitchApiProvider;
  }

  public static MembersInjector<AppController> create(Provider<TwitchApi> twitchApiProvider) {
    return new AppController_MembersInjector(twitchApiProvider);
  }

  @Override
  public void injectMembers(AppController instance) {
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    instance.twitchApi = twitchApiProvider.get();
  }

  public static void injectTwitchApi(
      AppController instance, Provider<TwitchApi> twitchApiProvider) {
    instance.twitchApi = twitchApiProvider.get();
  }
}
