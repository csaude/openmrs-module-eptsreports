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

public class ResumoMensalCCRQueries {

  /**
   * Indicador 1: Total de 1as consultas
   *
   * @return String
   */
  public static String getChildrenWithFirstConsultationCCRDuringPeriodIndicator1() {

    String query =
        "	select 	p.patient_id,min(encounter_datetime) date_first_consultation_ccr "
            + "	from 	patient p "
            + "			inner join encounter e on e.patient_id=p.patient_id "
            + "	where 	p.voided=0 and e.voided=0 and e.encounter_type=92 and "
            + "			e.location_id=:location and e.encounter_datetime between :startDate and :endDate "
            + "	group by p.patient_id ";

    return query;
  }

  /**
   * Indicador 2: Total de crianças com contacto com tuberculose
   *
   * @return String
   */
  public static String getChildrenWithTbHivContactIndicator2() {

    String query =
        "	select p.patient_id, min(e.encounter_datetime) tb_contact_date "
            + "	           from patient p "
            + "	             inner join encounter e on p.patient_id=e.patient_id "
            + "	             inner join obs o on o.encounter_id=e.encounter_id "
            + "	             where e.voided=0 "
            + "	             and o.voided=0 "
            + "	             and p.voided=0 "
            + "	             and e.encounter_type = 92 "
            + "	             and o.concept_id=1874 "
            + "	             and o.value_coded = 1845 "
            + "	             and e.encounter_datetime between :startDate and :endDate "
            + "	             and e.location_id=:location "
            + "	         group by p.patient_id ";

    return query;
  }

  /**
   * Total de crianças com desnutrição aguda
   *
   * @return String
   */
  public static String getChildrenWithAcuteMalNutrition() {

    String query =
        "		select p.patient_id, min(e.encounter_datetime) desnutricao_aguda_date "
            + "           from patient p "
            + "             inner join encounter e on p.patient_id=e.patient_id "
            + "             inner join obs o on o.encounter_id=e.encounter_id "
            + "             where e.voided=0 "
            + "             and o.voided=0 "
            + "             and p.voided=0 "
            + "             and e.encounter_type = 92 "
            + "             and o.concept_id=1874 "
            + "             and o.value_coded = 1844 "
            + "             and e.encounter_datetime between :startDate and :endDate "
            + "             and e.location_id=:location "
            + "         group by p.patient_id ";

    return query;
  }

  /**
   * Total de crianças com desnutrição aguda
   *
   * @return String
   */
  public static String getChildrenWithModerateAcuteMalNutrition() {

    String query =
        "         		               select p.patient_id, min(e.encounter_datetime) desnutricao_moderada_date "
            + "           from patient p "
            + "             inner join encounter e on p.patient_id=e.patient_id "
            + "             inner join obs o on o.encounter_id=e.encounter_id "
            + "             where e.voided=0 "
            + "             and o.voided=0 "
            + "             and p.voided=0 "
            + "             and e.encounter_type = 93 "
            + "             and o.concept_id=23756 "
            + "             and o.value_coded = 165497 "
            + "             and e.encounter_datetime between :startDate and :endDate "
            + "             and e.location_id=:location "
            + "         group by p.patient_id ";

    return query;
  }

  /**
   * Total de crianças com desnutrição aguda grave
   *
   * @return String
   */
  public static String getChildrenWithSevereAcuteMalNutrition() {

    String query =
        "         		               select p.patient_id, min(e.encounter_datetime) desnutricao_moderada_date "
            + "           from patient p "
            + "             inner join encounter e on p.patient_id=e.patient_id "
            + "             inner join obs o on o.encounter_id=e.encounter_id "
            + "             where e.voided=0 "
            + "             and o.voided=0 "
            + "             and p.voided=0 "
            + "             and e.encounter_type = 93 "
            + "             and o.concept_id=23756 "
            + "             and o.value_coded = 165496 "
            + "             and e.encounter_datetime between :startDate and :endDate "
            + "             and e.location_id=:location "
            + "         group by p.patient_id ";

    return query;
  }

