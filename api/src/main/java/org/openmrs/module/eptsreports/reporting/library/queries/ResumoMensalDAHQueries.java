package org.openmrs.module.eptsreports.reporting.library.queries;

import java.util.Arrays;
import org.apache.commons.lang.StringUtils;

public class ResumoMensalDAHQueries {

  /**
   * Número total de activos em DAH em TARV, até ao fim do mês anterior
   *
   * @return String
   */
  public static String getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfPreviousMonthIndicator0() {

    String query =
        "     select patient_id from ( "
            + "	 	select activeInDAH.patient_id,saidaDAH.obs_datetime  from( "
            + " 	 	select p.patient_id,max(e.encounter_datetime) maxDataInicioDAH "
            + " 	from "
            + "  	patient p inner join encounter e on p.patient_id=e.patient_id "
            + " 	where p.voided=0 and e.voided=0 and e.encounter_type=90 and e.encounter_datetime <= :startDate "
            + "	and e.location_id=:location "
            + "	group by p.patient_id "
            + " 	) activeInDAH "
            + "	left join "
            + "             ( "
            + "		select p.patient_id, o.obs_datetime from patient p "
            + "           join encounter e on p.patient_id=e.patient_id "
            + "           join obs o on o.encounter_id=e.encounter_id "
            + "       where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 90 "
            + "           and o.concept_id=1708 and o.obs_datetime <= :startDate and o.value_coded in (1366,1707,1706) and e.location_id=:location "
            + "           union "
            + "            select p.patient_id, o.value_datetime from patient p "
            + "           join encounter e on p.patient_id=e.patient_id "
            + "           join obs o on o.encounter_id=e.encounter_id "
            + "       where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 90 "
            + "           and o.concept_id=165386  and o.value_datetime <= :startDate and e.location_id=:location "
            + "	)saidaDAH on saidaDAH.patient_id = activeInDAH.patient_id "
            + "	where ((saidaDAH.obs_datetime >= activeInDAH.maxDataInicioDAH and  saidaDAH.obs_datetime <= :startDate) or saidaDAH.patient_id is null) "
            + "	) final where final.obs_datetime is null ";

    return query;
  }

  public static String getNumberOfPatientsActiveInDAHWhoAreInTARVByEndOfMonthIndicator5() {

    String query =
        "     select patient_id from ( "
            + "	 	select activeInDAH.patient_id,saidaDAH.obs_datetime  from( "
            + " 	 	select p.patient_id,max(e.encounter_datetime) maxDataInicioDAH "
            + " 	from "
            + "  	patient p inner join encounter e on p.patient_id=e.patient_id "
            + " 	where p.voided=0 and e.voided=0 and e.encounter_type=90 and e.encounter_datetime <= :endDate "
            + "	and e.location_id=:location "
            + "	group by p.patient_id "
            + " 	) activeInDAH "
            + "	left join "
            + "             ( "
            + "		select p.patient_id, o.obs_datetime from patient p "
            + "           join encounter e on p.patient_id=e.patient_id "
            + "           join obs o on o.encounter_id=e.encounter_id "
            + "       where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 90 "
            + "           and o.concept_id=1708 and o.obs_datetime <= :endDate and o.value_coded in (1366,1707,1706) and e.location_id=:location "
            + "           union "
            + "            select p.patient_id, o.value_datetime from patient p "
            + "           join encounter e on p.patient_id=e.patient_id "
            + "           join obs o on o.encounter_id=e.encounter_id "
            + "       where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 90 "
            + "           and o.concept_id=165386  and o.value_datetime <= :endDate and e.location_id=:location "
            + "	)saidaDAH on saidaDAH.patient_id = activeInDAH.patient_id "
            + "	where ((saidaDAH.obs_datetime >= activeInDAH.maxDataInicioDAH and  saidaDAH.obs_datetime <= :endDate) or saidaDAH.patient_id is null) "
            + "	) final where final.obs_datetime is null ";

    return query;
  }

  /**
   * 6 - Com registo de “Data de Início no Modelo de DAH”, na Ficha de DAH, ocorrida na coorte de 6
   * meses
   *
   * @return String
   */
  public static String findPatientsWithDAHBeforeEndDate() {

    String query =
        " 	select p.patient_id "
            + "from "
            + "patient p inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type=90 and e.encounter_datetime <= :endDate "
            + "and e.location_id=:location ";
    return query;
  }

  /**
   * Com registo de “Data de Início no Modelo de DAH”, na Ficha de DAH, ocorrida durante o período
   *
   * @return String
   */
  public static String getNumberOfPatientsNewEnrolledInARTWhoInitiatedDAHDuringReportPeriod1() {

    String query =
        "	select patient_id from ( "
            + "	select p.patient_id, max( e.encounter_datetime) encounter_datetime "
            + " 	from "
            + "  	patient p inner join encounter e on p.patient_id=e.patient_id "
            + " 	where p.voided=0 and e.voided=0 and e.encounter_type=90 and e.encounter_datetime >= :startDate "
            + " 	and e.encounter_datetime <= :endDate and e.location_id = :location "
            + "	group by p.patient_id "
            + "	) fichaDah ";
    return query;
  }

