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
package org.openmrs.module.eptsreports.reporting.library.queries;

import java.util.Arrays;
import org.apache.commons.lang.StringUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;

public interface TB4MontlyCascadeReportQueries {

  public class QUERY {

    private static final String FIND_PATIENTS_WHO_ARE_CURRENTLY_ENROLLED_ON_ART =
        "TPT_CASCADE/TPT_CASCADE_PATIENTS_START_ART.sql";

    public static final String FIND_PATIENTS_WITH_POSITIVE_TBLAM_RESULT_AND_POSITIVITY_LEVEL =
        "TB4/TBLAM_POSITIVITY_LEVEL.sql";

    public static final String FIND_PATIENTS_WITH_POSITIVE_TBLAM_RESULT_WITHOUT_POSITIVITY_LEVEL =
        "TB4/TBLAM_WITHOUT_POSITIVITY_LEVEL.sql";

    public static final String findPatientsWithClinicalConsultationIntheLastSixMonths =
        "																	"
            + "	select p.patient_id from patient p 															"
            + "		inner join encounter e on e.patient_id = p.patient_id 																					"
            + "	where p.voided is false and e.voided is false and e.encounter_type in (6,9) and e.location_id = :location  "
            + " 	and e.encounter_datetime between (:endDate - interval 6 month + interval 1 day)  and :endDate ";

    public enum EnrollmentPeriod {
      NEWLY,
      PREVIOUSLY;
    }

    public static final String findPatientsWhoAreCurrentlyEnrolledOnARTByPeriod(
        EnrollmentPeriod enrollmentPeriod) {

      String query = EptsQuerysUtils.loadQuery(FIND_PATIENTS_WHO_ARE_CURRENTLY_ENROLLED_ON_ART);

      switch (enrollmentPeriod) {
        case NEWLY:
          query +=
              " where data_inicio > date_add(:endDate, interval - 6 month) and data_inicio <= :endDate";
          break;

        case PREVIOUSLY:
          query += "";
          break;
      }
      return query;
    }

    public enum DiagnosticTestTypes {
      GENEXPERT,
      BACILOSCOPIA,
      CULTURA;
    }

    public static final String findDiagnosticTestsWithPositiveTestResults(
        DiagnosticTestTypes diagnosticTestType) {
      String query =
          "																														"
              + "select distinct obs.person_id 																																"
              + "from obs 																																					"
              + "		inner join encounter on encounter.encounter_id = obs.encounter_id 																						"
              + "where encounter.encounter_type in (6,9) 																														"
              + "		and obs.concept_id in(#) and obs.value_coded =703																			 							"
              + "		and encounter.voided = 0 and obs.voided =0 and obs.location_id =:location 																				"
              + "		and encounter.encounter_datetime >=:startDate and encounter.encounter_datetime <= :endDate																"
              + "union																																						"
              + "select distinct obs.person_id 																																"
              + "from obs 																																					"
              + "		inner join encounter on encounter.encounter_id = obs.encounter_id 																						"
              + "where encounter.encounter_type = 13 																															"
              + "		and obs.concept_id in (#)and obs.value_coded in (703,1065,165190,6230,6229,6228,165587) and encounter.voided = 0 and obs.voided =0 and obs.location_id =:location 					"
              + "  	and encounter.encounter_datetime >=:startDate  and encounter.encounter_datetime <=:endDate																";

      switch (diagnosticTestType) {
        case GENEXPERT:
          query =
              StringUtils.replace(query, "#", StringUtils.join(Arrays.asList(23723, 165588), ","));
          break;
        case BACILOSCOPIA:
          query = StringUtils.replace(query, "#", "307");
          break;
        case CULTURA:
          query = StringUtils.replace(query, "#", "23774");
          break;
      }
      return query;
    }

