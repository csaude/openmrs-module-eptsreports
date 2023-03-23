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
import org.openmrs.module.eptsreports.reporting.library.datasets.DatimCodeDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.SismaCodeDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.data.quality.duplicate.ficharesumo.EC1PatientListDuplicateFichaResumoDataset;
import org.openmrs.module.eptsreports.reporting.library.datasets.data.quality.duplicate.ficharesumo.EC2PatientListDuplicateFichaResumoDataset;
import org.openmrs.module.eptsreports.reporting.library.datasets.data.quality.duplicate.ficharesumo.SummaryDataQualityDuplicateFichaResumoDataset;
import org.openmrs.module.eptsreports.reporting.reports.manager.EptsDataExportManager;
import org.openmrs.module.reporting.ReportingException;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetupDataQualityDuplicateFichaResumoReport extends EptsDataExportManager {

  @Autowired
  private SummaryDataQualityDuplicateFichaResumoDataset
      summaryDataQualityDuplicateFichaResumoDataset;

  @Autowired private DatimCodeDataSet datimCodeDataset;

  @Autowired private SismaCodeDataSet sismaCodeDataset;

  @Autowired
  private EC1PatientListDuplicateFichaResumoDataset eC1PatientListDuplicateFichaResumoDataset;

  @Autowired
  private EC2PatientListDuplicateFichaResumoDataset eC2PatientListDuplicateFichaResumoDataset;

  @Override
  public String getExcelDesignUuid() {
    return "d34d80ae-fbad-11eb-9a03-0242ac130003";
  }

  @Override
  public String getUuid() {
    return "dce1c0b2-fbad-11eb-9a03-0242ac130003";
  }

  @Override
  public String getName() {
    return "RQD2: Relatório De Qualidade De Dados Para Identificar Fichas Resumo Duplicadas";
  }

  @Override
  public String getDescription() {
    return "Este relatório gera uma listagem de pacientes que atendem a determinadas verificações/validações dos dados existentes no sistema, e permite que os utilizadores confirmem as informações de modo a corrigir os registos duplicados no sistema SESP.";
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
            summaryDataQualityDuplicateFichaResumoDataset.constructSummaryDataQualityDatset(
                this.getDataParameters())));

    rd.addDataSetDefinition(
        "D",
        Mapped.mapStraightThrough(this.datimCodeDataset.constructDataset(this.getParameters())));

    rd.addDataSetDefinition(
        "SC",
        Mapped.mapStraightThrough(this.sismaCodeDataset.constructDataset(this.getParameters())));

    rd.addDataSetDefinition(
        "ECD1",
        Mapped.mapStraightThrough(
            eC1PatientListDuplicateFichaResumoDataset
                .ec1PatientWithDuplicatedFichaResumoListDataset(this.getDataParameters())));

    rd.addDataSetDefinition(
        "ECD2",
        Mapped.mapStraightThrough(
            eC2PatientListDuplicateFichaResumoDataset
                .ec2PatientWithDuplicatedFichaResumoListDataset(this.getDataParameters())));

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
              "Data_Quality_Duplicates_Ficha_Resumo_Report.xls",
              "Relatório De Qualidade De Dados Para Identificar Fichas Resumo Duplicadas",
              getExcelDesignUuid(),
              null);
      Properties props = new Properties();
      props.put("repeatingSections", "sheet:2,row:8,dataset:ECD1 | sheet:3,row:8,dataset:ECD2");
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