  public enum ARTSituation {
    NEW_ENROLLED,

    RESTART,

    ACTIVE,

    PREGNANT,

    SEGUIMENTO_DAH,

    PRE_TARV,

    ALL
  }

  /**
   * Situação do TARV no início do seguimento
   *
   * @return String
   */
  public static String findPatientsARTSituation(ARTSituation artSituation) {

    String query =
        "select "
            + "patient_id "
            + "from "
            + "( "
            + "   select "
            + "   p.patient_id, "
            + "   max(e.encounter_datetime) "
            + "   from patient p "
            + "   join encounter e on p.patient_id=e.patient_id "
            + "   join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = 90 "
            + "   and o.concept_id=1255 "
            + "   and o.value_coded in ( %s ) "
            + "   and e.encounter_datetime <= :endDate "
            + "   and e.location_id=:location "
            + "   group by p.patient_id "
            + ") "
            + "fichaDah ";

    switch (artSituation) {
      case NEW_ENROLLED:
        query = String.format(query, 1256);
        break;

      case RESTART:
        query = String.format(query, 1705);
        break;

      case ACTIVE:
        query = String.format(query, 6276);
        break;

      case PRE_TARV:
        query = String.format(query, 6275);
        break;

      case ALL:
        query = String.format(query, StringUtils.join(Arrays.asList(1256, 1705, 6276, 6275), ","));
        break;
    }
    return query;
  }

  /**
   * Número de utentes em seguimento para Doença Avançada que saíram da abordagem durante o mês
   *
   * @return String
   */
  public static String getNumberOfPatientsWhoLeftDAHDuringReportPeriod4() {

    String query =
        "  select p.patient_id from patient p "
            + "      join encounter e on p.patient_id=e.patient_id "
            + "      join obs o on o.encounter_id=e.encounter_id "
            + "  where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 90 "
            + "      and o.concept_id=1708 and o.value_coded in (1366,1707,1706) "
            + "      and  o.obs_datetime >= :startDate and  o.obs_datetime <= :endDate and e.location_id=:location "
            + "      union "
            + "  select p.patient_id from patient p "
            + "      join encounter e on p.patient_id=e.patient_id "
            + "      join obs o on o.encounter_id=e.encounter_id "
            + "  where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 90 "
            + "      and o.concept_id=165386  and  o.value_datetime >= :startDate and  o.value_datetime <= :endDate and e.location_id=:location ";

    return query;
  }

  /**
   * 6 - Com registo de “Data de Início no Modelo de DAH”, na Ficha de DAH, ocorrida na coorte de 6
   * meses
   *
   * @return String
   */
  public static String getNumberOfPatientsWithDAHInSixMonthsCoorte() {

    String query =
        "select patient_id from ( "
            + " 	 	select p.patient_id, max(e.encounter_datetime) encounter_datetime "
            + " 	from "
            + "  	patient p inner join encounter e on p.patient_id=e.patient_id "
            + " 	where p.voided=0 and e.voided=0 and e.encounter_type=90 and e.encounter_datetime >= :startDate "
            + " 	AND e.encounter_datetime <= :endDate "
            + "	and e.location_id=:location "
            + "	group by p.patient_id "
            + "	) inDah ";

    return query;
  }

  /**
   * 6 - Número total de óbitos nos utentes com diagnóstico de Doença Avançada na coorte de 6 meses
   * após o início de seguimento
   *
   * @return String
   */
  public static String getNumberOfPatientsDiedWithDAHSixMonthsAfterDAH6() {

    String query =
        "		select dah.patient_id from ( "
            + " 	 	select p.patient_id,e.encounter_datetime "
            + " 	from "
            + "  	patient p inner join encounter e on p.patient_id=e.patient_id "
            + " 	where p.voided=0 and e.voided=0 and e.encounter_type=90 and e.encounter_datetime >= :startDate - interval 7 month "
            + " 	AND e.encounter_datetime <= :startDate -  interval 6 month - interval 1 day"
            + "	and e.location_id=:location "
            + "	) dah "
            + "	inner join ( "
            + "       select p.patient_id, o.obs_datetime from patient p "
            + "      join encounter e on p.patient_id=e.patient_id "
            + "      join obs o on o.encounter_id=e.encounter_id "
            + "  where e.voided=0 and p.voided=0 and e.encounter_type = 90 and "
            + "      o.voided = 0 and o.concept_id=1708 and o.obs_datetime <= :endDate and o.value_coded = 1366 and e.location_id=:location "
            + "	) saidaDAH on saidaDAH.patient_id = dah.patient_id "
            + "	where saidaDAH.obs_datetime >= dah.encounter_datetime and saidaDAH.obs_datetime <= :endDate ";

    return query;
  }

