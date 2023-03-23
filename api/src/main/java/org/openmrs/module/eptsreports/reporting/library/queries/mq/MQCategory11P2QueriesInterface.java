package org.openmrs.module.eptsreports.reporting.library.queries.mq;

public interface MQCategory11P2QueriesInterface {
  class QUERY {
    public static final String findPatientsWhoArePregnantDuringInclusionPeriodByB3 =
        " SELECT tx_new.patient_id FROM ( "
            + " SELECT patient_id, MIN(art_start_date) art_start_date FROM ( "
            + " SELECT p.patient_id, MIN(value_datetime) art_start_date FROM patient p "
            + " INNER JOIN encounter e ON p.patient_id = e.patient_id "
            + " INNER JOIN obs o ON e.encounter_id = o.encounter_id "
            + " WHERE p.voided = 0 AND e.voided = 0 AND o.voided = 0 AND e.encounter_type = 53 "
            + " AND o.concept_id = 1190 AND o.value_datetime is NOT NULL AND o.value_datetime <= :endInclusionDate AND e.location_id = :location "
            + " GROUP BY p.patient_id "
            + " ) art_start "
            + " GROUP BY patient_id "
            + " ) tx_new "
            + " INNER JOIN encounter e ON e.patient_id = tx_new.patient_id AND e.voided = 0 AND e.encounter_type = 6  AND e.location_id = :location "
            + " INNER JOIN obs o ON o.encounter_id = e.encounter_id AND o.concept_id = 1982 AND o.value_coded = 1065 AND o.voided = 0 "
            + " INNER JOIN person pe ON tx_new.patient_id = pe.person_id "
            + " WHERE tx_new.art_start_date BETWEEN :startInclusionDate AND :endInclusionDate "
            + " AND tx_new.art_start_date = e.encounter_datetime "
            + " AND pe.voided = 0 and pe.gender = 'F' ";

    public static final String
        findPatientsWhoHasCVBiggerThan1000AndMarkedAsPregnantInTheSameClinicalConsultation =
            " select primeiraCVAlta.patient_id "
                + " from "
                + " ( "
                + " Select p.patient_id, min(e.encounter_datetime) data_carga "
                + " from 	patient p "
                + " inner join encounter e on p.patient_id = e.patient_id "
                + " inner join obs o on e.encounter_id=o.encounter_id "
                + " where 	p.voided = 0 and e.voided = 0 and o.voided = 0 and e.encounter_type = 6 and  o.concept_id = 856 and "
                + " DATE(e.encounter_datetime) between :startInclusionDate and :endInclusionDate and e.location_id = :location and o.value_numeric >= 1000 "
                + " group by p.patient_id "
                + " ) primeiraCVAlta "
                + " inner join encounter consultaGravida on consultaGravida.patient_id = primeiraCVAlta.patient_id "
                + " inner join obs obsGravida on consultaGravida.encounter_id = obsGravida.encounter_id "
                + " inner join person on person.person_id = primeiraCVAlta.patient_id "
                + " where 	consultaGravida.voided = 0 and consultaGravida.encounter_type = 6 and consultaGravida.encounter_datetime = primeiraCVAlta.data_carga and consultaGravida.location_id = :location and "
                + " obsGravida.voided = 0 and obsGravida.concept_id = 1982 and obsGravida.value_coded = 1065 and person.gender = 'F' ";

