package com.ibm.rcs.ap.beans;

public class ReqExtension {
	/*
	 * This class is to store mapping between core and module artifact
	 */
	private String modArtifactRef;
	private String coreArtifactRef;
	
	public String getModArtifactRef() {
		return modArtifactRef;
	}
	public void setModArtifactRef(String modArtifactRef) {
		this.modArtifactRef = modArtifactRef;
	}
	public String getCoreArtifactRef() {
		return coreArtifactRef;
	}
	public void setCoreArtifactRef(String coreArtifactRef) {
		this.coreArtifactRef = coreArtifactRef;
	}

}
