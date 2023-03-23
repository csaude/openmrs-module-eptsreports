package org.openmrs.module.eptsreports.reporting.library.queries.mq;

public interface MQQueriesInterface {

  class QUERY {
    public static final String findPatientsWhoAreNewlyEnrolledOnARTRF05 =
        "SELECT tx_new.patient_id FROM (   "
            + "SELECT patient_id, MIN(art_start_date) art_start_date FROM  "
            + "( SELECT p.patient_id, o.value_datetime art_start_date FROM patient p   "
            + "INNER JOIN encounter e ON p.patient_id = e.patient_id   "
            + "INNER JOIN obs o ON e.encounter_id = o.encounter_id   "
            + "WHERE p.voided = 0   "
            + "AND e.voided = 0   "
            + "AND o.voided = 0   "
            + "AND e.encounter_type = 53   "
            + "AND o.concept_id = 1190   "
            + "AND e.location_id=:location "
            + "AND date(o.value_datetime) <= :endRevisionDate   "
            + ") art_start   "
            + "GROUP BY patient_id   "
            + ") tx_new WHERE art_start_date BETWEEN :startInclusionDate AND :endInclusionDate ";
    public static final String
        findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardRF06 =
            "SELECT p.patient_id from patient p "
                + "INNER JOIN encounter e ON p.patient_id=e.patient_id "
                + "INNER JOIN obs obsTrans ON e.encounter_id=obsTrans.encounter_id AND obsTrans.voided=0 AND obsTrans.concept_id=1369 AND obsTrans.value_coded=1065 "
                + "INNER JOIN obs obsTarv ON e.encounter_id=obsTarv.encounter_id AND obsTarv.voided=0 AND obsTarv.concept_id=6300 AND obsTarv.value_coded=6276 "
                + "WHERE p.voided=0 AND e.voided=0 AND e.encounter_type=53 AND  e.location_id=:location ";

    public static final String findPatientsWhoTransferedOutRF07 =
        "select saida.patient_id from "
            + "( "
            + "select p.patient_id, max(o.obs_datetime) data_estado from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs  o on e.encounter_id=o.encounter_id "
            + "where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (53,6) and "
            + "o.concept_id in(6272,6273) and o.value_coded=1706 and o.obs_datetime<=:endRevisionDate and e.location_id=:location "
            + "group by p.patient_id "
            + ") saida "
            + "inner join "
            + "( "
            + "select patient_id,max(encounter_datetime) encounter_datetime from ( "
            + "select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_datetime<=:endRevisionDate and "
            + "e.location_id=:location and e.encounter_type=6 "
            + "group by p.patient_id "
            + "union "
            + "select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_datetime<=:endRevisionDate and "
            + "e.location_id=:location and e.encounter_type=18 "
            + "group by p.patient_id "
            + ") consultaLev "
            + "group by patient_id "
            + ") consultaOuARV on saida.patient_id=consultaOuARV.patient_id "
            + "where consultaOuARV.encounter_datetime <= saida.data_estado and saida.data_estado <= :endRevisionDate ";

    public static final String getPatientsWhoDiedEndRevisioDate =
        "select obito.patient_id from ( "
            + "select patient_id,max(data_obito) data_obito from ( "
            + "select maxEstado.patient_id,maxEstado.data_obito from ( "
            + "select pg.patient_id,max(ps.start_date) data_obito from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "where pg.voided=0 and ps.voided=0 and p.voided=0 and "
            + "pg.program_id=2 and ps.start_date<=:endRevisionDate and pg.location_id=:location "
            + "group by p.patient_id ) maxEstado "
            + "inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id "
            + "inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id "
            + "where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and "
            + "ps2.start_date=maxEstado.data_obito and pg2.location_id=:location and ps2.state=10 "
            + "union "
            + "select p.patient_id,max(o.obs_datetime) data_obito from  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where e.voided=0 and p.voided=0 and o.obs_datetime<=:endRevisionDate and "
            + "o.voided=0 and o.concept_id=6272 and o.value_coded=1366 and e.encounter_type=53 and  e.location_id=:location group by p.patient_id "
            + "union  "
            + "select p.patient_id,max(e.encounter_datetime) data_obito from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id where e.voided=0 and p.voided=0 and e.encounter_datetime<=:endRevisionDate "
            + "and o.voided=0 and o.concept_id=6273 and o.value_coded=1366 and e.encounter_type=6 and  e.location_id=:location "
            + "group by p.patient_id "
            + "union  "
            + "Select person_id,death_date from person p where p.dead=1 and p.death_date<=:endRevisionDate "
            + ")transferido "
            + "group by patient_id) obito "
            + "inner join ( "
            + "select patient_id,max(encounter_datetime) encounter_datetime from ( "
            + "select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_datetime<=:endRevisionDate and e.location_id=:location and e.encounter_type in (18,6,9) "
            + "group by p.patient_id "
            + "union "
            + "select p.patient_id,max(value_datetime) encounter_datetime from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and "
            + "o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=:endRevisionDate and e.location_id=:location "
            + "group by p.patient_id ) consultaLev "
            + "group by patient_id ) "
            + "consultaOuARV on obito.patient_id=consultaOuARV.patient_id "
            + "where consultaOuARV.encounter_datetime<=obito.data_obito and obito.data_obito <= :endRevisionDate ";

    public static final String findPatientsWhoArePregnantInclusionDateRF08 =
        "Select p.patient_id from person pe "
            + "inner join patient p on pe.person_id=p.patient_id "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "inner join obs obsGravida on e.encounter_id=obsGravida.encounter_id "
            + "where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0 and obsGravida.voided=0 and e.encounter_type=53 and e.location_id=:location and "
            + "o.concept_id=1190 and o.value_datetime is not null and "
            + "obsGravida.concept_id=1982 and obsGravida.value_coded=1065 and pe.gender='F' ";

    public static final String findPatientsWhoAreBreastfeedingInclusionDateRF09 =
        "Select p.patient_id from person pe "
            + "inner join patient p on pe.person_id=p.patient_id "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "inner join obs obsLactante on e.encounter_id=obsLactante.encounter_id "
            + "where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0 and obsLactante.voided=0 and e.encounter_type=53 and e.location_id=:location and "
            + "o.concept_id=1190 and o.value_datetime is not null and "
            + "obsLactante.concept_id=6332 and obsLactante.value_coded=1065 and pe.gender='F' ";

