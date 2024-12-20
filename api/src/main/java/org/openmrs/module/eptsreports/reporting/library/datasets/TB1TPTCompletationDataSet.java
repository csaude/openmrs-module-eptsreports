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

import java.util.ArrayList;
import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.TB1TPTCompletationCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.TxCurrCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TB1TPTCompletationDataSet extends BaseDataSet {

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired private TB1TPTCompletationCohortQueries tb1TPTCompletationCohortQueries;

  @Autowired private TxCurrCohortQueries txCurrCohortQueries;

  public DataSetDefinition constructDatset() {
    CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();
    String mappings = "endDate=${endDate},location=${location}";
    dsd.setName("TPT Completion Cascade Data Set");
    dsd.addParameters(this.getParameters());

    dsd.addColumn(
        "TXCURR",
        "TX_CURR: Number of patients currently receiving ART",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "TX_CURR: Number of patients currently receiving ART",
                EptsReportUtils.map(txCurrCohortQueries.findPatientsWhoAreActiveOnART(), mappings)),
            mappings),
        "");

    dsd.addColumn(
        "TXCURR-TPT-COMPLETION",
        "TX_CURR with TPT Completion",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "TX_CURR with TPT Completion",
                EptsReportUtils.map(
                    tb1TPTCompletationCohortQueries.findTxCurrWithTPTCompletation(), mappings)),
            mappings),
        "");

    dsd.addColumn(
        "TXCURR-TPT-NO-COMPLETION",
        "TX_CURR without TPT Completion",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "TX_CURR without TPT Completion",
                EptsReportUtils.map(
                    tb1TPTCompletationCohortQueries.findTxCurrWithoutTPTCompletation(), mappings)),
            mappings),
        "");

    dsd.addColumn(
        "TXCURR-TPT-NO-COMPLETION-TB",
        "TX_CURR without TPT Completion with TB Treatment(Last 3 Years)",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "TX_CURR without TPT Completion with TB Treatment(Last 3 Years)",
                EptsReportUtils.map(
                    tb1TPTCompletationCohortQueries
                        .findTxCurrWithoutTPTCompletionWhoWereTreatedForTBForLast3Years(),
                    mappings)),
            mappings),
        "");

    dsd.addColumn(
        "TXCURR-TPT-NO-COMPLETION-TB-POSITIVE-SCREENING",
        "TX_CURR without TPT Completion with Positive TB Screening",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "TX_CURR without TPT Completion with Positive TB Screening",
                EptsReportUtils.map(
                    tb1TPTCompletationCohortQueries
                        .findTxCurrWithoutTPTCompletionWithPositivTBScreening(),
                    mappings)),
            mappings),
        "");

    dsd.addColumn(
        "TXCURR-ELIGIBLE-TPT-COMPLETION",
        "TX_CURR eligible for TPT Completion",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "TX_CURR eligible for TPT Completion",
                EptsReportUtils.map(
                    tb1TPTCompletationCohortQueries
                        .findTxCurrWithoutTPTCompletionButEligibleForTPTCompletation(),
                    mappings)),
            mappings),
        "");

    dsd.addColumn(
        "TXCURR-WITH-TPT-7MONTHS",
        "TX_CURR with TPT in last 7 months",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "TX_CURR with TPT in last 7 months",
                EptsReportUtils.map(
                    tb1TPTCompletationCohortQueries
                        .findTxCurrWithoutTPTCompletionWhoInitiatedTPTInLast7Months(),
                    mappings)),
            mappings),
        "");

    dsd.addColumn(
        "TXCURR-ELIGIBLE-TPT-INITIATION",
        "TX_CURR eligible for TPT Initiation",
        EptsReportUtils.map(
            eptsGeneralIndicator.getIndicator(
                "TX_CURR eligible for TPT Initiation",
                EptsReportUtils.map(
                    tb1TPTCompletationCohortQueries
                        .findTxCurrWithoutTPTCompletionButEligibleForTPTInitiation(),
                    mappings)),
            mappings),
        "");

    return dsd;
  }

  public List<Parameter> getParameters() {
    List<Parameter> parameters = new ArrayList<Parameter>();
    parameters.add(ReportingConstants.END_DATE_PARAMETER);
    parameters.add(ReportingConstants.LOCATION_PARAMETER);
    return parameters;
  }
}
