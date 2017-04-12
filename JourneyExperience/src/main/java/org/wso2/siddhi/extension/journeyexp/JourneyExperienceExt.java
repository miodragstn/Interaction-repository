package org.wso2.siddhi.extension.journeyexp;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import org.wso2.siddhi.core.config.ExecutionPlanContext;
import org.wso2.siddhi.core.exception.ExecutionPlanRuntimeException;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.executor.function.FunctionExecutor;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.exception.ExecutionPlanValidationException;

import rs.telenor.intrep.db.ConnectionManager;
import rs.telenor.intrep.db.JsonManager;
import rs.telenor.intrep.db.beans.InteractionInstance;
import rs.telenor.intrep.db.tables.InteractionInstanceManager;
import rs.telenor.intrep.db.tables.InteractionManager;
import rs.telenor.journeyexperience.JourneyExperienceManager;

 
/*
* IR CEP Extension
* Returns Interaction Instance ID 
* Accept Type(s) : STRINNG
* Return Type(s): LONG
*/
public class JourneyExperienceExt extends FunctionExecutor {
 
	private static final Logger serviceLog = Logger
            .getLogger(JourneyExperienceExt.class);
	
    /**
     * The initialization method for SinFunctionExtension, this method will be called before the other methods
     *
     * @param attributeExpressionExecutors the executors of each function parameter
     * @param executionPlanContext         the context of the execution plan
     */
    @Override
    protected void init(ExpressionExecutor[] attributeExpressionExecutors, ExecutionPlanContext executionPlanContext) {
//        if (attributeExpressionExecutors.length != 1) {
//            throw new ExecutionPlanValidationException("Invalid no of arguments passed to IR extension function, " +
//                    "required 4, but found " + attributeExpressionExecutors.length);
//        }
//        Attribute.Type attributeType = attributeExpressionExecutors[0].getReturnType();
//        if (!((attributeType == Attribute.Type.DOUBLE)
//                || (attributeType == Attribute.Type.INT)
//                || (attributeType == Attribute.Type.FLOAT)
//                || (attributeType == Attribute.Type.LONG))) {
//            throw new ExecutionPlanValidationException("Invalid parameter type found for the argument of IR extension function, " +
//                    "required " + Attribute.Type.INT + " or " + Attribute.Type.LONG +
//                    " or " + Attribute.Type.FLOAT + " or " + Attribute.Type.DOUBLE +
//                    ", but found " + attributeType.toString());
//        }
        
        serviceLog.info("JE_MX "+" - init") ;
        
    }
 
    /**
     * The main execution method which will be called upon event arrival
     * when there are more than one function parameter
     *
     * @param data the runtime values of function parameters
     * @return the function result
     */
    @Override
    protected Object execute(Object[] data) {
    	
    	return null;
    }
 
    /**
     * The main execution method which will be called upon event arrival
     * when there are zero or one function parameter
     *
     */
    @Override
    protected Object execute(Object data) {
    	serviceLog.info("START_JE_MX") ;
    	
   	 
    	String message=null;

    	try {
    		long interactionIns = (long) data;
			message = JourneyExperienceManager.getJourneyExperienceMessage(interactionIns, serviceLog) ;
			serviceLog.info("interaction_JE_MX "+message) ;
				
		} catch (SQLException e) {
			serviceLog.error("interaction_JE_MX "+e.getMessage(),e) ;
		}
//    	
        return message;
//    	return "MX";
    }
 
    @Override
    public void start() {
        //Implement start logic to acquire relevant resources
    }
 
    @Override
    public void stop() {
        //Implement stop logic to release the acquired resources
    }
 
    @Override
    public Attribute.Type getReturnType() {
        return Attribute.Type.DOUBLE;
    }
 
 
    /**
     * Used to collect the serializable state of the processing element, that need to be
     * persisted for the reconstructing the element to the same state on a different point of time
     *
     * @return stateful objects of the processing element as an array
     */
    @Override
    public Object[] currentState() {
        return null;
    }
 
    /**
     * Used to restore serialized state of the processing element, for reconstructing
     * the element to the same state as if was on a previous point of time.
     *
     * @param state the stateful objects of the element as an array on
     *              the same order provided by currentState().
     */
    @Override
    public void restoreState(Object[] state) {
        //Implement restore state logic.
    }
}