    public static final String
        findPatientsWhoAreNewEnrolledOnArtByAgeUsingYearAdulyAndHaveFirstConsultInclusionPeriodCategory3FR12Numerator =
            " SELECT FichaResumo.patient_id "
                + " FROM "
                + " ( "
                + " SELECT patient_id, min(TR_Date) as TR_Date FROM "
                + " (SELECT p.patient_id AS patient_id, o.obs_datetime As TR_Date "
                + " FROM patient p "
                + " INNER JOIN encounter e ON p.patient_id = e.patient_id "
                + " INNER JOIN obs o ON e.encounter_id = o.encounter_id "
                + " WHERE p.voided = 0 AND e.voided = 0 AND o.voided = 0 AND o.concept_id = 22772 "
                + " AND o.value_coded IN (1030,1040) "
                + " AND e.encounter_type = 53 "
                + " AND e.location_id IN (:location) "
                + " AND o.obs_datetime <= :endInclusionDate "
                + " UNION "
                + " SELECT p.patient_id AS patient_id, o.obs_datetime As TR_Date "
                + " FROM patient p "
                + " INNER JOIN encounter e ON p.patient_id = e.patient_id "
                + " INNER JOIN obs o ON e.encounter_id = o.encounter_id "
                + " WHERE p.voided = 0 AND e.voided = 0 AND o.voided = 0 AND o.concept_id = 23807 "
                + " AND o.value_coded = 1065 "
                + " AND e.encounter_type = 53 "
                + " AND e.location_id IN (:location) "
                + " AND o.obs_datetime <= :endInclusionDate) Minconsult "
                + " group by patient_id "
                + " ) FichaResumo "
                + " INNER JOIN "
                + " ( "
                + " SELECT p.patient_id AS patient_id, e.encounter_datetime AS consult_date "
                + " FROM patient p "
                + " INNER JOIN encounter e ON p.patient_id = e.patient_id "
                + " WHERE p.voided = 0 AND e.voided = 0 "
                + " AND e.encounter_type = 6 "
                + " AND e.location_id IN (:location) "
                + " AND e.encounter_datetime <= :endInclusionDate "
                + " group by p.patient_id "
                + " ) FichaClinica on FichaResumo.patient_id = FichaClinica.patient_id "
                + " AND FichaClinica.consult_date between FichaResumo.TR_Date AND date_add(FichaResumo.TR_Date, INTERVAL 7 DAY)";

    public static final String findPatientsWhoHasNutritionalAssessmentInLastConsultation =
        "select  firstClinica.patient_id  from (  "
            + "select  "
            + "p.patient_id,  "
            + "max(e.encounter_datetime) art_start_date  from patient p  "
            + "inner join encounter e on e.patient_id=p.patient_id  "
            + "where p.voided=0  "
            + "and e.voided=0  "
            + "and e.encounter_datetime between :startInclusionDate and :endRevisionDate  "
            + "and  e.location_id=:location  and e.encounter_type=6  "
            + "group by p.patient_id  "
            + ")  "
            + "firstClinica  "
            + "inner join obs obsGrau on obsGrau.person_id=firstClinica.patient_id  "
            + "where firstClinica.art_start_date=obsGrau.obs_datetime  "
            + "and obsGrau.concept_id=6336  "
            + "and obsGrau.value_coded in (6335,1115,1844,68)  and obsGrau.voided=0 ";

    public static final String
        findPatientsWhoAreNewEnrolledOnArtByAgeUsingYearChildAndHaveFirstConsultMarkedDAMDAGInclusionPeriod =
            " select firstClinica.patient_id "
                + " from "
                + " ( "
                + " select p.patient_id, min(e.encounter_datetime) encounter_datetime "
                + " from patient p "
                + " inner join encounter e on e.patient_id = p.patient_id "
                + " where p.voided = 0 and e.voided = 0 and e.encounter_datetime between :startInclusionDate and :endRevisionDate and "
                + " e.location_id = :location and e.encounter_type = 6 "
                + " group by p.patient_id "
                + " ) firstClinica "
                + " inner join obs obsGrau on obsGrau.person_id = firstClinica.patient_id "
                + " where firstClinica.encounter_datetime = obsGrau.obs_datetime and obsGrau.concept_id = 6336 "
                + " and obsGrau.value_coded in (68,1844) and obsGrau.voided = 0 ";

    public static final String
        findPatientsWhoAreNewEnrolledOnArtByAgeUsingYearChildAndHaveFirstConsultMarkedDAMDAGANDATPUSOJAInclusionPeriod =
            " select firstClinica.patient_id "
                + " from "
                + " ( "
                + " select p.patient_id, min(e.encounter_datetime) encounter_datetime "
                + " from patient p "
                + " inner join encounter e on e.patient_id = p.patient_id "
                + " where p.voided = 0 and e.voided = 0 and e.encounter_datetime between :startInclusionDate and :endRevisionDate and "
                + " e.location_id = :location and e.encounter_type = 6 "
                + " group by p.patient_id "
                + " ) firstClinica "
                + " inner join obs obsGrau on obsGrau.person_id = firstClinica.patient_id "
                + " inner join obs obsApoio on obsApoio.person_id = firstClinica.patient_id "
                + " where firstClinica.encounter_datetime = obsGrau.obs_datetime and obsGrau.concept_id = 6336 and obsGrau.value_coded "
                + " in (68,1844) and obsGrau.voided = 0 and "
                + " firstClinica.encounter_datetime = obsApoio.obs_datetime and obsApoio.concept_id = 2152 and obsApoio.value_coded in "
                + " (2151,6143) and obsApoio.voided = 0 ";

    public static final String findPatientsWhoHasNutritionalAssessmentDAMandDAGInLastConsultation =
        "select firstClinica.patient_id from ( "
            + "select p.patient_id, max(e.encounter_datetime) art_start_date from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_datetime between :startInclusionDate and :endRevisionDate and "
            + "e.location_id=:location and e.encounter_type=6  group by p.patient_id "
            + ") "
            + "firstClinica "
            + "inner join obs obsGrau on obsGrau.person_id=firstClinica.patient_id "
            + "where firstClinica.art_start_date=obsGrau.obs_datetime and obsGrau.concept_id=6336 and obsGrau.value_coded in (1844,68) "
            + "and obsGrau.voided=0 ";

    public static final String
        findPatientsWhoHasNutritionalAssessmentDAMandDAGAndATPUInLastConsultation =
            "select firstClinica.patient_id from  (   "
                + "select p.patient_id,min(e.encounter_datetime) encounter_datetime  from  patient p   "
                + "inner join encounter e on e.patient_id=p.patient_id  "
                + "where p.voided=0 and e.voided=0 and e.encounter_datetime between :startInclusionDate and :endRevisionDate and   "
                + "e.location_id=:location and e.encounter_type=6   "
                + "group by p.patient_id   "
                + ") firstClinica   "
                + "inner join obs obsGrau on obsGrau.person_id=firstClinica.patient_id   "
                + "inner join obs obsApoio on obsApoio.person_id=firstClinica.patient_id  "
                + "where firstClinica.encounter_datetime=obsGrau.obs_datetime and obsGrau.concept_id=6336 and obsGrau.value_coded in (68,1844) and obsGrau.voided=0 and   "
                + "firstClinica.encounter_datetime=obsApoio.obs_datetime and obsApoio.concept_id=2152 and obsApoio.value_coded in (2151,6143) and obsApoio.voided=0  ";

