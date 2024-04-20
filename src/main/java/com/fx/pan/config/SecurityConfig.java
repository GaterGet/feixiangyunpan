package com.fx.pan.config;

import com.fx.pan.filter.JwtAuthenticationTokenFilter;
import com.fx.pan.handle.AccessDeniedHandlerImpl;
import com.fx.pan.handle.AuthenticationEntryPointImpl;
import com.fx.pan.handle.LogoutSuccessHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;


/**
 * @author leaving
 * @date 2022/1/13 17:23
 * @version 1.0
 */
@Configuration()
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private ObjectPostProcessor<Object> objectPostProcessor;

    /**
     * 自定义用户认证逻辑
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * token认证过滤器
     */
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    /**
     * 认证失败处理
     */
    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    /**
     * 授权失败处理
     */
    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    /**
     * 退出处理类
     */
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;


    // 创建BCryptPasswordEncoder 注入容器
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 关闭csrf(前后端分离项目要关闭此功能）
        http.csrf(csrf -> csrf.disable()).sessionManagement(withDefaults());
        http.authorizeHttpRequests(requests -> {
                    requests.requestMatchers(HttpMethod.GET, "/office/excel/online/data").anonymous()
                            .requestMatchers(HttpMethod.POST, "/user/login", "/user/register").permitAll()
                            .requestMatchers("/manage/**", "/admin/**").hasAnyAuthority("ADMIN", "SUPERADMIN", "admin",
                                    "superAdmin")
                            .requestMatchers(
                                    HttpMethod.GET,
                                    "/profile/**"
                            ).permitAll()
                            // 登录或未登录都能访问
                            .requestMatchers("/user/shareuser").permitAll()
                            .requestMatchers("/share/shareinfo").permitAll()
                            .requestMatchers("/share/checkextractioncode").permitAll()
                            .requestMatchers("/office/excel/check").permitAll()
                            .requestMatchers("/office/excel/online/data").permitAll()
                            .requestMatchers("/images/**").permitAll()
                            .requestMatchers("/image/**").permitAll()
                            // .antMatchers("/share/*").permitAll()
                            .requestMatchers("/user/userinfo").permitAll()
                            .requestMatchers("/user/captcha").permitAll()
                            .requestMatchers("/system/config").permitAll()
                            // 任意用户，认证之后都可以访问（除上面外的所有请求全部需要鉴权认证）
                            .anyRequest().authenticated();
                });
        // 添加 jwt 认证过滤器，在 UsernamePasswordAuthenticationFilter 过滤器之前
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 配置异常处理器
        http.exceptionHandling((exceptions) -> {
            exceptions
                    // 配置认证失败处理器
                    .authenticationEntryPoint(authenticationEntryPoint)
                    // 配置授权失败处理器
                    .accessDeniedHandler(accessDeniedHandler);
        });
        // 注销成功处理器
        http.logout((logout) -> {
            logout.logoutSuccessHandler(logoutSuccessHandler);
        });
        // 允许跨域
        http.cors(withDefaults());
        // http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOriginPattern("*"); // 允许任何源
        corsConfig.addAllowedMethod("*"); // 允许任何HTTP方法
        corsConfig.addAllowedHeader("*"); // 允许任何HTTP头
        corsConfig.setAllowCredentials(true); // 允许证书（cookies）
        corsConfig.setMaxAge(3600L); // 预检请求的缓存时间（秒）

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // 对所有路径应用这个配置
        return source;
    }
    @Bean
    RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_superAdmin > ROLE_admin ROLE_admin > ROLE_user";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }


    /**
     * 身份认证接口
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

}
