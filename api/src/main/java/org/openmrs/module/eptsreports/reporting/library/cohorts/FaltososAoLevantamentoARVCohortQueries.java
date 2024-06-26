package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.queries.FaltososAoLevantamentoARVQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

@Component
public class FaltososAoLevantamentoARVCohortQueries {

  private static final String PACIENTES_FALTOSOS_AO_LEVANTAMENTO =
      "FALTOSOS_LEVANTAMENTO_ARV/pacientes_faltosos_ao_levantamento_arv.sql";

  private static final String PACIENTES_SEM_SUPRESSAO_CARGA_VIRAL =
      "FALTOSOS_LEVANTAMENTO_ARV/pacientes_sem_supressao_viral.sql";

  @DocumentedDefinition(value = "findPatientswhoHaveScheduledAppointmentsDuringReportingPeriod")
  public CohortDefinition findPatientswhoHaveScheduledAppointmentsDuringReportingPeriod() {
    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientswhoHaveScheduledAppointmentsDuringReportingPeriod");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    definition.setQuery(
        FaltososAoLevantamentoARVQueries
            .findPatientswhoHaveScheduledAppointmentsDuringReportingPeriod());

    return definition;
  }

  @DocumentedDefinition(value = "TRF-OUT")
  public CohortDefinition getPatientsTransferredFromAnotherHealthFacilityUntilEndDate() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("getPatientsTransferredFromAnotherHealthFacilityUntilEndDate");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        FaltososAoLevantamentoARVQueries
            .getPatientsTransferredFromAnotherHealthFacilityUntilEndDate();
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "DIED")
  public CohortDefinition getPatientsWhoDied() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("getPatientsWhoDied");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = FaltososAoLevantamentoARVQueries.getPatientsWhoDied();
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "SUSPEND")
  public CohortDefinition getPatientsWhoSuspendTratment() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("getPatientsWhoSuspendTratment");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = FaltososAoLevantamentoARVQueries.getPatientsWhoSuspendTratment();
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "NUMERATOR-B")
  public CohortDefinition
      findPatientswhoHaveScheduledAppointmentsDuringReportingPeriodNumeratorB() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("findPatientswhoHaveScheduledAppointmentsDuringReportingPeriodNumeratorB");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = EptsQuerysUtils.loadQuery(PACIENTES_FALTOSOS_AO_LEVANTAMENTO);
    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "NUMERATOR-C")
  public CohortDefinition
      findPatientswhoHaveScheduledAppointmentsDuringReportingPeriodNumeratorC() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("findPatientswhoHaveScheduledAppointmentsDuringReportingPeriodNumeratorC");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        FaltososAoLevantamentoARVQueries
            .findPatientswhoHaveScheduledAppointmentsDuringReportingPeriodNumeratorC();
    definition.setQuery(query);

    return definition;
  }

  public CohortDefinition getTotalDenominator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("getTotalDenominator");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this.findPatientswhoHaveScheduledAppointmentsDuringReportingPeriod(), mappings));

    definition.addSearch(
        "TRF-OUT",
        EptsReportUtils.map(
            this.getPatientsTransferredFromAnotherHealthFacilityUntilEndDate(), mappings));

    definition.addSearch("DIED", EptsReportUtils.map(this.getPatientsWhoDied(), mappings));

    definition.addSearch(
        "SUSPEND", EptsReportUtils.map(this.getPatientsWhoSuspendTratment(), mappings));

    definition.setCompositionString(" DENOMINATOR NOT(TRF-OUT OR DIED OR SUSPEND)");

    return definition;
  }

  private CohortDefinition getTotalNun() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("getTotalNumerator");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch(
        "NUMERATOR-B",
        EptsReportUtils.map(
            this.findPatientswhoHaveScheduledAppointmentsDuringReportingPeriodNumeratorB(),
            mappings));

    definition.addSearch(
        "NUMERATOR-C",
        EptsReportUtils.map(
            this.findPatientswhoHaveScheduledAppointmentsDuringReportingPeriodNumeratorC(),
            mappings));

    definition.setCompositionString("(NUMERATOR-B OR NUMERATOR-C)");

    return definition;
  }

  public CohortDefinition getTotalNumerator() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("getTotalNumerator");
    definition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    definition.addSearch("DENOMINATOR", EptsReportUtils.map(this.getTotalDenominator(), mappings));

    definition.addSearch("NUMERATOR", EptsReportUtils.map(this.getTotalNun(), mappings));

    definition.setCompositionString("(DENOMINATOR AND NUMERATOR");

    return definition;
  }

  @DocumentedDefinition(value = "patientsWithoutSupressedViralLoad")
  public CohortDefinition getPatientsWithoutSurpressedViralLoad() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();
    definition.setName("patientsWithoutSupressedViralLoad");
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = EptsQuerysUtils.loadQuery(PACIENTES_SEM_SUPRESSAO_CARGA_VIRAL);

    definition.setQuery(query);

    return definition;
  }
}
