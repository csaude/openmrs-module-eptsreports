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
import org.springframework.stereotype.Component;

@Component
public class ResumoMensalCCRCohortQueries {

  private GenericCohortQueries genericCohortQueries;

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
                "I2", ResumoMensalCCRQueries.getChildrenWithTbHivContactIndicator2()),
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
                "desnutricaoAguda", ResumoMensalCCRQueries.getChildrenWithAcuteMalNutrition()),
            mappings));

    definition.addSearch(
        "DAGM",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "desnutricaoAgudaModerada",
                ResumoMensalCCRQueries.getChildrenWithModerateAcuteMalNutrition()),
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
                "desnutricaoAguda", ResumoMensalCCRQueries.getChildrenWithAcuteMalNutrition()),
            mappings));

    definition.addSearch(
        "DAGG",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "desnutricaoAgudaGrave",
                ResumoMensalCCRQueries.getChildrenWithSevereAcuteMalNutrition()),
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
                "desnutricaoAguda",
                ResumoMensalCCRQueries.getChildrenWithExposureToHIVIndicator5()),
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

    definition.setCompositionString("DI1 AND INH");

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
                "HIV", ResumoMensalCCRQueries.getChildrenExposedAndTestedForHIV()),
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
}
