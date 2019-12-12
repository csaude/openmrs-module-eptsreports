package org.openmrs.module.eptsreports.reporting.calculation.txml;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.time.DateUtils;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.eptsreports.reporting.calculation.BooleanResult;
import org.openmrs.module.eptsreports.reporting.calculation.FGHAbstractPatientCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.generic.LastRecepcaoLevantamentoCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.generic.NextFilaDateCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.generic.NextSeguimentoDateCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.generic.OnArtInitiatedArvDrugsCalculation;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class TxMLPatientsWhoMissedNextApointmentCalculation extends FGHAbstractPatientCalculation {

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
      if (maxNextDate != null) {
        Date nextDatePlus28 = getDatePlusDays(maxNextDate, 28);
        if (nextDatePlus28.compareTo(startDate) > 0 && nextDatePlus28.compareTo(endDate) < 0) {
          isCandidate = true;
        }
      }
      resultMap.put(patientId, new BooleanResult(isCandidate, this));
    }
    return resultMap;
  }

  @Override
  public CalculationResultMap evaluate(
      Collection<Integer> cohort, Map<String, Object> parameterValues, EvaluationContext context) {
    return this.evaluate(parameterValues, context);
  }

  protected Date getMaxDate(Integer patientId, CalculationResultMap... calculationResulsts) {
    Date finalComparisonDate = DateUtil.getDateTime(Integer.MAX_VALUE, 1, 1);
    Date maxDate = DateUtil.getDateTime(Integer.MAX_VALUE, 1, 1);

    for (CalculationResultMap resultItem : calculationResulsts) {
      CalculationResult calculationResult = resultItem.get(patientId);
      if (calculationResult != null && calculationResult.getValue() != null) {
        Date date = (Date) calculationResult.getValue();

        if (date.compareTo(maxDate) > 0) {
          maxDate = date;
        }
      }
    }
    if (!DateUtils.isSameDay(maxDate, finalComparisonDate)) {
      return maxDate;
    }
    return null;
  }

  protected CalculationResultMap getLastRecepcaoLevantamentoPlus30(
      Integer patientId,
      CalculationResultMap lastRecepcaoLevantamentoResult,
      LastRecepcaoLevantamentoCalculation lastRecepcaoLevantamentoCalculation) {

    CalculationResultMap lastRecepcaoLevantamentoPlus30 = new CalculationResultMap();
    CalculationResult maxRecepcao = lastRecepcaoLevantamentoResult.get(patientId);
    if (maxRecepcao != null) {
      lastRecepcaoLevantamentoPlus30.put(
          patientId,
          new SimpleResult(
              this.getDatePlusDays((Date) maxRecepcao.getValue(), 30),
              lastRecepcaoLevantamentoCalculation));
    }
    return lastRecepcaoLevantamentoPlus30;
  }

  protected Date getDatePlusDays(Date date, int days) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DATE, days);
    return calendar.getTime();
  }
}
