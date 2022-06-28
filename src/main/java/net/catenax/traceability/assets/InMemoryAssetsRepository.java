package net.catenax.traceability.assets;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryAssetsRepository implements AssetRepository {

	private final Map<String, Asset> assets;

	private final BpnRepository bpnRepository;

	public InMemoryAssetsRepository(BpnRepository bpnRepository) {
		this.bpnRepository = bpnRepository;
		this.assets = AssetRandomizer.generateAssets(1000);
	}

	@Override
	public Asset getAssetById(String assetId) {
		return Optional.ofNullable(assets.get(assetId))
			.map(this::addManufacturerName)
			.orElse(null);
	}

	@Override
	public Asset getAssetByChildId(String assetId, String childId) {
		return assets.get(assetId).childDescriptions().stream()
			.filter(childDescription -> childDescription.id().equals(childId))
			.map(childDescription -> assets.get(childDescription.id()))
			.findFirst()
			.map(this::addManufacturerName)
			.orElse(null);
	}

	@Override
	public List<Asset> getAssets() {
		return assets.values().stream()
			.map(this::addManufacturerName)
			.toList();
	}

	@Override
	public PageResult<Asset> getAssets(Pageable pageable) {
		PagedListHolder<Asset> pageListHolder = new PagedListHolder<>(new ArrayList<>(getAssets()));
		Sort sort = pageable.getSortOr(Sort.unsorted());

		if (sort.isSorted()) {
			sort.stream().findFirst().ifPresent(order -> {
				pageListHolder.setSort(new MutableSortDefinition(order.getProperty(), true, order.isAscending()));
				pageListHolder.resort();
			});
		}

		return new PageResult<>(pageListHolder);
	}

	private Asset addManufacturerName(Asset asset) {
		return bpnRepository.findManufacturerName(asset.manufacturerId())
			.map(asset::withManufacturerName)
			.orElse(asset);
	}
}
