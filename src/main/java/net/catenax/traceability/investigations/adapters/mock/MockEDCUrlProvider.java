package net.catenax.traceability.investigations.adapters.mock;

import net.catenax.traceability.investigations.domain.ports.EDCUrlProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MockEDCUrlProvider implements EDCUrlProvider {

	private static final String DEFAULT_EDC_URL = "https://consumer-controlplane.dev.demo.ftcpro.co";

	private static final Map<String, String> bpnMapping = Map.of(
		"man-1", "https://consumer-controlplane.dev.demo.ftcpro.co",
		"BPNL00000003AXS3", "https://consumer-controlplane-3.dev.demo.ftcpro.co",
		"BPNL00000002AXS2", "https://consumer-controlplane.dev.demo.ftcpro.co",
		"BPNL00000001AXS1", "https://trace-consumer-controlplane.dev.demo.ftcpro.co"
	);

	@Value("${traceability.bpn}")
	private String senderBpn;

	public String getEdcUrl(String bpn) {
		return bpnMapping.getOrDefault(bpn, DEFAULT_EDC_URL);
	}

	@Override
	public String getSenderUrl() {
		return getEdcUrl(senderBpn);
	}

}
