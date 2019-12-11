package org.openmrs.module.eptsreports.reporting.calculation.generic;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eptsreports.reporting.calculation.BooleanResult;
import org.openmrs.module.eptsreports.reporting.calculation.FGHAbstractPatientCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.processor.TxMLPatientsWhoAreDeadProcessor;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class TxMLPatientsWhoAreTransferedOutCalculation extends FGHAbstractPatientCalculation {

  @Override
  public CalculationResultMap evaluate(
      Map<String, Object> parameterValues, EvaluationContext context) {

    Map<Integer, Date> processorResult =
        Context.getRegisteredComponents(TxMLPatientsWhoAreDeadProcessor.class)
            .get(0)
            .getPatienTransferedOutResults(context);
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
