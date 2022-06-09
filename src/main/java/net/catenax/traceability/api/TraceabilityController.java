package net.catenax.traceability.api;

import net.catenax.traceability.assets.Asset;
import net.catenax.traceability.assets.AssetRepository;
import net.catenax.traceability.assets.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TraceabilityController {

	public record Invitation(String id, Instant createdDate, String name, String surname, String email, String phoneNumber) {
		public Invitation(Instant createdDate, String name, String surname, String email, String phoneNumber) {
			this(UUID.randomUUID().toString(), createdDate, name, surname, email, phoneNumber);
		}
	}

	private AssetRepository assetRepository;

	public TraceabilityController(AssetRepository assetRepository) {
		this.assetRepository = assetRepository;
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

	@PutMapping("/invite/{id}")
	void inviteUser(@PathVariable String id, @RequestBody Invitation request) {
	}

	@DeleteMapping("/invite/{id}")
	void removeInvitation(@PathVariable String id) {
	}

	@PostMapping("/invite")
	void inviteUser(@RequestBody Invitation invitation) {
	}

	@GetMapping("/invitations")
	PageResult<Invitation> getInvitations(Pageable pageable) {
		return null;
	}
}