  /**
   * 8 - Número de utentes com pedido de CD4 de rastreio durante o mês
   *
   * @return String
   */
  public static String getNumberOfPatientsRequestingCD4ScreeningDuringThePeriod8() {

    String query =
        "	      Select p.patient_id from patient p "
            + "inner join encounter  e on e.patient_id=p.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where  e.voided=0 and o.concept_id = 23722 and o.value_coded=1695 and e.encounter_type=6 and o.voided=0 "
            + "and e.encounter_datetime >= :startDate and e.encounter_datetime <= :endDate "
            + "and e.location_id=:location ";

    return query;
  }

  /**
   * 9 -Número de utentes com resultado de CD4 de rastreio disponível durante o mês
   *
   * @return String
   */
  public static String getNumberOfPatientWithCD4ResultDuringThePeriod9() {

    String query =
        "	Select p.patient_id "
            + "	From patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	inner join obs o on e.encounter_id=o.encounter_id "
            + "	where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id = 1695 and  e.encounter_type in (90,6) and o.value_numeric is not null "
            + "	and e.location_id=:location and o.obs_datetime between :startDate and :endDate "
            + "	union "
            + "	Select p.patient_id "
            + "	From patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	inner join obs o on e.encounter_id=o.encounter_id "
            + "	where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id = 165515 and  e.encounter_type in (90,6) and o.value_coded is not null "
            + "	and e.location_id=:location and o.obs_datetime between :startDate and  :endDate ";

    return query;
  }

  /**
   * 10) Número de utentes com resultado de CD4 baixo* durante o mês
   *
   * @return String
   */
  public static String getNumberOfPatientWithLowCD4ResultDuringThePeriod10() {

    String query =
        "	select f.patient_id "
            + "from "
            + "( "
            + "select p.patient_id, o.obs_datetime,o.value_numeric, e.encounter_type,e.encounter_id,o.concept_id,o.value_coded "
            + "   from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "  where p.voided=0 and  e.voided=0 and e.encounter_type in (6,90) and o.concept_id in(1695,165515)  and o.voided=0 and (o.value_numeric is not null or o.value_coded is not null) "
            + "        and  e.location_id=:location and o.obs_datetime between :startDate and :endDate "
            + ")f "
            + "inner join person pe on pe.person_id=f.patient_id "
            + "WHERE       (TIMESTAMPDIFF(year,pe.birthdate,:endDate)>=5 and ((f.concept_id=1695 and f.value_numeric<200) OR (f.concept_id=165515 and f.value_coded=165513))) "
            + "        OR  ((TIMESTAMPDIFF(year,pe.birthdate,:endDate) between 1 and 4) and f.value_numeric<500 ) "
            + "        OR  (TIMESTAMPDIFF(year,pe.birthdate,:endDate)<1 and f.value_numeric<750 ) "
            + "GROUP BY f.patient_id ";

    return query;
  }

  public static final String findPatientsWhoHaveSpecificExamTestResults(TypesOfExams typeOfExam) {

    String query =
        "select p.patient_id "
            + "   from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "  where p.voided=0 and  e.voided=0 and e.encounter_type = 90 and o.concept_id=%s  and o.voided=0 and o.value_coded in (%s) "
            + "        and  e.location_id=:location and o.obs_datetime between :startDate and :endDate "
            + "        union "
            + "select p.patient_id "
            + "   from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "  where p.voided=0 and  e.voided=0 and e.encounter_type = 6 and o.concept_id=%s  and o.voided=0 and o.value_coded in (%s) "
            + "        and  e.location_id=:location and e.encounter_datetime between :startDate and :endDate ";

    switch (typeOfExam) {
      case TBLAM:
        query =
            String.format(
                query,
                23951,
                StringUtils.join(Arrays.asList(703, 664), ","),
                23951,
                StringUtils.join(Arrays.asList(703, 664), ","));
        break;

      case TBLAM_POS:
        query = String.format(query, 23951, 703, 23951, 703);
        break;

      case CRAG_SORO:
        query =
            String.format(
                query,
                23952,
                StringUtils.join(Arrays.asList(703, 664), ","),
                23952,
                StringUtils.join(Arrays.asList(703, 664), ","));
        break;

      case CRAG_SORO_POS:
        query = String.format(query, 23952, 703, 23952, 703);
        break;

      case CRAG_LCR:
        query =
            String.format(
                query,
                165362,
                StringUtils.join(Arrays.asList(703, 664), ","),
                165362,
                StringUtils.join(Arrays.asList(703, 664), ","));
        break;

      case CRAG_LCR_POS:
        query = String.format(query, 165362, 703, 165362, 703);
        break;
    }
    return query;
  }

