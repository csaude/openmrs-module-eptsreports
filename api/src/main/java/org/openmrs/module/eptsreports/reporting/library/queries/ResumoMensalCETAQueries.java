/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.eptsreports.reporting.library.queries;

public class ResumoMensalCETAQueries {

  /**
   * Incluindo todos os utentes que foram rastreados usando a Ficha FICA-BEM durante o período de
   * reporte (RF7)
   *
   * @return String
   */
  public static String findPatientsWhoWereTrackedUsingTheFICABEMForm() {

    String query =
        "select "
            + "p.patient_id "
            + "from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where e.voided=0 "
            + "and p.voided=0 "
            + "and e.encounter_type=96 "
            + "and e.encounter_datetime >=:startDate "
            + "and e.encounter_datetime <=:endDate "
            + "and e.location_id=:location ";

    return query;
  }

  /**
   * Caso o utente não tenha Ficha Inicial CETA registada no sistema
   *
   * @return String
   */
  public static String findPatientsWhoHaveAtLeastOneFichaInicialCETA() {

    String query =
        "Select p.patient_id "
            + "From patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type = 97 and e.location_id=:location "
            + "and e.encounter_datetime <= :endDate ";

    return query;
  }

  /**
   * Excluindo todos os utentes com registo da Ficha FICA-BEM entre data início de reporte -12 meses
   * e data início de reporte para RF32, RF33, RF34, e RF35.
   *
   * @return String
   */
  public static String findPatientsWhithFicaBemInTheLast12Months() {

    String query =
        "select "
            + "p.patient_id "
            + "from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where e.voided=0 "
            + "and p.voided=0 "
            + "and e.encounter_type=96 "
            + "and e.encounter_datetime >=:startDate - interval 12 month "
            + "and e.encounter_datetime <=:startDate "
            + "and e.location_id=:location ";

    return query;
  }

  /**
   * RF28 - Outras Fontes: 2ª Consulta TARV O sistema irá identificar utentes com 2ª consulta de
   * TARV de sempre com base nos seguintes critérios
   *
   * @return String
   */
  public static String findPatientsWithSecondFichaClinicaDuringPeriod() {

    String query =
        "select firstConsultation.patient_id from ( "
            + "select "
            + "p.patient_id, e.encounter_datetime, count(e.encounter_id) nConsultas "
            + "from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where e.voided=0 "
            + "and p.voided=0 "
            + "and e.encounter_type=6 "
            + "and e.encounter_datetime < :startDate "
            + "and e.location_id=:location "
            + "group by p.patient_id "
            + "having nConsultas = 1 "
            + ") firstConsultation "
            + "inner join "
            + "( "
            + "select "
            + "p.patient_id, "
            + "e.encounter_datetime "
            + "from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where e.voided=0 "
            + "and p.voided=0 "
            + "and e.encounter_type=6 "
            + "and e.encounter_datetime >= :startDate "
            + "and e.encounter_datetime <= :endDate "
            + "and e.location_id=:location "
            + "group by p.patient_id "
            + ")secondConsultaion on secondConsultaion.patient_id = firstConsultation.patient_id "
            + "	union "
            + "select patient_id from ( "
            + "select "
            + "p.patient_id, e.encounter_datetime, count(e.encounter_id) nConsultas "
            + "from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where e.voided=0 "
            + "and p.voided=0 "
            + "and e.encounter_type=6 "
            + "and e.encounter_datetime between :startDate and :endDate "
            + "and e.location_id=:location "
            + "group by p.patient_id "
            + "having nConsultas >= 2 "
            + "and p.patient_id "
            + " not in ( "
            + "select "
            + "p.patient_id "
            + "from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where e.voided=0 "
            + "and p.voided=0 "
            + "and e.encounter_type=6 "
            + "and e.encounter_datetime < :startDate "
            + "and e.location_id=:location "
            + "group by p.patient_id "
            + ") "
            + ") final ";

    return query;
  }

