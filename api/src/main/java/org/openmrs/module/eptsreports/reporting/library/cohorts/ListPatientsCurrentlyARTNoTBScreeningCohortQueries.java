package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

@Component
public class ListPatientsCurrentlyARTNoTBScreeningCohortQueries {

  private static final String TB5 = "TB5/ListPatientsCurrentlyARTNoTBScreeningSummary.sql";
  private static final String TB5_WITH_FC = "TB5/TB5_WITH_FC.sql";
  private static final String TB5_WITHOUT_FC = "TB5/TB5_WITHOUT_FC.sql";

  public CohortDefinition findPatientsWhoActiveOnARTAndNotHaveTBScreening() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String query = EptsQuerysUtils.loadQuery(TB5);

    definition.setQuery(query);

    return definition;
  }

  public CohortDefinition
      findPatientsWhoActiveOnARTAndNotHaveTBScreeningInLast6MonthsWithOneConsultation() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose Com Consulta Clinica");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String query = EptsQuerysUtils.loadQuery(TB5_WITH_FC);

    definition.setQuery(query);

    return definition;
  }

  public CohortDefinition
      getPatientsWhoActiveOnARTAndNotHaveTBScreeningInLast6MonthsWithoutOneConsultation() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("Lista de Pacientes Actualmente Em TARV Sem Rastreio de Tuberculose");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String query = EptsQuerysUtils.loadQuery(TB5_WITHOUT_FC);

    definition.setQuery(query);

    return definition;
  }
}