package com.dev.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobApplication.class, args);
	}

    /*
    @Bean
    CommandLineRunner createAdmin(UserRepository repository, PasswordEncoder encoder){
        return args -> {
            if(repository.existsByEmail(do@dev.com)){
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("do@dev.com");
                admin.setPassword(passwordEncoder.encode("admin123")); // mã hóa mật khẩu
                admin.setRole(UserRole.ADMIN);
                admin.setStatus(UserStatus.ACTIVE);
                admin.setCreatedAt(LocalDate.now());
                userRepository.save(admin);
            }
        }
    }
    */
}
