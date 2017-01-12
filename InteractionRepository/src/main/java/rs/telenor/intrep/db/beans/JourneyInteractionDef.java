package rs.telenor.intrep.db.beans;

import java.util.*;

public class JourneyInteractionDef {
	int JourneyId;
	int ComponentId;
	int ComponentOrder;
	int PreviousStep;
	int NextStep;
	int JourneyIdentifierType;
	int JourneyExpiryPeriod;
	int NumberOfRepetiotions;
	int JourneyAction;
	ArrayList<ComplexCondition> conditions;
}
