package org.openmrs.module.eptsreports.reporting.library.queries.mq;

import org.openmrs.module.eptsreports.reporting.utils.TypePTV;

public interface MQCategory13Section1QueriesInterface {

  class QUERY {

    public static final String findPatientsWithLastClinicalConsultationDenominatorB1 =
        "Select maxEnc.patient_id from ( "
            + "Select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and "
            + "e.encounter_datetime >=:startInclusionDate and e.encounter_datetime<=:endRevisionDate  and e.location_id=:location "
            + "group by p.patient_id "
            + ")maxEnc";

    public static final String findPatientsWithLastClinicalConsultationDenominatorB1AgeCalculation(
        int startAge, int endAge) {

      final String sql =
          "Select final.patient_id  from ( "
              + "Select maxEnc.patient_id,maxEnc.encounter_datetime from ( "
              + "Select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
              + "inner join encounter e on p.patient_id=e.patient_id "
              + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and "
              + "e.encounter_datetime >=:startInclusionDate and e.encounter_datetime<=:endRevisionDate  and e.location_id=:location "
              + "group by p.patient_id "
              + ")maxEnc "
              + "INNER JOIN person pe ON maxEnc.patient_id=pe.person_id "
              + "WHERE (TIMESTAMPDIFF(year,birthdate,maxEnc.encounter_datetime)) >= %s AND (TIMESTAMPDIFF(year,birthdate,maxEnc.encounter_datetime)) <= %s AND birthdate IS NOT NULL and pe.voided = 0 "
              + " ) final ";

      return String.format(sql, startAge, endAge);
    }

    public static final String
        findPatientsWithLastClinicalConsultationDenominatorB1AgeCalculation15Plus(int startAge) {

      final String sql =
          "Select final.patient_id from ( "
              + "Select maxEnc.patient_id,maxEnc.encounter_datetime from ( "
              + "Select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
              + "inner join encounter e on p.patient_id=e.patient_id "
              + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and "
              + "e.encounter_datetime >=:startInclusionDate and e.encounter_datetime<=:endRevisionDate  and e.location_id=:location "
              + "group by p.patient_id "
              + ")maxEnc "
              + "INNER JOIN person pe ON maxEnc.patient_id=pe.person_id "
              + "WHERE (TIMESTAMPDIFF(year,birthdate,maxEnc.encounter_datetime)) >= %s AND birthdate IS NOT NULL and pe.voided = 0 "
              + ") final";

      return String.format(sql, startAge);
    }

    public static final String
        findPatientsWithLastClinicalConsultationAdultOrPregnantOrBreatfeedingDenominatorB1AgeCalculation15Plus(
            int startAge) {

      final String sql =
          "Select final.patient_id from  (  "
              + "Select maxEnc.patient_id,maxEnc.encounter_datetime from (  "
              + "Select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p  "
              + "inner join encounter e on p.patient_id=e.patient_id  "
              + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and  "
              + "e.encounter_datetime >=:startInclusionDate and e.encounter_datetime<=:endRevisionDate  and e.location_id=:location  "
              + "group by p.patient_id  "
              + ")maxEnc  "
              + "INNER JOIN person pe ON maxEnc.patient_id=pe.person_id  "
              + "WHERE (TIMESTAMPDIFF(year,birthdate,maxEnc.encounter_datetime)) >= %s AND birthdate IS NOT NULL "
              + ") final "
              + "union "
              + "select patient_id from (   "
              + "select "
              + "p.person_id as  patient_id, "
              + "lactante.data_lactante,  "
              + "gravida.data_gravida,  "
              + "if(lactante.data_lactante is null,1, if(gravida.data_gravida is null,2, if(gravida.data_gravida>=lactante.data_lactante,1,2))) decisao "
              + "from person p  "
              + "left join  "
              + "( "
              + "select ultima_consulta.patient_id,ultima_consulta.data_ultima_consulta,e_.encounter_datetime  data_gravida from  (   "
              + "Select p.patient_id, max(e.encounter_datetime) as data_ultima_consulta from  patient p   "
              + "inner join encounter e on p.patient_id = e.patient_id   "
              + "where p.voided = 0 and e.voided = 0 and e.encounter_type = 6 and e.location_id = :location   "
              + "and e.encounter_datetime BETWEEN :startInclusionDate AND :endRevisionDate   "
              + "group by p.patient_id   "
              + ") ultima_consulta   "
              + "inner join encounter e_ on e_.patient_id = ultima_consulta.patient_id   "
              + "inner join obs obsGravida on e_.encounter_id = obsGravida.encounter_id   "
              + "inner join person pe on pe.person_id = e_.patient_id   "
              + "where pe.voided = 0 and e_.voided = 0 and obsGravida.voided = 0 and e_.encounter_type = 6 and e_.location_id = :location  "
              + "and obsGravida.concept_id = 1982 and obsGravida.value_coded = 1065 and pe.gender = 'F'   "
              + "and e_.encounter_datetime = ultima_consulta.data_ultima_consulta   "
              + ")gravida on gravida.patient_id=p.person_id "
              + "left join ( "
              + "select ultima_consulta.patient_id,ultima_consulta.data_ultima_consulta,e_.encounter_datetime  data_lactante from  (   "
              + "Select p.patient_id, max(e.encounter_datetime) as data_ultima_consulta from  patient p   "
              + "inner join encounter e on p.patient_id = e.patient_id   "
              + "where p.voided = 0 and e.voided = 0 and e.encounter_type = 6 and e.location_id = :location   "
              + "and e.encounter_datetime BETWEEN :startInclusionDate AND :endRevisionDate   "
              + "group by p.patient_id   "
              + ") ultima_consulta   "
              + "inner join encounter e_ on e_.patient_id = ultima_consulta.patient_id   "
              + "inner join obs obsGravida on e_.encounter_id = obsGravida.encounter_id   "
              + "inner join person pe on pe.person_id = e_.patient_id   "
              + "where pe.voided = 0 and e_.voided = 0 and obsGravida.voided = 0 and e_.encounter_type = 6 and e_.location_id = :location  "
              + "and obsGravida.concept_id = 6332 and obsGravida.value_coded = 1065 and pe.gender = 'F'   "
              + "and e_.encounter_datetime = ultima_consulta.data_ultima_consulta   "
              + ")lactante on lactante.patient_id=p.person_id "
              + "where (lactante.data_lactante is not null or gravida.data_gravida is not null) and p.voided=0 and p.gender='F' "
              + "group by p.person_id "
              + ")f  where f.decisao=2 ";

      return String.format(sql, startAge);
    }

