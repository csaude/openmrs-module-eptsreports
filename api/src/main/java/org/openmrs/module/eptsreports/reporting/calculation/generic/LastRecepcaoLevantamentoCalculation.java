package org.openmrs.module.eptsreports.reporting.calculation.generic;

import java.util.Collection;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.eptsreports.reporting.calculation.FGHAbstractPatientCalculation;
import org.springframework.stereotype.Component;

@Component
public class LastRecepcaoLevantamentoCalculation extends FGHAbstractPatientCalculation {

  @Override
  public CalculationResultMap evaluate( // FGHCalculationCohortDefinition cohortDefinition,
      Map<String, Object> parameterValues, PatientCalculationContext context) {
    throw new IllegalArgumentException(
        String.format(
            "The Calculation '%s' Requires a specific Cohort of patients Argument",
            this.getClass().getName()));
  }

  @Override
  public CalculationResultMap evaluate( // FGHCalculationCohortDefinition cohortDefinition,
      Collection<Integer> cohort,
      Map<String, Object> parameterValues,
      PatientCalculationContext context) {
    Map<Integer, Object> processorResult =
        Context.getRegisteredComponents(LastRecepcaoLevantamentoProcessor.class)
            .get(0)
            .getResutls();

    CalculationResultMap resultMap = new CalculationResultMap();
    for (Integer patientId : cohort) {
      if (processorResult.get(patientId) != null) {
        resultMap.put(patientId, new SimpleResult(processorResult.get(patientId), this));
      }
    }
    return resultMap;
  }
}
