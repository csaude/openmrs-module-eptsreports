package org.openmrs.module.eptsreports.reporting.library.queries.mq;

import org.openmrs.module.eptsreports.reporting.utils.TypePTV;

public interface MICategory9QueriesInterface {

  class QUERY {
    public static final String findPatientsFirstConsultationOnInclusionDate =
        "select firstConsultation.patient_id from (  "
            + "Select p.patient_id, min(e.encounter_datetime) encounter_datetime from patient p  "
            + "inner join encounter e on p.patient_id=e.patient_id  "
            + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and e.location_id=:location  "
            + "group by p.patient_id "
            + ")firstConsultation  "
            + "Where firstConsultation.encounter_datetime between :startInclusionDate AND :endInclusionDate ";

    public static String getPatientsWhoArePregnantOrBreastfeeding(TypePTV typePTV) {
      String query =
          "select f.patient_id from (    "
              + "select  "
              + "f.patient_id, "
              + "f.encounter_datetime, "
              + "f.data_gravida, "
              + "f.data_lactante,   "
              + "if(f.data_lactante is null,1, if(f.data_gravida is null,2, if(f.data_gravida>=f.data_lactante,1,2))) decisao  from  (    "
              + "Select p.patient_id,p.encounter_datetime,gravida.data_gravida,lactante.data_lactante from  "
              + "(  select * from  (    "
              + "Select p.patient_id, min(e.encounter_datetime) encounter_datetime from patient p    "
              + "inner join encounter e on p.patient_id=e.patient_id    "
              + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and e.location_id=:location    "
              + "group by p.patient_id   "
              + ")firstConsultation    "
              + "Where firstConsultation.encounter_datetime between :startInclusionDate and :endInclusionDate   "
              + ")p    "
              + "inner join   (    "
              + "Select p.patient_id,o.obs_datetime as data_gravida  from patient p    "
              + "inner join person pe on pe.person_id = p.patient_id    "
              + "inner join encounter e on p.patient_id=e.patient_id    "
              + "inner join obs o on e.encounter_id=o.encounter_id    "
              + "where p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=6 and e.location_id=:location and  o.concept_id=1982    "
              + "and e.encounter_datetime between :startInclusionDate and :endInclusionDate    "
              + "and pe.voided = 0 and pe.gender = 'F'   "
              + "group by p.patient_id    "
              + ")gravida on gravida.patient_id=p.patient_id and p.encounter_datetime=gravida.data_gravida     "
              + "left join  (    "
              + "Select p.patient_id,o.obs_datetime as data_lactante from patient p    "
              + "inner join person pe on pe.person_id = p.patient_id    "
              + "inner join encounter e on p.patient_id=e.patient_id    "
              + "inner join obs o on e.encounter_id=o.encounter_id    "
              + "where p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=6 and e.location_id=:location and  o.concept_id=6332    "
              + "and e.encounter_datetime between :startInclusionDate and :endInclusionDate   "
              + "and pe.gender = 'F' and pe.voided = 0    "
              + "group by p.patient_id    "
              + ") lactante on lactante.patient_id=gravida.patient_id and lactante.data_lactante=p.encounter_datetime  "
              + ")f   "
              + ")f ";

      switch (typePTV) {
        case PREGNANT:
          query = query + "where f.decisao = 1 ";
          break;

        case BREASTFEEDING:
          query = query + "where f.decisao = 2 ";
          break;
      }

      return query;
    }

    public static final String findPatientsWhoArePregnantDuringPreviousPeriod =
        "select pregnant.patient_id from "
            + "( "
            + "Select p.patient_id, min(e.encounter_datetime) encounter_datetime from person pe "
            + "inner join patient p on pe.person_id=p.patient_id "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=6 and e.location_id=:location and pe.gender='F' and "
            + "o.concept_id=1982 and o.value_coded=1065 and e.encounter_datetime  between :startInclusionDate AND :endInclusionDate "
            + "group by p.patient_id "
            + ")pregnant "
            + "where pregnant.patient_id not in "
            + "( "
            + "Select p.patient_id from person pe "
            + "inner join patient p on pe.person_id=p.patient_id "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=6 and e.location_id=:location and pe.gender='F' and "
            + "o.concept_id=1982 and o.value_coded=1065 "
            + "and e.encounter_datetime >= DATE_SUB(pregnant.encounter_datetime, INTERVAL 3 Month) and e.encounter_datetime < pregnant.encounter_datetime "
            + "group by p.patient_id "
            + ") ";

