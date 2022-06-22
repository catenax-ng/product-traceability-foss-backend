package net.catenax.traceability.api;

import net.catenax.traceability.assets.Asset;
import net.catenax.traceability.assets.AssetRepository;
import net.catenax.traceability.assets.PageResult;
import net.catenax.traceability.clients.openapi.bpn.BpnApiClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TraceabilityController {

	private AssetRepository assetRepository;

	private BpnApiClient bpnApiClient;

	public TraceabilityController(AssetRepository assetRepository, BpnApiClient bpnApiClient) {
		this.assetRepository = assetRepository;
		this.bpnApiClient = bpnApiClient;
	}

	@GetMapping("/assets")
	public PageResult<Asset> assets(Pageable pageable) {
		return assetRepository.getAssets(pageable);
	}

	@GetMapping("/assets/{assetId}")
	public Asset asset(@PathVariable String assetId) {
		return assetRepository.getAssetById(assetId);
	}

	@GetMapping("/assets/{assetId}/children/{childId}")
	public Asset asset(@PathVariable String assetId, @PathVariable String childId) {
		return assetRepository.getAssetByChildId(assetId, childId);
	}

	@GetMapping("/demo")
	public String assets() {
		return bpnApiClient.getBusinessPartner("BPNL000000000001", "").getBpn();

//		return "";
	}
}
