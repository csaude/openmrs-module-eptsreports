package org.openmrs.module.eptsreports.reporting.library.queries.mi;

public interface MICategory11QueriesInterface {

  class QUERY {

    public static final String
        findPatientsWhoHaveAtLeast3APSSConsultationIn99DaysAfterInitiatedART =
            "SELECT patient_id from ( "
                + "select tx_new.patient_id, count(apss.patient_id) apss_count "
                + "from ( "
                + "	select patient_id, min(art_start_date) art_start_date "
                + "	from ( "
                + "		select p.patient_id, min(value_datetime) art_start_date "
                + " 		from patient p "
                + " 			join encounter e on p.patient_id=e.patient_id "
                + " 			join obs o on e.encounter_id=o.encounter_id "
                + " 		where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53 "
                + " 			and o.concept_id=1190 and o.value_datetime is not null "
                + "			and o.value_datetime<=:endInclusionDate and e.location_id=:location "
                + " 		group by p.patient_id "
                + " 	) art_start "
                + " 	group by patient_id) tx_new "
                + " 	inner join "
                + " 	(select p.patient_id, e.encounter_datetime "
                + " 	from patient p "
                + "	 	join encounter e on e.patient_id = p.patient_id "
                + "	where p.voided=0 and e.voided=0 and e.encounter_type = 35 "
                + "	 	and e.encounter_datetime and e.location_id=:location "
                + "	) apss on apss.patient_id = tx_new.patient_id "
                + "where tx_new.art_start_date between :startInclusionDate and :endInclusionDate and apss.encounter_datetime between tx_new.art_start_date and date_add(tx_new.art_start_date, INTERVAL 99 DAY) "
                + "group by tx_new.patient_id "
                + "having apss_count >=3 "
                + ") apss ";

    public static final String findPatientWithCVOver1000CopiesRegistredInClinicalConsultation =
        " select patient_id from ( "
            + " select carga_viral.patient_id, min(data_carga) data_carga from ( "
            + " Select p.patient_id, min(e.encounter_datetime) data_carga from patient p "
            + " inner join encounter e on p.patient_id = e.patient_id "
            + " inner join obs o on e.encounter_id=o.encounter_id "
            + " where p.voided = 0 and e.voided = 0 and o.voided = 0 and e.encounter_type = 6 and  o.concept_id = 856 and "
            + " DATE(e.encounter_datetime) between :startInclusionDate and :endInclusionDate and e.location_id = :location and o.value_numeric >= 1000 "
            + " group by p.patient_id "
            + " ) carga_viral "
            + " group by carga_viral.patient_id "
            + " ) final ";

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
                + " group by p.patient_id "
                + " ) primeiraCVAlta "
                + " inner join encounter consultaLactante on consultaLactante.patient_id = primeiraCVAlta.patient_id "
                + " inner join obs obsLactante on consultaLactante.encounter_id = obsLactante.encounter_id "
                + " inner join person on person.person_id = primeiraCVAlta.patient_id "
                + " where 	consultaLactante.voided = 0 and consultaLactante.encounter_type = 6 and consultaLactante.encounter_datetime = primeiraCVAlta.data_carga and consultaLactante.location_id = :location and "
                + " obsLactante.voided = 0 and obsLactante.concept_id = 6332 and obsLactante.value_coded = 1065 and person.gender = 'F' ";
  }
}
