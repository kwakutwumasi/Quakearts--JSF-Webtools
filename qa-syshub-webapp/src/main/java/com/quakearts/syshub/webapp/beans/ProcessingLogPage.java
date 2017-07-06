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

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;

import com.quakearts.webapp.facelets.base.BaseBean;
import com.quakearts.webapp.orm.query.Range;
import com.quakearts.webapp.orm.query.helper.ParameterMapBuilder;
import com.quakearts.webapp.orm.exception.DataStoreException;
import com.quakearts.syshub.model.ProcessingLog;
import com.quakearts.syshub.SysHub;
import com.quakearts.syshub.core.runner.AgentRunner;
import com.quakearts.syshub.exception.ProcessingException;
import com.quakearts.syshub.model.AgentConfiguration;

@ManagedBean(name="processingLogPage")
@ViewScoped
public class ProcessingLogPage extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1849694857204230644L;

	@Inject SysHub sysHub;
	
	private static Logger log = Logger.getLogger(ProcessingLogPage.class.getName());

	private ProcessingLog processingLog;
	private WebApplicationMain webappmain;
	private transient ProcessingLogFinder finder = new ProcessingLogFinder();
		
	public ProcessingLogPage(){
		webappmain = new WebApplicationMain();
	}
	
	public WebApplicationMain getWebappmain(){
		return webappmain;
	}
	
	public ProcessingLog getProcessingLog() {
		if(processingLog==null){
			if(hasParameter("processingLog")){
				setProcessingLog(finder.getById(getParameterLong("processingLog")));
				webappmain.setMode("read");
			} else {
    			processingLog = new ProcessingLog();
			}
		}
		
		return processingLog;
	}
    	
	public void setProcessingLog(ProcessingLog processingLog) {
		this.processingLog = processingLog;
		if(processingLog!=null){
			AgentConfiguration agentConfiguration = processingLog.getAgentConfiguration();
			if(agentConfiguration!=null){
				getAgentConfigurationDropdownHelper().addToFoundItemsList(agentConfiguration);
			}
		}
	}
	
	private AgentConfigurationDropdownHelper agentConfigurationDropdownHelper;

	public AgentConfigurationDropdownHelper getAgentConfigurationDropdownHelper(){
		if(agentConfigurationDropdownHelper == null)
			agentConfigurationDropdownHelper = new AgentConfigurationDropdownHelper();
			
		return agentConfigurationDropdownHelper;
	}
	
	private AgentModuleDropdownHelper agentModuleDropdownHelper;
	
	public AgentModuleDropdownHelper getAgentModuleDropdownHelper() {
		if(agentModuleDropdownHelper == null)
			agentModuleDropdownHelper = new AgentModuleDropdownHelper();
			
		return agentModuleDropdownHelper;
	}
	
	private List<ProcessingLog> processingLogList;
	
	public List<ProcessingLog> getProcessingLogList(){
		return processingLogList;
	}
	
	private Range logDtRange = new Range();
	
	public Range getLogDtRange() {
		return logDtRange;
	}
	
	public void findProcessingLog(ActionEvent event){
		ParameterMapBuilder parameterBuilder = new ParameterMapBuilder();
		if(processingLog.getAgentConfiguration() != null){
			parameterBuilder.add("agentConfiguration", processingLog.getAgentConfiguration());
		}
		if(processingLog.getAgentModule() != null){
			parameterBuilder.add("agentModule", processingLog.getAgentModule());
		}
		if(processingLog.isError()){
			parameterBuilder.add("error", processingLog.isError());
		}
		if(!logDtRange.isEmpty()){
			parameterBuilder.add("logDt", logDtRange);
		}
		if(processingLog.getMessageData() != null){
			parameterBuilder.add("messageData", processingLog.getMessageData());
		}
		if(processingLog.getMid() != null && ! processingLog.getMid().trim().isEmpty()){
			parameterBuilder.addVariableString("mid", processingLog.getMid());
		}
		if(processingLog.getRecipient() != null && ! processingLog.getRecipient().trim().isEmpty()){
			parameterBuilder.addVariableString("recipient", processingLog.getRecipient());
		}
		if(processingLog.getRetries() != 0){
			parameterBuilder.add("retries", processingLog.getRetries());
		}
		if(processingLog.getStatusMessage() != null && ! processingLog.getStatusMessage().trim().isEmpty()){
			parameterBuilder.addVariableString("statusMessage", processingLog.getStatusMessage());
		}
		if(processingLog.getType() != null){
			parameterBuilder.add("type", processingLog.getType());
		}
    		
		try {
			processingLogList = finder.findObjects(parameterBuilder.build());
		} catch (DataStoreException e) {
			addError("Search error", "An error occured while searching for Processing Log", FacesContext.getCurrentInstance());
			log.severe("Exception of type " + e.getClass().getName() + " was thrown. Message is " + e.getMessage()
					+ ". Exception occured whiles searching for ProcessingLog");
		}		
	}
    	
	public void removeProcessingLog(ActionEvent event){
		if(processingLog!=null && processingLogList!=null){
			processingLogList.remove(processingLog);
		}
	}
	
	public boolean isInCreateOrEditMode(){
		return FacesContext.getCurrentInstance().getViewRoot().getViewId().endsWith("create.xhtml") || "edit".equals(webappmain.getMode());
	}
	
	public void retryProcessingMessage(AjaxBehaviorEvent event){
		if(getProcessingLog().getLogID() == 0){
			addError("Invalid Data", "Select a valid ProcessingLog to continue", FacesContext.getCurrentInstance());
			return;
		}
		
		if(sysHub != null){
			AgentRunner agentRunner = sysHub.fetchAgentRunner(getProcessingLog().getAgentConfiguration());
			if(agentRunner != null){
				try {
					agentRunner.getProcessingAgent().reprocessProcessingLog(getProcessingLog());
					removeProcessingLog(null);
				} catch (ClassNotFoundException | IOException | ProcessingException e) {
					addError("System Error", "Unable to reprocess message: "+e.getMessage(), FacesContext.getCurrentInstance());
				}
			} else {
				addError("System Error", "Agent Runner "
						+getProcessingLog().getAgentConfiguration().getAgentName()
						+" has not been deployed.", FacesContext.getCurrentInstance());
			}
		}
	}
}
