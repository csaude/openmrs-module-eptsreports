package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.List;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.openmrs.module.eptsreports.reporting.library.dimensions.AgeDimensionCohortInterface;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ListMDSEvaluationReportDataSet extends BaseDataSet {

  private static final String LIST_MDS_EVALUATION_REPORT_COORTE_12_MESES =
      "AMDS/LIST_MDS_EVALUATION_REPORT_COORTE_12_MESES.sql";
  private static final String LIST_MDS_EVALUATION_REPORT_COORTE_24_MESES =
      "AMDS/LIST_MDS_EVALUATION_REPORT_COORTE_24_MESES.sql";
  private static final String LIST_MDS_EVALUATION_REPORT_COORTE_36_MESES =
      "AMDS/LIST_MDS_EVALUATION_REPORT_COORTE_36_MESES.sql";

  @Autowired EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired
  @Qualifier("commonAgeDimensionCohort")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private AgeDimensionCohortInterface ageDimensionCohort;

  public DataSetDefinition constructDataset12Coorte(List<Parameter> parameterList) {
    SqlDataSetDefinition definition = new SqlDataSetDefinition();
    definition.setName("Relatório de Avaliação de MDS (Coorte 12 Meses)");
    definition.addParameters(parameterList);
    String query = EptsQuerysUtils.loadQuery(LIST_MDS_EVALUATION_REPORT_COORTE_12_MESES);
    definition.setSqlQuery(query);
    return definition;
  }

  public DataSetDefinition constructDataset24Coorte(List<Parameter> parameterList) {
    SqlDataSetDefinition definition = new SqlDataSetDefinition();
    definition.setName("Relatório de Avaliação de MDS (Coorte 24 Meses)");
    definition.addParameters(parameterList);
    String query = EptsQuerysUtils.loadQuery(LIST_MDS_EVALUATION_REPORT_COORTE_24_MESES);
    definition.setSqlQuery(query);
    return definition;
  }

  public DataSetDefinition constructDataset36Coorte(List<Parameter> parameterList) {
    SqlDataSetDefinition definition = new SqlDataSetDefinition();
    definition.setName("Relatório de Avaliação de MDS (Coorte 36 Meses)");
    definition.addParameters(parameterList);
    String query = EptsQuerysUtils.loadQuery(LIST_MDS_EVALUATION_REPORT_COORTE_36_MESES);
    definition.setSqlQuery(query);
    return definition;
  }
}
