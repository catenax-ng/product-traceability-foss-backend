package net.catenax.traceability.investigations.domain.ports;

public interface EDCUrlProvider {
	String getEdcUrl(String bpn);
	String getSenderUrl();
	String getSenderBpn();
}
