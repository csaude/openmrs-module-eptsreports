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
        "select patient_id from ( "
            + "	select 	p.patient_id,min(encounter_datetime) date_first_consultation_ccr "
            + "	from 	patient p "
            + "			inner join encounter e on e.patient_id=p.patient_id "
            + "	where 	p.voided=0 and e.voided=0 and e.encounter_type=92 and "
            + "			e.location_id=:location and e.encounter_datetime between :startDate and :endDate "
            + "	group by p.patient_id "
            + "	) final ";

    return query;
  }

  /**
   * Indicador 2: Total de crianças com contacto com tuberculose 1845 = contacto com tuberculose
   * 1844 = Desnutricao Aguda 1586 = Mae HIV Positivo
   *
   * @return String
   */
  public static String getChildrenWithXMotivoDeConsulta(int valueCoded) {

    String query =
        "select patient_id from ( "
            + "	select p.patient_id, min(e.encounter_datetime) tb_contact_date "
            + "	           from patient p "
            + "	             inner join encounter e on p.patient_id=e.patient_id "
            + "	             inner join obs o on o.encounter_id=e.encounter_id "
            + "	             where e.voided=0 "
            + "	             and o.voided=0 "
            + "	             and p.voided=0 "
            + "	             and e.encounter_type = 92 "
            + "	             and o.concept_id=1874 "
            + "	             and o.value_coded = %s "
            + "	             and e.encounter_datetime between :startDate and :endDate "
            + "	             and e.location_id=:location "
            + "	         group by p.patient_id "
            + "	         ) f ";

    return String.format(query, valueCoded);
  }

  /**
   * Total de crianças com desnutrição aguda
   *
   * <p>165497 = Desnutricao Aguda Moderada 165496 = Desnutricao Aguda Grave
   *
   * @return String
   */
  public static String getChildrenWithXAcuteMalNutrition(int valueCoded) {

    String query =
        "		select patient_id from ( "
            + "    		             select p.patient_id, min(e.encounter_datetime) desnutricao_moderada_date "
            + "      from patient p "
            + "        inner join encounter e on p.patient_id=e.patient_id "
            + "        inner join obs o on o.encounter_id=e.encounter_id "
            + "        where e.voided=0 "
            + "        and o.voided=0 "
            + "        and p.voided=0 "
            + "        and e.encounter_type = 93 "
            + "        and o.concept_id=23756 "
            + "        and o.value_coded = %s "
            + "        and e.encounter_datetime between :startDate and :endDate "
            + "        and e.location_id=:location "
            + "    group by p.patient_id "
            + "    )f ";

    return String.format(query, valueCoded);
  }

  /**
   * Indicador 6 “Total de crianças com outra condição de Risco”
   *
   * @return String
   */
  public static String getChildrenWithOtherRiskConditionIndicator6() {

    String query =
        "select patient_id from ( "
            + "		  select p.patient_id, min(e.encounter_datetime) exposicao_hiv_date "
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
            + "group by p.patient_id "
            + ")f ";

    return query;
  }

  /**
   * Total de crianças que iniciaram Isoniazida na CCR
   *
   * @return String
   */
  public static String getChildrenWhoInitiatedINHTreatment() {

    String query =
        "select patient_id from ( "
            + "		select p.patient_id, min(e.encounter_datetime) inh_date "
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
            + "group by p.patient_id "
            + ")f ";

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
        "select patient_id from ( "
            + "		select p.patient_id, count(e.encounter_id) as numero_consultas "
            + " from patient p "
            + "   inner join encounter e on p.patient_id=e.patient_id "
            + "   inner join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = 93 "
            + "   and o.concept_id=23985 "
            + "   and o.value_coded = 656 "
            + "   and e.encounter_datetime between :startDate - interval 8 month and :endDate "
            + "   and e.location_id=:location "
            + "     group by p.patient_id "
            + "   having count(e.encounter_id) >= 6 "
            + "   )f ";

    return query;
  }

  /**
   * Total de crianças que receberam ATPU
   *
   * @return String
   */
  public static String getChildrenWhithATPU() {

    String query =
        "select patient_id from ( "
            + "	select p.patient_id, min(e.encounter_datetime) atpu_date "
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
            + "group by p.patient_id "
            + ")f ";

    return query;
  }

  /**
   * Total de crianças que receberam CSB/suplemento nutricional
   *
   * @return String
   */
  public static String getChildrenWhoReceivedCSB() {

    String query =
        "select patient_id from ( "
            + "		select p.patient_id, min(e.encounter_datetime) atpu_date "
            + " from patient p "
            + "   inner join encounter e on p.patient_id=e.patient_id "
            + "   inner join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = 93 "
            + "   and o.concept_id=2151 "
            + "   and o.value_coded = 1065 "
            + "   and e.encounter_datetime between :startDate and :endDate "
            + "   and e.location_id=:location "
            + "group by p.patient_id "
            + ")f ";

    return query;
  }

  /**
   * Total de crianças que iniciaram CTZ
   *
   * @return String
   */
  public static String getChildrenWhoInitiatedCTZTreatment() {

    String query =
        "select patient_id from ( "
            + "		select p.patient_id, min(e.encounter_datetime) ctz_date "
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
            + "group by p.patient_id "
            + ")f ";
    return query;
  }

  /**
   * Total de 1º PCR colhido
   *
   * @return String
   */
  public static String getChildrenWithFirsPCRCollected() {

    String query =
        "select patient_id from ( "
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
            + "         group by p.patient_id "
            + "         )f ";

    return query;
  }

  /**
   * Total de crianças expostas ≥9 meses testadas com Teste Rápido de HIV
   *
   * @return String
   */
  public static String getChildrenWithAnyHIVTestResult() {

    String query =
        "select patient_id from ( "
            + "		select p.patient_id, min(e.encounter_datetime) hiv_date "
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
            + "group by p.patient_id "
            + ")f ";

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
        "select patient_id from ( "
            + "		select p.patient_id, min(e.encounter_datetime) hiv_date "
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
            + "group by p.patient_id "
            + ")f ";

    return query;
  }

  /**
   * Filtrando as crianças que tiveram registo de “Transferido para Consulta de Criança Sadia” na
   * “Ficha Resumo de CCR” 165485 = Transferido para Consulta de Criança Sadia 165484 = Transferido
   * para Consultas Integradas 1707 = Abandono 1366 = Obito, 165483 = Transferido para sector de TB
   *
   * @return String
   */
  public static String getChildrenTransferredToXCCRResumo(int valueCoded) {

    String query =
        "select patient_id from ( "
            + "		select p.patient_id, min(e.encounter_datetime) data_abandono "
            + " from patient p "
            + "   inner join encounter e on p.patient_id=e.patient_id "
            + "   inner join obs o on o.encounter_id=e.encounter_id "
            + "   where e.voided=0 "
            + "   and o.voided=0 "
            + "   and p.voided=0 "
            + "   and e.encounter_type = 92 "
            + "   and o.concept_id=1873 "
            + "   and o.value_coded = %s "
            + "   and e.encounter_datetime between :startDate and :endDate "
            + "   and e.location_id=:location "
            + "     group by p.patient_id "
            + "     )f ";

    return String.format(query, valueCoded);
  }

  /**
   * Filtrando as crianças que tiveram registo de “Transferido para Consulta de Criança Sadia” “” na
   * última “Ficha de Seguimento de CCR” 165485 = Transferido para Consulta de Criança Sadia 165484
   * = Transferido para Consultas Integradas 1707 = Abandono 1366 = Obito, 165483 = Transferido para
   * sector de TB
   *
   * @return String
   */
  public static String getChildrenTransferredToXSeguimentoCCR(int valueCoded) {

    String query =
        "	select patient_id from ( "
            + "			select 	sadiaSeguimento.patient_id, data "
            + "from "
            + "	( "
            + "		select 	p.patient_id,max(encounter_datetime) data "
            + "		from 	patient p "
            + "				inner join encounter e on e.patient_id=p.patient_id "
            + "		where 	p.voided=0 and e.voided=0 and e.encounter_type=93 and "
            + "				e.location_id=:location and e.encounter_datetime between :startDate and :endDate "
            + "		group by p.patient_id "
            + "	) 	sadiaSeguimento "
            + "		inner join encounter e on e.patient_id=sadiaSeguimento.patient_id "
            + "		inner join obs o on e.encounter_id=o.encounter_id "
            + " "
            + "	where 	e.voided=0 and o.voided=0 and e.encounter_type=93 and "
            + "			e.encounter_datetime=sadiaSeguimento.data and "
            + "			o.concept_id = 1873 and o.value_coded = %s "
            + "			)f ";

    return String.format(query, valueCoded);
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
            + "				e.location_id=:location and e.encounter_datetime between :startDate and :endDate "
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
   * Filtrando as crianças que tiveram registo de “PTV Mãe” igual a “TARV”
   *
   * @return String
   */
  public static String getChildrenWithMotherInTARV() {

    String query =
        "select patient_id from ( "
            + "	select p.patient_id, min(e.encounter_datetime) data_abandono "
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
            + "     group by p.patient_id "
            + "     )f ";

    return query;
  }

  /**
   * Filtrando as crianças que tiveram registo de “Aleitamento Materno Exclusivo/Misto” igual a
   * “Sim” numa “Ficha de Seguimento de CCR”
   *
   * @return String
   */
  public static String getChildrenWithAleitamentoMaternoExclusivoInFichaCCR(
      int conceptId, int valueCoded) {

    String query =
        "	select patient_id from ( "
            + "			select 	aleitamentoExclusivo.patient_id, data "
            + "from "
            + "	( "
            + "		select 	p.patient_id,encounter_datetime data "
            + "		from 	patient p "
            + "				inner join encounter e on e.patient_id=p.patient_id "
            + "		where 	p.voided=0 and e.voided=0 and e.encounter_type=93 and "
            + "				e.location_id=:location and	e.encounter_datetime between :startDate and :endDate "
            + "	) 	aleitamentoExclusivo "
            + "		inner join encounter e on e.patient_id=aleitamentoExclusivo.patient_id "
            + "		inner join obs o on e.encounter_id=o.encounter_id "
            + " "
            + "	where 	e.voided=0 and o.voided=0 and e.encounter_type=93 and "
            + "			e.encounter_datetime=aleitamentoExclusivo.data and "
            + "			o.concept_id = %s and o.value_coded = %s "
            + "			)f "
            + "		inner join person on person_id = f.patient_id "
            + "		WHERE (TIMESTAMPDIFF(month, birthdate, f.data)) = 5  AND birthdate IS NOT NULL and voided = 0 ";

    return String.format(query, conceptId, valueCoded);
  }

  /**
   * Filtrando as crianças que tiveram registo de “PCR (Data de Colheita)
   *
   * @return String
   */
  public static String getChildrenWithPCRCollected(String idade) {

    String query =
        "select patient_id from ( "
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
            + "         group by p.patient_id "
            + "         )f "
            + "	inner join person on person_id = f.patient_id "
            + "	WHERE (TIMESTAMPDIFF(month, birthdate, f.pcr_date)) %s  AND birthdate IS NOT NULL and voided = 0 ";

    return String.format(query, idade);
  }

  /**
   * Filtrando as crianças que tiveram registo de “PCR (Resultado) igual a “Positivo”
   *
   * @return String
   */
  public static String getChildrenWithPCRResultPositive(String idade) {

    String query =
        "select patient_id from ( "
            + "				select p.patient_id, min(o.obs_datetime) pcr_date "
            + "           from patient p "
            + "             inner join encounter e on p.patient_id=e.patient_id "
            + "             inner join obs o on o.encounter_id=e.encounter_id "
            + "             where e.voided=0 "
            + "             and o.voided=0 "
            + "             and p.voided=0 "
            + "             and e.encounter_type = 93 "
            + "             and o.concept_id=1030 "
            + "    		   and o.value_coded = 703 "
            + "             and o.obs_datetime between :startDate and :endDate "
            + "             and e.location_id=:location "
            + "         group by p.patient_id "
            + "         )f "
            + "	inner join person on person_id = f.patient_id "
            + "	WHERE (TIMESTAMPDIFF(month, birthdate, f.pcr_date)) %s  AND birthdate IS NOT NULL and voided = 0 ";

    return String.format(query, idade);
  }

  /**
   * Filtrando as crianças que tiveram registo de “PCR (Resultado) igual a “Positivo”
   *
   * @return String
   */
  public static String getChildrenWithPCROrHIVResult(int conceptId, int valueCoded) {

    String query =
        "select patient_id from ( "
            + "				select p.patient_id, max(o.obs_datetime) pcr_date "
            + "           from patient p "
            + "             inner join encounter e on p.patient_id=e.patient_id "
            + "             inner join obs o on o.encounter_id=e.encounter_id "
            + "             where e.voided=0 "
            + "             and o.voided=0 "
            + "             and p.voided=0 "
            + "             and e.encounter_type = 93 "
            + "             and o.concept_id=%s "
            + "    		   and o.value_coded = %s "
            + "             and o.obs_datetime between :startDate and :endDate "
            + "             and e.location_id=:location "
            + "         group by p.patient_id "
            + "         )f ";

    return String.format(query, conceptId, valueCoded);
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
            + "	   WHERE (TIMESTAMPDIFF(month, birthdate, firstConsultationCCR.date_first_consultation_ccr)) >= %s  AND birthdate IS NOT NULL and voided = 0 ";

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
            + "	   WHERE (TIMESTAMPDIFF(month, birthdate, firstConsultationCCR.date_first_consultation_ccr)) < %s  AND birthdate IS NOT NULL and voided = 0 ";

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
            + "	   WHERE (TIMESTAMPDIFF(month, birthdate, firstConsultationCCR.date_first_consultation_ccr)) = %s  AND birthdate IS NOT NULL and voided = 0 ";

    return String.format(query, age);
  }
}