    public static final String
        findPatientsWhithCD4OnFirstClinicalConsultationDuringInclusionDateNumeratorCategory9 =
            "select firstClinica.patient_id  from  (  "
                + "Select p.patient_id, min(e.encounter_datetime) encounter_datetime from patient p  "
                + "inner join encounter e on p.patient_id=e.patient_id  "
                + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and e.location_id=:location  "
                + "group by p.patient_id "
                + ") firstClinica  "
                + "inner join obs obsCD4 on obsCD4.person_id=firstClinica.patient_id  "
                + "where obsCD4.obs_datetime=firstClinica.encounter_datetime  "
                + "and obsCD4.concept_id=23722 and obsCD4.value_coded=1695  and obsCD4.voided=0  "
                + "and firstClinica.encounter_datetime between  :startInclusionDate AND :endInclusionDate  "
                + "and obsCD4.location_id=:location  "
                + "group by obsCD4.person_id  ";

    public static final String
        findPregnantWomanWhithCD4OnFirstClinicalConsultationDuringInclusionDateNumeratorCategory9 =
            "select firstClinica.patient_id from  ( "
                + "select * from ( "
                + "Select p.patient_id, min(e.encounter_datetime) encounter_datetime from person pe "
                + "inner join patient p on pe.person_id=p.patient_id "
                + "inner join encounter e on p.patient_id=e.patient_id "
                + "inner join obs o on e.encounter_id=o.encounter_id "
                + "where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=6 and e.location_id=:location and pe.gender='F' "
                + "and e.encounter_datetime between :startInclusionDate AND :endInclusionDate and "
                + "o.concept_id=1982 and o.value_coded=1065 "
                + "group by p.patient_id "
                + ")pregnat "
                + ") firstClinica "
                + "inner join obs obsCD4 on obsCD4.person_id=firstClinica.patient_id "
                + "where obsCD4.concept_id = 23722 and obsCD4.value_coded=1695 and obsCD4.voided=0 and firstClinica.encounter_datetime=obsCD4.obs_datetime "
                + "and obsCD4.location_id=:location ";

    public static final String
        findPatientsWhithCD4On33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9 =
            "select firstClinica.patient_id  from  ( "
                + "Select p.patient_id, min(e.encounter_datetime) encounter_datetime from patient p  "
                + "inner join encounter e on p.patient_id=e.patient_id  "
                + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and e.location_id=:location  "
                + "group by p.patient_id "
                + ") firstClinica "
                + "inner join obs obsCD4 on obsCD4.person_id=firstClinica.patient_id "
                + "where firstClinica.encounter_datetime between :startInclusionDate AND :endInclusionDate and "
                + "obsCD4.obs_datetime > firstClinica.encounter_datetime and obsCD4.obs_datetime <=  DATE_ADD(firstClinica.encounter_datetime, INTERVAL 33 DAY) "
                + "and obsCD4.concept_id=23722 and obsCD4.value_coded=1695  and obsCD4.voided=0 "
                + "and obsCD4.location_id = :location ";

    public static final String
        findPatientsWhithCD4ResultOn33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9 =
            "select firstClinica.patient_id  from  (  "
                + "Select p.patient_id, min(e.encounter_datetime) encounter_datetime from patient p   "
                + "inner join encounter e on p.patient_id=e.patient_id   "
                + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and e.location_id=:location   "
                + "group by p.patient_id  "
                + ") firstClinica  "
                + "inner join encounter e on firstClinica.patient_id=e.patient_id  "
                + "inner join obs obsCD4 on obsCD4.encounter_id=e.encounter_id  "
                + "where firstClinica.encounter_datetime between  :startInclusionDate AND :endInclusionDate  "
                + "and obsCD4.obs_datetime >= firstClinica.encounter_datetime and obsCD4.obs_datetime <=  DATE_ADD(firstClinica.encounter_datetime, INTERVAL 33 DAY)  "
                + "and ((obsCD4.concept_id in(1695,730) and obsCD4.value_numeric is not null) or (obsCD4.concept_id=165515 and obsCD4.value_coded is not null )) and obsCD4.voided=0  "
                + "and obsCD4.location_id=:location and e.encounter_type=6 ";