  public enum MCCTreatment {
    MCC_PREVENTIVE_TREATMENT,

    MCC_TREATMENT,

    QUIMIOTHERAPHY_CICLE_ONE
  }

  public enum PeriodoAbandono {
    PRIMEIRO,

    SEGUNDO,

    TERCEIRO
  }

  /**
   * 16) Número de utentes com CrAg sérico positivo que iniciaram tratamento preventivo de MCC
   * durante o mês
   *
   * @return String
   */
  public static String findPatientsWhoInMCCTreatment(MCCTreatment treatment) {

    String query =
        "	select p.patient_id from patient p "
            + "	join encounter e on p.patient_id=e.patient_id "
            + "	join obs o on o.encounter_id=e.encounter_id "
            + "	where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 90 "
            + "	and o.concept_id=%s and "
            + "	o.value_datetime >= :startDate and o.value_datetime <= :endDate "
            + "	and e.location_id=:location ";

    switch (treatment) {
      case MCC_PREVENTIVE_TREATMENT:
        query = String.format(query, 165393);
        break;

      case MCC_TREATMENT:
        query = String.format(query, 165363);
        break;

      case QUIMIOTHERAPHY_CICLE_ONE:
        query = String.format(query, 165379);
        break;
    }
    return query;
  }

  /**
   * 18) Número de utentes com novo diagnóstico de sarcoma de kaposi e com indicação para
   * quimioterapia durante o mês
   *
   * @return String
   */
  public static String
      getNumberOfPatientsWithNewSKDiagnosticAndQuimiotheraphyIndicationDuringPeriod18() {

    String query =
        "	select p.patient_id from patient p "
            + "	join encounter e on p.patient_id=e.patient_id "
            + "	join obs o on o.encounter_id=e.encounter_id "
            + "     join obs oQuimio on oQuimio.encounter_id = e.encounter_id "
            + "	where e.voided=0 and o.voided=0 and oQuimio.voided = 0 and p.voided=0 and e.encounter_type = 90 "
            + "	and o.concept_id=1413 and oQuimio.concept_id = 20294 and oQuimio.value_coded = 1065 "
            + "	and o.value_datetime >= :startDate and o.value_datetime <= :endDate "
            + "	and e.location_id=:location ";

    return query;
  }

  /**
   * O sistema irá identificar mulheres grávidas HIV+, para desagregação dos indicadores 8 a 19
   *
   * @return String
   */
  public static String findPatientsMarkedAsPregnantInTheLast6MonthsBeforeEndDate() {

    String query =
        "Select p.patient_id from person pe "
            + "inner join patient p on pe.person_id=p.patient_id "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where pe.voided=0 and p.voided=0 and e.voided=0 and o.voided=0  and e.encounter_type=6 and e.location_id=:location and pe.gender='F' and "
            + "o.concept_id=1982 and o.value_coded=1065 and e.encounter_datetime "
            + "between :startDate and :endDate ";

    return query;
  }

  /**
   * Com registo de pelo menos um motivo (Óbito/ Abandono/ Transferido Para) e “Data de Saída de
   * TARV na US” (secção J), na Ficha de DAH, ocorrida durante o período (“Data de Saída de TARV na
   * US” >= “Data Início” e <= “Data Fim”)
   *
   * @return String
   */
  public static String findPatientsMarkedAsObitoOrAbandonoOrTransferredOutDuringPeriod() {

    String query =
        "          select p.patient_id from patient p "
            + "      join encounter e on p.patient_id=e.patient_id "
            + "      join obs o on o.encounter_id=e.encounter_id "
            + "  where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 90 "
            + "      and o.concept_id=1708 and o.obs_datetime >= :startDate and o.obs_datetime <= :endDate and o.value_coded in (1366,1707,1706) "
            + "      and e.location_id=:location ";

    return query;
  }

  /**
   * Com registo de pelo menos um motivo (Óbito/ Abandono/ Transferido Para) e “Data de Saída de
   * TARV na US” (secção J), na Ficha de DAH, ocorrida durante o período (“Data de Saída de TARV na
   * US” >= “Data Início” e <= “Data Fim”)
   *
   * @return String
   */
  public static String findPatientsWithDatadeSaidaDuringPeriod() {

    String query =
        "  select p.patient_id from patient p "
            + "      join encounter e on p.patient_id=e.patient_id "
            + "      join obs o on o.encounter_id=e.encounter_id "
            + "  where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 90 "
            + "      and o.concept_id=165386 and o.value_datetime >= :startDate and o.value_datetime <= :endDate and e.location_id=:location ";

    return query;
  }

