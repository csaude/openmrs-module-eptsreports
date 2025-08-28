package org.openmrs.module.eptsreports.reporting.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.openmrs.module.eptsreports.reporting.library.cohorts.GenericCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.datasets.DatimCodeDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.SismaCodeDataSet;
import org.openmrs.module.eptsreports.reporting.library.datasets.TB7AdvancedDiseaseAndTBDataSet;
import org.openmrs.module.eptsreports.reporting.library.queries.BaseQueries;
import org.openmrs.module.eptsreports.reporting.reports.manager.EptsPeriodIndicatorDataExportManager;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.ReportingException;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.PeriodIndicatorReportDefinition;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetupTB7AdvancedDiseaseAndTBCascadeReport
    extends EptsPeriodIndicatorDataExportManager {

  @Autowired private GenericCohortQueries genericCohortQueries;

  @Autowired private DatimCodeDataSet datimCodeDataSet;

  @Autowired private SismaCodeDataSet sismaCodeDataSet;

  @Autowired private TB7AdvancedDiseaseAndTBDataSet tb7AdvancedDiseaseAndTBDataSet;

  @Override
  public String getExcelDesignUuid() {
    return "6048b366-36c6-11ee-9f91-b7c3d375b234";
  }

  @Override
  public String getUuid() {
    return "689ec2f8-36c6-11ee-9bda-a37b135a5c96";
  }

  @Override
  public String getVersion() {
    return "1.0-SNAPSHOT";
  }

  @Override
  public String getName() {
    return "TB7: Relatorio Cascatas de Doenca Avancada por HIV e TB";
  }

  @Override
  public String getDescription() {
    return "O Relatório Cascatas de Doença Avançada e TB gera o número de utentes de acordo com duas cascatas clínicas pré-definidas de Doença Avançada por HIV e TB. O período de inclusão é calculado com base na data de fim do relatório. A data de geração do relatório e também usado como parâmetro";
  }

  @Override
  public PeriodIndicatorReportDefinition constructReportDefinition() {
    PeriodIndicatorReportDefinition reportDefinition = new PeriodIndicatorReportDefinition();
    reportDefinition.setUuid(getUuid());
    reportDefinition.setName(getName());
    reportDefinition.setDescription(getDescription());
    reportDefinition.setParameters(this.getParameters());

    reportDefinition.addDataSetDefinition(
        "TB7", Mapped.mapStraightThrough(tb7AdvancedDiseaseAndTBDataSet.constructDataset()));

    reportDefinition.addDataSetDefinition(
        "D",
        Mapped.mapStraightThrough(this.datimCodeDataSet.constructDataset(this.getParameters())));

    reportDefinition.addDataSetDefinition(
        "SC",
        Mapped.mapStraightThrough(this.sismaCodeDataSet.constructDataset(this.getParameters())));

    reportDefinition.setBaseCohortDefinition(
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "baseCohortQuery", BaseQueries.getBaseCohortQuery()),
            "endDate=${endDate},location=${location}"));

    return reportDefinition;
  }

  @Override
  public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
    ReportDesign reportDesign = null;
    try {
      reportDesign =
          createXlsReportDesign(
              reportDefinition,
              "TB7_Relatorio_Cascata_DAH_TB.xls",
              this.getName(),
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

  public List<Parameter> getParameters() {
    List<Parameter> parameters = new ArrayList<Parameter>();
    parameters.add(ReportingConstants.END_DATE_PARAMETER);
    parameters.add(ReportingConstants.LOCATION_PARAMETER);
    return parameters;
  }
}
