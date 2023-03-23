package org.openmrs.module.eptsreports.reporting.calculation.txml;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.eptsreports.reporting.calculation.BaseFghCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.generic.LastRecepcaoLevantamentoCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.generic.TXMLLostFollowupCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.util.processor.CalculationProcessorUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsDateUtil;
import org.openmrs.module.reporting.evaluation.EvaluationContext;

public abstract class TxMLPatientCalculation extends BaseFghCalculation {

  @Override
  public CalculationResultMap evaluate(
      Map<String, Object> parameterValues, EvaluationContext context) {

    CalculationResultMap txmlResults =
        Context.getRegisteredComponents(TXMLLostFollowupCalculation.class)
            .get(0)
            .evaluate(null, parameterValues, context);

    return this.evaluateUsingCalculationRules(context, txmlResults);
  }

  @Override
  public CalculationResultMap evaluate(
      Collection<Integer> cohort, Map<String, Object> parameterValues, EvaluationContext context) {
    return this.evaluate(parameterValues, context);
  }

  protected abstract CalculationResultMap evaluateUsingCalculationRules(
      EvaluationContext context, CalculationResultMap txmlResults);

  protected void checkConsultationsOrFilaWithoutNextConsultationDate(
      Integer patientId,
      CalculationResultMap resultMap,
      Date endDate,
      CalculationResultMap lastResult,
      CalculationResultMap nextResult) {

    CalculationResult calculationLastResult = lastResult.get(patientId);
    CalculationResult calculationNextResult = nextResult.get(patientId);

    if (calculationNextResult != null && calculationNextResult.getValue() == null) {
      if (calculationLastResult != null) {
        Date lastDate = (Date) calculationLastResult.getValue();
        if (EptsDateUtil.getDaysBetween(lastDate, endDate) >= 0) {
          resultMap.put(patientId, new SimpleResult(lastDate, this));
        }
      }
    }
  }

  public static CalculationResultMap getLastRecepcaoLevantamentoPlus30(
      Integer patientId,
      CalculationResultMap lastRecepcaoLevantamentoResult,
      LastRecepcaoLevantamentoCalculation lastRecepcaoLevantamentoCalculation) {

    CalculationResultMap lastRecepcaoLevantamentoPlus30 = new CalculationResultMap();
    CalculationResult maxRecepcao = lastRecepcaoLevantamentoResult.get(patientId);
    if (maxRecepcao != null) {
      lastRecepcaoLevantamentoPlus30.put(
          patientId,
          new SimpleResult(
              CalculationProcessorUtils.adjustDaysInDate((Date) maxRecepcao.getValue(), 30),
              lastRecepcaoLevantamentoCalculation));
    }
    return lastRecepcaoLevantamentoPlus30;
  }

  public static Map<Integer, Date> excludeEarlyHomeVisitDatesFromNextExpectedDateNumerator(
      CalculationResultMap numerator,
      Map<Integer, Date> deadInHomeVisitForm,
      CalculationResultMap lastFilaCalculationResult,
      CalculationResultMap lastSeguimentoCalculationResult,
      CalculationResultMap lastRecepcaoLevantamentoResult) {
    Map<Integer, Date> result = new HashMap<>();
    for (Integer patientId : numerator.keySet()) {
      Date maxDate =
          CalculationProcessorUtils.getMaxDate(
              patientId,
              lastFilaCalculationResult,
              lastSeguimentoCalculationResult,
              lastRecepcaoLevantamentoResult);

      if (maxDate != null) {
        Date candidateDate = deadInHomeVisitForm.get(patientId);
        if (candidateDate != null) {
          if (candidateDate.compareTo(maxDate) > 0) {
            result.put(patientId, candidateDate);
          }
        }
      }
    }
    return result;
  }
}
