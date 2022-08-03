package net.catenax.traceability.investigations.infrastructure.adapters.rest;

import net.catenax.traceability.common.security.InjectedKeycloakAuthentication;
import net.catenax.traceability.common.security.KeycloakAuthentication;
import net.catenax.traceability.investigations.application.InvestigationsFacade;
import net.catenax.traceability.investigations.domain.model.QualityInvestigationId;
import net.catenax.traceability.investigations.domain.service.AddQualityInvestigation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/investigations")
public class InvestigationsController {

	private final InvestigationsFacade investigationsFacade;

	public InvestigationsController(InvestigationsFacade investigationsFacade) {
		this.investigationsFacade = investigationsFacade;
	}

	@PostMapping
	AddQualityInvestigationResponse addQualityInvestigation(
		@Valid @RequestBody AddQualityInvestigationRequest request,
		@InjectedKeycloakAuthentication KeycloakAuthentication keycloakAuthentication
	) {
		AddQualityInvestigation addQualityInvestigation = new AddQualityInvestigation(
			keycloakAuthentication.getUserId(),
			request.partIds(),
			request.description()
		);

		QualityInvestigationId qualityInvestigationId = investigationsFacade.handle(addQualityInvestigation);

		return new AddQualityInvestigationResponse(qualityInvestigationId.raw());
	}
}
