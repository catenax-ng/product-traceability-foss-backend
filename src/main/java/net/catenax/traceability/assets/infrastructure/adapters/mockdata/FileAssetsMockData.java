package net.catenax.traceability.assets.infrastructure.adapters.mockdata;

import net.catenax.traceability.assets.domain.model.Asset;
import net.catenax.traceability.assets.domain.ports.AssetMockDataRepository;
import net.catenax.traceability.assets.infrastructure.adapters.feign.irs.model.AssetsConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileAssetsMockData implements AssetMockDataRepository {

	private final AssetsConverter assetsConverter;

	public FileAssetsMockData(AssetsConverter assetsConverter) {
		this.assetsConverter = assetsConverter;
	}

	@Override
	public List<Asset> mockData() {
		return assetsConverter.readAndConvertAssets();
	}
}
