package twice.config;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import twice.config.locale.AngularCookieLocaleResolver;

import java.util.Locale;

@Configuration
public class LocaleConfigurer extends WebMvcConfigurerAdapter implements EnvironmentAware {

    private RelaxedPropertyResolver propertyResolver;

    @Bean(name = "localeResolver")
    public LocaleResolver getLocaleResolver() {
        return new AngularCookieLocaleResolver() {{
            setDefaultLocale(new Locale("en"));
            setCookieName("NG_TRANSLATE_LANG_KEY");
        }};
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleChangeInterceptor() {{
            setParamName("lang");
        }});
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver
                (environment, "spring.messages.");
    }
}
