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
package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.Arrays;
import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.PvlsBySourceCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.dimensions.AgeDimensionCohortInterface;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.dimensions.KeyPopulationDimension;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.library.queries.TxPvlsBySourceQueriesInterface;
import org.openmrs.module.eptsreports.reporting.library.queries.TxPvlsBySourceQueriesInterface.QUERY.SourceType;
import org.openmrs.module.eptsreports.reporting.library.queries.TxPvlsQueriesInterface.QUERY.WomanState;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TxPvlsByMasterCardSourceDataset extends BaseDataSet {

  @Autowired private EptsCommonDimension eptsCommonDimension;

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired private PvlsBySourceCohortQueries pvlsBySourceCohortQueries;

  @Autowired private KeyPopulationDimension keyPopulationDimension;

  @Autowired
  @Qualifier("commonAgeDimensionCohort")
  private AgeDimensionCohortInterface ageDimensionCohort;

  public DataSetDefinition constructTxPvlsDatset() {

    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dataSetDefinition.setName("TX_PVLS Data Set");

    dataSetDefinition.addParameters(this.getParameters());

    dataSetDefinition.addDimension("gender", EptsReportUtils.map(eptsCommonDimension.gender(), ""));

    dataSetDefinition.addDimension(
        "age",
        EptsReportUtils.map(
            eptsCommonDimension.age(ageDimensionCohort), "effectiveDate=${endDate}"));

    final CohortIndicator pvlsByMasterCardDenominator =
        this.eptsGeneralIndicator.getIndicator(
            "PVLS By Source Denominator",
            EptsReportUtils.map(
                pvlsBySourceCohortQueries
                    .findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsBySource(
                        SourceType.MASTERCARD),
                mappings));

    dataSetDefinition.addColumn(
        "PVLS2-DEN-TOTAL",
        "Total PVLS by source denominator (Mastercard Source)",
        EptsReportUtils.map(pvlsByMasterCardDenominator, mappings),
        "");

    addRow(
        dataSetDefinition,
        "PVLS2-DEN",
        "Adults & Children Denominator (Mastercard Source)",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "viral load results adults and children",
                EptsReportUtils.map(
                    this.pvlsBySourceCohortQueries
                        .findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsBySource(
                            SourceType.MASTERCARD),
                    mappings)),
            mappings),
        getAdultChildrenColumns());

    dataSetDefinition.addColumn(
        "PREGNANT2-DEN",
        "PVLS by source denominator - Pregnant Patients (Mastercard Source)",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "Pregant (Mastercard Card)",
                EptsReportUtils.map(
                    pvlsBySourceCohortQueries
                        .findWomanStateWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(
                            SourceType.MASTERCARD, WomanState.PREGNANT),
                    mappings)),
            mappings),
        "");

    dataSetDefinition.addColumn(
        "BREASTFEEDING2-DEN",
        "PVLS by source denominator - Breastfeeding Patients (Mastercard Source)",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "PVLS by source denominator - Breastfeeding Patients (Mastercard Source)",
                EptsReportUtils.map(
                    pvlsBySourceCohortQueries
                        .findWomanStateWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(
                            SourceType.MASTERCARD, WomanState.BREASTFEEDING),
                    mappings)),
            mappings),
        "");

    //// NUMERATOR

    final CohortIndicator pvlsByMasterCardNumerator =
        this.eptsGeneralIndicator.getIndicator(
            "rotine",
            EptsReportUtils.map(
                pvlsBySourceCohortQueries
                    .findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12Months(
                        SourceType.MASTERCARD),
                mappings));

    dataSetDefinition.addColumn(
        "PVLS2-NUM-TOTAL",
        "Total PVLS by source numerator (Mastercard Source)",
        EptsReportUtils.map(pvlsByMasterCardNumerator, mappings),
        "");

    addRow(
        dataSetDefinition,
        "PVLS2-NUM",
        "Adults & Children Numerator (Mastercard Source)",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "viral load results adults and children",
                EptsReportUtils.map(
                    this.pvlsBySourceCohortQueries
                        .findPatientsWhoHaveMoreThan3MonthsOnArtWithViralLoadResultLessthan1000RegisteredInTheLast12Months(
                            SourceType.MASTERCARD),
                    mappings)),
            mappings),
        getAdultChildrenColumns());

    dataSetDefinition.addColumn(
        "PREGNANT2-NUM",
        "PVLS by source numerator - Pregnant Patients (Mastercard Source)",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "Pregant routine",
                EptsReportUtils.map(
                    pvlsBySourceCohortQueries
                        .findPregnantWomanWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsWithVlMoreThan1000(
                            SourceType.MASTERCARD, WomanState.PREGNANT),
                    mappings)),
            mappings),
        "");

    dataSetDefinition.addColumn(
        "BREASTFEEDING2-NUM",
        "PVLS by source numerator - Breastfeeding Patients (Mastercard Source)",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "Breastfeeding routine",
                EptsReportUtils.map(
                    pvlsBySourceCohortQueries
                        .findPregnantBreatsFeedingWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12MonthsWithVlMoreThan1000Rotine(
                            SourceType.MASTERCARD, WomanState.BREASTFEEDING),
                    mappings)),
            mappings),
        "");

    // Key Population dimension

    dataSetDefinition.addDimension(
        "homosexual",
        EptsReportUtils.map(this.keyPopulationDimension.findPatientsWhoAreHomosexual(), mappings));

    dataSetDefinition.addDimension(
        "drug-user",
        EptsReportUtils.map(this.keyPopulationDimension.findPatientsWhoUseDrugs(), mappings));

    dataSetDefinition.addDimension(
        "prisioner",
        EptsReportUtils.map(this.keyPopulationDimension.findPatientsWhoAreInPrison(), mappings));

    dataSetDefinition.addDimension(
        "sex-worker",
        EptsReportUtils.map(this.keyPopulationDimension.findPatientsWhoAreSexWorker(), mappings));

    // Key population collumn denominator

    dataSetDefinition.addColumn(
        "DMSM",
        "Denominator - Homosexual (MasterCard Source)",
        EptsReportUtils.map(pvlsByMasterCardDenominator, mappings),
        "gender=M|homosexual=homosexual");

    dataSetDefinition.addColumn(
        "DPWID",
        "Denominator - Drugs User (Mastercard Source) ",
        EptsReportUtils.map(pvlsByMasterCardDenominator, mappings),
        "drug-user=drug-user");

    dataSetDefinition.addColumn(
        "DPRI",
        "Denominator - Prisioners (Mastercard Source)",
        EptsReportUtils.map(pvlsByMasterCardDenominator, mappings),
        "prisioner=prisioner");

    dataSetDefinition.addColumn(
        "DFSW",
        "Denominator - Sex Worker (Mastercard Source)",
        EptsReportUtils.map(pvlsByMasterCardDenominator, mappings),
        "gender=F|sex-worker=sex-worker");

    // Key population collumn Numerator

    dataSetDefinition.addColumn(
        "NPWID",
        "Numerator - Drugs User (Mastercard Source)",
        EptsReportUtils.map(pvlsByMasterCardNumerator, mappings),
        "drug-user=drug-user");

    dataSetDefinition.addColumn(
        "NMSM",
        "Numerator - Homosexual (Mastercard Source)",
        EptsReportUtils.map(pvlsByMasterCardNumerator, mappings),
        "gender=M|homosexual=homosexual");

    dataSetDefinition.addColumn(
        "NFSW",
        "Numerator - Sex Worker (Mastercard Source)",
        EptsReportUtils.map(pvlsByMasterCardNumerator, mappings),
        "gender=F|sex-worker=sex-worker");

    dataSetDefinition.addColumn(
        "NPRI",
        "Numerator - Prisioners (Mastercard Source)",
        EptsReportUtils.map(pvlsByMasterCardNumerator, mappings),
        "prisioner=prisioner");

    return dataSetDefinition;
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
    ColumnParameters fiftyTo54M =
        new ColumnParameters("fiftyTo54M", "50 - 54 male", "gender=M|age=50-54", "12");
    ColumnParameters fiftyFiveTo59M =
        new ColumnParameters("fiftyFiveTo59M", "55 - 59 male", "gender=M|age=55-59", "13");
    ColumnParameters sixtyTo64M =
        new ColumnParameters("sixtyTo64M", "60 - 64 male", "gender=M|age=60-64", "14");
    ColumnParameters above65M =
        new ColumnParameters("above65M", "65+ male", "gender=M|age=65+", "15");
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
    ColumnParameters fiftyTo54F =
        new ColumnParameters("fiftyTo54F", "50 - 54 female", "gender=F|age=50-54", "28");
    ColumnParameters fiftyFiveTo59F =
        new ColumnParameters("fiftyFiveTo59F", "55 - 59 female", "gender=F|age=55-59", "29");
    ColumnParameters sixtyTo64F =
        new ColumnParameters("sixtyTo64F", "60 - 64 female", "gender=F|age=60-64", "30");
    ColumnParameters above65F =
        new ColumnParameters("above65F", "65+ female", "gender=F|age=65+", "31");
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
        fiftyTo54M,
        fiftyFiveTo59M,
        sixtyTo64M,
        above65M,
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
        fiftyTo54F,
        fiftyFiveTo59F,
        sixtyTo64F,
        above65F);
  }

  public static void main(String[] args) {

    String query =
        TxPvlsBySourceQueriesInterface.QUERY
            .findWomanStateWhoHaveMoreThan3MonthsOnArtWithViralLoadRegisteredInTheLast12Months(
                SourceType.MASTERCARD, WomanState.BREASTFEEDING);

    System.out.println(query);
  }
}
