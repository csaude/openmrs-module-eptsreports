package org.openmrs.module.eptsreports.reporting.calculation.generic;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.time.DateUtils;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.ListResult;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.eptsreports.metadata.HivMetadata;
import org.openmrs.module.eptsreports.reporting.calculation.FGHAbstractPatientCalculation;
import org.openmrs.module.eptsreports.reporting.calculation.common.EPTSCalculationService;
import org.openmrs.module.eptsreports.reporting.utils.EptsCalculationUtils;
import org.openmrs.module.reporting.common.DateUtil;
import org.springframework.stereotype.Component;

@Component
public class NextSeguimentoDateCalculation extends FGHAbstractPatientCalculation {

  @Override
  public CalculationResultMap evaluate( // FGHCalculationCohortDefinition cohortDefinition,
      Map<String, Object> parameterValues, PatientCalculationContext context) {
    throw new IllegalArgumentException(
        String.format(
            "The Calculation '%s' Requires a specific Cohort of patients Argument",
            this.getClass().getName()));
  }

  @Override
  public CalculationResultMap evaluate( // FGHCalculationCohortDefinition cohortDefinition,
      Collection<Integer> cohort,
      Map<String, Object> parameterValues,
      PatientCalculationContext context) {

    CalculationResultMap resultMap = new CalculationResultMap();

    Location location = (Location) context.getFromCache("location");
    CalculationResultMap lastSeguimentoResult =
        calculate(
            Context.getRegisteredComponents(LastSeguimentoCalculation.class).get(0),
            cohort,
            parameterValues,
            context);

    HivMetadata hivMetadata = Context.getRegisteredComponents(HivMetadata.class).get(0);
    CalculationResultMap obsSegmentoResult =
        Context.getRegisteredComponents(EPTSCalculationService.class)
            .get(0)
            .allObservations(
                hivMetadata.getReturnVisitDateConcept(),
                null,
                Arrays.asList(
                    hivMetadata.getARVPharmaciaEncounterType(),
                    hivMetadata.getAdultoSeguimentoEncounterType(),
                    hivMetadata.getARVPediatriaSeguimentoEncounterType()),
                location,
                cohort,
                context);

    for (Integer patientId : obsSegmentoResult.keySet()) {

      CalculationResult calculationResult = lastSeguimentoResult.get(patientId);

      Date lastDateSeguimento =
          (Date) (calculationResult != null ? calculationResult.getValue() : null);

      List<Obs> allObsSeguimento =
          EptsCalculationUtils.extractResultValues((ListResult) obsSegmentoResult.get(patientId));

      this.setMaxValueDateTime(patientId, lastDateSeguimento, allObsSeguimento, resultMap);
    }
    return resultMap;
  }

  private void setMaxValueDateTime(
      Integer pId,
      Date lastDateSeguimento,
      List<Obs> allObsSeguimento,
      CalculationResultMap resultMap) {

    Date finalComparisonDate = DateUtil.getDateTime(Integer.MAX_VALUE, 1, 1);
    Date maxDate = DateUtil.getDateTime(Integer.MAX_VALUE, 1, 1);

    if (lastDateSeguimento != null) {

      for (Obs obs : allObsSeguimento) {
        if (obs != null && obs.getObsDatetime() != null) {
          if (obs.getObsDatetime().compareTo(lastDateSeguimento) == 0) {
            Date valueDatetime = obs.getValueDatetime();
            if (valueDatetime != null && valueDatetime.compareTo(maxDate) > 0) {
              maxDate = valueDatetime;
            }
          }
        } else {
          System.out.println("Erro na OBS da NextDrugPickUpDateCalculation => " + obs);
        }
      }
    }
    if (!DateUtils.isSameDay(maxDate, finalComparisonDate)) {
      resultMap.put(pId, new SimpleResult(maxDate, this));
    }
  }
}
