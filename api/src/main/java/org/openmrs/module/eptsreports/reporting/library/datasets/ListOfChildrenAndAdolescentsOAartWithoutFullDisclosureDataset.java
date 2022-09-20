package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.ListOfChildrenAndAdolescentsOAartWithoutFullDisclosureCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.ResumoMensalCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.dimensions.AgeDimensionCohortInterface;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ListOfChildrenAndAdolescentsOAartWithoutFullDisclosureDataset extends BaseDataSet {

  private static final String DISCLOSURE_LIST =
      "LIST_OF_CHILDREN_AND_ADOLESCENTS_ON_ART_WITHOUT_FULL_DISCLOSURE/LIST_OF_CHILDREN_AND_ADOLESCENTS_ON_ART_WITHOUT_FULL_DISCLOSURE.sql";

  @Autowired EptsGeneralIndicator eptsGeneralIndicator;
  @Autowired private EptsCommonDimension eptsCommonDimension;
  @Autowired private ResumoMensalCohortQueries resumoMensalCohortQueries;

  @Autowired
  private ListOfChildrenAndAdolescentsOAartWithoutFullDisclosureCohortQueries
      ListOfChildrenAndAdolescentsOAartWithoutFullDisclosureCohortQueries;

  @Autowired
  @Qualifier("commonAgeDimensionCohort")
  private AgeDimensionCohortInterface ageDimensionCohort;

  public DataSetDefinition constructDataset(List<Parameter> parameterList) {
    SqlDataSetDefinition definition = new SqlDataSetDefinition();
    definition.setName("Lista de Crianças e Adolescentes em TARV sem Divulgação Completa");
    definition.addParameters(parameterList);
    String query = EptsQuerysUtils.loadQuery(DISCLOSURE_LIST);
    definition.setSqlQuery(query);
    return definition;
  }

  public DataSetDefinition getTotaSuummary() {

    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();

    dataSetDefinition.setParameters(this.getParameters());
    dataSetDefinition.addDimension(
        "age",
        EptsReportUtils.map(
            eptsCommonDimension.age(ageDimensionCohort), "effectiveDate=${endDate}"));

    dataSetDefinition.addDimension("gender", EptsReportUtils.map(eptsCommonDimension.gender(), ""));

    final String mappings = "endDate=${endDate},location=${location}";

    final CohortDefinition RM =
        this.resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13();

    dataSetDefinition.addColumn(
        "TXCURR_8TO14",
        "Lista de Criancas de 8 a 14 Anos Actualmente Em TARV ",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "Lista de Criancas de 8 a 14 Anos Actualmente Em TARV",
                EptsReportUtils.map(RM, mappings)),
            mappings),
        "age=8-14");

    dataSetDefinition.addColumn(
        "TXCURR_8TO14_N",
        "Lista de Criancas de 8 a 14 Anos Actualmente Em TARV ",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "Lista de Criancas de 8 a 14 Anos Actualmente Em TARV",
                EptsReportUtils.map(
                    ListOfChildrenAndAdolescentsOAartWithoutFullDisclosureCohortQueries
                        .findPatientsOAartWithoutFullDisclosure_N(),
                    mappings)),
            mappings),
        "");

    dataSetDefinition.addColumn(
        "TXCURR_8TO14_T",
        "Lista de Criancas de 8 a 14 Anos Actualmente Em TARV ",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "Lista de Criancas de 8 a 14 Anos Actualmente Em TARV",
                EptsReportUtils.map(
                    ListOfChildrenAndAdolescentsOAartWithoutFullDisclosureCohortQueries
                        .findPatientsOAartWithoutFullDisclosure_T(),
                    mappings)),
            mappings),
        "");

    dataSetDefinition.addColumn(
        "TXCURR_8TO14_P",
        "Lista de Criancas de 8 a 14 Anos Actualmente Em TARV ",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "Lista de Criancas de 8 a 14 Anos Actualmente Em TARV",
                EptsReportUtils.map(
                    ListOfChildrenAndAdolescentsOAartWithoutFullDisclosureCohortQueries
                        .findPatientsOAartWithoutFullDisclosure_P(),
                    mappings)),
            mappings),
        "");

    dataSetDefinition.addColumn(
        "TXCURR_8TO14_B",
        "Lista de Criancas de 8 a 14 Anos Actualmente Em TARV ",
        EptsReportUtils.map(
            this.eptsGeneralIndicator.getIndicator(
                "Lista de Criancas de 8 a 14 Anos Actualmente Em TARV",
                EptsReportUtils.map(
                    ListOfChildrenAndAdolescentsOAartWithoutFullDisclosureCohortQueries
                        .findPatientsOAartWithoutFullDisclosure_B(),
                    mappings)),
            mappings),
        "");
    return dataSetDefinition;
  }

  public List<Parameter> getParameters() {
    List<Parameter> parameters = new ArrayList<Parameter>();
    parameters.add(new Parameter("endDate", "End Date", Date.class));
    parameters.add(ReportingConstants.LOCATION_PARAMETER);
    return parameters;
  }
}
