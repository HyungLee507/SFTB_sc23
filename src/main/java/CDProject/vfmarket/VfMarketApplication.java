package CDProject.vfmarket;

import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class VfMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(VfMarketApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        // 추후 세션에 저장된 회원 정보를 통해서 수정 -> 굳이 필요할지는 고려해야됨.
        return () -> Optional.of(UUID.randomUUID().toString());
    }

}