  /**
   * RF32 - Utentes Transferidos de Outra US(para exclusão)
   *
   * @return String
   */
  public static String findPatientsWhoWereTransferredIn() {

    String query =
        "SELECT p.patient_id from patient p "
            + "INNER JOIN encounter e ON p.patient_id=e.patient_id "
            + "INNER JOIN obs obsTrans ON e.encounter_id=obsTrans.encounter_id AND obsTrans.voided=0 AND obsTrans.concept_id=1369 AND obsTrans.value_coded=1065 "
            + "WHERE p.voided=0 AND e.voided=0 AND e.encounter_type=53 AND  e.location_id=:location ";

    return query;
  }

  /**
   * RF29 - Identificação de utentes com última CV >1000 cps – Outras Fontes:
   *
   * @return String
   */
  public static String findPatientsWithCVGreaterThan1000Copies() {

    String query =
        "select maxCV.patient_id from ( "
            + "Select p.patient_id, max(o.obs_datetime) as obs_datetime "
            + "From patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 856 and  e.encounter_type in (6,13,53,51) and e.location_id=:location "
            + "and o.obs_datetime between :startDate and :endDate "
            + "group by p.patient_id "
            + ") maxCV "
            + "  inner join encounter e on e.patient_id = maxCV.patient_id "
            + "  inner join obs o on e.encounter_id = o.encounter_id "
            + "  where e.voided = 0 and o.voided = 0 and e.encounter_type in (6,13,53,51) and o.concept_id=856 and o.value_numeric>1000 "
            + "  and e.location_id =:location and date(o.obs_datetime) = date(maxCV.obs_datetime) "
            + "  group by e.patient_id ";

    return query;
  }

  /**
   * RF30 - Outras Fontes: Reintegrado / Ma Adesao
   *
   * @return String
   */
  public static String findPatientsWithReinicioFichaClinicaAndFichaResumo() {

    String query =
        "select reinicio.patient_id from "
            + "( "
            + "select p.patient_id, max(o.obs_datetime) data_estado from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs  o on e.encounter_id=o.encounter_id "
            + "where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 6 and "
            + "o.concept_id = 6273 and o.obs_datetime>=:startDate and o.obs_datetime<=:endDate and e.location_id=:location "
            + "group by p.patient_id "
            + "union "
            + "select p.patient_id, max(o.obs_datetime) data_estado from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs  o on e.encounter_id=o.encounter_id "
            + "where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 53 and "
            + "o.concept_id = 6272 and o.obs_datetime>=:startDate and o.obs_datetime<=:endDate and e.location_id=:location "
            + "group by p.patient_id "
            + ") reinicio "
            + "inner join obs o on o.person_id = reinicio.patient_id "
            + "where o.concept_id in (6272,6273) and o.value_coded = 1705 "
            + "and o.obs_datetime = reinicio.data_estado and o.voided = 0 "
            + "group by reinicio.patient_id ";

    return query;
  }

  /**
   * RF31 - Incluindo todos os utentes com pelo menos um dos seguintes critérios registados no campo
   * Factores Psicossocias numa Ficha APSS/PP dentro do período de reporte
   *
   * @return String
   */
  public static String findPatientsRegisteredInPsychosocialFactorsInAPSSForm() {

    String query =
        "select p.patient_id from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	inner join obs  o on e.encounter_id=o.encounter_id "
            + "	where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 35 and "
            + "	o.concept_id = 6193 and o.value_coded in (1956,6303,207,1603) "
            + "and e.encounter_datetime>=:startDate and e.encounter_datetime<=:endDate and e.location_id=:location ";

    return query;
  }

