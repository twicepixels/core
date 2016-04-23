package twice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import twice.filter.CsrfCookieFilter;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    OAuth2ClientContext oAuth2Client;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/app/**/*.{js,html}")
                .antMatchers("/bower_components/**")
                .antMatchers("/content/**")
                .antMatchers("/scripts/**")
                .antMatchers("/bower/**")
                .antMatchers("/i18n/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /* @formatter:off */
        http
            .csrf()
            .csrfTokenRepository(csrfTokenRepository())
        .and()
            .addFilterAfter(new CsrfCookieFilter(), CsrfFilter.class)
//            .addFilterBefore(ssoContextFilter(), BasicAuthenticationFilter.class)
            .exceptionHandling()
        .and()
            .authorizeRequests()
            .antMatchers("/api/authenticate").permitAll();
        ;
        /* @formatter:on */
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository =
                new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

//    @Bean
//    public FilterRegistrationBean oauth2ClientFilterRegistration
//            (OAuth2ClientContextFilter filter) {
//        return new FilterRegistrationBean() {{
//            setOrder(-100);
//            setFilter(filter);
//        }};
//    }
//
//    @Bean
//    @ConfigurationProperties("facebook")
//    OauthResources facebook() {
//        return new OauthResources();
//    }
//
//    private Filter ssoContextFilter() {
//        BiFunction<OauthResources, String, Filter> my = (client, path) ->
//                new OAuth2ClientAuthenticationProcessingFilter(path) {{
//                    String clientId = client.getResource().getClientId();
//                    String userInfoUri = client.getResource().getUserInfoUri();
//                    OAuth2ProtectedResourceDetails details = client.getClient();
//                    setRestTemplate(new OAuth2RestTemplate(details, oAuth2Client));
//                    setTokenServices(new UserInfoTokenServices(userInfoUri, clientId));
//                }};
//        List<Filter> filters = new ArrayList<>();
//        filters.add(my.apply(facebook(), "/login/facebook"));
//        return new CompositeFilter() {{
//            setFilters(filters);
//        }};
//    }
}