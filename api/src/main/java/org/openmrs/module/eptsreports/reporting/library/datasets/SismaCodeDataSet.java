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
        "select p.patient_id, la.value_reference SismaCode "
            + "from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	inner join location l on l.location_id=e.location_id "
            + "	inner join location_attribute la on la.location_id = l.location_id "
            + "where la.attribute_type_id =4 and la.location_id= :location "
            + "	and e.location_id = :location and l.location_id = :location and la.voided=0 "
            + "	and p.patient_id = ( "
            + "		select patient_id "
            + "		from ( "
            + "			select p.patient_id "
            + "			from patient p "
            + "				inner join encounter e on e.patient_id=p.patient_id "
            + "			where e.voided=0 and p.voided=0 and e.encounter_type in (5,7) and e.location_id = :location "
            + "			union "
            + "			select pg.patient_id "
            + "			from patient p "
            + "				inner join patient_program pg on p.patient_id=pg.patient_id "
            + "			where pg.voided=0 and p.voided=0 and program_id in (1,2) and location_id=:location "
            + "			union "
            + "			select p.patient_id "
            + "			from patient p "
            + "				inner join encounter e on p.patient_id=e.patient_id "
            + "				inner join obs o on e.encounter_id=o.encounter_id "
            + "			where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53 and o.concept_id=23891 and o.value_datetime is not null and e.location_id=:location "
            + "			union "
            + "			select p.patient_id "
            + "			from patient p "
            + "				inner join patient_program pp on pp.patient_id = p.patient_id "
            + "			where p.voided = 0 and pp.voided = 0 and pp.program_id = 25 "
            + "		) "
            + "	baseCohort   limit 1 "
            + ") limit 1 ");

    return dsd;
  }
}