  /**
   * Indicador 5 “Total de crianças com exposição ao HIV”
   *
   * @return String
   */
  public static String getChildrenWithExposureToHIVIndicator5() {

    String query =
        "		   select p.patient_id, min(e.encounter_datetime) exposicao_hiv_date "
            + " from patient p "
            + "   inner join encounter e on p.patient_id=e.patient_id "
            + "   inner join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = 92 "
            + "   and o.concept_id=1874 "
            + "   and o.value_coded = 1586 "
            + "   and e.encounter_datetime between :startDate and :endDate "
            + "   and e.location_id=:location "
            + "group by p.patient_id ";

    return query;
  }

  /**
   * Indicador 6 “Total de crianças com outra condição de Risco”
   *
   * @return String
   */
  public static String getChildrenWithOtherRiskConditionIndicator6() {

    String query =
        "		   select p.patient_id, min(e.encounter_datetime) exposicao_hiv_date "
            + " from patient p "
            + "   inner join encounter e on p.patient_id=e.patient_id "
            + "   inner join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = 92 "
            + "   and o.concept_id=1874 "
            + "   and o.value_coded in (1842,6397,5050,1847,1846,1843,6409,5622) "
            + "   and e.encounter_datetime between :startDate and :endDate "
            + "   and e.location_id=:location "
            + "group by p.patient_id ";

    return query;
  }

  /**
   * Total de crianças que iniciaram Isoniazida na CCR
   *
   * @return String
   */
  public static String getChildrenWhoInitiatedINHTreatment() {

    String query =
        "		select p.patient_id, min(e.encounter_datetime) inh_date "
            + " from patient p "
            + "   inner join encounter e on p.patient_id=e.patient_id "
            + "   inner join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = 93 "
            + "   and o.concept_id=23985 "
            + "   and o.value_coded = 656 "
            + "   and e.encounter_datetime between :startDate and :endDate "
            + "   and e.location_id=:location "
            + "group by p.patient_id ";

    return query;
  }

  /**
   * Filtrando as crianças que tiveram registo de “Diagnóstico Tratamento” igual a "Profilaxia com
   * Isoniazida” em seis (6) consultas (“Ficha de Seguimento de CCR”)
   *
   * @return String
   */
  public static String getChildrenWhoHave6OrMoreCCRWithINHTreatmentRegistered() {

    String query =
        "		select p.patient_id, count(e.encounter_id) as numero_consultas "
            + " from patient p "
            + "   inner join encounter e on p.patient_id=e.patient_id "
            + "   inner join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = 93 "
            + "   and o.concept_id=23985 "
            + "   and o.value_coded = 656 "
            + "   and e.encounter_datetime between :startDate - interval 10 month and :endDate "
            + "   and e.location_id=:location "
            + "     group by p.patient_id "
            + "   having count(e.encounter_id) >= 6 ";

    return query;
  }

  /**
   * Total de crianças que receberam ATPU
   *
   * @return String
   */
  public static String getChildrenWhithATPU() {

    String query =
        "		select p.patient_id, min(e.encounter_datetime) atpu_date "
            + " from patient p "
            + "   inner join encounter e on p.patient_id=e.patient_id "
            + "   inner join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = 93 "
            + "   and o.concept_id=6143 "
            + "   and o.value_coded = 1065 "
            + "   and e.encounter_datetime between :startDate and :endDate "
            + "   and e.location_id=:location "
            + "group by p.patient_id ";

    return query;
  }

  /**
   * Total de crianças que receberam CSB/suplemento nutricional
   *
   * @return String
   */
  public static String getChildrenWhoReceivedCSB() {

    String query =
        "		select p.patient_id, min(e.encounter_datetime) atpu_date "
            + " from patient p "
            + "   inner join encounter e on p.patient_id=e.patient_id "
            + "   inner join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = 93 "
            + "   and o.concept_id=6143 "
            + "   and o.value_coded = 1065 "
            + "   and e.encounter_datetime between :startDate and :endDate "
            + "   and e.location_id=:location "
            + "group by p.patient_id ";

    return query;
  }

