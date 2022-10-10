package net.catenax.traceability


import net.catenax.traceability.assets.infrastructure.adapters.jpa.asset.JpaAssetsRepository
import net.catenax.traceability.assets.infrastructure.adapters.jpa.shelldescriptor.JpaShellDescriptorRepository
import net.catenax.traceability.infrastructure.jpa.investigation.JpaInvestigationRepository
import net.catenax.traceability.infrastructure.jpa.notification.JpaNotificationRepository
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

class DatabaseAwareSpecification extends Specification {

	@Autowired
	private JpaAssetsRepository assetsRepository

	@Autowired
	private JpaShellDescriptorRepository shellDescriptorRepository

	@Autowired
	private JpaInvestigationRepository investigationRepository

	@Autowired
	private JpaNotificationRepository notificationRepository

	def cleanup() {
		notificationRepository.deleteAll()
		investigationRepository.deleteAll()
		assetsRepository.deleteAll()
		shellDescriptorRepository.deleteAll()
	}
}
