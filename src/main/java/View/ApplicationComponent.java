package View;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {DaggerModule.class})
public interface ApplicationComponent {
    void inject(AppController controller);
}
