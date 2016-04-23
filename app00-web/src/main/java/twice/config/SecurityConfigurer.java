package twice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.CompositeFilter;
import twice.filter.CsrfCookieFilter;
import twice.security.OauthResources;

import javax.inject.Inject;
import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    OAuth2ClientContext oAuth2Client;

//    @Autowired
//    private WebUserDetailsService userDetailsService;

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
//            .accessDeniedHandler(new CustomAccessDeniedHandler())
//            .authenticationEntryPoint(authenticationEntryPoint)
//        .and()
//            .rememberMe()
//            .rememberMeServices(rememberMeServices)
//            .rememberMeParameter("remember-me")
//            .key(jHipsterProperties.getSecurity().getRememberMe().getKey())
        .and()
            .formLogin()
            .loginProcessingUrl("/api/authentication")
////            .successHandler(ajaxAuthenticationSuccessHandler)
////            .failureHandler(ajaxAuthenticationFailureHandler)
            .usernameParameter("j_username")
            .passwordParameter("j_password")
            .permitAll()
        .and()
            .logout()
            .logoutSuccessUrl("/")
////            .logoutUrl("/api/logout")
////            .logoutSuccessHandler(ajaxLogoutSuccessHandler)
            .deleteCookies("TWSESSION", "CSRF-TOKEN")
            .permitAll()
//        .and()
//            .headers()
//            .frameOptions()
//            .disable()
        .and()
            .authorizeRequests()
            .antMatchers("/api/authenticate").permitAll();
        ;
        /* @formatter:on */
//        http.antMatcher("/**").authorizeRequests().anyRequest().permitAll();
    }

//    @Inject
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService);
////                .passwordEncoder(passwordEncoder());
//    }

//    @Bean
//    public FilterRegistrationBean oauth2ClientFilterRegistration
//            (OAuth2ClientContextFilter filter) {
//        return new FilterRegistrationBean() {{
//            setOrder(-100);
//            setFilter(filter);
//        }};
//    }

//    @Bean
//    @ConfigurationProperties("facebook")
//    OauthResources facebook() {
//        return new OauthResources();
//    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository =
                new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

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