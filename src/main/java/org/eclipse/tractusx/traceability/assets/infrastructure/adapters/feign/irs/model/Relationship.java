package org.eclipse.tractusx.traceability.assets.infrastructure.adapters.feign.irs.model;

record Relationship(String catenaXId, LinkedItem linkedItem) {
	String childCatenaXId() {
		return linkedItem.childCatenaXId();
	}
}

record LinkedItem(
	String childCatenaXId
) {}