  /**
   * Total de crianças que iniciaram CTZ
   *
   * @return String
   */
  public static String getChildrenWhoInitiatedCTZTreatment() {

    String query =
        "		select p.patient_id, min(e.encounter_datetime) ctz_date "
            + " from patient p "
            + "   inner join encounter e on p.patient_id=e.patient_id "
            + "   inner join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = 93 "
            + "   and o.concept_id=6121 "
            + "   and o.value_coded = 1065 "
            + "   and e.encounter_datetime between :startDate and :endDate "
            + "   and e.location_id=:location "
            + "group by p.patient_id ";
    return query;
  }

  /**
   * Total de 1º PCR colhido
   *
   * @return String
   */
  public static String getChildrenWithFirsPCRCollected() {

    String query =
        "select pcr.patient_id,pcr.pcr_date from ( "
            + "				select p.patient_id, min(o.obs_datetime) pcr_date "
            + "           from patient p "
            + "             inner join encounter e on p.patient_id=e.patient_id "
            + "             inner join obs o on o.encounter_id=e.encounter_id "
            + "             where e.voided=0 "
            + "             and o.voided=0 "
            + "             and p.voided=0 "
            + "             and e.encounter_type = 93 "
            + "             and o.concept_id=1998 "
            + "             and o.obs_datetime between :startDate and :endDate "
            + "             and e.location_id=:location "
            + "         group by p.patient_id ";
    return query;
  }

  /**
   * Total de crianças expostas ≥9 meses testadas com Teste Rápido de HIV
   *
   * @return String
   */
  public static String getChildrenExposedAndTestedForHIV() {

    String query =
        "		   select p.patient_id, min(e.encounter_datetime) exposicao_hiv_date "
            + " from patient p "
            + "   inner join encounter e on p.patient_id=e.patient_id "
            + "   inner join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = 92 "
            + "   and o.concept_id=1874 "
            + "   and o.value_coded = 1586 "
            + "   and e.encounter_datetime between :startDate and :endDate "
            + "   and e.location_id=:location "
            + "group by p.patient_id ";
    return query;
  }

  /**
   * Total de crianças expostas ≥9 meses testadas com Teste Rápido de HIV
   *
   * @return String
   */
  public static String getChildrenWithAnyHIVTestResult() {

    String query =
        "		select p.patient_id, min(e.encounter_datetime) hiv_date "
            + " from patient p "
            + "   inner join encounter e on p.patient_id=e.patient_id "
            + "   inner join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = 93 "
            + "   and o.concept_id=1040 "
            + "   and o.value_coded in (703,664,1138) "
            + "   and e.encounter_datetime between :startDate and :endDate "
            + "   and e.location_id=:location "
            + "group by p.patient_id ";
    return query;
  }

  /**
   * Indicador 16 “Total de crianças não expostas ao HIV, testadas com Teste Rápido que tiveram
   * resultado positivo”
   *
   * @return String
   */
  public static String getChildrenWithAnyHIVTestResultPositive() {

    String query =
        "		select p.patient_id, min(e.encounter_datetime) hiv_date "
            + " from patient p "
            + "   inner join encounter e on p.patient_id=e.patient_id "
            + "   inner join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = 93 "
            + "   and o.concept_id=1040 "
            + "   and o.value_coded = 703 "
            + "   and e.encounter_datetime between :startDate and :endDate "
            + "   and e.location_id=:location "
            + "group by p.patient_id ";
    return query;
  }

  /**
   * Filtrando as crianças que tiveram registo de “Transferido para sector de TB” na “Ficha Resumo
   * de CCR” ou na última “Ficha de Seguimento de CCR”
   *
   * @return String
   */
  public static String getChildrenReferredToPNCT(int encounterType) {

    String query =
        "		select p.patient_id, min(e.encounter_datetime) data_transferencia_tb "
            + " from patient p "
            + "   inner join encounter e on p.patient_id=e.patient_id "
            + "   inner join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = %d "
            + "   and o.concept_id=1873 "
            + "   and o.value_coded = 165483 "
            + "   and e.encounter_datetime between :startDate and :endDate "
            + "   and e.location_id=:location "
            + "     group by p.patient_id ";

    return String.format(query, encounterType);
  }

