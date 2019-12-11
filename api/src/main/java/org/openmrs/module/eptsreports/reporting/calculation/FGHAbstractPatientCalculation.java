package org.openmrs.module.eptsreports.reporting.calculation;

import java.util.Map;
import org.openmrs.calculation.BaseCalculation;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.reporting.evaluation.EvaluationContext;

public abstract class FGHAbstractPatientCalculation extends BaseCalculation
    implements FGHPatientCalculation {

  @Override
  public CalculationResultMap evaluate(
      Map<String, Object> parameterValues, EvaluationContext context) {
    throw new IllegalArgumentException(
        String.format(
            "The Calculation '%s' Requires a specific Cohort of patients Argument",
            this.getClass().getName()));
  }
}
