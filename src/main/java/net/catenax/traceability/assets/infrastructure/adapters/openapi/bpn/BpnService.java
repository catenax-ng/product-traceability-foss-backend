package net.catenax.traceability.assets.infrastructure.adapters.openapi.bpn;

import feign.FeignException;
import net.catenax.traceability.assets.domain.BpnRepository;
import net.catenax.traceability.assets.infrastructure.adapters.cache.bpn.BpnCache;
import net.catenax.traceability.assets.infrastructure.adapters.cache.bpn.BpnMapping;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BpnService implements BpnRepository {

	private static final Logger logger = LoggerFactory.getLogger(BpnService.class);

	private static final String BPN_TYPE = "BPN";

	private final BpnCache bpnCache;
	private final BpnApiClient bpnApiClient;

	public BpnService(BpnCache bpnCache, BpnApiClient bpnApiClient) {
		this.bpnCache = bpnCache;
		this.bpnApiClient = bpnApiClient;
	}

	@Override
	public Optional<String> findManufacturerName(String manufacturerId) {
		return Optional.of("OEM A");
	}
}