    public static final String
        findPatientsWhoDiagnosedWithTBActiveInTheLastConsultationIThePeriodCatetory6 =
            "select lastClinica.patient_id from ( "
                + "select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
                + "inner join encounter e on e.patient_id=p.patient_id "
                + "where p.voided=0 and e.voided=0 and e.encounter_datetime between :startInclusionDate and :endRevisionDate and "
                + "e.location_id=:location and e.encounter_type=6 "
                + "group by p.patient_id "
                + ") lastClinica "
                + "inner join obs obsTBActiva on obsTBActiva.person_id=lastClinica.patient_id "
                + "where lastClinica.encounter_datetime=obsTBActiva.obs_datetime and obsTBActiva.concept_id=23761 and obsTBActiva.value_coded=1065 and obsTBActiva.voided=0 ";

    public static final String
        findPatientWwithTBScreeningAtTheLastConsultationOfThePeriodCategory6 =
            "select lastClinica.patient_id from ( "
                + "select p.patient_id,max(e.encounter_datetime) encounter_datetime  from patient p "
                + "inner join encounter e on e.patient_id=p.patient_id "
                + "where p.voided=0 and e.voided=0 and e.encounter_datetime between :startInclusionDate and :endRevisionDate and "
                + "e.location_id=:location and e.encounter_type=6 "
                + "group by p.patient_id "
                + ") lastClinica "
                + "inner join obs obsRastreio on obsRastreio.person_id=lastClinica.patient_id "
                + "where lastClinica.encounter_datetime=obsRastreio.obs_datetime and obsRastreio.concept_id=23758 and obsRastreio.value_coded in (1065,1066) and obsRastreio.voided=0 ";

    public static final String findPatientsDiagnosedWithActiveTBDuringDuringPeriodCategory7 =
        "select p.patient_id  from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "inner join obs obsTBActiva on obsTBActiva.encounter_id=e.encounter_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_datetime between :startInclusionDate and :endInclusionDate and  "
            + "e.location_id=:location and e.encounter_type=6 and obsTBActiva.concept_id=23761 and obsTBActiva.value_coded=1065 and obsTBActiva.voided=0 "
            + "group by p.patient_id ";

    public static final String findPatientsWithPositiveTBScreeningInDurindPeriodCategory7 =
        "select p.patient_id from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "inner join obs obsTBPositivo on obsTBPositivo.encounter_id=e.encounter_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_datetime between :startInclusionDate and :endInclusionDate and "
            + "e.location_id=:location and e.encounter_type=6 and obsTBPositivo.concept_id=23758 and obsTBPositivo.value_coded=1065 and obsTBPositivo.voided=0 "
            + "group by p.patient_id ";

    public static final String finPatientHaveTBTreatmentDuringPeriodCategory7 =
        "select p.patient_id from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "inner join obs obsTB on obsTB.encounter_id=e.encounter_id "
            + "where p.voided=0 and e.voided=0 and obsTB.obs_datetime between :startInclusionDate and :endInclusionDate and  "
            + "e.location_id=:location and e.encounter_type=6 and obsTB.concept_id=1268 and obsTB.value_coded in (1256,1257,1267) and obsTB.voided=0 "
            + "group by p.patient_id ";

    public static final String findPatientWhoStartTPINHDuringPeriodCategory7 =
        "select tpt.patient_id from ( "
            + "select p.patient_id, MAX(estadoProfilaxia.obs_datetime) dataInicioTPI  "
            + "from patient p  "
            + "inner join encounter e on p.patient_id = e.patient_id  "
            + "inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id  "
            + "inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id "
            + "where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0 and estadoProfilaxia.voided = 0   "
            + "and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 656 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256  "
            + "and e.encounter_type in (6,9,53) and e.location_id=:location "
            + "and DATE(estadoProfilaxia.obs_datetime) BETWEEN :startInclusionDate AND :endInclusionDate  "
            + "group by p.patient_id "
            + ") tpt";

    public static final String findPatientWhoCompleteTPIINHCategory7 =
        "select finalTPI.patient_id from ( "
            + "SELECT B4_1_2.patient_id, MAX(dataInicioTPI) dataInicioTPI,obsFimTPI.obs_datetime  FROM (   "
            + "select p.patient_id, MAX(estadoProfilaxia.obs_datetime) dataInicioTPI   from patient p   "
            + "inner join encounter e on p.patient_id = e.patient_id   "
            + "inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id   "
            + "inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id  "
            + "where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0 and estadoProfilaxia.voided = 0    "
            + "and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 656 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256   "
            + "and e.encounter_type in (6,9,53) and e.location_id=:location  "
            + "and DATE(estadoProfilaxia.obs_datetime) BETWEEN :startInclusionDate AND :endInclusionDate "
            + "group by p.patient_id  "
            + ") B4_1_2   "
            + "inner join  (   "
            + "select p.patient_id, MAX(obsEstado.obs_datetime) obs_datetime  from patient p   "
            + "inner join encounter e on p.patient_id = e.patient_id   "
            + "inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id   "
            + "inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id  "
            + "where   e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=656   "
            + "and obsEstado.concept_id=165308 and obsEstado.value_coded=1267   "
            + "and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id =:location  "
            + "and obsEstado.obs_datetime BETWEEN :startInclusionDate AND :endRevisionDate "
            + "group by p.patient_id  "
            + ") obsFimTPI on obsFimTPI.patient_id = B4_1_2.patient_id   "
            + " WHERE obsFimTPI.obs_datetime between (B4_1_2.dataInicioTPI + INTERVAL 170 DAY) and (B4_1_2.dataInicioTPI + INTERVAL 297 DAY) "
            + "group by B4_1_2.patient_id "
            + ")finalTPI ";

    public static final String findPatientWhoStartTPI3HPDuringPeriodCategory7 =
        " SELECT TPI.patient_id FROM "
            + " (  "
            + "select p.patient_id, max(obsEstado.obs_datetime) data_inicio_3HP  from patient p  "
            + "inner join encounter e on p.patient_id = e.patient_id  "
            + "inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id  "
            + "inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id "
            + "where   e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954  "
            + "and obsEstado.concept_id=165308 and obsEstado.value_coded=1256  "
            + "and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and e.location_id=:location "
            + "and DATE(obsEstado.obs_datetime) BETWEEN :startInclusionDate AND :endInclusionDate  "
            + "group by p.patient_id "
            + ") TPI ";

