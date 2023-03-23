package org.openmrs.module.eptsreports.reporting.library.queries.mq;

public interface MQCategory18QueriesInterface {

  class QUERY {

    public static final String findChildrenAndAdolescentsWithFullDisclore =
        "SELECT tx_new.patient_id FROM "
            + "( "
            + "SELECT patient_id, MIN(art_start_date) art_start_date FROM "
            + "( "
            + "SELECT p.patient_id, o.value_datetime art_start_date FROM patient p "
            + "INNER JOIN encounter e ON p.patient_id = e.patient_id "
            + "INNER JOIN obs o ON e.encounter_id = o.encounter_id "
            + "WHERE p.voided = 0 "
            + "AND e.voided = 0 "
            + "AND o.voided = 0 "
            + "AND e.encounter_type = 53 "
            + "AND o.concept_id = 1190 "
            + "AND e.location_id=:location "
            + "AND date(o.value_datetime) <= :endRevisionDate "
            + ") art_start "
            + " GROUP BY patient_id "
            + ") tx_new "
            + "inner join "
            + "( "
            + "select p.patient_id,max(e.encounter_datetime) dataRevelacaoTotal from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id = 6340 and o.value_coded=6337 and e.encounter_type=35 and "
            + "e.encounter_datetime <=:endRevisionDate and e.location_id=:location "
            + "group by p.patient_id "
            + ") revelado on revelado.patient_id=tx_new.patient_id "
            + "where  (tx_new.art_start_date BETWEEN DATE_ADD(DATE_SUB(:endRevisionDate, INTERVAL 14 MONTH), INTERVAL 1 DAY) and DATE_SUB(:endRevisionDate, INTERVAL 11 MONTH)) and "
            + "       (revelado.dataRevelacaoTotal BETWEEN tx_new.art_start_date and DATE_ADD(tx_new.art_start_date, INTERVAL 12 MONTH)) ";
  }
}
