package org.openmrs.module.eptsreports.reporting.library.dimensions;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.cohorts.TxMlCohortQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.dimension.CohortDefinitionDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TxMLDimensions {

  @Autowired private TxMlCohortQueries txMlCohortQueries;

  public CohortDefinitionDimension findPatientsWhoAreAsDead() {
    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("Dead Dimension");
    dimension.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dimension.addCohortDefinition(
        "dead", EptsReportUtils.map(this.txMlCohortQueries.getPatientsMarkedAsDead(), mappings));

    return dimension;
  }

  public CohortDefinitionDimension findPatientsWhoAreTransferedOut() {
    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("Transfered Out Dimension");
    dimension.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dimension.addCohortDefinition(
        "transferedout",
        EptsReportUtils.map(this.txMlCohortQueries.getPatientsWhoAreTransferedOut(), mappings));

    return dimension;
  }

  public CohortDefinitionDimension findPatientsWhoRefusedOrStoppedTreatment() {
    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("Refused/Stopped Treatment Dimension");
    dimension.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dimension.addCohortDefinition(
        "refusedorstoppedtreatment",
        EptsReportUtils.map(
            this.txMlCohortQueries.getPatientsWhoRefusedOrStoppedTreatment(), mappings));

    return dimension;
  }

  public CohortDefinitionDimension findPatientsIITLess3Months() {
    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("IIT Less Than 3 Months Dimension");
    dimension.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dimension.addCohortDefinition(
        "iitless3months",
        EptsReportUtils.map(
            this.txMlCohortQueries.getPatientsWhoAreIITLessThan3Months(), mappings));

    return dimension;
  }

  public CohortDefinitionDimension findPatientsIITBetween3And5Months() {
    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("IIT Between 3 and 5 Months Dimension");
    dimension.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dimension.addCohortDefinition(
        "iitbetween3and5months",
        EptsReportUtils.map(
            this.txMlCohortQueries.getPatientsWhoAreIITBetween3And5Months(), mappings));

    return dimension;
  }

  public CohortDefinitionDimension findPatientsIITGreaterOrEqual6Months() {
    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.setName("IIT Greater or Equal 6 Months Dimension");
    dimension.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dimension.addParameter(new Parameter("endDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dimension.addCohortDefinition(
        "iitgreaterorequal6months",
        EptsReportUtils.map(
            this.txMlCohortQueries.getPatientsWhoAreIITGreaterOrEqual6Months(), mappings));

    return dimension;
  }
}