    public static final String findPatientWhoCompleteTPI3HPCategory7 =
        "select DISTINCT B4_1_2.patient_id from ( "
            + "SELECT patient_id, MAX(dataInicioTPI) dataInicioTPI FROM (  "
            + "select p.patient_id, max(obsEstado.obs_datetime) dataInicioTPI  from patient p  "
            + "inner join encounter e on p.patient_id = e.patient_id  "
            + "inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id  "
            + "inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id  "
            + "where   e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954  "
            + "and obsEstado.concept_id=165308 and obsEstado.value_coded=1256  "
            + "and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id=:location "
            + "and DATE(obsEstado.obs_datetime) BETWEEN :startInclusionDate AND :endInclusionDate  "
            + "group by p.patient_id  "
            + ")maxdatainicio GROUP BY patient_id    "
            + ") B4_1_2   "
            + "inner join  (   "
            + "SELECT maxfimtpi.patient_id, MAX(maxfimtpi.obs_datetime) obs_datetime FROM (  "
            + "select p.patient_id,obsEstado.obs_datetime obs_datetime from patient p   "
            + "inner join encounter e on p.patient_id = e.patient_id   "
            + "inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id    "
            + "inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id  "
            + "where   e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954   "
            + "and obsEstado.concept_id=165308 and obsEstado.value_coded=1267   "
            + "and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id =:location "
            + "and DATE(obsEstado.obs_datetime) BETWEEN :startInclusionDate AND :endRevisionDate  "
            + "group by p.patient_id  "
            + ") maxfimtpi   "
            + ") obsFimTPI on obsFimTPI.patient_id = B4_1_2.patient_id   "
            + " WHERE obsFimTPI.obs_datetime between (B4_1_2.dataInicioTPI + INTERVAL 80 DAY) and (B4_1_2.dataInicioTPI + INTERVAL 190 DAY) "
            + " group by B4_1_2.patient_id ";

    public static final String findPatientWhoStartTPI4MonthsAfterDateOfInclusionCategory7 =
        "select p.patient_id  from patient p  "
            + "inner join encounter e on p.patient_id = e.patient_id  "
            + "inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id  "
            + "inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id "
            + "where   e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954  "
            + "and obsEstado.concept_id=165308 and obsEstado.value_coded=1256  "
            + "and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id=:location "
            + "and DATE(obsEstado.obs_datetime) between (:startInclusionDate + INTERVAL 4 MONTH)  and :endRevisionDate  ";
    public static final String
        findPatientsOnARTWithMinimum3APSSFollowupConsultationsIntheFirst3MonthsAfterStartingARTCategory11Numerator =
            " SELECT adult.patient_id FROM ( "
                + " SELECT second_consultation.patient_id, MIN(terceira_consulta.encounter_datetime) data_terceira_consulta "
                + " FROM "
                + " ( "
                + " SELECT first_consultetion.patient_id, MIN(segunda_consulta.encounter_datetime) data_segunda_consulta "
                + " FROM "
                + " ( "
                + " Select first_cons.patient_id, first_cons.data_primeira_consulta from ( "
                + " SELECT art_start.patient_id, MIN(primeira_consulta.encounter_datetime) data_primeira_consulta, art_start.art_start_date "
                + " FROM "
                + " ( "
                + " SELECT p.patient_id, MIN(value_datetime) art_start_date FROM patient p "
                + " INNER JOIN encounter e ON p.patient_id = e.patient_id "
                + " INNER JOIN obs o ON e.encounter_id = o.encounter_id "
                + " WHERE p.voided = 0 AND e.voided = 0 AND o.voided = 0 AND e.encounter_type = 53 "
                + " AND o.concept_id = 1190 AND o.value_datetime is NOT NULL AND o.value_datetime <= :endInclusionDate AND e.location_id = :location "
                + " GROUP BY p.patient_id  "
                + " ) art_start "
                + " INNER JOIN ( "
                + " SELECT e.patient_id, e.encounter_datetime "
                + " FROM encounter e "
                + " WHERE e.voided = 0 AND encounter_type = 35 AND e.location_id = :location "
                + " ) primeira_consulta ON primeira_consulta.patient_id =  art_start.patient_id AND primeira_consulta.encounter_datetime > art_start.art_start_date "
                + " WHERE art_start.art_start_date BETWEEN :startInclusionDate  AND :endInclusionDate "
                + " GROUP  by art_start.patient_id  "
                + " ) first_cons where (TIMESTAMPDIFF(DAY, first_cons.art_start_date, first_cons.data_primeira_consulta)) BETWEEN 20 AND 33 "
                + " ) first_consultetion "
                + " INNER JOIN "
                + " ( "
                + " SELECT e.patient_id, e.encounter_datetime "
                + " FROM encounter e "
                + " WHERE e.voided = 0 AND encounter_type = 35 AND e.location_id = :location "
                + " ) segunda_consulta ON segunda_consulta.patient_id = first_consultetion.patient_id AND "
                + " (TIMESTAMPDIFF(DAY, first_consultetion.data_primeira_consulta, segunda_consulta.encounter_datetime)) BETWEEN 20 AND 33 "
                + " GROUP BY first_consultetion.patient_id  "
                + " ) second_consultation "
                + " INNER JOIN "
                + " ( "
                + " SELECT e.patient_id, e.encounter_datetime "
                + " FROM encounter e "
                + " WHERE e.voided = 0 AND encounter_type = 35 AND e.location_id = :location "
                + " ) terceira_consulta ON terceira_consulta.patient_id = second_consultation.patient_id AND "
                + " (TIMESTAMPDIFF(DAY, second_consultation.data_segunda_consulta, terceira_consulta.encounter_datetime)) BETWEEN 20 AND 33 "
                + " GROUP BY second_consultation.patient_id "
                + " ) adult ";

    public static final String findPatientWithCVOver1000CopiesCategory11B2 =
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

    public static final String findPatientWithCVOver1000CopiesAndPregnatCategory11B4 =
        "select carga_viral.patient_id from (  "
            + "Select p.patient_id, min(o.obs_datetime) data_carga from patient p  "
            + "inner join encounter e on p.patient_id = e.patient_id  "
            + "inner join obs o on e.encounter_id=o.encounter_id  "
            + "where p.voided = 0 and e.voided = 0 and o.voided = 0 and e.encounter_type = 6 and  o.concept_id = 856 and  "
            + "o.obs_datetime between :startInclusionDate and :endInclusionDate and e.location_id = :location and o.value_numeric >= 1000  "
            + "group by p.patient_id  "
            + ") carga_viral  "
            + "inner join  obs obsGestante on obsGestante.person_id=carga_viral.patient_id  "
            + "and obsGestante.concept_id=1982  "
            + "and obsGestante.value_coded=1065  "
            + "and obsGestante.obs_datetime=carga_viral.data_carga  "
            + "and obsGestante.voided=0 ";

