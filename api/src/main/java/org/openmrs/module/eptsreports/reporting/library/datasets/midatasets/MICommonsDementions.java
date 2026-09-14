package org.openmrs.module.eptsreports.reporting.library.datasets.midatasets;

import static org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils.map;

import org.openmrs.module.eptsreports.reporting.library.dimensions.AgeDimensionCohortInterface;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.dimensions.MIAgeDimentions;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MICommonsDementions {

  @Autowired private EptsCommonDimension eptsCommonDimension;

  @Autowired
  @Qualifier("commonAgeDimensionCohort")
  private AgeDimensionCohortInterface ageDimensionCohort;

  @Autowired private MIAgeDimentions mIAgeDimentions;

  public void getMICommonDementions(
      final CohortIndicatorDataSetDefinition dataSetDefinition, String mappings) {

    dataSetDefinition.addDimension("gender", map(eptsCommonDimension.gender(), ""));

    dataSetDefinition.addDimension(
        "ageMiNewART",
        EptsReportUtils.map(
            this.mIAgeDimentions.getDimensionForPatientsWhoAreNewlyEnrolledOnART(), mappings));

    dataSetDefinition.addDimension(
        "ageOnCV",
        EptsReportUtils.map(
            this.mIAgeDimentions.getDimensionForPatientsPatientWithCVOver1000Copies(), mappings));

    dataSetDefinition.addDimension(
        "age",
        EptsReportUtils.map(
            this.mIAgeDimentions.getDimensionForLastClinicalConsultation(), mappings));

    dataSetDefinition.addDimension(
        "ageOnEndInclusionDate",
        EptsReportUtils.map(this.mIAgeDimentions.getDimensionAgeEndInclusionDate(), mappings));

    dataSetDefinition.addDimension(
        "ageOnB2NEW",
        EptsReportUtils.map(
            this.mIAgeDimentions
                .findAllPatientsWhoHaveTherapheuticLineSecondLineDuringInclusionPeriodP3B2NEWCalculeteAgeBiggerThan(),
            mappings));

    dataSetDefinition.addDimension(
        "ageMiEndRevisionDate",
        EptsReportUtils.map(
            mIAgeDimentions.getDimensionAgeEndInclusionDateEndRevisionDate(), mappings));

    dataSetDefinition.addDimension(
        "ageOnTheFirstConsultationDuringInclusionPeriod",
        EptsReportUtils.map(
            this.mIAgeDimentions.getDimensionAgeOnTheFirstConsultation(), mappings));

    dataSetDefinition.addDimension(
        "ageOnReinicio",
        EptsReportUtils.map(
            this.mIAgeDimentions
                .getDimensionForPatientsWhoReinitiatedTreatmentInClinicalConsultation(),
            mappings));

    dataSetDefinition.addDimension(
        "ageOnPresuntiveTB",
        EptsReportUtils.map(this.mIAgeDimentions.getDimensionAgeOnThePresuntiveTB(), mappings));

    dataSetDefinition.addDimension(
        "ageOnGeneXpertRequest",
        EptsReportUtils.map(this.mIAgeDimentions.getDimensionAgeOnGeneXpertRequest(), mappings));

    dataSetDefinition.addDimension(
        "ageOnTBDiagnostic",
        EptsReportUtils.map(this.mIAgeDimentions.getDimensionAgeOnTBDiagnostic(), mappings));
  }
}