  /**
   * Filtrando as crianças que tiveram registo de “Abandono” na “Ficha Resumo de CCR” ou na última
   * “Ficha de Seguimento de CCR”
   *
   * @return String
   */
  public static String getChildrenWhoAbandonedCCR(int encounterType) {

    String query =
        "		select p.patient_id, min(e.encounter_datetime) data_abandono "
            + " from patient p "
            + "   inner join encounter e on p.patient_id=e.patient_id "
            + "   inner join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = %d "
            + "   and o.concept_id=1873 "
            + "   and o.value_coded = 1707 "
            + "   and e.encounter_datetime between :startDate and :endDate "
            + "   and e.location_id=:location "
            + "     group by p.patient_id ";

    return String.format(query, encounterType);
  }

  /**
   * Filtrando as crianças que tiveram registo de “Transferido para Consulta de Criança Sadia” na
   * “Ficha Resumo de CCR” ou na última “Ficha de Seguimento de CCR”
   *
   * @return String
   */
  public static String getChildrenTransferredToHealthyChildConsultation(int encounterType) {

    String query =
        "		select p.patient_id, min(e.encounter_datetime) data_abandono "
            + " from patient p "
            + "   inner join encounter e on p.patient_id=e.patient_id "
            + "   inner join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = %d "
            + "   and o.concept_id=1873 "
            + "   and o.value_coded = 165485 "
            + "   and e.encounter_datetime between :startDate and :endDate "
            + "   and e.location_id=:location "
            + "     group by p.patient_id ";

    return String.format(query, encounterType);
  }

  /**
   * Filtrando as que tiveram o registo de “Referido para Internamento” igual a "Sim” na última
   * consulta de CCR
   *
   * @return String
   */
  public static String getChildrenWithDAGReferredForHospitalization() {

    String query =
        "	select 	internamento.patient_id "
            + "	from "
            + "	( "
            + "		select 	p.patient_id,max(encounter_datetime) data_internamento "
            + "		from 	patient p "
            + "				inner join encounter e on e.patient_id=p.patient_id "
            + "		where 	p.voided=0 and e.voided=0 and e.encounter_type=93 and "
            + "				e.location_id=220 and e.encounter_datetime between :startDate and :endDate "
            + "		group by p.patient_id "
            + "	) 	internamento "
            + "		inner join encounter e on e.patient_id=internamento.patient_id "
            + "		inner join obs o on e.encounter_id=o.encounter_id "
            + " "
            + "	where 	e.voided=0 and o.voided=0 and e.encounter_type=93 and "
            + "			e.encounter_datetime=internamento.data_internamento and "
            + "			o.concept_id=1595 and o.value_coded = 1065 ";

    return query;
  }

  /**
   * Filtrando as crianças que tiveram registo de “Óbito” na “Ficha Resumo de CCR”
   *
   * @return String
   */
  public static String getChildrenMarkedDeathOnFichaResumoCCR() {

    String query =
        "		select p.patient_id, min(e.encounter_datetime) data_abandono "
            + " from patient p "
            + "   inner join encounter e on p.patient_id=e.patient_id "
            + "   inner join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = 92 "
            + "   and o.concept_id=1873 "
            + "   and o.value_coded = 1366 "
            + "   and e.encounter_datetime between :startDate and :endDate "
            + "   and e.location_id=:location "
            + "     group by p.patient_id ";

    return query;
  }

  /**
   * Filtrando as crianças que tiveram registo de “Óbito” na última “Ficha de Seguimento de CCR”
   *
   * @return String
   */
  public static String getChildrenMarkedDeathOnFichaSeguimentoCCR() {

    String query =
        "	select 	obito.patient_id "
            + "	from "
            + "	( "
            + "		select 	p.patient_id,max(encounter_datetime) data_obito "
            + "		from 	patient p "
            + "				inner join encounter e on e.patient_id=p.patient_id "
            + "		where 	p.voided=0 and e.voided=0 and e.encounter_type=93 and "
            + "				e.location_id=:location and e.encounter_datetime between :startDate and :endDate "
            + "		group by p.patient_id "
            + "	) 	obito "
            + "		inner join encounter e on e.patient_id=obito.patient_id "
            + "		inner join obs o on e.encounter_id=o.encounter_id "
            + " "
            + "	where 	e.voided=0 and o.voided=0 and e.encounter_type=93 and "
            + "			e.encounter_datetime=obito.data_obito and "
            + "			o.concept_id=1873 and o.value_coded = 165485 ";

    return query;
  }

