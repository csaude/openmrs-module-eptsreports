package org.openmrs.module.eptsreports.reporting.calculation.generic;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Component;

@Component
public class LastSeguimentoProcessor {

  private Map<Integer, Object> resutls = new HashMap<Integer, Object>();

  public Map<Integer, Object> getResutls() {
    this.process();
    return resutls;
  }

  private void process() {
    // -- Aqui encontramos a data da ultima consulta clinica do paciente
    List<List<Object>> queryResult =
        Context.getAdministrationService()
            .executeSQL(
                "Select p.patient_id,max(encounter_datetime) data_seguimento from patient p "
                    + "			inner join encounter e on e.patient_id=p.patient_id "
                    + "	where 	p.voided=0 and e.voided=0 and e.encounter_type in (6,9) and "
                    + "			e.location_id=221 and e.encounter_datetime<='2019-10-20' "
                    + "	group by p.patient_id",
                true);

    for (List<Object> row : queryResult) {
      resutls.put((Integer) row.get(0), (Date) row.get(1));
    }
  }
}
