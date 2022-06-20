package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.List;
import org.openmrs.Location;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

@Component
public class SismaCodeDataSet extends BaseDataSet {

  public DataSetDefinition constructDataset(List<Parameter> list) {

    SqlDataSetDefinition dsd = new SqlDataSetDefinition();
    dsd.setName("SismaCode");
    dsd.addParameters(list);
    dsd.addParameter(new Parameter("location", "location", Location.class));

    dsd.setSqlQuery(
        "select p.patient_id,la.value_reference SismaCode from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join location l on l.location_id=e.location_id "
            + "inner join location_attribute la on la.location_id = l.location_id "
            + "where la.attribute_type_id = 4 and la.location_id= :location and e.location_id = :location and l.location_id = :location and la.voided=0 "
            + "limit 1 ");

    return dsd;
  }
}
