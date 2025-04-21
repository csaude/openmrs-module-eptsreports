package org.openmrs.module.eptsreports.reporting.library.queries;

import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;

public interface ListOfChildrenARVFormulationsQueries {
  final String LIST_OF_CHILDREN_ARV_FORMULATIONS =
      "FORMULATIONS/LIST_OF_CHILDREN_ARV_FORMULATIONS.sql";

  class QUERY {
    public static final String findChildrenARVFormulations =
        EptsQuerysUtils.loadQuery(LIST_OF_CHILDREN_ARV_FORMULATIONS);
  }
}
