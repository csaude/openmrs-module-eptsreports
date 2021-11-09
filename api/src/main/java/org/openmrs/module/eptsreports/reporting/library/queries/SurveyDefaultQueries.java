package org.openmrs.module.eptsreports.reporting.library.queries;

public class SurveyDefaultQueries {

  public static String findPatientswhoHaveScheduledAppointmentsDuringReportingPeriod() {
    String query =
        "select defaulters.patient_id from ( "
            + "select fila.patient_id, fila.data_levantamento,obs_fila.value_datetime data_proximo_levantamento from (   "
            + "select p.patient_id,max(e.encounter_datetime) as data_levantamento from patient p  "
            + "inner join encounter e on p.patient_id=e.patient_id  "
            + "where encounter_type=18 and e.encounter_datetime <=:startDate and e.location_id=:location and e.voided=0 and p.voided=0  "
            + "group by p.patient_id  "
            + ")fila  "
            + "inner join obs obs_fila on obs_fila.person_id=fila.patient_id  "
            + "where obs_fila.voided=0 and obs_fila.concept_id=5096 and fila.data_levantamento=obs_fila.obs_datetime "
            + "union "
            + "select p.patient_id,max(o.value_datetime) data_levantamento, date_add(max(o.value_datetime), INTERVAL 30 day)  data_proximo_levantamento  from patient p "
            + "inner join encounter e on p.patient_id = e.patient_id  "
            + "inner join obs o on o.encounter_id = e.encounter_id  "
            + "where  e.voided = 0 and p.voided = 0 and o.value_datetime <= :startDate and o.voided = 0 and o.concept_id = 23866 and e.encounter_type=52 and e.location_id=:location  "
            + "group by p.patient_id "
            + ") defaulters "
            + "where defaulters.data_proximo_levantamento between :startDate  AND :endDate "
            + "group by defaulters.patient_id ";

    return query;
  }

  public static String findPatientswhoHaveScheduledAppointmentsDuringReportingPeriodNumerator() {
    String query =
        "select defaulters.patient_id from ( "
            + "select fila.patient_id, fila.data_levantamento,obs_fila.value_datetime data_proximo_levantamento from (   "
            + "select p.patient_id,max(e.encounter_datetime) as data_levantamento from patient p  "
            + "inner join encounter e on p.patient_id=e.patient_id  "
            + "where encounter_type=18 and e.location_id=:location  and e.voided=0 and p.voided=0  "
            + "group by p.patient_id  "
            + ")fila  "
            + "inner join obs obs_fila on obs_fila.person_id=fila.patient_id  "
            + "where obs_fila.voided=0 and obs_fila.concept_id=5096 and fila.data_levantamento=obs_fila.obs_datetime "
            + "union "
            + "select p.patient_id,max(o.value_datetime) data_levantamento, date_add(max(o.value_datetime), INTERVAL 30 day)  data_proximo_levantamento  "
            + "from patient p inner join encounter e on p.patient_id = e.patient_id  "
            + "inner join obs o on o.encounter_id = e.encounter_id 	 "
            + "where  e.voided = 0 and p.voided = 0 and o.voided = 0 and o.concept_id = 23866 and e.encounter_type=52 and e.location_id=:location   "
            + "group by p.patient_id "
            + ") defaulters "
            + "where DATEDIFF(DATE_ADD(:endDate, INTERVAL 7 DAY),  DATE_ADD(defaulters.data_proximo_levantamento, INTERVAL 30 DAY)) > 7  "
            + "OR  "
            + "DATEDIFF(:startDate,DATE_ADD(:endDate, INTERVAL 7 DAY)) = 0 "
            + "group by defaulters.patient_id ";
    return query;
  }

