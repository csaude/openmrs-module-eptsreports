package org.openmrs.module.eptsreports.reporting.calculation.processor;

import java.util.Date;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.stereotype.Component;

@Component
public class LastFilaProcessor {

  public Map<Integer, Date> getLastLevantamentoOnFila(EvaluationContext context) {

    SqlQueryBuilder qb =
        new SqlQueryBuilder(
            "Select p.patient_id,max(encounter_datetime) data_fila from patient p "
                + "			inner join encounter e on e.patient_id=p.patient_id "
                + "	where 	p.voided=0 and e.voided=0 and e.encounter_type=18 and "
                + "			e.location_id= :location and e.encounter_datetime<= :endDate group by p.patient_id",
            context.getParameterValues());

    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }

  public Map<Integer, Date> getLastTipoDeLevantamentoOnFichaClinicaMasterCard(
      EvaluationContext context, Integer conceptId, Integer valueCodedId) {

    SqlQueryBuilder qb =
        new SqlQueryBuilder(
            String.format(
                "Select p.patient_id,max(e.encounter_datetime) data_levantamento "
                    + "				from 	patient p inner join encounter e on p.patient_id=e.patient_id "
                    + "						inner join obs o on o.encounter_id=e.encounter_id "
                    + "				where 	e.voided=0 and o.voided=0 and p.voided=0 and "
                    + "						e.encounter_type =6 and o.concept_id=%s and o.value_coded=%s and "
                    + "						e.encounter_datetime<=:endDate and e.location_id=:location "
                    + "				group by p.patient_id ",
                conceptId, valueCodedId),
            context.getParameterValues());

    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }

  public Map<Integer, Date> getLastMarkedInModelosDiferenciadosDeCuidadosOnFichaClinicaMasterCard(
      EvaluationContext context, Integer conceptId) {

    SqlQueryBuilder qb =
        new SqlQueryBuilder(
            String.format(
                "Select p.patient_id,max(e.encounter_datetime) data_marcado "
                    + "				from 	patient p inner join encounter e on p.patient_id=e.patient_id "
                    + "						inner join obs o on o.encounter_id=e.encounter_id "
                    + "				where 	e.voided=0 and o.voided=0 and p.voided=0 and "
                    + "						e.encounter_type =6 and o.concept_id=%s and o.value_coded in(1256,1257) and "
                    + "						e.encounter_datetime<=:endDate and e.location_id=:location "
                    + "				group by p.patient_id ",
                conceptId),
            context.getParameterValues());

    return Context.getRegisteredComponents(EvaluationService.class)
        .get(0)
        .evaluateToMap(qb, Integer.class, Date.class, context);
  }
}
