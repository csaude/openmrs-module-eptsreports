package org.openmrs.module.eptsreports.reporting.calculation.generic;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.eptsreports.reporting.calculation.FGHAbstractPatientCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.processor.LastSeguimentoProcessor;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class LastSeguimentoCalculation extends FGHAbstractPatientCalculation {

  @Override
  public CalculationResultMap evaluate(
      Collection<Integer> cohort, Map<String, Object> parameterValues, EvaluationContext context) {

    Map<Integer, Date> lastSeguimentoResult =
        Context.getRegisteredComponents(LastSeguimentoProcessor.class).get(0).getResutls(context);
    CalculationResultMap resultMap = new CalculationResultMap();
    for (Integer patientId : cohort) {
      if (lastSeguimentoResult.get(patientId) != null) {
        resultMap.put(patientId, new SimpleResult(lastSeguimentoResult.get(patientId), this));
      }
    }
    return resultMap;
  }
}
