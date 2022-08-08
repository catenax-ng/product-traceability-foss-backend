package net.catenax.traceability.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.InstantSource;

@Configuration
public class InstantSourceConfig {

	@Bean
	public InstantSource instantSource() {
		return Clock.systemUTC();
	}
}