    public static final String findPatientWithCVOver1000CopiesAndBreastfeedingCategory11B5 =
        "select carga_viral.patient_id from (  "
            + "Select p.patient_id, min(o.obs_datetime) data_carga from patient p  "
            + "inner join encounter e on p.patient_id = e.patient_id  "
            + "inner join obs o on e.encounter_id=o.encounter_id  "
            + "where p.voided = 0 and e.voided = 0 and o.voided = 0 and e.encounter_type = 6 and  o.concept_id = 856 and  "
            + "o.obs_datetime between :startInclusionDate and :endInclusionDate and e.location_id = :location and o.value_numeric >= 1000  "
            + "group by p.patient_id  "
            + ") carga_viral  "
            + "inner join  obs obsGestante on obsGestante.person_id=carga_viral.patient_id  "
            + "and obsGestante.concept_id=6332  "
            + "and obsGestante.value_coded=1065  "
            + "and obsGestante.obs_datetime=carga_viral.data_carga  "
            + "and obsGestante.voided=0 ";

    public static final String findPatientsWhoHaveLastFirstLineTerapeutic =
        " select distinct firstLine.patient_id from ( "
            + " select maxLinha.patient_id, maxLinha.maxDataLinha from ( "
            + " select p.patient_id,max(o.obs_datetime) maxDataLinha from patient p "
            + " join encounter e on p.patient_id=e.patient_id "
            + " join obs o on o.encounter_id=e.encounter_id "
            + " where e.encounter_type=6 and e.voided=0 and o.voided=0 and p.voided=0 "
            + " and o.concept_id=21151 and e.location_id=:location "
            + " and o.obs_datetime between :startInclusionDate and :endInclusionDate "
            + " group by p.patient_id "
            + " ) maxLinha "
            + " inner join encounter e on e.patient_id = maxLinha.patient_id "
            + " inner join obs on obs.person_id=maxLinha.patient_id and maxLinha.maxDataLinha=obs.obs_datetime "
            + " where obs.concept_id=21151 and obs.value_coded=21150 and obs.voided=0 and obs.location_id=:location "
            + " and e.voided = 0 and e.encounter_type = 6 and e.location_id = :location "
            + ") firstLine ";

    public static final String
        findPatientsOnThe1stLineOfRTWithCVOver1000CopiesWhoHad3ConsecutiveMonthlyAPSSConsultationsCategory11Numerator =
            "select carga_viral.patient_id from ( "
                + "Select p.patient_id, min(o.obs_datetime) data_carga from patient p "
                + "inner join encounter e on p.patient_id = e.patient_id "
                + "inner join obs o on e.encounter_id=o.encounter_id "
                + "where p.voided = 0 and e.voided = 0 and o.voided = 0 and e.encounter_type = 6 and  o.concept_id = 856 and "
                + "o.obs_datetime between :startInclusionDate and :endInclusionDate and e.location_id = :location and o.value_numeric >= 1000 "
                + "group by p.patient_id "
                + ") carga_viral "
                + "inner join encounter primeira_consulta on carga_viral.patient_id = primeira_consulta.patient_id and primeira_consulta.voided = 0 and primeira_consulta.encounter_type = 35 and "
                + "primeira_consulta.encounter_datetime=carga_viral.data_carga "
                + "inner join encounter segunda_consulta on carga_viral.patient_id = segunda_consulta.patient_id and segunda_consulta.voided = 0 and segunda_consulta.encounter_type = 35 "
                + "and (TIMESTAMPDIFF(DAY, carga_viral.data_carga, segunda_consulta.encounter_datetime)) between 20 and 33 "
                + "inner join encounter terceira_consulta on carga_viral.patient_id = terceira_consulta.patient_id and terceira_consulta.voided = 0 and terceira_consulta.encounter_type = 35 "
                + "and (TIMESTAMPDIFF(DAY, segunda_consulta.encounter_datetime, terceira_consulta.encounter_datetime)) between 20 and 33 ";

    public static final String
        findPatientsOnThe1stLineOfRTWithCVOver50CopiesWhoHad3ConsecutiveMonthlyAPSSConsultationsCategory11Numerator =
            "select carga_viral.patient_id from ( "
                + "Select p.patient_id, min(o.obs_datetime) data_carga from patient p "
                + "inner join encounter e on p.patient_id = e.patient_id "
                + "inner join obs o on e.encounter_id=o.encounter_id "
                + "where p.voided = 0 and e.voided = 0 and o.voided = 0 and e.encounter_type = 6 and  o.concept_id = 856 and "
                + "o.obs_datetime between :startInclusionDate and :endInclusionDate and e.location_id = :location and o.value_numeric > 50 "
                + "group by p.patient_id "
                + ") carga_viral "
                + "inner join encounter primeira_consulta on carga_viral.patient_id = primeira_consulta.patient_id and primeira_consulta.voided = 0 and primeira_consulta.encounter_type = 35 and "
                + "primeira_consulta.encounter_datetime=carga_viral.data_carga "
                + "inner join encounter segunda_consulta on carga_viral.patient_id = segunda_consulta.patient_id and segunda_consulta.voided = 0 and segunda_consulta.encounter_type = 35 "
                + "and (TIMESTAMPDIFF(DAY, carga_viral.data_carga, segunda_consulta.encounter_datetime)) between 20 and 33 "
                + "inner join encounter terceira_consulta on carga_viral.patient_id = terceira_consulta.patient_id and terceira_consulta.voided = 0 and terceira_consulta.encounter_type = 35 "
                + "and (TIMESTAMPDIFF(DAY, segunda_consulta.encounter_datetime, terceira_consulta.encounter_datetime)) between 20 and 33 ";

