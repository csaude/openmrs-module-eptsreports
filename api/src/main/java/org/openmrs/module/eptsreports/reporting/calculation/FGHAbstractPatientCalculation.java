package org.openmrs.module.eptsreports.reporting.calculation;

import java.util.Collection;
import java.util.Map;
import org.openmrs.calculation.BaseCalculation;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.reporting.evaluation.EvaluationContext;

public abstract class FGHAbstractPatientCalculation extends BaseCalculation
    implements FGHPatientCalculation {

  protected static CalculationResultMap calculate(
      FGHPatientCalculation calculation,
      Collection<Integer> cohort,
      Map<String, Object> parameterValues,
      EvaluationContext calculationContext) {
    return calculation.evaluate(cohort, parameterValues, calculationContext);
  }

  protected static CalculationResultMap calculate(
      FGHPatientCalculation calculation,
      Map<String, Object> parameterValues,
      EvaluationContext calculationContext) {
    return calculation.evaluate(parameterValues, calculationContext);
  }
}
