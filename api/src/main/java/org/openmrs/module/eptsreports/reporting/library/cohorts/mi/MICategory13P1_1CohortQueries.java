package org.openmrs.module.eptsreports.reporting.library.cohorts.mi;

import java.util.Date;
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCategory13Section1CohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.mq.MQCohortQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MICategory13P1_1CohortQueries {

  @Autowired private MQCategory13Section1CohortQueries MQCategory13Section1CohortQueries;
  @Autowired private MQCohortQueries mQCohortQueries;

  @DocumentedDefinition(value = "findDenominatorCategory13SectionIB")
  public CohortDefinition findDenominatorCategory13SectionIB() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findDenominatorCategory13SectionIB");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-2m+1d},endInclusionDate=${endRevisionDate-1m},endRevisionDate=${endRevisionDate},location=${location}";

    final String mappingsMI =
        "startInclusionDate=${endRevisionDate-2m+1d},endInclusionDate=${endRevisionDate-1m},endRevisionDate=${endRevisionDate-1m},location=${location}";

    final String mappingsReinitiated =
        "startInclusionDate=${endRevisionDate-2m+1d},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate-1m},location=${location}";

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            MQCategory13Section1CohortQueries
                .findPatientsWithLastClinicalConsultationDenominatorB1(),
            mappingsMI));

    definition.addSearch(
        "B2NEW",
        EptsReportUtils.map(
            MQCategory13Section1CohortQueries
                .findPatientsWithLastClinicalConsultationwhoAreNotInFistLineDenominatorB2NEW(),
            mappings));

    definition.addSearch(
        "B3",
        EptsReportUtils.map(
            MQCategory13Section1CohortQueries
                .findPatientsWithLastClinicalConsultationwhoAreInLinhaAlternativaDenominatorB3(),
            mappingsMI));

    definition.addSearch(
        "B3E",
        EptsReportUtils.map(
            MQCategory13Section1CohortQueries
                .findPatientsWithLastClinicalConsultationwhoAreDiferentFirstLineLinhaAternativaDenominatorB3E(),
            mappingsMI));

    definition.addSearch(
        "B5E",
        EptsReportUtils.map(
            MQCategory13Section1CohortQueries
                .findPatientsWithRequestCVInTheLast12MonthsBeforeLastConsultation(),
            mappingsMI));

    definition.addSearch(
        "C",
        EptsReportUtils.map(
            MQCategory13Section1CohortQueries.findPatientsWhoArePregnantCAT13Part1(), mappingsMI));

    definition.addSearch(
        "DROPPEDOUT",
        EptsReportUtils.map(
            mQCohortQueries
                .findAllPatientsWhoDroppedOutARTDuringTheLastSixMonthsBeforeLastClinicalConsultation(),
            mappings));

    definition.addSearch(
        "REINITIATED-ART",
        EptsReportUtils.map(
            MQCategory13Section1CohortQueries
                .findPatientsMarkedAsReinitiatedARTForAtLeastSixMonthsBeforeLastClinicalConsultation(),
            mappingsReinitiated));

    definition.setCompositionString(
        "(B1 AND (B2NEW OR REINITIATED-ART OR (B3 NOT B3E ))) NOT B5E NOT C NOT DROPPEDOUT");

    return definition;
  }

  @DocumentedDefinition(value = "findFinalNumeratorCategory13SectionIC")
  public CohortDefinition findFinalNumeratorCategory13SectionIC() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("findFinalNumeratorCategory13SectionIC");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-2m+1d},endInclusionDate=${endRevisionDate-1m},endRevisionDate=${endRevisionDate},location=${location}";

    final String mappingsMI =
        "startInclusionDate=${endRevisionDate-2m+1d},endInclusionDate=${endRevisionDate-1m},endRevisionDate=${endRevisionDate-1m},location=${location}";

    definition.addSearch(
        "Denominator", EptsReportUtils.map(this.findDenominatorCategory13SectionIB(), mappings));

    definition.addSearch(
        "G",
        EptsReportUtils.map(
            MQCategory13Section1CohortQueries.findNumeratorCategory13Section1C(), mappingsMI));

    definition.setCompositionString("(Denominator AND G)");

    return definition;
  }
}