  /**
   * Filtrando as crianças que tiveram registo de “PTV Mãe” igual a “TARV”
   *
   * @return String
   */
  public static String getChildrenWithMotherInTARV() {

    String query =
        "		select p.patient_id, min(e.encounter_datetime) data_abandono "
            + " from patient p "
            + "   inner join encounter e on p.patient_id=e.patient_id "
            + "   inner join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = 92 "
            + "   and o.concept_id=6394 "
            + "   and o.value_coded = 6276 "
            + "   and e.encounter_datetime between :startDate and :endDate "
            + "   and e.location_id=:location "
            + "     group by p.patient_id ";

    return query;
  }

  /**
   * Filtrando as crianças que tiveram registo de “Aleitamento Materno Exclusivo” igual a “Sim” numa
   * “Ficha de Seguimento de CCR”
   *
   * @return String
   */
  public static String getChildrenWithAleitamentoMaternoExclusivoInFichaCCR() {

    String query =
        "		select p.patient_id, min(e.encounter_datetime) data_abandono "
            + " from patient p "
            + "   inner join encounter e on p.patient_id=e.patient_id "
            + "   inner join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = 92 "
            + "   and o.concept_id=6394 "
            + "   and o.value_coded = 6276 "
            + "   and e.encounter_datetime between :startDate and :endDate "
            + "   and e.location_id=:location "
            + "     group by p.patient_id ";

    return query;
  }

  public static String findPatientsAgeGreaterThan(int age) {

    String query =
        "select firstConsultationCCR.patient_id from ( "
            + "	select p.patient_id,min(encounter_datetime) date_first_consultation_ccr "
            + "		from 	patient p "
            + "				inner join encounter e on e.patient_id=p.patient_id "
            + "		where 	p.voided=0 and e.voided=0 and e.encounter_type=92 and "
            + "				e.location_id=:location and e.encounter_datetime between :startDate and :endDate "
            + "		group by p.patient_id "
            + "		) firstConsultationCCR "
            + "		inner join person on person_id = firstConsultationCCR.patient_id "
            + "	   WHERE (TIMESTAMPDIFF(month, birthdate, firstConsultationCCR.date_first_consultation_ccr)) >= %d  AND birthdate IS NOT NULL and voided = 0 ";

    return String.format(query, age);
  }

  public static String findPatientsAgeLessThan(int age) {

    String query =
        "select firstConsultationCCR.patient_id from ( "
            + "	select p.patient_id,min(encounter_datetime) date_first_consultation_ccr "
            + "		from 	patient p "
            + "				inner join encounter e on e.patient_id=p.patient_id "
            + "		where 	p.voided=0 and e.voided=0 and e.encounter_type=92 and "
            + "				e.location_id=:location and e.encounter_datetime between :startDate and :endDate "
            + "		group by p.patient_id "
            + "		) firstConsultationCCR "
            + "		inner join person on person_id = firstConsultationCCR.patient_id "
            + "	   WHERE (TIMESTAMPDIFF(month, birthdate, firstConsultationCCR.date_first_consultation_ccr)) < %d  AND birthdate IS NOT NULL and voided = 0 ";

    return String.format(query, age);
  }

  public static String findPatientsWithAgeEqualTo(int age) {

    String query =
        "select firstConsultationCCR.patient_id from ( "
            + "	select p.patient_id,min(encounter_datetime) date_first_consultation_ccr "
            + "		from 	patient p "
            + "				inner join encounter e on e.patient_id=p.patient_id "
            + "		where 	p.voided=0 and e.voided=0 and e.encounter_type=92 and "
            + "				e.location_id=:location and e.encounter_datetime between :startDate and :endDate "
            + "		group by p.patient_id "
            + "		) firstConsultationCCR "
            + "		inner join person on person_id = firstConsultationCCR.patient_id "
            + "	   WHERE (TIMESTAMPDIFF(month, birthdate, firstConsultationCCR.date_first_consultation_ccr)) = %d  AND birthdate IS NOT NULL and voided = 0 ";

    return String.format(query, age);
  }
}
