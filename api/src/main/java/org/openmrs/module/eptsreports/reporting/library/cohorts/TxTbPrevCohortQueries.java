package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.TxTbPrevQueriesInterface;
import org.openmrs.module.eptsreports.reporting.library.queries.TxTbPrevQueriesInterface.QUERY.DisaggregationTypes;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TxTbPrevCohortQueries {

  @Autowired private GenericCohortQueries genericCohorts;

  private static final String
      FIND_PATIENTS_WHO_STARTED_TB_PREV_PREVENTIVE_TREATMENT_DURING_PREVIOUS_REPORTING_PERIOD =
          "TBPREV/PATIENTS_WHO_STARTED_TB_PREV_PREVENTIVE_TREATMENT_DURING_PREVIOUS_REPORTING_PERIOD.sql";
  private static final String
      FIND_PATIENTS_WHO_COMPLETED_TB_PREV_PREVENTIVE_TREATMENT_DURING_REPORTING_PERIOD =
          "TBPREV/PATIENTS_WHO_COMPLETED_TB_PREV_PREVENTIVE_TREATMENT_DURING_REPORTING_PERIOD.sql";

  private static final String TRANSFERRED_OUT =
      "TRANSFERRED_OUT/FIND_PATIENTS_WHO_ARE_TRANSFERRED_OUT.sql";

  @DocumentedDefinition(value = "getTbPrevTotalDenominator")
  public CohortDefinition getTbPrevTotalDenominator() {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName("Patients Who Started TPT");
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "STARTED-TPT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Finding Patients Who have Started TPT During Previous Reporting Period",
                EptsQuerysUtils.loadQuery(
                    FIND_PATIENTS_WHO_STARTED_TB_PREV_PREVENTIVE_TREATMENT_DURING_PREVIOUS_REPORTING_PERIOD)),
            mappings));

    dsd.addSearch(
        "TRF-OUT",
        EptsReportUtils.map(
            this.findPatientsTransferredOut(),
            "startDate=${startDate-6m},endDate=${endDate},location=${location}"));

    dsd.addSearch(
        "ENDED-TPT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Finding Patients Who have Completed TPT",
                EptsQuerysUtils.loadQuery(
                    FIND_PATIENTS_WHO_COMPLETED_TB_PREV_PREVENTIVE_TREATMENT_DURING_REPORTING_PERIOD)),
            mappings));

    dsd.addSearch(
        "NEWLY-ART",
        EptsReportUtils.map(this.findPatientsWhoStartedArtAndTpiNewDessagragation(), mappings));
    dsd.addSearch(
        "PREVIOUS-ART",
        EptsReportUtils.map(
            this.findPatientsWhoStartedArtAndTpiPreviouslyDessagragation(), mappings));

    dsd.setCompositionString(
        "(STARTED-TPT AND (NEWLY-ART OR PREVIOUS-ART)) NOT (TRF-OUT NOT ENDED-TPT )");

    return dsd;
  }

  @DocumentedDefinition(value = "getTbPrevTotalNumerator")
  public CohortDefinition getTbPrevTotalNumerator() {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName("get Patients Who Completed TPT");
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "ENDED-TPT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Finding Patients Who have Completed TPT",
                EptsQuerysUtils.loadQuery(
                    FIND_PATIENTS_WHO_COMPLETED_TB_PREV_PREVENTIVE_TREATMENT_DURING_REPORTING_PERIOD)),
            mappings));
    dsd.addSearch("DENOMINATOR", EptsReportUtils.map(this.getTbPrevTotalDenominator(), mappings));

    dsd.setCompositionString("ENDED-TPT AND DENOMINATOR");

    return dsd;
  }

  @DocumentedDefinition(value = "findPatientsWhoStartedArtAndTpiNewDessagragation")
  public CohortDefinition findPatientsWhoStartedArtAndTpiNewDessagragation() {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName("get Patients Who Started Art And TPT New - Dessagragation");
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "STARTED-TPT-AND-ART",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Finding Patients New on ART Who Have Started TPT",
                TxTbPrevQueriesInterface.QUERY
                    .findPatientsWhoStartedArtAndTbPrevPreventiveTreatmentInDisaggregation(
                        DisaggregationTypes.NEWLY_ENROLLED)),
            mappings));
    dsd.setCompositionString("STARTED-TPT-AND-ART");

    return dsd;
  }

  @DocumentedDefinition(value = "findPatientsWhoStartedArtAndTpiPreviouslyDessagragation")
  public CohortDefinition findPatientsWhoStartedArtAndTpiPreviouslyDessagragation() {
    final CompositionCohortDefinition dsd = new CompositionCohortDefinition();

    dsd.setName("get Patients Who Started Art And TPT Previows - Dessagragation");
    dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
    dsd.addParameter(new Parameter("location", "location", Location.class));
    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    dsd.addSearch(
        "STARTED-TPT-AND-ART",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Finding Patients Previously on ART Who Have Started TPT",
                TxTbPrevQueriesInterface.QUERY
                    .findPatientsWhoStartedArtAndTbPrevPreventiveTreatmentInDisaggregation(
                        DisaggregationTypes.PREVIOUSLY_ENROLLED)),
            mappings));
    dsd.setCompositionString("STARTED-TPT-AND-ART");
    return dsd;
  }

  @DocumentedDefinition(value = "findPatientsTransferredOut")
  public CohortDefinition findPatientsTransferredOut() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("get Patients Who were Transferred Out");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(EptsQuerysUtils.loadQuery(TRANSFERRED_OUT));

    return definition;
  }
}
