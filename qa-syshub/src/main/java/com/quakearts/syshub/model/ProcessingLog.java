package com.quakearts.syshub.model;

import java.io.Serializable;

// Generated Jul 18, 2011 3:16:30 PM by Hibernate Tools 3.3.0.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="processing_log", schema="dbo")
public class ProcessingLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2529467718157449441L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long logID;
	@Column(nullable=false, length=50)
	private String mid;
	@Column(nullable=false, length=100)
	private String recipient;
	@Enumerated(EnumType.ORDINAL)
	@Column(nullable=true)
	private LogType type;
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	private AgentConfiguration agentConfiguration;
	@Column(nullable=false, length=200)
	private String statusMessage;
	@Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(nullable=false, columnDefinition="blob(5M)")
	private byte[] messageData;
	@Column(nullable=false)
	private long retries;
	@Column(nullable=false)
	private boolean error;
	@Column(nullable=false)
	private Date logDt;
	@OneToMany(mappedBy="processingLog", fetch=FetchType.LAZY, cascade={CascadeType.REMOVE})
	private Set<TransactionLog> transactionLogs = new HashSet<>();

	public static enum LogType {
		INFO,
		ERROR,
		QUEUED,
		STORED,
		RESENT
	}
	
	public ProcessingLog() {
	}

	public ProcessingLog(Date logDt) {
		this.logDt = logDt;
	}

	public ProcessingLog(String mid, String recipient, LogType type,
			AgentConfiguration agentConfiguration,
			String statusMessage, byte[] messageData, long retries,
			boolean error, Date logDt) {
		this.mid = mid;
		this.recipient = recipient;
		this.type = type;
		this.agentConfiguration = agentConfiguration;
		this.statusMessage = statusMessage;
		this.messageData = messageData;
		this.retries = retries;
		this.error = error;
		this.logDt = logDt;
	}

	public long getLogID() {
		return this.logID;
	}

	public void setLogID(long logID) {
		this.logID = logID;
	}

	public String getMid() {
		return this.mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getRecipient() {
		return this.recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public LogType getType() {
		return this.type;
	}

	public void setType(LogType type) {
		this.type = type;
	}

	public AgentConfiguration getAgentConfiguration() {
		return agentConfiguration;
	}

	public void setAgentConfiguration(AgentConfiguration agentConfiguration) {
		this.agentConfiguration = agentConfiguration;
	}

	public String getStatusMessage() {
		return this.statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public byte[] getMessageData() {
		return this.messageData;
	}

	public void setMessageData(byte[] messageData) {
		this.messageData = messageData;
	}

	public long getRetries() {
		return this.retries;
	}

	public void setRetries(long retries) {
		this.retries = retries;
	}

	public boolean isError() {
		return this.error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public Date getLogDt() {
		return this.logDt;
	}

	public void setLogDt(Date logDt) {
		this.logDt = logDt;
	}

	public Set<TransactionLog> getTransactionLogs() {
		return transactionLogs;
	}

	public void setTransactionLogs(Set<TransactionLog> transactionLogs) {
		this.transactionLogs = transactionLogs;
	}

}