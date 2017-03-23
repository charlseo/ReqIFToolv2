package com.ibm.rcs.ap.factory;

import java.util.ArrayList;
import com.ibm.rcs.ap.beans.ReqExtension;
import com.ibm.rcs.ap.beans.ReqRelation;

public class LinkConstructor {
	
	private ArrayList<ReqExtension> reqExtensions = new ArrayList<ReqExtension>();
	private ArrayList<ReqRelation> reqRelations = new ArrayList<ReqRelation>();
	
	public void addModArtifactRef(ArrayList<ReqExtension> reqExtensions, ArrayList<ReqRelation> reqRelations) {
		this.reqExtensions = reqExtensions;
		this.reqRelations = reqRelations;
		
		for (int i = 0; i < this.reqRelations.size(); i++){
			for (int j = 0; j < this.reqExtensions.size(); j++) {
				if (this.reqRelations.get(i).getSourceCoreReqID().equals(this.reqExtensions.get(j).getCoreArtifactRef())) {
					this.reqRelations.get(i).setSourceReqID(this.reqExtensions.get(j).getModArtifactRef());				
				}
				if (this.reqRelations.get(i).getTargetCoreReqID().equals(this.reqExtensions.get(j).getCoreArtifactRef())) {
					this.reqRelations.get(i).setTargetReqID(this.reqExtensions.get(j).getModArtifactRef());				
				} 
			}
		}
	}
	
	public ArrayList<ReqExtension> getReqExtensions() {
		return reqExtensions;
	}
	public void setReqExtensions(ArrayList<ReqExtension> reqExtensions) {
		this.reqExtensions = reqExtensions;
	}
	public ArrayList<ReqRelation> getReqRelations() {
		return reqRelations;
	}
	public void setReqRelations(ArrayList<ReqRelation> reqRelations) {
		this.reqRelations = reqRelations;
	}
	
	
}