    public static final String
        findPatientsWithLastClinicalConsultationwhoAreInFistLineDenominatorB2 =
            "Select final.patient_id from ( "
                + "Select firstLine.patient_id,firstLine.dataLinha, firstLine.ultimaConsulta from ( "
                + "Select enc.patient_id,enc.encounter_datetime ultimaConsulta,min(obsLinha.obs_datetime) dataLinha from ( "
                + "Select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
                + "inner join encounter e on p.patient_id=e.patient_id "
                + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and "
                + "e.encounter_datetime >:startInclusionDate and e.encounter_datetime<=:endRevisionDate  and e.location_id=:location "
                + "group by p.patient_id "
                + ") enc "
                + "inner join encounter  e on e.patient_id=enc.patient_id "
                + " inner join obs obsLinha on obsLinha.encounter_id=e.encounter_id	"
                + "where obsLinha.concept_id=21151 and e.encounter_type=6 and obsLinha.value_coded=21150 "
                + "and obsLinha.voided=0 and e.voided=0 and e.encounter_datetime<enc.encounter_datetime and e.location_id=:location "
                + "group by enc.patient_id "
                + ") firstLine "
                + "where (TIMESTAMPDIFF(MONTH,firstLine.dataLinha,firstLine.ultimaConsulta)) >= 6 "
                + ") final ";

    public static final String
        findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2E =
            "Select final.patient_id from ("
                + "Select firstLine.patient_id,firstLine.dataLinha, firstLine.ultimaConsulta,obsDiferenteLinha.obs_datetime dataLinhaDiferente from ( "
                + "Select enc.patient_id,enc.encounter_datetime ultimaConsulta,min(obsLinha.obs_datetime) dataLinha from ( "
                + "Select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
                + "inner join encounter e on p.patient_id=e.patient_id "
                + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and "
                + "e.encounter_datetime >:startInclusionDate and e.encounter_datetime<=:endRevisionDate  and e.location_id=:location "
                + "group by p.patient_id "
                + ") enc "
                + "inner join encounter  e on e.patient_id=enc.patient_id "
                + "inner join obs obsLinha on obsLinha.encounter_id=e.encounter_id "
                + "where obsLinha.concept_id=21151 and e.encounter_type=6 and obsLinha.value_coded=21150 "
                + "and obsLinha.voided=0 and e.voided=0 and e.encounter_datetime<enc.encounter_datetime and e.location_id=:location "
                + "group by enc.patient_id "
                + ") firstLine "
                + "left join obs obsDiferenteLinha on firstLine.patient_id=obsDiferenteLinha.person_id  "
                + "and obsDiferenteLinha.voided=0 and obsDiferenteLinha.concept_id=21151  "
                + "and obsDiferenteLinha.value_coded<>21150  "
                + "and obsDiferenteLinha.obs_datetime>firstLine.dataLinha  "
                + "and obsDiferenteLinha.obs_datetime<=firstLine.ultimaConsulta  "
                + "and obsDiferenteLinha.location_id=:location "
                + "where (TIMESTAMPDIFF(MONTH,firstLine.dataLinha,firstLine.ultimaConsulta)) >= 6  "
                + "and obsDiferenteLinha.obs_datetime is null "
                + ") final ";

