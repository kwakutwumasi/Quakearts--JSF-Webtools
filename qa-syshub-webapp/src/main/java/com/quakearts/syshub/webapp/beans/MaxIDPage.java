/*******************************************************************************
 * Copyright (C) 2017 Kwaku Twumasi-Afriyie <kwaku.twumasi@quakearts.com>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Kwaku Twumasi-Afriyie <kwaku.twumasi@quakearts.com> - initial API and implementation
 ******************************************************************************/
package com.quakearts.syshub.webapp.beans;

import java.util.List;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import com.quakearts.webapp.facelets.base.BaseBean;
import com.quakearts.webapp.orm.query.helper.ParameterMapBuilder;
import com.quakearts.webapp.orm.exception.DataStoreException;
import com.quakearts.syshub.model.MaxID;

@ManagedBean(name="maxIDPage")
@ViewScoped
public class MaxIDPage extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3444082063694556548L;

	private static Logger log = Logger.getLogger(MaxIDPage.class.getName());

	private MaxID maxID;
	private WebApplicationMain webappmain;
	private transient MaxIDFinder finder = new MaxIDFinder();
		
	public MaxIDPage(){
		webappmain = new WebApplicationMain();
	}
	
	public WebApplicationMain getWebappmain(){
		return webappmain;
	}
	
	public MaxID getMaxID() {
		if(maxID==null){
			if(hasParameter("maxID")){
				setMaxID(finder.getById(getParameterInt("maxID")));
				webappmain.setMode("read");
			} else {
    			maxID = new MaxID();
			}
		}
		
		return maxID;
	}
    	
	public void setMaxID(MaxID maxID) {
		this.maxID = maxID;
		if(maxID!=null){
		}
	}
	
	
	private List<MaxID> maxIDList;
	
	public List<MaxID> getMaxIDList(){
		return maxIDList;
	}
	
	public void findMaxID(ActionEvent event){
		ParameterMapBuilder parameterBuilder = new ParameterMapBuilder();
		if(maxID.getMaxIDName() != null && ! maxID.getMaxIDName().trim().isEmpty()){
			parameterBuilder.addVariableString("maxIDName", maxID.getMaxIDName());
		}
		if(maxID.getMaxIDValue() != 0){
			parameterBuilder.add("maxIDValue", maxID.getMaxIDValue());
		}
    		
		try {
			maxIDList = finder.findObjects(parameterBuilder.build());
		} catch (DataStoreException e) {
			addError("Search error", "An error occured while searching for Max I D", FacesContext.getCurrentInstance());
			log.severe("Exception of type " + e.getClass().getName() + " was thrown. Message is " + e.getMessage()
					+ ". Exception occured whiles searching for MaxID");
		}		
	}
    	
	public void removeMaxID(ActionEvent event){
		if(maxID!=null && maxIDList!=null){
			maxIDList.remove(maxID);
		}
	}
	
	public boolean isInCreateOrEditMode(){
		return FacesContext.getCurrentInstance().getViewRoot().getViewId().endsWith("create.xhtml") || "edit".equals(webappmain.getMode());
	}
}