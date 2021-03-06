package net.catenax.traceability.assets;

import net.catenax.traceability.api.assets.UpdateAsset;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AssetService {

	private final AssetRepository assetRepository;

	public AssetService(AssetRepository assetRepository) {
		this.assetRepository = assetRepository;
	}

	public Asset updateAsset(String assetId, UpdateAsset updateAsset) {
		Asset foundAsset = assetRepository.getAssetById(assetId);

		if (foundAsset == null) {
			throw new AssetNotFoundException("Asset with id %s was not found.".formatted(assetId));
		}

		Asset updatedAsset = foundAsset.update(updateAsset.qualityType());

		return assetRepository.save(updatedAsset);
	}

	public Map<String, Long> getAssetsCountryMap() {
		return assetRepository.getAssets().stream()
			.collect(Collectors.groupingBy(Asset::manufacturingCountry, Collectors.counting()));
	}
}