    public static final String
        findPatientsWithLastClinicalConsultationwhoAreInLinhaAlternativaDenominatorB3 =
            "Select enc.patient_id from ( "
                + "Select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
                + "inner join encounter e on p.patient_id=e.patient_id "
                + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and "
                + "e.encounter_datetime >=:startInclusionDate and e.encounter_datetime<=:endRevisionDate  and e.location_id=:location "
                + "group by p.patient_id "
                + ") enc "
                + "inner join "
                + "( "
                + "Select e.patient_id,max(obsLinha.obs_datetime) dataLinha "
                + "from patient p "
                + "inner join encounter  e on e.patient_id=p.patient_id "
                + "inner join obs obsLinha on obsLinha.encounter_id=e.encounter_id "
                + "where  p.voided=0 and obsLinha.concept_id=21190 and e.encounter_type=53 and obsLinha.voided=0 and e.voided=0 and  e.location_id=:location and obsLinha.obs_datetime<=:endRevisionDate "
                + "group by p.patient_id "
                + ")alternativeLine on alternativeLine.patient_id = enc.patient_id "
                + "where alternativeLine.dataLinha<enc.encounter_datetime and (TIMESTAMPDIFF(DAY,alternativeLine.dataLinha,enc.encounter_datetime)) >= 165 ";

    public static final String
        findPatientsWithLastClinicalConsultationwhoAreDiferentFirstLineLinhaAternativaDenominatorB3E =
            " Select linhaAlternativa.patient_id from ( "
                + "Select enc.patient_id,enc.encounter_datetime ultimaConsulta,max(obsLinha.obs_datetime) dataLinha from ( "
                + "Select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
                + "inner join encounter e on p.patient_id=e.patient_id "
                + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and "
                + "e.encounter_datetime >=:startInclusionDate and e.encounter_datetime<=:endRevisionDate  and e.location_id=:location "
                + "group by p.patient_id "
                + ") enc "
                + "inner join encounter  e on e.patient_id=enc.patient_id "
                + "inner join obs obsLinha on obsLinha.encounter_id=e.encounter_id "
                + "where obsLinha.concept_id=21190 and e.encounter_type=53 "
                + "and obsLinha.voided=0 and e.voided=0 and obsLinha.obs_datetime<enc.encounter_datetime and e.location_id=:location "
                + "group by enc.patient_id "
                + ") linhaAlternativa "
                + " inner join encounter e_ on e_.patient_id = linhaAlternativa.patient_id and e_.voided = 0 and e_.encounter_type = 6 and e_.location_id = :location "
                + " inner join obs obsDiferenteLinha on e_.encounter_id = obsDiferenteLinha.encounter_id "
                + "and obsDiferenteLinha.voided=0 and obsDiferenteLinha.concept_id=21151 and obsDiferenteLinha.value_coded<>21150 "
                + "and obsDiferenteLinha.obs_datetime>=linhaAlternativa.dataLinha "
                + "and obsDiferenteLinha.obs_datetime<=linhaAlternativa.ultimaConsulta "
                + "and obsDiferenteLinha.location_id=:location "
                + "where (TIMESTAMPDIFF(DAY,linhaAlternativa.dataLinha,linhaAlternativa.ultimaConsulta)) >= 165 and obsDiferenteLinha.obs_datetime is not null group by linhaAlternativa.patient_id";

