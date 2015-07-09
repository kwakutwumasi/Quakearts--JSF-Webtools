package com.quakearts.webapp.facelets.tag.listener;

import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;

import com.quakearts.webapp.facelets.tag.BaseListener;
import com.quakearts.webapp.facelets.tag.utils.SubjectHelper;
import com.quakearts.webapp.facelets.util.UtilityMethods;

public class ProfileListener extends BaseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5884918670013392555L;
	private ValueExpression varExpression;
	private static final Logger log = Logger.getLogger(ProfileListener.class);
	
	public ProfileListener(ValueExpression varExpression) {
		this.varExpression = varExpression;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void continueProcessing(ActionEvent event, FacesContext ctx) {
		Map sessionMap = ctx.getExternalContext().getSessionMap();
		SubjectHelper subject;
		if(sessionMap.containsKey(SubjectHelper.KEY)){
			subject = (SubjectHelper) sessionMap.get(SubjectHelper.KEY);
		}else{					
			try {
				subject = new SubjectHelper();
				sessionMap.put(SubjectHelper.KEY, subject);
			} catch (Exception e) {
				ctx.addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Application error", "Could not obtain user profile object"));
				try {
					UtilityMethods.getTransaction().setRollbackOnly();					
				} catch (Exception e2) {
				}
				log.error("Exception of type " + e.getClass().getName()
						+ " was thrown. Message is " + e.getMessage());
				return;
			}
		}
		varExpression.setValue(ctx.getELContext(), subject.getSubjectMap());
	}

}
