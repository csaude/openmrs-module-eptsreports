package org.openmrs.module.eptsreports.reporting.calculation;

import java.util.Collection;
import java.util.Map;
import org.openmrs.calculation.Calculation;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.reporting.evaluation.EvaluationContext;

public interface FGHPatientCalculation extends Calculation {

  CalculationResultMap evaluate(Map<String, Object> parameterValues, EvaluationContext context);

  CalculationResultMap evaluate(
      Collection<Integer> cohort, Map<String, Object> parameterValues, EvaluationContext context);
}
