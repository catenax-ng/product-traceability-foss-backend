package net.catenax.traceability.common.properties;

import net.catenax.traceability.common.model.BPN;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("traceability")
public class TraceabilityProperties {

	private final BPN bpn;

	public TraceabilityProperties(String bpn) {
		this.bpn = new BPN(bpn);
	}

	public BPN getBpn() {
		return bpn;
	}
}
