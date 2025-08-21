package org.demo.security.authentication.config;

import jakarta.servlet.Filter;
import java.util.List;
import org.demo.security.authentication.handler.exception.CustomAuthenticationExceptionHandler;
import org.demo.security.authentication.handler.exception.CustomAuthorizationExceptionHandler;
import org.demo.security.authentication.handler.exception.CustomSecurityExceptionHandler;
import org.demo.security.authentication.handler.login.LoginFailHandler;
import org.demo.security.authentication.handler.login.LoginSuccessHandler;
import org.demo.security.authentication.handler.login.sms.SmsAuthenticationFilter;
import org.demo.security.authentication.handler.login.sms.SmsAuthenticationProvider;
import org.demo.security.authentication.handler.login.username.UsernameAuthenticationFilter;
import org.demo.security.authentication.handler.login.username.UsernameAuthenticationProvider;
import org.demo.security.authentication.handler.resourceapi.openapi1.MyJwtAuthenticationFilter;
import org.demo.security.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class CustomWebSecurityConfig {
  private final ApplicationContext applicationContext;


  public CustomWebSecurityConfig(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * 认证失败时，会执行这个类中的方法。将原因告知客户端
   */
  private final AuthenticationEntryPoint authenticationExceptionHandler = new CustomAuthenticationExceptionHandler();
  /**
   * 认证成功(Authentication), 但无权访问时,或者SpringSecurity框架捕捉到 AccessDeniedException时会执行这个类中的方法,将原因告知客户端
   */
  private final AccessDeniedHandler authorizationExceptionHandler = new CustomAuthorizationExceptionHandler();

  /**
   * 项目中全局的异常处理,全局中出了异常都会执行这个类中的方法,将原因告知客户端
   */
  private final Filter globalSpringSecurityExceptionHandler = new CustomSecurityExceptionHandler();

  /**
   * 密码加密使用的编码器
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

    /**
     * 自定义跨域配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");  //设置允许跨域请求的域名
        config.addAllowedMethod("*");  //设置允许的请求方式
        config.addAllowedHeader("*");  //设置允许的header属性
        config.setAllowCredentials(true);  //是否允许cookie
        config.setMaxAge(3600L);  //跨域允许时间

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);  //设置允许跨域的路径
        return source;
    }

  /** 禁用不必要的默认filter，处理异常响应内容 */
  private void commonHttpSetting(HttpSecurity http) throws Exception {
    http.formLogin(AbstractHttpConfigurer::disable) //关闭默认的两种登录方式
        .httpBasic(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)  //关闭推出登录之后，要重定向的功能
        .sessionManagement(AbstractHttpConfigurer::disable) //关闭session功能
        .csrf(AbstractHttpConfigurer::disable)  //关闭CSRF防护功能
        .cors((cors) -> cors.configurationSource(corsConfigurationSource())) //允许跨域
        // requestCache用于重定向，前后端分析项目无需重定向，requestCache也用不上
        .requestCache(cache -> cache
            .requestCache(new NullRequestCache())
        )
        .anonymous(AbstractHttpConfigurer::disable);  // 无需给用户一个匿名身份

    // 处理 SpringSecurity 异常响应结果。响应数据的结构，改成业务统一的JSON结构。不要框架默认的响应结构
    http.exceptionHandling(exceptionHandling ->
        exceptionHandling
            // 认证失败异常
            .authenticationEntryPoint(authenticationExceptionHandler)
            // 鉴权失败异常
            .accessDeniedHandler(authorizationExceptionHandler)
    );
    // 其他未知异常. 尽量提前加载。
    http.addFilterBefore(globalSpringSecurityExceptionHandler, SecurityContextHolderFilter.class);
  }


  /**
   * 登录的过滤器链
   */
  @Bean
  public SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception {
    commonHttpSetting(http);
    // 使用securityMatcher限定当前配置作用的路径
    http.securityMatcher("/authentication/login/*")
        .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());

    LoginSuccessHandler loginSuccessHandler = applicationContext.getBean(LoginSuccessHandler.class);
    LoginFailHandler loginFailHandler = applicationContext.getBean(LoginFailHandler.class);

    //用户名、密码登录认证过滤器
    UsernameAuthenticationFilter usernameLoginFilter = new UsernameAuthenticationFilter(
        new AntPathRequestMatcher("/authentication/login/username", HttpMethod.POST.name()),
        new ProviderManager(
            List.of(applicationContext.getBean(UsernameAuthenticationProvider.class))),
        loginSuccessHandler,
        loginFailHandler);
    http.addFilterBefore(usernameLoginFilter, UsernamePasswordAuthenticationFilter.class);


    // 短信验证码 登录登录认证过滤器
    SmsAuthenticationFilter smsLoginFilter = new SmsAuthenticationFilter(
        new AntPathRequestMatcher("/authentication/login/sms", HttpMethod.POST.name()),
        new ProviderManager(
            List.of(applicationContext.getBean(SmsAuthenticationProvider.class))),
        loginSuccessHandler,
        loginFailHandler);

    http.addFilterBefore(smsLoginFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  /**
   * 无需登登录，直接放行的过滤器链
   */
  @Bean
  public SecurityFilterChain defaultApiFilterChain3(HttpSecurity http) throws Exception {
    commonHttpSetting(http);
    http.securityMatchers(matcher ->matcher.requestMatchers("/druid/**","/Sms/**","/User/updatePwd","/User/individualRegist","/User/legalPersonRegist"))
            .authorizeHttpRequests(author->author.anyRequest().permitAll());
    return http.build();
  }

  /**
   * 需要校验是否登录过
   */
  @Bean
  public SecurityFilterChain defaultApiFilterChain(HttpSecurity http) throws Exception {
    commonHttpSetting(http);
    MyJwtAuthenticationFilter myJwtAuthenticationFilter=new MyJwtAuthenticationFilter(applicationContext.getBean(TokenService.class),applicationContext.getBean(UserMapper.class));
    http.addFilterBefore(myJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
