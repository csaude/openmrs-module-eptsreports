package org.openmrs.module.eptsreports.reporting.library.datasets;

import org.openmrs.module.eptsreports.reporting.library.cohorts.Eri2MonthsCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.dimensions.Eri2MonthsDamision;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Erin2MonthDataSets extends BaseDataSet {

  @Autowired private Eri2MonthsCohortQueries eri2MonthsCohortQueries;

  @Autowired private Eri2MonthsDamision eri2MonthsDamision;

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  public DataSetDefinition constructEri2MonthsDataset() {

    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();

    dataSetDefinition.setName("Erin 2 Months Data Set");
    dataSetDefinition.addParameters(this.getParameters());

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    final CohortDefinition patientEnrolledInART2Months =
        this.eri2MonthsCohortQueries.getEri2MonthsCompositionCohort("patientEnrolledInART2Months");

    final CohortIndicator patientEnrolledInHIVStartedART2MonthsIndicator =
        this.eptsGeneralIndicator.getIndicator(
            "patientEnrolledInHIVStartedART2MonthsIndicator",
            EptsReportUtils.map(patientEnrolledInART2Months, mappings));

    dataSetDefinition.addDimension(
        "1All",
        EptsReportUtils.map(eri2MonthsDamision.findPatientsWhoStartArtOneMonth(), mappings));

    dataSetDefinition.addColumn(
        "1All",
        "Erin 2 Months: New on ART 2 Months",
        EptsReportUtils.map(patientEnrolledInHIVStartedART2MonthsIndicator, mappings),
        "");

    dataSetDefinition.addDimension(
        "ERI2.R21-02",
        EptsReportUtils.map(
            eri2MonthsDamision
                .findPatientsWhoStartedArtAtAPeriodAndDidNotHaveASecondPickupsOrClinicalConsultationWithin33DaysAfterArtInitiation(),
            mappings));

    dataSetDefinition.addColumn(
        "ERI2.R21-02",
        "Erin 2 Months: New on ART 2 Month Not Have Pickup and Clinical Consultation",
        EptsReportUtils.map(patientEnrolledInHIVStartedART2MonthsIndicator, mappings),
        "");

    dataSetDefinition.addDimension(
        "ERI2.R21-03",
        EptsReportUtils.map(
            eri2MonthsDamision
                .findPatientsStartedArtLastMonthWith2PickupsOrConsultationWithin33DaysExcludingDeadAndTransferedOutAndIn(),
            mappings));

    dataSetDefinition.addColumn(
        "ERI2.R21-03",
        "Erin 2 Months: Start ART Exclud Trasfered Out",
        EptsReportUtils.map(patientEnrolledInHIVStartedART2MonthsIndicator, mappings),
        "");

    dataSetDefinition.addDimension(
        "ERI2.R21-04",
        EptsReportUtils.map(
            eri2MonthsDamision
                .findPatientsWhoStartedArtInAPeriodAndAreDeath33DaysAfterArtInitiation(),
            mappings));

    dataSetDefinition.addColumn(
        "ERI2.R21-04",
        "Erin 2 Months: DEAD",
        EptsReportUtils.map(patientEnrolledInHIVStartedART2MonthsIndicator, mappings),
        "");

    dataSetDefinition.addDimension(
        "ERI2.R21-05",
        EptsReportUtils.map(
            eri2MonthsDamision
                .findPatientsWhoStartedArtInAPeriodAndAreTrasferedOut33DaysAfterInitiation(),
            mappings));

    dataSetDefinition.addColumn(
        "ERI2.R21-05",
        "Erin 2 Months: Transfered Out",
        EptsReportUtils.map(patientEnrolledInHIVStartedART2MonthsIndicator, mappings),
        "");

    dataSetDefinition.addDimension(
        "ERI2.R21-06",
        EptsReportUtils.map(
            eri2MonthsDamision
                .findPatientsWhoStartedArtInAPeriodAndSuspendTratement33DaysAfterInitiation(),
            mappings));

    dataSetDefinition.addColumn(
        "ERI2.R21-06",
        "Erin 2 Months: Suspend",
        EptsReportUtils.map(patientEnrolledInHIVStartedART2MonthsIndicator, mappings),
        "");
    return dataSetDefinition;
  }
}