    public static final String
        findFirstPatientChildrenAPSSConsultationWithinInclusionReportingPeriod =
            "select min_consultation.patient_id "
                + "from (                                                                                                 		"
                + "	select patient_id, min(art_start_date) art_start_date                                                 		"
                + "	from (                                                                                                		"
                + "		select p.patient_id, min(value_datetime) art_start_date                                            		"
                + " 		from patient p                                                                                 		"
                + " 			join encounter e on p.patient_id=e.patient_id                                              		"
                + " 			join obs o on e.encounter_id=o.encounter_id                                                		"
                + " 		where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53                         		"
                + " 			and o.concept_id=1190 and o.value_datetime is not null                                     		"
                + "			and o.value_datetime<=:endInclusionDate and e.location_id=:location                        			"
                + " 		group by p.patient_id                                                                          		"
                + " 	) art_start                                                                                       		"
                + " 	group by patient_id) tx_new                                                                       		"
                + " 	join                                                                                              		"
                + " 	(select p.patient_id, min(e.encounter_datetime) min_consultation_date                                   "
                + " 	from patient p                                                                                   		"
                + "	 	join encounter e on e.patient_id = p.patient_id                             							"
                + "	where p.voided=0 and e.voided=0 and e.encounter_type = 35         								  			"
                + "	 	and e.encounter_datetime between :startInclusionDate and :endInclusionDate and e.location_id=:location 	"
                + "	group by p.patient_id                                                            				  			"
                + "	) min_consultation on min_consultation.patient_id = tx_new.patient_id	                          			"
                + "	join person pe on pe.person_id= tx_new.patient_id                                                 			"
                + "where  tx_new.art_start_date between :startInclusionDate and :endInclusionDate and tx_new.art_start_date < min_consultation.min_consultation_date";

    public static final String
        findPatientsWhithCD4RegistredInClinicalConsultationUnder33DaysFromTheFirstClinicalConsultation =
            "select firstClinica.patient_id  from  ( "
                + "select p.patient_id,min(e.encounter_datetime) encounter_datetime  from  patient p "
                + "inner join encounter e on e.patient_id=p.patient_id "
                + "where p.voided=0 and e.voided=0 and e.encounter_datetime <= :endRevisionDate and "
                + "e.location_id=:location and e.encounter_type=6 "
                + "group by p.patient_id "
                + ") firstClinica "
                + "inner join obs obsCD4 on obsCD4.person_id=firstClinica.patient_id "
                + "where obsCD4.obs_datetime > firstClinica.encounter_datetime and obsCD4.obs_datetime <=  DATE_ADD(firstClinica.encounter_datetime, INTERVAL 33 DAY) and obsCD4.concept_id=1695 and obsCD4.value_numeric is not null and obsCD4.voided=0 "
                + "and obsCD4.location_id = :location ";

    public static final String findPatientsAgeRange =
        "SELECT patient_id FROM patient "
            + "INNER JOIN person ON patient_id = person_id WHERE patient.voided=0 AND person.voided=0 "
            + "AND TIMESTAMPDIFF(year,birthdate,:endInclusionDate) BETWEEN %d AND %d AND birthdate IS NOT NULL";

    public static final String findPatientsAgeRangeEndRevisionDate =
        "SELECT patient_id FROM patient "
            + "INNER JOIN person ON patient_id = person_id WHERE patient.voided=0 AND person.voided=0 "
            + "AND TIMESTAMPDIFF(year,birthdate,:endRevisionDate) BETWEEN %d AND %d AND birthdate IS NOT NULL";

    public static final String findPatientsBiggerThanRevisionDate =
        "SELECT patient_id FROM patient "
            + "INNER JOIN person ON patient_id = person_id WHERE patient.voided=0 AND person.voided=0 "
            + "AND TIMESTAMPDIFF(year,birthdate,:endRevisionDate) >= %d AND birthdate IS NOT NULL";

    public static final String findPatientsLessThanRevisionDate =
        "SELECT patient_id FROM patient "
            + "INNER JOIN person ON patient_id = person_id WHERE patient.voided=0 AND person.voided=0 "
            + "AND TIMESTAMPDIFF(year,birthdate,:endRevisionDate) <  %d AND birthdate IS NOT NULL";

    public static final String findPatientsBiggerThan =
        "SELECT patient_id FROM patient "
            + "INNER JOIN person ON patient_id = person_id WHERE patient.voided=0 AND person.voided=0 "
            + "AND TIMESTAMPDIFF(year,birthdate,:endInclusionDate) >= %d AND birthdate IS NOT NULL";

    public static final String findPatientsLessThan =
        "SELECT patient_id FROM patient "
            + "INNER JOIN person ON patient_id = person_id WHERE patient.voided=0 AND person.voided=0 "
            + "AND TIMESTAMPDIFF(year,birthdate,:endInclusionDate) <  %d AND birthdate IS NOT NULL";

    public static final String
        findAllPatientsWhoHaveTherapheuticLineSecondLineDuringInclusionPeriodCategory13P3B2NEWDenominatorBiggerThan =
            " select alternativa.patient_id "
                + " from "
                + " ( "
                + " Select p.patient_id, max(obsLinha.obs_datetime) data_linha "
                + " from patient p "
                + " inner join encounter e on p.patient_id = e.patient_id "
                + " inner join obs obsLinha on obsLinha.encounter_id = e.encounter_id "
                + " left join obs obsJustificacao on obsJustificacao.encounter_id = e.encounter_id and obsJustificacao.voided = 0 and "
                + " obsJustificacao.concept_id = 1792 "
                + " where p.voided = 0 and e.voided = 0 and e.encounter_type = 53 and obsLinha.concept_id = 21187 and obsLinha.voided = 0 and "
                + " obsLinha.obs_datetime BETWEEN :startInclusionDate and :endInclusionDate and e.location_id = :location and "
                + " (obsJustificacao.value_coded is null or (obsJustificacao.value_coded is not null and obsJustificacao.value_coded <> 1982)) "
                + " group by p.patient_id "
                + " ) alternativa "
                + "INNER JOIN person ON alternativa.patient_id = person.person_id WHERE person.voided=0 "
                + "AND TIMESTAMPDIFF(year,birthdate,alternativa.data_linha) >= %d AND birthdate IS NOT NULL";

    public static final String
        findAllPatientsWhoHaveTherapheuticLineSecondLineDuringInclusionPeriodCategory13P3B2NEWDenominatorLessThan =
            " select alternativa.patient_id "
                + " from "
                + " ( "
                + " Select p.patient_id, max(obsLinha.obs_datetime) data_linha "
                + " from patient p "
                + " inner join encounter e on p.patient_id = e.patient_id "
                + " inner join obs obsLinha on obsLinha.encounter_id = e.encounter_id "
                + " left join obs obsJustificacao on obsJustificacao.encounter_id = e.encounter_id and obsJustificacao.voided = 0 and "
                + " obsJustificacao.concept_id = 1792 "
                + " where p.voided = 0 and e.voided = 0 and e.encounter_type = 53 and obsLinha.concept_id = 21187 and obsLinha.voided = 0 and "
                + " obsLinha.obs_datetime BETWEEN :startInclusionDate and :endInclusionDate and e.location_id = :location and "
                + " (obsJustificacao.value_coded is null or (obsJustificacao.value_coded is not null and obsJustificacao.value_coded <> 1982)) "
                + " group by p.patient_id "
                + " ) alternativa "
                + "INNER JOIN person ON alternativa.patient_id = person.person_id WHERE person.voided=0 "
                + "AND TIMESTAMPDIFF(year,birthdate,alternativa.data_linha) < %d AND birthdate IS NOT NULL";