    public static final String
        findPregnantWomanWhithCD4ResultOn33DaysAfterFirstClinicalConsultationDuringInclusionDateNumeratorCategory9 =
            "select  firstClinica.patient_id from  ( "
                + "select * from (     "
                + "Select p.patient_id, min(e.encounter_datetime) encounter_datetime from person pe     "
                + "inner join patient p on pe.person_id=p.patient_id     "
                + "inner join encounter e on p.patient_id=e.patient_id     "
                + "inner join obs o on e.encounter_id=o.encounter_id     "
                + "where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=6 and e.location_id=:location and pe.gender='F'  and "
                + "e.encounter_datetime between  :startInclusionDate AND :endInclusionDate and o.concept_id=1982 and o.value_coded=1065 "
                + "group by p.patient_id    "
                + ")pregnat  "
                + ") firstClinica     "
                + "inner join encounter e on firstClinica.patient_id=e.patient_id   "
                + "inner join obs obsCD4 on obsCD4.encounter_id=e.encounter_id   "
                + "where ((obsCD4.concept_id in(1695,730) and obsCD4.value_numeric is not null) or (obsCD4.concept_id=165515 and obsCD4.value_coded is not null ))  "
                + "and obsCD4.voided=0   "
                + "and obsCD4.obs_datetime >=firstClinica.encounter_datetime  "
                + "and obsCD4.obs_datetime<=date_add(firstClinica.encounter_datetime, interval 33 day)   "
                + "and obsCD4.location_id=:location "
                + "and e.encounter_type=6 ";

    public static final String calculateAgeOnTheFirstConsultationDateBiggerThanParamOrBreastfeeding(
        int startAge) {

      String sql =
          "select patient_id from ( "
              + "select age.patient_id from   (   "
              + "SELECT * FROM (     "
              + "SELECT patient_id, MIN(art_start_date) art_start_date FROM  (   "
              + "SELECT p.patient_id, e.encounter_datetime art_start_date FROM patient p     "
              + "INNER JOIN encounter e ON p.patient_id = e.patient_id     "
              + "WHERE p.voided = 0     "
              + "AND e.voided = 0     "
              + "AND e.encounter_type = 6     "
              + "AND e.location_id=:location   "
              + "AND date(e.encounter_datetime) <= :endRevisionDate     "
              + ") art_start     "
              + "GROUP BY patient_id     "
              + ") tx_new WHERE art_start_date between :startInclusionDate AND :endInclusionDate   "
              + ")age   "
              + "inner join person pe on pe.person_id=age.patient_id   "
              + "where  TIMESTAMPDIFF(year,pe.birthdate,age.art_start_date) >= %s and  birthdate IS NOT NULL  "
              + ")f  "
              + "union "
              + "select f.patient_id from  "
              + "( "
              + "select f.patient_id,f.data_lactante,f.data_gravida, if(f.data_lactante is null,1, if(f.data_gravida is null,2, if(f.data_gravida>=f.data_lactante,1,2))) decisao  "
              + "from  (  "
              + "select p.person_id as patient_id ,gravida.data_gravida,lactante.data_lactante from person  p  "
              + "left join (  "
              + "Select p.patient_id,obsGravida.obs_datetime data_gravida  from person pe   "
              + "inner join patient p on pe.person_id=p.patient_id   "
              + "inner join encounter e on p.patient_id=e.patient_id   "
              + "inner join obs o on e.encounter_id=o.encounter_id   "
              + "inner join obs obsGravida on e.encounter_id=obsGravida.encounter_id   "
              + "where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0 and obsGravida.voided=0 and e.encounter_type=53 and e.location_id=:location and   "
              + "o.concept_id=1190 and o.value_datetime is not null and   "
              + "obsGravida.concept_id=1982 and obsGravida.value_coded=1065 and pe.gender='F'   "
              + ")gravida on gravida.patient_id=p.person_id  "
              + "left join  "
              + "( Select p.patient_id,obsLactante.obs_datetime data_lactante from person pe  "
              + "inner join patient p on pe.person_id=p.patient_id   "
              + "inner join encounter e on p.patient_id=e.patient_id   "
              + "inner join obs o on e.encounter_id=o.encounter_id   "
              + "inner join obs obsLactante on e.encounter_id=obsLactante.encounter_id   "
              + "where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0 and obsLactante.voided=0 and e.encounter_type=53 and e.location_id=:location and   "
              + "o.concept_id=1190 and o.value_datetime is not null and   "
              + "obsLactante.concept_id=6332 and obsLactante.value_coded=1065 and pe.gender='F'   "
              + ")lactante  on lactante.patient_id=p.person_id  "
              + ")f  "
              + "GROUP by f.patient_id   "
              + ")f WHERE decisao=2 "
              + "union "
              + "select f.patient_id from (    "
              + "select  "
              + "f.patient_id, "
              + "f.encounter_datetime, "
              + "f.data_gravida, "
              + "f.data_lactante,   "
              + "if(f.data_lactante is null,1, if(f.data_gravida is null,2, if(f.data_gravida>=f.data_lactante,1,2))) decisao  from  (    "
              + "Select p.patient_id,p.encounter_datetime,gravida.data_gravida,lactante.data_lactante from  "
              + "(  select * from  (    "
              + "Select p.patient_id, min(e.encounter_datetime) encounter_datetime from patient p    "
              + "inner join encounter e on p.patient_id=e.patient_id    "
              + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and e.location_id=:location    "
              + "group by p.patient_id   "
              + ")firstConsultation    "
              + "Where firstConsultation.encounter_datetime between :startInclusionDate AND :endInclusionDate   "
              + ")p    "
              + "inner join   (    "
              + "Select p.patient_id,o.obs_datetime as data_gravida  from patient p    "
              + "inner join person pe on pe.person_id = p.patient_id    "
              + "inner join encounter e on p.patient_id=e.patient_id    "
              + "inner join obs o on e.encounter_id=o.encounter_id    "
              + "where p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=6 and e.location_id=:location and  o.concept_id=1982    "
              + "and e.encounter_datetime BETWEEN :startInclusionDate AND :endInclusionDate    "
              + "and pe.voided = 0 and pe.gender = 'F'   "
              + "group by p.patient_id    "
              + ")gravida on gravida.patient_id=p.patient_id and p.encounter_datetime=gravida.data_gravida     "
              + "left join  (    "
              + "Select p.patient_id,o.obs_datetime as data_lactante from patient p    "
              + "inner join person pe on pe.person_id = p.patient_id    "
              + "inner join encounter e on p.patient_id=e.patient_id    "
              + "inner join obs o on e.encounter_id=o.encounter_id    "
              + "where p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=6 and e.location_id=:location and  o.concept_id=6332    "
              + "and e.encounter_datetime BETWEEN :startInclusionDate AND :endInclusionDate   "
              + "and pe.gender = 'F' and pe.voided = 0    "
              + "group by p.patient_id    "
              + ") lactante on lactante.patient_id=gravida.patient_id and lactante.data_lactante=p.encounter_datetime  "
              + ")f   "
              + ")f WHERE f.decisao=2 ";

      return String.format(sql, startAge);
    }

