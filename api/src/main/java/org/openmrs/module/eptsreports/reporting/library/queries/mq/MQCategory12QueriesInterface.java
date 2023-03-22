package org.openmrs.module.eptsreports.reporting.library.queries.mq;

public interface MQCategory12QueriesInterface {

  class QUERY {

    public static final String
        findPatientsWhoAreNewlyEnrolledOnARTByAge14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12A =
            "SELECT patient_id FROM ( "
                + "SELECT patient_id, MIN(art_start_date) art_start_date FROM ( "
                + "SELECT p.patient_id, MIN(value_datetime) art_start_date FROM patient p "
                + "INNER JOIN encounter e ON p.patient_id=e.patient_id "
                + "INNER JOIN obs o ON e.encounter_id=o.encounter_id "
                + "WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=53 "
                + "AND o.concept_id=1190 AND o.value_datetime is NOT NULL AND o.value_datetime<=:endRevisionDate AND e.location_id=:location "
                + "GROUP BY p.patient_id "
                + ") art_start GROUP "
                + "BY patient_id "
                + ") tx_new "
                + "WHERE art_start_date BETWEEN date_add(:endRevisionDate, interval -14 MONTH) AND  date_add(:endRevisionDate, interval -11 MONTH) ";

    public static final String
        findPatientsWhoAreInTheFirstLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12B1 =
            "select firstLine.patient_id from ( "
                + "select maxLinha.patient_id, maxLinha.maxDataLinha from ( "
                + "select p.patient_id,max(o.obs_datetime) maxDataLinha from patient p "
                + "join encounter e on p.patient_id=e.patient_id "
                + "join obs o on o.encounter_id=e.encounter_id "
                + "where e.encounter_type=6 and e.voided=0 and o.voided=0 and p.voided=0 "
                + "and o.concept_id=21151 and e.location_id=:location "
                + "and o.obs_datetime between date_add(:endRevisionDate, interval -14 MONTH) AND  date_add(:endRevisionDate, interval -11 MONTH) "
                + "group by p.patient_id "
                + ") maxLinha "
                + "inner join obs on obs.person_id=maxLinha.patient_id and maxLinha.maxDataLinha=obs.obs_datetime "
                + "where obs.concept_id=21151 and obs.value_coded=21150 and obs.voided=0 and obs.location_id=:location "
                + ") firstLine ";

    public static final String
        findPatientsWhoAreNotInTheFirstLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12B1E =
            " select Diff_firstLine.patient_id from ( "
                + " SELECT maxLinha.patient_id, maxLinha.maxDataLinha FROM ( "
                + " select p.patient_id, max(o.obs_datetime) maxDataLinha "
                + " FROM patient p "
                + " join encounter e on p.patient_id = e.patient_id "
                + " join obs o on o.encounter_id = e.encounter_id "
                + " where e.encounter_type = 6 and e.voided = 0 and o.voided = 0 and p.voided = 0 "
                + " and o.concept_id = 21151 and e.location_id = :location "
                + " and o.obs_datetime BETWEEN :startInclusionDate AND :endRevisionDate "
                + " group by p.patient_id "
                + " ) maxLinha "
                + " inner join obs on obs.person_id = maxLinha.patient_id and maxLinha.maxDataLinha = obs.obs_datetime "
                + " where obs.concept_id = 21151 and obs.value_coded <> 21150 and obs.voided = 0 and obs.location_id = :location "
                + " ) Diff_firstLine ";

    public static final String
        findPatientsWhoAreInTheSecondLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateCategory12B2 =
            "select secondLine.patient_id from ( "
                + "select maxLinha.patient_id, maxLinha.maxDataLinha from ( "
                + "select p.patient_id,max(o.obs_datetime) maxDataLinha from patient p "
                + "join encounter e on p.patient_id=e.patient_id "
                + "join obs o on o.encounter_id=e.encounter_id "
                + "where e.encounter_type=6 and e.voided=0 and o.voided=0 and p.voided=0 "
                + "and o.concept_id=21151 and e.location_id=:location "
                + "and o.obs_datetime between date_add(:endRevisionDate, interval -14 MONTH) AND  date_add(:endRevisionDate, interval -11 MONTH) "
                + "group by p.patient_id "
                + ") maxLinha "
                + "inner join obs on obs.person_id=maxLinha.patient_id and maxLinha.maxDataLinha=obs.obs_datetime "
                + "where obs.concept_id=21151 and obs.value_coded=21148 and obs.voided=0 and obs.location_id=:location "
                + ") secondLine ";

