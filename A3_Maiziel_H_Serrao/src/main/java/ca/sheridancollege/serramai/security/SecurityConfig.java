package ca.sheridancollege.serramai.security;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.context.annotation.Bean;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Enabling of jdbc authentication
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder());
	}

	// creation of sample accounts
	@Bean
	public JdbcUserDetailsManager jdbcUserDetailsManager() throws Exception {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
		jdbcUserDetailsManager.setDataSource(dataSource);

		List<GrantedAuthority> authBoss = new ArrayList<GrantedAuthority>();
		authBoss.add(new SimpleGrantedAuthority("ROLE_BOSS"));
		List<GrantedAuthority> authMinion = new ArrayList<GrantedAuthority>();
		authMinion.add(new SimpleGrantedAuthority("ROLE_MINION"));
		List<GrantedAuthority> authBoth = new ArrayList<GrantedAuthority>();
		authBoth.add(new SimpleGrantedAuthority("ROLE_BOSS"));
		authBoth.add(new SimpleGrantedAuthority("ROLE_MINION"));

		String encodedPassword = new BCryptPasswordEncoder().encode("pass");

		User u1 = new User("mrbean", encodedPassword, authBoss);
		User u2 = new User("maizy", encodedPassword, authMinion);
		User u3 = new User("admin", encodedPassword, authBoth);

		jdbcUserDetailsManager.createUser(u1);
		jdbcUserDetailsManager.createUser(u2);
		jdbcUserDetailsManager.createUser(u3);
		return jdbcUserDetailsManager;
	}

	// declares permissions of page and logout behaviour
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/user/**").hasAnyRole("MINION", "BOSS") // protected
				.antMatchers( // unprotected
						"/", "/js/**", "/css/**", "/img/**", "/**")
				.permitAll().anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll().and()
				.logout().invalidateHttpSession(true).clearAuthentication(true)
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/?logout").permitAll()
				.and().requiresChannel().anyRequest().requiresSecure();

	}

	// configures access to the static resources
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**").and().ignoring().antMatchers("/h2-console/**");
	}

}
