/*
 * The contents of this file are subject to the OpenMRS Public License Version
 * 1.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TRFINCohortQueries {

  @Autowired private TxCurrCohortQueries txCurrCohortQueries;

  @Autowired private GenericCohortQueries genericCohorts;

  private static final String FIND_PATIENTS_WHO_ARE_TRANSFERRED_IN =
      "TX_TRANSFERRED_IN/FIND_PATIENTS_WHO_ARE_TRANSFERRED_IN.sql";

  @DocumentedDefinition(value = "patientsWhoAreTransferedIn")
  public CohortDefinition getPatiensWhoAreTransferredIn() {

    final CompositionCohortDefinition compositionDefinition = new CompositionCohortDefinition();

    compositionDefinition.setName("TRF-IN-NUMERATOR");
    compositionDefinition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    compositionDefinition.addParameter(new Parameter("endDate", "End Date", Date.class));
    compositionDefinition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    compositionDefinition.addSearch(
        "TRF-IN",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Finding patients who are transferredIn",
                EptsQuerysUtils.loadQuery(FIND_PATIENTS_WHO_ARE_TRANSFERRED_IN)),
            mappings));

    compositionDefinition.addSearch(
        "TX-CURR-PREVIOUS-PERIOD",
        EptsReportUtils.map(
            this.txCurrCohortQueries.findPatientsWhoAreActiveOnART(),
            "endDate=${startDate-1d},location=${location}"));

    compositionDefinition.setCompositionString("(TRF-IN NOT TX-CURR-PREVIOUS-PERIOD");

    return compositionDefinition;
  }
}