    public static final String
        findPatientsWhoAreNotInTheSecondLine14MonthsBeforeRevisionDateAnd11MonthsBeforeRevisionDateB2E =
            " select secondLine.patient_id from ( "
                + " SELECT maxLinha.patient_id, maxLinha.maxDataLinha FROM ( "
                + " select p.patient_id, max(o.obs_datetime) maxDataLinha "
                + " FROM patient p "
                + " join encounter e on p.patient_id = e.patient_id "
                + " join obs o on o.encounter_id = e.encounter_id "
                + " where e.encounter_type = 6 and e.voided = 0 and o.voided = 0 and p.voided = 0 "
                + " and o.concept_id = 21151 and e.location_id = :location "
                + " and o.obs_datetime BETWEEN :startInclusionDate AND :endRevisionDate "
                + " group by p.patient_id "
                + " ) maxLinha "
                + " inner join obs on obs.person_id = maxLinha.patient_id and maxLinha.maxDataLinha = obs.obs_datetime "
                + " where obs.concept_id = 21151 and obs.value_coded <> 21148 and obs.voided = 0 and obs.location_id = :location "
                + " ) secondLine ";

    public static final String
        findPatientsWhoStartedARTInTheInclusionPeriodAndReturnedForClinicalConsultation33DaysAfterAtartingARTCategory12 =
            "SELECT ret33.patient_id FROM  (  "
                + "select tx_new.patient_id,tx_new.art_start_date as art_start_date  from (  "
                + "SELECT patient_id, MIN(art_start_date) art_start_date  FROM (  "
                + "SELECT p.patient_id, MIN(value_datetime) art_start_date FROM patient p  "
                + "INNER JOIN encounter e ON p.patient_id=e.patient_id  "
                + "INNER JOIN obs o ON e.encounter_id=o.encounter_id   "
                + "WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=53  "
                + "AND o.concept_id=1190 AND o.value_datetime is NOT NULL AND o.value_datetime<=:endInclusionDate  AND e.location_id=:location  "
                + "GROUP BY p.patient_id  "
                + ") art_start  "
                + "GROUP BY patient_id  "
                + ") tx_new    "
                + "inner join "
                + "( select p.patient_id, cc.encounter_datetime as dataret33 from patient p   "
                + "inner join encounter cc on p.patient_id=cc.patient_id   "
                + "WHERE cc.voided=0 and cc.encounter_type=6  and cc.location_id=:location and cc.encounter_datetime BETWEEN :startInclusionDate and :endRevisionDate "
                + "union "
                + "select p.patient_id, o.value_datetime as dataret33  from patient p  "
                + "inner join encounter e on p.patient_id=e.patient_id   "
                + "inner join obs o on o.encounter_id=e.encounter_id  "
                + "inner join obs oLevantou on e.encounter_id=oLevantou.encounter_id   "
                + "where  p.voided=0 and e.voided=0 and o.voided=0 and oLevantou.voided=0 and e.encounter_type=52 and o.concept_id=23866   "
                + "and o.value_datetime is not null and e.location_id=:location and  o.value_datetime BETWEEN :startInclusionDate and :endRevisionDate "
                + "and oLevantou.concept_id=23865 and oLevantou.value_coded=1065  "
                + ") ret on ret.patient_id=tx_new.patient_id "
                + "where tx_new.art_start_date BETWEEN :startInclusionDate AND :endInclusionDate and (TIMESTAMPDIFF(DAY, tx_new.art_start_date,ret.dataret33)) between 20 and 33  "
                + "group by tx_new.patient_id  "
                + ")ret33 ";

