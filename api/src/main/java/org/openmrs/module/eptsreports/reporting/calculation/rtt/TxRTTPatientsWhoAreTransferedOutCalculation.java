package org.openmrs.module.eptsreports.reporting.calculation.rtt;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eptsreports.metadata.HivMetadata;
import org.openmrs.module.eptsreports.reporting.calculation.BaseFghCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.BooleanResult;
import org.openmrs.module.eptsreports.reporting.calculation.generic.MaxLastDateFromFilaSeguimentoRecepcaoCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.util.processor.CalculationProcessorUtils;
import org.openmrs.module.eptsreports.reporting.calculation.util.processor.QueryDisaggregationProcessor;
import org.openmrs.module.eptsreports.reporting.calculation.util.processor.TxMLPatientDisagregationProcessor;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class TxRTTPatientsWhoAreTransferedOutCalculation extends BaseFghCalculation {

  public static Integer AUTO_TRANSFER = 23863;

  @SuppressWarnings("unchecked")
  @Override
  public CalculationResultMap evaluate(
      Map<String, Object> parameterValues, EvaluationContext context) {
    CalculationResultMap resultMap = new CalculationResultMap();
    HivMetadata hivMetadata = Context.getRegisteredComponents(HivMetadata.class).get(0);

    QueryDisaggregationProcessor queryDisaggregation =
        Context.getRegisteredComponents(QueryDisaggregationProcessor.class).get(0);

    Map<Integer, Date> transferedOutByProgram =
        queryDisaggregation.findMapMaxPatientStateDateByProgramAndPatientStateAndEndDate(
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

    Map<Integer, Date> lastTransferrdOutInHomeVisitCard =
        queryDisaggregation.findMapfindLastTransferredOutInHomVistCardByReportingEndDate(
            context,
            Arrays.asList(hivMetadata.getDefaultingMotiveConcept().getConceptId()),
            Arrays.asList(
                hivMetadata.getTransferOutToAnotherFacilityConcept().getConceptId(),
                AUTO_TRANSFER));

    Map<Integer, Date> maxResultFromAllSources =
        CalculationProcessorUtils.getMaxMapDateByPatient(
            transferedOutByProgram,
            transferrdOutInFichaClinica,
            transferredOutInFichaResumo,
            lastTransferrdOutInHomeVisitCard);

    // Excluir todos pacientes com consulta ou levantamento apos terem sido marcados
    // como transferidos para
    CalculationResultMap possiblePatientsToExclude =
        Context.getRegisteredComponents(MaxLastDateFromFilaSeguimentoRecepcaoCalculation.class)
            .get(0)
            .evaluate(parameterValues, context);

    for (Integer patientId : maxResultFromAllSources.keySet()) {
      Date candidateDate = maxResultFromAllSources.get(patientId);

      if (!(TxMLPatientDisagregationProcessor.hasDatesGreatherThanEvaluatedDateToExclude(
          patientId, candidateDate, possiblePatientsToExclude))) {
        resultMap.put(patientId, new BooleanResult(Boolean.TRUE, this));
      }
    }
    return resultMap;
  }
}
