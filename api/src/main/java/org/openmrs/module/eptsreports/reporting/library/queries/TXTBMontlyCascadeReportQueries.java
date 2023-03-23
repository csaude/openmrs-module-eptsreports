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

public interface TXTBMontlyCascadeReportQueries {

  public class QUERY {

    private static final String FIND_PATIENTS_WHO_ARE_CURRENTLY_ENROLLED_ON_ART =
        "TPT_CASCADE/TPT_CASCADE_PATIENTS_START_ART.sql";

    public static final String findPatientsWithClinicalConsultationIntheLastSixMonths =
        "																	"
            + "select distinct lastConsultation.patient_id 																									"
            + "from	( 																																		"
            + "	select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p 															"
            + "		inner join encounter e on e.patient_id = p.patient_id 																					"
            + "	where p.voided is false and e.voided is false and e.encounter_type in (6,9) and e.location_id = :location 									"
            + "		group by p.patient_id 																													"
            + "	) 																																			"
            + "lastConsultation 																															"
            + "	inner join encounter e on e.patient_id = lastConsultation.patient_id								 		"
            + "where e.voided is false and e.location_id = :location  																						"
            + "	and e.encounter_datetime between (:endDate - interval 6 month) and :endDate and e.encounter_type in (6,9) 																";

    public static final String findPatientsWithClinicalConsultationsForMoreThanSixMonths =
        "																"
            + "select distinct lastConsultation.patient_id 																									"
            + "from	( 																																		"
            + "	select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p 															"
            + "		inner join encounter e on e.patient_id = p.patient_id 																					"
            + "	where p.voided is false and e.voided is false and e.encounter_type in (6,9) and e.location_id = :location 									"
            + "		group by p.patient_id 																													"
            + "	) 																																			"
            + "lastConsultation 																															"
            + "	inner join encounter e on e.patient_id = lastConsultation.patient_id 																		"
            + "where e.voided is false and e.location_id = :location and e.encounter_datetime < (:endDate - interval 6 month) and :endDate and e.encounter_type in (6,9) 								";

    public enum EnrollmentPeriod {
      NEWLY,
      PREVIOUSLY;
    }

    public static final String findPatientsWhoAreCurrentlyEnrolledOnARTByPeriod(
        EnrollmentPeriod enrollmentPeriod) {

      String query = EptsQuerysUtils.loadQuery(FIND_PATIENTS_WHO_ARE_CURRENTLY_ENROLLED_ON_ART);

      switch (enrollmentPeriod) {
        case NEWLY:
          query += " where data_inicio between (:endDate - interval 6 month) and :endDate";
          break;

        case PREVIOUSLY:
          query += " where data_inicio < (:endDate - interval 6 month)";
          break;
      }
      return query;
    }

    public enum DiagnosticTestTypes {
      GENEXPERT,
      BACILOSCOPIA,
      TBLAM,
      CULTURA;
    }

    public static final String findDiagnosticTests(DiagnosticTestTypes diagnosticTestType) {
      String query =
          "																														"
              + "select distinct obs.person_id 																																"
              + "from obs 																																					"
              + "		inner join encounter on encounter.encounter_id = obs.encounter_id 																						"
              + "where encounter.encounter_type in (6,9) 																														"
              + "		and ((obs.concept_id = 23722 and obs.value_coded in (#)) or (obs.concept_id in(#) and obs.value_coded in (703,664)) )  							"
              + "		and encounter.voided = 0 and obs.voided =0 and obs.location_id =:location 																				"
              + "		and encounter.encounter_datetime >=:startDate and encounter.encounter_datetime <= :endDate																"
              + "union																																						"
              + "select distinct obs.person_id 																																"
              + "from obs 																																					"
              + "		inner join encounter on encounter.encounter_id = obs.encounter_id 																						"
              + "where encounter.encounter_type = 13 																															"
              + "		and obs.concept_id in (#)and obs.value_coded in (703,664,1065,1066,165184) and encounter.voided = 0 and obs.voided =0 and obs.location_id =:location 					"
              + "  	and encounter.encounter_datetime >=:startDate  and encounter.encounter_datetime <=:endDate																";

      switch (diagnosticTestType) {
        case GENEXPERT:
          query =
              StringUtils.replace(query, "#", StringUtils.join(Arrays.asList(23723, 165189), ","));
          break;

        case BACILOSCOPIA:
          query = StringUtils.replace(query, "#", "307");
          break;
        case TBLAM:
          query = StringUtils.replace(query, "#", "23951");
          break;
        case CULTURA:
          query = StringUtils.replace(query, "#", "23774");
          break;
      }
      return query;
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
              + "		and obs.concept_id in (#)and obs.value_coded in (703,1065) and encounter.voided = 0 and obs.voided =0 and obs.location_id =:location 					"
              + "  	and encounter.encounter_datetime >=:startDate  and encounter.encounter_datetime <=:endDate																";

      switch (diagnosticTestType) {
        case GENEXPERT:
          query =
              StringUtils.replace(query, "#", StringUtils.join(Arrays.asList(23723, 165189), ","));
          break;

        case BACILOSCOPIA:
          query = StringUtils.replace(query, "#", "307");
          break;
        case TBLAM:
          query = StringUtils.replace(query, "#", "23951");
          break;
        case CULTURA:
          query = StringUtils.replace(query, "#", "23774");
          break;
      }
      return query;
    }
  }
}
