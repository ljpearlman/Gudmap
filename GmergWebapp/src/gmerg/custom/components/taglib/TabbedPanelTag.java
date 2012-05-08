package gmerg.custom.components.taglib;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;
import javax.el.ValueExpression;



public class TabbedPanelTag extends UIComponentELTag {
	private ValueExpression value = null;
    private ValueExpression activeTabStyleClass;
    private ValueExpression inactiveTabStyleClass;
    private ValueExpression activeSubStyleClass;
    private ValueExpression inactiveSubStyleClass;
    private ValueExpression tabContentStyleClass;
	public String getRendererType() {
		return null;
	}
	
	public String getComponentType() {
		return "gudmap.TabbedPanel";
	}

	public void setProperties(UIComponent component) {
		super.setProperties(component);
		component.setValueExpression("value", value);
		component.setValueExpression("activeTabStyleClass", activeTabStyleClass);
		component.setValueExpression("inactiveTabStyleClass", inactiveTabStyleClass);
		component.setValueExpression("activeSubStyleClass", activeSubStyleClass);
		component.setValueExpression("inactiveSubStyleClass", inactiveSubStyleClass);
		component.setValueExpression("tabContentStyleClass", tabContentStyleClass);
	}
	
	public void release() {
		super.release();
		value = null;
        activeTabStyleClass = null;
        inactiveTabStyleClass = null;
        activeSubStyleClass = null;
        inactiveSubStyleClass = null;
        tabContentStyleClass = null;
		
	}

	public void setValue(ValueExpression value) {
		this.value = value;
	}
	
	public void setActiveSubStyleClass(ValueExpression activeSubStyleClass) {
		this.activeSubStyleClass = activeSubStyleClass;
	}

	public void setActiveTabStyleClass(ValueExpression activeTabStyleClass) {
		this.activeTabStyleClass = activeTabStyleClass;
	}

	public void setInactiveSubStyleClass(ValueExpression inactiveSubStyleClass) {
		this.inactiveSubStyleClass = inactiveSubStyleClass;
	}

	public void setInactiveTabStyleClass(ValueExpression inactiveTabStyleClass) {
		this.inactiveTabStyleClass = inactiveTabStyleClass;
	}

	public void setTabContentStyleClass(ValueExpression tabContentStyleClass) {
		this.tabContentStyleClass = tabContentStyleClass;
	}
}

