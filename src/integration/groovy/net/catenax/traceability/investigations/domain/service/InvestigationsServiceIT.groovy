package net.catenax.traceability.investigations.domain.service

import net.catenax.traceability.IntegrationSpecification
import net.catenax.traceability.assets.domain.model.QualityType
import net.catenax.traceability.common.support.AssetsSupport
import net.catenax.traceability.common.support.BpnSupport
import net.catenax.traceability.common.support.InvestigationsSupport
import net.catenax.traceability.common.support.IrsApiSupport
import net.catenax.traceability.common.support.NotificationsSupport
import net.catenax.traceability.investigations.domain.model.AffectedPart
import net.catenax.traceability.investigations.domain.model.InvestigationStatus
import net.catenax.traceability.investigations.domain.model.Notification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

class InvestigationsServiceIT extends IntegrationSpecification implements IrsApiSupport, AssetsSupport, InvestigationsSupport, NotificationsSupport, BpnSupport {

	@Autowired
	InvestigationsService investigationsService

	@Transactional
	def "should receive notification"() {
		given:
			defaultAssetsStored()

			Notification notification = new Notification(
				null, "bpn", "edcUrl", null, "description",
				InvestigationStatus.RECEIVED,
				[new AffectedPart("urn:uuid:d387fa8e-603c-42bd-98c3-4d87fef8d2bb", QualityType.MAJOR)]
			)

		when:
			investigationsService.receiveNotification(notification)

		then:
			assertInvestigationsSize(1)
			assertNotificationsSize(1)
	}

}
