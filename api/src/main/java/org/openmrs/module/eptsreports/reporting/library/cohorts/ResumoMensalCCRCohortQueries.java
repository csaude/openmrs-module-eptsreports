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

package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.ResumoMensalCCRQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResumoMensalCCRCohortQueries {

  @Autowired private GenericCohortQueries genericCohortQueries;

  /** Indicador 1: Total de 1as consultas */
  public CohortDefinition getChildrenWithFirstConsultationCCRDuringPeriodIndicator1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getChildrenWithFirstConsultationCCRDuringPeriodIndicator1");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getChildrenWithFirstConsultationCCRDuringPeriodIndicator1");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I1",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I1",
                ResumoMensalCCRQueries.getChildrenWithFirstConsultationCCRDuringPeriodIndicator1()),
            mappings));

    definition.setCompositionString("I1");

    return definition;
  }

  /** Indicador 2: Total de crianças com contacto com tuberculose */
  public CohortDefinition getChildrenWithTbHivContactIndicator2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getChildrenWithTbHivContactIndicator2");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getChildrenWithTbHivContactIndicator2");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I2",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I2", ResumoMensalCCRQueries.getChildrenWithXMotivoDeConsulta(1845)),
            mappings));

    definition.setCompositionString("I2");

    return definition;
  }

  /** Indicador 3: Total de crianças com desnutrição aguda moderada */
  public CohortDefinition getNumberOfChildrenWithModerateAcuteMalnutritionIndicator3() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfChildrenWithModerateAcuteMalnutritionIndicator3");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfChildrenWithModerateAcuteMalnutritionIndicator3");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "DAG",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "desnutricaoAguda", ResumoMensalCCRQueries.getChildrenWithXMotivoDeConsulta(1844)),
            mappings));

    definition.addSearch(
        "DAGM",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "desnutricaoAgudaModerada",
                ResumoMensalCCRQueries.getChildrenWithXAcuteMalNutrition(165497)),
            mappings));

    definition.setCompositionString("DAG AND DAGM");

    return definition;
  }

  /** Indicador 4: Total de crianças com desnutrição aguda grave */
  public CohortDefinition getNumberOfChildrenWithSevereAcuteMalnutritionIndicator4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfChildrenWithSevereAcuteMalnutritionIndicator4");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfChildrenWithSevereAcuteMalnutritionIndicator4");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "DAG",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "desnutricaoAguda", ResumoMensalCCRQueries.getChildrenWithXMotivoDeConsulta(1844)),
            mappings));

    definition.addSearch(
        "DAGG",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "desnutricaoAgudaGrave",
                ResumoMensalCCRQueries.getChildrenWithXAcuteMalNutrition(165496)),
            mappings));

    definition.setCompositionString("DAG AND DAGG");

    return definition;
  }

  /** Indicador 5 “Total de crianças com exposição ao HIV” */
  public CohortDefinition getChildrenWithExposureToHIVIndicator5() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("ChildrenWithExposureToHIVIndicator5");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getChildrenWithExposureToHIVIndicator5");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I5",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "desnutricaoAguda", ResumoMensalCCRQueries.getChildrenWithXMotivoDeConsulta(1586)),
            mappings));

    definition.setCompositionString("I5");

    return definition;
  }

  /** Indicador 6 “Total de crianças com outra condição de Risco” */
  public CohortDefinition getChildrenWithOtherRiskConditionIndicator6() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getChildrenWithOtherRiskConditionIndicator6");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getChildrenWithOtherRiskConditionIndicator6");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I6",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I6", ResumoMensalCCRQueries.getChildrenWithOtherRiskConditionIndicator6()),
            mappings));

    definition.setCompositionString("I6");

    return definition;
  }

  /** Indicador 7: Total de crianças que iniciaram Isoniazida na CCR */
  public CohortDefinition getNumberOfChildrenWhoInitiatedINHTreatmentIndicator7() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfChildrenWhoInitiatedINHTreatmentIndicator7");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfChildrenWhoInitiatedINHTreatmentIndicator7");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I1",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I1",
                ResumoMensalCCRQueries.getChildrenWithFirstConsultationCCRDuringPeriodIndicator1()),
            mappings));

    definition.addSearch(
        "INH",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "INH", ResumoMensalCCRQueries.getChildrenWhoInitiatedINHTreatment()),
            mappings));

    definition.setCompositionString("I1 AND INH");

    return definition;
  }

  /** Indicador 8: Total de crianças que receberam ATPU */
  public CohortDefinition getNumberOfChildrenWhithATPUIndicator8() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getNumberOfChildrenWhithATPUIndicator8");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfChildrenWhithATPUIndicator8");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I1",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I1",
                ResumoMensalCCRQueries.getChildrenWithFirstConsultationCCRDuringPeriodIndicator1()),
            mappings));

    definition.addSearch(
        "ATPU",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "ATPU", ResumoMensalCCRQueries.getChildrenWhithATPU()),
            mappings));

    definition.setCompositionString("I1 AND ATPU");

    return definition;
  }

  /** Indicador 9: Total de crianças que receberam CSB/suplemento nutricional */
  public CohortDefinition getNumberOfChildrenWhoReceivedCSBIndicator9() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getNumberOfChildrenWhoReceivedCSBIndicator9");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfChildrenWhoReceivedCSBIndicator9");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I1",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I1",
                ResumoMensalCCRQueries.getChildrenWithFirstConsultationCCRDuringPeriodIndicator1()),
            mappings));

    definition.addSearch(
        "CSB",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CSB", ResumoMensalCCRQueries.getChildrenWhoReceivedCSB()),
            mappings));

    definition.setCompositionString("I1 AND CSB");

    return definition;
  }

  /** Indicador 10 “Total de crianças que iniciaram CTZ < 2 meses de idade” */
  public CohortDefinition getNumberOfChildrenWhoInitiatedCTZTreatmentIndicator10() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfChildrenWhoInitiatedCTZTreatmentIndicator10");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfChildrenWhoInitiatedCTZTreatmentIndicator10");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I1",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I1",
                ResumoMensalCCRQueries.getChildrenWithFirstConsultationCCRDuringPeriodIndicator1()),
            mappings));

    definition.addSearch(
        "CTZ",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CTZ", ResumoMensalCCRQueries.getChildrenWhoInitiatedCTZTreatment()),
            mappings));

    definition.addSearch(
        "AGE",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "AGE", ResumoMensalCCRQueries.findPatientsAgeLessThan(2)),
            mappings));

    definition.setCompositionString("I1 AND CTZ AND AGE");

    return definition;
  }

  /** Indicador 11 “Total de crianças que iniciaram CTZ ≥ 2 meses de idade” */
  public CohortDefinition getNumberOfChildrenWhoInitiatedCTZTreatmentIndicator11() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfChildrenWhoInitiatedCTZTreatmentIndicator11");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfChildrenWhoInitiatedCTZTreatmentIndicator11");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I1",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I1",
                ResumoMensalCCRQueries.getChildrenWithFirstConsultationCCRDuringPeriodIndicator1()),
            mappings));

    definition.addSearch(
        "CTZ",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CTZ", ResumoMensalCCRQueries.getChildrenWhoInitiatedCTZTreatment()),
            mappings));

    definition.addSearch(
        "AGE",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "AGE", ResumoMensalCCRQueries.findPatientsAgeGreaterThan(2)),
            mappings));

    definition.setCompositionString("I1 AND CTZ AND AGE");

    return definition;
  }

  /** Indicador 12 “Total de 1º PCR colhido < 2 meses de idade” */
  public CohortDefinition getNumberOfChildrenWhithTheFirstPCRCollectedIndicator12() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfChildrenWhithTheFirstPCRCollectedIndicator12");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfChildrenWhithTheFirstPCRCollectedIndicator12");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I1",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I1",
                ResumoMensalCCRQueries.getChildrenWithFirstConsultationCCRDuringPeriodIndicator1()),
            mappings));

    definition.addSearch(
        "PCR",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PCR", ResumoMensalCCRQueries.getChildrenWithFirsPCRCollected()),
            mappings));

    definition.addSearch(
        "AGE",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "AGE", ResumoMensalCCRQueries.findPatientsAgeLessThan(2)),
            mappings));

    definition.setCompositionString("I1 AND PCR AND AGE");

    return definition;
  }

  /** Indicador 13 “Total de 1º PCR colhido ≥ 2 meses de idade” */
  public CohortDefinition getNumberOfChildrenWhithTheFirstPCRCollectedIndicator13() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfChildrenWhithTheFirstPCRCollectedIndicator13");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfChildrenWhithTheFirstPCRCollectedIndicator13");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I1",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I1",
                ResumoMensalCCRQueries.getChildrenWithFirstConsultationCCRDuringPeriodIndicator1()),
            mappings));

    definition.addSearch(
        "PCR",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PCR", ResumoMensalCCRQueries.getChildrenWithFirsPCRCollected()),
            mappings));

    definition.addSearch(
        "AGE",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "AGE", ResumoMensalCCRQueries.findPatientsAgeGreaterThan(2)),
            mappings));

    definition.setCompositionString("I1 AND PCR AND AGE");

    return definition;
  }

  /** Indicador 14 “Total de crianças expostas ≥9 meses testadas com Teste Rápido de HIV” */
  public CohortDefinition getNumberOfChildrenExposedIndicator14() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getNumberOfChildrenExposedIndicator14");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfChildrenExposedIndicator14");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "HIV",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "HIV", ResumoMensalCCRQueries.getChildrenWithXMotivoDeConsulta(1586)),
            mappings));

    definition.addSearch(
        "TEST-RESULT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "TEST-RESULT", ResumoMensalCCRQueries.getChildrenWithAnyHIVTestResult()),
            mappings));

    definition.addSearch(
        "AGE",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "AGE", ResumoMensalCCRQueries.findPatientsAgeGreaterThan(9)),
            mappings));

    definition.setCompositionString("HIV AND TEST-RESULT AND AGE");

    return definition;
  }

  /** Indicador 15 “Total de crianças não expostas ao HIV testadas com Teste Rápido de HIV” */
  public CohortDefinition getNumberOfChildrenNotExposedToHIVTestedIndicator15() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getNumberOfChildrenNotExposedToHIVTestedIndicator15");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfChildrenNotExposedToHIVTestedIndicator15");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I1",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I1",
                ResumoMensalCCRQueries.getChildrenWithFirstConsultationCCRDuringPeriodIndicator1()),
            mappings));

    definition.addSearch(
        "TEST-RESULT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "TEST-RESULT", ResumoMensalCCRQueries.getChildrenWithAnyHIVTestResult()),
            mappings));

    definition.addSearch(
        "I5", EptsReportUtils.map(getChildrenWithExposureToHIVIndicator5(), mappings));

    definition.setCompositionString("(I1 AND TEST-RESULT) NOT I5");

    return definition;
  }

  /**
   * Indicador 16 “Total de crianças não expostas ao HIV, testadas com Teste Rápido que tiveram
   * resultado positivo”
   */
  public CohortDefinition
      getNumberOfChildrenNotExposedToHIVTestedWithTestResultPositiveIndicator16() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("NumberOfChildrenNotExposedToHIVTestedWithTestResultPositiveIndicator16");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfChildrenNotExposedToHIVTestedWithTestResultPositiveIndicator16");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I1",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I1",
                ResumoMensalCCRQueries.getChildrenWithFirstConsultationCCRDuringPeriodIndicator1()),
            mappings));

    definition.addSearch(
        "POSITIVE-RESULT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "TEST-RESULT", ResumoMensalCCRQueries.getChildrenWithAnyHIVTestResultPositive()),
            mappings));

    definition.addSearch(
        "I5", EptsReportUtils.map(getChildrenWithExposureToHIVIndicator5(), mappings));

    definition.setCompositionString("(I1 AND POSITIVE-RESULT) NOT I5");

    return definition;
  }

  /** Indicador 20 “Total de crianças com contacto com TB” na coorte de 9 meses */
  public CohortDefinition getChildrenWithTbHivContact9MonthsAgoIndicator20() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("ChildrenWithTbHivContact9MonthsAgoIndicator20");
    final String mappings = "startDate=${startDate-8m},endDate=${endDate-8m},location=${location}";

    definition.setName("getChildrenWithTbHivContact9MonthsAgoIndicator20");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I1",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I1",
                ResumoMensalCCRQueries.getChildrenWithFirstConsultationCCRDuringPeriodIndicator1()),
            mappings));

    definition.addSearch(
        "I2",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I2", ResumoMensalCCRQueries.getChildrenWithXMotivoDeConsulta(1845)),
            mappings));

    definition.setCompositionString("I1 AND I2");

    return definition;
  }

  /** Indicador 21 “Total de crianças que completaram Isoniazida” */
  public CohortDefinition getChildrenWhoCompletedINHTreatmentCoorte9MonthsIndicator21() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getChildrenWhoCompletedINHTreatmentCoorte9MonthsIndicator21");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getChildrenWhoCompletedINHTreatmentCoorte9MonthsIndicator21");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I20", EptsReportUtils.map(getChildrenWithTbHivContact9MonthsAgoIndicator20(), mappings));

    definition.addSearch(
        "INH",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "INH",
                ResumoMensalCCRQueries.getChildrenWhoHave6OrMoreCCRWithINHTreatmentRegistered()),
            mappings));

    definition.setCompositionString("I20 AND INH");

    return definition;
  }

  /** Indicador 22 “Total de crianças referidas para PNCT” */
  public CohortDefinition getChildrenReferredToPNCTIndicator22() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getChildrenReferredToPNCTIndicator22");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCcrResumo =
        "startDate=${startDate-8m},endDate=${endDate-8m},location=${location}";
    final String mappingsCcrSeguimento =
        "startDate=${startDate-8m},endDate=${endDate},location=${location}";

    definition.setName("getChildrenReferredToPNCTIndicator22");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I20", EptsReportUtils.map(getChildrenWithTbHivContact9MonthsAgoIndicator20(), mappings));

    definition.addSearch(
        "CCR-RESUMO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-RESUMO", ResumoMensalCCRQueries.getChildrenTransferredToXCCRResumo(165483)),
            mappingsCcrResumo));

    definition.addSearch(
        "CCR-SEGUIMENTO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-SEGUIMENTO",
                ResumoMensalCCRQueries.getChildrenTransferredToXSeguimentoCCR(165483)),
            mappingsCcrSeguimento));

    definition.setCompositionString("I20 AND (CCR-RESUMO OR CCR-SEGUIMENTO)");

    return definition;
  }

  /** Indicador 23 “Total de crianças que abandonaram” */
  public CohortDefinition getChildrenWhoAbandonedCCRIndicator23() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getChildrenWhoAbandonedCCRIndicator23");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCcrResumo =
        "startDate=${startDate-8m},endDate=${endDate-8m},location=${location}";
    final String mappingsCcrSeguimento =
        "startDate=${startDate-8m},endDate=${endDate},location=${location}";

    definition.setName("getChildrenWhoAbandonedCCRIndicator23");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I20", EptsReportUtils.map(getChildrenWithTbHivContact9MonthsAgoIndicator20(), mappings));

    definition.addSearch(
        "ABANDONO-RESUMO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-RESUMO", ResumoMensalCCRQueries.getChildrenTransferredToXCCRResumo(1707)),
            mappingsCcrResumo));

    definition.addSearch(
        "ABANDONO-SEGUIMENTO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-SEGUIMENTO",
                ResumoMensalCCRQueries.getChildrenTransferredToXSeguimentoCCR(1707)),
            mappingsCcrSeguimento));

    definition.setCompositionString("I20 AND (ABANDONO-RESUMO OR ABANDONO-SEGUIMENTO)");

    return definition;
  }

  /** Indicador 24 “Total de crianças com DAM” */
  public CohortDefinition
      getNumberOfChildrenWithModerateAcuteMalnutritionCoorte9MonthsIndicator24() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getNumberOfChildrenWithModerateAcuteMalnutritionCoorte9MonthsIndicator24");
    final String mappings = "startDate=${startDate-8m},endDate=${endDate-8m},location=${location}";

    definition.setName("getNumberOfChildrenWithModerateAcuteMalnutritionCoorte9MonthsIndicator24");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "DAG",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "desnutricaoAguda", ResumoMensalCCRQueries.getChildrenWithXMotivoDeConsulta(1844)),
            mappings));

    definition.addSearch(
        "DAGM",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "desnutricaoAgudaModerada",
                ResumoMensalCCRQueries.getChildrenWithXAcuteMalNutrition(165497)),
            mappings));

    definition.setCompositionString("DAG AND DAGM");

    return definition;
  }

  /** Indicador 25 “Total de Crianças com DAM recuperadas” */
  public CohortDefinition
      getNumberOfChildrenWithModerateAcuteMalnutritionCuredCoorte9MonthsIndicator25() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName(
        "getNumberOfChildrenWithModerateAcuteMalnutritionCuredCoorte9MonthsIndicator25");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCcrResumo =
        "startDate=${startDate-8m},endDate=${endDate-8m},location=${location}";
    final String mappingsCcrSeguimento =
        "startDate=${startDate-8m},endDate=${endDate},location=${location}";

    definition.setName(
        "getNumberOfChildrenWithModerateAcuteMalnutritionCuredCoorte9MonthsIndicator25");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I24",
        EptsReportUtils.map(
            getNumberOfChildrenWithModerateAcuteMalnutritionCoorte9MonthsIndicator24(), mappings));

    definition.addSearch(
        "CCR-RESUMO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-RESUMO", ResumoMensalCCRQueries.getChildrenTransferredToXCCRResumo(165485)),
            mappingsCcrResumo));

    definition.addSearch(
        "CCR-SEGUIMENTO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-SEGUIMENTO",
                ResumoMensalCCRQueries.getChildrenTransferredToXSeguimentoCCR(165485)),
            mappingsCcrSeguimento));

    definition.setCompositionString("I24 AND (CCR-RESUMO OR CCR-SEGUIMENTO)");

    return definition;
  }

  /** Indicador 26 “Total de Crianças com DAM que abandonaram” */
  public CohortDefinition
      getNumberOfChildrenWithModerateAcuteMalnutritionWhoAbandonedCCRCoorte9MonthsIndicator26() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName(
        "getNumberOfChildrenWithModerateAcuteMalnutritionWhoAbandonedCCRCoorte9MonthsIndicator26");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCcrResumo =
        "startDate=${startDate-8m},endDate=${endDate-8m},location=${location}";
    final String mappingsCcrSeguimento =
        "startDate=${startDate-8m},endDate=${endDate},location=${location}";

    definition.setName(
        "getNumberOfChildrenWithModerateAcuteMalnutritionWhoAbandonedCCRCoorte9MonthsIndicator26");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I24",
        EptsReportUtils.map(
            getNumberOfChildrenWithModerateAcuteMalnutritionCoorte9MonthsIndicator24(), mappings));

    definition.addSearch(
        "ABANDONO-RESUMO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-RESUMO", ResumoMensalCCRQueries.getChildrenTransferredToXCCRResumo(1707)),
            mappingsCcrResumo));

    definition.addSearch(
        "ABANDONO-SEGUIMENTO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-SEGUIMENTO",
                ResumoMensalCCRQueries.getChildrenTransferredToXSeguimentoCCR(1707)),
            mappingsCcrSeguimento));

    definition.setCompositionString("I24 AND (ABANDONO-RESUMO OR ABANDONO-SEGUIMENTO)");

    return definition;
  }

  /** Indicador 27 “Total de crianças com DAG” */
  public CohortDefinition getNumberOfChildrenWithSevereAcuteMalnutritionCoorte9MonthsIndicator27() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getNumberOfChildrenWithSevereAcuteMalnutritionCoorte9MonthsIndicator27");
    final String mappings = "startDate=${startDate-8m},endDate=${endDate-8m},location=${location}";

    definition.setName("getNumberOfChildrenWithSevereAcuteMalnutritionCoorte9MonthsIndicator27");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "DAG",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "desnutricaoAguda", ResumoMensalCCRQueries.getChildrenWithXMotivoDeConsulta(1844)),
            mappings));

    definition.addSearch(
        "DAGG",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "desnutricaoAgudaGrave",
                ResumoMensalCCRQueries.getChildrenWithXAcuteMalNutrition(165496)),
            mappings));

    definition.setCompositionString("DAG AND DAGG");

    return definition;
  }

  /** Indicador 28 “Total de crianças com DAG que foram referidas para internamento” */
  public CohortDefinition
      getNumberOfChildrenWithDAGReferredForHospitalizationCoorte9MonthsIndicator28() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName(
        "getNumberOfChildrenWithDAGReferredForHospitalizationCoorte9MonthsIndicator28");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCcrSeguimento =
        "startDate=${startDate-8m},endDate=${endDate},location=${location}";

    definition.setName(
        "getNumberOfChildrenWithDAGReferredForHospitalizationCoorte9MonthsIndicator28");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I27",
        EptsReportUtils.map(
            getNumberOfChildrenWithSevereAcuteMalnutritionCoorte9MonthsIndicator27(), mappings));

    definition.addSearch(
        "INTERNAMENTO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "desnutricaoAgudaGrave",
                ResumoMensalCCRQueries.getChildrenWithDAGReferredForHospitalization()),
            mappingsCcrSeguimento));

    definition.setCompositionString("I27 AND INTERNAMENTO");

    return definition;
  }

  /** Indicador 29 “Total de Crianças com DAG recuperadas” */
  public CohortDefinition
      getNumberOfChildrenWithSevereAcuteMalnutritionCuredCoorte9MonthsIndicator29() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName(
        "getNumberOfChildrenWithSevereAcuteMalnutritionCuredCoorte9MonthsIndicator29");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCcrResumo =
        "startDate=${startDate-8m},endDate=${endDate-8m},location=${location}";
    final String mappingsCcrSeguimento =
        "startDate=${startDate-8m},endDate=${endDate},location=${location}";

    definition.setName(
        "getNumberOfChildrenWithSevereAcuteMalnutritionCuredCoorte9MonthsIndicator29");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I27",
        EptsReportUtils.map(
            getNumberOfChildrenWithSevereAcuteMalnutritionCoorte9MonthsIndicator27(), mappings));

    definition.addSearch(
        "CCR-RESUMO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-RESUMO", ResumoMensalCCRQueries.getChildrenTransferredToXCCRResumo(165485)),
            mappingsCcrResumo));

    definition.addSearch(
        "CCR-SEGUIMENTO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-SEGUIMENTO",
                ResumoMensalCCRQueries.getChildrenTransferredToXSeguimentoCCR(165485)),
            mappingsCcrSeguimento));

    definition.setCompositionString("I27 AND (CCR-RESUMO OR CCR-SEGUIMENTO)");

    return definition;
  }

  /** Indicador 30 “Total de crianças com DAG que abandonaram” */
  public CohortDefinition
      getNumberOfChildrenWithSevereAcuteMalnutritionWhoAbandonedCCRCoorte9MonthsIndicator30() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName(
        "getNumberOfChildrenWithSevereAcuteMalnutritionWhoAbandonedCCRCoorte9MonthsIndicator30");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCcrResumo =
        "startDate=${startDate-8m},endDate=${endDate-8m},location=${location}";
    final String mappingsCcrSeguimento =
        "startDate=${startDate-8m},endDate=${endDate},location=${location}";

    definition.setName(
        "getNumberOfChildrenWithSevereAcuteMalnutritionWhoAbandonedCCRCoorte9MonthsIndicator30");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I27",
        EptsReportUtils.map(
            getNumberOfChildrenWithSevereAcuteMalnutritionCoorte9MonthsIndicator27(), mappings));

    definition.addSearch(
        "CCR-RESUMO-ABANDONO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-RESUMO", ResumoMensalCCRQueries.getChildrenTransferredToXCCRResumo(1707)),
            mappingsCcrResumo));

    definition.addSearch(
        "CCR-SEGUIMENTO-ABANDONO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-SEGUIMENTO",
                ResumoMensalCCRQueries.getChildrenTransferredToXSeguimentoCCR(1707)),
            mappingsCcrSeguimento));

    definition.setCompositionString("I27 AND (CCR-RESUMO-ABANDONO OR CCR-SEGUIMENTO-ABANDONO)");

    return definition;
  }

  /** Indicador 31 “Total de crianças com DAG que foram óbito” */
  public CohortDefinition
      getNumberOfChildrenWithSevereAcuteMalnutritionWhoDiedCCRCoorte9MonthsIndicator31() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName(
        "getNumberOfChildrenWithSevereAcuteMalnutritionWhoDiedCCRCoorte9MonthsIndicator31");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCcrResumo =
        "startDate=${startDate-8m},endDate=${endDate-8m},location=${location}";
    final String mappingsCcrSeguimento =
        "startDate=${startDate-8m},endDate=${endDate},location=${location}";

    definition.setName(
        "getNumberOfChildrenWithSevereAcuteMalnutritionWhoDiedCCRCoorte9MonthsIndicator31");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I27",
        EptsReportUtils.map(
            getNumberOfChildrenWithSevereAcuteMalnutritionCoorte9MonthsIndicator27(), mappings));

    definition.addSearch(
        "CCR-RESUMO-OBITO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-RESUMO", ResumoMensalCCRQueries.getChildrenTransferredToXCCRResumo(1366)),
            mappingsCcrResumo));

    definition.addSearch(
        "CCR-SEGUIMENTO-OBITO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-SEGUIMENTO",
                ResumoMensalCCRQueries.getChildrenTransferredToXSeguimentoCCR(1366)),
            mappingsCcrSeguimento));

    definition.setCompositionString("I27 AND (CCR-RESUMO-OBITO OR CCR-SEGUIMENTO-OBITO)");

    return definition;
  }

  /** Indicador 32 “Total de crianças expostas” */
  public CohortDefinition getChildrenWithExposureToHIVCoorte9MonthsIndicator32() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getChildrenWithExposureToHIVCoorte9MonthsIndicator32");
    final String mappings = "startDate=${startDate-8m},endDate=${endDate-8m},location=${location}";

    definition.setName("getChildrenWithExposureToHIVCoorte9MonthsIndicator32");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I1",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I1",
                ResumoMensalCCRQueries.getChildrenWithFirstConsultationCCRDuringPeriodIndicator1()),
            mappings));

    definition.addSearch(
        "I5",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I5", ResumoMensalCCRQueries.getChildrenWithXMotivoDeConsulta(1586)),
            mappings));

    definition.setCompositionString("I1 AND I5");

    return definition;
  }

  /** Indicador 33 “Total de crianças expostas com 5 meses de idade e com mãe em TARV” */
  public CohortDefinition getChildrenExposureToHIVCoorte9MonthsWithTheMotherInPTVIndicator33() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getChildrenExposureToHIVCoorte9MonthsWithTheMotherInPTVIndicator33");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte9Months =
        "startDate=${startDate-8m},endDate=${endDate-8m},location=${location}";

    definition.setName("getChildrenExposureToHIVCoorte9MonthsWithTheMotherInPTVIndicator33");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I32",
        EptsReportUtils.map(getChildrenWithExposureToHIVCoorte9MonthsIndicator32(), mappings));

    definition.addSearch(
        "PTV",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PTV", ResumoMensalCCRQueries.getChildrenWithMotherInTARV()),
            mappingsCoorte9Months));

    definition.addSearch(
        "AGE-5-MONTHS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "AGE", ResumoMensalCCRQueries.findPatientsWithAgeEqualTo(5)),
            mappingsCoorte9Months));

    definition.setCompositionString("I32 AND PTV AND AGE-5-MONTHS");

    return definition;
  }

  /** Indicador 34 “Total de crianças expostas com aleitamento materno exclusivo aos 5 meses” */
  public CohortDefinition getChildrenExposedToExclusiveBreastfeedingIndicator34() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getChildrenExposedToExclusiveBreastfeedingIndicator34");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte9Months =
        "startDate=${startDate-8m},endDate=${endDate},location=${location}";

    definition.setName("getChildrenExposedToExclusiveBreastfeedingIndicator34");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I32",
        EptsReportUtils.map(getChildrenWithExposureToHIVCoorte9MonthsIndicator32(), mappings));

    definition.addSearch(
        "EXCLUSIVO-5-MONTHS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "AGE",
                ResumoMensalCCRQueries.getChildrenWithAleitamentoMaternoExclusivoInFichaCCR(
                    5526, 1065)),
            mappingsCoorte9Months));

    definition.setCompositionString("I32 AND EXCLUSIVO-5-MONTHS");

    return definition;
  }

  /** Indicador 35 “Total de crianças expostas em Aleitamento Misto aos 5 meses” */
  public CohortDefinition getChildrenExposedToMixedBreastfeedingIndicator35() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getChildrenExposedToExclusiveBreastfeedingIndicator34");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte9Months =
        "startDate=${startDate-8m},endDate=${endDate},location=${location}";

    definition.setName("getChildrenExposedToExclusiveBreastfeedingIndicator34");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I32",
        EptsReportUtils.map(getChildrenWithExposureToHIVCoorte9MonthsIndicator32(), mappings));

    definition.addSearch(
        "MISTO-5-MONTHS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "AGE",
                ResumoMensalCCRQueries.getChildrenWithAleitamentoMaternoExclusivoInFichaCCR(
                    6046, 1065)),
            mappingsCoorte9Months));

    definition.setCompositionString("I32 AND MISTO-5-MONTHS");

    return definition;
  }

  /** Indicador 36 “Total de crianças expostas que receberam ARV aos 5 meses” */
  public CohortDefinition getChildrenExposedWhoReceivedARVIndicator36() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getChildrenExposedWhoReceivedARVIndicator36");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte9Months =
        "startDate=${startDate-8m},endDate=${endDate},location=${location}";

    definition.setName("getChildrenExposedWhoReceivedARVIndicator36");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I32",
        EptsReportUtils.map(getChildrenWithExposureToHIVCoorte9MonthsIndicator32(), mappings));

    definition.addSearch(
        "NEVIRAPINA",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "NEVIRAPINA",
                ResumoMensalCCRQueries.getChildrenWithAleitamentoMaternoExclusivoInFichaCCR(
                    631, 1065)),
            mappingsCoorte9Months));

    definition.addSearch(
        "ZIDOVUDINA",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "ZIDOVUDINA",
                ResumoMensalCCRQueries.getChildrenWithAleitamentoMaternoExclusivoInFichaCCR(
                    791, 1065)),
            mappingsCoorte9Months));

    definition.setCompositionString("I32 AND (NEVIRAPINA OR ZIDOVUDINA)");

    return definition;
  }

  /** Indicador 37 “Total de PCR colhido <2 meses de idade” */
  public CohortDefinition getTotalChildrenWhoReceivedPCRIndicator37() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getTotalChildrenWhoReceivedPCRIndicator37");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte9Months =
        "startDate=${startDate-8m},endDate=${endDate},location=${location}";

    definition.setName("getTotalChildrenWhoReceivedPCRIndicator37");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I32",
        EptsReportUtils.map(getChildrenWithExposureToHIVCoorte9MonthsIndicator32(), mappings));

    definition.addSearch(
        "PCR-MENOR-2MONTHS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PCR-MENOR-2MONTHS", ResumoMensalCCRQueries.getChildrenWithPCRCollected("< 2")),
            mappingsCoorte9Months));

    definition.setCompositionString("I32 AND PCR-MENOR-2MONTHS");

    return definition;
  }

  /** Indicador 38 “Total de PCR colhido >=2 meses de idade” */
  public CohortDefinition getTotalChildrenWhoReceivedPCRIndicator38() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getTotalChildrenWhoReceivedPCRIndicator38");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte9Months =
        "startDate=${startDate-8m},endDate=${endDate},location=${location}";

    definition.setName("getTotalChildrenWhoReceivedPCRIndicator38");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I32",
        EptsReportUtils.map(getChildrenWithExposureToHIVCoorte9MonthsIndicator32(), mappings));

    definition.addSearch(
        "PCR-MAIOR-2MONTHS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PCR-MAIOR-2MONTHS", ResumoMensalCCRQueries.getChildrenWithPCRCollected(">= 2")),
            mappingsCoorte9Months));

    definition.setCompositionString("I32 AND PCR-MAIOR-2MONTHS");

    return definition;
  }

  /**
   * Indicador 39 “Total de crianças com resultados PCR positivo <2 meses de idade” da seguinte
   * forma:
   */
  public CohortDefinition getTotalChildrenWhoReceivedPCRResultPositiveIndicator39() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getTotalChildrenWhoReceivedPCRResultPositiveIndicator39");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte9Months =
        "startDate=${startDate-8m},endDate=${endDate},location=${location}";

    definition.setName("getTotalChildrenWhoReceivedPCRResultPositiveIndicator39");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I32",
        EptsReportUtils.map(getChildrenWithExposureToHIVCoorte9MonthsIndicator32(), mappings));

    definition.addSearch(
        "PCRPOSIVITO-MENOR-2MONTHS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PCRPOSIVITO-MENOR-2MONTHS",
                ResumoMensalCCRQueries.getChildrenWithPCRResultPositive("< 2")),
            mappingsCoorte9Months));

    definition.setCompositionString("I32 AND PCRPOSIVITO-MENOR-2MONTHS");

    return definition;
  }

  /** Indicador 40 “Total de crianças com resultados PCR positivo >= 2 meses de idade” */
  public CohortDefinition getTotalChildrenWhoReceivedPCRResultPositiveIndicator40() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getTotalChildrenWhoReceivedPCRResultPositiveIndicator40");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCoorte9Months =
        "startDate=${startDate-8m},endDate=${endDate},location=${location}";

    definition.setName("getTotalChildrenWhoReceivedPCRResultPositiveIndicator40");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I32",
        EptsReportUtils.map(getChildrenWithExposureToHIVCoorte9MonthsIndicator32(), mappings));

    definition.addSearch(
        "PCRPOSIVITO-MAIOR-2MONTHS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PCRPOSIVITO-MAIOR-2MONTHS",
                ResumoMensalCCRQueries.getChildrenWithPCRResultPositive(">= 2")),
            mappingsCoorte9Months));

    definition.setCompositionString("I32 AND PCRPOSIVITO-MAIOR-2MONTHS");

    return definition;
  }

  /** Indicador 41 “Total de crianças expostas” */
  public CohortDefinition getChildrenWithExposureToHIVCoorte18MonthsIndicator41() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getChildrenWithExposureToHIVCoorte18MonthsIndicator41");
    final String mappings =
        "startDate=${startDate-17m},endDate=${endDate-17m},location=${location}";

    definition.setName("getChildrenWithExposureToHIVCoorte18MonthsIndicator41");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I1",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I1",
                ResumoMensalCCRQueries.getChildrenWithFirstConsultationCCRDuringPeriodIndicator1()),
            mappings));

    definition.addSearch(
        "I5",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I5", ResumoMensalCCRQueries.getChildrenWithXMotivoDeConsulta(1586)),
            mappings));

    definition.setCompositionString("I1 AND I5");

    return definition;
  }

  /** Indicador 42 “Total de crianças expostas com resultado definitivo de HIV positivo” */
  public CohortDefinition
      getChildrenWithExposureToHIVWithDefinitiveHIVResultPositiveCoorte18MonthsIndicator42() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName(
        "getChildrenWithExposureToHIVWithDefinitiveHIVResultPositiveCoorte18MonthsIndicator42");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCcrSeguimento =
        "startDate=${startDate-17m},endDate=${endDate},location=${location}";

    definition.setName(
        "getChildrenWithExposureToHIVWithDefinitiveHIVResultPositiveCoorte18MonthsIndicator42");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I41",
        EptsReportUtils.map(
            this.getChildrenWithExposureToHIVCoorte18MonthsIndicator41(), mappings));

    definition.addSearch(
        "PCR-POSITIVO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PCR", ResumoMensalCCRQueries.getChildrenWithPCROrHIVResult(1030, 703)),
            mappingsCcrSeguimento));

    definition.addSearch(
        "HIV-POSITIVO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "HIV", ResumoMensalCCRQueries.getChildrenWithPCROrHIVResult(1040, 703)),
            mappingsCcrSeguimento));

    definition.setCompositionString("I41 AND (PCR-POSITIVO OR HIV-POSITIVO)");

    return definition;
  }

  /** Indicador 43 “Total de crianças expostas com resultado definitivo de HIV negativo” */
  public CohortDefinition
      getChildrenWithExposureToHIVWithDefinitiveHIVResultNegativeCoorte18MonthsIndicator43() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName(
        "getChildrenWithExposureToHIVWithDefinitiveHIVResultNegativeCoorte18MonthsIndicator43");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCcrSeguimento =
        "startDate=${startDate-17m},endDate=${endDate},location=${location}";

    definition.setName(
        "getChildrenWithExposureToHIVWithDefinitiveHIVResultNegativeCoorte18MonthsIndicator43");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I41",
        EptsReportUtils.map(
            this.getChildrenWithExposureToHIVCoorte18MonthsIndicator41(), mappings));

    definition.addSearch(
        "PCR-NEGATIVO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PCR", ResumoMensalCCRQueries.getChildrenWithPCROrHIVResult(1030, 664)),
            mappingsCcrSeguimento));

    definition.addSearch(
        "HIV-NEGATIVO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "HIV", ResumoMensalCCRQueries.getChildrenWithPCROrHIVResult(1040, 664)),
            mappingsCcrSeguimento));

    definition.setCompositionString("I41 AND (PCR-NEGATIVO OR HIV-NEGATIVO)");

    return definition;
  }

  /** Indicador 44 “Total de crianças expostas transferidas para a Consulta Criança Sadia” */
  public CohortDefinition getNumberOfChildrenExpousedAndCuredCoorte18MonthsIndicator44() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getNumberOfChildrenExpousedAndCuredCoorte18MonthsIndicator44");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCcrResumo =
        "startDate=${startDate-17m},endDate=${endDate-17m},location=${location}";
    final String mappingsCcrSeguimento =
        "startDate=${startDate-17m},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfChildrenExpousedAndCuredCoorte18MonthsIndicator44");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I41",
        EptsReportUtils.map(
            this.getChildrenWithExposureToHIVCoorte18MonthsIndicator41(), mappings));

    definition.addSearch(
        "CCR-RESUMO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-RESUMO", ResumoMensalCCRQueries.getChildrenTransferredToXCCRResumo(165485)),
            mappingsCcrResumo));

    definition.addSearch(
        "CCR-SEGUIMENTO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-SEGUIMENTO",
                ResumoMensalCCRQueries.getChildrenTransferredToXSeguimentoCCR(165485)),
            mappingsCcrSeguimento));

    definition.setCompositionString("I41 AND (CCR-RESUMO OR CCR-SEGUIMENTO)");

    return definition;
  }

  /** Indicador 45 “Total de crianças expostas transferidas para as Consultas Integradas” */
  public CohortDefinition
      getNumberOfChildrenExpousedAndTransferredToIntegratedConsultationsCoorte18MonthsIndicator45() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName(
        "getNumberOfChildrenExpousedAndTransferredToIntegratedConsultationsCoorte18MonthsIndicator45");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCcrResumo =
        "startDate=${startDate-17m},endDate=${endDate-17m},location=${location}";
    final String mappingsCcrSeguimento =
        "startDate=${startDate-17m},endDate=${endDate},location=${location}";

    definition.setName(
        "getNumberOfChildrenExpousedAndTransferredToIntegratedConsultationsCoorte18MonthsIndicator45");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I41",
        EptsReportUtils.map(
            this.getChildrenWithExposureToHIVCoorte18MonthsIndicator41(), mappings));

    definition.addSearch(
        "INTEGRADAS-RESUMO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-RESUMO", ResumoMensalCCRQueries.getChildrenTransferredToXCCRResumo(165484)),
            mappingsCcrResumo));

    definition.addSearch(
        "INTEGRADAS-SEGUIMENTO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-SEGUIMENTO",
                ResumoMensalCCRQueries.getChildrenTransferredToXSeguimentoCCR(165484)),
            mappingsCcrSeguimento));

    definition.setCompositionString("I41 AND (INTEGRADAS-RESUMO OR INTEGRADAS-SEGUIMENTO)");

    return definition;
  }

  /** Indicador 46 “Total de crianças expostas que abandonaram” */
  public CohortDefinition
      getNumberOfChildrenExpousedAndRegisteredAsAbandonoCoorte18MonthsIndicator46() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName(
        "getNumberOfChildrenExpousedAndRegisteredAsAbandonoCoorte18MonthsIndicator46");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCcrResumo =
        "startDate=${startDate-17m},endDate=${endDate-17m},location=${location}";
    final String mappingsCcrSeguimento =
        "startDate=${startDate-17m},endDate=${endDate},location=${location}";

    definition.setName(
        "getNumberOfChildrenExpousedAndRegisteredAsAbandonoCoorte18MonthsIndicator46");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I41",
        EptsReportUtils.map(
            this.getChildrenWithExposureToHIVCoorte18MonthsIndicator41(), mappings));

    definition.addSearch(
        "ABANDONO-RESUMO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-RESUMO", ResumoMensalCCRQueries.getChildrenTransferredToXCCRResumo(1707)),
            mappingsCcrResumo));

    definition.addSearch(
        "ABANDONO-SEGUIMENTO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-SEGUIMENTO",
                ResumoMensalCCRQueries.getChildrenTransferredToXSeguimentoCCR(1707)),
            mappingsCcrSeguimento));

    definition.setCompositionString("I41 AND (ABANDONO-RESUMO OR ABANDONO-SEGUIMENTO)");

    return definition;
  }

  /** Indicador 47 “Total de crianças expostas que foram óbito” */
  public CohortDefinition
      getNumberOfChildrenExpousedAndRegisteredAsDeadCoorte18MonthsIndicator47() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getNumberOfChildrenExpousedAndRegisteredAsDeadCoorte18MonthsIndicator47");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    final String mappingsCcrResumo =
        "startDate=${startDate-17m},endDate=${endDate-17m},location=${location}";
    final String mappingsCcrSeguimento =
        "startDate=${startDate-17m},endDate=${endDate},location=${location}";

    definition.setName("getNumberOfChildrenExpousedAndRegisteredAsDeadCoorte18MonthsIndicator47");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I41",
        EptsReportUtils.map(
            this.getChildrenWithExposureToHIVCoorte18MonthsIndicator41(), mappings));

    definition.addSearch(
        "OBITO-RESUMO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-RESUMO", ResumoMensalCCRQueries.getChildrenTransferredToXCCRResumo(1366)),
            mappingsCcrResumo));

    definition.addSearch(
        "OBITO-SEGUIMENTO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CCR-SEGUIMENTO",
                ResumoMensalCCRQueries.getChildrenTransferredToXSeguimentoCCR(1366)),
            mappingsCcrSeguimento));

    definition.setCompositionString("I41 AND (OBITO-RESUMO OR OBITO-SEGUIMENTO)");

    return definition;
  }
}
