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
import org.openmrs.module.eptsreports.reporting.library.cohorts.TXTBCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.dimensions.AgeDimensionCohortInterface;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TxTBDataset extends BaseDataSet {
  @Autowired private EptsCommonDimension eptsCommonDimension;

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired private TXTBCohortQueries txTbCohortQueries;

  @Autowired
  @Qualifier("commonAgeDimensionCohort")
  private AgeDimensionCohortInterface ageDimensionCohort;

  public DataSetDefinition constructTxTBDataset() {
    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";
    CohortIndicatorDataSetDefinition dataSetDefinition = new CohortIndicatorDataSetDefinition();
    dataSetDefinition.setName("TX_TB Data Set");
    dataSetDefinition.addParameters(getParameters());

    dataSetDefinition.addDimension("gender", EptsReportUtils.map(eptsCommonDimension.gender(), ""));
    dataSetDefinition.addDimension(
        "age",
        EptsReportUtils.map(
            eptsCommonDimension.age(ageDimensionCohort), "effectiveDate=${endDate}"));

    addTXTBDenominator(mappings, dataSetDefinition);
    addTXTBNumerator(mappings, dataSetDefinition);
    addSpecimenSentDisaggregation(mappings, dataSetDefinition);
    addDiagnositcTestDisaggregation(mappings, dataSetDefinition);
    addPositiveResultsDisaggregation(mappings, dataSetDefinition);
    addCXRDisaggregation(mappings, dataSetDefinition);

    return dataSetDefinition;
  }

  private void addCXRDisaggregation(
      String mappings, CohortIndicatorDataSetDefinition dataSetDefinition) {
    CohortIndicator specimentSent =
        eptsGeneralIndicator.getIndicator(
            "CX", EptsReportUtils.map(txTbCohortQueries.getPatientWhoAreCXR(), mappings));

    dataSetDefinition.addColumn(
        "TX_TB_TOTAL_CXR",
        "TX_TB: Patients screened using CXR - Total",
        EptsReportUtils.map(specimentSent, mappings),
        "");
  }

  private void addTXTBNumerator(
      String mappings, CohortIndicatorDataSetDefinition dataSetDefinition) {
    CohortIndicator numerator =
        eptsGeneralIndicator.getIndicator(
            "NUMERATOR", EptsReportUtils.map(txTbCohortQueries.txTbNumerator(), mappings));

    CohortIndicator patientsPreviouslyOnARTNumerator =
        eptsGeneralIndicator.getIndicator(
            "patientsPreviouslyOnARTNumerator",
            EptsReportUtils.map(txTbCohortQueries.patientsPreviouslyOnARTNumerator(), mappings));
    CohortIndicator patientsNewOnARTNumerator =
        eptsGeneralIndicator.getIndicator(
            "patientsNewOnARTNumerator",
            EptsReportUtils.map(txTbCohortQueries.patientsNewOnARTNumerator(), mappings));

    dataSetDefinition.addColumn(
        "TXB_NUM",
        "TX_TB: Patients diagnosed with TB and started on TB treatment (Numerator total)",
        EptsReportUtils.map(numerator, mappings),
        "");
    addRow(
        dataSetDefinition,
        "TXB_NUM_PREV",
        "Numerator (Patients Already on ART)",
        EptsReportUtils.map(patientsPreviouslyOnARTNumerator, mappings),
        getAdultChildrenColumns());
    addRow(
        dataSetDefinition,
        "TXB_NUM_NEW",
        "Numerator (Patients New on ART)",
        EptsReportUtils.map(patientsNewOnARTNumerator, mappings),
        getAdultChildrenColumns());
  }

  private void addTXTBDenominator(
      String mappings, CohortIndicatorDataSetDefinition dataSetDefinition) {

    CohortIndicator previouslyOnARTPostiveScreening =
        eptsGeneralIndicator.getIndicator(
            "previouslyOnARTPositiveScreening",
            EptsReportUtils.map(txTbCohortQueries.previouslyOnARTPositiveScreening(), mappings));

    CohortIndicator previouslyOnARTNegativeScreening =
        eptsGeneralIndicator.getIndicator(
            "previouslyOnARTNegativeScreening",
            EptsReportUtils.map(txTbCohortQueries.previouslyOnARTNegativeScreening(), mappings));

    CohortIndicator newOnARTPositiveScreening =
        eptsGeneralIndicator.getIndicator(
            "newOnARTPositiveScreening",
            EptsReportUtils.map(txTbCohortQueries.newOnARTPositiveScreening(), mappings));

    CohortIndicator newOnARTNegativeScreening =
        eptsGeneralIndicator.getIndicator(
            "newOnARTNegativeScreening",
            EptsReportUtils.map(txTbCohortQueries.newOnARTNegativeScreening(), mappings));

    dataSetDefinition.addColumn(
        "TXB_DEN",
        "TX_TB: Patients on ART screened for TB(Denominator Total)",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "Denominator Total",
                EptsReportUtils.map(txTbCohortQueries.getDenominator(), mappings)),
            mappings),
        "");

    addRow(
        dataSetDefinition,
        "TXB_DEN_NEW_POS",
        "Denominator (Patients New on ART Positive Screening)",
        EptsReportUtils.map(newOnARTPositiveScreening, mappings),
        getAdultChildrenColumns());
    addRow(
        dataSetDefinition,
        "TXB_DEN_NEW_NEG",
        "Denominator (Patients New on ART Negative Screening)",
        EptsReportUtils.map(newOnARTNegativeScreening, mappings),
        getAdultChildrenColumns());
    addRow(
        dataSetDefinition,
        "TXB_DEN_PREV_POS",
        "Denominator (Patients Already on ART Positive Screening)",
        EptsReportUtils.map(previouslyOnARTPostiveScreening, mappings),
        getAdultChildrenColumns());
    addRow(
        dataSetDefinition,
        "TXB_DEN_PREV_NEG",
        "Denominator (Patients Already on ART Negative Screening)",
        EptsReportUtils.map(previouslyOnARTNegativeScreening, mappings),
        getAdultChildrenColumns());
  }

  private void addSpecimenSentDisaggregation(
      String mappings, CohortIndicatorDataSetDefinition dataSetDefinition) {

    CohortIndicator specimentSent =
        eptsGeneralIndicator.getIndicator(
            "SPECIMEN-SENT",
            EptsReportUtils.map(
                txTbCohortQueries.getSpecimenSentCohortDefinition(mappings), mappings));

    dataSetDefinition.addColumn(
        "TX_TB_TOTAL_SPECIMEN_SENT",
        "TX_TB: Total Patients With Specimen Sent",
        EptsReportUtils.map(specimentSent, mappings),
        "");
  }

  private void addDiagnositcTestDisaggregation(
      String mappings, CohortIndicatorDataSetDefinition dataSetDefinition) {

    CohortIndicator geneExpert =
        eptsGeneralIndicator.getIndicator(
            "GENEXPERT-DIAGNOSTIC-TEST",
            EptsReportUtils.map(
                txTbCohortQueries.getDiagnosticTestCohortDefinitionMWRS(), mappings));

    CohortIndicator smearOnly =
        eptsGeneralIndicator.getIndicator(
            "SMEAR-ONLY-DIAGNOSTIC-TEST",
            EptsReportUtils.map(
                txTbCohortQueries.getDiagnosticTestSmearMicroscopyOnly(), mappings));

    CohortIndicator otherNoExpert =
        eptsGeneralIndicator.getIndicator(
            "OTHER-NO-EXPERT-DIAGNOSTIC-TEST",
            EptsReportUtils.map(
                txTbCohortQueries.getDiagnosticTestCohortDefinitionOther(), mappings));

    CohortIndicator symptomScreenAlone =
        eptsGeneralIndicator.getIndicator(
            "OTHER-NO-EXPERT-DIAGNOSTIC-TEST",
            EptsReportUtils.map(txTbCohortQueries.getSymptomScreenAlone(), mappings));

    dataSetDefinition.addColumn(
        "TX_TB_TOTAL_GENEXPERT_DIAGNOSTIC",
        "TX_TB: mWRD (with or without other testing)",
        EptsReportUtils.map(geneExpert, mappings),
        "");
    dataSetDefinition.addColumn(
        "TX_TB_TOTAL_SMEAR_ONLY_DIAGNOSTIC",
        "TX_TB: Smear microscopy only ",
        EptsReportUtils.map(smearOnly, mappings),
        "");
    dataSetDefinition.addColumn(
        "TX_TB_TOTAL_OTHER-NO-EXPERT-DIAGNOSTIC",
        "TX_TB: Additional test other than mWRD",
        EptsReportUtils.map(otherNoExpert, mappings),
        "");

    dataSetDefinition.addColumn(
        "TX_TB_TOTAL_SYMPTOM_SCREEN",
        "Patients screened using Symptom Screen (alone) - Total",
        EptsReportUtils.map(symptomScreenAlone, mappings),
        "");
  }

  private void addPositiveResultsDisaggregation(
      String mappings, CohortIndicatorDataSetDefinition dataSetDefinition) {

    CohortIndicator positiveResults =
        eptsGeneralIndicator.getIndicator(
            "POSITIVE-RESULT",
            EptsReportUtils.map(
                txTbCohortQueries.getDenominatorAndPositiveResults(mappings), mappings));

    dataSetDefinition.addColumn(
        "TX_TB_TOTAL_POSITIVE_RESULT",
        "TX_TB: Total Patients With Positive Results",
        EptsReportUtils.map(positiveResults, mappings),
        "");
  }

  private List<ColumnParameters> getAdultChildrenColumns() {
    // Male
    ColumnParameters under1M =
        new ColumnParameters("under1M", "under 1 year male", "gender=M|age=<1", "01");
    ColumnParameters oneTo4M =
        new ColumnParameters("oneTo4M", "1 - 4 years male", "gender=M|age=1-4", "02");
    ColumnParameters fiveTo9M =
        new ColumnParameters("fiveTo9M", "5 - 9 years male", "gender=M|age=5-9", "03");
    ColumnParameters tenTo14M =
        new ColumnParameters("tenTo14M", "10 - 14 male", "gender=M|age=10-14", "04");
    ColumnParameters fifteenTo19M =
        new ColumnParameters("fifteenTo19M", "15 - 19 male", "gender=M|age=15-19", "05");
    ColumnParameters twentyTo24M =
        new ColumnParameters("twentyTo24M", "20 - 24 male", "gender=M|age=20-24", "06");
    ColumnParameters twenty5To29M =
        new ColumnParameters("twenty4To29M", "25 - 29 male", "gender=M|age=25-29", "07");
    ColumnParameters thirtyTo34M =
        new ColumnParameters("thirtyTo34M", "30 - 34 male", "gender=M|age=30-34", "08");
    ColumnParameters thirty5To39M =
        new ColumnParameters("thirty5To39M", "35 - 39 male", "gender=M|age=35-39", "09");
    ColumnParameters foutyTo44M =
        new ColumnParameters("foutyTo44M", "40 - 44 male", "gender=M|age=40-44", "10");
    ColumnParameters fouty5To49M =
        new ColumnParameters("fouty5To49M", "45 - 49 male", "gender=M|age=45-49", "11");

    ColumnParameters fiftyT054 =
        new ColumnParameters("fiftyT054", "50 - 54 male", "gender=M|age=50-54", "12");

    ColumnParameters fiftyfiveT059 =
        new ColumnParameters("fouty5To49M", "55 - 59 male", "gender=M|age=55-59", "13");

    ColumnParameters sixtyT064 =
        new ColumnParameters("fiftyfiveT059", "60 - 64 male", "gender=M|age=60-64", "14");

    ColumnParameters above65 =
        new ColumnParameters("above65", "65+  male", "gender=M|age=65+", "15");
    ColumnParameters unknownM =
        new ColumnParameters("unknownM", "Unknown age male", "gender=M|age=UK", "16");

    // Female
    ColumnParameters under1F =
        new ColumnParameters("under1F", "under 1 year female", "gender=F|age=<1", "17");
    ColumnParameters oneTo4F =
        new ColumnParameters("oneTo4F", "1 - 4 years female", "gender=F|age=1-4", "18");
    ColumnParameters fiveTo9F =
        new ColumnParameters("fiveTo9F", "5 - 9 years female", "gender=F|age=5-9", "19");
    ColumnParameters tenTo14F =
        new ColumnParameters("tenTo14F", "10 - 14 female", "gender=F|age=10-14", "20");
    ColumnParameters fifteenTo19F =
        new ColumnParameters("fifteenTo19F", "15 - 19 female", "gender=F|age=15-19", "21");
    ColumnParameters twentyTo24F =
        new ColumnParameters("twentyTo24F", "20 - 24 female", "gender=F|age=20-24", "22");
    ColumnParameters twenty5To29F =
        new ColumnParameters("twenty4To29F", "25 - 29 female", "gender=F|age=25-29", "23");
    ColumnParameters thirtyTo34F =
        new ColumnParameters("thirtyTo34F", "30 - 34 female", "gender=F|age=30-34", "24");
    ColumnParameters thirty5To39F =
        new ColumnParameters("thirty5To39F", "35 - 39 female", "gender=F|age=35-39", "25");
    ColumnParameters foutyTo44F =
        new ColumnParameters("foutyTo44F", "40 - 44 female", "gender=F|age=40-44", "26");
    ColumnParameters fouty5To49F =
        new ColumnParameters("fouty5To49F", "45 - 49 female", "gender=F|age=45-49", "27");

    ColumnParameters fiftyT054F =
        new ColumnParameters("fiftyT054F", "50 - 54 female", "gender=F|age=50-54", "28");

    ColumnParameters fiftyfiveT059F =
        new ColumnParameters("fiftyfiveT059F", "55 - 59 female", "gender=F|age=55-59", "29");

    ColumnParameters sixtyT064F =
        new ColumnParameters("sixtyT064F", "60 - 64 female", "gender=F|age=60-64", "30");

    ColumnParameters above65F =
        new ColumnParameters("above65", "65+ female", "gender=F|age=65+", "31");

    ColumnParameters unknownF =
        new ColumnParameters("unknownF", "Unknown age female", "gender=F|age=UK", "32");

    return Arrays.asList(
        unknownM,
        under1M,
        oneTo4M,
        fiveTo9M,
        tenTo14M,
        fifteenTo19M,
        twentyTo24M,
        twenty5To29M,
        thirtyTo34M,
        thirty5To39M,
        foutyTo44M,
        fouty5To49M,
        unknownF,
        under1F,
        oneTo4F,
        fiveTo9F,
        tenTo14F,
        fifteenTo19F,
        twentyTo24F,
        twenty5To29F,
        thirtyTo34F,
        thirty5To39F,
        foutyTo44F,
        fouty5To49F,
        fiftyT054,
        fiftyfiveT059,
        sixtyT064,
        above65,
        fiftyT054F,
        fiftyfiveT059F,
        sixtyT064F,
        above65F);
  }
}