  /**
   * RF30 O sistema irá identificar utentes em seguimento de DAH, para desagregação dos indicadores
   * 10 a 19
   *
   * @return String
   */
  public static String findPatientsInDAHExcludingPatientsWithSaidaDAH() {

    String query =
        "	select final.patient_id from ( "
            + "	select inicioDAH.patient_id, inicioDAH.dataInicioDAH, saidasDAH.dataSaidaDAH from ( "
            + "	select p.patient_id,max(e.encounter_datetime) dataInicioDAH "
            + " 	from "
            + "  	patient p inner join encounter e on p.patient_id=e.patient_id "
            + " 	where p.voided=0 and e.voided=0 and e.encounter_type=90 "
            + " 	and e.encounter_datetime <= :endDate "
            + "	and e.location_id=:location "
            + "	group by p.patient_id "
            + "	) inicioDAH left join "
            + "	( "
            + "  select p.patient_id, o.obs_datetime dataSaidaDAH from patient p "
            + "      join encounter e on p.patient_id=e.patient_id "
            + "      join obs o on o.encounter_id=e.encounter_id "
            + "  where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 90 "
            + "      and o.concept_id=1708 and o.obs_datetime <= :endDate and o.value_coded in (1366,1707,1706) "
            + "      and e.location_id=:location "
            + "      union "
            + "  select p.patient_id, o.value_datetime dataSaidaDAH from patient p "
            + "      join encounter e on p.patient_id=e.patient_id "
            + "      join obs o on o.encounter_id=e.encounter_id "
            + "  where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 90 "
            + "      and o.concept_id=165386 and  o.value_datetime <= :endDate and e.location_id=:location "
            + "      )saidasDAH on saidasDAH.patient_id = inicioDAH.patient_id "
            + "      and saidasDAH.dataSaidaDAH between inicioDAH.dataInicioDAH and :endDate "
            + "      ) final "
            + "        where final.dataSaidaDAH is null ";

    return query;
  }

  public static String getPatientsWhoAbandonedTratmentToBeExclude() {
    String query =
        "Select B7.patient_id from (select patient_id,max(data_levantamento) data_levantamento,max(data_proximo_levantamento) data_proximo_levantamento, date_add(max(data_proximo_levantamento), INTERVAL 60 day) data_proximo_levantamento60 "
            + "from( "
            + "select p.patient_id,max(o.value_datetime) data_levantamento, date_add(max(o.value_datetime), INTERVAL 30 day)  data_proximo_levantamento  "
            + "from patient p inner  "
            + "join encounter e on p.patient_id = e.patient_id  "
            + "inner join obs o on o.encounter_id = e.encounter_id  "
            + "inner join obs obsLevantou on obsLevantou.encounter_id= e.encounter_id  "
            + "where  e.voided = 0 and p.voided = 0 and o.value_datetime <= :endDate and o.voided = 0 and o.concept_id = 23866  "
            + "and obsLevantou.concept_id=23865  "
            + "and obsLevantou.value_coded=1065 and obsLevantou.voided=0 and e.encounter_type=52 and e.location_id=:location  "
            + "group by p.patient_id "
            + "union  "
            + "select fila.patient_id, fila.data_levantamento,proximoLevantamento.data_proximo_levantamento  from   "
            + "(  "
            + "select p.patient_id,max(e.encounter_datetime) as data_levantamento from patient p  "
            + "inner join encounter e on p.patient_id=e.patient_id where encounter_type=18 and e.encounter_datetime <=:endDate  "
            + "and e.location_id=:location and e.voided=0 and p.voided=0 "
            + "group by p.patient_id  "
            + ")fila  "
            + "left join "
            + "( "
            + "select fr.patient_id,fr.encounter_datetime data_levantamento,obs_fila.value_datetime data_proximo_levantamento,fr.encounter_id from ( "
            + "Select  p.patient_id,max(encounter_datetime) encounter_datetime, e.encounter_id from patient p   "
            + "inner join encounter e on e.patient_id=p.patient_id  "
            + "where   p.voided=0 and e.voided=0 and e.encounter_type=18 and   "
            + "e.location_id=:location and e.encounter_datetime<=:endDate  "
            + "group by p.patient_id  "
            + ") fr  "
            + "inner join encounter e on e.patient_id=fr.patient_id "
            + "inner join obs obs_fila on obs_fila.encounter_id=e.encounter_id   "
            + "and  obs_fila.concept_id=5096  "
            + "and obs_fila.voided=0 and  e.encounter_type =18 and e.encounter_datetime=fr.encounter_datetime "
            + ") proximoLevantamento on proximoLevantamento.patient_id=fila.patient_id  "
            + ") maxFilaRecepcao  "
            + "group by patient_id  "
            + "having date_add(max(data_proximo_levantamento), INTERVAL 59 day )< :endDate   "
            + ")B7 ";

    return query;
  }