  /**
   * RF31 - Incluindo todos os utentes que se encontram inscritos no Programa TARV-Cuidados ou tem
   * Ficha Resumo
   *
   * @return String
   */
  public static String findPatientsRegisteredInTARVCuidadosOrHaveMasterCardFichaResumo() {

    String query =
        "select max_estado.patient_id "
            + "from( "
            + "		select pp.patient_id, max(max_estado.data_estado) data_estado,ps.patient_state_id,ps.state "
            + "		from( "
            + "			select pg.patient_id, ps.start_date data_estado,ps.patient_state_id ,ps.voided,ps.state "
            + "			from patient p "
            + "					inner join patient_program pg on p.patient_id = pg.patient_id "
            + "			  	inner join patient_state ps on pg.patient_program_id = ps.patient_program_id "
            + "			where pg.voided=0  and p.voided=0 and  pg.program_id = 1 "
            + "			and ps.voided=0 "
            + "				and ps.start_date  <= :endDate  and pg.location_id=3 and p.patient_id "
            + "				not in ( select pg.patient_id "
            + "			from patient p "
            + "					inner join patient_program pg on p.patient_id = pg.patient_id "
            + "			  	inner join patient_state ps on pg.patient_program_id = ps.patient_program_id "
            + "			where pg.voided=0  and p.voided=0 and  pg.program_id = 2 "
            + "			and ps.voided=0 "
            + "				and ps.start_date  <= :endDate  and pg.location_id=:location "
            + "				) "
            + "               order by pg.patient_id, ps.patient_state_id desc, ps.start_date desc "
            + "		)max_estado "
            + "			inner join patient_program pp on pp.patient_id = max_estado.patient_id "
            + "		 	inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado and max_estado.state=ps.state "
            + "  		where pp.program_id = 1 and pp.voided = 0 and ps.voided = 0 and pp.location_id=:location and pp.date_completed is null "
            + "		group by pp.patient_id "
            + ") max_estado "
            + "inner join patient_state ps on ps.patient_state_id =max_estado.patient_state_id "
            + "inner join patient_program pp on pp.patient_program_id = ps.patient_program_id "
            + "where ps.state in (1,28) "
            + "union "
            + "select p.patient_id from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	inner join obs  o on e.encounter_id=o.encounter_id "
            + "	where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 53 and "
            + "	o.concept_id = 23891 and o.value_datetime<=:endDate and e.location_id=:location ";

    return query;
  }

  /**
   * RF31 - Filtrando utentes que tem registado um FILA ou Ficha Recepcao Levantou ARVs até ao fim
   * do período de reporte
   *
   * @return String
   */
  public static String findPatientsWhoPickedUpARV() {

    String query =
        "select p.patient_id from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	where e.voided=0 and p.voided=0 and e.encounter_type in (18,52) "
            + "and e.encounter_datetime <= :endDate and e.location_id=:location ";

    return query;
  }

  /**
   * Indicador 2: Utentes com resultado Positivo no FICA-BEM durante o período de reporte
   *
   * @return String
   */
  public static String findPatientsWithPositiveResultInFICABEMForm() {

    String query =
        "select p.patient_id from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	inner join obs  o on e.encounter_id=o.encounter_id "
            + "	where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 96 and "
            + "	o.concept_id = 165464 and o.value_coded in (165465,165466) "
            + "and e.encounter_datetime>=:startDate and e.encounter_datetime<=:endDate and e.location_id=:location ";

    return query;
  }

  /**
   * RF9 - Indicador 3 – Nr de Pacientes referidos para o seguimento de Doença Mental Grave OU
   * Doença Mental Comum (psiquiatria/fluxo normal) DMG=165466 DMC=165465
   *
   * @return String
   */
  public static String findPatientsWithDMGOrDMCResultInFICABEMForm(int valueCoded) {

    String query =
        "select p.patient_id from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	inner join obs  o on e.encounter_id=o.encounter_id "
            + "	where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 96 and "
            + "	o.concept_id = 165464 and o.value_coded = %s "
            + "and e.encounter_datetime>=:startDate and e.encounter_datetime<=:endDate and e.location_id=:location ";

    return String.format(query, valueCoded);
  }

  /**
   * RF11 - Indicador 5 - Nr de Pacientes que iniciaram o tratamento de SM - CETA
   *
   * @return String
   */
  public static String findPatientsWhoInitiatedSMTreatment() {

    String query =
        "select p.patient_id from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	inner join obs  o on e.encounter_id=o.encounter_id "
            + "	where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 97 and "
            + "	o.concept_id = 165553 and o.value_coded is not null "
            + "and e.encounter_datetime>=:startDate and e.encounter_datetime<=:endDate and e.location_id=:location "
            + " "
            + "union "
            + " "
            + "select p.patient_id from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	where e.voided=0 and p.voided=0 and e.encounter_type = 98 "
            + "	and e.encounter_datetime >= :startDate "
            + "and e.encounter_datetime <= :endDate and e.location_id=:location "
            + "and p.patient_id not in ( "
            + "select p.patient_id from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	where e.voided=0 and p.voided=0 and e.encounter_type = 98 "
            + "	and e.encounter_datetime < :startDate and e.location_id=:location "
            + ") ";

    return query;
  }