    public static final String
        findPatientsWhoAreNewlyEnrolledOnARTTUntilRevisionDateLessThanParamFC(int startAge) {

      final String sql =
          "select age.patient_id from   (  "
              + "SELECT * FROM (    "
              + "SELECT patient_id, MIN(art_start_date) art_start_date FROM  (  "
              + "SELECT p.patient_id, e.encounter_datetime art_start_date FROM patient p    "
              + "INNER JOIN encounter e ON p.patient_id = e.patient_id    "
              + "WHERE p.voided = 0    "
              + "AND e.voided = 0    "
              + "AND e.encounter_type = 6    "
              + "AND e.location_id=:location  "
              + "AND date(e.encounter_datetime) <= :endRevisionDate    "
              + ") art_start    "
              + "GROUP BY patient_id    "
              + ") tx_new WHERE art_start_date between :startInclusionDate AND :endInclusionDate  "
              + ")age  "
              + "inner join person pe on pe.person_id=age.patient_id  "
              + "where  TIMESTAMPDIFF(year,pe.birthdate,age.art_start_date) < %s and  birthdate IS NOT NULL ";

      return String.format(sql, startAge);
    }

    public static final String
        calculatePatientAgeOnTheFirstClinicalConsultationDuringAvaliationPeriod(int startAge) {

      final String sql =
          "select patient_id from ( "
              + "Select p.patient_id, min(e.encounter_datetime) encounter_datetime from patient p "
              + "inner join encounter e on p.patient_id=e.patient_id "
              + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and e.location_id=:location "
              + "group by p.patient_id "
              + ")firstConsultation "
              + "inner join person pe on pe.person_id=firstConsultation.patient_id "
              + "where  TIMESTAMPDIFF(year,pe.birthdate,firstConsultation.encounter_datetime) < %s and  birthdate IS NOT NULL "
              + "and firstConsultation.encounter_datetime between :startInclusionDate and :endInclusionDate ";

      return String.format(sql, startAge);
    }
  }
}
