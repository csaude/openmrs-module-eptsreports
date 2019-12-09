package org.openmrs.module.eptsreports.reporting.calculation.generic;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.time.DateUtils;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.eptsreports.reporting.calculation.FGHAbstractPatientCalculation;
import org.openmrs.module.reporting.common.DateUtil;
import org.springframework.stereotype.Component;

@Component
public class TxCurrCalculation extends FGHAbstractPatientCalculation {

  @Override
  public CalculationResultMap evaluate( // FGHCalculationCohortDefinition cohortDefinition,
      Map<String, Object> parameterValues, PatientCalculationContext context) {
    CalculationResultMap resultMap = new CalculationResultMap();

    Date onOrBefore = (Date) context.getFromCache("onOrBefore");

    CalculationResultMap inicioRealResult =
        super.calculate(
            Context.getRegisteredComponents(OnArtInitiatedArvDrugsCalculation.class).get(0),
            parameterValues,
            context);
    Set<Integer> cohort = inicioRealResult.keySet();

    CalculationResultMap saidaResult =
        super.calculate(
            Context.getRegisteredComponents(LastPatientStateCalculation.class).get(0),
            cohort,
            parameterValues,
            context);

    CalculationResultMap lastFilaResult =
        super.calculate(
            Context.getRegisteredComponents(LastFilaCalculation.class).get(0),
            cohort,
            parameterValues,
            context);

    CalculationResultMap lastSeguimentoResult =
        super.calculate(
            Context.getRegisteredComponents(LastSeguimentoCalculation.class).get(0),
            cohort,
            parameterValues,
            context);

    CalculationResultMap lastRecepcaoLevantamentoResult =
        super.calculate(
            Context.getRegisteredComponents(LastRecepcaoLevantamentoCalculation.class).get(0),
            cohort,
            parameterValues,
            context);

    CalculationResultMap nextFilaResult =
        super.calculate(
            Context.getRegisteredComponents(NextFilaDateCalculation.class).get(0),
            cohort,
            parameterValues,
            context);

    CalculationResultMap nextSeguimentoResult =
        super.calculate(
            Context.getRegisteredComponents(NextSeguimentoDateCalculation.class).get(0),
            cohort,
            parameterValues,
            context);

    int countTrue = 0;
    for (Integer patientId : cohort) {
      boolean isCandidate = false;
      Date lastDateState =
          (Date)
              (saidaResult.get(patientId) != null ? saidaResult.get(patientId).getValue() : null);
      Date maxLastDate =
          getMaxDate(
              patientId, lastFilaResult, lastSeguimentoResult, lastRecepcaoLevantamentoResult);
      Date maxNextDate =
          getMaxDate(
              patientId,
              nextFilaResult,
              nextSeguimentoResult,
              getLastRecepcaoLevantamentoPlus30(patientId, lastRecepcaoLevantamentoResult));
      if (maxLastDate != null && maxNextDate != null) {
        Date nextDatePlus30 = getDatePlusDays(maxNextDate, 30);
        if ((lastDateState == null || (lastDateState.compareTo(maxLastDate) < 0))
            && nextDatePlus30.compareTo(onOrBefore) > 0) {
          isCandidate = true;
          countTrue++;
        }
      }
      resultMap.put(patientId, new SimpleResult(isCandidate, this));
    }

    System.out.println("MNresultMap =>" + resultMap.size());
    System.out.println("TERMINOU => END. TOTAL PASSING => " + countTrue);

    return resultMap;
  }

  @Override
  public CalculationResultMap evaluate( // FGHCalculationCohortDefinition cohortDefinition,
      Collection<Integer> cohort,
      Map<String, Object> parameterValues,
      PatientCalculationContext context) {
    return this.evaluate(parameterValues, context);
  }

  private Date getMaxDate(Integer patientId, CalculationResultMap... calculationResulsts) {
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

  private CalculationResultMap getLastRecepcaoLevantamentoPlus30(
      Integer patientId, CalculationResultMap lastRecepcaoLevantamentoResult) {

    CalculationResultMap lastRecepcaoLevantamentoPlus30 = new CalculationResultMap();
    CalculationResult maxRecepcao = lastRecepcaoLevantamentoResult.get(patientId);
    if (maxRecepcao != null) {
      lastRecepcaoLevantamentoPlus30.put(
          patientId,
          new SimpleResult(
              this.getDatePlusDays((Date) maxRecepcao.getValue(), 30),
              Context.getRegisteredComponents(LastRecepcaoLevantamentoCalculation.class).get(0)));
    }
    return lastRecepcaoLevantamentoPlus30;
  }

  private Date getDatePlusDays(Date date, int days) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DATE, days);
    return calendar.getTime();
  }
}
