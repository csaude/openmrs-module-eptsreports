package org.openmrs.module.eptsreports.reporting.library.queries;

import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;

public interface ListPatientsDefaultersIITQueries {

  class QUERY {
    private static final String PATIENTS_DEFAULTERS_IIT_LIST =
        "IIT/PATIENTS_DEFAULTERS_IIT_LIST.sql";

    private static final String PATIENTS_DEFAULTERS_IIT_TOTAL =
        "IIT/PATIENTS_DEFAULTERS_IIT_TOTAL.sql";

    public static final String findPatientsDefaultersIITList =
        EptsQuerysUtils.loadQuery(PATIENTS_DEFAULTERS_IIT_LIST);

    public static final String findPatientsDefaultersIIT =
        EptsQuerysUtils.loadQuery(PATIENTS_DEFAULTERS_IIT_TOTAL);
  }
}
