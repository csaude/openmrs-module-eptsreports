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
import org.openmrs.module.eptsreports.reporting.library.datasets.data.quality.duplicate.EC1PatientListDuplicateDataset;
import org.openmrs.module.eptsreports.reporting.library.datasets.data.quality.duplicate.EC2PatientListDuplicateDataset;
import org.openmrs.module.eptsreports.reporting.library.datasets.data.quality.duplicate.SummaryDataQualityDuplicateDataset;
import org.openmrs.module.eptsreports.reporting.reports.manager.EptsDataExportManager;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.ReportingException;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetupDataQualityDuplicateReport extends EptsDataExportManager {

  @Autowired private SummaryDataQualityDuplicateDataset summaryDataQualityDuplicateDataset;

  @Autowired private EC1PatientListDuplicateDataset ec1PatientListDuplicateDataset;

  @Autowired private EC2PatientListDuplicateDataset ec2PatientListDuplicateDataset;

  @Autowired protected GenericCohortQueries genericCohortQueries;

  @Override
  public String getExcelDesignUuid() {
    return "3784716d-c6a5-49ca-b25c-14f804eb4f6e";
  }

  @Override
  public String getUuid() {
    return "20e14afe-16c4-4a38-92cb-b548726c5428";
  }

  @Override
  public String getName() {
    return "RQD3: Relatório de Qualidade de Dados para Identificar Potenciais Duplicações de NIDs-Pacientes";
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

    rd.addDataSetDefinition(
        "SD",
        Mapped.mapStraightThrough(
            summaryDataQualityDuplicateDataset.constructSummaryDataQualityDatset()));

    rd.addDataSetDefinition(
        "ECD1",
        Mapped.mapStraightThrough(ec1PatientListDuplicateDataset.ec1PatientDuplicateListDataset()));

    rd.addDataSetDefinition(
        "ECD2",
        Mapped.mapStraightThrough(ec2PatientListDuplicateDataset.ec2PatientDuplicateListDataset()));

    rd.setBaseCohortDefinition(
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "baseCohortQuery", summaryDataQualityDuplicateDataset.getBaseCohortQuery()),
            null));
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
              "Data_Quality_Duplicates_Report.xls",
              "Relatório de Qualidade de Dados para Identificar Potenciais Duplicações de NIDs-Pacientes",
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
}
