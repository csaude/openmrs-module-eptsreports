/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.eptsreports.reporting.cohort.evaluator;

import java.util.Set;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.eptsreports.reporting.cohort.definition.FGHCalculationCohortDefinition;
import org.openmrs.module.eptsreports.reporting.utils.EptsCalculationUtils;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

/** Evaluator for calculation based cohorts */
@Handler(supports = FGHCalculationCohortDefinition.class)
public class FGHCalculationCohortDefinitionEvaluator implements CohortDefinitionEvaluator {

  @SuppressWarnings("deprecation")
  @Override
  public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context)
      throws EvaluationException {
    FGHCalculationCohortDefinition cd = (FGHCalculationCohortDefinition) cohortDefinition;

    CalculationResultMap map = cd.getCalculation().evaluate(cd.getCalculationParameters(), context);
    context.addToCache("location", cd.getLocation());
    context.addToCache("onOrAfter", cd.getOnOrAfter());
    context.addToCache("onOrBefore", cd.getOnOrBefore());

    Set<Integer> passing =
        EptsCalculationUtils.patientsThatPass(
            map, cd.getWithResult(), cd.getWithResultFinder(), context);

    System.out.println(
        "FGHCalculationCohortDefinitionEvaluator resultMAP =>" + map.keySet().size());

    System.out.println("FGHCalculationCohortDefinitionEvaluator passing =>" + passing.size());

    EvaluatedCohort evaluatedCohort =
        new EvaluatedCohort(new Cohort(passing), cohortDefinition, context);

    System.out.println(" PatientEvaluated => " + evaluatedCohort.getPatientIds().size());
    return evaluatedCohort; // new EvaluatedCohort(new Cohort(passing), cohortDefinition, context);
  }
}
