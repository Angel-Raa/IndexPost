package io.github.angel.raa.configuration;

import io.hypersistence.utils.spring.repository.BaseJpaRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(
        basePackages = "io.github.angel.raa.persistence.repository",
        value = {
                "io.hypersistence.utils.spring.repository",
                "io.github.angel.raa.persistence.repository"
        },
        repositoryBaseClass = BaseJpaRepositoryImpl.class
)
@Configuration
public class JpaConfiguration {
}
