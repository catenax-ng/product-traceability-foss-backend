package net.catenax.traceability.investigations.domain.model;

public final class Notification {
	private Long id;
	private String bpnNumber;
	private String edcUrl;

	public Notification(Long id, String bpnNumber, String edcUrl) {
		this.id = id;
		this.bpnNumber = bpnNumber;
		this.edcUrl = edcUrl;
	}

	public Long getId() {
		return id;
	}

	public String getBpnNumber() {
		return bpnNumber;
	}

	public void setEdcUrl(String edcUrl) {
		this.edcUrl = edcUrl;
	}

	public String getEdcUrl() {
		return edcUrl;
	}
}
