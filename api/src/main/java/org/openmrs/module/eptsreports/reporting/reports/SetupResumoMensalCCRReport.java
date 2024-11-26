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

import static org.openmrs.module.reporting.evaluation.parameter.Mapped.mapStraightThrough;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.openmrs.module.eptsreports.reporting.library.cohorts.GenericCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.LocationDataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ResumoMensalDataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ResumoMensalEncounterDataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.queries.BaseQueries;
import org.openmrs.module.eptsreports.reporting.reports.manager.EptsPeriodIndicatorDataExportManager;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.ReportingException;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.PeriodIndicatorReportDefinition;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetupResumoMensalCCRReport extends EptsPeriodIndicatorDataExportManager {

  @Autowired private ResumoMensalDataSetDefinition resumoMensalDataSetDefinition;
  @Autowired private ResumoMensalEncounterDataSetDefinition resumoMensalEncounterDataSetDefinition;

  @Autowired protected GenericCohortQueries genericCohortQueries;

  @Autowired
  public SetupResumoMensalCCRReport(ResumoMensalDataSetDefinition resumoMensalDataSetDefinition) {
    this.resumoMensalDataSetDefinition = resumoMensalDataSetDefinition;
  }

  @Override
  public String getExcelDesignUuid() {
    return "6204658e-abe0-11ef-8ffc-5f4156fe9ca4";
  }

  @Override
  public String getUuid() {
    return "5a5f5c4e-abe0-11ef-ab57-f3aa6c7a3d3d";
  }

  @Override
  public String getName() {
    return "Resumo Mensal de CCR";
  }

  @Override
  public String getDescription() {
    return "Este relatório apresenta os dados do Resumo Mensal de CCR provenientes da ferramenta Ficha CCR e outras existentes no sistema.";
  }

  @Override
  public PeriodIndicatorReportDefinition constructReportDefinition() {

    PeriodIndicatorReportDefinition rd =
        SetupResumoMensalCCRReport.getDefaultPeriodIndicatorReportDefinition();

    rd.setUuid(getUuid());
    rd.setName(getName());
    rd.setDescription(getDescription());
    rd.addParameters(resumoMensalDataSetDefinition.getParameters());

    rd.addDataSetDefinition("HF", mapStraightThrough(new LocationDataSetDefinition()));
    rd.addDataSetDefinition(
        "R", mapStraightThrough(resumoMensalDataSetDefinition.constructResumoMensalDataset()));

    rd.addDataSetDefinition(
        "RE",
        mapStraightThrough(resumoMensalEncounterDataSetDefinition.constructResumoMensalDataset()));

    rd.setBaseCohortDefinition(
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "baseCohortQuery", BaseQueries.getBaseCohortQuery()),
            "endDate=${endDate},location=${location}"));
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
              "Relatorio_Mensal.xls",
              "Relatorio Mensal Report",
              getExcelDesignUuid(),
              null);
      Properties props = new Properties();
      props.put("sortWeight", "5000");
      reportDesign.setProperties(props);
    } catch (IOException e) {
      throw new ReportingException(e.toString());
    }

    return Arrays.asList(reportDesign);
  }

  public static PeriodIndicatorReportDefinition getDefaultPeriodIndicatorReportDefinition() {
    PeriodIndicatorReportDefinition rd = new PeriodIndicatorReportDefinition();
    rd.removeParameter(ReportingConstants.START_DATE_PARAMETER);
    rd.removeParameter(ReportingConstants.END_DATE_PARAMETER);
    rd.removeParameter(ReportingConstants.LOCATION_PARAMETER);

    return rd;
  }
}
