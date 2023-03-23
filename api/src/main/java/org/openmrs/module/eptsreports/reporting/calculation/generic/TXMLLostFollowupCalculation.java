package org.openmrs.module.eptsreports.reporting.calculation.generic;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.eptsreports.reporting.calculation.BaseFghCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.util.processor.TxmlLostFollowUpProcessor;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class TXMLLostFollowupCalculation extends BaseFghCalculation {

  @Override
  public CalculationResultMap evaluate(
      Collection<Integer> cohort, Map<String, Object> parameterValues, EvaluationContext context) {

    Map<Integer, Map<String, Date>> processorResult =
        Context.getRegisteredComponents(TxmlLostFollowUpProcessor.class).get(0).getResutls(context);

    CalculationResultMap resultMap = new CalculationResultMap();

    for (Entry<Integer, Map<String, Date>> pair : processorResult.entrySet()) {
      resultMap.put(pair.getKey(), new SimpleResult(pair.getValue(), this));
    }
    return resultMap;
  }
}