    public static final String findPatientsWithCVDenominatorB4E =
        "Select final.patient_id from ( "
            + "Select cv.patient_id,enc.encounter_datetime ultimaConsulta,min(cv.dataCV) data_cv from ( "
            + "Select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and "
            + "e.encounter_datetime >=:startInclusionDate and e.encounter_datetime<=:endRevisionDate and e.location_id=:location "
            + "group by p.patient_id "
            + ") enc "
            + "inner join "
            + "( "
            + "Select p.patient_id,e.encounter_datetime as dataCV from patient p "
            + "inner join encounter  e on e.patient_id=p.patient_id "
            + "inner join obs obscv on obscv.encounter_id=e.encounter_id "
            + "where  e.voided=0 and ((obscv.concept_id = 856 and obscv.value_numeric < 1000) OR (obscv.concept_id = 1305 and obscv.value_coded is not null)) "
            + "and e.encounter_datetime <=:endRevisionDate and e.encounter_type=6 and obscv.voided=0 and e.location_id=:location "
            + "union "
            + "Select p.patient_id,obscv.obs_datetime as dataCV from patient p "
            + "inner join encounter  e on e.patient_id=p.patient_id "
            + "inner join obs obscv on obscv.encounter_id=e.encounter_id "
            + "where  e.voided=0 and ((obscv.concept_id = 856 and obscv.value_numeric < 1000) OR (obscv.concept_id = 1305 and obscv.value_coded is not null)) "
            + "and obscv.obs_datetime<=:endRevisionDate and e.encounter_type =53 and obscv.voided=0 and e.location_id=:location "
            + ") cv on cv.patient_id=enc.patient_id "
            + "where cv.dataCV BETWEEN date_add(enc.encounter_datetime, interval -12 MONTH) and enc.encounter_datetime "
            + "group by patient_id "
            + ") final ";

    public static final String findPatientsWithRequestCVDenominatorB5E =
        "Select final.patient_id from ( "
            + "Select cvPedido.patient_id,enc.encounter_datetime ultimaConsulta,min(cvPedido.dataPedidoCV) dataPedidoCV from ( "
            + "Select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and "
            + "e.encounter_datetime >=:startInclusionDate and e.encounter_datetime<=:endRevisionDate   and e.location_id=:location "
            + "group by p.patient_id "
            + ") enc inner join ( "
            + "Select p.patient_id,o.obs_datetime as dataPedidoCV from patient p "
            + "inner join encounter  e on e.patient_id=p.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id	 "
            + "where  e.voided=0 and o.concept_id = 23722 and o.value_coded=856 and e.encounter_type=6 and o.voided=0 and e.location_id=:location "
            + ") cvPedido on cvPedido.patient_id=enc.patient_id "
            + "where cvPedido.dataPedidoCV >= date_add(enc.encounter_datetime, interval -12 MONTH) and cvPedido.dataPedidoCV < enc.encounter_datetime "
            + "group by patient_id "
            + ") final";

    public static final String findPatientsWithRequestCVInTheLast12MonthsBeforeLastConsultation =
        "Select final.patient_id from ( "
            + "Select cvPedido.patient_id,enc.encounter_datetime ultimaConsulta,min(cvPedido.dataPedidoCV) dataPedidoCV from ( "
            + "Select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and "
            + "e.encounter_datetime >=:startInclusionDate and e.encounter_datetime<=:endRevisionDate   and e.location_id=:location "
            + "group by p.patient_id "
            + ") enc inner join ( "
            + "Select p.patient_id,o.obs_datetime as dataPedidoCV from patient p "
            + "inner join encounter  e on e.patient_id=p.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id	 "
            + "where  e.voided=0 and o.concept_id = 23722 and o.value_coded=856 and e.encounter_type=6 and o.voided=0 and e.location_id=:location "
            + ") cvPedido on cvPedido.patient_id=enc.patient_id "
            + "where cvPedido.dataPedidoCV >= date_add(enc.encounter_datetime, interval -12 MONTH) and cvPedido.dataPedidoCV < enc.encounter_datetime "
            + "group by patient_id "
            + ") final";

    public static final String findNumeratorC =
        " Select enc.patient_id from ( "
            + " Select p.patient_id, max(e.encounter_datetime) encounter_datetime from patient p "
            + " inner join encounter e on p.patient_id = e.patient_id "
            + " where p.voided = 0 and e.voided = 0 and e.encounter_type = 6 and "
            + " e.encounter_datetime >= :startInclusionDate and e.encounter_datetime <= :endRevisionDate and e.location_id = :location "
            + " group by p.patient_id "
            + " ) enc "
            + " inner join encounter e_ on e_.patient_id = enc.patient_id "
            + " inner join obs cvPedido on cvPedido.encounter_id = e_.encounter_id "
            + " where cvPedido.obs_datetime = enc.encounter_datetime and cvPedido.concept_id = 23722 and cvPedido.value_coded = 856 and cvPedido.voided = 0 "
            + " and e_.voided = 0 and e_.encounter_type = 6 and e_.location_id = :location and cvPedido.location_id = :location ";

