package net.catenax.traceability.assets.infrastructure.adapters.rest.assets;

import net.catenax.traceability.assets.domain.RegistryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class RegistryController {
	private final RegistryService registryService;

	public RegistryController(RegistryService registryService) {
		this.registryService = registryService;
	}

	@GetMapping("/registry/fetch/{bpn}")
	public void assets(@PathVariable String bpn) {
		registryService.loadShellDescriptorsFor(bpn);
	}
}
