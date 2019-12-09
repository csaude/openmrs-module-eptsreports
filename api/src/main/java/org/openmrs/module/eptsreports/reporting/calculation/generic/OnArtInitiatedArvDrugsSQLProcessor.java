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
public class OnArtInitiatedArvDrugsSQLProcessor {

  private Map<Integer, Object> resutls = new HashMap<Integer, Object>();

  private Map<Integer, Object> process() {

    /* Patients on ART who initiated the ARV DRUGS: ART Regimen Start Date */
    Map<Integer, Date> patientsInitiatedArvDrugs = new HashMap<>();
    List<List<Object>> initiatedArvQuery =
        Context.getAdministrationService()
            .executeSQL(
                "Select 	p.patient_id,min(e.encounter_datetime) data_inicio "
                    + "				from 	patient p "
                    + "						inner join encounter e on p.patient_id=e.patient_id	"
                    + "						inner join obs o on o.encounter_id=e.encounter_id "
                    + "				where 	e.voided=0 and o.voided=0 and p.voided=0 and "
                    + "						e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and "
                    + "						e.encounter_datetime<='2019-10-20' and e.location_id=221 "
                    + "				group by p.patient_id",
                true);

    for (List<Object> row : initiatedArvQuery) {
      patientsInitiatedArvDrugs.put((Integer) row.get(0), (Date) row.get(1));
    }

    /* Patients on ART who have art start date: ART Start date */
    Map<Integer, Date> patientsWithArtStartDate = new HashMap<>();
    List<List<Object>> patientWithArtStartDateQuery =
        Context.getAdministrationService()
            .executeSQL(
                "Select p.patient_id,min(value_datetime) data_inicio from 	patient p "
                    + "						inner join encounter e on p.patient_id=e.patient_id "
                    + "						inner join obs o on e.encounter_id=o.encounter_id "
                    + "				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) and "
                    + "						o.concept_id=1190 and o.value_datetime is not null and "
                    + "						o.value_datetime<='2019-10-20' and e.location_id=221 "
                    + "				group by p.patient_id",
                true);

    for (List<Object> row : patientWithArtStartDateQuery) {
      patientsWithArtStartDate.put((Integer) row.get(0), (Date) row.get(1));
    }

    /* Patients enrolled in ART Program: OpenMRS Program */
    Map<Integer, Date> patientEnrolledInArtProgram = new HashMap<>();
    List<List<Object>> patientEnrolledInArtProgramQuery =
        Context.getAdministrationService()
            .executeSQL(
                "select 	pg.patient_id,min(date_enrolled) data_inicio "
                    + "				from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id "
                    + "				where 	pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<='2019-10-20' and location_id=221 "
                    + "				group by pg.patient_id",
                true);

    for (List<Object> row : patientEnrolledInArtProgramQuery) {
      patientEnrolledInArtProgram.put((Integer) row.get(0), (Date) row.get(1));
    }

    /*
     * Patients with first drugs pick up date set in Pharmacy: First ART Start Date
     */
    Map<Integer, Date> patientWithFirstDrugPickUpDateInPharmacy = new HashMap<>();
    List<List<Object>> patientWithFirstDrugPickUpDateInPharmacyQuery =
        Context.getAdministrationService()
            .executeSQL(
                "SELECT 	e.patient_id, MIN(e.encounter_datetime) AS data_inicio "
                    + "				  FROM 		patient p "
                    + "							inner join encounter e on p.patient_id=e.patient_id "
                    + "				  WHERE		p.voided=0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime<='2019-10-20' and e.location_id=221 "
                    + "				  GROUP BY 	p.patient_id",
                true);

    for (List<Object> row : patientWithFirstDrugPickUpDateInPharmacyQuery) {
      patientWithFirstDrugPickUpDateInPharmacy.put((Integer) row.get(0), (Date) row.get(1));
    }

    /* Patients with first drugs pick up date set: Recepcao Levantou ARV */
    Map<Integer, Date> patientWithFirstDrugPickUpDateSetInReception = new HashMap<>();
    List<List<Object>> patientWithFirstDrugPickUpDateSetReceptionQuery =
        Context.getAdministrationService()
            .executeSQL(
                "Select 	p.patient_id,min(value_datetime) data_inicio "
                    + "				from 	patient p "
                    + "						inner join encounter e on p.patient_id=e.patient_id "
                    + "						inner join obs o on e.encounter_id=o.encounter_id "
                    + "				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and "
                    + "						o.concept_id=23866 and o.value_datetime is not null and "
                    + "						o.value_datetime<='2019-10-20' and e.location_id=221 "
                    + "				group by p.patient_id ",
                true);

    for (List<Object> row : patientWithFirstDrugPickUpDateSetReceptionQuery) {
      patientWithFirstDrugPickUpDateSetInReception.put((Integer) row.get(0), (Date) row.get(1));
    }

    return getResultMap(
        patientsInitiatedArvDrugs,
        patientsWithArtStartDate,
        patientEnrolledInArtProgram,
        patientWithFirstDrugPickUpDateInPharmacy,
        patientWithFirstDrugPickUpDateSetInReception);
  }

  private Map<Integer, Object> getResultMap(
      Map<Integer, Date> map1,
      Map<Integer, Date> map2,
      Map<Integer, Date> map3,
      Map<Integer, Date> map4,
      Map<Integer, Date> map5) {
    this.resutls = new HashMap<>();
    Set<Integer> ids = new TreeSet<>();

    ids.addAll(map1.keySet());
    ids.addAll(map2.keySet());
    ids.addAll(map3.keySet());
    ids.addAll(map4.keySet());
    ids.addAll(map5.keySet());

    Date finalComparisonDate = DateUtil.getDateTime(Integer.MIN_VALUE, 1, 1);
    for (Integer id : ids) {

      Date minDate = DateUtil.getDateTime(Integer.MIN_VALUE, 1, 1);
      Date dateMap1 = map1.get(id);
      Date dateMap2 = map2.get(id);
      Date dateMap3 = map3.get(id);
      Date dateMap4 = map4.get(id);
      Date dateMap5 = map5.get(id);

      if (dateMap1 != null && dateMap1.compareTo(minDate) < 0) {
        minDate = dateMap1;
      }

      if (dateMap2 != null && dateMap2.compareTo(minDate) < 0) {
        minDate = dateMap2;
      }

      if (dateMap3 != null && dateMap3.compareTo(minDate) < 0) {
        minDate = dateMap3;
      }
      if (dateMap4 != null && dateMap4.compareTo(minDate) < 0) {
        minDate = dateMap4;
      }
      if (dateMap5 != null && dateMap5.compareTo(minDate) < 0) {
        minDate = dateMap5;
      }

      if (!DateUtils.isSameDay(minDate, finalComparisonDate)) {
        this.resutls.put(id, minDate);
      }
    }
    return this.resutls;
  }

  public Map<Integer, Object> getResutls() {
    this.process();
    return this.resutls;
  }
}
