package org.openmrs.module.eptsreports.reporting.calculation.dsd;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.eptsreports.reporting.calculation.BaseFghCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.BooleanResult;
import org.openmrs.module.eptsreports.reporting.calculation.generic.LastFilaCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.generic.LastRecepcaoLevantamentoCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.generic.TxRttNextFilaUntilEndDateCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.txml.TxMLPatientCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.txml.TxMLPatientsWhoMissedNextApointmentCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.util.processor.CalculationProcessorUtils;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.common.DurationUnit;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class DSDPatientsWhoExperiencedIITCalculation extends BaseFghCalculation {

  @Override
  public CalculationResultMap evaluate(
      Map<String, Object> parameterValues, EvaluationContext context) {
    CalculationResultMap resultMap = new CalculationResultMap();

    Location location = (Location) context.getParameterValues().get("location");
    Date endDate = (Date) context.getParameterValues().get("endDate");

    Date endDateMinus3Months = DateUtil.adjustDate(endDate, -3, DurationUnit.MONTHS);

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("endDate", endDateMinus3Months);
    parameters.put("location", location);

    EvaluationContext newContext =
        TxMLPatientsWhoMissedNextApointmentCalculation.getNewEvaluationContext(parameters);

    CalculationResultMap inicioRealResult =
        Context.getRegisteredComponents(OnArtInitiatedArvDrugsMisaDefinitionCalculation.class)
            .get(0)
            .evaluate(parameterValues, context);

    Set<Integer> cohort = inicioRealResult.keySet();

    CalculationResultMap lastFilaCalculationResult =
        Context.getRegisteredComponents(LastFilaCalculation.class)
            .get(0)
            .evaluate(cohort, parameterValues, newContext);

    LastRecepcaoLevantamentoCalculation lastRecepcaoLevantamentoCalculation =
        Context.getRegisteredComponents(LastRecepcaoLevantamentoCalculation.class).get(0);

    CalculationResultMap lastRecepcaoLevantamentoResult =
        lastRecepcaoLevantamentoCalculation.evaluate(cohort, parameterValues, newContext);

    CalculationResultMap nextFilaResult =
        Context.getRegisteredComponents(TxRttNextFilaUntilEndDateCalculation.class)
            .get(0)
            .evaluate(lastFilaCalculationResult.keySet(), parameterValues, newContext);

    return this.evaluateUsingCalculationRules(
        cohort,
        endDateMinus3Months,
        resultMap,
        lastFilaCalculationResult,
        nextFilaResult,
        lastRecepcaoLevantamentoResult,
        lastRecepcaoLevantamentoCalculation);
  }

  protected CalculationResultMap evaluateUsingCalculationRules(
      Set<Integer> cohort,
      Date endDate,
      CalculationResultMap resultMap,
      CalculationResultMap lastFilaCalculationResult,
      CalculationResultMap nextFilaResult,
      CalculationResultMap lastRecepcaoLevantamentoResult,
      LastRecepcaoLevantamentoCalculation lastRecepcaoLevantamentoCalculation) {

    for (Integer patientId : cohort) {
      Date maxNextDate =
          CalculationProcessorUtils.getMaxDate(
              patientId,
              nextFilaResult,
              TxMLPatientCalculation.getLastRecepcaoLevantamentoPlus30(
                  patientId, lastRecepcaoLevantamentoResult, lastRecepcaoLevantamentoCalculation));

      if (maxNextDate != null) {
        Date nextDatePlus59 = CalculationProcessorUtils.adjustDaysInDate(maxNextDate, 59);

        if (nextDatePlus59.compareTo(endDate) < 0) {
          resultMap.put(patientId, new SimpleResult(maxNextDate, this));
        }
      } else {
        this.checkConsultationsOrFilaWithoutNextConsultationDate(
            patientId,
            resultMap,
            lastFilaCalculationResult,
            nextFilaResult,
            lastRecepcaoLevantamentoResult);
      }
    }
    return resultMap;
  }

  protected void checkConsultationsOrFilaWithoutNextConsultationDate(
      Integer patientId,
      CalculationResultMap resultMap,
      CalculationResultMap lastFilaCalculationResult,
      CalculationResultMap nextFilaCalculationResult,
      CalculationResultMap lastRecepcaoLevantamentoCalculationResult) {

    CalculationResult lastFilaResult = lastFilaCalculationResult.get(patientId);
    CalculationResult nextFilaResult = nextFilaCalculationResult.get(patientId);
    CalculationResult lastRecepcaoResult = lastRecepcaoLevantamentoCalculationResult.get(patientId);

    if (lastFilaCalculationResult != null
        && lastFilaResult != null
        && nextFilaResult == null
        && lastRecepcaoResult == null) {
      resultMap.put(patientId, new BooleanResult(true, this));
    }
  }
}
