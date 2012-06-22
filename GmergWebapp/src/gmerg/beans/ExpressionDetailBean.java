package gmerg.beans;

import gmerg.assemblers.ExpressionDetailAssembler;
import gmerg.entities.submission.ExpressionDetail;

import java.util.Map;
import javax.faces.context.FacesContext;

public class ExpressionDetailBean {
    private boolean debug = false;
    private ExpressionDetail expression;
    private ExpressionDetailAssembler expAssembler;
    private boolean hasSecondaryStrength;
    private boolean hasPatterns;
    
    public ExpressionDetailBean () {
	    if (debug)
		System.out.println("ExpressionDetailBean.constructor");

        FacesContext context = FacesContext.getCurrentInstance();
        Map requestParams =
            context.getExternalContext().getRequestParameterMap();
        String subId = (String)requestParams.get("id");
        String compId = (String) requestParams.get("componentId");
        
        expAssembler = new ExpressionDetailAssembler();
        hasSecondaryStrength = false;
        hasPatterns = false;
        
        expression = expAssembler.getData(subId, compId);
        if(expression != null){
            if(expression.getSecondaryStrength() != null && !expression.getSecondaryStrength().equals("")) {
                hasSecondaryStrength = true;
            }
            if(expression.getPattern() != null){
                hasPatterns = true;
            }
        }
    }
    
    
    public ExpressionDetail getExpressionDetail() {
        return expression;
    }
    
    public void setExpressionDetail (ExpressionDetail value){
        expression = value;
    }
    
    public boolean getHasSecondaryStrength() {
        return hasSecondaryStrength;
    }
    
    public boolean getHasPatterns() {
        return hasPatterns;
    }
    
}
