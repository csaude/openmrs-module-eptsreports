package org.openmrs.module.eptsreports.reporting.calculation.generic;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.eptsreports.reporting.calculation.FGHAbstractPatientCalculation;
import org.springframework.stereotype.Component;

@Component
public class OnArtInitiatedArvDrugsCalculation extends FGHAbstractPatientCalculation {

  @Override
  public CalculationResultMap evaluate( // FGHCalculationCohortDefinition cohortDefinition,
      Map<String, Object> parameterValues, PatientCalculationContext context) {

    CalculationResultMap resultMap = new CalculationResultMap();
    Map<Integer, Object> processorResult =
        Context.getRegisteredComponents(OnArtInitiatedArvDrugsSQLProcessor.class)
            .get(0)
            .getResutls();
    for (Integer pId : processorResult.keySet()) {
      Date date = (Date) processorResult.get(pId);
      if (date != null) {
        resultMap.put(pId, new SimpleResult(date, this));
      }
    }
    return resultMap;
  }

  @Override
  public CalculationResultMap evaluate( // FGHCalculationCohortDefinition cohortDefinition,
      Collection<Integer> cohort,
      Map<String, Object> parameterValues,
      PatientCalculationContext context) {
    return this.evaluate(parameterValues, context);
  }
}
