package org.openmrs.module.eptsreports.reporting.calculation;

import java.util.Collection;
import java.util.Map;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.reporting.evaluation.EvaluationContext;

public interface FGHPatientCalculation {

  CalculationResultMap evaluate(Map<String, Object> parameterValues, EvaluationContext context);

  CalculationResultMap evaluate(
      Collection<Integer> cohort, Map<String, Object> parameterValues, EvaluationContext context);
}
