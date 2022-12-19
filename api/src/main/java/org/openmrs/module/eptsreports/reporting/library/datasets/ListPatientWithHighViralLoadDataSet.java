package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.List;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

@Component
public class ListPatientWithHighViralLoadDataSet extends BaseDataSet {

  private static final String FIND_PATIENTS_WITH_HIGH_VL_LIST =
      "PATIENTS_WITH_HIGH_VL/FIND_PATIENTS_WITH_HIGH_VL_LIST.sql";

  private static final String FIND_PATIENTS_WITH_HIGH_VL_ALERTS =
      "PATIENTS_WITH_HIGH_VL/FIND_PATIENTS_WITH_HIGH_VL_ALERTS.sql";

  public DataSetDefinition constructDataset(List<Parameter> list) {

    SqlDataSetDefinition dsd = new SqlDataSetDefinition();
    dsd.setName("Lista de Seguimento De Pacientes Com Carga Viral nao Suprimida");
    dsd.addParameters(list);
    dsd.setSqlQuery(EptsQuerysUtils.loadQuery(FIND_PATIENTS_WITH_HIGH_VL_LIST));
    return dsd;
  }

  public DataSetDefinition getAlertMessagesForListsPatientsEligibleViralLoadDataset(
      List<Parameter> list) {

    SqlDataSetDefinition dsd = new SqlDataSetDefinition();
    dsd.setName(
        "Mensagens de alerta - Lista de Seguimento De Pacientes Com Carga Viral nao Suprimida");
    dsd.addParameters(list);
    dsd.setSqlQuery(EptsQuerysUtils.loadQuery(FIND_PATIENTS_WITH_HIGH_VL_ALERTS));
    return dsd;
  }

  public DataSetDefinition getEndDatePlus7Days(List<Parameter> list) {

    SqlDataSetDefinition dsd = new SqlDataSetDefinition();
    dsd.setName("endDatePlus7Days");
    dsd.addParameters(list);
    dsd.addParameter(new Parameter("location", "location", Location.class));

    dsd.setSqlQuery(
        "select p.patient_id, DATE_FORMAT(DATE(date_add(:endDate, interval 7 day)), '%d-%m-%Y') endDatePlus7Days                          "
            + "from patient p                                                                           "
            + "   inner join encounter e on e.patient_id=p.patient_id                                   "
            + "where e.voided=0 and p.voided=0 and e.encounter_type in (5,7)                            "
            + "   and e.encounter_datetime<=:endDate and e.location_id = :location                      "
            + "                                                                                         "
            + "union                                                                                    "
            + "                                                                                         "
            + "select pg.patient_id, DATE_FORMAT(DATE(date_add(:endDate, interval 7 day)), '%d-%m-%Y')   endDatePlus7Days                                              "
            + "from patient p                                                                             "
            + "   inner join patient_program pg on p.patient_id=pg.patient_id                             "
            + "where pg.voided=0 and p.voided=0 and program_id in (1,2)                                   "
            + "   and date_enrolled<=:endDate and location_id=:location                                   "
            + "                                                                                          "
            + "union                                                                                      "
            + "                                                                                          "
            + "select p.patient_id, DATE_FORMAT(DATE(date_add(:endDate, interval 7 day)), '%d-%m-%Y')      endDatePlus7Days                                                        "
            + "from patient p                                                                             "
            + "   inner join encounter e on p.patient_id=e.patient_id                                     "
            + "   inner join obs o on e.encounter_id=o.encounter_id                                       "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53                     "
            + "   and o.concept_id=23891                                                                  "
            + "   and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location  "
            + "                                                                                          "
            + "union                                                                                      "
            + "                                                                                          "
            + "select p.patient_id, DATE_FORMAT(DATE(date_add(:endDate, interval 7 day)), '%d-%m-%Y') endDatePlus7Days from patient p                                               "
            + "   inner join patient_program pp on pp.patient_id = p.patient_id                           "
            + "where p.voided = 0 and pp.voided = 0 and pp.program_id = 25                                "
            + "   and pp.date_enrolled <= :endDate   limit 1                                              ");

    return dsd;
  }
}
