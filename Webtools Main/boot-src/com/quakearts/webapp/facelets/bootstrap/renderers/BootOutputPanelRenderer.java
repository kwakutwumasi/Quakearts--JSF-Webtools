package com.quakearts.webapp.facelets.bootstrap.renderers;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import com.quakearts.webapp.facelets.bootstrap.components.BootOutputPanel;
import com.quakearts.webapp.facelets.bootstrap.renderkit.Attribute;
import com.quakearts.webapp.facelets.bootstrap.renderkit.AttributeManager;
import com.quakearts.webapp.facelets.bootstrap.renderkit.AttributeManager.Key;
import com.quakearts.webapp.facelets.bootstrap.renderkit.html_basic.HtmlBasicRenderer;
import static com.quakearts.webapp.facelets.bootstrap.renderkit.RenderKitUtils.*;

public class BootOutputPanelRenderer extends HtmlBasicRenderer {

	private static final Attribute[] ATTRIBUTES = AttributeManager.getAttributes(Key.BOOTPANEL);
	
	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		if (context == null)
        	throw new NullPointerException();
        
        if(!(component instanceof BootOutputPanel)) {
    		throw new IOException("Component must be of type "+BootOutputPanel.class.getName());
        }
        
        BootOutputPanel panel = (BootOutputPanel)component;
 
		String type = panel.get("type");
		String styleClass = panel.get("styleClass");

		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", component);
		writer.writeAttribute("class", "panel panel-" + type
				+ (styleClass != null ? " "+styleClass : ""), null);
		writeIdAttributeIfNecessary(context, writer, component);
		renderPassThruAttributes(context, writer, component, ATTRIBUTES);
		writer.write("\n");
		
		UIComponent facet = panel.getFacet("header");
		if(facet!=null && facet.isRendered()){
			writer.startElement("div", component);
			writer.writeAttribute("class", "panel-heading", null);
			facet.encodeAll(context);
		   	writer.write("\n");
			writer.endElement("div");
 		}
		
    	writer.write("\n");
		writer.startElement("div", component);
		writer.writeAttribute("class", "panel-body", null);

	}
		
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
		ResponseWriter writer =context.getResponseWriter();
    	writer.write("\n");
		writer.endElement("div");
		
        BootOutputPanel panel = (BootOutputPanel)component;
		
		UIComponent facet = panel.getFacet("footer");
		if(facet!=null && facet.isRendered()){
	    	writer.write("\n");
			writer.startElement("div", component);
			writer.writeAttribute("class", "panel-footer", null);
			facet.encodeAll(context);
			writer.endElement("div");
	    	writer.write("\n");
		}
		writer.endElement("div");
    	writer.write("\n");
	}
	
	@Override
	public boolean getRendersChildren() {
		return true;
	}
}
