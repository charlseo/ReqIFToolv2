package com.ibm.rcs.ap.beans;

public class ReqRelation {
	
	private String soureModReqRef;
	private String targetModReqRef;
	private String sourceCoreReqRef;
	private String targetCoreReqRef;
	private String lastChange;
	private String identifier;
	private String type;
		
	
	public String getSourceReqID() {
		return soureModReqRef;
	}
	public void setSourceReqID(String sourceReqID) {
		this.soureModReqRef = sourceReqID;
	}
	public String getTargetReqID() {
		return targetModReqRef;
	}
	public void setTargetReqID(String targetReqID) {
		this.targetModReqRef = targetReqID;
	}
	public String getSourceCoreReqID() {
		return sourceCoreReqRef;
	}
	public void setSourceCoreReqID(String sourceCoreReqID) {
		this.sourceCoreReqRef = sourceCoreReqID;
	}
	public String getTargetCoreReqID() {
		return targetCoreReqRef;
	}
	public void setTargetCoreReqID(String targetCoreReqID) {
		this.targetCoreReqRef = targetCoreReqID;
	}
	public String getLastChange() {
		return lastChange;
	}
	public void setLastChange(String lastChange) {
		this.lastChange = lastChange;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	

}
