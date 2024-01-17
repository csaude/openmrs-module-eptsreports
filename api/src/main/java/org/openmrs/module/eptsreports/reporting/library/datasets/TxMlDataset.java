package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.module.eptsreports.reporting.library.cohorts.TxMlCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.dimensions.AgeDimensionCohortInterface;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.dimensions.KeyPopulationDimension;
import org.openmrs.module.eptsreports.reporting.library.dimensions.TxMLDimensions;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TxMlDataset extends BaseDataSet {

  @Autowired private EptsCommonDimension eptsCommonDimension;

  @Autowired
  @Qualifier("commonAgeDimensionCohort")
  private AgeDimensionCohortInterface ageDimensionCohort;

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired private TxMlCohortQueries txMlCohortQueries;

  @Autowired private TxMLDimensions txMLDimensions;

  @Autowired private KeyPopulationDimension keyPopulationDimension;

  public DataSetDefinition constructtxMlDataset() {
    CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
    dsd.setName("Tx_Ml Data Set");
    dsd.addParameters(getParameters());
    String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    CohortDefinition patientsWhoMissedNextApointment =
        txMlCohortQueries.getPatientsWhoMissedNextApointment();
    CohortDefinition iitLessThan3Months =
        this.txMlCohortQueries.getPatientsWhoAreIITLessThan3Months();
    CohortDefinition iitBetween3And5Months =
        this.txMlCohortQueries.getPatientsWhoAreIITBetween3And5Months();
    CohortDefinition iitGreaterOrEqual6Months =
        this.txMlCohortQueries.getPatientsWhoAreIITGreaterOrEqual6Months();

    CohortDefinition patientstotalIITCohortDefinitions =
        this.txMlCohortQueries.getPatientstotalIIT();

    final CohortIndicator patientsWhoMissedNextApointmentIndicator =
        this.eptsGeneralIndicator.getIndicator(
            "findPatientsWhoMissedNextApointment",
            EptsReportUtils.map(patientsWhoMissedNextApointment, mappings));
    final CohortIndicator iitLessThan3MonthsIndicator =
        this.eptsGeneralIndicator.getIndicator(
            "iitLessThan3Months", EptsReportUtils.map(iitLessThan3Months, mappings));
    final CohortIndicator iitGreaterOrEqual6MonthsIndicator =
        this.eptsGeneralIndicator.getIndicator(
            "iitGreaterOrEqual6Months", EptsReportUtils.map(iitGreaterOrEqual6Months, mappings));
    final CohortIndicator iitBetween3And5MonthsIndicator =
        this.eptsGeneralIndicator.getIndicator(
            "iitBetween3And5Months", EptsReportUtils.map(iitBetween3And5Months, mappings));

    final CohortIndicator patientstotalIITIndicator =
        this.eptsGeneralIndicator.getIndicator(
            "patientstotalIITIndicator",
            EptsReportUtils.map(patientstotalIITCohortDefinitions, mappings));

    dsd.addDimension("gender", EptsReportUtils.map(eptsCommonDimension.gender(), ""));
    dsd.addDimension(
        "age",
        EptsReportUtils.map(
            eptsCommonDimension.age(ageDimensionCohort), "effectiveDate=${endDate}"));
    dsd.addDimension(
        "dead", EptsReportUtils.map(this.txMLDimensions.findPatientsWhoAreAsDead(), mappings));
    dsd.addDimension(
        "transferedout",
        EptsReportUtils.map(this.txMLDimensions.findPatientsWhoAreTransferedOut(), mappings));
    dsd.addDimension(
        "refusedorstoppedtreatment",
        EptsReportUtils.map(
            this.txMLDimensions.findPatientsWhoRefusedOrStoppedTreatment(), mappings));

    dsd.addDimension(
        "iitless3months",
        EptsReportUtils.map(this.txMLDimensions.findPatientsIITLess3Months(), mappings));

    dsd.addDimension(
        "iitbetween3and5months",
        EptsReportUtils.map(this.txMLDimensions.findPatientsIITBetween3And5Months(), mappings));

    dsd.addDimension(
        "iitgreaterorequal6months",
        EptsReportUtils.map(this.txMLDimensions.findPatientsIITGreaterOrEqual6Months(), mappings));

    dsd.addDimension(
        "homosexual",
        EptsReportUtils.map(this.keyPopulationDimension.findPatientsWhoAreHomosexual(), mappings));
    dsd.addDimension(
        "drug-user",
        EptsReportUtils.map(this.keyPopulationDimension.findPatientsWhoUseDrugs(), mappings));
    dsd.addDimension(
        "prisioner",
        EptsReportUtils.map(this.keyPopulationDimension.findPatientsWhoAreInPrison(), mappings));
    dsd.addDimension(
        "sex-worker",
        EptsReportUtils.map(this.keyPopulationDimension.findPatientsWhoAreSexWorker(), mappings));

    dsd.addColumn(
        "ML1",
        "Total missed appointments",
        EptsReportUtils.map(patientsWhoMissedNextApointmentIndicator, mappings),
        "");
    super.addRow(
        dsd,
        "ML2",
        "Age and Gender",
        EptsReportUtils.map(patientsWhoMissedNextApointmentIndicator, mappings),
        getColumnsForAgeAndGender());

    dsd.addColumn(
        "ML2-TotalMale",
        " Age and Gender (Totals male) ",
        EptsReportUtils.map(patientsWhoMissedNextApointmentIndicator, mappings),
        "gender=M");
    dsd.addColumn(
        "ML2-TotalFemale",
        "Age and Gender (Totals female) ",
        EptsReportUtils.map(patientsWhoMissedNextApointmentIndicator, mappings),
        "gender=F");

    dsd.addColumn(
        "ML4-TotalIIT", "Total IIT", EptsReportUtils.map(patientstotalIITIndicator, mappings), "");

    super.addRow(
        dsd,
        "ML4",
        "IIT < 90 days",
        EptsReportUtils.map(iitLessThan3MonthsIndicator, mappings),
        getColumnsForAgeAndGender());
    dsd.addColumn(
        "ML4-TotalMale",
        "IIT < 90 days (Totals male) ",
        EptsReportUtils.map(iitLessThan3MonthsIndicator, mappings),
        "gender=M");
    dsd.addColumn(
        "ML4-TotalFemale",
        "IIT < 90 days (Totals female) ",
        EptsReportUtils.map(iitLessThan3MonthsIndicator, mappings),
        "gender=F");

    super.addRow(
        dsd,
        "ML5",
        "IIT >= 180 days",
        EptsReportUtils.map(iitGreaterOrEqual6MonthsIndicator, mappings),
        getColumnsForAgeAndGender());
    dsd.addColumn(
        "ML5-TotalMale",
        "IIT >= 180 days (Totals male) ",
        EptsReportUtils.map(iitGreaterOrEqual6MonthsIndicator, mappings),
        "gender=M");
    dsd.addColumn(
        "ML5-TotalFemale",
        "IIT >= 180 days (Totals female) ",
        EptsReportUtils.map(iitGreaterOrEqual6MonthsIndicator, mappings),
        "gender=F");

    super.addRow(
        dsd,
        "ML8",
        "IIT >= 90 days AND IIT < 180 days",
        EptsReportUtils.map(iitBetween3And5MonthsIndicator, mappings),
        getColumnsForAgeAndGender());
    dsd.addColumn(
        "ML8-TotalMale",
        "IIT >= 90 days AND IIT < 180 days (Totals male) ",
        EptsReportUtils.map(iitBetween3And5MonthsIndicator, mappings),
        "gender=M");
    dsd.addColumn(
        "ML8-TotalFemale",
        "IIT >= 90 days AND IIT < 180 days (Totals female) ",
        EptsReportUtils.map(iitBetween3And5MonthsIndicator, mappings),
        "gender=F");

    this.setDeadDimension(
        dsd, EptsReportUtils.map(patientsWhoMissedNextApointmentIndicator, mappings), mappings);
    this.setTransferedDimension(
        dsd, EptsReportUtils.map(patientsWhoMissedNextApointmentIndicator, mappings), mappings);
    this.setRefusedOrStoppedTreatmentDimension(
        dsd, EptsReportUtils.map(patientsWhoMissedNextApointmentIndicator, mappings), mappings);

    this.setKeyPopsDimension(
        dsd,
        EptsReportUtils.map(patientsWhoMissedNextApointmentIndicator, mappings),
        mappings,
        "ML2",
        "TX_ML",
        "");

    this.setKeyPopsDimension(
        dsd,
        EptsReportUtils.map(patientsWhoMissedNextApointmentIndicator, mappings),
        mappings,
        "ML3",
        "Died",
        "dead=dead");

    this.setKeyPopsDimension(
        dsd,
        EptsReportUtils.map(iitLessThan3MonthsIndicator, mappings),
        mappings,
        "ML4",
        "IIT < 3 months",
        "iitless3months=iitless3months");

    this.setKeyPopsDimension(
        dsd,
        EptsReportUtils.map(iitBetween3And5MonthsIndicator, mappings),
        mappings,
        "ML8",
        "IIT for 3-5 months",
        "iitbetween3and5months=iitbetween3and5months");

    this.setKeyPopsDimension(
        dsd,
        EptsReportUtils.map(iitGreaterOrEqual6MonthsIndicator, mappings),
        mappings,
        "ML5",
        "IIT > = 6 months",
        "iitgreaterorequal6months=iitgreaterorequal6months");

    this.setKeyPopsDimension(
        dsd,
        EptsReportUtils.map(patientsWhoMissedNextApointmentIndicator, mappings),
        mappings,
        "ML6",
        "Transfered Out",
        "transferedout=transferedout");

    this.setKeyPopsDimension(
        dsd,
        EptsReportUtils.map(patientsWhoMissedNextApointmentIndicator, mappings),
        mappings,
        "ML7",
        "Refused (Stopped) Treatment",
        "refusedorstoppedtreatment=refusedorstoppedtreatment");

    return dsd;
  }

  private void setDeadDimension(
      final CohortIndicatorDataSetDefinition dataSetDefinition,
      Mapped<? extends CohortIndicator> indicator,
      final String mappings) {
    String dimension = "dead=dead";
    for (ColumnParameters column : getColumnsForAgeAndGender()) {
      String name = "ML3" + "-" + column.getColumn();
      String label = "Dead" + " (" + column.getLabel() + ")";
      String dimensionIter =
          (column.getDimensions().length() > 2)
              ? column.getDimensions() + "|" + dimension
              : dimension;
      dataSetDefinition.addColumn(name, label, indicator, dimensionIter);
    }
    dataSetDefinition.addColumn(
        "ML3-TotalMale", "Dead (Totals male) ", indicator, "gender=M|dead=dead");
    dataSetDefinition.addColumn(
        "ML3-TotalFemale", "Dead (Totals female) ", indicator, "gender=F|dead=dead");
  }

  private void setTransferedDimension(
      final CohortIndicatorDataSetDefinition dataSetDefinition,
      Mapped<? extends CohortIndicator> indicator,
      final String mappings) {
    String dimension = "transferedout=transferedout";
    for (ColumnParameters column : getColumnsForAgeAndGender()) {
      String name = "ML6" + "-" + column.getColumn();
      String label = "Transfered Out" + " (" + column.getLabel() + ")";
      String dimensionIter =
          (column.getDimensions().length() > 2)
              ? column.getDimensions() + "|" + dimension
              : dimension;
      dataSetDefinition.addColumn(name, label, indicator, dimensionIter);
    }
    dataSetDefinition.addColumn(
        "ML6-TotalMale",
        "Transfered Out (Totals male) ",
        indicator,
        "gender=M|transferedout=transferedout");
    dataSetDefinition.addColumn(
        "ML6-TotalFemale",
        "Transfered Out (Totals female) ",
        indicator,
        "gender=F|transferedout=transferedout");
  }

  private void setRefusedOrStoppedTreatmentDimension(
      final CohortIndicatorDataSetDefinition dataSetDefinition,
      Mapped<? extends CohortIndicator> indicator,
      final String mappings) {

    String dimension = "refusedorstoppedtreatment=refusedorstoppedtreatment";
    for (ColumnParameters column : getColumnsForAgeAndGender()) {
      String name = "ML7" + "-" + column.getColumn();
      String label = "Stopped/Refused Treatment" + " (" + column.getLabel() + ")";
      String dimensionIter =
          (column.getDimensions().length() > 2)
              ? column.getDimensions() + "|" + dimension
              : dimension;
      dataSetDefinition.addColumn(name, label, indicator, dimensionIter);
    }
    dataSetDefinition.addColumn(
        "ML7-TotalMale",
        "Stopped/Refused Treatment (Totals male) ",
        indicator,
        "gender=M|refusedorstoppedtreatment=refusedorstoppedtreatment");
    dataSetDefinition.addColumn(
        "ML7-TotalFemale",
        "Stopped/Refused Treatment (Totals female) ",
        indicator,
        "gender=F|refusedorstoppedtreatment=refusedorstoppedtreatment");
  }

  private List<ColumnParameters> getColumnsForAgeAndGender() {
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

    ColumnParameters fiftyT054M =
        new ColumnParameters("fiftyT054", "50 - 54 male", "gender=M|age=50-54", "12");

    ColumnParameters fiftyfiveT059M =
        new ColumnParameters("fouty5To49M", "55 - 59 male", "gender=M|age=55-59", "13");

    ColumnParameters sixtyT064M =
        new ColumnParameters("fiftyfiveT059", "60 - 64 male", "gender=M|age=60-64", "14");

    ColumnParameters above65M =
        new ColumnParameters("above65", "65+  male", "gender=M|age=65+", "15");
    ColumnParameters unknownM =
        new ColumnParameters("unknownM", "Unknown age male", "gender=M|age=UK", "16");

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

    ColumnParameters total = new ColumnParameters("totals", "Totals", "", "33");

    return Arrays.asList(
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
        unknownM,
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
        unknownF,
        fiftyT054M,
        fiftyfiveT059M,
        sixtyT064M,
        above65M,
        fiftyT054F,
        fiftyfiveT059F,
        sixtyT064F,
        above65F,
        total);
  }

  private void setKeyPopsDimension(
      final CohortIndicatorDataSetDefinition dataSetDefinition,
      Mapped<? extends CohortIndicator> indicator,
      final String mappings,
      String columnNamePrefix,
      String columnNameLabel,
      String dimension) {

    String aggregatedDimension = StringUtils.EMPTY;
    if (!StringUtils.isEmpty(dimension)) {
      aggregatedDimension = "|" + dimension;
    } else {
      aggregatedDimension = dimension;
    }

    dataSetDefinition.addColumn(
        columnNamePrefix + "-MSM",
        columnNameLabel + " Homosexual",
        indicator,
        "gender=M|homosexual=homosexual" + aggregatedDimension);

    dataSetDefinition.addColumn(
        columnNamePrefix + "-PWID",
        columnNameLabel + " Drugs User",
        indicator,
        "drug-user=drug-user" + aggregatedDimension);

    dataSetDefinition.addColumn(
        columnNamePrefix + "-PRI",
        columnNameLabel + " Prisioners",
        indicator,
        "prisioner=prisioner" + aggregatedDimension);

    dataSetDefinition.addColumn(
        columnNamePrefix + "-FSW",
        columnNameLabel + " Sex Worker",
        indicator,
        "gender=F|sex-worker=sex-worker" + aggregatedDimension);
  }
}
