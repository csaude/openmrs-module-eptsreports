package org.openmrs.module.eptsreports.reporting.calculation.generic;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Component;

@Component
public class LastFilaProcessor {

  private Map<Integer, Object> resutls = new HashMap<Integer, Object>();

  public Map<Integer, Object> getResutls() {
    this.process();
    return this.resutls;
  }

  private void process() {
    // -- Aqui encontramos a data do ultimo levantamento de ARV do paciente
    List<List<Object>> lastDrugPickUpFilaQuery =
        Context.getAdministrationService()
            .executeSQL(
                "Select p.patient_id,max(encounter_datetime) data_fila from patient p "
                    + "			inner join encounter e on e.patient_id=p.patient_id "
                    + "	where 	p.voided=0 and e.voided=0 and e.encounter_type=18 and "
                    + "			e.location_id=221 and e.encounter_datetime<='2019-10-20' "
                    + "	group by p.patient_id",
                true);

    this.resutls = new HashMap<>();
    for (List<Object> row : lastDrugPickUpFilaQuery) {
      resutls.put((Integer) row.get(0), (Date) row.get(1));
    }
  }
}
