package org.openmrs.module.eptsreports.reporting.library.dimensions;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.cohorts.ResumoMensalCETACohortQueries;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.dimension.CohortDefinitionDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResumoMensalCETADimensions {

  @Autowired private ResumoMensalCETACohortQueries resumoMensalCETACohortQueries;

  public CohortDefinitionDimension getResumoMensalCETADimension(CohortDefinition indicator) {
    final CohortDefinitionDimension dim = new CohortDefinitionDimension();
    dim.setName("resumoMensalCETADimension");

    dim.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dim.addParameter(new Parameter("endDate", "End Date", Date.class));
    dim.addParameter(new Parameter("location", "location", Location.class));

    dim.addCohortDefinition(
        "secondARVConsultation",
        Mapped.mapStraightThrough(
            this.resumoMensalCETACohortQueries.getPatientsDisagregationsSecondLineTARV26_1()));

    dim.addCohortDefinition(
        "VLGreaterThan1000",
        Mapped.mapStraightThrough(
            this.resumoMensalCETACohortQueries
                .getPatientsDisagregationsCVGreaterThan1000Copies26_2()));

    dim.addCohortDefinition(
        "reintegreted",
        Mapped.mapStraightThrough(
            this.resumoMensalCETACohortQueries
                .getPatientsDisagregationsPatientsReintegreted26_3()));

    dim.addCohortDefinition(
        "psychosocialFactors",
        Mapped.mapStraightThrough(
            this.resumoMensalCETACohortQueries.getPatientsDisagregationsPsychosocialFactors26_4()));

    dim.addCohortDefinition(
        "whithoutInformation",
        Mapped.mapStraightThrough(
            this.resumoMensalCETACohortQueries.getPatientsIndicatorWithoutInformationDisag(
                indicator)));

    return dim;
  }
}
