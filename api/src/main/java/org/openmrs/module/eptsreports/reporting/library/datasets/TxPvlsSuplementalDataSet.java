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

import org.openmrs.module.eptsreports.reporting.library.cohorts.PvlsCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TxPvlsSuplementalDataSet extends BaseDataSet {

  @Autowired private PvlsCohortQueries pvlsCohortQueries;

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired
  public DataSetDefinition constructTxPvlsSupplementalDataset() {

    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();

    dataSetDefinition.setName("TxPvls Suplemental Data Set");
    dataSetDefinition.addParameters(this.getParameters());

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    final CohortIndicator denominator =
        this.eptsGeneralIndicator.getIndicator(
            "pregnant-denominator",
            EptsReportUtils.map(
                this.pvlsCohortQueries
                    .findPregnantAndBreastfeedingWomenForTxPvlsSupplementalCoverageDenominatorsPatients(),
                mappings));

    final CohortIndicator pregnantDenominator =
        this.eptsGeneralIndicator.getIndicator(
            "pregnant-denominator",
            EptsReportUtils.map(
                this.pvlsCohortQueries
                    .findPregnantWomanForTxPvlsSupplementalCoverageDenominatorsPatients(),
                mappings));

    final CohortIndicator breastFeedingDenominator =
        this.eptsGeneralIndicator.getIndicator(
            "breastfeeding-denominator",
            EptsReportUtils.map(
                this.pvlsCohortQueries
                    .findBreastfeedingWomanForTxPvlsSupplementalCoverageDenominatorsPatients(),
                mappings));

    dataSetDefinition.addColumn(
        "TXPVLS-SUPLEMENTAL-TOTAL",
        "TXPVLS-SUPLEMENTAL: Eligible for a VL test and on ART for 90 days (Pregnant Women)",
        EptsReportUtils.map(denominator, mappings),
        "");

    dataSetDefinition.addColumn(
        "TXPVLS-SUPLEMENTAL-PREGNANT",
        "TXPVLS-SUPLEMENTAL: Eligible for a VL test and on ART for 90 days (Pregnant Women)",
        EptsReportUtils.map(pregnantDenominator, mappings),
        "");

    dataSetDefinition.addColumn(
        "TXPVLS-SUPLEMENTAL-BREASTFEEDING",
        "TXPVLS-SUPLEMENTAL: Eligible for a VL test and on ART for 90 days (Breastfeeding Women)",
        EptsReportUtils.map(breastFeedingDenominator, mappings),
        "");

    return dataSetDefinition;
  }
}