  /**
   * RF12 - Indicador 6: Utentes em seguimento no CETA até ao final do mês
   *
   * @return String
   */
  public static String findPatientsInFollowUpCETAUntilThEndOfTheMonth() {

    String query =
        "select p.patient_id from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	where e.voided=0 and p.voided=0 and e.encounter_type = 98 "
            + "	and e.encounter_datetime >= :endDate - interval 59 day "
            + "and e.encounter_datetime <= :endDate and e.location_id=:location ";

    return query;
  }

  /**
   * RF13 - Indicador 7: Utentes com pelo menos uma Tentativa de Suicídio a entrada
   *
   * @return String
   */
  public static String findPatientsWithAtLeastOneSuicideAttempt() {

    String query =
        "select maxFI.patient_id from ( "
            + "Select p.patient_id, max(e.encounter_datetime) as encounter_datetime "
            + "From patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type = 96 and e.location_id=:location "
            + "and e.encounter_datetime <= :endDate "
            + "group by p.patient_id "
            + ") maxFI "
            + "  inner join encounter e on e.patient_id = maxFI.patient_id "
            + "  inner join obs o on e.encounter_id = o.encounter_id "
            + "  where e.voided = 0 and o.voided = 0 and e.encounter_type = 96 and o.concept_id=165460 and o.value_coded=703 "
            + "  and e.location_id =:location and e.encounter_datetime = maxFI.encounter_datetime "
            + "  group by e.patient_id "
            + " "
            + "  union "
            + " "
            + "select maxFI.patient_id from ( "
            + "Select p.patient_id, max(e.encounter_datetime) as encounter_datetime "
            + "From patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type = 97 and e.location_id=:location "
            + "and e.encounter_datetime <= :endDate "
            + "group by p.patient_id "
            + ") maxFI "
            + "  inner join encounter e on e.patient_id = maxFI.patient_id "
            + "  inner join obs o on e.encounter_id = o.encounter_id "
            + "  where e.voided = 0 and o.voided = 0 and e.encounter_type = 97 and o.concept_id=165610 and o.value_coded in (1065,165551) "
            + "  and e.location_id =:location and e.encounter_datetime = maxFI.encounter_datetime "
            + "  group by e.patient_id ";

    return query;
  }

  /**
   * RF13 - Indicador 8- Nr de Pacientes com Ideação de Suicídio na entrada
   *
   * @return String
   */
  public static String findPatientsWithAtLeastOneSuicideIdeation() {

    String query =
        "select maxFI.patient_id from ( "
            + "Select p.patient_id, max(e.encounter_datetime) as encounter_datetime "
            + "From patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type = 97 and e.location_id=:location "
            + "and e.encounter_datetime <= :endDate "
            + "group by p.patient_id "
            + ") maxFI "
            + "  inner join encounter e on e.patient_id = maxFI.patient_id "
            + "  inner join obs o on e.encounter_id = o.encounter_id "
            + "  where e.voided = 0 and o.voided = 0 and e.encounter_type = 97 and o.concept_id=165549 and o.value_coded in (1065,165551) "
            + "  and e.location_id =:location and e.encounter_datetime = maxFI.encounter_datetime "
            + "  group by e.patient_id "
            + " "
            + "  union "
            + " "
            + "  select maxFI.patient_id from ( "
            + "Select p.patient_id, max(e.encounter_datetime) as encounter_datetime "
            + "From patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type = 96 and e.location_id=:location "
            + "and e.encounter_datetime <= :endDate "
            + "group by p.patient_id "
            + ") maxFI "
            + "  inner join encounter e on e.patient_id = maxFI.patient_id "
            + "  inner join obs o on e.encounter_id = o.encounter_id "
            + "  where e.voided = 0 and o.voided = 0 and e.encounter_type = 96 and o.concept_id=165459 and o.value_coded = 703 "
            + "  and e.location_id =:location and e.encounter_datetime = maxFI.encounter_datetime "
            + "  group by e.patient_id ";

    return query;
  }