  public static String getPatientsWhoSuspendTratmentLastMonth() {

    String query =
        "select suspenso1.patient_id from  "
            + "( "
            + "select patient_id,max(data_suspencao) data_suspencao from ( "
            + "select maxEstado.patient_id,maxEstado.data_suspencao from "
            + "( "
            + "select pg.patient_id,max(ps.start_date) data_suspencao from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "where pg.voided=0 and ps.voided=0 and p.voided=0 and "
            + "pg.program_id=2 and ps.start_date<=:endDate "
            + "and pg.location_id=:location "
            + "group by p.patient_id  "
            + ")maxEstado "
            + "inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id "
            + "inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id "
            + "where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and "
            + "ps2.start_date=maxEstado.data_suspencao and pg2.location_id=:location and ps2.state=8 "
            + "union "
            + "select p.patient_id,max(o.obs_datetime) data_suspencao from  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate "
            + "and o.voided=0 and o.concept_id=6272 "
            + "and o.value_coded=1709 and e.encounter_type=53 and  e.location_id=:location "
            + "group by p.patient_id "
            + "union "
            + "select  p.patient_id,max(e.encounter_datetime) data_suspencao from  patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where  e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate "
            + "and o.voided=0 and o.concept_id=6273 "
            + "and o.value_coded=1709 and e.encounter_type=6 and  e.location_id=:location "
            + "group by p.patient_id "
            + ") suspenso "
            + " group by patient_id "
            + ") suspenso1 "
            + "left join "
            + "( "
            + "select patient_id,max(encounter_datetime) encounter_datetime from ( "
            + "select p.patient_id,max(e.encounter_datetime) encounter_datetime from  patient p "
            + "inner join encounter e on e.patient_id=p.patient_id where p.voided=0 and e.voided=0 "
            + "and e.encounter_datetime<=:endDate and  "
            + "e.location_id=:location and e.encounter_type=18 "
            + "group by p.patient_id "
            + ") consultaLev "
            + "group by patient_id "
            + ") consultaOuARV on suspenso1.patient_id=consultaOuARV.patient_id "
            + "where (consultaOuARV.encounter_datetime<=suspenso1.data_suspencao OR consultaOuARV.encounter_datetime is null) ";

    return query;
  }

  public static String getPatientsWhoDiedTratmentLastMonth() {

    String query =
        "select obito.patient_id from ( "
            + "select patient_id,max(data_obito) data_obito from"
            + "( "
            + "select maxEstado.patient_id,maxEstado.data_obito from "
            + "( "
            + "select pg.patient_id,max(ps.start_date) data_obito from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "where pg.voided=0 and ps.voided=0 and p.voided=0 and "
            + "pg.program_id=2 and ps.start_date<=:endDate "
            + " and pg.location_id=:location "
            + "group by p.patient_id  "
            + ") maxEstado "
            + "inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id "
            + "inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id "
            + "where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and "
            + "ps2.start_date=maxEstado.data_obito and pg2.location_id=:location and ps2.state=10 "
            + "union "
            + "select p.patient_id,max(o.obs_datetime) data_obito from	patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id "
            + "where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate "
            + "and o.voided=0 and o.concept_id=6272 and o.value_coded=1366 and e.encounter_type=53 "
            + "and  e.location_id=:location "
            + "group by p.patient_id "
            + "union  "
            + "select p.patient_id,max(e.encounter_datetime) data_obito from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on o.encounter_id=e.encounter_id where e.voided=0 and p.voided=0 "
            + "and e.encounter_datetime<=:endDate "
            + "and o.voided=0 and o.concept_id=6273 and o.value_coded=1366 and e.encounter_type=6 "
            + "and  e.location_id=:location "
            + "group by p.patient_id "
            + "union  "
            + "Select person_id,death_date from person p where p.dead=1 "
            + "and p.death_date<=:endDate "
            + ")transferido "
            + "group by patient_id "
            + ") obito "
            + "inner join "
            + "( "
            + "select patient_id,max(encounter_datetime) encounter_datetime from"
            + "( "
            + "select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p "
            + "inner join encounter e on e.patient_id=p.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate "
            + "and e.location_id=:location and e.encounter_type in (18,6,9) "
            + "group by p.patient_id "
            + "union "
            + "select p.patient_id,max(value_datetime) encounter_datetime from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and "
            + "o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=:endDate "
            + "and e.location_id=:location "
            + "group by p.patient_id  "
            + ") consultaLev "
            + "group by patient_id "
            + ") consultaOuARV on obito.patient_id=consultaOuARV.patient_id "
            + "where consultaOuARV.encounter_datetime<=obito.data_obito "
            + "and obito.data_obito <=:endDate ";

    return query;
  }

