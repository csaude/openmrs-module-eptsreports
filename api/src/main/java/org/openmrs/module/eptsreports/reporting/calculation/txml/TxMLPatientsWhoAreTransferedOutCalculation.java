package org.openmrs.module.eptsreports.reporting.calculation.txml;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eptsreports.metadata.HivMetadata;
import org.openmrs.module.eptsreports.reporting.calculation.BaseFghCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.BooleanResult;
import org.openmrs.module.eptsreports.reporting.calculation.generic.LastFilaCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.generic.LastRecepcaoLevantamentoCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.generic.LastSeguimentoCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.generic.MaxLastDateFromFilaSeguimentoRecepcaoCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.util.processor.CalculationProcessorUtils;
import org.openmrs.module.eptsreports.reporting.calculation.util.processor.QueryDisaggregationProcessor;
import org.openmrs.module.eptsreports.reporting.calculation.util.processor.TxMLPatientDisagregationProcessor;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class TxMLPatientsWhoAreTransferedOutCalculation extends BaseFghCalculation {

  @SuppressWarnings("unchecked")
  @Override
  public CalculationResultMap evaluate(
      Map<String, Object> parameterValues, EvaluationContext context) {
    CalculationResultMap resultMap = new CalculationResultMap();
    HivMetadata hivMetadata = Context.getRegisteredComponents(HivMetadata.class).get(0);
    CalculationResultMap numerator =
        Context.getRegisteredComponents(TxMLPatientsWhoMissedNextApointmentCalculation.class)
            .get(0)
            .evaluate(parameterValues, context);

    QueryDisaggregationProcessor queryDisaggregation =
        Context.getRegisteredComponents(QueryDisaggregationProcessor.class).get(0);

    Map<Integer, Date> transferedOutByProgram =
        queryDisaggregation
            .findMapMaxPatientStateDateByProgramAndPatientStateAndPatientStateEndDateNullAndEndDate(
                context,
                hivMetadata.getARTProgram(),
                hivMetadata.getTransferredOutToAnotherHealthFacilityWorkflowState());

    Map<Integer, Date> transferrdOutInFichaClinica =
        queryDisaggregation
            .findMapMaxEncounterDatetimeByEncountersAndQuestionsAndAnswerAndEndOfReportingPeriod(
                context,
                hivMetadata.getAdultoSeguimentoEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    hivMetadata.getStateOfStayPriorArtPatient().getConceptId(),
                    hivMetadata.getStateOfStayOfArtPatient().getConceptId()),
                Arrays.asList(hivMetadata.getTransferOutToAnotherFacilityConcept().getConceptId()));

    Map<Integer, Date> transferredOutInFichaResumo =
        queryDisaggregation
            .findMapMaxObsDatetimeByEncountersAndQuestionsAndAnswerAndEndOfReportingPeriod(
                context,
                hivMetadata.getMasterCardEncounterType().getEncounterTypeId(),
                Arrays.asList(
                    hivMetadata.getStateOfStayPriorArtPatient().getConceptId(),
                    hivMetadata.getStateOfStayOfArtPatient().getConceptId()),
                hivMetadata.getTransferOutToAnotherFacilityConcept());

    Map<Integer, Date> transferredOutInHomeVisitForm =
        queryDisaggregation.findMapMaxObsDatetimeByEncounterAndQuestionsAndAnswersInPeriod(
            context,
            hivMetadata.getBuscaActivaEncounterType(),
            Arrays.asList(hivMetadata.getDefaultingMotiveConcept().getConceptId()),
            Arrays.asList(
                hivMetadata.getTransferOutToAnotherFacilityConcept().getConceptId(),
                hivMetadata.getAutoTransfer().getConceptId()));

    Set<Integer> cohort = numerator.keySet();
    CalculationResultMap lastFilaCalculationResult =
        Context.getRegisteredComponents(LastFilaCalculation.class)
            .get(0)
            .evaluate(cohort, parameterValues, context);
    CalculationResultMap lastSeguimentoCalculationResult =
        Context.getRegisteredComponents(LastSeguimentoCalculation.class)
            .get(0)
            .evaluate(cohort, parameterValues, context);
    LastRecepcaoLevantamentoCalculation lastRecepcaoLevantamentoCalculation =
        Context.getRegisteredComponents(LastRecepcaoLevantamentoCalculation.class).get(0);
    CalculationResultMap lastRecepcaoLevantamentoResult =
        lastRecepcaoLevantamentoCalculation.evaluate(cohort, parameterValues, context);

    transferredOutInHomeVisitForm =
        TxMLPatientCalculation.excludeEarlyHomeVisitDatesFromNextExpectedDateNumerator(
            numerator,
            transferredOutInHomeVisitForm,
            lastFilaCalculationResult,
            lastSeguimentoCalculationResult,
            lastRecepcaoLevantamentoResult);

    Map<Integer, Date> maxResultFromAllSources =
        CalculationProcessorUtils.getMaxMapDateByPatient(
            transferedOutByProgram,
            transferredOutInHomeVisitForm,
            transferrdOutInFichaClinica,
            transferredOutInFichaResumo);

    // Excluir todos pacientes com consulta ou levantamento apos terem sido marcados
    // como transferidos para
    CalculationResultMap possiblePatientsToExclude =
        Context.getRegisteredComponents(MaxLastDateFromFilaSeguimentoRecepcaoCalculation.class)
            .get(0)
            .evaluate(parameterValues, context);
    CalculationResultMap deadExclusion =
        Context.getRegisteredComponents(TxMLPatientsWhoAreDeadCalculation.class)
            .get(0)
            .evaluate(parameterValues, context);

    for (Integer patientId : maxResultFromAllSources.keySet()) {
      Date candidateDate = maxResultFromAllSources.get(patientId);

      if (!(TxMLPatientDisagregationProcessor.hasPatientsFromOtherDisaggregationToExclude(
              patientId, deadExclusion)
          || TxMLPatientDisagregationProcessor.hasDatesGreatherThanEvaluatedDateToExclude(
              patientId, candidateDate, possiblePatientsToExclude))) {
        resultMap.put(patientId, new BooleanResult(Boolean.TRUE, this));
      }
    }
    return resultMap;
  }
}