    public static final String findPatientsWhoArePregnantOrBreastfeeding(TypePTV typePTV) {
      String query =
          "select patient_id from (   "
              + "select "
              + "p.person_id as  patient_id, "
              + "lactante.data_lactante,  "
              + "gravida.data_gravida,  "
              + "if(lactante.data_lactante is null,1, if(gravida.data_gravida is null,2, if(gravida.data_gravida>=lactante.data_lactante,1,2))) decisao "
              + "from person p  "
              + "left join  "
              + "( "
              + "select ultima_consulta.patient_id,ultima_consulta.data_ultima_consulta,e_.encounter_datetime  data_gravida from  (   "
              + "Select p.patient_id, max(e.encounter_datetime) as data_ultima_consulta from  patient p   "
              + "inner join encounter e on p.patient_id = e.patient_id   "
              + "where p.voided = 0 and e.voided = 0 and e.encounter_type = 6 and e.location_id = :location   "
              + "and e.encounter_datetime BETWEEN :startInclusionDate AND :endRevisionDate   "
              + "group by p.patient_id   "
              + ") ultima_consulta   "
              + "inner join encounter e_ on e_.patient_id = ultima_consulta.patient_id   "
              + "inner join obs obsGravida on e_.encounter_id = obsGravida.encounter_id   "
              + "inner join person pe on pe.person_id = e_.patient_id   "
              + "where pe.voided = 0 and e_.voided = 0 and obsGravida.voided = 0 and e_.encounter_type = 6 and e_.location_id = :location  "
              + "and obsGravida.concept_id = 1982 and obsGravida.value_coded = 1065 and pe.gender = 'F'   "
              + "and e_.encounter_datetime = ultima_consulta.data_ultima_consulta   "
              + ")gravida on gravida.patient_id=p.person_id "
              + "left join ( "
              + "select ultima_consulta.patient_id,ultima_consulta.data_ultima_consulta,e_.encounter_datetime  data_lactante from  (   "
              + "Select p.patient_id, max(e.encounter_datetime) as data_ultima_consulta from  patient p   "
              + "inner join encounter e on p.patient_id = e.patient_id   "
              + "where p.voided = 0 and e.voided = 0 and e.encounter_type = 6 and e.location_id = :location   "
              + "and e.encounter_datetime BETWEEN :startInclusionDate AND :endRevisionDate   "
              + "group by p.patient_id   "
              + ") ultima_consulta   "
              + "inner join encounter e_ on e_.patient_id = ultima_consulta.patient_id   "
              + "inner join obs obsGravida on e_.encounter_id = obsGravida.encounter_id   "
              + "inner join person pe on pe.person_id = e_.patient_id   "
              + "where pe.voided = 0 and e_.voided = 0 and obsGravida.voided = 0 and e_.encounter_type = 6 and e_.location_id = :location  "
              + "and obsGravida.concept_id = 6332 and obsGravida.value_coded = 1065 and pe.gender = 'F'   "
              + "and e_.encounter_datetime = ultima_consulta.data_ultima_consulta   "
              + ")lactante on lactante.patient_id=p.person_id "
              + "where (lactante.data_lactante is not null or gravida.data_gravida is not null) and p.voided=0 and p.gender='F' "
              + "group by p.person_id "
              + ")f  ";

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

    public static final String
        findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2NEW =
            " select art_start.patient_id "
                + " from "
                + " ( "
                + " SELECT p.patient_id, MIN(value_datetime) art_start_date "
                + " FROM patient p "
                + " INNER JOIN encounter e ON p.patient_id = e.patient_id "
                + " INNER JOIN obs o ON e.encounter_id = o.encounter_id "
                + " WHERE p.voided = 0 AND e.voided = 0 AND o.voided = 0 AND e.encounter_type = 53 "
                + " AND o.concept_id = 1190 AND o.value_datetime is NOT NULL AND o.value_datetime <= :endRevisionDate "
                + " AND e.location_id = :location "
                + " GROUP BY p.patient_id  "
                + " ) art_start "
                + " inner join "
                + " ( "
                + " select maxLinha.patient_id, maxLinha.maxDataLinha "
                + " from ( "
                + " select p.patient_id, max(e.encounter_datetime) maxDataLinha "
                + " from patient p "
                + " join encounter e on p.patient_id=e.patient_id "
                + " join obs o on o.encounter_id = e.encounter_id "
                + " where o.concept_id = 21151 and o.value_coded = 21150 "
                + " and e.voided = 0 and o.voided = 0 and o.location_id = :location and e.location_id = :location "
                + " and e.encounter_datetime between :startInclusionDate and :endRevisionDate "
                + " and e.encounter_type = 6 "
                + " group by p.patient_id  "
                + " ) maxLinha "
                + " ) primeiraLinha on art_start.patient_id = primeiraLinha.patient_id "
                + " where TIMESTAMPDIFF(DAY,art_start.art_start_date,primeiraLinha.maxDataLinha)>165 ";

    public static final String findPatientsWhoAbandonedARTInTheFirstSixMonthsOfARTStart =
        "select primeiraLinha.patient_id from ( "
            + " select art_start.patient_id,art_start.art_start_date,maxDataLinha "
            + " from "
            + " ( "
            + " SELECT p.patient_id, MIN(value_datetime) art_start_date "
            + " FROM patient p "
            + " INNER JOIN encounter e ON p.patient_id = e.patient_id "
            + " INNER JOIN obs o ON e.encounter_id = o.encounter_id "
            + " WHERE p.voided = 0 AND e.voided = 0 AND o.voided = 0 AND e.encounter_type = 53 "
            + " AND o.concept_id = 1190 AND o.value_datetime is NOT NULL AND o.value_datetime between :startInclusionDate and :endInclusionDate "
            + " AND e.location_id = :location "
            + " GROUP BY p.patient_id "
            + " ) art_start "
            + " inner join "
            + " ( "
            + " select maxLinha.patient_id, maxLinha.maxDataLinha "
            + " from ( "
            + " select p.patient_id, max(e.encounter_datetime) maxDataLinha "
            + " from patient p "
            + " join encounter e on p.patient_id=e.patient_id "
            + " where e.encounter_type = 6 and e.voided = 0 and p.voided = 0 "
            + " and e.encounter_datetime between :startInclusionDate and :endRevisionDate "
            + " group by p.patient_id "
            + " ) maxLinha "
            + " join encounter e on e.patient_id = maxLinha.patient_id "
            + " join obs on obs.encounter_id = e.encounter_id "
            + " where obs.concept_id = 21151 and obs.value_coded = 21150 and maxLinha.maxDataLinha = e.encounter_datetime "
            + " and obs.voided = 0 and obs.location_id = :location and e.location_id = :location "
            + " and e.voided = 0 and e.encounter_type = 6 "
            + " ) primeiraLinha on art_start.patient_id = primeiraLinha.patient_id "
            + " group by primeiraLinha.patient_id "
            + " )primeiraLinha "
            + " inner join "
            + " ( "
            + "select p.patient_id, max(o.obs_datetime) data_estado from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs  o on e.encounter_id=o.encounter_id "
            + "where e.voided=0 and o.voided=0 and p.voided=0 and  e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1707) and e.location_id=:location "
            + "group by p.patient_id "
            + " ) abandono on abandono.patient_id = primeiraLinha.patient_id "
            + " where (TIMESTAMPDIFF(MONTH,primeiraLinha.art_start_date, primeiraLinha.maxDataLinha)) >= 6 and "
            + " abandono.data_estado between primeiraLinha.art_start_date and date_add(primeiraLinha.art_start_date, interval 6 MONTH) ";

    public static final String
        findPatientsMarkedAsReinitiatedARTForAtLeastSixMonthsBeforeLastClinicalConsultation =
            " Select reinicio_tarv.patient_id "
                + " from ( "
                + "Select enc.patient_id,enc.encounter_datetime ultimaConsulta,max(o.obs_datetime) data_reinicio_tarv from ( "
                + "Select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
                + "inner join encounter e on p.patient_id=e.patient_id "
                + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and "
                + "e.encounter_datetime >=:startInclusionDate and e.encounter_datetime<=:endRevisionDate  and e.location_id=:location "
                + "group by p.patient_id "
                + ") enc "
                + "inner join encounter e on e.patient_id=enc.patient_id "
                + "inner join obs  o on e.encounter_id=o.encounter_id "
                + "where e.voided=0 and o.voided=0 and  e.encounter_type = 6 and o.concept_id = 6273 and o.value_coded = 1705 and o.obs_datetime>=:startInclusionDate and o.obs_datetime<=:endInclusionDate and e.location_id=:location "
                + "group by enc.patient_id "
                + ") reinicio_tarv "
                + "where (TIMESTAMPDIFF(MONTH,reinicio_tarv.data_reinicio_tarv,reinicio_tarv.ultimaConsulta)) >= 6 ";

    public static final String
        findPatientsWhoAbandonedARTInTheFirstSixMonthsAfterChangeFirstLineRegimenART =
            " Select linhaAlternativa.patient_id "
                + " from ( "
                + "Select enc.patient_id,enc.encounter_datetime ultimaConsulta,max(obsLinha.obs_datetime) dataLinha from ( "
                + "Select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
                + "inner join encounter e on p.patient_id=e.patient_id "
                + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and "
                + "e.encounter_datetime >=:startInclusionDate and e.encounter_datetime<=:endRevisionDate  and e.location_id=:location "
                + "group by p.patient_id "
                + ") enc "
                + "inner join encounter  e on e.patient_id=enc.patient_id "
                + "inner join obs obsLinha on obsLinha.encounter_id=e.encounter_id "
                + "where obsLinha.concept_id=21190 and e.encounter_type=53 "
                + "and obsLinha.voided=0 and e.voided=0 and obsLinha.obs_datetime<enc.encounter_datetime and e.location_id=:location "
                + "group by enc.patient_id "
                + ") linhaAlternativa "
                + "inner join "
                + " ( "
                + "select p.patient_id, max(o.obs_datetime) data_estado from patient p "
                + "inner join encounter e on p.patient_id=e.patient_id "
                + "inner join obs  o on e.encounter_id=o.encounter_id "
                + "where e.voided=0 and o.voided=0 and p.voided=0 and  e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1707) and e.location_id=:location "
                + "group by p.patient_id "
                + " ) abandono on abandono.patient_id = linhaAlternativa.patient_id "
                + "where (TIMESTAMPDIFF(MONTH,linhaAlternativa.dataLinha,linhaAlternativa.ultimaConsulta)) >= 6 and "
                + "abandono.data_estado between linhaAlternativa.dataLinha and date_add(linhaAlternativa.dataLinha, interval 6 MONTH) "
                + "group by linhaAlternativa.patient_id ";

    public static final String
        findPatientsWhoAbandonedARTInTheFirstSixMonthsAfterInitiatedSecondLineRegimenART =
            "Select segundaLinha.patient_id from ( "
                + " Select enc.patient_id, enc.encounter_datetime ultimaConsulta, max(obsLinha.obs_datetime) dataLinha from ( "
                + " Select p.patient_id, max(e.encounter_datetime) encounter_datetime from patient p "
                + " inner join encounter e on p.patient_id=e.patient_id "
                + " where p.voided = 0 and e.voided = 0 and e.encounter_type = 6 and "
                + " e.encounter_datetime >= :startInclusionDate and e.encounter_datetime <= :endRevisionDate and e.location_id = :location "
                + " group by p.patient_id "
                + " ) enc "
                + " inner join encounter e on e.patient_id = enc.patient_id "
                + " inner join obs obsLinha on obsLinha.encounter_id = e.encounter_id "
                + " where obsLinha.concept_id = 21187 and e.encounter_type = 53 "
                + " and obsLinha.voided = 0 and e.voided = 0 and obsLinha.obs_datetime < enc.encounter_datetime and e.location_id = :location "
                + " group by enc.patient_id "
                + " ) segundaLinha "
                + " inner join "
                + " ( "
                + "select p.patient_id, max(o.obs_datetime) data_estado from patient p "
                + "inner join encounter e on p.patient_id=e.patient_id "
                + "inner join obs  o on e.encounter_id=o.encounter_id "
                + "where e.voided=0 and o.voided=0 and p.voided=0 and  e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1707) and e.location_id=:location "
                + "group by p.patient_id "
                + " ) abandono on abandono.patient_id = segundaLinha.patient_id "
                + "where  (TIMESTAMPDIFF(MONTH,segundaLinha.dataLinha,segundaLinha.ultimaConsulta)) >= 6 and "
                + "abandono.data_estado between segundaLinha.dataLinha and date_add(segundaLinha.dataLinha, interval 6 MONTH) "
                + "group by segundaLinha.patient_id ";

    public static final String
        findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2NEWPartII =
            " select maxEnc.patient_id "
                + " from "
                + " ( "
                + " Select p.patient_id, max(e.encounter_datetime) encounter_datetime from patient p "
                + " inner join encounter e on p.patient_id = e.patient_id "
                + " where p.voided = 0 and e.voided = 0 and e.encounter_type = 6 and "
                + " e.encounter_datetime >= :startInclusionDate and e.encounter_datetime <= :endRevisionDate  and e.location_id = :location "
                + " group by p.patient_id "
                + " )maxEnc "
                + " inner join "
                + " ( "
                + " select p.patient_id, o.obs_datetime "
                + " from patient p "
                + " join encounter e on p.patient_id = e.patient_id "
                + " join obs o on o.encounter_id = e.encounter_id "
                + " where e.encounter_type = 53 and e.voided = 0 and p.voided = 0 and o.voided = 0 and o.value_coded is not null "
                + " and o.concept_id = 21187 "
                + " and o.obs_datetime between :startInclusionDate and :endRevisionDate and e.location_id = :location "
                + " group by p.patient_id "
                + " ) segundaLinha on maxEnc.patient_id = segundaLinha.patient_id "
                + " where maxEnc.encounter_datetime >= date_add(segundaLinha.obs_datetime, INTERVAL 165 DAY) ";

    public static final String
        findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2ENEW =
            " Select linhaAlternativa.patient_id from ( "
                + " Select enc.patient_id, enc.encounter_datetime ultimaConsulta, max(obsLinha.obs_datetime) dataLinha from ( "
                + " Select p.patient_id, max(e.encounter_datetime) encounter_datetime from patient p "
                + " inner join encounter e on p.patient_id=e.patient_id "
                + " where p.voided = 0 and e.voided = 0 and e.encounter_type = 6 and "
                + " e.encounter_datetime >= :startInclusionDate and e.encounter_datetime <= :endRevisionDate and e.location_id = :location "
                + " group by p.patient_id "
                + " ) enc "
                + " inner join encounter e on e.patient_id = enc.patient_id "
                + " inner join obs obsLinha on obsLinha.encounter_id = e.encounter_id "
                + " where obsLinha.concept_id = 21187 and e.encounter_type = 53 "
                + " and obsLinha.voided = 0 and e.voided = 0 and obsLinha.obs_datetime < enc.encounter_datetime and e.location_id = :location "
                + " group by enc.patient_id "
                + " ) linhaAlternativa "
                + " left join encounter e_ on e_.patient_id = linhaAlternativa.patient_id and e_.voided = 0 and e_.encounter_type = 6 and e_.location_id = :location "
                + " left join obs obsDiferenteLinha on e_.encounter_id = obsDiferenteLinha.encounter_id "
                + " and obsDiferenteLinha.voided = 0 and obsDiferenteLinha.concept_id = 21151 and obsDiferenteLinha.value_coded <> 21148 "
                + " and obsDiferenteLinha.obs_datetime > linhaAlternativa.dataLinha "
                + " and obsDiferenteLinha.obs_datetime <= linhaAlternativa.ultimaConsulta "
                + " and obsDiferenteLinha.location_id = :location "
                + " where (TIMESTAMPDIFF(DAY,linhaAlternativa.dataLinha,linhaAlternativa.ultimaConsulta)) >= 165 and obsDiferenteLinha.obs_datetime is null "
                + " group by linhaAlternativa.patient_id ";

    public static final String findPatientsWithDiagnosticoTBAtivaDuringRevisionPeriod =
        "select patient_id from ( "
            + "select maxTb.patient_id, max(max_datatb) max_datatb from ( "
            + "select p.patient_id,max(e.encounter_datetime) max_datatb from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where e.encounter_type=6 and e.voided=0 and o.voided=0 and p.voided=0 and o.concept_id=23761 and o.value_coded=1065 "
            + "and e.encounter_datetime between :startInclusionDate and :endInclusionDate and e.location_id=:location "
            + "group by p.patient_id "
            + "union "
            + "select p.patient_id, max(o.obs_datetime) max_datatb from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where e.encounter_type=6 and o.concept_id=1268 and o.value_coded in (1256,1257) and o.voided = 0 "
            + "and e.location_id=:location and e.voided=0 and p.voided=0 and o.obs_datetime between :startInclusionDate and :endInclusionDate "
            + "and e.encounter_datetime between :startInclusionDate and :endInclusionDate "
            + "group by p.patient_id "
            + ") maxTb group by maxTb.patient_id "
            + ") finalTB ";

    public static final String
        findPatientsWithRequestCVInTheLastClinicalConsultationDuringRevisionPeriod =
            "Select final.patient_id from ( "
                + "Select cvPedido.patient_id,enc.encounter_datetime ultimaConsulta,min(cvPedido.dataPedidoCV) dataPedidoCV from ( "
                + "Select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
                + "inner join encounter e on p.patient_id=e.patient_id "
                + "where p.voided=0 and e.voided=0 and e.encounter_type=6 and "
                + "e.encounter_datetime >=:startInclusionDate and e.encounter_datetime<=:endRevisionDate   and e.location_id=:location "
                + "group by p.patient_id "
                + ") enc inner join ( "
                + "Select p.patient_id,o.obs_datetime as dataPedidoCV from patient p "
                + "inner join encounter  e on e.patient_id=p.patient_id "
                + "inner join obs o on o.encounter_id=e.encounter_id	 "
                + "where  e.voided=0 and o.concept_id = 23722 and o.value_coded=856 and e.encounter_type=6 and o.voided=0 and e.location_id=:location "
                + ") cvPedido on cvPedido.patient_id=enc.patient_id "
                + "where date(cvPedido.dataPedidoCV) = date(enc.encounter_datetime) "
                + "group by patient_id "
                + ") final";
  }
}
