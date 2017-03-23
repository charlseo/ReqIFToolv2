package com.ibm.rcs.ap.beans;

public class Requirement {
	
	private String modArtifactRef;
	private String coreArtifactRef;
	private String reqID;
	
	public String getRef() {
		return modArtifactRef;
	}
	public void setRef(String ref) {
		this.modArtifactRef = ref;
	}
	public String getCoreRef() {
		return coreArtifactRef;
	}
	public void setCoreRef(String coreRef) {
		this.coreArtifactRef = coreRef;
	}
	public String getReqID() {
		return reqID;
	}
	public void setReqID(String reqID) {
		this.reqID = reqID;
	}
	
	
}