  public static String getPatientsTransferredFromAnotherHealthFacilityLastMonth() {
    String query =
        "        select "
            + "            transferidopara.patient_id "
            + "            from "
            + "            ( "
            + "               select "
            + "               patient_id, "
            + "               max(data_transferidopara) data_transferidopara "
            + "               from "
            + "               ( "
            + "                  select "
            + "                  maxEstado.patient_id, "
            + "                  maxEstado.data_transferidopara "
            + "                  from "
            + "                  ( "
            + "                     select "
            + "                     pg.patient_id, "
            + "                     max(ps.start_date) data_transferidopara "
            + "                     from patient p "
            + "                     inner join patient_program pg on p.patient_id = pg.patient_id "
            + "                     inner join patient_state ps on pg.patient_program_id = ps.patient_program_id "
            + "                     where pg.voided = 0 "
            + "                     and ps.voided = 0 "
            + "                     and p.voided = 0 "
            + "                     and pg.program_id = 2 "
            + "                     and ps.start_date <= :endDate "
            + "                     and pg.location_id =:location "
            + "                     group by p.patient_id "
            + "                  ) "
            + "                  maxEstado "
            + "                  inner join patient_program pg2 on pg2.patient_id = maxEstado.patient_id "
            + "                  inner join patient_state ps2 on pg2.patient_program_id = ps2.patient_program_id "
            + "                  where pg2.voided = 0 "
            + "                  and ps2.voided = 0 "
            + "                  and pg2.program_id = 2 "
            + "                  and ps2.start_date = maxEstado.data_transferidopara "
            + "                  and pg2.location_id =:location "
            + "                  and ps2.state = 7 "
            + "                  union "
            + "			  select saida.patient_id, data_estado as data_transferidopara from "
            + "			( "
            + "			select p.patient_id, max(o.obs_datetime) data_estado from patient p "
            + "			inner join encounter e on p.patient_id=e.patient_id "
            + "			inner join obs  o on e.encounter_id=o.encounter_id "
            + "			where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 6 and "
            + "			o.concept_id = 6273 and e.location_id=:location and o.obs_datetime<=:endDate "
            + "			group by p.patient_id "
            + "			) saida "
            + "			inner join obs o on o.person_id = saida.patient_id "
            + "			where o.concept_id = 6273 and o.value_coded = 1706 "
            + "			and o.obs_datetime = saida.data_estado and o.voided = 0 "
            + "			group by saida.patient_id "
            + "			union "
            + "			select saida.patient_id, data_estado as data_transferidopara from "
            + "			( "
            + "			select p.patient_id, max(o.obs_datetime) data_estado from patient p "
            + "			inner join encounter e on p.patient_id=e.patient_id "
            + "			inner join obs  o on e.encounter_id=o.encounter_id "
            + "			where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 53 and "
            + "			o.concept_id = 6272 and e.location_id=:location and o.obs_datetime<=:endDate "
            + "			group by p.patient_id "
            + "			) saida "
            + "			inner join obs o on o.person_id = saida.patient_id "
            + "			where o.concept_id = 6272 and o.value_coded = 1706 "
            + "			and o.obs_datetime = saida.data_estado and o.voided = 0 "
            + "			group by saida.patient_id "
            + "               ) "
            + "               transferido "
            + "               group by patient_id "
            + "            ) "
            + "            transferidopara "
            + "            inner join "
            + "            ( "
            + "               select "
            + "               * "
            + "               from "
            + "               ( "
            + "                  select patient_id, max(data_ultimo_levantamento) data_ultimo_levantamento from "
            + "                  ( "
            + "                     select "
            + "                     ultimo_fila.patient_id, "
            + "                     date_add(obs_fila.value_datetime , interval 1 day ) data_ultimo_levantamento from "
            + "                     ( "
            + "                        select "
            + "                        p.patient_id, "
            + "                        max(encounter_datetime) data_fila "
            + "                        from patient p "
            + "                        inner join person pe on pe.person_id = p.patient_id "
            + "                        inner join encounter e on e.patient_id = p.patient_id "
            + "                        where p.voided = 0 "
            + "                        and pe.voided = 0 "
            + "                        and e.voided = 0 "
            + "                        and e.encounter_type = 18 "
            + "                        and e.location_id =:location "
            + "                        and e.encounter_datetime <= :endDate "
            + "                        group by p.patient_id "
            + "                     ) ultimo_fila "
            + "				 left join "
            + "				  encounter ultimo_fila_data_criacao on ultimo_fila_data_criacao.patient_id=ultimo_fila.patient_id "
            + "					and ultimo_fila_data_criacao.voided=0 "
            + "					and ultimo_fila_data_criacao.encounter_type = 18 "
            + "					and date(ultimo_fila_data_criacao.encounter_datetime) = date(ultimo_fila.data_fila) "
            + "					and ultimo_fila_data_criacao.location_id=:location "
            + "				  left join "
            + "				  obs obs_fila on obs_fila.person_id=ultimo_fila.patient_id "
            + "					and obs_fila.voided=0 "
            + "					and (date(obs_fila.obs_datetime)=date(ultimo_fila.data_fila)  or (date(ultimo_fila_data_criacao.date_created) = date(obs_fila.date_created) and ultimo_fila_data_criacao.encounter_id = obs_fila.encounter_id )) "
            + "					and obs_fila.concept_id=5096 "
            + "					and obs_fila.location_id=:location "
            + "                     union "
            + "                     select "
            + "                     p.patient_id, "
            + "                     date_add( max(value_datetime),interval 31 day ) data_ultimo_levantamento "
            + "                     from patient p "
            + "                     inner join person pe on pe.person_id = p.patient_id "
            + "                     inner join encounter e on p.patient_id = e.patient_id "
            + "                     inner join obs o on e.encounter_id = o.encounter_id "
            + "                     where p.voided = 0 "
            + "                     and pe.voided = 0 "
            + "                     and e.voided = 0 "
            + "                     and o.voided = 0 "
            + "                     and e.encounter_type = 52 "
            + "                     and o.concept_id = 23866 "
            + "                     and o.value_datetime is not null "
            + "                     and e.location_id =:location "
            + "                     and o.value_datetime <= :endDate "
            + "                     group by p.patient_id "
            + "                  ) "
            + "                  ultimo_levantamento "
            + "                  group by patient_id "
            + "               ) "
            + "               final "
            + "               where final.data_ultimo_levantamento<= :endDate "
            + "            ) "
            + "            TR_OUT on TR_OUT.patient_id = transferidopara.patient_id "
            + "            inner join "
            + "            ( "
            + "               select "
            + "               patient_id, "
            + "               max(encounter_datetime) encounter_datetime "
            + "               from "
            + "               ( "
            + "                  select "
            + "                  p.patient_id, "
            + "                  max(e.encounter_datetime) encounter_datetime "
            + "                  from patient p "
            + "                  inner join encounter e on e.patient_id = p.patient_id "
            + "                  where p.voided = 0 "
            + "                  and e.voided = 0 "
            + "                  and e.encounter_datetime <=:endDate "
            + "                  and e.location_id =:location "
            + "                  and e.encounter_type =18 "
            + "                  group by p.patient_id "
            + "               ) "
            + "               consultaLev "
            + "               group by patient_id "
            + "            ) "
            + "            consultaOuARV on transferidopara.patient_id = consultaOuARV.patient_id "
            + "            where "
            + "            consultaOuARV.encounter_datetime <= transferidopara.data_transferidopara "
            + "            and TR_OUT.data_ultimo_levantamento<=:endDate ";

    return query;
  }

