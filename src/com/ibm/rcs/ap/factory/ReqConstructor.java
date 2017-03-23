package com.ibm.rcs.ap.factory;

import java.util.ArrayList;
import com.ibm.rcs.ap.beans.*;

public class ReqConstructor {
	
	private ArrayList<Requirement> requirements = new ArrayList<Requirement>();
	private ArrayList<ReqExtension> reqExtensions = new ArrayList<ReqExtension>();
	
	public void addCoreArtifactRef(ArrayList<Requirement> requirements, ArrayList<ReqExtension> reqExtensions) {
		this.requirements = requirements;
		this.reqExtensions = reqExtensions;
		
		for (int i = 0; i < requirements.size(); i++){
			for (int j = 0; j < reqExtensions.size(); j++) {
				if (requirements.get(i).getRef().equals(reqExtensions.get(j).getModArtifactRef())) {
					requirements.get(i).setCoreRef(reqExtensions.get(j).getCoreArtifactRef());
				}
			}
		}
	}
	
	
	public ArrayList<Requirement> getRequirements() {
		return requirements;
	}
	public void setRequirements(ArrayList<Requirement> requirements) {
		this.requirements = requirements;
	}
	public ArrayList<ReqExtension> getReqExtensions() {
		return reqExtensions;
	}
	public void setReqExtensions(ArrayList<ReqExtension> reqExtensions) {
		this.reqExtensions = reqExtensions;
	}
	

}
