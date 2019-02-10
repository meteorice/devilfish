package com.meteorice.devilfish.web.config;

import com.meteorice.devilfish.service.MyAuthenticationProvider;
import com.meteorice.devilfish.util.security.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    private MyAuthenticationProvider provider;

    private static String[] staticResoureces = new String[]{
            "/login", "/logout", "/AdminLTE/**", "/business/**", "/druid/**"
            , "/monitor/**"};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //取消Spring Boot不允许加载iframe问题解决
        http.headers().frameOptions().disable();

//        http.authorizeRequests().antMatchers("/**/*.html").permitAll();
        http.authorizeRequests().antMatchers("/**/*.css").permitAll();
        http.authorizeRequests().antMatchers("/**/*.svg").permitAll();
        http.authorizeRequests().antMatchers("/**/*.js").permitAll();
        http.authorizeRequests().antMatchers("/**/*.jpeg").permitAll();
        http.authorizeRequests().antMatchers("/**/*.png").permitAll();
        http.authorizeRequests().antMatchers("/user/addUser").permitAll();

        http.authorizeRequests().antMatchers("/login").permitAll()
                .antMatchers(staticResoureces)
                .permitAll()
                .and()
                // 定义当需要用户登录时候，转到的登录页面。
                .formLogin().loginPage("/login").loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=true")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").permitAll()
                .and()
                .authorizeRequests()    // 定义哪些URL需要被保护、哪些不需要被保护
                .anyRequest()           // 任何请求,登录后可以访问
                .authenticated();
//                .and()
        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        //将验证过程交给自定义验证工具
        auth.authenticationProvider(provider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MD5();
    }
}
