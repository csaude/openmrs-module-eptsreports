package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.List;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListMDSEvaluationReportDataSet extends BaseDataSet {

  private static final String LIST_MDS_EVALUATION_REPORT = "AMDS/LIST_MDS_EVALUATION_REPORT.sql";

  @Autowired EptsGeneralIndicator eptsGeneralIndicator;

  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  public DataSetDefinition constructDatasetCoorte(List<Parameter> parameterList) {
    SqlDataSetDefinition definition = new SqlDataSetDefinition();
    definition.setName("Relatório de Avaliação de MDS");
    definition.addParameters(parameterList);
    String query = EptsQuerysUtils.loadQuery(LIST_MDS_EVALUATION_REPORT);
    definition.setSqlQuery(query);
    return definition;
  }
}
