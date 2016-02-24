package com.quakearts.webapp.facelets.bootstrap.handlers;

import javax.faces.component.UIComponent;
import javax.faces.event.AbortProcessingException;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.FaceletContext;

public class BootColumnHandler extends BootBaseHandler {

	public BootColumnHandler(ComponentConfig config) {
		super(config);
	}
	
	@Override
	public void onComponentCreated(FaceletContext ctx, UIComponent c,
			UIComponent parent) {

		if(c.getAttributes().get("xs")==null && c.getValueExpression("xs")==null
				&& c.getAttributes().get("sm")==null && c.getValueExpression("sm")==null
				&& c.getAttributes().get("md")==null && c.getValueExpression("md")==null
				&& c.getAttributes().get("lg")==null && c.getValueExpression("lg")==null)
			throw new AbortProcessingException("At least one attribute of x, sm, md or lg is required");
	}
}
