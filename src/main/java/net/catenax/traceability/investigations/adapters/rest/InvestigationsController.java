package net.catenax.traceability.investigations.adapters.rest;

import net.catenax.traceability.investigations.domain.service.InvestigationsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/investigations")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERVISOR', 'ROLE_USER')")
public class InvestigationsController {

	private final InvestigationsService investigationsService;

	public InvestigationsController(InvestigationsService investigationsService) {
		this.investigationsService = investigationsService;
	}

	@PostMapping
	public void investigateAssets(@RequestBody @Valid StartInvestigationRequest request) {
		investigationsService.startInvestigation(request.partIds(), request.description());
	}

	@PutMapping("/{investigationId}/status")
	public void updateInvestigationStatus(@PathVariable Long investigationId,
										  @Valid @RequestBody UpdateInvestigationStatusRequest updateStatusRequest
	) {
		investigationsService.updateInvestigationStatus(investigationId, updateStatusRequest.status());
	}

}
