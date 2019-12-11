package org.openmrs.module.eptsreports.reporting.calculation.processor;

import java.util.Date;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.stereotype.Component;

@Component
public class LastPatientStateProcessor {

  @SuppressWarnings("unchecked")
  public Map<Integer, Date> getResutls(EvaluationContext context) {

    Map<Integer, Date> patientProgramState = getMaxProgramState(context);

    Map<Integer, Date> fichaResumoEstadoPermanenciaState = getEstadoPermanenciaFichaResumo(context);

    Map<Integer, Date> obitoDemoGraficoState = getObitoDemograficoState(context);

    Map<Integer, Date> obitoFichaDeBuscaState = getMaxObitoFichadeBusca(context);

    return CalculationProcessorUtils.getMaxMapDateByPatient(
        patientProgramState,
        fichaResumoEstadoPermanenciaState,
        obitoDemoGraficoState,
        obitoFichaDeBuscaState);
  }

  /* Estado no programa */
  private Map<Integer, Date> getMaxProgramState(EvaluationContext context) {

    SqlQueryBuilder qb =
        new SqlQueryBuilder(
            "select pg.patient_id, max(ps.start_date) data_estado from 	patient p "
                + "					inner join patient_program pg on p.patient_id=pg.patient_id "
                + "					inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
                + "			where 	pg.voided=0 and ps.voided=0 and p.voided=0 and "
                + "					pg.program_id=2 and ps.state in (7,8,10) and ps.end_date is null and "
                + "					ps.start_date<= :endDate and location_id= :location "
                + "			group by pg.patient_id",
            context.getParameterValues());

    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }

  /* Estado no estado de permanencia da ficha resumo */
  public Map<Integer, Date> getEstadoPermanenciaFichaResumo(EvaluationContext context) {

    SqlQueryBuilder qb =
        new SqlQueryBuilder(
            "select p.patient_id, max(o.obs_datetime) data_estado from patient p "
                + "					inner join encounter e on p.patient_id=e.patient_id "
                + "					inner join obs  o on e.encounter_id=o.encounter_id "
                + "			where 	e.voided=0 and o.voided=0 and p.voided=0 and "
                + "					e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1706,1366,1709) and "
                + "					o.obs_datetime<= :endDate and e.location_id= :location "
                + "			group by p.patient_id",
            context.getParameterValues());

    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }

  /* Obito demografico */
  private Map<Integer, Date> getObitoDemograficoState(EvaluationContext context) {

    SqlQueryBuilder qb =
        new SqlQueryBuilder(
            "select person_id as patient_id,death_date as data_estado from person "
                + "			where dead=1 and death_date is not null and death_date<= :endDate ",
            context.getParameterValues());
    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }

  /* Obito na ficha de busca */
  private Map<Integer, Date> getMaxObitoFichadeBusca(EvaluationContext context) {

    SqlQueryBuilder qb =
        new SqlQueryBuilder(
            "select p.patient_id, max(obsObito.obs_datetime) data_estado "
                + "			from 	patient p "
                + "					inner join encounter e on p.patient_id=e.patient_id "
                + "					inner join obs obsEncontrado on e.encounter_id=obsEncontrado.encounter_id "
                + "					inner join obs obsObito on e.encounter_id=obsObito.encounter_id "
                + "			where 	e.voided=0 and obsEncontrado.voided=0 and p.voided=0 and obsObito.voided=0 and "
                + "					e.encounter_type in (21,36,37) and  e.encounter_datetime<= :endDate and  e.location_id= :location and "
                + "					obsEncontrado.concept_id in (2003, 6348) and obsEncontrado.value_coded=1066 and "
                + "					obsObito.concept_id=2031 and obsObito.value_coded=1383 "
                + "			group by p.patient_id",
            context.getParameterValues());
    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }
}
