package org.openmrs.module.eptsreports.reporting.library.dimensions;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.library.cohorts.TB4MontlyCascadeCohortQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.dimension.CohortDefinitionDimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TB4TXTBMontlyCascadeReportDimensions {

  @Autowired private TB4MontlyCascadeCohortQueries txtbDenominatorForTBMontlyCascade;

  private String mappings = "startDate=${endDate-6m+1d},endDate=${endDate},location=${location}";
  private String mappingsTillEndDate = "endDate=${endDate},location=${location}";

  public CohortDefinitionDimension getClinicalConsultationDimension() {
    final CohortDefinitionDimension dim = new CohortDefinitionDimension();
    dim.setName("clinicalConsultation");
    dim.addParameter(new Parameter("endDate", "End Date", Date.class));
    dim.addParameter(new Parameter("location", "location", Location.class));

    dim.addCohortDefinition(
        "clinicalConsultationNewly",
        EptsReportUtils.map(
            txtbDenominatorForTBMontlyCascade.getClinicalConsultationsInLastSixMonths(),
            mappingsTillEndDate));

    return dim;
  }

  public CohortDefinitionDimension getArtStartRangeDimension() {
    final CohortDefinitionDimension dim = new CohortDefinitionDimension();
    dim.setName("artStartState");
    dim.addParameter(new Parameter("endDate", "End Date", Date.class));
    dim.addParameter(new Parameter("location", "location", Location.class));

    dim.addCohortDefinition(
        "txcurrNewlyOnArt",
        EptsReportUtils.map(
            txtbDenominatorForTBMontlyCascade.getPatientsEnrollendOnARTForTheLastSixMonths(),
            mappingsTillEndDate));
    dim.addCohortDefinition(
        "txcurrPreviouslyOnArt",
        EptsReportUtils.map(
            this.txtbDenominatorForTBMontlyCascade.getPatientsEnrolledOnArtForMoreThanSixMonths(),
            mappingsTillEndDate));

    return dim;
  }

  public CohortDefinitionDimension DiagnosticTest() {
    final CohortDefinitionDimension dim = new CohortDefinitionDimension();
    dim.setName("diagnostictest");
    dim.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dim.addParameter(new Parameter("endDate", "End Date", Date.class));
    dim.addParameter(new Parameter("location", "location", Location.class));

    dim.addCohortDefinition(
        "genexpert",
        EptsReportUtils.map(this.txtbDenominatorForTBMontlyCascade.getGenExpertTests(), mappings));
    dim.addCohortDefinition(
        "baciloscopia",
        EptsReportUtils.map(
            this.txtbDenominatorForTBMontlyCascade.getBaciloscopiaTests(), mappings));
    dim.addCohortDefinition(
        "tblam",
        EptsReportUtils.map(this.txtbDenominatorForTBMontlyCascade.getTBLAMTests(), mappings));
    dim.addCohortDefinition(
        "additonalDiagnostic",
        EptsReportUtils.map(this.txtbDenominatorForTBMontlyCascade.getAdditionalTests(), mappings));
    return dim;
  }

  public CohortDefinitionDimension getPositiveTestResults() {
    final CohortDefinitionDimension dim = new CohortDefinitionDimension();
    dim.setName("posetiveTestResult");
    dim.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dim.addParameter(new Parameter("endDate", "End Date", Date.class));
    dim.addParameter(new Parameter("location", "location", Location.class));

    dim.addCohortDefinition(
        "positiveGenexpert",
        EptsReportUtils.map(
            this.txtbDenominatorForTBMontlyCascade.getGenExpertPositiveTestResults(), mappings));
    dim.addCohortDefinition(
        "positiveBaciloscopia",
        EptsReportUtils.map(
            this.txtbDenominatorForTBMontlyCascade.getBaciloscopiaPositiveTestResults(), mappings));
    dim.addCohortDefinition(
        "positiveTblam",
        EptsReportUtils.map(
            this.txtbDenominatorForTBMontlyCascade.getTBLAMPositiveTestResults(), mappings));
    dim.addCohortDefinition(
        "positiveAdditonalDiagnostic",
        EptsReportUtils.map(
            this.txtbDenominatorForTBMontlyCascade.getAdditionalPositiveTestResults(), mappings));
    return dim;
  }

  public CohortDefinitionDimension getNegativeTestResults() {
    final CohortDefinitionDimension dim = new CohortDefinitionDimension();
    dim.setName("negativeTestResult");
    dim.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dim.addParameter(new Parameter("endDate", "End Date", Date.class));
    dim.addParameter(new Parameter("location", "location", Location.class));

    dim.addCohortDefinition(
        "negativeGenexpert",
        EptsReportUtils.map(
            this.txtbDenominatorForTBMontlyCascade.getGenExpertNegativeTestResults(), mappings));
    dim.addCohortDefinition(
        "negativeBaciloscopia",
        EptsReportUtils.map(
            this.txtbDenominatorForTBMontlyCascade.getBaciloscopiaNegativeTestResults(), mappings));
    dim.addCohortDefinition(
        "negativeTblam",
        EptsReportUtils.map(
            this.txtbDenominatorForTBMontlyCascade.getTBLAMNegativeTestResults(), mappings));
    dim.addCohortDefinition(
        "negativeAdditonalDiagnostic",
        EptsReportUtils.map(
            this.txtbDenominatorForTBMontlyCascade.getAdditionalNegativeTestResults(), mappings));
    return dim;
  }

  public CohortDefinitionDimension getTBLAMGradeLevelDimension() {
    final CohortDefinitionDimension dim = new CohortDefinitionDimension();
    dim.setName("tblam-grade-level");

    dim.addParameter(new Parameter("startDate", "Start Date", Date.class));
    dim.addParameter(new Parameter("endDate", "End Date", Date.class));
    dim.addParameter(new Parameter("location", "location", Location.class));

    dim.addCohortDefinition(
        "four",
        Mapped.mapStraightThrough(
            this.txtbDenominatorForTBMontlyCascade
                .getNumberOfClientsWithGradeFourLevelPositiveTBLAMResults()));
    dim.addCohortDefinition(
        "three",
        Mapped.mapStraightThrough(
            this.txtbDenominatorForTBMontlyCascade
                .getNumberOfClientsWithGradeThreeLevelPositiveTBLAMResults()));
    dim.addCohortDefinition(
        "two",
        Mapped.mapStraightThrough(
            this.txtbDenominatorForTBMontlyCascade
                .getNumberOfClientsWithGradeTwoLevelPositiveTBLAMResults()));
    dim.addCohortDefinition(
        "one",
        Mapped.mapStraightThrough(
            this.txtbDenominatorForTBMontlyCascade
                .getNumberOfClientsWithGradeOneLevelPositiveTBLAMResults()));
    dim.addCohortDefinition(
        "no-level",
        Mapped.mapStraightThrough(
            this.txtbDenominatorForTBMontlyCascade
                .getNumberOfClientsWithGradeNoLevelPositiveTBLAMResults()));

    return dim;
  }
}
