package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.utils.DiscloreType;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

@Component
public class ListOfChildrenAndAdolescentsOAartWithoutFullDisclosureCohortQueries {

  private static final String DISCLOSURE_LIST_MARKED =
      "LIST_OF_CHILDREN_AND_ADOLESCENTS_ON_ART_WITHOUT_FULL_DISCLOSURE/LIST_OF_CHILDREN_AND_ADOLESCENTS_ON_ART_WITHOUT_FULL_DISCLOSURE_TOTAL.sql";

  public static String findPatientsOAartWithoutFullDisclosure(DiscloreType discloreType) {

    String query = EptsQuerysUtils.loadQuery(DISCLOSURE_LIST_MARKED);

    switch (discloreType) {
      case T:
        query = query + " where statusRevelacao=1 ";
        break;

      case P:
        query = query + " where statusRevelacao=2 ";
        break;

      case N:
        query = query + " where statusRevelacao=3 ";
        break;

      case EMPTY:
        query = query + " where statusRevelacao=4 ";
        break;
    }

    return query;
  }

  public CohortDefinition findPatientsOAartWithoutFullDisclosure_T() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("Lista de Crianças e Adolescentes em TARV sem Divulgação Completa T");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String query = findPatientsOAartWithoutFullDisclosure(DiscloreType.T);

    definition.setQuery(query);

    return definition;
  }

  public CohortDefinition findPatientsOAartWithoutFullDisclosure_P() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("Lista de Crianças e Adolescentes em TARV sem Divulgação Completa P");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String query = findPatientsOAartWithoutFullDisclosure(DiscloreType.P);

    definition.setQuery(query);

    return definition;
  }

  public CohortDefinition findPatientsOAartWithoutFullDisclosure_B() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("Lista de Crianças e Adolescentes em TARV sem Divulgação Completa B");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String query = findPatientsOAartWithoutFullDisclosure(DiscloreType.EMPTY);

    definition.setQuery(query);

    return definition;
  }

  public CohortDefinition findPatientsOAartWithoutFullDisclosure_N() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("Lista de Crianças e Adolescentes em TARV sem Divulgação Completa  N");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    String query = findPatientsOAartWithoutFullDisclosure(DiscloreType.N);

    definition.setQuery(query);

    return definition;
  }
}
