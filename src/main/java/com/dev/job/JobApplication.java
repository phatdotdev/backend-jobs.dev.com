package com.dev.job;

import com.dev.job.entity.user.User;
import com.dev.job.entity.user.UserRole;
import com.dev.job.entity.user.UserStatus;
import com.dev.job.repository.User.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class JobApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobApplication.class, args);
	}


    @Bean
    CommandLineRunner createAdmin(UserRepository repository, PasswordEncoder encoder){
        return args -> {
            if(!repository.existsByEmail("admin.do@gmail.com")){
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin.do@gmail.com");
                admin.setPassword(encoder.encode("12345678"));
                admin.setRole(UserRole.ADMIN);
                admin.setStatus(UserStatus.ACTIVE);
                admin.setCreatedAt(LocalDateTime.now());
                repository.save(admin);
            }
        };
    }

}
