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
import org.openmrs.module.eptsreports.reporting.library.queries.ResumoMensalCETAQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResumoMensalCETACohortQueries {

  @Autowired private GenericCohortQueries genericCohortQueries;

  /** Indicador 0: Nr de Pacientes que reunem critérios para o rastreio FICA-BEM (RF6) */
  public CohortDefinition getPatientsIndicator0() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWithCriteriasForTheFICABEMScreening");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWithCriteriasForTheFICABEMScreening");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "RF7",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "RF7", ResumoMensalCETAQueries.findPatientsWhoWereTrackedUsingTheFICABEMForm()),
            mappings));

    definition.addSearch("RF32", EptsReportUtils.map(getPatientsRF32(), mappings));

    definition.addSearch("RF33", EptsReportUtils.map(getPatientsRF33(), mappings));

    definition.addSearch("RF34", EptsReportUtils.map(getPatientsRF34(), mappings));

    definition.addSearch("RF35", EptsReportUtils.map(getPatientsRF35(), mappings));

    definition.addSearch(
        "FB-12MONTHS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "FB-12MONTHS", ResumoMensalCETAQueries.findPatientsWhithFicaBemInTheLast12Months()),
            mappings));

    definition.setCompositionString("RF7 OR ((RF32 OR RF33 OR RF34 OR RF35) NOT FB-12MONTHS)");

    return definition;
  }

  /** Indicador 1: Nr de Pacientes rastreados usando o FICA-BEM (RF7) */
  public CohortDefinition getPatientsIndicator1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsScreenedUsingFichaFICABEM");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsScreenedUsingFichaFICABEM");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "RF7",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "RF7", ResumoMensalCETAQueries.findPatientsWhoWereTrackedUsingTheFICABEMForm()),
            mappings));

    definition.setCompositionString("RF7");

    return definition;
  }

  /** Indicador 2 - Nr de Pacientes com resultado Positivo no FICA-BEM */
  public CohortDefinition getPatientsIndicator2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWithPositiveResultInFICABEMForm");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWithPositiveResultInFICABEMForm");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I1", EptsReportUtils.map(getPatientsIndicator1(), mappings));

    definition.addSearch(
        "DMC-DMG",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "DMC-DMG", ResumoMensalCETAQueries.findPatientsWithPositiveResultInFICABEMForm()),
            mappings));

    definition.setCompositionString("I1 AND DMC-DMG");

    return definition;
  }

  /**
   * RF9 - Indicador 3 – Nr de Pacientes referidos para o seguimento de Doença Mental Grave
   * (psiquiatria/fluxo normal)
   */
  public CohortDefinition getPatientsIndicator3() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsReferredForFollowUpSevereMentalIllness");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsReferredForFollowUpSevereMentalIllness");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I2", EptsReportUtils.map(getPatientsIndicator2(), mappings));

    definition.addSearch(
        "DMG",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "DMG", ResumoMensalCETAQueries.findPatientsWithDMGOrDMCResultInFICABEMForm(165466)),
            mappings));

    definition.setCompositionString("I2 AND DMG");

    return definition;
  }

  /** RF10 - Indicador 4 – Nr de Pacientes com resultado positivo para Epilepsia */
  public CohortDefinition getPatientsIndicator4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWithPositiveResultForEpilepsy");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWithPositiveResultForEpilepsy");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I2", EptsReportUtils.map(getPatientsIndicator2(), mappings));

    definition.addSearch(
        "EPILEPSIA",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "EPILEPSIA",
                ResumoMensalCETAQueries.findPatientsWithDMGOrDMCResultInFICABEMForm(155)),
            mappings));

    definition.setCompositionString("I2 AND EPILEPSIA");

    return definition;
  }

  /**
   * RF11 - Indicador 5 – Nr de Pacientes referidos para o seguimento de Doença Mental Comum
   * (Psicólogo)
   */
  public CohortDefinition getPatientsIndicator5() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsReferredForFollowUpCommonMentalIllness");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsReferredForFollowUpCommonMentalIllness");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I2", EptsReportUtils.map(getPatientsIndicator2(), mappings));

    definition.addSearch(
        "DMC",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "DMC", ResumoMensalCETAQueries.findPatientsWithDMGOrDMCResultInFICABEMForm(165465)),
            mappings));

    definition.setCompositionString("I2 AND DMC");

    return definition;
  }

  /** RF12 - Indicador 6 - Nr de Pacientes que iniciaram o tratamento de SM - CETA */
  public CohortDefinition getPatientsIndicator6() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoInitiatedTreatmentCETA");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoInitiatedTreatmentCETA");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I6",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I6", ResumoMensalCETAQueries.findPatientsWhoInitiatedSMTreatment()),
            mappings));

    definition.setCompositionString("I6");

    return definition;
  }

  /** RF13 - Indicador 7: Nr de Pacientes em seguimento no CETA até ao final do mês */
  public CohortDefinition getPatientsIndicator7() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsInFollowUpCETA");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsInFollowUpCETA");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "I7",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I7", ResumoMensalCETAQueries.findPatientsInFollowUpCETAUntilThEndOfTheMonth()),
            mappings));

    definition.setCompositionString("I7");

    return definition;
  }

  /** RF14 - Indicador 8 - Nr de Pacientes com Ideação de Suicídio na entrada */
  public CohortDefinition getPatientsIndicator8() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWithAtLeastOneSuicideAttempt");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWithAtLeastOneSuicideAttempt");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I6", EptsReportUtils.map(getPatientsIndicator6(), mappings));

    definition.addSearch(
        "SUICIDE-IDEATION",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "SUICIDE-IDEATION",
                ResumoMensalCETAQueries.findPatientsWithAtLeastOneSuicideIdeation()),
            mappings));

    definition.setCompositionString("I6 AND SUICIDE-IDEATION");

    return definition;
  }

  /**
   * RF15 - Indicador 9 - Número de Pacientes com, pelo menos, uma Tentativa de Suicídio à entrada
   */
  public CohortDefinition getPatientsIndicator9() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWithAtLeastOneSuicideAttempt");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWithAtLeastOneSuicideAttempt");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I6", EptsReportUtils.map(getPatientsIndicator6(), mappings));

    definition.addSearch(
        "I9",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I9", ResumoMensalCETAQueries.findPatientsWithAtLeastOneSuicideAttempt()),
            mappings));

    definition.setCompositionString("I6 AND I9");

    return definition;
  }

  /** RF16 - Indicador 10 – Nr de Pacientes com Ideação de Homicídio a entrada */
  public CohortDefinition getPatientsIndicator10() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWithAtLeastOneHomicideIdeation");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWithAtLeastOneHomicideIdeation");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I6", EptsReportUtils.map(getPatientsIndicator6(), mappings));

    definition.addSearch(
        "HOMICIDE-IDEATION",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "HOMICIDE-IDEATION",
                ResumoMensalCETAQueries.findPatientsWithAtLeastOneHomicideIdeation()),
            mappings));

    definition.setCompositionString("I6 AND HOMICIDE-IDEATION");

    return definition;
  }

  /** RF17 - Indicador 11 – Nr de Pacientes com Tentativa de Homicídio a entrada */
  public CohortDefinition getPatientsIndicator11() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWithAtLeastOneHomicideAttempt");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWithAtLeastOneHomicideAttempt");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I6", EptsReportUtils.map(getPatientsIndicator6(), mappings));

    definition.addSearch(
        "HOMICIDE-ATTEMPT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "HOMICIDE-ATTEMPT",
                ResumoMensalCETAQueries.findPatientsWithAtLeastOneHomicideAttempt()),
            mappings));

    definition.setCompositionString("I6 AND HOMICIDE-ATTEMPT");

    return definition;
  }

  /**
   * RF18 - Indicador 12 - Nr de pacientes com sintomas de depressão
   *
   * @return
   */
  public CohortDefinition getPatientsIndicator12() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWithSymptomsOfDepression");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWithSymptomsOfDepression");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I6", EptsReportUtils.map(getPatientsIndicator6(), mappings));

    definition.addSearch(
        "I12",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I12", ResumoMensalCETAQueries.findPatientsWithSymptomsOfDepression()),
            mappings));

    definition.setCompositionString("I6 AND I12");

    return definition;
  }

  /**
   * RF19 - Indicador 13 – Nr de pacientes com sintomas de ansiedade
   *
   * @return
   */
  public CohortDefinition getPatientsIndicator13() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWithSymptomsOfAnxiety");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWithSymptomsOfAnxiety");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I6", EptsReportUtils.map(getPatientsIndicator6(), mappings));

    definition.addSearch(
        "I13",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I13", ResumoMensalCETAQueries.findPatientsWithSymptomsOfAnxiety()),
            mappings));

    definition.setCompositionString("I6 AND I13");

    return definition;
  }

  /** RF20 - Indicador 14 - Nr de pacientes com trauma */
  public CohortDefinition getPatientsIndicator14() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWithTrauma");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWithTrauma");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I6", EptsReportUtils.map(getPatientsIndicator6(), mappings));

    definition.addSearch(
        "I14",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I14", ResumoMensalCETAQueries.findPatientsWithTrauma()),
            mappings));

    definition.setCompositionString("I6 AND I14");

    return definition;
  }

  /** RF21 - Indicador 15 - Nr de pacientes que consomem abusivamente bebidas alcoólicas */
  public CohortDefinition getPatientsIndicator15() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoAbuseAlcohol");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoAbuseAlcohol");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I6", EptsReportUtils.map(getPatientsIndicator6(), mappings));

    definition.addSearch(
        "I15",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I15", ResumoMensalCETAQueries.findPatientsWhoAbuseAlcohol()),
            mappings));

    definition.setCompositionString("I6 AND I15");

    return definition;
  }

  /**
   * RF22 - Indicador 16 - Nr de pacientes que consomem outras substâncias psicoactivas (ex.
   * Canabis, marijuana, etc)
   */
  public CohortDefinition getPatientsIndicator16() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoConsumeOtherPsychoactiveSubstances");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoConsumeOtherPsychoactiveSubstances");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I6", EptsReportUtils.map(getPatientsIndicator6(), mappings));

    definition.addSearch(
        "I16",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I16", ResumoMensalCETAQueries.findPatientsWhoConsumeOtherPsychoactiveSubstances()),
            mappings));

    definition.setCompositionString("I6 AND I16");

    return definition;
  }

  /** RF23 - Indicador 17- Nr de pacientes que interromperam o tratamento */
  public CohortDefinition getPatientsIndicator17() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoConsumeOtherPsychoactiveSubstances");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoConsumeOtherPsychoactiveSubstances");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "CETA-89",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CETA-89",
                ResumoMensalCETAQueries.findPatientsWithFichaSeguimentoCETA89DaysBeforeEndDate()),
            mappings));

    definition.addSearch(
        "CETA-59",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CETA-59",
                ResumoMensalCETAQueries
                    .findPatientsWithoutFichaSeguimentoCETA59DaysBeforeEndDate()),
            mappings));

    definition.addSearch(
        "TRATAMENTO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "TRATAMENTO",
                ResumoMensalCETAQueries
                    .findPatientsWithTreatmentPlannedRegisteredInSection7ByReportEndDate()),
            mappings));

    definition.setCompositionString("(CETA-89 NOT CETA-59) NOT TRATAMENTO");

    return definition;
  }

  /** RF24 - Indicador 18 - Nr de pacientes referidos */
  public CohortDefinition getPatientsIndicator18() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoWereReferred");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoWereReferred");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I17", EptsReportUtils.map(getPatientsIndicator17(), mappings));

    definition.addSearch(
        "I18",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I18", ResumoMensalCETAQueries.findPatientsWhoWereReferred()),
            mappings));

    definition.setCompositionString("I17 AND I18");

    return definition;
  }

  /** RF25 - Indicador 15 - Nr de pacientes transferidos */
  public CohortDefinition getPatientsIndicator19() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoHaveTransferredAsAreasonOfInterruption");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoHaveTransferredAsAreasonOfInterruption");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I17", EptsReportUtils.map(getPatientsIndicator17(), mappings));

    definition.addSearch(
        "I19",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I19",
                ResumoMensalCETAQueries.findPatientsWhoHaveTransferredAsAreasonOfInterruption()),
            mappings));

    definition.setCompositionString("I17 AND I19");

    return definition;
  }

  /** RF26 - Indicador 20 - Nr de pacientes reintegrados */
  public CohortDefinition getPatientsIndicator20() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoConsumeOtherPsychoactiveSubstances");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoConsumeOtherPsychoactiveSubstances");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I7", EptsReportUtils.map(getPatientsIndicator7(), mappings));

    definition.addSearch("I6", EptsReportUtils.map(getPatientsIndicator6(), mappings));

    definition.addSearch(
        "I20",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I20", ResumoMensalCETAQueries.findPatientsWhoWereReintegrated()),
            mappings));

    definition.setCompositionString("(I7 NOT I6) NOT I20");

    return definition;
  }

  /** RF27 - Indicador 21 - Nr de óbitos */
  public CohortDefinition getPatientsIndicator21() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsDeaths");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsDeaths");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I17", EptsReportUtils.map(getPatientsIndicator17(), mappings));

    definition.addSearch(
        "I21",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "I21", ResumoMensalCETAQueries.findPatientsDeaths()),
            mappings));

    definition.setCompositionString("I17 AND I21");

    return definition;
  }

  /** RF28 - Indicador 22 - Nr de abandonos */
  public CohortDefinition getPatientsIndicator22() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoAbandonedTreatment");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoAbandonedTreatment");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I17", EptsReportUtils.map(getPatientsIndicator17(), mappings));

    definition.addSearch("I18", EptsReportUtils.map(getPatientsIndicator18(), mappings));

    definition.addSearch("I19", EptsReportUtils.map(getPatientsIndicator19(), mappings));

    definition.addSearch("I20", EptsReportUtils.map(getPatientsIndicator20(), mappings));

    definition.addSearch("I21", EptsReportUtils.map(getPatientsIndicator21(), mappings));

    definition.setCompositionString("I17 NOT (I18 OR I19 OR I20 OR I21)");

    return definition;
  }

  /** RF29 - Indicador 23: Nr de Pacientes que terminaram o tratamento */
  public CohortDefinition getPatientsIndicator23() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsWhoFinishedTreatment");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsWhoFinishedTreatment");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "CETA-AFTER-89",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CETA-AFTER-89",
                ResumoMensalCETAQueries
                    .findPatientsWithFichaSeguimentoCETAAfter89DaysMinusEndDate()),
            mappings));

    definition.addSearch(
        "CETA-AFTER-59",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CETA-AFTER-59",
                ResumoMensalCETAQueries
                    .findPatientsWithoutFichaSeguimentoCETA59DaysBeforeEndDate()),
            mappings));

    definition.addSearch(
        "TRATAMENTO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "TRATAMENTO",
                ResumoMensalCETAQueries
                    .findPatientsWithTreatmentPlannedRegisteredInSection7ByReportEndDate()),
            mappings));

    definition.setCompositionString("(CETA-AFTER-89 NOT CETA-AFTER-59) AND TRATAMENTO");

    return definition;
  }

  /** RF26.1: Relatório Desagregação – Tipo de paciente: 2ª consulta TARV */
  public CohortDefinition getPatientsDisagregationsSecondLineTARV26_1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsScreenedUsingFichaFICABEM");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsScreenedUsingFichaFICABEM");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "INICIAL-CETA",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "INICIAL-CETA",
                ResumoMensalCETAQueries
                    .findPatientsWhoHaveScreeningCriteriaSecondConsultationInFichaInicialCETA()),
            mappings));

    definition.addSearch(
        "INICIAL-CETA-ENDDATE",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "INICIAL-CETA-ENDDATE",
                ResumoMensalCETAQueries.findPatientsWhoHaveAtLeastOneFichaInicialCETA()),
            mappings));

    definition.addSearch("RF32", EptsReportUtils.map(getPatientsRF32(), mappings));

    definition.setCompositionString("INICIAL-CETA OR (RF32 NOT INICIAL-CETA-ENDDATE)");

    return definition;
  }

  /** RF26.2: Relatório Desagregação – Tipo de paciente: CV>1000 cp/ml */
  public CohortDefinition getPatientsDisagregationsCVGreaterThan1000Copies26_2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("findPatientsScreenedUsingFichaFICABEM");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("findPatientsScreenedUsingFichaFICABEM");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("RF29", EptsReportUtils.map(getPatientsRF33(), mappings));

    definition.addSearch(
        "CV-1000",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "CV-1000",
                ResumoMensalCETAQueries
                    .findPatientsWhoHaveScreeningCriteriaCVGreaterThan1000CopiesInFichaInicialCETA()),
            mappings));

    definition.addSearch(
        "INICIAL-CETA-ENDDATE",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "INICIAL-CETA-ENDDATE",
                ResumoMensalCETAQueries.findPatientsWhoHaveAtLeastOneFichaInicialCETA()),
            mappings));

    definition.addSearch(
        "DISAG1", EptsReportUtils.map(getPatientsDisagregationsSecondLineTARV26_1(), mappings));

    definition.setCompositionString("((RF29 NOT INICIAL-CETA-ENDDATE)  OR CV-1000) NOT DISAG1");

    return definition;
  }

  /** RF26.3: Relatório Desagregação – Tipo de paciente: Reintegrado / Ma Adesao */
  public CohortDefinition getPatientsDisagregationsPatientsReintegreted26_3() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getPatientsDisagregationsPatientsReintegreted26_3");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getPatientsDisagregationsPatientsReintegreted26_3");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "REINTEGRADO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "REINTEGRADO",
                ResumoMensalCETAQueries
                    .findPatientsWhoHaveScreeningCriteriaReintegretedInFichaInicialCETA()),
            mappings));

    definition.addSearch("RF34", EptsReportUtils.map(getPatientsRF34(), mappings));

    definition.addSearch(
        "DISAG1", EptsReportUtils.map(getPatientsDisagregationsSecondLineTARV26_1(), mappings));

    definition.addSearch(
        "DISAG2",
        EptsReportUtils.map(getPatientsDisagregationsCVGreaterThan1000Copies26_2(), mappings));

    definition.addSearch(
        "INICIAL-CETA-ENDDATE",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "INICIAL-CETA-ENDDATE",
                ResumoMensalCETAQueries.findPatientsWhoHaveAtLeastOneFichaInicialCETA()),
            mappings));

    definition.setCompositionString(
        "(REINTEGRADO OR (RF34 NOT INICIAL-CETA-ENDDATE)) NOT (DISAG1 OR DISAG2)");

    return definition;
  }

  /** RF26.4: Relatório Desagregação – Tipo de paciente: Factores Psicossociais */
  public CohortDefinition getPatientsDisagregationsPsychosocialFactors26_4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getPatientsDisagregationsPsychosocialFactors26_4");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getPatientsDisagregationsPsychosocialFactors26_4");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "PSICOSSOCIAIS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PSICOSSOCIAIS",
                ResumoMensalCETAQueries
                    .findPatientsWhoHaveScreeningCriteriaPsychosocialFactorsInFichaInicialCETA()),
            mappings));

    definition.addSearch("RF35", EptsReportUtils.map(getPatientsRF35(), mappings));

    definition.addSearch(
        "DISAG1", EptsReportUtils.map(getPatientsDisagregationsSecondLineTARV26_1(), mappings));

    definition.addSearch(
        "DISAG2",
        EptsReportUtils.map(getPatientsDisagregationsCVGreaterThan1000Copies26_2(), mappings));

    definition.addSearch(
        "DISAG3",
        EptsReportUtils.map(getPatientsDisagregationsPatientsReintegreted26_3(), mappings));

    definition.addSearch(
        "INICIAL-CETA-ENDDATE",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "INICIAL-CETA-ENDDATE",
                ResumoMensalCETAQueries.findPatientsWhoHaveAtLeastOneFichaInicialCETA()),
            mappings));

    definition.setCompositionString(
        "(PSICOSSOCIAIS OR (RF35 NOT INICIAL-CETA-ENDDATE)) NOT (DISAG1 OR DISAG2 OR DISAG3)");

    return definition;
  }

  /** RF26.5 - All Indicators: Tipo de paciente: Sem Informação */
  public CohortDefinition getPatientsIndicatorWithoutInformationDisag(CohortDefinition indicator) {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getPatientsIndicator0WithoutInformationDisag");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getPatientsIndicator0WithoutInformationDisag");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch("I", EptsReportUtils.map(indicator, mappings));

    definition.addSearch(
        "DISAG1", EptsReportUtils.map(getPatientsDisagregationsSecondLineTARV26_1(), mappings));

    definition.addSearch(
        "DISAG2",
        EptsReportUtils.map(getPatientsDisagregationsCVGreaterThan1000Copies26_2(), mappings));

    definition.addSearch(
        "DISAG3",
        EptsReportUtils.map(getPatientsDisagregationsPatientsReintegreted26_3(), mappings));

    definition.addSearch(
        "DISAG4",
        EptsReportUtils.map(getPatientsDisagregationsPsychosocialFactors26_4(), mappings));

    definition.setCompositionString("I NOT (DISAG1 OR DISAG2 OR DISAG3 OR DISAG4)");

    return definition;
  }

  /** RF28: Outras Fontes: 2ª Consulta TARV */
  public CohortDefinition getPatientsRF32() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getPatientsRF28");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getPatientsRF28");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "RF28-SEGUNDA-CONSULTA",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "RF28-SEGUNDA-CONSULTA",
                ResumoMensalCETAQueries.findPatientsWithSecondFichaClinicaDuringPeriod()),
            mappings));

    definition.addSearch(
        "RF28-TRANSFERIDOS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "RF28-TRANSFERIDOS", ResumoMensalCETAQueries.findPatientsWhoWereTransferredIn()),
            mappings));

    definition.setCompositionString("RF28-SEGUNDA-CONSULTA NOT RF28-TRANSFERIDOS");

    return definition;
  }

  /** RF29 - Outras Fontes: CV > 1000 cps */
  public CohortDefinition getPatientsRF33() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getPatientsRF33");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getPatientsRF33");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "RF33",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "RF33", ResumoMensalCETAQueries.findPatientsWithCVGreaterThan1000Copies()),
            mappings));

    definition.setCompositionString("RF33");

    return definition;
  }

  /** RF34 - Outras Fontes: Reintegrado / Ma Adesao */
  public CohortDefinition getPatientsRF34() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getPatientsRF34");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getPatientsRF34");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "RF34-REINTEGRADO",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "RF34-REINTEGRADO",
                ResumoMensalCETAQueries.findPatientsWithReinicioFichaClinicaAndFichaResumo()),
            mappings));

    definition.setCompositionString("RF34-REINTEGRADO");

    return definition;
  }

  /** RF30 - Outras Fontes: Reintegrado / Ma Adesao */
  public CohortDefinition getPatientsRF35() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("getPatientsRF35");
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.setName("getPatientsRF35");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.addSearch(
        "RF31-PSICOSSOCIAIS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "RF31-PSICOSSOCIAIS",
                ResumoMensalCETAQueries.findPatientsRegisteredInPsychosocialFactorsInAPSSForm()),
            mappings));

    definition.setCompositionString("RF31-PSICOSSOCIAIS");

    return definition;
  }
}
