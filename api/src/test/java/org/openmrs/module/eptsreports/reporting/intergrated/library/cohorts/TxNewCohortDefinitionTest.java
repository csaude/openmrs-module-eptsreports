package org.openmrs.module.eptsreports.reporting.intergrated.library.cohorts;

import static org.junit.Assert.assertFalse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.eptsreports.reporting.intergrated.utils.DefinitionsFGHLiveTest;
import org.openmrs.module.eptsreports.reporting.library.cohorts.DSDCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.TxRTTCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.dimensions.KeyPopulationDimension;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.dimension.CohortDefinitionDimension;
import org.openmrs.module.reporting.indicator.dimension.CohortDimensionResult;
import org.springframework.beans.factory.annotation.Autowired;

/** @author Stélio Moiane */
public class TxNewCohortDefinitionTest extends DefinitionsFGHLiveTest {

  @Autowired private DSDCohortQueries txNewCohortQueries;

  @Autowired private TxRTTCohortQueries txRTTCohortQueries;

  @Autowired private KeyPopulationDimension keyPopulationDimension;

  @Test
  public void shouldFindPatientsNewlyEnrolledInART() throws EvaluationException {

    final Location location = Context.getLocationService().getLocation(221);
    final Date startDate = DateUtil.getDateTime(2019, 7, 21);
    final Date endDate = DateUtil.getDateTime(2019, 12, 20);
    // final Date reportingEndDate = DateUtil.getDateTime(2018, 9, 20);

    final CohortDefinitionDimension txNewCompositionCohort =
        this.keyPopulationDimension.findPatientsWhoAreHomosexual();

    final Map<Parameter, Object> parameters = new HashMap<>();
    parameters.put(new Parameter("startDate", "Start Date", Date.class), startDate);
    parameters.put(new Parameter("endDate", "End Date", Date.class), endDate);
    parameters.put(new Parameter("location", "Location", Location.class), location);

    // parameters.put(new Parameter("reportingEndDate", "End Date",
    // Date.class), reportingEndDate);

    final CohortDimensionResult evaluateCohortDefinitionDimension =
        this.evaluateCohortDefinitionDimension(txNewCompositionCohort, parameters);

    assertFalse(evaluateCohortDefinitionDimension.getOptionCohorts().entrySet().isEmpty());
  }

  @Override
  protected String username() {
    return "admin";
  }

  @Override
  protected String password() {
    return "H!$fGH0Mr$";
  }
}