  /**
   * RF14 - Indicador 8: Utentes com pelo menos uma Tentativa de Homicídio a entrada
   *
   * @return String
   */
  public static String findPatientsWithAtLeastOneHomicideAttempt() {

    String query =
        "select maxFI.patient_id from ( "
            + "Select p.patient_id, max(e.encounter_datetime) as encounter_datetime "
            + "From patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type = 97 and e.location_id=:location "
            + "and e.encounter_datetime <= :endDate "
            + "group by p.patient_id "
            + ") maxFI "
            + "  inner join encounter e on e.patient_id = maxFI.patient_id "
            + "  inner join obs o on e.encounter_id = o.encounter_id "
            + "  where e.voided = 0 and o.voided = 0 and e.encounter_type = 97 and o.concept_id=165611 and o.value_coded in (1065,165550,165551) "
            + "  and e.location_id =:location and e.encounter_datetime = maxFI.encounter_datetime "
            + "  group by e.patient_id ";

    return query;
  }

  /**
   * RF18 - Indicador 12 -- Nr de pacientes com sintomas de depressão
   *
   * @return String
   */
  public static String findPatientsWithSymptomsOfDepression() {

    String query =
        "select patient_id from ( "
            + "select p.patient_id, max(e.encounter_datetime) encounter_datetime from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	inner join obs  o on e.encounter_id=o.encounter_id "
            + "	inner join obs  oG on e.encounter_id=oG.encounter_id "
            + "	where e.voided=0 and o.voided=0 and oG.voided= 0 and p.voided=0 and e.encounter_type = 97 and "
            + "	o.concept_id = 165539 and oG.concept_id = 165541 and o.value_numeric > 0 "
            + "	and o.obs_group_id = oG.obs_id "
            + "and e.encounter_datetime<=:endDate and e.location_id=:location "
            + "group by p.patient_id "
            + ")f ";

    return query;
  }

  /**
   * Indicador 13 – Nr de pacientes com sintomas de ansiedade
   *
   * @return String
   */
  public static String findPatientsWithSymptomsOfAnxiety() {

    String query =
        "select patient_id from ( "
            + "select p.patient_id, max(e.encounter_datetime) encounter_datetime,o.value_numeric from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	inner join obs  o on e.encounter_id=o.encounter_id "
            + "	inner join obs  oG on e.encounter_id=oG.encounter_id "
            + "	where e.voided=0 and o.voided=0 and oG.voided= 0 and p.voided=0 and e.encounter_type = 97 and "
            + "	o.concept_id = 165539 and oG.concept_id = 165542 and o.value_numeric > 0 "
            + "	and o.obs_group_id = oG.obs_id "
            + "and e.encounter_datetime<=:endDate and e.location_id=:location "
            + "group by p.patient_id "
            + ")f ";

    return query;
  }

  /**
   * RF16 - Indicador 10 - Nr de Pacientes com Ideação de Homicídio a entrada
   *
   * @return String
   */
  public static String findPatientsWithAtLeastOneHomicideIdeation() {

    String query =
        "  select maxFI.patient_id from ( "
            + "Select p.patient_id, max(e.encounter_datetime) as encounter_datetime "
            + "From patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type = 97 and e.location_id=:location "
            + "and e.encounter_datetime <= :endDate "
            + "group by p.patient_id "
            + ") maxFI "
            + "  inner join encounter e on e.patient_id = maxFI.patient_id "
            + "  inner join obs o on e.encounter_id = o.encounter_id "
            + "  where e.voided = 0 and o.voided = 0 and e.encounter_type = 97 and o.concept_id=165552 and o.value_coded in (1065, 165551) "
            + "  and e.location_id =:location and e.encounter_datetime = maxFI.encounter_datetime "
            + "  group by e.patient_id ";

    return query;
  }