    public static final String
        findAllPatientsWhoHaveTherapheuticLineSecondLineDuringInclusionPeriodCategory13P3B2NEWDenominatorAgeRange =
            " select alternativa.patient_id "
                + " from "
                + " ( "
                + " Select p.patient_id, max(obsLinha.obs_datetime) data_linha "
                + " from patient p "
                + " inner join encounter e on p.patient_id = e.patient_id "
                + " inner join obs obsLinha on obsLinha.encounter_id = e.encounter_id "
                + " left join obs obsJustificacao on obsJustificacao.encounter_id = e.encounter_id and obsJustificacao.voided = 0 and "
                + " obsJustificacao.concept_id = 1792 "
                + " where p.voided = 0 and e.voided = 0 and e.encounter_type = 53 and obsLinha.concept_id = 21187 and obsLinha.voided = 0 and "
                + " obsLinha.obs_datetime BETWEEN :startInclusionDate and :endInclusionDate and e.location_id = :location and "
                + " (obsJustificacao.value_coded is null or (obsJustificacao.value_coded is not null and obsJustificacao.value_coded <> 1982)) "
                + " group by p.patient_id "
                + " ) alternativa "
                + "INNER JOIN person ON alternativa.patient_id = person.person_id WHERE person.voided=0 "
                + "AND TIMESTAMPDIFF(year,birthdate,alternativa.data_linha) BETWEEN %d and %s AND birthdate IS NOT NULL";

    public static final String findPatientWhoAreTBActiveASixMonthfterLastConsultationRF13_1 =
        "SELECT TPI3HP.patient_id FROM  (  "
            + "select p.patient_id, max(obsEstado.obs_datetime) data_inicio_3HP  from patient p  "
            + "inner join encounter e on p.patient_id = e.patient_id  "
            + "inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id  "
            + "inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id "
            + "where   e.encounter_type in(6,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954  "
            + "and obsEstado.concept_id=165308 and obsEstado.value_coded=1256  "
            + "and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and e.location_id=:location "
            + "and DATE(obsEstado.obs_datetime) BETWEEN :startInclusionDate AND :endInclusionDate  "
            + "group by p.patient_id "
            + ") TPI3HP  "
            + "INNER JOIN encounter e ON e.patient_id = TPI3HP.patient_id  "
            + "INNER JOIN obs obsTBActiva ON obsTBActiva.encounter_id = e.encounter_id  "
            + "WHERE e.voided = 0 AND e.location_id = :location AND obsTBActiva.voided = 0 "
            + "AND  e.encounter_datetime BETWEEN TPI3HP.data_inicio_3HP AND (TPI3HP.data_inicio_3HP + INTERVAL 6 MONTH) "
            + "AND e.encounter_type = 6 AND obsTBActiva.concept_id = 23761 AND obsTBActiva.value_coded = 1065  ";

    public static final String findPatientWhoAreTBScreeningSixMonthfterLastConsultationRF13_1 =
        "SELECT TPI3HP.patient_id FROM  (  "
            + "select p.patient_id, max(obsEstado.obs_datetime) data_inicio_3HP  from patient p  "
            + "inner join encounter e on p.patient_id = e.patient_id  "
            + "inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id  "
            + "inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id "
            + "where   e.encounter_type in (6,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954  "
            + "and obsEstado.concept_id=165308 and obsEstado.value_coded=1256  "
            + "and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and e.location_id=:location "
            + "and DATE(obsEstado.obs_datetime) BETWEEN :startInclusionDate AND :endInclusionDate  "
            + "group by p.patient_id "
            + ") TPI3HP  "
            + " INNER JOIN encounter e ON e.patient_id = TPI3HP.patient_id "
            + " INNER JOIN obs obsTBPositivo ON obsTBPositivo.encounter_id = e.encounter_id "
            + " WHERE e.voided = 0 AND obsTBPositivo.voided = 0 "
            + " AND e.encounter_datetime BETWEEN TPI3HP.data_inicio_3HP AND (TPI3HP.data_inicio_3HP + INTERVAL 6 MONTH) "
            + " AND e.location_id = :location AND e.encounter_type = 6 AND obsTBPositivo.concept_id = 23758 AND obsTBPositivo.value_coded = 1065 ";

    public static final String findPatientWhoAreTBTretmantSixMonthfterLastConsultationRF13_1 =
        "SELECT TPI3HP.patient_id FROM  (  "
            + "select p.patient_id, max(obsEstado.obs_datetime) data_inicio_3HP  from patient p  "
            + "inner join encounter e on p.patient_id = e.patient_id  "
            + "inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id  "
            + "inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id "
            + "where   e.encounter_type in (6,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954  "
            + "and obsEstado.concept_id=165308 and obsEstado.value_coded=1256  "
            + "and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and e.location_id=:location "
            + "and DATE(obsEstado.obs_datetime) BETWEEN :startInclusionDate AND :endInclusionDate  "
            + "group by p.patient_id "
            + ") TPI3HP  "
            + " INNER JOIN encounter e ON e.patient_id = TPI3HP.patient_id "
            + " INNER JOIN obs obsTB ON obsTB.encounter_id = e.encounter_id "
            + " WHERE e.voided = 0 AND e.location_id = :location  AND obsTB.voided = 0 AND "
            + " obsTB.obs_datetime BETWEEN TPI3HP.data_inicio_3HP AND (TPI3HP.data_inicio_3HP + INTERVAL 6 MONTH) AND "
            + " e.encounter_type = 6 AND obsTB.concept_id = 1268 AND obsTB.value_coded IN (1256,1257,1267) ";

    public static final String
        findPatientsDiagnosedWithActiveTBDuring9MonthsAfterInitiatedTPICategory7 =
            "select inh.patient_id from ( "
                + "select p.patient_id, MAX(estadoProfilaxia.obs_datetime) dataInicioTPI "
                + "from patient p "
                + "inner join encounter e on p.patient_id = e.patient_id "
                + "inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id "
                + "inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id "
                + "where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0 and estadoProfilaxia.voided = 0 "
                + "and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 656 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256 "
                + "and e.encounter_type in (6,53) and e.location_id = :location "
                + "and DATE(estadoProfilaxia.obs_datetime) BETWEEN :startInclusionDate AND :endInclusionDate "
                + "group by p.patient_id "
                + ") inh "
                + "INNER JOIN encounter e ON e.patient_id = inh.patient_id "
                + "INNER JOIN obs obsTBActiva ON obsTBActiva.encounter_id = e.encounter_id "
                + "WHERE e.voided = 0 AND e.location_id = :location AND obsTBActiva.voided = 0 "
                + "AND  e.encounter_datetime BETWEEN inh.dataInicioTPI AND (inh.dataInicioTPI + INTERVAL 9 MONTH) "
                + "AND e.encounter_type = 6 AND obsTBActiva.concept_id = 23761 AND obsTBActiva.value_coded = 1065 ";

