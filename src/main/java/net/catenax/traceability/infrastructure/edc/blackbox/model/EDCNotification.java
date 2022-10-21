package net.catenax.traceability.infrastructure.edc.blackbox.model;

import net.catenax.traceability.investigations.domain.model.AffectedPart;
import net.catenax.traceability.investigations.domain.model.InvestigationStatus;
import net.catenax.traceability.investigations.domain.model.Notification;

import java.util.List;


public class EDCNotification {

	private Long notificationId;
	private String senderBPN;
	private String senderAddress;
	private String recipientBPN;
	private String information;
	private List<AffectedPart> listOfAffectedItems;
	private InvestigationStatus status;

	private NotificationType classification;


	public EDCNotification() {
	}

	public EDCNotification(String senderBPN, String senderEDC, Notification notification) {
		this.notificationId = notification.getId();
		this.senderBPN = senderBPN;
		this.senderAddress = senderEDC;
		this.recipientBPN = notification.getEdcUrl();
		this.information = notification.getDescription();
		this.listOfAffectedItems = notification.getAffectedParts();
		this.status = notification.getInvestigationStatus();
		this.classification = NotificationType.QMINVESTIGATION;
	}

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public String getSenderBPN() {
		return senderBPN;
	}

	public void setSenderBPN(String senderBPN) {
		this.senderBPN = senderBPN;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getRecipientBPN() {
		return recipientBPN;
	}

	public void setRecipientBPN(String recipientBPN) {
		this.recipientBPN = recipientBPN;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public InvestigationStatus getStatus() {
		return status;
	}

	public void setStatus(InvestigationStatus status) {
		this.status = status;
	}

	public List<AffectedPart> getListOfAffectedItems() {
		return listOfAffectedItems;
	}

	public void setListOfAffectedItems(List<AffectedPart> listOfAffectedItems) {
		this.listOfAffectedItems = listOfAffectedItems;
	}

	public NotificationType getClassification() {
		return classification;
	}

	public void setClassification(NotificationType classification) {
		this.classification = classification;
	}

}
