/*
 * The contents of this file are subject to the OpenMRS Public License Version
 * 1.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Arrays;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.metadata.HivMetadata;
import org.openmrs.module.eptsreports.metadata.TbMetadata;
import org.openmrs.module.eptsreports.reporting.library.queries.TB4MontlyCascadeReportQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.TB4MontlyCascadeReportQueries.QUERY.DiagnosticTestTypes;
import org.openmrs.module.eptsreports.reporting.library.queries.TB4MontlyCascadeReportQueries.QUERY.EnrollmentPeriod;
import org.openmrs.module.eptsreports.reporting.library.queries.TB7AdvancedDiseaseQueries.QUERY.PositivityLevel;
import org.openmrs.module.eptsreports.reporting.library.queries.TXTBQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TB4MontlyCascadeCohortQueries {

  @Autowired private TbMetadata tbMetadata;

  @Autowired private HivMetadata hivMetadata;

  @Autowired private GenericCohortQueries genericCohortQueries;

  @Autowired private TXTBCohortQueries txtbCohortQueries;

  @Autowired private TxCurrCohortQueries txCurrCohortQueries;

  private String generalParameterMapping =
      "startDate=${endDate-6m+1d},endDate=${endDate},location=${location}";

  @DocumentedDefinition(value = "TxTBDenominatorNegativeScreening")
  public CohortDefinition getTxTBDenominatorAndNegativeScreening() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TxTB - Denominator  Negative Screening");

    definition.addSearch(
        "denominator",
        EptsReportUtils.map(this.txtbCohortQueries.getDenominator(), generalParameterMapping));

    definition.addSearch(
        "positive-screening",
        EptsReportUtils.map(
            this.txtbCohortQueries.findPatientWhoAreTBPositiveScreening(),
            generalParameterMapping));

    definition.addSearch(
        "new-on-art", EptsReportUtils.map(this.getNewOnArt(), this.generalParameterMapping));

    this.addGeneralParameters(definition);
    definition.setCompositionString("denominator NOT positive-screening");
    return definition;
  }

  @DocumentedDefinition(value = "get New on Art")
  public CohortDefinition getNewOnArt() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TxTB - New on ART");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "started-on-period",
        EptsReportUtils.map(
            this.genericCohortQueries.getStartedArtOnPeriod(false, true),
            "onOrAfter=${endDate-6m+1d},onOrBefore=${endDate},location=${location}"));
    definition.setCompositionString("started-on-period");
    return definition;
  }

  @DocumentedDefinition(value = "getGenExpertPositiveTestResults")
  public CohortDefinition getGenExpertPositiveTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("GeneXpert MTB/RIF Posetive Test Results");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "genexperts",
        EptsReportUtils.map(
            this.findDiagnosticPositiveTestResults(DiagnosticTestTypes.GENEXPERT),
            generalParameterMapping));
    definition.setCompositionString("genexperts");
    return definition;
  }

  @DocumentedDefinition(value = "allNegativeTestResults")
  public CohortDefinition getAllNegativeTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Posetive Test Results");
    this.addGeneralParameters(definition);

    definition.addSearch(
        "genexpertsNegativeTestResults",
        EptsReportUtils.map(this.getGenExpertNegativeTestResults(), generalParameterMapping));

    definition.addSearch(
        "baciloscopiaNegativeTestResults",
        EptsReportUtils.map(this.getBaciloscopiaNegativeTestResults(), generalParameterMapping));

    definition.addSearch(
        "tblamNegativeTestResults",
        EptsReportUtils.map(this.getTBLAMNegativeTestResults(), generalParameterMapping));

    definition.addSearch(
        "otherAditionalNegativeTestResults",
        EptsReportUtils.map(this.getAdditionalNegativeTestResults(), generalParameterMapping));

    definition.setCompositionString(
        "genexpertsNegativeTestResults OR baciloscopiaNegativeTestResults OR tblamNegativeTestResults OR otherAditionalNegativeTestResults");
    return definition;
  }

  @DocumentedDefinition(value = "getGenExpertNegativeTestResults")
  public CohortDefinition getGenExpertNegativeTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("GeneXpert MTB/RIF Negative Test Results");
    this.addGeneralParameters(definition);

    definition.addSearch(
        "negativeGenexperts",
        EptsReportUtils.map(
            this.findDiagnosticNegativeTestResults(DiagnosticTestTypes.GENEXPERT),
            generalParameterMapping));

    definition.addSearch(
        "genexpertsPositiveTestResults",
        EptsReportUtils.map(this.getGenExpertPositiveTestResults(), generalParameterMapping));

    definition.setCompositionString("negativeGenexperts NOT genexpertsPositiveTestResults");
    return definition;
  }

  @DocumentedDefinition(value = "GenExpertTests")
  public CohortDefinition getGenExpertTests() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("GeneXpert MTB/RIF");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    definition.setQuery(TXTBQueries.findPatientWhoAreDiagnosticTestMWRD());

    return definition;
  }

  @DocumentedDefinition(value = "BaciloscopiaTests")
  public CohortDefinition getBaciloscopiaTests() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Smear microscopy only");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "baciloscopia",
        EptsReportUtils.map(this.getDiagnosticTestSmearMicroscopyOnly(), generalParameterMapping));

    definition.addSearch(
        "genexperts", EptsReportUtils.map(this.getGenExpertTests(), generalParameterMapping));

    definition.setCompositionString("baciloscopia NOT genexperts");
    return definition;
  }

  @DocumentedDefinition(value = "getBaciloscopiaPositiveTestResults")
  public CohortDefinition getBaciloscopiaPositiveTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Smear microscopy only Positive Test Results");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "baciloscopia-positive-test-results",
        EptsReportUtils.map(
            this.findDiagnosticPositiveTestResults(DiagnosticTestTypes.BACILOSCOPIA),
            generalParameterMapping));

    definition.addSearch(
        "genexperts-tests", EptsReportUtils.map(this.getGenExpertTests(), generalParameterMapping));

    definition.setCompositionString("baciloscopia-positive-test-results NOT genexperts-tests");
    return definition;
  }

  @DocumentedDefinition(value = "getBaciloscopiaNegativeTestResults")
  public CohortDefinition getBaciloscopiaNegativeTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Smear microscopy only Negative Test Results");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "baciloscopia-negative-test-results",
        EptsReportUtils.map(
            this.findDiagnosticNegativeTestResults(DiagnosticTestTypes.BACILOSCOPIA),
            generalParameterMapping));

    definition.addSearch(
        "genexperts-tests", EptsReportUtils.map(this.getGenExpertTests(), generalParameterMapping));

    definition.addSearch(
        "baciloscopia-positive-test-results",
        EptsReportUtils.map(this.getBaciloscopiaPositiveTestResults(), generalParameterMapping));

    definition.setCompositionString(
        "baciloscopia-negative-test-results NOT (genexperts-tests OR baciloscopia-positive-test-results)");
    return definition;
  }

  @DocumentedDefinition(value = "TBLAM")
  public CohortDefinition getTBLAMTests() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB LAM");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "tblam", EptsReportUtils.map(this.getTBLAMTest(), generalParameterMapping));

    definition.addSearch(
        "genexperts", EptsReportUtils.map(this.getGenExpertTests(), generalParameterMapping));
    definition.addSearch(
        "baciloscopia", EptsReportUtils.map(this.getBaciloscopiaTests(), generalParameterMapping));

    definition.setCompositionString("tblam NOT (genexperts OR baciloscopia)");
    return definition;
  }

  @DocumentedDefinition(value = "allPositiveTestResults")
  public CohortDefinition getAllPositiveTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Posetive Test Results");
    this.addGeneralParameters(definition);

    definition.addSearch(
        "genexpertsPositiveTestResults",
        EptsReportUtils.map(this.getGenExpertPositiveTestResults(), generalParameterMapping));

    definition.addSearch(
        "baciloscopiaPositiveTestResults",
        EptsReportUtils.map(this.getBaciloscopiaPositiveTestResults(), generalParameterMapping));

    definition.addSearch(
        "tblamPositiveTestResults",
        EptsReportUtils.map(this.getTBLAMPositiveTestResults(), generalParameterMapping));

    definition.addSearch(
        "otherAditionalPositiveTestResults",
        EptsReportUtils.map(this.getAdditionalPositiveTestResults(), generalParameterMapping));

    definition.setCompositionString(
        "genexpertsPositiveTestResults OR baciloscopiaPositiveTestResults OR tblamPositiveTestResults OR otherAditionalPositiveTestResults ");
    return definition;
  }

  @DocumentedDefinition(value = "getDenominatorAndPositiveOrNegativeResults")
  public CohortDefinition getDenominatorAndPositiveOrNegativeCohort(
      CohortDefinition positiveOrNegativeCohort, String mapping) {

    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    this.addGeneralParameters(cd);
    cd.setName("TxTB - Positive Results");

    cd.addSearch(
        "DENOMINATOR", EptsReportUtils.map(this.txtbCohortQueries.getDenominator(), mapping));

    cd.addSearch("POS-NEG-COHORT", EptsReportUtils.map(positiveOrNegativeCohort, mapping));

    cd.setCompositionString("DENOMINATOR AND POS-NEG-COHORT");

    return cd;
  }

  @DocumentedDefinition(value = "getDiagnosticTestSmearMicroscopyOnly")
  private CohortDefinition getDiagnosticTestSmearMicroscopyOnly() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("Smear microscopy only");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    definition.setQuery(TXTBQueries.findPatientWhoAreDiagnosticTestSmearMicroscopyOnly());

    return definition;
  }

  @DocumentedDefinition(value = "getTBLAMTest")
  private CohortDefinition getTBLAMTest() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("TB LAM Tests");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    definition.setQuery(TXTBQueries.findPatientWhoHaveTBLAMDiagnosticTest());

    return definition;
  }

  @DocumentedDefinition(value = "getTBLAMPositiveTestResults")
  public CohortDefinition getTBLAMPositiveTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB LAM Positive Test Results");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "tblam", EptsReportUtils.map(this.getTBLAMPositiveTestsResults(), generalParameterMapping));

    definition.addSearch(
        "genexperts", EptsReportUtils.map(this.getGenExpertTests(), generalParameterMapping));
    definition.addSearch(
        "baciloscopia", EptsReportUtils.map(this.getBaciloscopiaTests(), generalParameterMapping));

    definition.setCompositionString("tblam NOT (genexperts OR baciloscopia)");
    return definition;
  }

  @DocumentedDefinition(value = "getTBLAMPositiveTestsResults")
  private CohortDefinition getTBLAMPositiveTestsResults() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("B LAM Positive Test Results");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    definition.setQuery(
        TB4MontlyCascadeReportQueries.QUERY.findDiagnosticTestsWithPositiveTestResultsTBLAM());

    return definition;
  }

  @DocumentedDefinition(value = "getTBLAMNegativeTestResults")
  public CohortDefinition getTBLAMNegativeTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB LAM Negative Test Results");
    this.addGeneralParameters(definition);

    definition.addSearch(
        "negative-tblam",
        EptsReportUtils.map(this.getTBLAMNegativeTestsResults(), generalParameterMapping));

    definition.addSearch(
        "genexperts", EptsReportUtils.map(this.getGenExpertTests(), generalParameterMapping));
    definition.addSearch(
        "baciloscopia", EptsReportUtils.map(this.getBaciloscopiaTests(), generalParameterMapping));

    definition.addSearch(
        "tblam-positive-results",
        EptsReportUtils.map(this.getTBLAMPositiveTestsResults(), generalParameterMapping));

    definition.setCompositionString(
        "negative-tblam NOT (genexperts OR baciloscopia OR tblam-positive-results)");
    return definition;
  }

  @DocumentedDefinition(value = "getTBLAMNegativeTestsResults")
  private CohortDefinition getTBLAMNegativeTestsResults() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("B LAM Negative Test Results");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    definition.setQuery(
        TB4MontlyCascadeReportQueries.QUERY.findDiagnosticTestsWithNegativeTestResultsTBLAM());

    return definition;
  }

  @DocumentedDefinition(value = "AdditionalTests")
  public CohortDefinition getAdditionalTests() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Additional test other than mWRD");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "additionalTests",
        EptsReportUtils.map(this.getAdditionalTestsCulture(), generalParameterMapping));

    definition.addSearch(
        "genexperts", EptsReportUtils.map(this.getGenExpertTests(), generalParameterMapping));
    definition.addSearch(
        "baciloscopia", EptsReportUtils.map(this.getBaciloscopiaTests(), generalParameterMapping));
    definition.addSearch(
        "tblam", EptsReportUtils.map(this.getTBLAMTests(), generalParameterMapping));

    definition.setCompositionString("additionalTests NOT (genexperts OR baciloscopia OR tblam)");
    return definition;
  }

  @DocumentedDefinition(value = "getAdditionalTestsCulture")
  private CohortDefinition getAdditionalTestsCulture() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("Additional test other than mWRD");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    definition.setQuery(TXTBQueries.findPatientWhoAreDiagnosticTestOtherCulture());

    return definition;
  }

  @DocumentedDefinition(value = "getAdditionalPositiveTestResults")
  public CohortDefinition getAdditionalPositiveTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("Additional test other than GeneXpert Positive Test Results");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "additional-tests",
        EptsReportUtils.map(
            this.findDiagnosticPositiveTestResults(DiagnosticTestTypes.CULTURA),
            generalParameterMapping));

    definition.addSearch(
        "genexperts", EptsReportUtils.map(this.getGenExpertTests(), generalParameterMapping));
    definition.addSearch(
        "baciloscopia", EptsReportUtils.map(this.getBaciloscopiaTests(), generalParameterMapping));
    definition.addSearch(
        "tblam", EptsReportUtils.map(this.getTBLAMTests(), generalParameterMapping));

    definition.setCompositionString("additional-tests NOT (genexperts OR baciloscopia OR tblam)");
    return definition;
  }

  @DocumentedDefinition(value = "getAdditionalNegativeTestResults")
  public CohortDefinition getAdditionalNegativeTestResults() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB LAM Negative Test Results");
    this.addGeneralParameters(definition);
    definition.addSearch(
        "negative-additional-tests",
        EptsReportUtils.map(
            this.findDiagnosticNegativeTestResults(DiagnosticTestTypes.CULTURA),
            generalParameterMapping));

    definition.addSearch(
        "genexperts", EptsReportUtils.map(this.getGenExpertTests(), generalParameterMapping));
    definition.addSearch(
        "baciloscopia", EptsReportUtils.map(this.getBaciloscopiaTests(), generalParameterMapping));
    definition.addSearch(
        "tblam", EptsReportUtils.map(this.getTBLAMTests(), generalParameterMapping));

    definition.addSearch(
        "posetive-additional-tests",
        EptsReportUtils.map(this.getAdditionalPositiveTestResults(), generalParameterMapping));

    definition.setCompositionString(
        "negative-additional-tests NOT (genexperts OR baciloscopia OR tblam OR posetive-additional-tests)");
    return definition;
  }

  @DocumentedDefinition(value = "txTbNumeratorA")
  private CohortDefinition txTbNumeratorA() {
    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    cd.setName("TxTB - txTbNumeratorA");
    final CohortDefinition i =
        this.genericCohortQueries.generalSql(
            "onTbTreatment",
            TXTBQueries.dateObs(
                this.tbMetadata.getTBDrugTreatmentStartDate().getConceptId(),
                Arrays.asList(
                    this.hivMetadata.getAdultoSeguimentoEncounterType().getId(),
                    this.hivMetadata.getARVPediatriaSeguimentoEncounterType().getId()),
                true));
    final CohortDefinition ii = txtbCohortQueries.getInTBProgram();

    this.addGeneralParameters(i);

    cd.addSearch("i", EptsReportUtils.map(i, generalParameterMapping));
    cd.addSearch("ii", EptsReportUtils.map(ii, generalParameterMapping));
    cd.addSearch(
        "iii",
        EptsReportUtils.map(
            txtbCohortQueries.getPulmonaryTBWithinReportingDate(), generalParameterMapping));
    cd.addSearch(
        "iv",
        EptsReportUtils.map(
            txtbCohortQueries.getTuberculosisTreatmentPlanWithinReportingDate(),
            generalParameterMapping));

    final CohortDefinition artList = txtbCohortQueries.artList();
    cd.addSearch("artList", EptsReportUtils.map(artList, generalParameterMapping));
    cd.setCompositionString("(i OR ii OR iii OR iv) AND artList");
    this.addGeneralParameters(cd);
    return cd;
  }

  @DocumentedDefinition(value = "findDiagnosticPositiveTestResults")
  private CohortDefinition findDiagnosticPositiveTestResults(
      DiagnosticTestTypes diagnosticTestType) {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findDiagnosticPositiveTestResults");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    definition.setQuery(
        TB4MontlyCascadeReportQueries.QUERY.findDiagnosticTestsWithPositiveTestResults(
            diagnosticTestType));

    return definition;
  }

  @DocumentedDefinition(value = "findDiagnosticNegativeTestResults")
  private CohortDefinition findDiagnosticNegativeTestResults(
      DiagnosticTestTypes diagnosticTestType) {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findDiagnosticNegativeTestResults");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));
    definition.setQuery(
        TB4MontlyCascadeReportQueries.QUERY.findDiagnosticTestsWithNegativeTestResults(
            diagnosticTestType));

    return definition;
  }

  @DocumentedDefinition(value = "PatientsWithPositiveResultWhoStartedTBTreatment")
  public CohortDefinition getPositiveResultAndTXTBNumerator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TxTB -Denominator Positive Results Who Started TB Treatment");
    this.addGeneralParameters(definition);

    definition.addSearch(
        "posetiveResults",
        EptsReportUtils.map(
            this.getDenominatorAndPositiveOrNegativeCohort(
                this.getAllPositiveTestResults(), generalParameterMapping),
            generalParameterMapping));

    definition.addSearch(
        "txtbNumerator",
        EptsReportUtils.map(txtbCohortQueries.txTbNumerator(), generalParameterMapping));

    definition.setCompositionString("posetiveResults AND txtbNumerator");
    return definition;
  }

  @DocumentedDefinition(value = "ScreenedPatientsWhoStartedTBTreatmentAndTXCurr")
  public CohortDefinition getScreenedPatientsWhoStartedTBTreatmentAndTXCurr() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TxTB - Screened Patients Who Started TB Treatment and Are TX_CURR");
    this.addGeneralParameters(definition);

    definition.addSearch(
        "txTB", EptsReportUtils.map(txtbCohortQueries.txTbNumerator(), generalParameterMapping));
    definition.addSearch(
        "txCurr",
        EptsReportUtils.map(
            this.txCurrCohortQueries.findPatientsWhoAreActiveOnART(),
            "endDate=${endDate},location=${location}"));

    definition.setCompositionString("txTB AND txCurr");
    return definition;
  }

  @DocumentedDefinition(value = "get Negative Results")
  public CohortDefinition getNegativeResultCohortDefinition() {

    final CompositionCohortDefinition cd = new CompositionCohortDefinition();
    this.addGeneralParameters(cd);
    cd.setName("TxTB -Denominator Negative Results");

    cd.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(this.txtbCohortQueries.getDenominator(), this.generalParameterMapping));

    cd.addSearch(
        "all-negative-test-results",
        EptsReportUtils.map(this.getAllNegativeTestResults(), this.generalParameterMapping));

    cd.setCompositionString("DENOMINATOR AND all-negative-test-results ");

    return cd;
  }

  @DocumentedDefinition(value = "TBDenominatorAndTxCurr")
  public CohortDefinition getTxBTDenominatorAndTxCurr() {
    final CompositionCohortDefinition composiiton = new CompositionCohortDefinition();

    composiiton.setName("TX_TB denominator in the last 6 months and TX_CURR ");
    composiiton.addParameter(new Parameter("endDate", "End Date", Date.class));
    composiiton.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate},location=${location}";

    composiiton.addSearch(
        "TX-CURR",
        EptsReportUtils.map(this.txCurrCohortQueries.findPatientsWhoAreActiveOnART(), mappings));

    composiiton.addSearch(
        "TX-TB-DENOMINATOR",
        EptsReportUtils.map(
            this.txtbCohortQueries.getDenominator(),
            "startDate=${endDate-6m+1d},endDate=${endDate},location=${location}"));

    composiiton.setCompositionString("TX-CURR and TX-TB-DENOMINATOR ");

    return composiiton;
  }

  @DocumentedDefinition(value = "patientsWhoAreCurrentyEnrolledOnArtInTheLastSixMonths")
  public CohortDefinition getPatientsEnrollendOnARTForTheLastSixMonths() {
    final CompositionCohortDefinition composiiton = new CompositionCohortDefinition();

    composiiton.setName("Patient On ART in the last 6 Months ");
    composiiton.addParameter(new Parameter("endDate", "End Date", Date.class));
    composiiton.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate},location=${location}";

    composiiton.addSearch(
        "newlyOnArt",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PatientsWithClinicalConsultationIntheLastSixMonths",
                TB4MontlyCascadeReportQueries.QUERY
                    .findPatientsWhoAreCurrentlyEnrolledOnARTByPeriod(EnrollmentPeriod.NEWLY)),
            mappings));

    composiiton.setCompositionString("newlyOnArt");
    return composiiton;
  }

  @DocumentedDefinition(value = "patientsWhoAreCurrentlyEnrolledOnARTInForMoreThanSixMonths")
  public CohortDefinition getPatientsEnrolledOnArtForMoreThanSixMonths() {
    final CompositionCohortDefinition composiiton = new CompositionCohortDefinition();

    composiiton.setName("Patient On ART For More Than 6 Months");
    composiiton.addParameter(new Parameter("endDate", "End Date", Date.class));
    composiiton.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate},location=${location}";

    composiiton.addSearch(
        "previouslyOnArt",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "patientsWhoAreCurrentlyEnrolledOnARTInForMoreThanSixMonths",
                TB4MontlyCascadeReportQueries.QUERY
                    .findPatientsWhoAreCurrentlyEnrolledOnARTByPeriod(EnrollmentPeriod.PREVIOUSLY)),
            mappings));

    composiiton.addSearch(
        "newlyOnArt",
        EptsReportUtils.map(this.getPatientsEnrollendOnARTForTheLastSixMonths(), mappings));

    composiiton.setCompositionString("previouslyOnArt NOT newlyOnArt");

    return composiiton;
  }

  @DocumentedDefinition(value = "patientsWithClinicalConsultationsInTheLastSixMonths")
  public CohortDefinition getClinicalConsultationsInLastSixMonths() {
    final CompositionCohortDefinition composiiton = new CompositionCohortDefinition();

    composiiton.setName("Patients with Clinical Consultations in the last 6 months");
    composiiton.addParameter(new Parameter("endDate", "End Date", Date.class));
    composiiton.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "endDate=${endDate},location=${location}";

    composiiton.addSearch(
        "consultationsLast6Months",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PatientsWithClinicalConsultationIntheLastSixMonths",
                TB4MontlyCascadeReportQueries.QUERY
                    .findPatientsWithClinicalConsultationIntheLastSixMonths),
            mappings));

    composiiton.setCompositionString("consultationsLast6Months");

    return composiiton;
  }

  private void addGeneralParameters(final CohortDefinition cd) {
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));
  }

  public CohortDefinition getNumberOfClientsWithGradeFourLevelPositiveTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB4 - Positive TB LAM Results: Grade 4");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(
                TB4MontlyCascadeReportQueries.QUERY
                    .FIND_PATIENTS_WITH_POSITIVE_TBLAM_RESULT_AND_POSITIVITY_LEVEL),
            PositivityLevel.GRADE_FOUR.getValue(),
            PositivityLevel.GRADE_FOUR.getValue(),
            PositivityLevel.GRADE_FOUR.getValue(),
            StringUtils.join(Arrays.asList(0), ","),
            StringUtils.join(Arrays.asList(0), ","),
            StringUtils.join(Arrays.asList(0), ","));

    definition.addSearch(
        "POSITIVE-TBLAM-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with Positive TBLAM Results during the inclusion period - "
                    + PositivityLevel.GRADE_FOUR.name(),
                query),
            generalParameterMapping));

    definition.setCompositionString("POSITIVE-TBLAM-RESULTS");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithGradeThreeLevelPositiveTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB4 - Positive TB LAM Results: Grade 3");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(
                TB4MontlyCascadeReportQueries.QUERY
                    .FIND_PATIENTS_WITH_POSITIVE_TBLAM_RESULT_AND_POSITIVITY_LEVEL),
            PositivityLevel.GRADE_THREE.getValue(),
            PositivityLevel.GRADE_THREE.getValue(),
            PositivityLevel.GRADE_THREE.getValue(),
            StringUtils.join(Arrays.asList(PositivityLevel.GRADE_FOUR.getValue()), ","),
            StringUtils.join(Arrays.asList(PositivityLevel.GRADE_FOUR.getValue()), ","),
            StringUtils.join(Arrays.asList(PositivityLevel.GRADE_FOUR.getValue()), ","));

    definition.addSearch(
        "POSITIVE-TBLAM-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with Positive TBLAM Results during the inclusion period - "
                    + PositivityLevel.GRADE_THREE.name(),
                query),
            generalParameterMapping));

    definition.addSearch(
        "GRADE4",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeFourLevelPositiveTBLAMResults(),
            generalParameterMapping));

    definition.setCompositionString("POSITIVE-TBLAM-RESULTS NOT GRADE4");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithGradeTwoLevelPositiveTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB4 - Positive TB LAM Results: Grade 2");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(
                TB4MontlyCascadeReportQueries.QUERY
                    .FIND_PATIENTS_WITH_POSITIVE_TBLAM_RESULT_AND_POSITIVITY_LEVEL),
            PositivityLevel.GRADE_TWO.getValue(),
            PositivityLevel.GRADE_TWO.getValue(),
            PositivityLevel.GRADE_TWO.getValue(),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(), PositivityLevel.GRADE_THREE.getValue()),
                ","),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(), PositivityLevel.GRADE_THREE.getValue()),
                ","),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(), PositivityLevel.GRADE_THREE.getValue()),
                ","));

    definition.addSearch(
        "POSITIVE-TBLAM-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with Positive TBLAM Results during the inclusion period - "
                    + PositivityLevel.GRADE_TWO.name(),
                query),
            generalParameterMapping));

    definition.addSearch(
        "GRADE4",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeFourLevelPositiveTBLAMResults(),
            generalParameterMapping));

    definition.addSearch(
        "GRADE3",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeThreeLevelPositiveTBLAMResults(),
            generalParameterMapping));

    definition.setCompositionString("POSITIVE-TBLAM-RESULTS NOT (GRADE3 OR GRADE4)");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithGradeOneLevelPositiveTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB4 - Positive TB LAM Results: Grade 1");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(
                TB4MontlyCascadeReportQueries.QUERY
                    .FIND_PATIENTS_WITH_POSITIVE_TBLAM_RESULT_AND_POSITIVITY_LEVEL),
            PositivityLevel.GRADE_ONE.getValue(),
            PositivityLevel.GRADE_ONE.getValue(),
            PositivityLevel.GRADE_ONE.getValue(),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(),
                    PositivityLevel.GRADE_THREE.getValue(),
                    PositivityLevel.GRADE_TWO.getValue()),
                ","),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(),
                    PositivityLevel.GRADE_THREE.getValue(),
                    PositivityLevel.GRADE_TWO.getValue()),
                ","),
            StringUtils.join(
                Arrays.asList(
                    PositivityLevel.GRADE_FOUR.getValue(),
                    PositivityLevel.GRADE_THREE.getValue(),
                    PositivityLevel.GRADE_TWO.getValue()),
                ","));

    definition.addSearch(
        "POSITIVE-TBLAM-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with Positive TBLAM Results during the inclusion period - "
                    + PositivityLevel.GRADE_ONE.name(),
                query),
            generalParameterMapping));

    definition.addSearch(
        "GRADE4",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeFourLevelPositiveTBLAMResults(),
            generalParameterMapping));

    definition.addSearch(
        "GRADE3",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeThreeLevelPositiveTBLAMResults(),
            generalParameterMapping));

    definition.addSearch(
        "GRADE2",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeTwoLevelPositiveTBLAMResults(),
            generalParameterMapping));

    definition.setCompositionString("POSITIVE-TBLAM-RESULTS NOT (GRADE2 OR GRADE3 OR GRADE4)");

    return definition;
  }

  public CohortDefinition getNumberOfClientsWithGradeNoLevelPositiveTBLAMResults() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();
    definition.setName("TB4 - Positive TB LAM Results: Grade no Level");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String query =
        String.format(
            EptsQuerysUtils.loadQuery(
                TB4MontlyCascadeReportQueries.QUERY
                    .FIND_PATIENTS_WITH_POSITIVE_TBLAM_RESULT_WITHOUT_POSITIVITY_LEVEL));

    definition.addSearch(
        "POSITIVE-TBLAM-RESULTS",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "clients with Positive TBLAM Results during the inclusion period - "
                    + PositivityLevel.NO_GRADE.name(),
                query),
            generalParameterMapping));

    definition.addSearch(
        "GRADE4",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeFourLevelPositiveTBLAMResults(),
            generalParameterMapping));

    definition.addSearch(
        "GRADE3",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeThreeLevelPositiveTBLAMResults(),
            generalParameterMapping));

    definition.addSearch(
        "GRADE2",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeTwoLevelPositiveTBLAMResults(),
            generalParameterMapping));

    definition.addSearch(
        "GRADE1",
        EptsReportUtils.map(
            this.getNumberOfClientsWithGradeOneLevelPositiveTBLAMResults(),
            generalParameterMapping));

    definition.setCompositionString(
        "POSITIVE-TBLAM-RESULTS NOT (GRADE4 OR GRADE3 OR GRADE2 OR GRADE1)");

    return definition;
  }
}
