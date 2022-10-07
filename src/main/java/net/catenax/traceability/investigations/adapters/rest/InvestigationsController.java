package net.catenax.traceability.investigations.adapters.rest;

import net.catenax.traceability.investigations.domain.service.InvestigationsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERVISOR', 'ROLE_USER')")
public class InvestigationsController {

	private final InvestigationsService investigationsService;

	public InvestigationsController(InvestigationsService investigationsService) {
		this.investigationsService = investigationsService;
	}

	@PostMapping("/investigations")
	public void investigateAssets(@RequestBody @Valid StartInvestigationRequest request) {
		investigationsService.startInvestigation(request.partIds(), request.description());
	}

}
