package org.openmrs.module.eptsreports.reporting.calculation.generic;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Component;

@Component
public class LastRecepcaoLevantamentoProcessor {

  private Map<Integer, Object> resutls = new HashMap<Integer, Object>();

  public Map<Integer, Object> getResutls() {
    this.process();
    return resutls;
  }

  private void process() {
    // -- Aqui encontramos a data do proximo levantamento marcado no ultimo
    // levantamento de ARV
    List<List<Object>> queryResult =
        Context.getAdministrationService()
            .executeSQL(
                "Select p.patient_id,max(value_datetime) data_recepcao_levantou "
                    + "	from 	patient p inner join encounter e on p.patient_id=e.patient_id "
                    + "			inner join obs o on e.encounter_id=o.encounter_id "
                    + "	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and "
                    + "			o.concept_id=23866 and o.value_datetime is not null and "
                    + "			o.value_datetime<='2019-10-20' and e.location_id=221 group by p.patient_id",
                true);

    this.resutls = new HashMap<>();
    for (List<Object> row : queryResult) {
      resutls.put((Integer) row.get(0), (Date) row.get(1));
    }
  }
}
