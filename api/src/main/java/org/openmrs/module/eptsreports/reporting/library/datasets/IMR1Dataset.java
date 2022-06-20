/*
 * The contents of this file are subject to the OpenMRS Public License Version
 * 1.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */

package org.openmrs.module.eptsreports.reporting.library.datasets;

import org.openmrs.module.eptsreports.reporting.library.cohorts.IMR1CohortQueries;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.dimensions.IMR1Dimensions;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IMR1Dataset extends BaseDataSet {

  @Autowired private IMR1CohortQueries iMR1CohortQueries;

  @Autowired private IMR1Dimensions iMR1Dimensions;

  @Autowired private EptsGeneralIndicator eptsGeneralIndicator;

  @Autowired private EptsCommonDimension eptsCommonDimension;

  public CohortIndicatorDataSetDefinition constructIMR1DataSet() {

    final CohortIndicatorDataSetDefinition dataSetDefinition =
        new CohortIndicatorDataSetDefinition();
    dataSetDefinition.setName("IMR1 Data Set");
    dataSetDefinition.addParameters(this.getParameters());

    final String mappings = "endDate=${endDate},location=${location}";

    CohortDefinition denominatorDefinition =
        this.iMR1CohortQueries.getPatientsNewlyEnrolledOnArtCare();

    CohortDefinition childrenDenominatorDefinition =
        this.iMR1CohortQueries.getChildrenNewlyEnrolledOnArtCare();

    CohortDefinition numeratorDefinition =
        this.iMR1CohortQueries.getPatientsNewlyEnrolledOnArtCareNumerator();

    CohortDefinition childrenNumeratorDefinition =
        this.iMR1CohortQueries.getChildrenNewlyEnrolledOnArtCareNumerator();

    CohortDefinition denominatorExcludingPregnantAndBreastfeedingDefinition =
        this.iMR1CohortQueries
            .getPatientsNewlyEnrolledOnArtCareExcludingPregnantsAndBreastfeedingDenominator();
    CohortDefinition numerator1_ExcludingPregnantsAndBreastFeedingDefinition =
        this.iMR1CohortQueries
            .getPatientsNewlyEnrolledOnArtCareExcludingPregnantsAndBreasFeedingNumerator();

    CohortIndicator denominatorIndicator =
        this.eptsGeneralIndicator.getIndicator(
            "PatientsNewlyEnrolledOnArtCare", EptsReportUtils.map(denominatorDefinition, mappings));

    CohortIndicator childrenDenominatorIndicator =
        this.eptsGeneralIndicator.getIndicator(
            "childrenDenominatorIndicator",
            EptsReportUtils.map(childrenDenominatorDefinition, mappings));

    CohortIndicator numeratorIndicator =
        this.eptsGeneralIndicator.getIndicator(
            "PatientsNewlyEnrolledOnArtCareNumerator1B",
            EptsReportUtils.map(numeratorDefinition, mappings));

    CohortIndicator childrenNumeratorIndicator =
        this.eptsGeneralIndicator.getIndicator(
            "PatientsNewlyEnrolledOnArtCareNumerator1B",
            EptsReportUtils.map(childrenNumeratorDefinition, mappings));

    CohortIndicator denominatorExcludingPregnantsAndBreastFeedingIndicator =
        this.eptsGeneralIndicator.getIndicator(
            "PatientsNewlyEnrolledOnArtCareNumeratorExcludingPregnantsAndBreastFeedingDenominator",
            EptsReportUtils.map(denominatorExcludingPregnantAndBreastfeedingDefinition, mappings));

    CohortIndicator numeratorExcludingPregnantsAndBreastFeedingIndicator =
        this.eptsGeneralIndicator.getIndicator(
            "PatientsNewlyEnrolledOnArtCareExcludingPregnantsAndBreastFeedingNumerator",
            EptsReportUtils.map(numerator1_ExcludingPregnantsAndBreastFeedingDefinition, mappings));

    dataSetDefinition.addDimension(
        "state", EptsReportUtils.map(iMR1Dimensions.getDimension(), mappings));
    dataSetDefinition.addDimension("gender", EptsReportUtils.map(eptsCommonDimension.gender(), ""));

    dataSetDefinition.addColumn(
        "D-All", "Denominator: All", EptsReportUtils.map(denominatorIndicator, mappings), "");
    dataSetDefinition.addColumn(
        "D-PREGNANT",
        "Denominator Pregnant",
        EptsReportUtils.map(denominatorIndicator, mappings),
        "state=PREGNANT");
    dataSetDefinition.addColumn(
        "D-BREASTFEEDING",
        "Denominator Breastfeendig",
        EptsReportUtils.map(denominatorIndicator, mappings),
        "state=BREASTFEEDING");
    dataSetDefinition.addColumn(
        "D-CHILDREN",
        "Denominator children",
        EptsReportUtils.map(childrenDenominatorIndicator, mappings),
        "");
    dataSetDefinition.addColumn(
        "D-NON-PREGNANT",
        "Denominator Non Pregnant and Breastfeeding",
        EptsReportUtils.map(denominatorExcludingPregnantsAndBreastFeedingIndicator, mappings),
        "");

    dataSetDefinition.addColumn(
        "N-All", "Numerator: All", EptsReportUtils.map(numeratorIndicator, mappings), "");
    dataSetDefinition.addColumn(
        "N-PREGNANT",
        "Numerator Pregnant",
        EptsReportUtils.map(numeratorIndicator, mappings),
        "state=PREGNANT");
    dataSetDefinition.addColumn(
        "N-BREASTFEEDING",
        "Numerator Breastfeendig",
        EptsReportUtils.map(numeratorIndicator, mappings),
        "state=BREASTFEEDING");
    dataSetDefinition.addColumn(
        "N-CHILDREN",
        "Numerator children",
        EptsReportUtils.map(childrenNumeratorIndicator, mappings),
        "");
    dataSetDefinition.addColumn(
        "N-NON-PREGNANT",
        "Numerator Non Pregnant and Breasfeeding",
        EptsReportUtils.map(numeratorExcludingPregnantsAndBreastFeedingIndicator, mappings),
        "");

    return dataSetDefinition;
  }
}
