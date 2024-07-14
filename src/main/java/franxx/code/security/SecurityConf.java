package franxx.code.security;

import franxx.code.security.model.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConf {

  @Autowired
  private MyUserDetailService myUserDetailService;

  @Autowired
  private JwtAuthFilter jwtAuthFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(registry -> {
      registry.requestMatchers("/home", "/register/**", "/auth").permitAll();
      registry.requestMatchers("/admin/**").hasRole("ADMIN");
      registry.requestMatchers("/user/**").hasRole("USER");
      registry.anyRequest().authenticated();
    })
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
        .build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return myUserDetailService;
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(myUserDetailService);
    provider.setPasswordEncoder(passwordEncoder());

    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return new ProviderManager(authenticationProvider());
  }

//  @Bean
//  public UserDetailsService userDetailsService() {
//    UserDetails normalUser = User.builder()
//        .username("me")
//        .password("$2a$12$wm8tQFFbYMSRaQtiB/io5.iaVMGlRgmMN7RpJhYcdk6/.dH6nIWee")
//        .roles("USER")
//        .build();
//    UserDetails adminUser = User.builder()
//        .username("admin")
//        .password("$2a$12$wm8tQFFbYMSRaQtiB/io5.iaVMGlRgmMN7RpJhYcdk6/.dH6nIWee")
//        .roles("USER", "ADMIN")
//        .build();
//
//    return new InMemoryUserDetailsManager(adminUser, normalUser);
//  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