  /**
   * RF16 - Indicador 10: Utentes com trauma
   *
   * @return String
   */
  public static String findPatientsWithTrauma() {

    String query =
        "select patient_id from ( "
            + "select p.patient_id, max(e.encounter_datetime) encounter_datetime from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	inner join obs  o on e.encounter_id=o.encounter_id "
            + "	inner join obs  oG on e.encounter_id=oG.encounter_id "
            + "	where e.voided=0 and o.voided=0 and oG.voided= 0 and p.voided=0 and e.encounter_type = 97 and "
            + "	o.concept_id = 165539 and oG.concept_id = 165543 and o.value_numeric > 0 "
            + "and e.encounter_datetime<=:endDate and e.location_id=:location "
            + "	and o.obs_group_id = oG.obs_id "
            + "group by p.patient_id "
            + ")f ";

    return query;
  }

  /**
   * RF17 - Indicador 11: Utentes que consomem abusivamente bebidas
   *
   * @return String
   */
  public static String findPatientsWhoAbuseAlcohol() {

    String query =
        "select patient_id from ( "
            + "select p.patient_id, max(e.encounter_datetime) encounter_datetime from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	inner join obs  o on e.encounter_id=o.encounter_id "
            + "	where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 96 and "
            + "	o.concept_id = 165450 and o.value_coded in (1092,1093,165527) "
            + "and e.encounter_datetime<=:endDate and e.location_id=:location "
            + "group by p.patient_id "
            + " "
            + "union "
            + " "
            + "select p.patient_id, max(e.encounter_datetime) encounter_datetime from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	inner join obs  o on e.encounter_id=o.encounter_id "
            + "	where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 96 and "
            + "	o.concept_id = 165451 and o.value_coded in (165530,165531,165532) "
            + "and e.encounter_datetime<=:endDate and e.location_id=:location "
            + "group by p.patient_id "
            + ")f ";

    return query;
  }

  /**
   * RF18 - Indicador 12 - Nr de pacientes que consomem outras substâncias psicoactivas (ex.
   * Canabis, marijuana, etc)
   *
   * @return String
   */
  public static String findPatientsWhoConsumeOtherPsychoactiveSubstances() {

    String query =
        "select patient_id from ( "
            + "select p.patient_id, max(e.encounter_datetime) encounter_datetime from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	inner join obs  o on e.encounter_id=o.encounter_id "
            + "	where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 96 and "
            + "	o.concept_id = 20454 and o.value_coded in (21084,21081,165534) "
            + "and e.encounter_datetime<=:endDate and e.location_id=:location "
            + "group by p.patient_id "
            + ")f ";

    return query;
  }

  /**
   * RF19 - Incluindo todos os utentes com Ficha Seguimento CETA registada no período após a (Data
   * Fim de Reporte - 89 dias)
   *
   * @return String
   */
  public static String findPatientsWithFichaSeguimentoCETA89DaysBeforeEndDate() {

    String query =
        "select patient_id from ( "
            + "select p.patient_id, e.encounter_datetime from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	where e.voided=0 and p.voided=0 and e.encounter_type = 98 "
            + "	and e.encounter_datetime>=:endDate - interval 89 day "
            + "and e.encounter_datetime<=:endDate and e.location_id=:location "
            + ") f";

    return query;
  }

  /**
   * RF19 - Fltrando os utentes que não tem nenhuma Ficha de Seguimento CETA registada no sistema
   * durante período entre (Data Fim de Reporte - 59 dias) e (Data Fim de Reporte
   *
   * @return String
   */
  public static String findPatientsWithoutFichaSeguimentoCETA59DaysBeforeEndDate() {

    String query =
        "select patient_id from ( "
            + "select p.patient_id, e.encounter_datetime from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	where e.voided=0 and p.voided=0 and e.encounter_type = 98 "
            + "	and e.encounter_datetime>=:endDate - interval 59 day "
            + "and e.encounter_datetime<=:endDate and e.location_id=:location "
            + ")f";

    return query;
  }

