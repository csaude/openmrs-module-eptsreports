package org.openmrs.module.eptsreports.reporting.calculation;

import java.util.Collection;
import java.util.Map;
import org.openmrs.calculation.Calculation;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;

public interface FGHPatientCalculation extends Calculation {

  CalculationResultMap evaluate(
      Map<String, Object> parameterValues, PatientCalculationContext context);

  CalculationResultMap evaluate(
      Collection<Integer> cohort,
      Map<String, Object> parameterValues,
      PatientCalculationContext context);
}
