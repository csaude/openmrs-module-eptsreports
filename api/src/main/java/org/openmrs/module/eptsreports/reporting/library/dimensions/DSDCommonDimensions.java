/** */
package org.openmrs.module.eptsreports.reporting.library.dimensions;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.cohorts.GenericCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.DSDQueriesInterface;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.eptsreports.reporting.utils.TypePTV;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.dimension.CohortDefinitionDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DSDCommonDimensions {

  @Autowired private GenericCohortQueries genericCohortQueries;

  public CohortDefinitionDimension getDimensions() {

    final CohortDefinitionDimension dimension = new CohortDefinitionDimension();

    dimension.addParameter(new Parameter("endDate", "End Date", Date.class));
    dimension.addParameter(new Parameter("location", "location", Location.class));
    dimension.setName("Get patient states");

    dimension.addCohortDefinition(
        "PREGNANT",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "PREGNANT",
                DSDQueriesInterface.QUERY.findPatientsWhoArePregnantsAndBreastFeeding(
                    TypePTV.PREGNANT)),
            "endDate=${endDate},location=${location}"));

    dimension.addCohortDefinition(
        "BREASTFEEDING",
        EptsReportUtils.map(
            this.genericCohortQueries.generalSql(
                "BREASTFEEDING",
                DSDQueriesInterface.QUERY.findPatientsWhoArePregnantsAndBreastFeeding(
                    TypePTV.BREASTFEEDING)),
            "endDate=${endDate},location=${location}"));

    return dimension;
  }
}
