package org.openmrs.module.eptsreports.reporting.calculation.generic;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang3.time.DateUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.common.DateUtil;
import org.springframework.stereotype.Component;

@Component
public class LastPatientStateProcessor {

  private Map<Integer, Object> resutls = new HashMap<Integer, Object>();

  private Map<Integer, Object> process() {
    /* Estado no programa */
    Map<Integer, Date> patientProgramState = new HashMap<>();
    List<List<Object>> patientProgramStateQuery =
        Context.getAdministrationService()
            .executeSQL(
                "select pg.patient_id, max(ps.start_date) data_estado from 	patient p "
                    + "					inner join patient_program pg on p.patient_id=pg.patient_id "
                    + "					inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
                    + "			where 	pg.voided=0 and ps.voided=0 and p.voided=0 and "
                    + "					pg.program_id=2 and ps.state in (7,8,10) and ps.end_date is null and "
                    + "					ps.start_date<='2019-10-20' and location_id=221 "
                    + "			group by pg.patient_id",
                true);

    for (List<Object> row : patientProgramStateQuery) {
      patientProgramState.put((Integer) row.get(0), (Date) row.get(1));
    }

    /* Estado no estado de permanencia da ficha resumo */
    Map<Integer, Date> fichaResumoEstadoPermanenciaState = new HashMap<>();
    List<List<Object>> fichaResumoEstadoPermanenciaStateQuery =
        Context.getAdministrationService()
            .executeSQL(
                "select p.patient_id, max(o.obs_datetime) data_estado from 	patient p "
                    + "					inner join encounter e on p.patient_id=e.patient_id "
                    + "					inner join obs  o on e.encounter_id=o.encounter_id "
                    + "			where 	e.voided=0 and o.voided=0 and p.voided=0 and "
                    + "					e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1706,1366,1709) and "
                    + "					o.obs_datetime<='2019-10-20' and e.location_id=221 "
                    + "			group by p.patient_id",
                true);

    for (List<Object> row : fichaResumoEstadoPermanenciaStateQuery) {
      fichaResumoEstadoPermanenciaState.put((Integer) row.get(0), (Date) row.get(1));
    }

    /* Obito demografico */
    Map<Integer, Date> obitoDemoGraficoState = new HashMap<>();
    List<List<Object>> obitoDemoGraficoStateQuery =
        Context.getAdministrationService()
            .executeSQL(
                "select person_id as patient_id,death_date as data_estado from person "
                    + "			where dead=1 and death_date is not null and death_date<='2019-10-20'",
                true);
    for (List<Object> row : obitoDemoGraficoStateQuery) {
      obitoDemoGraficoState.put((Integer) row.get(0), (Date) row.get(1));
    }

    /* Obito na ficha de busca */
    Map<Integer, Date> obitoFichaDeBuscaState = new HashMap<>();
    List<List<Object>> obitoFichaDeBuscaStateQuery =
        Context.getAdministrationService()
            .executeSQL(
                "select p.patient_id, max(obsObito.obs_datetime) data_estado "
                    + "			from 	patient p "
                    + "					inner join encounter e on p.patient_id=e.patient_id "
                    + "					inner join obs obsEncontrado on e.encounter_id=obsEncontrado.encounter_id "
                    + "					inner join obs obsObito on e.encounter_id=obsObito.encounter_id "
                    + "			where 	e.voided=0 and obsEncontrado.voided=0 and p.voided=0 and obsObito.voided=0 and "
                    + "					e.encounter_type in (21,36,37) and  e.encounter_datetime<='2019-10-20' and  e.location_id=221 and "
                    + "					obsEncontrado.concept_id in (2003, 6348) and obsEncontrado.value_coded=1066 and "
                    + "					obsObito.concept_id=2031 and obsObito.value_coded=1383 "
                    + "			group by p.patient_id",
                true);

    for (List<Object> row : obitoFichaDeBuscaStateQuery) {
      obitoFichaDeBuscaState.put((Integer) row.get(0), (Date) row.get(1));
    }

    return getResultMap(
        patientProgramState,
        fichaResumoEstadoPermanenciaState,
        obitoDemoGraficoState,
        obitoFichaDeBuscaState);
  }

  private Map<Integer, Object> getResultMap(
      Map<Integer, Date> map1,
      Map<Integer, Date> map2,
      Map<Integer, Date> map3,
      Map<Integer, Date> map4) {

    Set<Integer> ids = new TreeSet<>();
    this.resutls = new HashMap<>();

    ids.addAll(map1.keySet());
    ids.addAll(map2.keySet());
    ids.addAll(map3.keySet());
    ids.addAll(map4.keySet());

    Date finalComparisonDate = DateUtil.getDateTime(Integer.MAX_VALUE, 1, 1);
    for (Integer id : ids) {

      Date maxDate = DateUtil.getDateTime(Integer.MAX_VALUE, 1, 1);
      Date dateMap1 = map1.get(id);
      Date dateMap2 = map2.get(id);
      Date dateMap3 = map3.get(id);

      if (dateMap1 != null && dateMap1.compareTo(maxDate) > 0) {
        maxDate = dateMap1;
      }

      if (dateMap2 != null && dateMap2.compareTo(maxDate) > 0) {
        maxDate = dateMap2;
      }

      if (dateMap3 != null && dateMap3.compareTo(maxDate) > 0) {
        maxDate = dateMap3;
      }
      if (!DateUtils.isSameDay(maxDate, finalComparisonDate)) {
        this.resutls.put(id, maxDate);
      }
    }
    return this.resutls;
  }

  public Map<Integer, Object> getResutls() {
    this.process();
    return this.resutls;
  }
}
