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
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.cohorts.GenericCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.DatimCodeDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.SismaCodeDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.data.quality.incompatiblemds.EC1IncompatibleMDSInFichaClinicaDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.data.quality.incompatiblemds.EC2IncompatibleMDSInFichaAPSSDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.data.quality.incompatiblemds.SummaryIncompatibleMDSDataSet;
import org.openmrs.module.eptsreports.reporting.reports.manager.EptsDataExportManager;
import org.openmrs.module.reporting.ReportingException;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetupDataQualitytIncompatibleDSDReport extends EptsDataExportManager {

  @Autowired private SummaryIncompatibleMDSDataSet summaryIncompatibleMDSDataSet;

  @Autowired
  private EC1IncompatibleMDSInFichaClinicaDataSet eC1IncompatibleMDSInFichaClinicaDataSet;

  @Autowired private EC2IncompatibleMDSInFichaAPSSDataSet eC2IncompatibleMDSInFichaAPSSDataSet;

  @Autowired private DatimCodeDataSet datimCodeDataset;

  @Autowired private SismaCodeDataSet sismaCodeDataset;

  @Autowired protected GenericCohortQueries genericCohortQueries;

  @Override
  public String getExcelDesignUuid() {
    return "7c7b3c0e-34c0-442f-9e4b-374e2763b024";
  }

  @Override
  public String getUuid() {
    return "4e5ef328-cb61-48be-9971-ec9dff9121ed";
  }

  @Override
  public String getName() {
    return "RQD4: Data Quality Report to Identify Incompatible DSD Models";
  }

  @Override
  public String getDescription() {
    return "Este relatório gera uma lista de utentes que respondem aos critérios de verificação/validação dos dados existentes no sistema, e permite que os utilizadores confirmem as informações de modo a corrigir os registos incompatíveis no sistema SESP.";
  }

  @Override
  public ReportDefinition constructReportDefinition() {
    ReportDefinition rd = new ReportDefinition();
    rd.setUuid(getUuid());
    rd.setName(getName());
    rd.setDescription(getDescription());
    rd.addParameters(this.getDataParameters());

    rd.addDataSetDefinition(
        "SD",
        Mapped.mapStraightThrough(
            summaryIncompatibleMDSDataSet.constructSummaryIncompatibleMDSDatset(
                getDataParameters())));

    rd.addDataSetDefinition(
        "D",
        Mapped.mapStraightThrough(this.datimCodeDataset.constructDataset(this.getParameters())));

    rd.addDataSetDefinition(
        "SC",
        Mapped.mapStraightThrough(this.sismaCodeDataset.constructDataset(this.getParameters())));

    rd.addDataSetDefinition(
        "EC1",
        Mapped.mapStraightThrough(
            eC1IncompatibleMDSInFichaClinicaDataSet.eC1IncompatibleMDSInFichaClinicaDataSet(
                getDataParameters())));

    rd.addDataSetDefinition(
        "EC2",
        Mapped.mapStraightThrough(
            eC2IncompatibleMDSInFichaAPSSDataSet.eC1IncompatibleMDSInFichaClinicaDataSet(
                getDataParameters())));

    return rd;
  }

  @Override
  public String getVersion() {
    return "1.0-SNAPSHOT";
  }

  @Override
  public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
    ReportDesign reportDesign = null;
    try {
      reportDesign =
          createXlsReportDesign(
              reportDefinition,
              "Data_Quality_Incompatible_DSD_Report.xls",
              "Relatório de Qualidade de Dados para Identificar MDS Incompatíveis",
              getExcelDesignUuid(),
              null);
      Properties props = new Properties();
      props.put("repeatingSections", "sheet:2,row:8,dataset:EC1 | sheet:3,row:8,dataset:EC2");
      props.put("sortWeight", "5000");
      props.put("sortWeight", "5000");
      reportDesign.setProperties(props);
    } catch (IOException e) {
      throw new ReportingException(e.toString());
    }

    return Arrays.asList(reportDesign);
  }

  private List<Parameter> getDataParameters() {
    return Arrays.asList(new Parameter("location", "Unidade Sanitária", Location.class));
  }
}
