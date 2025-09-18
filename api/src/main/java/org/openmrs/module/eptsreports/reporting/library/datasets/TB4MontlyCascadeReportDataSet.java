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
package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.Arrays;
import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.TB4MontlyCascadeCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.TXTBCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.TxCurrCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.dimensions.AgeDimensionCohortInterface;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.dimensions.TB4TXTBMontlyCascadeReportDimensions;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TB4MontlyCascadeReportDataSet extends BaseDataSet {

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired private TxCurrCohortQueries txCurrCohortQueries;

  @Autowired private TB4MontlyCascadeCohortQueries tb4MontlyCascadeCohortQuery;

  @Autowired private TXTBCohortQueries txtbCohortQueries;

  @Autowired private EptsCommonDimension eptsCommonDimension;

  @Autowired private TB4TXTBMontlyCascadeReportDimensions montlyCascadeReportDimensions;

  @Autowired
  @Qualifier("commonAgeDimensionCohort")
  private AgeDimensionCohortInterface ageDimensionCohort;

  public DataSetDefinition constructDatset(List<Parameter> parameters) {
    CohortIndicatorDataSetDefinition dataSetDefinition = new CohortIndicatorDataSetDefinition();
    String mappings = "endDate=${endDate},location=${location}";
    String mappingsPreviousPeriod =
        "startDate=${endDate-6m+1d},endDate=${endDate},location=${location}";
    dataSetDefinition.setName("TX TB Montly Cascade Data Set");
    dataSetDefinition.addParameters(parameters);

    dataSetDefinition.addDimension("gender", EptsReportUtils.map(eptsCommonDimension.gender(), ""));
    dataSetDefinition.addDimension(
        "age",
        EptsReportUtils.map(
            eptsCommonDimension.age(ageDimensionCohort), "effectiveDate=${endDate}"));

    dataSetDefinition.addDimension(
        "artStartState",
        EptsReportUtils.map(
            this.montlyCascadeReportDimensions.getArtStartRangeDimension(), mappings));

    dataSetDefinition.addDimension(
        "clinicalConsultation",
        EptsReportUtils.map(
            this.montlyCascadeReportDimensions.getClinicalConsultationDimension(), mappings));

    dataSetDefinition.addDimension(
        "diagnostictest",
        EptsReportUtils.map(this.montlyCascadeReportDimensions.DiagnosticTest(), mappings));

    dataSetDefinition.addDimension(
        "posetiveTestResult",
        EptsReportUtils.map(this.montlyCascadeReportDimensions.getPositiveTestResults(), mappings));

    dataSetDefinition.addDimension(
        "negativeTestResult",
        EptsReportUtils.map(this.montlyCascadeReportDimensions.getNegativeTestResults(), mappings));

    dataSetDefinition.addDimension(
        "tblam-grade-level",
        EptsReportUtils.map(
            this.montlyCascadeReportDimensions.getTBLAMGradeLevelDimension(), mappings));

    this.addSection1And2(dataSetDefinition, mappings);
    this.addSection3(dataSetDefinition, mappingsPreviousPeriod);
    this.addSEction4(dataSetDefinition, mappingsPreviousPeriod);
    this.addSection5(dataSetDefinition, mappingsPreviousPeriod);
    this.addSection6A(dataSetDefinition, mappingsPreviousPeriod);
    this.addSection6B(dataSetDefinition, mappingsPreviousPeriod);
    this.addSection7(dataSetDefinition, mappingsPreviousPeriod);
    this.addSection8(dataSetDefinition, mappingsPreviousPeriod);

    return dataSetDefinition;
  }

  private void addSection1And2(
      CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {

    final CohortIndicator txCurrIndicator =
        this.eptsGeneralIndicator.getIndicator(
            "findPatientsWhoAreActiveOnART",
            EptsReportUtils.map(txCurrCohortQueries.findPatientsWhoAreActiveOnART(), mappings));

    dataSetDefinition.addColumn(
        "TC",
        "1. TX_CURR: Number of patients currently receiving ART - Total",
        EptsReportUtils.map(txCurrIndicator, mappings),
        "");

    this.addRow(
        dataSetDefinition,
        "TCN",
        "1. TX_CURR: Number of patients currently receiving ART (New on ART)",
        EptsReportUtils.map(txCurrIndicator, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt");

    this.addRow(
        dataSetDefinition,
        "TCP",
        "1. TX_CURR: Number of patients currently receiving ART (Previously on ART)",
        EptsReportUtils.map(txCurrIndicator, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt");

    dataSetDefinition.addColumn(
        "TC-CC",
        "2. TX_CURR with clinical consultation in last 6 months - Total",
        EptsReportUtils.map(txCurrIndicator, mappings),
        "clinicalConsultation=clinicalConsultationNewly");

    this.addRow(
        dataSetDefinition,
        "TC-CC-N",
        "2. TX_CURR with clinical consultation in last 6 months (New on ART) ",
        EptsReportUtils.map(txCurrIndicator, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt|clinicalConsultation=clinicalConsultationNewly");

    this.addRow(
        dataSetDefinition,
        "TC-CC-P",
        "2. TX_CURR with clinical consultation in last 6 months (Previously on ART) ",
        EptsReportUtils.map(txCurrIndicator, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt|clinicalConsultation=clinicalConsultationNewly");
  }

  private void addSection3(CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {

    CohortIndicator txTBDenominatorPreviousPeriod =
        this.getIndicator(this.txtbCohortQueries.getDenominator(), mappings);

    dataSetDefinition.addColumn(
        "TC-TB",
        "3.a TX_TB denominator - Total",
        EptsReportUtils.map(txTBDenominatorPreviousPeriod, mappings),
        "");

    this.addRow(
        dataSetDefinition,
        "TC-TB-N",
        "3.a TX_TB denominator (New On ART)",
        EptsReportUtils.map(txTBDenominatorPreviousPeriod, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt");

    this.addRow(
        dataSetDefinition,
        "TC-TB-P",
        "3.a TX_TB denominator (Previously on ART)",
        EptsReportUtils.map(txTBDenominatorPreviousPeriod, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt");

    CohortIndicator tbDenominatorAndTxCurr =
        this.getIndicator(this.tb4MontlyCascadeCohortQuery.getTxBTDenominatorAndTxCurr());
    dataSetDefinition.addColumn(
        "TBD-TC",
        "3.b.TX_TB denominator not died not transferred-out - Total",
        EptsReportUtils.map(tbDenominatorAndTxCurr, mappings),
        "");

    this.addRow(
        dataSetDefinition,
        "TBD-TC-N",
        "3.b.TX_TB denominator not died not transferred-out (New On ART)",
        EptsReportUtils.map(tbDenominatorAndTxCurr, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt");

    this.addRow(
        dataSetDefinition,
        "TBD-TC-P",
        "3.b.TX_TB denominator not died not transferred-out (Previously on ART)",
        EptsReportUtils.map(tbDenominatorAndTxCurr, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt");
  }

  private void addSEction4(CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {

    CohortIndicator denominatorAndPosetiveScreening =
        getIndicator(this.txtbCohortQueries.getDenominatorAndPositiveScreening(mappings));

    dataSetDefinition.addColumn(
        "TBD-PS",
        "4a. Positive TB screening or specimen sent or notified with TB - Total",
        EptsReportUtils.map(denominatorAndPosetiveScreening, mappings),
        "");

    this.addRow(
        dataSetDefinition,
        "TBD-PS-N",
        "4a. Positive TB screening or specimen sent or notified with TB (New On ART)",
        EptsReportUtils.map(denominatorAndPosetiveScreening, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt");

    this.addRow(
        dataSetDefinition,
        "TBD-PS-P",
        "4a. Positive TB screening or specimen sent or notified with TB (Previously on ART)",
        EptsReportUtils.map(denominatorAndPosetiveScreening, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt");

    CohortIndicator denominatorAndNegativeScreening =
        this.getIndicator(
            this.tb4MontlyCascadeCohortQuery.getTxTBDenominatorAndNegativeScreening());

    dataSetDefinition.addColumn(
        "TBD-NS",
        "4b. Negative TB screening or specimen sent or notified with TB - Total",
        EptsReportUtils.map(denominatorAndNegativeScreening, mappings),
        "");

    this.addRow(
        dataSetDefinition,
        "TBD-NS-N",
        "4b. Negative TB screening or specimen sent or notified with TB (New On ART)",
        EptsReportUtils.map(denominatorAndNegativeScreening, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt");

    this.addRow(
        dataSetDefinition,
        "TBD-NS-P",
        "4b. Negative TB screening or specimen sent or notified with TB (Previously on ART)",
        EptsReportUtils.map(denominatorAndNegativeScreening, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt");
  }

  private void addSection5(CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {

    CohortIndicator specimenSet =
        this.getIndicator(this.txtbCohortQueries.getSpecimenSentCohortDefinition(mappings));

    dataSetDefinition.addColumn(
        "TBD-SP",
        "5. Screened patients with specimen sent -Total",
        EptsReportUtils.map(specimenSet, mappings),
        "");

    dataSetDefinition.addColumn(
        "TBD-SP-baciloscopia",
        "5. Screened patients with specimen sent -Total",
        EptsReportUtils.map(specimenSet, mappings),
        "diagnostictest=baciloscopia");

    this.addRow(
        dataSetDefinition,
        "TBD-SP-N",
        "5. Smear Only (Previously on ART) ",
        EptsReportUtils.map(specimenSet, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt|diagnostictest=baciloscopia");

    this.addRow(
        dataSetDefinition,
        "TBD-SP-P",
        "Smear Only (Previously on ART)",
        EptsReportUtils.map(specimenSet, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt|diagnostictest=baciloscopia");

    // mWRD
    dataSetDefinition.addColumn(
        "TBD-SP-mWRD",
        " 5. Screened patients with mWRD MTB/RIF",
        EptsReportUtils.map(specimenSet, mappings),
        "diagnostictest=genexpert");

    this.addRow(
        dataSetDefinition,
        "TBD-MD-N",
        "5. Screened patients with mWRD MTB/RIF (Previously on ART) ",
        EptsReportUtils.map(specimenSet, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt|diagnostictest=genexpert");

    this.addRow(
        dataSetDefinition,
        "TBD-MD-P",
        "5.Screened patients with mWRD MTB/RIF (Previously on ART)",
        EptsReportUtils.map(specimenSet, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt|diagnostictest=genexpert");

    // TB LAM
    dataSetDefinition.addColumn(
        "TBD-SP-tblam",
        " 5. Screened patients with TB LAM",
        EptsReportUtils.map(specimenSet, mappings),
        "diagnostictest=tblam");

    this.addRow(
        dataSetDefinition,
        "TBD-TBL-N",
        "5. TBL-SP-tblam (New on ART) ",
        EptsReportUtils.map(specimenSet, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt|diagnostictest=tblam");

    this.addRow(
        dataSetDefinition,
        "TBD-TBL-P",
        "Screened patients with TB LAM (Previously on ART)",
        EptsReportUtils.map(specimenSet, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt|diagnostictest=tblam");

    // Other

    dataSetDefinition.addColumn(
        "TBD-SP-other",
        " 5. Screened patients with Additional test other",
        EptsReportUtils.map(specimenSet, mappings),
        "diagnostictest=additonalDiagnostic");

    this.addRow(
        dataSetDefinition,
        "TBD-OD-N",
        "5. Screened patients with Additional test other (New on ART) ",
        EptsReportUtils.map(specimenSet, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt|diagnostictest=additonalDiagnostic");

    this.addRow(
        dataSetDefinition,
        "TBD-OD-P",
        "Screened patients with Additional test other (Previously on ART)",
        EptsReportUtils.map(specimenSet, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt|diagnostictest=additonalDiagnostic");
  }

  private void addSection6A(CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {
    CohortIndicator positiveResults =
        this.getIndicator(
            this.tb4MontlyCascadeCohortQuery.getDenominatorAndPositiveOrNegativeCohort(
                this.tb4MontlyCascadeCohortQuery.getAllPositiveTestResults(),
                "startDate=${endDate-6m+1d},endDate=${endDate},location=${location}"));

    dataSetDefinition.addColumn(
        "TBD-PR",
        "6a.Screened patients with positive TB testing result -Total",
        EptsReportUtils.map(positiveResults, mappings),
        "");

    dataSetDefinition.addColumn(
        "TBD-PR-baciloscopia",
        "6a.Screened patients with positive TB testing result -Total",
        EptsReportUtils.map(positiveResults, mappings),
        "diagnostictest=baciloscopia");

    this.addRow(
        dataSetDefinition,
        "TBD-PR-N",
        "6a.Screened patients with positive TB testing result (New on ART) ",
        EptsReportUtils.map(positiveResults, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt|diagnostictest=baciloscopia");

    this.addRow(
        dataSetDefinition,
        "TBD-PR-P",
        "6a.Screened patients with positive TB testing result (Previously on ART)",
        EptsReportUtils.map(positiveResults, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt|diagnostictest=baciloscopia");

    // mWRD
    dataSetDefinition.addColumn(
        "TBD-PR-mWRD",
        " 6a. Screened patients with positive TB testing result mWRD MTB/RIF",
        EptsReportUtils.map(positiveResults, mappings),
        "diagnostictest=genexpert");

    this.addRow(
        dataSetDefinition,
        "TBD-PRM-N",
        "6a. Screened patients with positive TB testing result mWRD MTB/RIF (Previously on ART) ",
        EptsReportUtils.map(positiveResults, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt|diagnostictest=genexpert");

    this.addRow(
        dataSetDefinition,
        "TBD-PRM-P",
        "6a.Screened patients with positive TB testing resultmWRD MTB/RIF (Previously on ART)",
        EptsReportUtils.map(positiveResults, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt|diagnostictest=genexpert");

    // TB LAM
    dataSetDefinition.addColumn(
        "TBD-PRL-tblam",
        " 6a. Screened patients with positive TB testing result TB LAM",
        EptsReportUtils.map(positiveResults, mappings),
        "diagnostictest=tblam");

    dataSetDefinition.addColumn(
        "TBD-PRL-N-GRADE4",
        " 6a. Screened patients with positive TB testing result TB LAM (Positivity Grade) - grade 4+",
        EptsReportUtils.map(positiveResults, mappings),
        "diagnostictest=tblam|tblam-grade-level=four");

    dataSetDefinition.addColumn(
        "TBD-PRL-N-GRADE3",
        " 6a. Screened patients with positive TB testing result TB LAM (Positivity Grade) - grade 3+",
        EptsReportUtils.map(positiveResults, mappings),
        "diagnostictest=tblam|tblam-grade-level=three");

    dataSetDefinition.addColumn(
        "TBD-PRL-N-GRADE2",
        " 6a. Screened patients with positive TB testing result TB LAM (Positivity Grade) - grade 2+",
        EptsReportUtils.map(positiveResults, mappings),
        "diagnostictest=tblam|tblam-grade-level=two");

    dataSetDefinition.addColumn(
        "TBD-PRL-N-GRADE1",
        " 6a. Screened patients with positive TB testing result TB LAM (Positivity Grade) - grade 1+",
        EptsReportUtils.map(positiveResults, mappings),
        "diagnostictest=tblam|tblam-grade-level=one");

    dataSetDefinition.addColumn(
        "TBD-PRL-N-GRADE0",
        " 6a. Screened patients with positive TB testing result TB LAM (Positivity Grade) - grade not reported",
        EptsReportUtils.map(positiveResults, mappings),
        "diagnostictest=tblam|tblam-grade-level=no-level");

    this.addRow(
        dataSetDefinition,
        "TBD-PRL-N",
        "6. Screened patients with positive TB testing result-tblam (New on ART) ",
        EptsReportUtils.map(positiveResults, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt|diagnostictest=tblam");

    this.addRow(
        dataSetDefinition,
        "TBD-PRL-P",
        "6a.Screened patients with positive TB testing result TB LAM (Previously on ART)",
        EptsReportUtils.map(positiveResults, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt|diagnostictest=tblam");
    // Other

    dataSetDefinition.addColumn(
        "TBD-PRO-other",
        " 6a. Screened patients with positive TB testing result with Additional test other",
        EptsReportUtils.map(positiveResults, mappings),
        "diagnostictest=additonalDiagnostic");

    this.addRow(
        dataSetDefinition,
        "TBD-PRO-N",
        "6a. Screened patients with positive TB testing result with Additional test other (New on ART) ",
        EptsReportUtils.map(positiveResults, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt|diagnostictest=additonalDiagnostic");

    this.addRow(
        dataSetDefinition,
        "TBD-PRO-P",
        "6a.Screened patients with positive TB testing result with Additional test other (Previously on ART)",
        EptsReportUtils.map(positiveResults, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt|diagnostictest=additonalDiagnostic");
  }

  private void addSection6B(CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {
    CohortIndicator negativeResults =
        this.getIndicator(
            this.tb4MontlyCascadeCohortQuery.getDenominatorAndPositiveOrNegativeCohort(
                this.tb4MontlyCascadeCohortQuery.getAllNegativeTestResults(),
                "startDate=${endDate-6m+1d},endDate=${endDate},location=${location}"));

    dataSetDefinition.addColumn(
        "TBD-NR",
        "6b.Screened patients with negative TB testing result -Total ",
        EptsReportUtils.map(negativeResults, mappings),
        "");

    dataSetDefinition.addColumn(
        "TBD-NR-baciloscopia",
        "6b.Screened patients with negative TB testing result - Baciloscopia",
        EptsReportUtils.map(negativeResults, mappings),
        "diagnostictest=baciloscopia|negativeTestResult=negativeBaciloscopia");

    this.addRow(
        dataSetDefinition,
        "TBD-NR-N",
        "6b.Screened patients with negative TB testing result (New on ART) ",
        EptsReportUtils.map(negativeResults, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt|diagnostictest=baciloscopia|negativeTestResult=negativeBaciloscopia");

    this.addRow(
        dataSetDefinition,
        "TBD-NR-P",
        "6b.Screened patients with negative TB testing result (Previously on ART)",
        EptsReportUtils.map(negativeResults, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt|diagnostictest=baciloscopia|negativeTestResult=negativeBaciloscopia");

    // mWRD
    dataSetDefinition.addColumn(
        "TBD-NRM-mWRD",
        " 6b.Screened patients with negative TB testing  result mWRD MTB/RIF",
        EptsReportUtils.map(negativeResults, mappings),
        "diagnostictest=genexpert|negativeTestResult=negativeGenexpert");

    this.addRow(
        dataSetDefinition,
        "TBD-NRM-N",
        "6b.Screened patients with negative TB testing result tmWRD MTB/RIF (Previously on ART) ",
        EptsReportUtils.map(negativeResults, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt|diagnostictest=genexpert|negativeTestResult=negativeGenexpert");

    this.addRow(
        dataSetDefinition,
        "TBD-NRM-P",
        "6b.Screened patients with negative TB testing result mWRD MTB/RIF (Previously on ART)",
        EptsReportUtils.map(negativeResults, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt|diagnostictest=genexpert|negativeTestResult=negativeGenexpert");

    // TB LAM
    dataSetDefinition.addColumn(
        "TBD-NR-tblam",
        " 6b.Screened patients with negative TB testing result TB testing result TB LAM",
        EptsReportUtils.map(negativeResults, mappings),
        "diagnostictest=tblam|negativeTestResult=negativeTblam");

    this.addRow(
        dataSetDefinition,
        "TBD-NRL-N",
        "6b.Screened patients with negative TB testing result-tblam (New on ART) ",
        EptsReportUtils.map(negativeResults, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt|diagnostictest=tblam|negativeTestResult=negativeTblam");

    this.addRow(
        dataSetDefinition,
        "TBD-NRL-P",
        "6b.Screened patients with negative TB testing result TB LAM (Previously on ART)",
        EptsReportUtils.map(negativeResults, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt|diagnostictest=tblam|negativeTestResult=negativeTblam");
    // Other

    dataSetDefinition.addColumn(
        "TBD-NR-other",
        " 6b.Screened patients with negative TB testing result with Additional test other",
        EptsReportUtils.map(negativeResults, mappings),
        "diagnostictest=additonalDiagnostic|negativeTestResult=negativeAdditonalDiagnostic");

    this.addRow(
        dataSetDefinition,
        "TBD-NRO-N",
        "6b.Screened patients with negative TB testing result with Additional test other (New on ART) ",
        EptsReportUtils.map(negativeResults, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt|diagnostictest=additonalDiagnostic|negativeTestResult=negativeAdditonalDiagnostic");

    this.addRow(
        dataSetDefinition,
        "TBD-NRO-P",
        "6b.Screened patients with negative TB testing result with Additional test other (Previously on ART)",
        EptsReportUtils.map(negativeResults, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt|diagnostictest=additonalDiagnostic|negativeTestResult=negativeAdditonalDiagnostic");
  }

  private void addSection7(CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {
    CohortIndicator startedTBTreatment =
        this.getIndicator(this.tb4MontlyCascadeCohortQuery.getPositiveResultAndTXTBNumerator());

    dataSetDefinition.addColumn(
        "TBD-STB",
        "7. Screened patients with pos TB testing result who initiated treatment -Total ",
        EptsReportUtils.map(startedTBTreatment, mappings),
        "");

    dataSetDefinition.addColumn(
        "TBD-STB-baciloscopia",
        "7. Screened patients with pos TB testing result who initiated treatment - Baciloscopia",
        EptsReportUtils.map(startedTBTreatment, mappings),
        "diagnostictest=baciloscopia");

    this.addRow(
        dataSetDefinition,
        "TBD-STB-N",
        "7. Screened patients with pos TB testing result who initiated treatment (New on ART) ",
        EptsReportUtils.map(startedTBTreatment, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt|diagnostictest=baciloscopia");

    this.addRow(
        dataSetDefinition,
        "TBD-STB-P",
        "7. Screened patients with pos TB testing result who initiated treatment (Previously on ART)",
        EptsReportUtils.map(startedTBTreatment, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt|diagnostictest=baciloscopia");

    // mWRD
    dataSetDefinition.addColumn(
        "TBD-STB-mWRD",
        " 7. Screened patients with pos TB testing result who initiated treatment result mWRD MTB/RIF",
        EptsReportUtils.map(startedTBTreatment, mappings),
        "diagnostictest=genexpert");

    this.addRow(
        dataSetDefinition,
        "TBD-STBM-N",
        "7. Screened patients with pos TB testing result who initiated treatment tmWRD MTB/RIF (Previously on ART) ",
        EptsReportUtils.map(startedTBTreatment, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt|diagnostictest=genexpert");

    this.addRow(
        dataSetDefinition,
        "TBD-STBM-P",
        "7. Screened patients with pos TB testing result who initiated treatment mWRD MTB/RIF (Previously on ART)",
        EptsReportUtils.map(startedTBTreatment, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt|diagnostictest=genexpert");

    // TB LAM
    dataSetDefinition.addColumn(
        "TBD-STBL-tblam",
        " 7. Screened patients with pos TB testing result who initiated treatment result TB LAM",
        EptsReportUtils.map(startedTBTreatment, mappings),
        "diagnostictest=tblam");

    dataSetDefinition.addColumn(
        "TBD-STBL-GRADE4",
        " 7. Screened patients with pos TB testing result who initiated treatment result TB LAM (Positivity Grade) - grade 4+",
        EptsReportUtils.map(startedTBTreatment, mappings),
        "diagnostictest=tblam|tblam-grade-level=four");

    dataSetDefinition.addColumn(
        "TBD-STBL-GRADE3",
        " 7. Screened patients with pos TB testing result who initiated treatment result TB LAM (Positivity Grade) - grade 3+",
        EptsReportUtils.map(startedTBTreatment, mappings),
        "diagnostictest=tblam|tblam-grade-level=three");

    dataSetDefinition.addColumn(
        "TBD-STBL-GRADE2",
        " 7. Screened patients with pos TB testing result who initiated treatment result TB LAM (Positivity Grade) - grade 2+",
        EptsReportUtils.map(startedTBTreatment, mappings),
        "tblam-grade-level=two");

    dataSetDefinition.addColumn(
        "TBD-STBL-GRADE1",
        " 7. Screened patients with pos TB testing result who initiated treatment result TB LAM (Positivity Grade) - grade 1+",
        EptsReportUtils.map(startedTBTreatment, mappings),
        "diagnostictest=tblam|tblam-grade-level=one");

    dataSetDefinition.addColumn(
        "TBD-STBL-GRADE0",
        " 7. Screened patients with pos TB testing result who initiated treatment result TB LAM (Positivity Grade) - grade not reported",
        EptsReportUtils.map(startedTBTreatment, mappings),
        "diagnostictest=tblam|tblam-grade-level=no-level");

    this.addRow(
        dataSetDefinition,
        "TBD-STBL-N",
        "7. Screened patients with pos TB testing result who initiated treatment testing result-tblam (New on ART) ",
        EptsReportUtils.map(startedTBTreatment, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt|diagnostictest=tblam");

    this.addRow(
        dataSetDefinition,
        "TBD-STBL-P",
        "7. Screened patients with pos TB testing result who initiated treatment result TB LAM (Previously on ART)",
        EptsReportUtils.map(startedTBTreatment, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt|diagnostictest=tblam");
    // Other

    dataSetDefinition.addColumn(
        "TBD-STBO-other",
        " 7. Screened patients with pos TB testing result who initiated treatment result with Additional test other",
        EptsReportUtils.map(startedTBTreatment, mappings),
        "diagnostictest=additonalDiagnostic");

    this.addRow(
        dataSetDefinition,
        "TBD-STBO-N",
        "7. Screened patients with pos TB testing result who initiated treatment result with Additional test other (New on ART) ",
        EptsReportUtils.map(startedTBTreatment, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt|diagnostictest=additonalDiagnostic");

    this.addRow(
        dataSetDefinition,
        "TBD-STBO-P",
        "7. Screened patients with pos TB testing result who initiated treatment with Additional test other (Previously on ART)",
        EptsReportUtils.map(startedTBTreatment, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt|diagnostictest=additonalDiagnostic");
  }

  private void addSection8(CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {

    CohortIndicator screenedAndStartedTB =
        eptsGeneralIndicator.getIndicator(
            "patientsNewOnARTNumerator",
            EptsReportUtils.map(txtbCohortQueries.txTbNumerator(), mappings));

    dataSetDefinition.addColumn(
        "TBD-SSTB",
        "8a. TX_TB numerator (screened patients initiated TB treatment -Total",
        EptsReportUtils.map(screenedAndStartedTB, mappings),
        "");

    this.addRow(
        dataSetDefinition,
        "TBD-SSTB-N",
        "8a. TX_TB numerator (screened patients initiated TB treatment (New On ART)",
        EptsReportUtils.map(screenedAndStartedTB, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt");

    this.addRow(
        dataSetDefinition,
        "TBD-SSTB-P",
        "8a. TX_TB numerator (screened patients initiated TB treatment (Previously on ART)",
        EptsReportUtils.map(screenedAndStartedTB, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt");

    CohortIndicator txTBAndTXCurr =
        this.getIndicator(
            this.tb4MontlyCascadeCohortQuery.getScreenedPatientsWhoStartedTBTreatmentAndTXCurr());

    dataSetDefinition.addColumn(
        "TBD-TBTC",
        "8b. TX_TB numerator not died not transferred-out (screened patients initiated TB treatment) -Total",
        EptsReportUtils.map(txTBAndTXCurr, mappings),
        "");

    this.addRow(
        dataSetDefinition,
        "TBD-TBTC-N",
        "8b. TX_TB numerator not died not transferred-out (screened patients initiated TB treatment) (New On ART)",
        EptsReportUtils.map(txTBAndTXCurr, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrNewlyOnArt");

    this.addRow(
        dataSetDefinition,
        "TBD-TBTC-P",
        "8b. TX_TB numerator not died not transferred-out (screened patients initiated TB treatment) (Previously on ART)",
        EptsReportUtils.map(txTBAndTXCurr, mappings),
        this.getColumnsForAgeDesaggregation(),
        mappings,
        "artStartState=txcurrPreviouslyOnArt");
  }

  private void addRow(
      CohortIndicatorDataSetDefinition dataSetDefinition,
      String indicatorPrefix,
      String baseLabel,
      Mapped<CohortIndicator> mappedIndicator,
      List<ColumnParameters> columns,
      String mappings,
      String dimensions) {

    dataSetDefinition.addColumn(
        indicatorPrefix + "-total",
        indicatorPrefix + ": " + baseLabel,
        mappedIndicator,
        dimensions);

    dataSetDefinition.addColumn(
        indicatorPrefix + "-TotalMale",
        indicatorPrefix + " - Age and Gender (Totals male) ",
        mappedIndicator,
        "gender=M|" + dimensions);
    dataSetDefinition.addColumn(
        indicatorPrefix + "-TotalFemale",
        indicatorPrefix + " - Age and Gender (Totals female) ",
        mappedIndicator,
        "gender=F|" + dimensions);

    for (ColumnParameters column : columns) {
      String name = indicatorPrefix + "-" + column.getName();
      String label = baseLabel + " (" + column.getLabel() + ")";
      dataSetDefinition.addColumn(
          name, label, mappedIndicator, column.getDimensions() + "|" + dimensions);
    }
  }

  private List<ColumnParameters> getColumnsForAgeDesaggregation() {

    ColumnParameters under15M =
        new ColumnParameters("under15M", "under 15 year male", "gender=M|age=<15", "01");
    ColumnParameters above15M =
        new ColumnParameters("above15M", "above 15 year male", "gender=M|age=15+", "02");
    ColumnParameters unknownM =
        new ColumnParameters("unknownM", "Unknown age male", "gender=M|age=UK", "03");

    ColumnParameters under15F =
        new ColumnParameters("under15F", "under 15 year female", "gender=F|age=<15", "04");
    ColumnParameters above15F =
        new ColumnParameters("above15F", "above 15 year female", "gender=F|age=15+", "05");
    ColumnParameters unknownF =
        new ColumnParameters("unknownF", "Unknown age female", "gender=F|age=UK", "06");

    return Arrays.asList(under15M, above15M, unknownM, under15F, above15F, unknownF);
  }

  private CohortIndicator getIndicator(CohortDefinition cohortDefinition) {
    String mappings = "endDate=${endDate},location=${location}";
    return this.eptsGeneralIndicator.getIndicator(
        "" + cohortDefinition, EptsReportUtils.map(cohortDefinition, mappings));
  }

  private CohortIndicator getIndicator(CohortDefinition cohortDefinition, String mappings) {
    return this.eptsGeneralIndicator.getIndicator(
        "" + cohortDefinition, EptsReportUtils.map(cohortDefinition, mappings));
  }
}
