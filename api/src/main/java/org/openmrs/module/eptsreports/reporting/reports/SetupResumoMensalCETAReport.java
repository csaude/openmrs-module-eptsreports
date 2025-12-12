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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.openmrs.module.eptsreports.reporting.library.cohorts.GenericCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.LocationDataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator0DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator10DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator11DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator12DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator13DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator14DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator15DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator16DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator17DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator18DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator19DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator1DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator20DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator21DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator22DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator23DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator2DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator3DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator4DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator5DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator6DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator7DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator8DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta.Indicator9DataSetDefinition;
import org.openmrs.module.eptsreports.reporting.reports.manager.EptsDataExportManager;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.ReportingException;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.PeriodIndicatorReportDefinition;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetupResumoMensalCETAReport extends EptsDataExportManager {

  @Autowired protected GenericCohortQueries genericCohortQueries;

  @Autowired private Indicator0DataSetDefinition indicator0DataSetDefinition;
  @Autowired private Indicator1DataSetDefinition indicator1DataSetDefinition;
  @Autowired private Indicator2DataSetDefinition indicator2DataSetDefinition;
  @Autowired private Indicator3DataSetDefinition indicator3DataSetDefinition;
  @Autowired private Indicator4DataSetDefinition indicator4DataSetDefinition;
  @Autowired private Indicator5DataSetDefinition indicator5DataSetDefinition;
  @Autowired private Indicator6DataSetDefinition indicator6DataSetDefinition;
  @Autowired private Indicator7DataSetDefinition indicator7DataSetDefinition;
  @Autowired private Indicator8DataSetDefinition indicator8DataSetDefinition;
  @Autowired private Indicator9DataSetDefinition indicator9DataSetDefinition;
  @Autowired private Indicator10DataSetDefinition indicator10DataSetDefinition;
  @Autowired private Indicator11DataSetDefinition indicator11DataSetDefinition;
  @Autowired private Indicator12DataSetDefinition indicator12DataSetDefinition;
  @Autowired private Indicator13DataSetDefinition indicator13DataSetDefinition;
  @Autowired private Indicator14DataSetDefinition indicator14DataSetDefinition;
  @Autowired private Indicator15DataSetDefinition indicator15DataSetDefinition;
  @Autowired private Indicator16DataSetDefinition indicator16DataSetDefinition;
  @Autowired private Indicator17DataSetDefinition indicator17DataSetDefinition;
  @Autowired private Indicator18DataSetDefinition indicator18DataSetDefinition;
  @Autowired private Indicator19DataSetDefinition indicator19DataSetDefinition;
  @Autowired private Indicator20DataSetDefinition indicator20DataSetDefinition;
  @Autowired private Indicator21DataSetDefinition indicator21DataSetDefinition;
  @Autowired private Indicator22DataSetDefinition indicator22DataSetDefinition;
  @Autowired private Indicator23DataSetDefinition indicator23DataSetDefinition;

  @Override
  public String getVersion() {
    return "1.0-SNAPSHOT";
  }

  @Override
  public String getUuid() {
    return "bc926674-6e16-11f0-a659-ef7e01776a60";
  }

  @Override
  public String getExcelDesignUuid() {
    return "c35f65c4-6e16-11f0-a31c-d355a615784d";
  }

  @Override
  public String getName() {
    return "Resumo Mensal CETA";
  }

  @Override
  public String getDescription() {
    return "Este relatório apresenta os dados do resumo mensal da Unidade Sanitária para CETA, provenientes das 3 fichas do pacote CETA e Ficha Mestra no sistema.";
  }

  @Override
  public PeriodIndicatorReportDefinition constructReportDefinition() {

    PeriodIndicatorReportDefinition rd =
        SetupResumoMensalCETAReport.getDefaultPeriodIndicatorReportDefinition();

    rd.setUuid(getUuid());
    rd.setName(getName());
    rd.setDescription(getDescription());
    rd.addParameters(this.getParameters());

    rd.addDataSetDefinition("HF", mapStraightThrough(new LocationDataSetDefinition()));
    rd.addDataSetDefinition(
        "I0", mapStraightThrough(this.indicator0DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I1", mapStraightThrough(this.indicator1DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I2", mapStraightThrough(this.indicator2DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I3", mapStraightThrough(this.indicator3DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I4", mapStraightThrough(this.indicator4DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I5", mapStraightThrough(this.indicator5DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I6", mapStraightThrough(this.indicator6DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I7", mapStraightThrough(this.indicator7DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I8", mapStraightThrough(this.indicator8DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I9", mapStraightThrough(this.indicator9DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I10", mapStraightThrough(this.indicator10DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I11", mapStraightThrough(this.indicator11DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I12", mapStraightThrough(this.indicator12DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I13", mapStraightThrough(this.indicator13DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I14", mapStraightThrough(this.indicator14DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I15", mapStraightThrough(this.indicator15DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I16", mapStraightThrough(this.indicator16DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I17", mapStraightThrough(this.indicator17DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I18", mapStraightThrough(this.indicator18DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I19", mapStraightThrough(this.indicator19DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I20", mapStraightThrough(this.indicator20DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I21", mapStraightThrough(this.indicator21DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I22", mapStraightThrough(this.indicator22DataSetDefinition.constructDataset()));
    rd.addDataSetDefinition(
        "I23", mapStraightThrough(this.indicator23DataSetDefinition.constructDataset()));

    return rd;
  }

  @Override
  public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
    ReportDesign reportDesign = null;
    try {
      reportDesign =
          createXlsReportDesign(
              reportDefinition,
              "Resumo_Mensal_CETA.xls",
              "Resumo Mensal CETA",
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

  public List<Parameter> getParameters() {
    List<Parameter> parameters = new ArrayList<Parameter>();
    parameters.add(ReportingConstants.START_DATE_PARAMETER);
    parameters.add(ReportingConstants.END_DATE_PARAMETER);
    parameters.add(ReportingConstants.LOCATION_PARAMETER);
    return parameters;
  }
}