  public static String getPatientsTransferredFromAnotherHealthFacilityUntilEndDate() {
    String query =
        "select transferidopara.patient_id from ( "
            + "select patient_id,max(data_transferidopara) data_transferidopara from ( "
            + "select maxEstado.patient_id,maxEstado.data_transferidopara from ( "
            + "select pg.patient_id,max(ps.start_date) data_transferidopara "
            + "from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "where pg.voided=0 and ps.voided=0 and p.voided=0 and "
            + "pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location group by p.patient_id "
            + ") maxEstado "
            + "inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id "
            + "inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id "
            + "where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and "
            + "ps2.start_date=maxEstado.data_transferidopara and pg2.location_id=:location and ps2.state=7 "
            + "union "
            + "select p.patient_id,max(o.obs_datetime) data_transferidopara from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53 and  e.location_id=:location group by p.patient_id "
            + "union "
            + "select p.patient_id,max(e.encounter_datetime) data_transferidopara from  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id where  e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate and "
            + "o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 and  e.location_id=:location group by p.patient_id) transferido group by patient_id ) transferidopara "
            + "inner join( "
            + "select patient_id,max(encounter_datetime) encounter_datetime from( "
            + "select p.patient_id,max(e.encounter_datetime) encounter_datetime from  patient p "
            + "inner join encounter e on e.patient_id=p.patient_id where  p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_type in (18,6,9) "
            + "group by p.patient_id "
            + "union "
            + "Select p.patient_id,max(value_datetime) encounter_datetime from  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location group by p.patient_id) consultaLev group by patient_id) consultaOuARV on transferidopara.patient_id=consultaOuARV.patient_id "
            + "where consultaOuARV.encounter_datetime<=transferidopara.data_transferidopara and transferidopara.data_transferidopara <= :endDate ";
    return query;
  }

  public static String getPatientsWhoDied() {

    String query =
        "select obito.patient_id from ( "
            + "select patient_id,max(data_obito) data_obito from ( "
            + "select maxEstado.patient_id,maxEstado.data_obito from ( "
            + "select pg.patient_id,max(ps.start_date) data_obito from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "where pg.voided=0 and ps.voided=0 and p.voided=0 and "
            + "pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location "
            + "group by p.patient_id ) maxEstado "
            + "inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id "
            + "inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id "
            + "where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and "
            + "ps2.start_date=maxEstado.data_obito and pg2.location_id=:location and ps2.state=10 "
            + "union "
            + "select p.patient_id,max(o.obs_datetime) data_obito from	patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and "
            + "o.voided=0 and o.concept_id=6272 and o.value_coded=1366 and e.encounter_type=53 and  e.location_id=:location group by p.patient_id "
            + "union  "
            + "select p.patient_id,max(e.encounter_datetime) data_obito from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate "
            + "and o.voided=0 and o.concept_id=6273 and o.value_coded=1366 and e.encounter_type=6 and  e.location_id=:location "
            + "group by p.patient_id "
            + "union  "
            + "Select person_id,death_date from person p where p.dead=1 and p.death_date<=:endDate )transferido "
            + "group by patient_id) obito "
            + "inner join ( "
            + "select patient_id,max(encounter_datetime) encounter_datetime from ( "
            + "select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_type in (18,6,9) "
            + "group by p.patient_id "
            + "union "
            + "select p.patient_id,max(value_datetime) encounter_datetime from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and "
            + "o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location "
            + "group by p.patient_id ) consultaLev "
            + "group by patient_id ) "
            + "consultaOuARV on obito.patient_id=consultaOuARV.patient_id "
            + "where consultaOuARV.encounter_datetime<=obito.data_obito and obito.data_obito <= :endDate ";

    return query;
  }

