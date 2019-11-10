/** */
package org.openmrs.module.eptsreports.reporting.intergrated.library.cohorts;

import org.junit.Ignore;
import org.openmrs.module.eptsreports.reporting.library.cohorts.TxNewCohortQueries;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.springframework.beans.factory.annotation.Autowired;

/** @author Stélio Moiane */
public class TxNewCohortDefinitionTest {

  @Autowired private TxNewCohortQueries txNewCohortQueries;

  @Ignore
  public void shouldFindPatientsNewlyEnrolledInART() throws EvaluationException {
    //
    // final Location location =
    // Context.getLocationService().getLocation(6);
    // final Date startDate = DateUtil.getDateTime(2018, 6, 21);
    // final Date endDate = DateUtil.getDateTime(2018, 9, 20);
    //
    // final CohortDefinition txNewCompositionCohort =
    // this.txNewCohortQueries.getPatientsPregnantEnrolledOnART();
    //
    // final Map<Parameter, Object> parameters = new HashMap<>();
    // parameters.put(new Parameter("startDate", "Start Date", Date.class),
    // startDate);
    // parameters.put(new Parameter("endDate", "End Date", Date.class),
    // endDate);
    // parameters.put(new Parameter("location", "Location", Location.class),
    // location);
    //
    // final EvaluatedCohort evaluatedCohort =
    // this.evaluateCohortDefinition(txNewCompositionCohort, parameters);
    //
    // assertFalse(evaluatedCohort.getMemberIds().isEmpty());
    // }
    //
    // @Override
    // protected String username() {
    // return "admin";
    // }
    //
    // @Override
    // protected String password() {
    // return "eSaude123";

  }
}
