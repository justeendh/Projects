package com.common.irendersql.irender.sql.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("repository")
@EntityScan("repository.entity")
@Slf4j
public class ModuleConfiguration {

}