  public static String getPatientsWhoSuspendTratment() {

    String query =
        "select suspenso1.patient_id from ( "
            + "select patient_id,max(data_suspencao) data_suspencao from ( "
            + "select maxEstado.patient_id,maxEstado.data_suspencao from( "
            + "select pg.patient_id,max(ps.start_date) data_suspencao from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "where pg.voided=0 and ps.voided=0 and p.voided=0 and "
            + "pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location "
            + "group by p.patient_id )maxEstado "
            + "inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id "
            + "inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and "
            + "ps2.start_date=maxEstado.data_suspencao and pg2.location_id=:location and ps2.state=8 "
            + "union "
            + " select p.patient_id,max(o.obs_datetime) data_suspencao from  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 "
            + "and o.value_coded=1709 and e.encounter_type=53 and  e.location_id=:location group by p.patient_id "
            + "union "
            + "select  p.patient_id,max(e.encounter_datetime) data_suspencao from  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where  e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate and o.voided=0 and o.concept_id=6273 "
            + "and o.value_coded=1709 and e.encounter_type=6 and  e.location_id=:location group by p.patient_id ) suspenso group by patient_id) suspenso1 "
            + "inner join ( "
            + "select patient_id,max(encounter_datetime) encounter_datetime from ( "
            + "select p.patient_id,max(e.encounter_datetime) encounter_datetime from  patient p "
            + "inner join encounter e on e.patient_id=p.patient_id where p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate and  "
            + "e.location_id=:location and e.encounter_type=18 group by p.patient_id "
            + "union "
            + "Select  p.patient_id,max(value_datetime) encounter_datetime from  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where  p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and o.concept_id=23866 "
            + "and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location group by p.patient_id"
            + ") consultaLev group by patient_id) consultaOuARV on suspenso1.patient_id=consultaOuARV.patient_id "
            + "where consultaOuARV.encounter_datetime<=suspenso1.data_suspencao and suspenso1.data_suspencao <= :endDate ";

    return query;
  }

  public static String getPatientsWhoAreBreastfeeding() {

    String query =
        "select p.patient_id from patient p "
            + "inner join encounter e on p.patient_id = e.patient_id  "
            + "inner join obs o on o.encounter_id = e.encounter_id  "
            + "where  e.voided = 0 and p.voided = 0  "
            + "and e.encounter_datetime between DATE_SUB(NOW(), INTERVAL 18 MONTH) and NOW() "
            + "and o.voided = 0  and o.concept_id = 6332 and o.value_coded = 1065 and e.encounter_type in(6,9)  and e.location_id=:location "
            + "group by p.patient_id ";

    return query;
  }

  public static String getPatientsWhoArePregnant() {

    String query =
        "select p.patient_id from patient p "
            + "inner join encounter e on p.patient_id = e.patient_id  "
            + "inner join obs o on o.encounter_id = e.encounter_id  "
            + "where  e.voided = 0 and p.voided = 0  "
            + "and e.encounter_datetime between DATE_SUB(NOW(), INTERVAL 9 MONTH) and NOW() "
            + "and o.voided = 0  and o.concept_id = 1982 and o.value_coded = 1065 and e.encounter_type in(6,9)  and e.location_id=:location "
            + "group by p.patient_id ";

    return query;
  }

  public static String getPatientsWhoHaveViralLoadNotSupresed() {

    String query =
        "SELECT cv.patient_id FROM ( "
            + "SELECT pat.patient_id, max(enc.encounter_datetime) FROM patient pat  "
            + "JOIN encounter enc ON pat.patient_id=enc.patient_id  "
            + "JOIN obs ob ON enc.encounter_id=ob.encounter_id  "
            + "WHERE pat.voided=0  "
            + "AND enc.voided=0  "
            + "AND ob.voided=0  "
            + "AND enc.location_id=:location   "
            + "and enc.encounter_datetime between DATE_SUB(NOW(), INTERVAL 18 MONTH) and NOW() "
            + "AND ob.value_numeric IS NOT NULL  "
            + "AND ob.concept_id=856  "
            + "AND enc.encounter_type in(6,53,51)  "
            + "AND ob.value_numeric >= 1000  "
            + ") cv  "
            + "group by cv.patient_id ";

    return query;
  }

  public static String getPatientsWhoHaveAPSSConsultation() {

    String query =
        "select p.patient_id from patient p "
            + "inner join encounter e on p.patient_id = e.patient_id  "
            + "inner join obs o on o.encounter_id = e.encounter_id  "
            + "where  e.voided = 0 and p.voided = 0  "
            + "and e.encounter_datetime between DATE_SUB(NOW(), INTERVAL 3 MONTH) and NOW() "
            + "and o.voided = 0  and e.encounter_type in(34)  and e.location_id=:location "
            + "group by p.patient_id ";

    return query;
  }
}