  /**
   * RF19 - Excluindo os utentes que tem tratamento planificado concluido registado na secção 7 da
   * Ficha Inicial – CETA mais recente até ao final do perío do de reporte
   *
   * @return String
   */
  public static String findPatientsWithTreatmentPlannedRegisteredInSection7ByReportEndDate() {

    String query =
        "select maxFI.patient_id from ( "
            + "Select p.patient_id, max(e.encounter_datetime) as encounter_datetime "
            + "From patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type = 97 and e.location_id=:location "
            + "and e.encounter_datetime <= :endDate "
            + "group by p.patient_id "
            + ") maxFI "
            + "  inner join encounter e on e.patient_id = maxFI.patient_id "
            + "  inner join obs o on e.encounter_id = o.encounter_id "
            + "  where e.voided = 0 and o.voided = 0 and e.encounter_type = 97 and o.concept_id=165566 and o.value_coded=1065 "
            + "  and e.location_id =:location and e.encounter_datetime = maxFI.encounter_datetime "
            + "  group by e.patient_id ";

    return query;
  }

  /**
   * RF20 - Indicador 14: Utentes referidos
   *
   * @return String
   */
  public static String findPatientsWhoWereReferred() {

    String query =
        "select patient_id from ( "
            + "select p.patient_id, max(e.encounter_datetime) encounter_datetime from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	inner join obs  o on e.encounter_id=o.encounter_id "
            + "	where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type = 97 and "
            + "	o.concept_id = 165567 and o.value_coded = 1272 "
            + "and e.encounter_datetime<=:endDate and e.location_id=:location "
            + "group by p.patient_id "
            + ") f";

    return query;
  }

  /**
   * RF21 - Indicador 15: Utentes transferidos
   *
   * @return String
   */
  public static String findPatientsWhoHaveTransferredAsAreasonOfInterruption() {

    String query =
        "select maxFI.patient_id from ( "
            + "Select p.patient_id, max(e.encounter_datetime) as encounter_datetime "
            + "From patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type = 97 and e.location_id=:location "
            + "and e.encounter_datetime <= :endDate "
            + "group by p.patient_id "
            + ") maxFI "
            + "  inner join encounter e on e.patient_id = maxFI.patient_id "
            + "  inner join obs o on e.encounter_id = o.encounter_id "
            + "  where e.voided = 0 and o.voided = 0 and e.encounter_type = 97 and o.concept_id=165567 and o.value_coded = 1706 "
            + "  and e.location_id =:location and e.encounter_datetime = maxFI.encounter_datetime "
            + "  group by e.patient_id ";

    return query;
  }

  /**
   * RF22 - Indicador 16: Utentes reintegrados
   *
   * @return String
   */
  public static String findPatientsWhoWereReintegrated() {

    String query =
        "select patient_id from ( "
            + "select p.patient_id, e.encounter_datetime from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	where e.voided=0 and p.voided=0 and e.encounter_type = 98 "
            + "	and e.encounter_datetime>=:startDate - interval 60 day "
            + "and e.encounter_datetime<:startDate and e.location_id=:location "
            + ")f";

    return query;
  }

  /**
   * RF23 - Indicador 17: Utentes óbitos
   *
   * @return String
   */
  public static String findPatientsDeaths() {

    String query =
        "select maxFI.patient_id from ( "
            + "Select p.patient_id, max(e.encounter_datetime) as encounter_datetime "
            + "From patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type = 97 and e.location_id=:location "
            + "and e.encounter_datetime <= :endDate "
            + "group by p.patient_id "
            + ") maxFI "
            + "  inner join encounter e on e.patient_id = maxFI.patient_id "
            + "  inner join obs o on e.encounter_id = o.encounter_id "
            + "  where e.voided = 0 and o.voided = 0 and e.encounter_type = 97 and o.concept_id=165567 and o.value_coded = 1366 "
            + "  and e.location_id =:location and e.encounter_datetime = maxFI.encounter_datetime "
            + "  group by e.patient_id ";

    return query;
  }

  /**
   * RF19 - Incluindo todos os utentes com Ficha Seguimento CETA registada no período após a (Data
   * Fim de Reporte - 89 dias)
   *
   * @return String
   */
  public static String findPatientsWithFichaSeguimentoCETAAfter89DaysMinusEndDate() {

    String query =
        "select p.patient_id from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	where e.voided=0 and p.voided=0 and e.encounter_type = 98 "
            + "	and e.encounter_datetime>=:endDate - interval 89 day "
            + " and e.encounter_datetime <= :endDate "
            + " and e.location_id=:location ";

    return query;
  }

