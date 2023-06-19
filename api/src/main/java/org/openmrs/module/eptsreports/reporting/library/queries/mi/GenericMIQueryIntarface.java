package org.openmrs.module.eptsreports.reporting.library.queries.mi;

public interface GenericMIQueryIntarface {

  class QUERY {

    public static final String calculateAgeOnTheFirstConsultationDateLessThanParamByAgeRenge(
        int startAge, int endAge) {

      final String sql =
          "select age.patient_id from   (  "
              + "SELECT * FROM (    "
              + "SELECT patient_id, MIN(art_start_date) art_start_date FROM  (  "
              + "SELECT p.patient_id, o.value_datetime art_start_date FROM patient p    "
              + "INNER JOIN encounter e ON p.patient_id = e.patient_id    "
              + "INNER JOIN obs o ON e.encounter_id = o.encounter_id    "
              + "WHERE p.voided = 0    "
              + "AND e.voided = 0    "
              + "AND o.voided = 0    "
              + "AND e.encounter_type = 53    "
              + "AND o.concept_id = 1190    "
              + "AND e.location_id=:location  "
              + "AND date(o.value_datetime) <= :endRevisionDate    "
              + ") art_start    "
              + "GROUP BY patient_id    "
              + ") tx_new WHERE art_start_date between DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 14 MONTH),INTERVAL 1 DAY) and DATE_SUB(:endRevisionDate, INTERVAL 13 MONTH)  "
              + ")age  "
              + "inner join person pe on pe.person_id=age.patient_id  "
              + "where TIMESTAMPDIFF(year,pe.birthdate,age.art_start_date) between %s AND %s ";

      return String.format(sql, startAge, endAge);
    }
  }
}
