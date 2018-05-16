package org.wso2.siddhi.extension.journey;

import java.util.ArrayList;
import java.util.List;

import org.wso2.siddhi.core.config.ExecutionPlanContext;
import org.wso2.siddhi.core.exception.ExecutionPlanCreationException;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.query.processor.stream.function.StreamFunctionProcessor;
import org.wso2.siddhi.query.api.definition.AbstractDefinition;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.apache.log4j.Logger;
import rs.telenor.intrep.db.tables.SubscriptionProfileManager;

public class SubscriptionStatusManager extends StreamFunctionProcessor {

	private static final Logger serviceLog = Logger
			.getLogger(SubscriptionStatusManager.class);
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] currentState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void restoreState(Object[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Object[] process(Object[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object[] process(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Attribute> init(AbstractDefinition arg0, ExpressionExecutor[] arg1, ExecutionPlanContext arg2) {
		serviceLog.info("Check if hashmaps are initialized");
		SubscriptionProfileManager.database2hashmap(serviceLog, "java:/TEST_DWH_IR");


		if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
			throw new ExecutionPlanCreationException(
					"First parameter should be of type string");
		}

		ArrayList<Attribute> attributes = new ArrayList<Attribute>(7);

		attributes.add(new Attribute("meta_CO_ID", Attribute.Type.INT));
		attributes.add(new Attribute("meta_CUSTOMER_ID ", Attribute.Type.INT));
		attributes.add(new Attribute("meta_CUSTCODE", Attribute.Type.STRING));
		attributes.add(new Attribute("meta_MSISDN", Attribute.Type.STRING));
		attributes.add(new Attribute("meta_SEGMENT", Attribute.Type.STRING));
		attributes.add(new Attribute("meta_SUBS_TYPE", Attribute.Type.STRING));
		attributes.add(new Attribute("meta_CID", Attribute.Type.INT));
		attributes.add(new Attribute("STATUS", Attribute.Type.STRING));
		attributes.add(new Attribute("STATUS_DATE", Attribute.Type.STRING));
		attributes.add(new Attribute("STATUS_REASON", Attribute.Type.STRING));

		return attributes;
	}

}