    public static final String
        findPatientsWhoStartedARTInTheInclusionPeriodAndReturnedForClinicalConsultation99DaysAfterAtartingARTCategory12 =
            " select tx_new.patient_id "
                + " from ( "
                + " SELECT patient_id, MIN(art_start_date) art_start_date "
                + " FROM ( "
                + " SELECT p.patient_id, MIN(value_datetime) art_start_date FROM patient p "
                + " INNER JOIN encounter e ON p.patient_id = e.patient_id "
                + " INNER JOIN obs o ON e.encounter_id = o.encounter_id "
                + " WHERE p.voided = 0 AND e.voided = 0 AND o.voided = 0 AND e.encounter_type = 53 "
                + " AND o.concept_id = 1190 AND o.value_datetime is NOT NULL AND o.value_datetime <= :endInclusionDate AND e.location_id = :location "
                + " GROUP BY p.patient_id  "
                + " ) art_start "
                + " where art_start.art_start_date BETWEEN :startInclusionDate AND :endInclusionDate "
                + " GROUP BY patient_id  "
                + " ) tx_new "
                + " inner join "
                + " ( "
                + " select e.patient_id, e.encounter_datetime as consulta from encounter e "
                + " where e.voided = 0 and e.encounter_type = 6 and e.location_id = :location and e.encounter_datetime > :startInclusionDate "
                + " UNION "
                + " select e_.patient_id, o_.value_datetime as consulta from "
                + " encounter e_ "
                + " inner join obs o ON o.encounter_id = e_.encounter_id and o.voided = 0 and o.concept_id = 23865 and o.value_coded = 1065 "
                + " inner join obs o_ ON o_.encounter_id = e_.encounter_id and o_.voided = 0 and o_.concept_id = 23866 "
                + " where e_.encounter_type = 52 and e_.location_id = :location and e_.voided = 0 and o_.value_datetime > :startInclusionDate "
                + " ) primeira_consulta ON primeira_consulta.patient_id = tx_new.patient_id "
                + " and (TIMESTAMPDIFF(DAY, tx_new.art_start_date, primeira_consulta.consulta)) between 20 and 33 "
                + " inner join "
                + " ( "
                + " select e.patient_id, e.encounter_datetime as consulta from encounter e "
                + " where e.voided = 0 and e.encounter_type = 6 and e.location_id = :location and e.encounter_datetime > :startInclusionDate "
                + " UNION "
                + " select e_.patient_id, o_.value_datetime as consulta from "
                + " encounter e_ "
                + " inner join obs o ON o.encounter_id = e_.encounter_id and o.voided = 0 and o.concept_id = 23865 and o.value_coded = 1065 "
                + " inner join obs o_ ON o_.encounter_id = e_.encounter_id and o_.voided = 0 and o_.concept_id = 23866 "
                + " where e_.encounter_type = 52 and e_.location_id = :location and e_.voided = 0 and o_.value_datetime > :startInclusionDate "
                + " ) segunda_consulta on tx_new.patient_id = segunda_consulta.patient_id "
                + " and (TIMESTAMPDIFF(DAY, tx_new.art_start_date, segunda_consulta.consulta)) between 34 and 66 "
                + " inner join "
                + " ( "
                + " select e.patient_id, e.encounter_datetime as consulta from encounter e "
                + " where e.voided = 0 and e.encounter_type = 6 and e.location_id = :location and e.encounter_datetime > :startInclusionDate "
                + " UNION "
                + " select e_.patient_id, o_.value_datetime as consulta from "
                + " encounter e_ "
                + " inner join obs o ON o.encounter_id = e_.encounter_id and o.voided = 0 and o.concept_id = 23865 and o.value_coded = 1065 "
                + " inner join obs o_ ON o_.encounter_id = e_.encounter_id and o_.voided = 0 and o_.concept_id = 23866 "
                + " where e_.encounter_type = 52 and e_.location_id = :location and e_.voided = 0 and o_.value_datetime > :startInclusionDate "
                + " ) terceira_consulta on tx_new.patient_id = terceira_consulta.patient_id "
                + " and (TIMESTAMPDIFF(DAY, tx_new.art_start_date, terceira_consulta.consulta)) between 67 and 99 "
                + " group by tx_new.patient_id ";
  }
}
