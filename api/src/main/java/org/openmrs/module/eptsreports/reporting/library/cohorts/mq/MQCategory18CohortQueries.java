package org.openmrs.module.eptsreports.reporting.library.cohorts.mq;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.cohorts.ResumoMensalCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQCategory18QueriesInterface;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQCategory18CohortQueries {

  @Autowired private MQCohortQueries mQCohortQueries;
  @Autowired private ResumoMensalCohortQueries resumoMensalCohortQueries;

  @DocumentedDefinition(value = "findChildrenAndAdolescentsWithFullDisclore")
  public CohortDefinition findChildrenAndAdolescentsWithFullDisclore() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQCategory18QueriesInterface.QUERY.findChildrenAndAdolescentsWithFullDisclore;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsNewlyEnrolledByAgeInAPeriodExcludingTrasferedInAdultCategory3RF11Denominator")
  public CohortDefinition
      findChildrenAndAdolescentsWithAgeBetween8And9YearsWhoStartArtInTheInclusionPeriodDenominator18() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findPatientsNewlyEnrolledByAgeInAPeriodExcludingTrasferedInAdultCategory3RF11Denominator");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-14m+1d},endInclusionDate=${endRevisionDate-11m},endRevisionDate=${endRevisionDate},location=${location}";

    final String mappingsEndRevisionDate = "endDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "START-ART",
        EptsReportUtils.map(
            this.mQCohortQueries.findPatientsWhoAreNewlyEnrolledOnARTRF05(), mappings));

    definition.addSearch(
        "B13",
        EptsReportUtils.map(
            this.resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(),
            mappingsEndRevisionDate));

    definition.addSearch(
        "TRANSFERED-IN",
        EptsReportUtils.map(
            this.mQCohortQueries
                .findPatientsWhoWhereMarkedAsTransferedInAndOnARTOnInAPeriodOnMasterCardRF06(),
            mappings));

    definition.setCompositionString("(START-ART AND B13) NOT TRANSFERED-IN");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findChildrenAndAdolescentsWithAgeBetween8And9YearsWhoStartArtInTheInclusionPeriodNumerator18")
  public CohortDefinition
      findChildrenAndAdolescentsWithAgeBetween8And9YearsWhoStartArtInTheInclusionPeriodNumerator18() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "findChildrenAndAdolescentsWithAgeBetween8And9YearsWhoStartArtInTheInclusionPeriodNumerator18");

    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(
            this
                .findChildrenAndAdolescentsWithAgeBetween8And9YearsWhoStartArtInTheInclusionPeriodDenominator18(),
            mappings));

    definition.addSearch(
        "TOTAL-REVELATION",
        EptsReportUtils.map(this.findChildrenAndAdolescentsWithFullDisclore(), mappings));

    definition.setCompositionString("DENOMINATOR AND TOTAL-REVELATION ");

    return definition;
  }
}
