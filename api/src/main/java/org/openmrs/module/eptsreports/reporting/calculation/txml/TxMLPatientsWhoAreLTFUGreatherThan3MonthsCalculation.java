package org.openmrs.module.eptsreports.reporting.calculation.txml;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eptsreports.reporting.calculation.BooleanResult;
import org.openmrs.module.eptsreports.reporting.calculation.generic.LastFilaCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.generic.LastRecepcaoLevantamentoCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.generic.LastSeguimentoCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.generic.NextFilaDateCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.generic.NextSeguimentoDateCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.generic.OnArtInitiatedArvDrugsCalculation;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class TxMLPatientsWhoAreLTFUGreatherThan3MonthsCalculation
    extends TxMLPatientsWhoMissedNextApointmentCalculation {

  @Override
  public CalculationResultMap evaluate(
      Map<String, Object> parameterValues, EvaluationContext context) {
    CalculationResultMap resultMap = new CalculationResultMap();

    Date startDate = (Date) context.getFromCache("startDate");
    Date endDate = (Date) context.getFromCache("endDate");

    CalculationResultMap inicioRealResult =
        Context.getRegisteredComponents(OnArtInitiatedArvDrugsCalculation.class)
            .get(0)
            .evaluate(parameterValues, context);

    Set<Integer> cohort = inicioRealResult.keySet();

    CalculationResultMap lastFilaResult =
        Context.getRegisteredComponents(LastFilaCalculation.class)
            .get(0)
            .evaluate(cohort, parameterValues, context);
    context.removeFromCache("lastFilaResult");
    context.addToCache("lastFilaResult", lastFilaResult);

    CalculationResultMap lastSeguimentoResult =
        Context.getRegisteredComponents(LastSeguimentoCalculation.class)
            .get(0)
            .evaluate(cohort, parameterValues, context);

    context.removeFromCache("lastSeguimentoResult");
    context.addToCache("lastSeguimentoResult", lastSeguimentoResult);

    LastRecepcaoLevantamentoCalculation lastRecepcaoLevantamentoCalculation =
        Context.getRegisteredComponents(LastRecepcaoLevantamentoCalculation.class).get(0);
    CalculationResultMap lastRecepcaoLevantamentoResult =
        lastRecepcaoLevantamentoCalculation.evaluate(cohort, parameterValues, context);

    CalculationResultMap nextFilaResult =
        Context.getRegisteredComponents(NextFilaDateCalculation.class)
            .get(0)
            .evaluate(cohort, parameterValues, context);

    CalculationResultMap nextSeguimentoResult =
        Context.getRegisteredComponents(NextSeguimentoDateCalculation.class)
            .get(0)
            .evaluate(cohort, parameterValues, context);

    for (Integer patientId : cohort) {

      boolean isCandidate = false;
      Date maxNextDate =
          getMaxDate(
              patientId,
              nextFilaResult,
              nextSeguimentoResult,
              getLastRecepcaoLevantamentoPlus30(
                  patientId, lastRecepcaoLevantamentoResult, lastRecepcaoLevantamentoCalculation));

      if ((maxNextDate != null && DateUtil.getDaysBetween(endDate, maxNextDate) >= 90)) {

        Date nextDatePlus28 = getDatePlusDays(maxNextDate, 28);

        if (nextDatePlus28.compareTo(startDate) > 0 && nextDatePlus28.compareTo(endDate) < 0) {
          isCandidate = true;
        }
      }
      resultMap.put(patientId, new BooleanResult(isCandidate, this));
    }
    return resultMap;
  }
}