    public static final String
        findPatientsWithPositiveTBScreeningDuring9MonthsAfterInitiatedINHCategory7 =
            "select inh.patient_id from ( "
                + "select p.patient_id, MAX(estadoProfilaxia.obs_datetime) dataInicioTPI "
                + "from patient p "
                + "inner join encounter e on p.patient_id = e.patient_id "
                + "inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id "
                + "inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id "
                + "where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0 and estadoProfilaxia.voided = 0 "
                + "and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 656 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256 "
                + "and e.encounter_type in (6,53) and e.location_id= :location "
                + "and DATE(estadoProfilaxia.obs_datetime) BETWEEN :startInclusionDate AND :endInclusionDate "
                + "group by p.patient_id "
                + ") inh "
                + "INNER JOIN encounter e ON e.patient_id = inh.patient_id "
                + "INNER JOIN obs obsTBActiva ON obsTBActiva.encounter_id = e.encounter_id "
                + "WHERE e.voided = 0 AND e.location_id = :location AND obsTBActiva.voided = 0 "
                + "AND  e.encounter_datetime BETWEEN inh.dataInicioTPI AND (inh.dataInicioTPI + INTERVAL 9 MONTH) "
                + "AND e.encounter_type = 6 AND obsTBActiva.concept_id = 23758 AND obsTBActiva.value_coded = 1065 ";

    public static final String finPatientsWhoHadTBTreatmentDuring9MonthsAfterInitiatedINHCategory7 =
        "select inh.patient_id from ( "
            + "select p.patient_id, MAX(estadoProfilaxia.obs_datetime) dataInicioTPI "
            + "from patient p "
            + "inner join encounter e on p.patient_id = e.patient_id "
            + "inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id "
            + "inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id "
            + "where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0 and estadoProfilaxia.voided = 0 "
            + "and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 656 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256 "
            + "and e.encounter_type in (6,53) and e.location_id= :location "
            + "and DATE(estadoProfilaxia.obs_datetime) BETWEEN :startInclusionDate AND :endInclusionDate "
            + "group by p.patient_id "
            + ") inh "
            + "INNER JOIN encounter e ON e.patient_id = inh.patient_id "
            + "INNER JOIN obs obsTBActiva ON obsTBActiva.encounter_id = e.encounter_id "
            + "WHERE e.voided = 0 AND e.location_id = :location AND obsTBActiva.voided = 0 "
            + "AND  e.encounter_datetime BETWEEN inh.dataInicioTPI AND (inh.dataInicioTPI + INTERVAL 9 MONTH) "
            + "AND e.encounter_type = 6 AND obsTBActiva.concept_id=1268 and obsTBActiva.value_coded in (1256,1257,1267) ";

    public static final String
        finPatientsWhoCompletedINHBetween170And297DaysAfterInitiatedTreatment =
            "select finalTPI.patient_id from ( "
                + "SELECT B4_1_2.patient_id, MAX(dataInicioTPI) dataInicioTPI,obsFimTPI.obs_datetime  FROM ( "
                + "select p.patient_id, MAX(estadoProfilaxia.obs_datetime) dataInicioTPI   from patient p "
                + "inner join encounter e on p.patient_id = e.patient_id "
                + "inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id "
                + "inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id "
                + "where p.voided = 0 and e.voided = 0  and profilaxiaINH.voided = 0 and estadoProfilaxia.voided = 0 "
                + "and  profilaxiaINH.concept_id = 23985  and profilaxiaINH.value_coded = 656 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256 "
                + "and e.encounter_type in (6,9,53) and e.location_id=:location "
                + "and DATE(estadoProfilaxia.obs_datetime) BETWEEN  :startInclusionDate and :endInclusionDate "
                + "group by p.patient_id "
                + ") B4_1_2 "
                + "inner join  ( "
                + "select p.patient_id, max(obsEstado.obs_datetime) obs_datetime  from patient p "
                + "inner join encounter e on p.patient_id = e.patient_id "
                + "inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id "
                + "inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id "
                + "where   e.encounter_type in(6,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=656 "
                + "and obsEstado.concept_id=165308 and obsEstado.value_coded=1267 "
                + "and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and e.location_id=:location "
                + "and date(obsEstado.obs_datetime) BETWEEN :startInclusionDate and :endRevisionDate "
                + "group by p.patient_id  "
                + ") obsFimTPI on obsFimTPI.patient_id = B4_1_2.patient_id "
                + "WHERE obsFimTPI.obs_datetime between (B4_1_2.dataInicioTPI + INTERVAL 170 DAY) and (B4_1_2.dataInicioTPI + INTERVAL 297 DAY) "
                + "group by B4_1_2.patient_id "
                + ")finalTPI ";

    public static final String
        finPatientsWhoCompleted3HPBetween80And190DaysAfterInitiatedTreatment =
            "select DISTINCT B4_1_2.patient_id from ( "
                + "SELECT patient_id, MAX(dataInicioTPI) dataInicioTPI FROM ( "
                + "select p.patient_id, max(obsEstado.obs_datetime) dataInicioTPI  from patient p "
                + "inner join encounter e on p.patient_id = e.patient_id "
                + "inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id "
                + "inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id "
                + "where   e.encounter_type in(6,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954 "
                + "and obsEstado.concept_id=165308 and obsEstado.value_coded=1256 "
                + "and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id=:location "
                + "and DATE(obsEstado.obs_datetime) BETWEEN :startInclusionDate and :endInclusionDate "
                + "group by p.patient_id "
                + ")maxdatainicio GROUP BY patient_id "
                + ") B4_1_2 "
                + "inner join  ( "
                + "select p.patient_id,max(obsEstado.obs_datetime) obs_datetime from patient p "
                + "inner join encounter e on p.patient_id = e.patient_id "
                + "inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id "
                + "inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id "
                + "where e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954 "
                + "and obsEstado.concept_id=165308 and obsEstado.value_coded=1267 "
                + "and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and e.location_id =:location "
                + "and date(obsEstado.obs_datetime) BETWEEN :startInclusionDate and :endRevisionDate "
                + "group by p.patient_id "
                + ") obsFimTPI on obsFimTPI.patient_id = B4_1_2.patient_id "
                + "WHERE obsFimTPI.obs_datetime between (B4_1_2.dataInicioTPI + INTERVAL 80 DAY) and (B4_1_2.dataInicioTPI + INTERVAL 190 DAY) "
                + "group by B4_1_2.patient_id ";
  }
}
