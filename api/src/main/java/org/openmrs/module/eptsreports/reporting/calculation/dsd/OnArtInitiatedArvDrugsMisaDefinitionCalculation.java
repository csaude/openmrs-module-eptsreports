package org.openmrs.module.eptsreports.reporting.calculation.dsd;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.eptsreports.reporting.calculation.BaseFghCalculation;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class OnArtInitiatedArvDrugsMisaDefinitionCalculation extends BaseFghCalculation {

  @Override
  public CalculationResultMap evaluate(
      Map<String, Object> parameterValues, EvaluationContext context) {

    CalculationResultMap resultMap = new CalculationResultMap();

    Map<Integer, ? extends Object> processorResult =
        Context.getRegisteredComponents(OnArtInitiatedArvDrugsMISAUDefinitionProcessor.class)
            .get(0)
            .getResutls(context);

    for (Integer pId : processorResult.keySet()) {
      Object dateObject = processorResult.get(pId);
      Date date = null;

      if (dateObject instanceof Date) {
        date = (Date) dateObject;
      } else if (dateObject instanceof LocalDateTime) {
        // Convert LocalDateTime to Date
        LocalDateTime localDateTime = (LocalDateTime) dateObject;
        date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
      }

      if (date != null) {
        resultMap.put(pId, new SimpleResult(date, this));
      }
    }
    return resultMap;
  }

  @Override
  public CalculationResultMap evaluate(
      Collection<Integer> cohort, Map<String, Object> parameterValues, EvaluationContext context) {
    return this.evaluate(parameterValues, context);
  }
}
