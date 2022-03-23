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
import org.openmrs.module.eptsreports.reporting.library.cohorts.Eri2MonthsCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.GenericCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.library.queries.Eri2MonthsQueriesInterface;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.eptsreports.reporting.utils.ErimType;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Eri2MonthsDataset extends BaseDataSet {

  @Autowired private EptsCommonDimension eptsCommonDimension;

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired private Eri2MonthsCohortQueries eri2MonthsCohortQueries;

  @Autowired private GenericCohortQueries genericCohortQueries;

  public DataSetDefinition constructEri2MonthsDatset() {

    final CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.setName("ERI-2months Data Set");
    dsd.addParameters(this.getParameters());

    dsd.addDimension(
        "state", EptsReportUtils.map(this.eptsCommonDimension.getEri2MonthsDimension2(), mappings));

    this.addRow(
        dsd,
        "R21",
        "All Patients retained on ART 2 months after ART initiation",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "All patients",
                EptsReportUtils.map(
                    this.genericCohortQueries.generalSql(
                        "All patients",
                        Eri2MonthsQueriesInterface.QUERY
                            .findPatientsWhoHaveEitherClinicalConsultationOrDrugsPickup33DaysForASpecificPatientType(
                                ErimType.TOTAL)),
                    mappings)),
            mappings),
        this.get2MonthsRetentionColumns());

    this.addRow(
        dsd,
        "R22",
        "Pregnant women retained on ART 2 months after ART initiation",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "Pregnant women",
                EptsReportUtils.map(
                    this.eri2MonthsCohortQueries.getEri2MonthsPregnatCompositionCohort("Pregnant"),
                    mappings)),
            mappings),
        this.get2MonthsRetentionColumns());

    this.addRow(
        dsd,
        "R23",
        "Breastfeeding women retained on ART 2 months after ART initiation",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "Breastfeeding women",
                EptsReportUtils.map(
                    this.eri2MonthsCohortQueries.getEri2MonthsBrestfeetingCompositionCohort(
                        "BrestFeeting"),
                    mappings)),
            mappings),
        this.get2MonthsRetentionColumns());

    this.addRow(
        dsd,
        "R24",
        "Children (0-14, excluding pregnant and breastfeeding women) retained on ART 2 months",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "Children",
                EptsReportUtils.map(
                    this.eri2MonthsCohortQueries.getEri2MonthsChildCompositionCohort("Children"),
                    mappings)),
            mappings),
        this.get2MonthsRetentionColumns());

    this.addRow(
        dsd,
        "R25",
        "Adults (15+, excluding pregnant and breastfeeding women)  retained on ART 2 months",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "Adult",
                EptsReportUtils.map(
                    this.eri2MonthsCohortQueries.getEri2MonthsAdultCompositionCohort("Adult"),
                    mappings)),
            mappings),
        this.get2MonthsRetentionColumns());

    return dsd;
  }

  private List<ColumnParameters> get2MonthsRetentionColumns() {
    final ColumnParameters artStart =
        new ColumnParameters("initiated ART", "Initiated ART", "state=IART", "01");
    final ColumnParameters didNotPickUpDrugs =
        new ColumnParameters("did Not pick drugs", "Did Not pick up drugs", "state=DNPUD", "02");
    final ColumnParameters pickedUpDrugs =
        new ColumnParameters("picked drugs", "Picked up Drugs", "state=PUD", "03");
    final ColumnParameters dead = new ColumnParameters("dead", "Dead", "state=DP", "04");
    final ColumnParameters transfers =
        new ColumnParameters("transfers", "Transferred Out", "state=TOP", "05");
    final ColumnParameters stopped =
        new ColumnParameters("stopped", "Stopped treatment", "state=STP", "06");
    return Arrays.asList(artStart, didNotPickUpDrugs, pickedUpDrugs, dead, transfers, stopped);
  }
}
