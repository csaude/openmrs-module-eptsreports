package org.openmrs.module.eptsreports.reporting.calculation.txml;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eptsreports.reporting.calculation.BooleanResult;
import org.openmrs.module.eptsreports.reporting.calculation.FGHAbstractPatientCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.processor.TxMLPatientDisagregationProcessor;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class TxMLPatientsWhoAreDeadCalculation extends FGHAbstractPatientCalculation {

  @Override
  public CalculationResultMap evaluate(
      Map<String, Object> parameterValues, EvaluationContext context) {

    Map<Integer, Date> processorResult =
        Context.getRegisteredComponents(TxMLPatientDisagregationProcessor.class)
            .get(0)
            .getPatientsMarkedAsDeadResults(context);
    CalculationResultMap resultMap = new CalculationResultMap();

    for (Integer patientId : processorResult.keySet()) {
      resultMap.put(patientId, new BooleanResult(Boolean.TRUE, this));
    }
    return resultMap;
  }

  @Override
  public CalculationResultMap evaluate(
      Collection<Integer> cohort, Map<String, Object> parameterValues, EvaluationContext context) {
    return this.evaluate(parameterValues, context);
  }
}
