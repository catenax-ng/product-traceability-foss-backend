package net.catenax.traceability.common.support

trait BpnSupport implements BpnRepositoryProvider, AssetRepositoryProvider {

	void cachedBpnsForDefaultAssets() {
		List<String> assetIds = assetsConverter().readAndConvertAssets().collect { it.manufacturerId }
		Map<String, String> bpnMappings = new HashMap<>()

		for (int i = 0; i < assetIds.size(); i++) {
			bpnMappings.put(assetIds.get(i), "Manufacturer Name $i".toString())
		}

		bpnRepository().updateManufacturers(bpnMappings)
	}
}
