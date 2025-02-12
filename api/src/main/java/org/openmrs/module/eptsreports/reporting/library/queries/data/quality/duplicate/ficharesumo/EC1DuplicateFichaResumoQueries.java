package org.openmrs.module.eptsreports.reporting.library.queries.data.quality.duplicate.ficharesumo;

public interface EC1DuplicateFichaResumoQueries {
  class QUERY {

    public static String findPatiendsWithDuplicatedFichaResumo =
        "SELECT "
            + "patient.patient_id,location.name locationName, "
            + "patient_identifier.identifier, "
            + "concat(ifnull(person_name.given_name,''),' ',ifnull(person_name.middle_name,''),' ',ifnull(person_name.family_name,'')) as nomeCompleto "
            + ",person.birthdate, if(person.birthdate_estimated = true, 'Sim','Não') birthdate_estimated , person.gender , patient.date_created "
            + ",patient.date_changed, "
            + "patient_program.date_enrolled , "
            + "e.encounter_datetime, "
            + "o23891.value_datetime as opening_date, "
            + "o23808.value_datetime as pre_art_date, "
            + "o1190.value_datetime  as art_start_date "
            + "FROM encounter e "
            + "inner join "
            + "(SELECT "
            + "distinct e.encounter_id, e.patient_id "
            + "FROM encounter e "
            + "inner join "
            + "(select "
            + "   * "
            + "   from encounter e "
            + "   where e.encounter_type = 53 "
            + "   and e.voided = 0 "
            + "   and e.location_id = :location "
            + "   group by e.patient_id having count(e.encounter_type)>=2) "
            + "temp on temp.patient_id = e.patient_id "
            + "where e.encounter_type = 53 "
            + "and e.voided = 0 "
            + ") temp on temp.encounter_id = e.encounter_id "
            + "inner join patient on patient.patient_id =e.patient_id "
            + "inner join patient_identifier on patient_identifier.patient_id = patient.patient_id "
            + "inner join person on person.person_id =patient.patient_id "
            + "inner join person_name on person_name.person_id = patient.patient_id "
            + "inner join location on (location.location_id =e.location_id and location.retired =0 and e.location_id = :location) "
            + "inner join patient_program on (patient_program.patient_id = patient.patient_id and patient_program.program_id=2 and patient_program.voided =0) "
            + "inner JOIN patient_state ps ON (patient_program.patient_program_id = ps.patient_program_id AND ps.start_date IS NOT NULL AND ps.end_date IS NULL ) "
            + "left join obs o23891 on (o23891.encounter_id = e.encounter_id and o23891.concept_id = 23891 and o23891.voided = 0 ) "
            + "left join obs o23808 on (o23808.encounter_id = e.encounter_id and o23808.concept_id = 23808 and o23808.voided = 0) "
            + "left join obs o1190 on  (o1190.encounter_id = e.encounter_id and o1190.concept_id = 1190 and o1190.voided = 0) "
            + "where patient_identifier.voided = 0 "
            + "and patient.voided =0 "
            + " and ps.patient_state_id = (select max(ps1.patient_state_id) from patient_state ps1 inner join patient_program pp on ps1.patient_program_id = pp.patient_program_id inner join patient p "
            + " on p.patient_id = pp.patient_id where pp.patient_id = patient.patient_id and pp.program_id = 2 and pp.voided = 0 "
            + " ) "
            + "and patient_identifier.preferred = 1 "
            + "and person_name.preferred = 1 "
            + "and person.voided = 0 "
            + "and patient_program.voided =0 "
            + "order by patient.patient_id, e.encounter_datetime desc ";

    public static String getEc1Total =
        "SELECT "
            + "patient.patient_id "
            + "FROM encounter e "
            + "inner join "
            + "(SELECT "
            + "distinct e.encounter_id, e.patient_id "
            + "FROM encounter e "
            + "inner join "
            + "(select "
            + "   * "
            + "   from encounter e "
            + "   where e.encounter_type = 53 "
            + "   and e.voided = 0 "
            + "   and e.location_id = :location "
            + "   group by e.patient_id having count(e.encounter_type)>=2) "
            + "temp on temp.patient_id = e.patient_id "
            + "where e.encounter_type = 53 "
            + "and e.voided = 0 "
            + ") temp on temp.encounter_id = e.encounter_id "
            + "inner join patient on patient.patient_id =e.patient_id "
            + "inner join patient_identifier on patient_identifier.patient_id = patient.patient_id "
            + "inner join person on person.person_id =patient.patient_id "
            + "inner join person_name on person_name.person_id = patient.patient_id "
            + "inner join location on (location.location_id =e.location_id and location.retired =0 and e.location_id = :location) "
            + "inner join patient_program on (patient_program.patient_id = patient.patient_id and patient_program.program_id=2 and patient_program.voided =0) "
            + "inner JOIN patient_state ps ON (patient_program.patient_program_id = ps.patient_program_id AND ps.start_date IS NOT NULL AND ps.end_date IS NULL ) "
            + "left join obs o23891 on (o23891.encounter_id = e.encounter_id and o23891.concept_id = 23891 and o23891.voided = 0 ) "
            + "left join obs o23808 on (o23808.encounter_id = e.encounter_id and o23808.concept_id = 23808 and o23808.voided = 0) "
            + "left join obs o1190 on  (o1190.encounter_id = e.encounter_id and o1190.concept_id = 1190 and o1190.voided = 0) "
            + "where patient_identifier.voided = 0 "
            + "and patient.voided =0 "
            + " and ps.patient_state_id = (select max(ps1.patient_state_id) from patient_state ps1 inner join patient_program pp on ps1.patient_program_id = pp.patient_program_id inner join patient p "
            + " on p.patient_id = pp.patient_id where pp.patient_id = patient.patient_id and pp.program_id = 2 and pp.voided = 0 "
            + " ) "
            + "and patient_identifier.preferred = 1 "
            + "and person_name.preferred = 1 "
            + "and person.voided = 0 "
            + "and patient_program.voided =0 "
            + "order by patient.patient_id, e.encounter_datetime desc ";
  }
}
