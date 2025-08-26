package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.Arrays;
import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.TB7AdvancedDiseaseAndTBCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.dimensions.AgeDimensionCohortInterface;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.dimensions.TB7CascadeReportDimensions;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TB7AdvancedDiseaseAndTBDataSet extends BaseDataSet {

  @Autowired private TB7AdvancedDiseaseAndTBCohortQueries tb7AdvancedDiseaseAndTBCohortQueries;
  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired
  @Qualifier("commonAgeDimensionCohort")
  private AgeDimensionCohortInterface ageDimensionCohort;

  @Autowired private EptsCommonDimension eptsCommonDimension;

  @Autowired private TB7CascadeReportDimensions tb7CascadeReportDimensions;

  public DataSetDefinition constructDataset() {

    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dataSetDefinition.setName("TB7 Data Set");

    dataSetDefinition.addParameters(this.getParameters());

    dataSetDefinition.addDimension(
        "cascadeOneIndicatorOneDimension",
        EptsReportUtils.map(
            this.tb7CascadeReportDimensions.getCascadeOneIndicatorOneDimension(), mappings));

    dataSetDefinition.addDimension(
        "cascadeOneIndicatorTwoDimension",
        EptsReportUtils.map(
            this.tb7CascadeReportDimensions.getCascadeOneIndicatorTwoDimension(), mappings));

    dataSetDefinition.addDimension(
        "cascadeOneIndicatorThreeDimension",
        EptsReportUtils.map(
            this.tb7CascadeReportDimensions.getCascadeOneIndicatorThreeDimension(), mappings));

    dataSetDefinition.addDimension(
        "cascadeOneIndicatorFourDimension",
        EptsReportUtils.map(
            this.tb7CascadeReportDimensions.getCascadeOneIndicatorFourDimension(), mappings));

    dataSetDefinition.addDimension(
        "tblam-grade-level",
        EptsReportUtils.map(
            this.tb7CascadeReportDimensions.getTBLAMGradeLevelDimension(), mappings));

    dataSetDefinition.addDimension("gender", EptsReportUtils.map(eptsCommonDimension.gender(), ""));

    dataSetDefinition.addDimension(
        "age",
        EptsReportUtils.map(
            eptsCommonDimension.age(ageDimensionCohort), "effectiveDate=${endDate}"));

    dataSetDefinition.setName("TB7");
    dataSetDefinition.addParameters(this.getParameters());

    final CohortIndicator tb7Indicator1 =
        this.eptsGeneralIndicator.getIndicator(
            "TB7",
            EptsReportUtils.map(
                this.tb7AdvancedDiseaseAndTBCohortQueries
                    .getNumberOfClientsEligibleForCD4CountRequestDuringInclusionPeriodIndicator1(),
                mappings));

    final CohortIndicator tb7Indicator2 =
        this.eptsGeneralIndicator.getIndicator(
            "TB7",
            EptsReportUtils.map(
                this.tb7AdvancedDiseaseAndTBCohortQueries
                    .getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator2(),
                mappings));

    final CohortIndicator tb7Indicator3 =
        this.eptsGeneralIndicator.getIndicator(
            "TB7",
            EptsReportUtils.map(
                this.tb7AdvancedDiseaseAndTBCohortQueries
                    .getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator3(),
                mappings));

    final CohortIndicator tb7Indicator4 =
        this.eptsGeneralIndicator.getIndicator(
            "TB7",
            EptsReportUtils.map(
                this.tb7AdvancedDiseaseAndTBCohortQueries
                    .getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicator4(),
                mappings));

    final CohortIndicator tb7Indicator1Cascade2 =
        this.eptsGeneralIndicator.getIndicator(
            "TB7",
            EptsReportUtils.map(
                this.tb7AdvancedDiseaseAndTBCohortQueries
                    .getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicatorCascade2(),
                mappings));

    final CohortIndicator tb7Indicator1Cascade2WithTBLAMResults =
        this.eptsGeneralIndicator.getIndicator(
            "TB7",
            EptsReportUtils.map(
                this.tb7AdvancedDiseaseAndTBCohortQueries
                    .getNumberOfClientsWithCd4ResultDuringInclusionPeriodIndicatorCascade2WithTBLAMResults(),
                mappings));

    final CohortIndicator tb7ImmunoCD4WithTBLamPositiveResults =
        this.eptsGeneralIndicator.getIndicator(
            "TB7",
            EptsReportUtils.map(
                this.tb7AdvancedDiseaseAndTBCohortQueries
                    .getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMPositiveResults(),
                mappings));

    final CohortIndicator tb7ImmunoCD4WithTBLamNegativeResults =
        this.eptsGeneralIndicator.getIndicator(
            "TB7",
            EptsReportUtils.map(
                this.tb7AdvancedDiseaseAndTBCohortQueries
                    .getNumberOfClientsWithNegativeTBLAMResults(),
                mappings));

    final CohortIndicator tb7WithoutGeneExpert =
        this.eptsGeneralIndicator.getIndicator(
            "TB7",
            EptsReportUtils.map(
                this.tb7AdvancedDiseaseAndTBCohortQueries
                    .getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMPositiveResultsWithoutGenexpert(),
                mappings));

    final CohortIndicator tb7WithGeneExpert =
        this.eptsGeneralIndicator.getIndicator(
            "TB7",
            EptsReportUtils.map(
                this.tb7AdvancedDiseaseAndTBCohortQueries
                    .getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMPositiveResultsWithGenexpert(),
                mappings));

    final CohortIndicator tb7WithGeneExpertPositiveResults =
        this.eptsGeneralIndicator.getIndicator(
            "TB7",
            EptsReportUtils.map(
                this.tb7AdvancedDiseaseAndTBCohortQueries
                    .getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMPositiveResultsWithGenexpertPositiveResults(),
                mappings));

    final CohortIndicator tb7AndTBTreatment =
        this.eptsGeneralIndicator.getIndicator(
            "TB7",
            EptsReportUtils.map(
                this.tb7AdvancedDiseaseAndTBCohortQueries
                    .getNumberOfClientsWithCD4ShowingImmunoSuppressionandWithTBLAMPositiveResultsAndInitiatedTBTreatment(),
                mappings));

    // TI1
    dataSetDefinition.addColumn(
        "TI1",
        "TI1-Number of clients eligible for CD4 count during the inclusion period",
        EptsReportUtils.map(tb7Indicator1, mappings),
        "");

    addRow(
        dataSetDefinition,
        "EL",
        "Number of clients eligible for CD4 count during the inclusion period",
        EptsReportUtils.map(tb7Indicator1, mappings),
        getColumns());

    dataSetDefinition.addColumn(
        "TI1-newlyOnArt",
        "TI1-Number of clients eligible for CD4 count during the inclusion period - Newly On Art",
        EptsReportUtils.map(tb7Indicator1, mappings),
        "cascadeOneIndicatorOneDimension=artInitiation");

    dataSetDefinition.addColumn(
        "TI1-pregnant",
        "TI1-Number of clients eligible for CD4 count during the inclusion period - Pregnant",
        EptsReportUtils.map(tb7Indicator1, mappings),
        "cascadeOneIndicatorOneDimension=pregnant");

    dataSetDefinition.addColumn(
        "TI1-consecutiveHighVL",
        "TI1-Number of clients eligible for CD4 count during the inclusion period - Consecutive High VL",
        EptsReportUtils.map(tb7Indicator1, mappings),
        "cascadeOneIndicatorOneDimension=consecutiveHighVL");

    dataSetDefinition.addColumn(
        "TI1-artRestart",
        "TI1-Number of clients eligible for CD4 count during the inclusion period - Art Restart",
        EptsReportUtils.map(tb7Indicator1, mappings),
        "cascadeOneIndicatorOneDimension=artRestart");

    // TI2
    dataSetDefinition.addColumn(
        "TI2",
        "TI2-Number of clients eligible for CD4 count who have a CD4 count result during the inclusion period",
        EptsReportUtils.map(tb7Indicator2, mappings),
        "");

    addRow(
        dataSetDefinition,
        "RL",
        "TI2-Number of clients eligible for CD4 count who have a CD4 count result during the inclusion period",
        EptsReportUtils.map(tb7Indicator2, mappings),
        getColumns());

    dataSetDefinition.addColumn(
        "TI2-newlyOnArt",
        "TI2-Number of clients eligible for CD4 count who have a CD4 count result during the inclusion period - Newly On Art",
        EptsReportUtils.map(tb7Indicator2, mappings),
        "cascadeOneIndicatorTwoDimension=artInitiation");

    dataSetDefinition.addColumn(
        "TI2-pregnant",
        "TI2-Number of clients eligible for CD4 count who have a CD4 count result during the inclusion period - Pregnant",
        EptsReportUtils.map(tb7Indicator2, mappings),
        "cascadeOneIndicatorTwoDimension=pregnant");

    dataSetDefinition.addColumn(
        "TI2-consecutiveHighVL",
        "TI2-Number of clients eligible for CD4 count who have a CD4 count result during the inclusion period - Consecutive High VL",
        EptsReportUtils.map(tb7Indicator2, mappings),
        "cascadeOneIndicatorTwoDimension=consecutiveHighVL");

    dataSetDefinition.addColumn(
        "TI2-artRestart",
        "TI2-Number of clients eligible for CD4 count who have a CD4 count result during the inclusion period - Art Restart",
        EptsReportUtils.map(tb7Indicator2, mappings),
        "cascadeOneIndicatorTwoDimension=artRestart");

    // T3
    dataSetDefinition.addColumn(
        "TI3",
        "TI3-Number of clients with CD4 count showing severe immunosuppression during the inclusion period",
        EptsReportUtils.map(tb7Indicator3, mappings),
        "");

    addRow(
        dataSetDefinition,
        "RM",
        "TI3-Number of clients with a CD4 count showing severe immunosuppression during the inclusion period",
        EptsReportUtils.map(tb7Indicator3, mappings),
        getColumns());

    dataSetDefinition.addColumn(
        "TI3-newlyOnArt",
        "TI3-Number of clients with a CD4 count showing severe immunosuppression during the inclusion period - Newly On Art",
        EptsReportUtils.map(tb7Indicator3, mappings),
        "cascadeOneIndicatorThreeDimension=artInitiation");

    dataSetDefinition.addColumn(
        "TI3-pregnant",
        "TI3-Number of clients with a CD4 count showing severe immunosuppression during the inclusion period - Pregnant",
        EptsReportUtils.map(tb7Indicator3, mappings),
        "cascadeOneIndicatorThreeDimension=pregnant");

    dataSetDefinition.addColumn(
        "TI3-consecutiveHighVL",
        "TI3-Number of clients with a CD4 count showing severe immunosuppression during the inclusion period - Consecutive High VL",
        EptsReportUtils.map(tb7Indicator3, mappings),
        "cascadeOneIndicatorThreeDimension=consecutiveHighVL");

    dataSetDefinition.addColumn(
        "TI3-artRestart",
        "TI3-Number of clients with a CD4 count showing severe immunosuppression during the inclusion period - Art Restart",
        EptsReportUtils.map(tb7Indicator3, mappings),
        "cascadeOneIndicatorThreeDimension=artRestart");

    // T4
    dataSetDefinition.addColumn(
        "TI4",
        "TI4-Number of clients eligible for CD4 with CD4 count showing severe immunosuppression and who have a TB LAM result by report generation date",
        EptsReportUtils.map(tb7Indicator4, mappings),
        "");

    addRow(
        dataSetDefinition,
        "RN",
        "TI4-Number of clients eligible for CD4 with CD4 count showing severe immunosuppression and who have a TB LAM result by report generation date",
        EptsReportUtils.map(tb7Indicator4, mappings),
        getColumns());

    dataSetDefinition.addColumn(
        "TI4-newlyOnArt",
        "TI4-Number of clients eligible for CD4 with CD4 count showing severe immunosuppression and who have a TB LAM result by report generation date - Newly On Art",
        EptsReportUtils.map(tb7Indicator4, mappings),
        "cascadeOneIndicatorFourDimension=artInitiation");

    dataSetDefinition.addColumn(
        "TI4-pregnant",
        "TI4-Number of clients eligible for CD4 with CD4 count showing severe immunosuppression and who have a TB LAM result by report generation date - Pregnant",
        EptsReportUtils.map(tb7Indicator4, mappings),
        "cascadeOneIndicatorFourDimension=pregnant");

    dataSetDefinition.addColumn(
        "TI4-consecutiveHighVL",
        "TI4-Number of clients eligible for CD4 with CD4 count showing severe immunosuppression and who have a TB LAM result by report generation date - Consecutive High VL",
        EptsReportUtils.map(tb7Indicator4, mappings),
        "cascadeOneIndicatorFourDimension=consecutiveHighVL");

    dataSetDefinition.addColumn(
        "TI4-artRestart",
        "TI4-Number of clients eligible for CD4 with CD4 count showing severe immunosuppression and who have a TB LAM result by report generation date - Art Restart",
        EptsReportUtils.map(tb7Indicator4, mappings),
        "cascadeOneIndicatorFourDimension=artRestart");

    // Cascate 2

    dataSetDefinition.addColumn(
        "CASCADE2-TI1",
        "Number of clients with a CD4 count during inclusion period (end date - 2 months + 1 day) and (end date - 1 month) showing severe immunosuppression",
        EptsReportUtils.map(tb7Indicator1Cascade2, mappings),
        "");

    dataSetDefinition.addColumn(
        "CASCADE2-TI2",
        "Number of clients with CD4 count showing severe immunosuppression and who have a TB LAM result during the inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month))",
        EptsReportUtils.map(tb7Indicator1Cascade2WithTBLAMResults, mappings),
        "");

    dataSetDefinition.addColumn(
        "CASCADE2-TI2-GRADE4",
        "Number of clients with CD4 count showing severe immunosuppression and who have a TB LAM result during the inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month)) - grade 4+",
        EptsReportUtils.map(tb7Indicator1Cascade2WithTBLAMResults, mappings),
        "tblam-grade-level=four");

    dataSetDefinition.addColumn(
        "CASCADE2-TI2-GRADE3",
        "Number of clients with CD4 count showing severe immunosuppression and who have a TB LAM result during the inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month)) - grade 3+",
        EptsReportUtils.map(tb7Indicator1Cascade2WithTBLAMResults, mappings),
        "tblam-grade-level=three");

    dataSetDefinition.addColumn(
        "CASCADE2-TI2-GRADE2",
        "Number of clients with CD4 count showing severe immunosuppression and who have a TB LAM result during the inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month)) - grade 2+",
        EptsReportUtils.map(tb7Indicator1Cascade2WithTBLAMResults, mappings),
        "tblam-grade-level=two");

    dataSetDefinition.addColumn(
        "CASCADE2-TI2-GRADE1",
        "Number of clients with CD4 count showing severe immunosuppression and who have a TB LAM result during the inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month)) - grade 1+",
        EptsReportUtils.map(tb7Indicator1Cascade2WithTBLAMResults, mappings),
        "tblam-grade-level=one");

    dataSetDefinition.addColumn(
        "CASCADE2-TI2-GRADENONE",
        "Number of clients with CD4 count showing severe immunosuppression and who have a TB LAM result during the inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month)) - grade not reported",
        EptsReportUtils.map(tb7Indicator1Cascade2WithTBLAMResults, mappings),
        "tblam-grade-level=no-level");

    dataSetDefinition.addColumn(
        "CASCADE2-TI2-POSITIVE",
        "Number of clients with CD4 count showing severe immunosuppression and with positive TB LAM during inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month))",
        EptsReportUtils.map(tb7ImmunoCD4WithTBLamPositiveResults, mappings),
        "");

    dataSetDefinition.addColumn(
        "CASCADE2-TI2-NEGATIVE",
        "Number of clients with a CD4 count during inclusion period (end date - 2 months + 1 day) and (end date - 1 month) showing severe immunosuppression - Negative Results",
        EptsReportUtils.map(tb7ImmunoCD4WithTBLamNegativeResults, mappings),
        "");

    // tb7WithoutGeneExpert
    dataSetDefinition.addColumn(
        "CASCADE2-TI2-WITHOUT-GEN",
        "Number of clients with positive TB LAM during inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month)) but NOT tested with PCR TB by report generation date",
        EptsReportUtils.map(tb7WithoutGeneExpert, mappings),
        "");

    // tb7WithGeneExpert
    dataSetDefinition.addColumn(
        "CASCADE2-TI2-WITH-GEN",
        "Number of clients with positive TB LAM during inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month)) and also tested with PCR TB by report generation date",
        EptsReportUtils.map(tb7WithGeneExpert, mappings),
        "");

    // tb7WithGeneExpertPositiveResults
    dataSetDefinition.addColumn(
        "CASCADE2-TI2-WITH-GEN-POS",
        "Number of clients with positive TB LAM during inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month)) and PCR TB positive for TB by report generation date",
        EptsReportUtils.map(tb7WithGeneExpertPositiveResults, mappings),
        "");

    // .tb7AndTBTreatment
    dataSetDefinition.addColumn(
        "CASCADE2-TI4",
        "Number of clients with positive TB LAM during inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month)) and on TB treatment by report generation date",
        EptsReportUtils.map(tb7AndTBTreatment, mappings),
        "");

    dataSetDefinition.addColumn(
        "CASCADE2-TI4-GRADE4",
        "Number of clients with positive TB LAM during inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month)) and on TB treatment by report generation date - grade 4+",
        EptsReportUtils.map(tb7AndTBTreatment, mappings),
        "tblam-grade-level=four");

    dataSetDefinition.addColumn(
        "CASCADE2-TI4-GRADE3",
        "Number of clients with positive TB LAM during inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month)) and on TB treatment by report generation date - grade 3+",
        EptsReportUtils.map(tb7AndTBTreatment, mappings),
        "tblam-grade-level=three");

    dataSetDefinition.addColumn(
        "CASCADE2-TI4-GRADE2",
        "Number of clients with positive TB LAM during inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month)) and on TB treatment by report generation date - grade 2+",
        EptsReportUtils.map(tb7AndTBTreatment, mappings),
        "tblam-grade-level=two");

    dataSetDefinition.addColumn(
        "CASCADE2-TI4-GRADE1",
        "Number of clients with positive TB LAM during inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month)) and on TB treatment by report generation date - grade 1+",
        EptsReportUtils.map(tb7AndTBTreatment, mappings),
        "tblam-grade-level=one");

    dataSetDefinition.addColumn(
        "CASCADE2-TI4-GRADENONE",
        "Number of clients with positive TB LAM during inclusion period (between (end date - 2 months + 1 day) and (end date - 1 month)) and on TB treatment by report generation date - grade not reported",
        EptsReportUtils.map(tb7AndTBTreatment, mappings),
        "tblam-grade-level=no-level");

    return dataSetDefinition;
  }

  private List<ColumnParameters> getColumns() {

    ColumnParameters a1 = new ColumnParameters("<1", "<1", "gender=F|age=<1", "01");
    ColumnParameters a2 = new ColumnParameters("1-4", "1-4 years female", "gender=F|age=1-4", "02");
    ColumnParameters a3 = new ColumnParameters("5-9", "5-9 years female", "gender=F|age=5-9", "03");
    ColumnParameters a4 = new ColumnParameters("10-14", "10-14 female", "gender=F|age=10-14", "04");
    ColumnParameters a5 = new ColumnParameters("15-19", "15-19 female", "gender=F|age=15-19", "05");
    ColumnParameters a6 = new ColumnParameters("20+", "20+ female", "gender=F|age=20+", "06");
    ColumnParameters unknownF =
        new ColumnParameters("unknownF", "Unknown age", "gender=F|age=UK", "07");

    ColumnParameters a7 = new ColumnParameters("<1", "<1 male", "gender=M|age=<1", "08");
    ColumnParameters a8 = new ColumnParameters("1-4", "1-4 years male", "gender=M|age=1-4", "09");
    ColumnParameters a9 = new ColumnParameters("5-9", "5-9 years male", "gender=M|age=5-9", "10");
    ColumnParameters a10 = new ColumnParameters("10-14", "10-14 male", "gender=M|age=10-14", "11");
    ColumnParameters a11 = new ColumnParameters("15-19", "15-19 male", "gender=M|age=15-19", "12");
    ColumnParameters a12 = new ColumnParameters("20+", "20+ male", "gender=M|age=20+", "13");
    ColumnParameters unknownM =
        new ColumnParameters("unknownF", "Unknown age", "gender=M|age=UK", "14");

    return Arrays.asList(a1, a2, a3, a4, a5, a6, a7, a8, unknownF, a9, a10, a11, a12, unknownM);
  }
}
