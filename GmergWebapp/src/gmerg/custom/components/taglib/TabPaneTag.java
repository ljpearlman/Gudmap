package gmerg.custom.components.taglib;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentELTag;
import javax.el.ValueExpression;



public class TabPaneTag extends UIComponentELTag {
	private ValueExpression label = null;
	private ValueExpression styleClass = null;
	
	public String getRendererType() {
		return null;
	}
	
	public String getComponentType() {
		return "gudmap.TabPane";
	}

	public void setProperties(UIComponent component) {
		super.setProperties(component);
		component.setValueExpression("label", label);
		component.setValueExpression("styleClass", styleClass);
	}
	
	public void release() {
		super.release();
		label = null;
		styleClass = null;
	}

	public void setLabel(ValueExpression label) {
		this.label = label;
	}

	public void setStyleClass(ValueExpression styleClass) {
		this.styleClass = styleClass; 
	}

}

