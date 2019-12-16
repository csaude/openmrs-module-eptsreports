package org.openmrs.module.eptsreports.reporting.calculation.txcurr;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eptsreports.reporting.calculation.BooleanResult;
import org.openmrs.module.eptsreports.reporting.calculation.FGHAbstractPatientCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.generic.NextFilaDateCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.processor.LastFilaProcessor;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class TxCurrPatientsOnArtOnArvDispenseIntervalsCalculation
    extends FGHAbstractPatientCalculation {

  public static String DISACGREGATION_FACTOR = "DISACGREGATION_FACTOR";

  @Override
  public CalculationResultMap evaluate(
      Map<String, Object> parameterValues, EvaluationContext context) {
    CalculationResultMap resultMap = new CalculationResultMap();

    DisagregationRanges disagregationRange =
        (DisagregationRanges) parameterValues.get(DISACGREGATION_FACTOR);

    Map<Integer, Date> lastFilaDates =
        Context.getRegisteredComponents(LastFilaProcessor.class)
            .get(0)
            .getLastLevantamentoOnFila(context);

    CalculationResultMap nextFilaResults =
        Context.getRegisteredComponents(NextFilaDateCalculation.class)
            .get(0)
            .evaluate(lastFilaDates.keySet(), parameterValues, context);

    if (DisagregationRanges.LESS_THAN_3_MONTHS.equals(disagregationRange)) {
      this.disagregationForLessThan3Months(context, lastFilaDates, nextFilaResults, resultMap);
    }

    if (DisagregationRanges.BETWEEN_3_AND_5_MONTHS.equals(disagregationRange)) {
      this.disagregationBetween3And5Months(context, lastFilaDates, nextFilaResults, resultMap);
    }

    if (DisagregationRanges.FOR_6_OR_MORE_MONTHS.equals(disagregationRange)) {
      this.disagregationFor6OrMoreMonths(context, lastFilaDates, nextFilaResults, resultMap);
    }

    return resultMap;
  }

  @Override
  public CalculationResultMap evaluate(
      Collection<Integer> cohort, Map<String, Object> parameterValues, EvaluationContext context) {
    return this.evaluate(parameterValues, context);
  }

  private CalculationResultMap disagregationForLessThan3Months(
      EvaluationContext context,
      Map<Integer, Date> lastFilaDates,
      CalculationResultMap nextFilaResults,
      CalculationResultMap resultMap) {

    for (Integer patientId : lastFilaDates.keySet()) {
      Date lastFilaDate = lastFilaDates.get(patientId);
      CalculationResult nextFilaCResult = nextFilaResults.get(patientId);
      Date nextFilaDate = (Date) ((nextFilaCResult != null) ? nextFilaCResult.getValue() : null);

      if (nextFilaDate != null && DateUtil.getDaysBetween(lastFilaDate, nextFilaDate) < 83) {
        resultMap.put(patientId, new BooleanResult(Boolean.TRUE, this));
      }
    }

    Map<Integer, Date> tipoLevantamentoMensal =
        Context.getRegisteredComponents(LastFilaProcessor.class)
            .get(0)
            .getLastTipoDeLevantamentoOnFichaClinicaMasterCard(
                context, Integer.valueOf(23739), Integer.valueOf(1098));

    for (Integer patientId : tipoLevantamentoMensal.keySet()) {

      if (!resultMap.containsKey(patientId)) {
        resultMap.put(patientId, new BooleanResult(Boolean.TRUE, this));
      }
    }

    return resultMap;
  }

  private CalculationResultMap disagregationBetween3And5Months(
      EvaluationContext context,
      Map<Integer, Date> lastFilaDates,
      CalculationResultMap nextFilaResults,
      CalculationResultMap resultMap) {

    for (Integer patientId : lastFilaDates.keySet()) {
      Date lastFilaDate = lastFilaDates.get(patientId);
      CalculationResult nextFilaCResult = nextFilaResults.get(patientId);
      Date nextFilaDate = (Date) ((nextFilaCResult != null) ? nextFilaCResult.getValue() : null);

      if (nextFilaDate != null) {
        int daysBetween = DateUtil.getDaysBetween(lastFilaDate, nextFilaDate);
        if (daysBetween >= 83 && daysBetween <= 173) {
          resultMap.put(patientId, new BooleanResult(Boolean.TRUE, this));
        }
      }
    }

    Map<Integer, Date> tipoLevantamentoTrimestral =
        Context.getRegisteredComponents(LastFilaProcessor.class)
            .get(0)
            .getLastTipoDeLevantamentoOnFichaClinicaMasterCard(
                context, Integer.valueOf(23739), Integer.valueOf(23720));

    Map<Integer, Date> modelosDiferenciadosTrimestral =
        Context.getRegisteredComponents(LastFilaProcessor.class)
            .get(0)
            .getLastMarkedInModelosDiferenciadosDeCuidadosOnFichaClinicaMasterCard(context, 23730);

    for (Integer patientId : tipoLevantamentoTrimestral.keySet()) {

      if (!resultMap.containsKey(patientId)) {
        resultMap.put(patientId, new BooleanResult(Boolean.TRUE, this));
      }
    }

    for (Integer patientId : modelosDiferenciadosTrimestral.keySet()) {

      if (!resultMap.containsKey(patientId)) {
        resultMap.put(patientId, new BooleanResult(Boolean.TRUE, this));
      }
    }
    return resultMap;
  }

  private CalculationResultMap disagregationFor6OrMoreMonths(
      EvaluationContext context,
      Map<Integer, Date> lastFilaDates,
      CalculationResultMap nextFilaResults,
      CalculationResultMap resultMap) {

    for (Integer patientId : lastFilaDates.keySet()) {
      Date lastFilaDate = lastFilaDates.get(patientId);
      CalculationResult nextFilaCResult = nextFilaResults.get(patientId);
      Date nextFilaDate = (Date) ((nextFilaCResult != null) ? nextFilaCResult.getValue() : null);

      if (nextFilaDate != null) {
        int daysBetween = DateUtil.getDaysBetween(lastFilaDate, nextFilaDate);
        if (daysBetween > 173) {
          resultMap.put(patientId, new BooleanResult(Boolean.TRUE, this));
        }
      }
    }

    Map<Integer, Date> tipoLevantamentoSemestral =
        Context.getRegisteredComponents(LastFilaProcessor.class)
            .get(0)
            .getLastTipoDeLevantamentoOnFichaClinicaMasterCard(
                context, Integer.valueOf(23739), Integer.valueOf(23888));

    Map<Integer, Date> modelosDiferenciadosSemestral =
        Context.getRegisteredComponents(LastFilaProcessor.class)
            .get(0)
            .getLastMarkedInModelosDiferenciadosDeCuidadosOnFichaClinicaMasterCard(context, 23888);

    for (Integer patientId : tipoLevantamentoSemestral.keySet()) {

      if (!resultMap.containsKey(patientId)) {
        resultMap.put(patientId, new BooleanResult(Boolean.TRUE, this));
      }
    }

    for (Integer patientId : modelosDiferenciadosSemestral.keySet()) {

      if (!resultMap.containsKey(patientId)) {
        resultMap.put(patientId, new BooleanResult(Boolean.TRUE, this));
      }
    }
    return resultMap;
  }
}
