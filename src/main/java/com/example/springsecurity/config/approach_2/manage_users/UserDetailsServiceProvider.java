package com.example.springsecurity.config.approach_2.manage_users;

import com.example.springsecurity.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class UserDetailsServiceProvider {

    private final DataSource dataSource;

    private final UserRepository userRepository;

    public UserDetailsServiceProvider(DataSource dataSource, UserRepository userRepository) {
        this.dataSource = dataSource;
        this.userRepository = userRepository;
    }

    //    @Bean
//    public UserDetailsService userDetailsServiceInMemory() {
//        return new InMemoryUserDetailsManager();
//    }

    @Bean
    public UserDetailsService userDetailsService() {
//        var userDetailsService = new JdbcUserDetailsManager(this.dataSource);
//        userDetailsService.setCreateUserSql("custom sql");
        var userDetailsService = new UserDetailsServiceCustom(this.userRepository);
        return userDetailsService;
    }


    // Todo: Custom UserDetailsService with LDAP

}