    public static final String
        findPatientsWhoHasCVBiggerThan1000AndMarkedAsBreastFeedingInTheSameClinicalConsultation =
            " select primeiraCVAlta.patient_id "
                + " from "
                + " ( "
                + " Select p.patient_id, min(e.encounter_datetime) data_carga "
                + " from 	patient p "
                + " inner join encounter e on p.patient_id = e.patient_id "
                + " inner join obs o on e.encounter_id=o.encounter_id "
                + " where 	p.voided = 0 and e.voided = 0 and o.voided = 0 and e.encounter_type = 6 and  o.concept_id = 856 and "
                + " DATE(e.encounter_datetime) between :startInclusionDate and :endInclusionDate  and e.location_id = :location and o.value_numeric >= 1000 "
                + " group by p.patient_id  "
                + " ) primeiraCVAlta "
                + " inner join encounter consultaLactante on consultaLactante.patient_id = primeiraCVAlta.patient_id "
                + " inner join obs obsLactante on consultaLactante.encounter_id = obsLactante.encounter_id "
                + " inner join person on person.person_id = primeiraCVAlta.patient_id "
                + " where 	consultaLactante.voided = 0 and consultaLactante.encounter_type = 6 and consultaLactante.encounter_datetime = primeiraCVAlta.data_carga and consultaLactante.location_id = :location and "
                + " obsLactante.voided = 0 and obsLactante.concept_id = 6332 and obsLactante.value_coded = 1065 and person.gender = 'F' ";

    public static final String
        findPatientsWhoHasCVBiggerThan50AndMarkedAsPregnantInTheSameClinicalConsultation =
            " select primeiraCVAlta.patient_id "
                + " from "
                + " ( "
                + " Select p.patient_id, min(e.encounter_datetime) data_carga "
                + " from 	patient p "
                + " inner join encounter e on p.patient_id = e.patient_id "
                + " inner join obs o on e.encounter_id=o.encounter_id "
                + " where 	p.voided = 0 and e.voided = 0 and o.voided = 0 and e.encounter_type = 6 and  o.concept_id = 856 and "
                + " DATE(e.encounter_datetime) between :startInclusionDate and :endInclusionDate and e.location_id = :location and o.value_numeric > 50 "
                + " group by p.patient_id "
                + " ) primeiraCVAlta "
                + " inner join encounter consultaGravida on consultaGravida.patient_id = primeiraCVAlta.patient_id "
                + " inner join obs obsGravida on consultaGravida.encounter_id = obsGravida.encounter_id "
                + " inner join person on person.person_id = primeiraCVAlta.patient_id "
                + " where 	consultaGravida.voided = 0 and consultaGravida.encounter_type = 6 and consultaGravida.encounter_datetime = primeiraCVAlta.data_carga and consultaGravida.location_id = :location and "
                + " obsGravida.voided = 0 and obsGravida.concept_id = 1982 and obsGravida.value_coded = 1065 and person.gender = 'F' ";

    public static final String
        findPatientsWhoHasCVBiggerThan50AndMarkedAsBreastFeedingInTheSameClinicalConsultation =
            " select primeiraCVAlta.patient_id "
                + " from "
                + " ( "
                + " Select p.patient_id, min(e.encounter_datetime) data_carga "
                + " from 	patient p "
                + " inner join encounter e on p.patient_id = e.patient_id "
                + " inner join obs o on e.encounter_id=o.encounter_id "
                + " where 	p.voided = 0 and e.voided = 0 and o.voided = 0 and e.encounter_type = 6 and  o.concept_id = 856 and "
                + " DATE(e.encounter_datetime) between :startInclusionDate and :endInclusionDate  and e.location_id = :location and o.value_numeric > 50 "
                + " group by p.patient_id  "
                + " ) primeiraCVAlta "
                + " inner join encounter consultaLactante on consultaLactante.patient_id = primeiraCVAlta.patient_id "
                + " inner join obs obsLactante on consultaLactante.encounter_id = obsLactante.encounter_id "
                + " inner join person on person.person_id = primeiraCVAlta.patient_id "
                + " where 	consultaLactante.voided = 0 and consultaLactante.encounter_type = 6 and consultaLactante.encounter_datetime = primeiraCVAlta.data_carga and consultaLactante.location_id = :location and "
                + " obsLactante.voided = 0 and obsLactante.concept_id = 6332 and obsLactante.value_coded = 1065 and person.gender = 'F' ";
  }
}