  public static String getPatientsWhoHaveDrugPickup() {

    String query =
        "select pickup.patient_id from ( "
            + "Select p.patient_id,value_datetime data_recepcao_levantou from patient p  "
            + "inner join encounter e on p.patient_id=e.patient_id  "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and "
            + "o.concept_id=23866 and o.value_datetime is not null and  "
            + "o.value_datetime BETWEEN :startDate AND :endDate and e.location_id= :location  "
            + "union "
            + "Select p.patient_id,encounter_datetime data_fila from patient p  "
            + "inner join encounter e on e.patient_id=p.patient_id  "
            + "where p.voided=0 and e.voided=0 and e.encounter_type=18 and  "
            + "e.location_id= :location and e.encounter_datetime BETWEEN :startDate AND :endDate "
            + ")pickup ";

    return query;
  }

  public static String findPatientsWithFichaDAHWithoutSituacaoDOTARVMarked() {

    String query =
        "select inicio.patient_id from ( "
            + "	select p.patient_id,max(e.encounter_datetime) dataInicioDAH "
            + " 	from "
            + "  	patient p inner join encounter e on p.patient_id=e.patient_id "
            + " 	where p.voided=0 and e.voided=0 and e.encounter_type=90 "
            + " 	and e.encounter_datetime <= :endDate "
            + "	and e.location_id=:location "
            + "	group by p.patient_id "
            + "	) inicio left join "
            + "	( "
            + "   select "
            + "   p.patient_id, "
            + "   max(e.encounter_datetime) dataInicioDAH "
            + "   from patient p "
            + "   join encounter e on p.patient_id=e.patient_id "
            + "   join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = 90 "
            + "   and o.concept_id=1255 "
            + "   and o.value_coded in ( 1256, 1705, 6276, 6275 ) "
            + "   and e.encounter_datetime <= :endDate "
            + "   and e.location_id=:location "
            + "   group by p.patient_id "
            + "   ) situacaoTarv on situacaoTarv.patient_id = inicio.patient_id "
            + "   where situacaoTarv.patient_id is null ";

    return query;
  }
}
