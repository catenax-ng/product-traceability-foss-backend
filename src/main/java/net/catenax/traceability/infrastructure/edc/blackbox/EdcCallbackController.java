package net.catenax.traceability.infrastructure.edc.blackbox;

import net.catenax.traceability.infrastructure.edc.blackbox.cache.EndpointDataReference;
import net.catenax.traceability.infrastructure.edc.blackbox.cache.InMemoryEndpointDataReferenceCache;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
@RequestMapping("/endpoint-data-reference")
public class EdcCallbackController {

	private final InMemoryEndpointDataReferenceCache endpointDataReferenceCache;

	public EdcCallbackController(InMemoryEndpointDataReferenceCache endpointDataReferenceCache) {
		this.endpointDataReferenceCache = endpointDataReferenceCache;
	}

	@PostMapping
	public void receiveEdcCallback(@RequestBody EndpointDataReference dataReference) {
		var contractAgreementId = dataReference.getProperties().get("cid");
		endpointDataReferenceCache.put(contractAgreementId, dataReference);
	}
}
