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
package org.openmrs.module.eptsreports.reporting.reports;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.openmrs.module.eptsreports.reporting.library.cohorts.GenericCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.DatimCodeDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.PMTCTEIDDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.PMTCTHEIDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.PrepCtDataset;
import org.openmrs.module.eptsreports.reporting.library.datasets.PrepNewDataset;
import org.openmrs.module.eptsreports.reporting.library.datasets.TRFINDataset;
import org.openmrs.module.eptsreports.reporting.library.datasets.TxCurrDataset;
import org.openmrs.module.eptsreports.reporting.library.datasets.TxMlDataset;
import org.openmrs.module.eptsreports.reporting.library.datasets.TxNewDataset;
import org.openmrs.module.eptsreports.reporting.library.datasets.TxPvlsDataset;
import org.openmrs.module.eptsreports.reporting.library.datasets.TxPvlsSuplementalDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.TxRttDataset;
import org.openmrs.module.eptsreports.reporting.library.queries.BaseQueries;
import org.openmrs.module.eptsreports.reporting.reports.manager.EptsPeriodIndicatorDataExportManager;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.ReportingException;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.PeriodIndicatorReportDefinition;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetupMERQuarterly extends EptsPeriodIndicatorDataExportManager {

  @Autowired private TxPvlsDataset txPvlsDataset;

  @Autowired private TxNewDataset txNewDataset;

  @Autowired private TxCurrDataset txCurrDataset;

  @Autowired private TxRttDataset txRttDataset;

  @Autowired private TxMlDataset txMlDataset;

  @Autowired private TRFINDataset txTfrInDataset;

  @Autowired private PrepNewDataset prepNewDataset;

  @Autowired private PrepCtDataset prepCtDataset;

  @Autowired private PMTCTEIDDataSet pmtcteidDataSet;

  @Autowired private PMTCTHEIDataSet pmtctheiDataSet;

  @Autowired private TxPvlsSuplementalDataSet txPvlsSuplementalDataSet;

  @Autowired protected GenericCohortQueries genericCohortQueries;
  @Autowired private DatimCodeDataSet datimCodeDataSet;

  @Override
  public String getVersion() {
    return "1.0-SNAPSHOT";
  }

  @Override
  public String getUuid() {
    return "fa20c1ac-94ea-11e3-96de-0023156365e4";
  }

  @Override
  public String getExcelDesignUuid() {
    return "cea86583-9ca5-4ad9-94e4-e20081a57619";
  }

  @Override
  public String getName() {
    return "PEPFAR MER 2.8 Quarterly";
  }

  @Override
  public String getDescription() {
    return "PEPFAR MER 2.8 Quarterly";
  }

  @Override
  public PeriodIndicatorReportDefinition constructReportDefinition() {

    PeriodIndicatorReportDefinition reportDefinition =
        SetupResumoMensalReport.getDefaultPeriodIndicatorReportDefinition();

    reportDefinition.setUuid(this.getUuid());
    reportDefinition.setName(this.getName());
    reportDefinition.setDescription(this.getDescription());
    reportDefinition.setParameters(this.txRttDataset.getParameters());

    reportDefinition.addDataSetDefinition(
        "N", Mapped.mapStraightThrough(this.txNewDataset.constructTxNewDataset()));

    reportDefinition.addDataSetDefinition(
        "TR", Mapped.mapStraightThrough(this.txTfrInDataset.constructTxTRFIN()));

    reportDefinition.addDataSetDefinition(
        "C", Mapped.mapStraightThrough(this.txCurrDataset.constructTxCurrDataset(true)));

    reportDefinition.addDataSetDefinition(
        "P", Mapped.mapStraightThrough(this.txPvlsDataset.constructTxPvlsDatset()));

    reportDefinition.addDataSetDefinition(
        "ML", Mapped.mapStraightThrough(this.txMlDataset.constructtxMlDataset()));

    reportDefinition.addDataSetDefinition(
        "R", Mapped.mapStraightThrough(this.txRttDataset.constructTxRttDataset()));

    reportDefinition.addDataSetDefinition(
        "PREP", Mapped.mapStraightThrough(this.prepNewDataset.constructPrepNewDataset()));

    reportDefinition.addDataSetDefinition(
        "PrEP_CT", Mapped.mapStraightThrough(this.prepCtDataset.constructPrepCtDataset()));

    reportDefinition.addDataSetDefinition(
        "PMTCT_EID", Mapped.mapStraightThrough(this.pmtcteidDataSet.constructPMTCTEIDDataset()));

    reportDefinition.addDataSetDefinition(
        "PMTCT_HEI", Mapped.mapStraightThrough(this.pmtctheiDataSet.constructPMTCTHEIDataset()));

    reportDefinition.addDataSetDefinition(
        "T-S",
        Mapped.mapStraightThrough(
            this.txPvlsSuplementalDataSet.constructTxPvlsSupplementalDataset()));

    reportDefinition.addDataSetDefinition(
        "D",
        Mapped.mapStraightThrough(this.datimCodeDataSet.constructDataset(this.getParameters())));

    reportDefinition.setBaseCohortDefinition(
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "baseCohortQuery", BaseQueries.getBaseCohortQuery()),
            "endDate=${endDate},location=${location}"));

    return reportDefinition;
  }

  @Override
  public List<ReportDesign> constructReportDesigns(final ReportDefinition reportDefinition) {
    ReportDesign reportDesign = null;
    try {
      reportDesign =
          this.createXlsReportDesign(
              reportDefinition,
              "PEPFAR_MER_2.8_Quarterly.xls",
              "PEPFAR MER 2.8 Quarterly",
              this.getExcelDesignUuid(),
              null);
      final Properties props = new Properties();
      props.put("sortWeight", "5000");
      reportDesign.setProperties(props);
    } catch (final IOException e) {
      throw new ReportingException(e.toString());
    }

    return Arrays.asList(reportDesign);
  }
}
