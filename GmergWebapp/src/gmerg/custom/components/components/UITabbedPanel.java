/*
 * Copyright 2007 Sun Microsystems, Inc.
 * All rights reserved.  You may not modify, use,
 * reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://developer.sun.com/berkeley_license.html
 */

package gmerg.custom.components.components;

import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class UITabbedPanel extends UIOutput {
	
	public UITabbedPanel() {
		this.setRendererType(null);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = getClientId(context);
		
		List children = getChildren();

		int currentTab = Utility.getIntValue(FacesUtil.getRequestParamValue("currentTabHolder"), 0);
		
		boolean isCurrentTabDisplayed = false;
		int secondCurrentTab = -1;
		int tabsNum = 0;
		for (int i=0; i<children.size(); i++) {
			UIComponent child = (UIComponent)children.get(i);
			if (child instanceof UITabPane && child.isRendered()) {
				if (tabsNum == currentTab)
					isCurrentTabDisplayed = true;
				if (secondCurrentTab < 0)
					secondCurrentTab = tabsNum;
				tabsNum++;
			}
		}
		if (!isCurrentTabDisplayed) 
			currentTab = secondCurrentTab;
			
		// Write tab headers
		writer.startElement("input", this);
		writer.writeAttribute("type", "hidden", null);
//		writer.writeAttribute("id", clientId+"currentTabHolder", null);
		writer.writeAttribute("id", "currentTabHolder", null);
		writer.writeAttribute("value", String.valueOf(currentTab), null);
		writer.endElement("input");
		writer.startElement("table", this);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("width", "100%", null);
		writer.writeAttribute("class", getStyleClass("tabbedPanel"), null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		
		//------- write tab header row
		writer.startElement("tr", this);
		int tabHeaderWidth = (int)Math.round(95.0/tabsNum);
		if (tabHeaderWidth*tabsNum > 98)
			tabHeaderWidth--;
		
		for (int i=0, tabIndex=0; i<children.size(); i++) {
			UIComponent child = (UIComponent)children.get(i);
			if (child instanceof UITabPane && child.isRendered()) {
				writeTabHeader(context, (UITabPane)child, tabIndex, tabHeaderWidth, currentTab);
				tabIndex++;
//				tabIds.add( child.getClientId(context));
			}
		}
		writer.startElement("td", this);
		writer.writeAttribute("class", getStyleClass("emptyTabStyleClass"), null);
		writer.writeAttribute("width", String.valueOf(100-tabHeaderWidth*tabsNum)+"%", null);
		writer.write("&nbsp");
		writer.endElement("td");
		writer.endElement("tr");
		
		//-------- Write sub-header row
		writer.startElement("tr", this);
		for (int i=0; i<tabsNum; i++) {
			writer.startElement("td", this);
			String id = getClientId(context)+"SubHeader"+String.valueOf(i); 
			writer.writeAttribute("id", id, null);
			String styleClass = "";
			if (i == 0)
				styleClass += getStyleClass("firstSubStyleClass") + " ";
			if (i == currentTab)
				styleClass += getStyleClass("activeSubStyleClass");
			else
				styleClass += getStyleClass("inactiveSubStyleClass");
			writer.writeAttribute("class", styleClass, null);
			writer.write("&nbsp");
			writer.endElement("td");
		}		
		writer.startElement("td", this);
		writer.writeAttribute("class", getStyleClass("lastSubStyleClass"), null);
		writer.write("&nbsp");
		writer.endElement("td");
		writer.endElement("tr");

		//-------- Write tab panes
		writer.startElement("tr", this);
		writer.startElement("td", this);
		writer.writeAttribute("colspan", tabsNum+1, null);
		writer.writeAttribute("class", getStyleClass("tabContentStyleClass"), null);
		for (int i=0, tabIndex=0; i<children.size(); i++) {
			UIComponent child = (UIComponent)children.get(i);
			if (child instanceof UITabPane && child.isRendered()) {
				writeTabContent(context, (UITabPane)child, tabIndex, tabIndex==currentTab);
				tabIndex++;
			}
			else
				renderChild(context, child);
		}
		writer.endElement("td");
		writer.endElement("tr");
		
		writer.endElement("table");
	}

	public boolean getRendersChildren() {
		return true;
	}

	
	public void encodeChildren(FacesContext context) throws IOException {

	}

/*	
	public void encodeEnd(FacesContext context) throws IOException {
	}
	
	public void decode(FacesContext context) {
	}
*/	
	
	
	/**************************************************************
	* private & protected methods
	*
	*/
	protected void writeTabHeader(FacesContext context, UITabPane tabPane, int tabIndex, int tabHeaderWidth, int currentTabIndex) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String tabPanelId = getClientId(context);
//		String label = (String)tabPane.getAttributes().get("label");
		String label = (String)tabPane.getAttributes().get("label");
//System.out.println("label="+label+"     decode="+URLDecoder.decode(label)+ "  dec2="+URLDecoder.decode(label, "UTF-8")+"     enc1="+URLEncoder.encode(label)+"    enc2= "+URLEncoder.encode(label, "UTF-8"));		
//System.out.println("label="+label+"     label2="+(String)getAttributes().get("label"));

		
		if (label==null || label.length()==0)
			label = "Tab " + tabIndex;
		String id = tabPanelId+"Header"+String.valueOf(tabIndex); //tabPane.getClientId(context);
		
		writer.startElement("td", this);
		writer.writeAttribute("id", id, null);
		if (tabHeaderWidth>0)
			writer.writeAttribute("width", String.valueOf(tabHeaderWidth)+"%", null);

		
/*		
		String activeSubHeaderStyleClass = (tabIndex==0)? getStyleClass("firstSubStyleClass")+" " : "";
		activeSubHeaderStyleClass += getStyleClass("activeSubStyleClass");
		String inactiveSubHeaderStyleClass = (currentTabIndex==0)? getStyleClass("firstSubStyleClass")+" " : "";
		inactiveSubHeaderStyleClass += getStyleClass("inactiveSubStyleClass");
*/
		if (tabIndex == currentTabIndex)  
			writer.writeAttribute("class", getStyleClass("activeTabStyleClass"), null);
		else 
			writer.writeAttribute("class", getStyleClass("inactiveTabStyleClass"), null);
		
		// Button
		writer.startElement("input", this);
		writer.writeAttribute("type", "submit", null);
		writer.writeAttribute("value", label, null);
		writer.writeAttribute("onclick", "return showTabPane("  + "'" + tabPanelId + "', "   
																+ "'" + String.valueOf(tabIndex) + "', "  
																+ "'" + getStyleClass("activeTabStyleClass") + "', "
																+ "'" + getStyleClass("inactiveTabStyleClass") + "', "
																+ "'" + getStyleClass("firstSubStyleClass") + "', "
																+ "'" + getStyleClass("activeSubStyleClass") + "', "
																+ "'" + getStyleClass("inactiveSubStyleClass") + "');", null);
		
		writer.writeAttribute("onclick", "getById('" + id+"Header"  + "', "   
				+ "'" + id+"Content" + "', ", null);
		
		writer.endElement("input");
		writer.endElement("td");
	 
/*		
		if( tabbedPane.isClientSide() ){
			String activeUserClass = tabbedPane.getActiveTabStyleClass();
			String inactiveUserClass = tabbedPane.getInactiveTabStyleClass();
			String activeSubStyleUserClass = tabbedPane.getActiveSubStyleClass();
			String inactiveSubStyleUserClass = tabbedPane.getInactiveSubStyleClass();
			String onclickEvent = tab.getAttributes().get(HTML.ONCLICK_ATTR) != null ? (String) tab.getAttributes().get(HTML.ONCLICK_ATTR) : "";

			writer.writeAttribute(HTML.ONCLICK_ATTR, onclickEvent  + "return myFaces_showPanelTab("
   +tabIndex+",'"+getTabIndexSubmitFieldIDAndName(tabbedPane, facesContext)+"',"
   +'\''+getHeaderCellID(tab, facesContext)+"','"+tab.getClientId(facesContext) + TAB_DIV_SUFFIX +"',"
   +getHeaderCellsIDsVar(tabbedPane,facesContext)+','+getTabsIDsVar(tabbedPane,facesContext)+','
   + (activeUserClass==null ? "null" : '\''+activeUserClass+'\'')+','+ (inactiveUserClass==null ? "null" : '\''+inactiveUserClass+'\'')+','
   + (activeSubStyleUserClass==null ? "null" : '\''+activeSubStyleUserClass+'\'')+','+ (inactiveSubStyleUserClass==null ? "null" : '\''+inactiveSubStyleUserClass+'\'')+");",
				   null);
*/
	}
	
	protected void writeTabContent(FacesContext context, UITabPane tabPane, int tabIndex, boolean active) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		
		String tabPanelId = getClientId(context);
//		String activeTabVar = getActiveTabVar();
//		if (activeTabVar != null) 
//			FacesUtil.setFacesRequestParamValue(activeTabVar, active);

		writer.startElement("DIV", this);
		writer.writeAttribute("ID", tabPanelId+"TabbedPaneContent"+String.valueOf(tabIndex), null);
		// the inactive tabs are hidden with a div-tag
		if (!active)  
			writer.writeAttribute("STYLE", "display:none", null);
		
		renderChild(context, tabPane);
		writer.endElement("DIV");

//		if (activeTabVar != null) 
//			context.getExternalContext().getRequestMap().remove(activeTabVar);
	}
	
	
	
	private static void renderChildren(FacesContext context, UIComponent component) throws java.io.IOException {
		if (component != null && component.isRendered()) {
			Iterator iter = component.getChildren().iterator();
			while (iter.hasNext()){
				UIComponent child = (UIComponent)iter.next();
				renderChild(context, child);
			}
		}
	}

	private static void renderChild(FacesContext context, UIComponent child) throws java.io.IOException {
		if (child == null || !child.isRendered()) 
			return;
		child.encodeBegin(context);
		if (child.getRendersChildren()) 
			child.encodeChildren(context);
		else
			renderChildren(context, child);
		child.encodeEnd(context);
		if (!child.isRendered())
			return;
	}
	
	
	
	
	
	
	
	protected enum DefaultStyleClasses {tabbedPanel, activeTabStyleClass, inactiveTabStyleClass, emptyTabStyleClass, 
										activeSubStyleClass, inactiveSubStyleClass, firstSubStyleClass, lastSubStyleClass,
										tabContentStyleClass }
	final static protected String[] styleClassNames = { "gudmap_tabbedPanel",
														"gudmap_tabbedPanel_activeTabHeader", 
														"gudmap_tabbedPanel_inactiveTabHeader",
														"gudmap_tabbedPanel_emptyTabHeader",
														"gudmap_tabbedPanel_subTabHeader gudmap_tabbedPanel_subTabHeader_active", 
														"gudmap_tabbedPanel_subTabHeader gudmap_tabbedPanel_subTabHeader_inactive",
														"gudmap_tabbedPanel_subTabHeader_first", 
														"gudmap_tabbedPanel_subTabHeader gudmap_tabbedPanel_subTabHeader_last",
														"gudmap_tabbedPanel_pane"
													   };
	
	protected String getStyleClass(String styleClass) {
		String defaultStyleClasse = styleClassNames[DefaultStyleClasses.valueOf(styleClass).ordinal()]; 
		String userDefinedStyleClass = (String)getAttributes().get(styleClass);
		if (userDefinedStyleClass == null)
			return defaultStyleClasse;
		return defaultStyleClasse + " " + userDefinedStyleClass;
	}
	
	
}