    public static final String findDiagnosticTestsWithPositiveTestResultsTBLAM() {
      return "																														"
          + "select distinct obs.person_id 																																"
          + "from obs 																																					"
          + "		inner join encounter on encounter.encounter_id = obs.encounter_id 																						"
          + "where encounter.encounter_type in (6,9) 																														"
          + "		and obs.concept_id in(23951) and obs.value_coded =703																			 							"
          + "		and encounter.voided = 0 and obs.voided =0 and obs.location_id =:location 																				"
          + "		and encounter.encounter_datetime >=:startDate and encounter.encounter_datetime <= :endDate																"
          + "union																																						"
          + "select distinct obs.person_id 																																"
          + "from obs 																																					"
          + "		inner join encounter on encounter.encounter_id = obs.encounter_id 																						"
          + "where encounter.encounter_type in(13, 51) 																															"
          + "		and obs.concept_id = 23951 and obs.value_coded in (703,1065) and encounter.voided = 0 and obs.voided =0 and obs.location_id =:location 					"
          + "  	and encounter.encounter_datetime >=:startDate  and encounter.encounter_datetime <=:endDate																";
    }

    public static final String findDiagnosticTestsWithNegativeTestResults(
        DiagnosticTestTypes diagnosticTestType) {
      String query =
          "																														"
              + "select distinct obs.person_id 																																"
              + "from obs 																																					"
              + "		inner join encounter on encounter.encounter_id = obs.encounter_id 																						"
              + "where encounter.encounter_type in (6,9) 																														"
              + "		and obs.concept_id in(#) and obs.value_coded =664																			 							"
              + "		and encounter.voided = 0 and obs.voided =0 and obs.location_id =:location 																				"
              + "		and encounter.encounter_datetime >=:startDate and encounter.encounter_datetime <= :endDate																"
              + "union																																						"
              + "select distinct obs.person_id 																																"
              + "from obs 																																					"
              + "		inner join encounter on encounter.encounter_id = obs.encounter_id 																						"
              + "where encounter.encounter_type = 13 																															"
              + "		and obs.concept_id in (#)and obs.value_coded in (664, 165184,1066) and encounter.voided = 0 and obs.voided =0 and obs.location_id =:location 					"
              + "  	and encounter.encounter_datetime >=:startDate  and encounter.encounter_datetime <=:endDate																";

      switch (diagnosticTestType) {
        case GENEXPERT:
          query =
              StringUtils.replace(query, "#", StringUtils.join(Arrays.asList(23723, 165588), ","));
          break;
        case BACILOSCOPIA:
          query = StringUtils.replace(query, "#", "307");
          break;
        case CULTURA:
          query = StringUtils.replace(query, "#", "23774");
          break;
      }
      return query;
    }

    public static final String findDiagnosticTestsWithNegativeTestResultsTBLAM() {
      return "																														"
          + "select distinct obs.person_id 																																"
          + "from obs 																																					"
          + "		inner join encounter on encounter.encounter_id = obs.encounter_id 																						"
          + "where encounter.encounter_type in (6,9) 																														"
          + "		and obs.concept_id in(23951) and obs.value_coded =664																			 							"
          + "		and encounter.voided = 0 and obs.voided =0 and obs.location_id =:location 																				"
          + "		and encounter.encounter_datetime >=:startDate and encounter.encounter_datetime <= :endDate																"
          + "union																																						"
          + "select distinct obs.person_id 																																"
          + "from obs 																																					"
          + "		inner join encounter on encounter.encounter_id = obs.encounter_id 																						"
          + "where encounter.encounter_type in (13, 51) 																													"
          + "		and obs.concept_id = 23951 and obs.value_coded = 664 and encounter.voided = 0 and obs.voided =0 and obs.location_id =:location 							"
          + "  	and encounter.encounter_datetime >=:startDate  and encounter.encounter_datetime <=:endDate																";
    }
  }

  public enum PositivityLevel {
    NO_GRADE(null),

    GRADE_ONE(165186),

    GRADE_TWO(165187),

    GRADE_THREE(165188),

    GRADE_FOUR(165348);

    private final Integer value;

    PositivityLevel(final Integer value) {
      this.value = value;
    }

    public Integer getValue() {
      return this.value;
    }
  }
}
