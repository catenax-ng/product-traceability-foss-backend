package net.catenax.traceability.clients.bpn;

import feign.FeignException;
import net.catenax.traceability.assets.BpnRepository;
import net.catenax.traceability.clients.cache.bpn.BpnCache;
import net.catenax.traceability.clients.cache.bpn.BpnMapping;
import net.catenax.traceability.clients.openapi.bpn.BpnApiClient;
import net.catenax.traceability.clients.openapi.bpn.model.BusinessPartnerResponse;
import net.catenax.traceability.clients.openapi.bpn.model.NameResponse;
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
		Optional<String> companyNameResult = bpnCache.getCompanyName(manufacturerId);

		if (companyNameResult.isPresent()) {
			return companyNameResult;
		}

		final BusinessPartnerResponse businessPartner;

		try {
			businessPartner = bpnApiClient.getBusinessPartner(manufacturerId, BPN_TYPE);
		} catch (FeignException e) {
			logger.error("Exception during calling bpn business partner api", e);

			return Optional.empty();
		}

		List<NameResponse> names = businessPartner.getNames();

		if (names.isEmpty()) {
			logger.warn("Names not found for {} BPN", manufacturerId);

			return Optional.empty();
		}

		Optional<String> firstNotEmptyManufacturerName = names.stream()
			.filter(it -> StringUtils.isNotBlank(it.getValue()))
			.findFirst()
			.map(NameResponse::getValue);

		firstNotEmptyManufacturerName.ifPresentOrElse(
			manufacturerName -> bpnCache.put(new BpnMapping(manufacturerId, manufacturerName)),
			() -> logger.warn("Manufacturer name not found for {} id", manufacturerId)
		);

		return firstNotEmptyManufacturerName;
	}
}
