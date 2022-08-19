package net.catenax.traceability.assets.infrastructure.adapters.scheduler;

import net.catenax.traceability.assets.application.RegistryFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ShellDescriptorRefreshJob {

	private final RegistryFacade registryFacade;
	private final String defaultBpn;

	public ShellDescriptorRefreshJob(RegistryFacade registryFacade, @Value("${feign.registryApi.defaultBpn}") String defaultBpn) {
		this.registryFacade = registryFacade;
		this.defaultBpn = defaultBpn;
	}

	@Scheduled(cron = "0 0 */2 * * ?", zone = "Europe/Berlin")
	void refresh() {
		registryFacade.loadShellDescriptorsFor(defaultBpn);
	}
}
