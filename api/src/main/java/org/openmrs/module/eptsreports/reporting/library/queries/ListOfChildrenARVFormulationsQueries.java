package org.openmrs.module.eptsreports.reporting.library.queries;

import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.TxCurrColumnsQuantity;
import org.openmrs.module.eptsreports.reporting.utils.TxCurrQuery;

public interface ListOfChildrenARVFormulationsQueries {
  final String LIST_OF_CHILDREN_ARV_FORMULATIONS =
      "FORMULATIONS/LIST_OF_CHILDREN_ARV_FORMULATIONS.sql";

  class QUERY {
    public static final String findChildrenARVFormulations =
        String.format(
            EptsQuerysUtils.loadQuery(LIST_OF_CHILDREN_ARV_FORMULATIONS),
            TxCurrQuery.findPatientsInTxCurr(TxCurrColumnsQuantity.ALL));
  }
}