  /**
   * 26.1 - Identificação de utentes com critério de rastreio - 2ª consulta de TARV – Ficha Inicial
   * CETA
   *
   * @return String
   */
  public static String findPatientsWhoHaveScreeningCriteriaSecondConsultationInFichaInicialCETA() {

    String query =
        "select maxFI.patient_id from ( "
            + "Select p.patient_id, max(e.encounter_datetime) as encounter_datetime "
            + "From patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type = 97 and e.location_id=:location "
            + "and e.encounter_datetime <= :endDate "
            + "group by p.patient_id "
            + ") maxFI "
            + "  inner join encounter e on e.patient_id = maxFI.patient_id "
            + "  inner join obs o on e.encounter_id = o.encounter_id "
            + "  where e.voided = 0 and o.voided = 0 and e.encounter_type = 97 and o.concept_id=165535 and o.value_coded=165536 "
            + "  and e.location_id =:location and e.encounter_datetime = maxFI.encounter_datetime "
            + "  group by e.patient_id ";

    return query;
  }

  /**
   * 26.2 - Identificação de utentes com critério de rastreio – CV>1000 cp/ml – Ficha Inicial CETA
   *
   * @return String
   */
  public static String
      findPatientsWhoHaveScreeningCriteriaCVGreaterThan1000CopiesInFichaInicialCETA() {

    String query =
        "select maxFI.patient_id from ( "
            + "Select p.patient_id, max(e.encounter_datetime) as encounter_datetime "
            + "From patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type = 97 and e.location_id=:location "
            + "and e.encounter_datetime <= :endDate "
            + "group by p.patient_id "
            + ") maxFI "
            + "  inner join encounter e on e.patient_id = maxFI.patient_id "
            + "  inner join obs o on e.encounter_id = o.encounter_id "
            + "  where e.voided = 0 and o.voided = 0 and e.encounter_type = 97 and o.concept_id=165535 and o.value_coded = 23912 "
            + "  and e.location_id =:location and e.encounter_datetime = maxFI.encounter_datetime "
            + "  group by e.patient_id ";

    return query;
  }

  /**
   * 26.3 - Identificação de utentes com critério de rastreio – Reintegrado / Má adesão – Ficha
   * Inicial CETA
   *
   * @return String
   */
  public static String findPatientsWhoHaveScreeningCriteriaReintegretedInFichaInicialCETA() {

    String query =
        "select maxFI.patient_id from ( "
            + "Select p.patient_id, max(e.encounter_datetime) as encounter_datetime "
            + "From patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type = 97 and e.location_id=:location "
            + "and e.encounter_datetime <= :endDate "
            + "group by p.patient_id "
            + ") maxFI "
            + "  inner join encounter e on e.patient_id = maxFI.patient_id "
            + "  inner join obs o on e.encounter_id = o.encounter_id "
            + "  where e.voided = 0 and o.voided = 0 and e.encounter_type = 97 and o.concept_id=165535 and o.value_coded = 23993 "
            + "  and e.location_id =:location and e.encounter_datetime = maxFI.encounter_datetime "
            + "  group by e.patient_id ";

    return query;
  }

  /**
   * 26.4 - Identificação de utentes com critério de rastreio – Factores Psicossociais – Ficha
   * Inicial CETA
   *
   * @return String
   */
  public static String findPatientsWhoHaveScreeningCriteriaPsychosocialFactorsInFichaInicialCETA() {

    String query =
        "select maxFI.patient_id from ( "
            + "Select p.patient_id, max(e.encounter_datetime) as encounter_datetime "
            + "From patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type = 97 and e.location_id=:location "
            + "and e.encounter_datetime <= :endDate "
            + "group by p.patient_id "
            + ") maxFI "
            + "  inner join encounter e on e.patient_id = maxFI.patient_id "
            + "  inner join obs o on e.encounter_id = o.encounter_id "
            + "  where e.voided = 0 and o.voided = 0 and e.encounter_type = 97 and o.concept_id=6193 and o.value_coded is not null "
            + "  and e.location_id =:location and e.encounter_datetime = maxFI.encounter_datetime "
            + "  group by e.patient_id ";

    return query;
  }
}
