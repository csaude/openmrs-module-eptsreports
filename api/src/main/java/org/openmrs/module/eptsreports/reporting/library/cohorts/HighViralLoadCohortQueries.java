package org.openmrs.module.eptsreports.reporting.library.cohorts;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HighViralLoadCohortQueries {

  @Autowired private GenericCohortQueries genericCohorts;
  private static final String PATIENTS_WITH_HIGH_VL_AGGREGATE_FIRST_HIGH_VL =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/FIRST_HIGH_VL.sql";
  private static final String PATIENTS_WITH_HIGH_VL_AGGREGATE_FIRST_FC =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/FIRST_FC_WITH_HIGH_VL.sql";
  private static final String APSS0_AFTER_HIGH_VL =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/APSS0_AFTER_HIGH_VL.sql";
  private static final String APSS1_AFTER_HIGH_VL =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/APSS1_AFTER_HIGH_VL.sql";
  private static final String APSS2_AFTER_HIGH_VL =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/APSS2_AFTER_HIGH_VL.sql";

  private static final String APSS3_AFTER_HIGH_VL =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/APSS3_AFTER_HIGH_VL.sql";
  private static final String FC_SECOND_VL_REQUEST =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/FC_SECOND_VL_REQUEST.sql";

  private static final String REPEAT_SECOND_VL =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/REPEAT_SECOND_VL.sql";

  private static final String SECOND_VL_RESULT_LAB_ELAB =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/SECOND_VL_RESULT_LAB_ELAB.sql";

  private static final String SECOND_VL_RESULT =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/SECOND_VL_RESULT.sql";

  private static final String NEW_TERAPEUTIC_LINE =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/NEW_TERAPEUTIC_LINE.sql";

  private static final String TERAPEUTIC_LINE =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/TERAPEUTIC_LINE.sql";

  private static final String APSS0_AFTER_CV2 =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/APSS0_AFTER_CV2.sql";

  private static final String APSS1_AFTER_CV2 =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/APSS1_AFTER_CV2.sql";

  private static final String APSS2_AFTER_CV2 =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/APSS2_AFTER_CV2.sql";

  private static final String APSS3_AFTER_CV2 =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/APSS3_AFTER_CV2.sql";

  private static final String THIRD_VL_REQUEST =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/THIRD_VL_REQUEST.sql";

  private static final String THIRD_VL_REPEAT =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/THIRD_VL_REPEAT.sql";

  private static final String THIRD_VL_RESULT_LAB_ELAB =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/THIRD_VL_RESULT_LAB_ELAB.sql";

  private static final String THIRD_HIGH_VL = "PATIENTS_WITH_HIGH_VL_AGGREGATE/THIRD_HIGH_VL.sql";
  private static final String THIRD_VL_NEW_TERAPEUTIC_LINE =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/THIRD_VL_NEW_TERAPEUTIC_LINE.sql";

  private static final String THIRD_VL_TERAPEUTIC_LINE =
      "PATIENTS_WITH_HIGH_VL_AGGREGATE/THIRD_VL_TERAPEUTIC_LINE.sql";

  public CohortDefinition getFirstHighVl() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "HIGHVL",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "HIGHVL", EptsQuerysUtils.loadQuery(PATIENTS_WITH_HIGH_VL_AGGREGATE_FIRST_HIGH_VL)),
            mappings));

    composition.setCompositionString("HIGHVL");

    return composition;
  }

  public CohortDefinition getFirstConsultationWithHighVlInformed() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "HIGHVLFC",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "HIGHVLFC", EptsQuerysUtils.loadQuery(PATIENTS_WITH_HIGH_VL_AGGREGATE_FIRST_FC)),
            mappings));

    composition.setCompositionString("HIGHVLFC");

    return composition;
  }

  public CohortDefinition getApss0AfterHighVl() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "APSS0AFTERHIGHVL",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "APSS0AFTERHIGHVL", EptsQuerysUtils.loadQuery(APSS0_AFTER_HIGH_VL)),
            mappings));

    composition.setCompositionString("APSS0AFTERHIGHVL");

    return composition;
  }

  public CohortDefinition getApss1AfterHighVl() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "APSS1AFTERHIGHVL",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "APSS1AFTERHIGHVL", EptsQuerysUtils.loadQuery(APSS1_AFTER_HIGH_VL)),
            mappings));

    composition.setCompositionString("APSS1AFTERHIGHVL");

    return composition;
  }

  public CohortDefinition getApss2AfterHighVl() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "APSS2AFTERHIGHVL",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "APSS2AFTERHIGHVL", EptsQuerysUtils.loadQuery(APSS2_AFTER_HIGH_VL)),
            mappings));

    composition.setCompositionString("APSS2AFTERHIGHVL");

    return composition;
  }

  public CohortDefinition getApss3AfterHighVl() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "APSS3AFTERHIGHVL",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "APSS3AFTERHIGHVL", EptsQuerysUtils.loadQuery(APSS3_AFTER_HIGH_VL)),
            mappings));

    composition.setCompositionString("APSS3AFTERHIGHVL");

    return composition;
  }

  public CohortDefinition getFCWithSecondHighVl() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "FCSECONDHIGHVL",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "FCSECONDHIGHVL", EptsQuerysUtils.loadQuery(FC_SECOND_VL_REQUEST)),
            mappings));

    composition.setCompositionString("FCSECONDHIGHVL");

    return composition;
  }

  public CohortDefinition getSecondRepeatVl() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "SECONDVLREPEAT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "SECONDVLREPEAT", EptsQuerysUtils.loadQuery(REPEAT_SECOND_VL)),
            mappings));

    composition.setCompositionString("SECONDVLREPEAT");

    return composition;
  }

  public CohortDefinition getSecondVlLabElab() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "SECONDVLLABELAB",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "SECONDVLLABELAB", EptsQuerysUtils.loadQuery(SECOND_VL_RESULT_LAB_ELAB)),
            mappings));

    composition.setCompositionString("SECONDVLLABELAB");

    return composition;
  }

  public CohortDefinition getSecondVlResult() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "SECONDVLRESULT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "SECONDVLRESULT", EptsQuerysUtils.loadQuery(SECOND_VL_RESULT)),
            mappings));

    composition.setCompositionString("SECONDVLRESULT");

    return composition;
  }

  public CohortDefinition getNewTerapeuticLine() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "NEWTERAPEUTICLINE",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "NEWTERAPEUTICLINE", EptsQuerysUtils.loadQuery(NEW_TERAPEUTIC_LINE)),
            mappings));

    composition.setCompositionString("NEWTERAPEUTICLINE");

    return composition;
  }

  public CohortDefinition getTerapeuticLine() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "TERAPEUTICLINE",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "TERAPEUTICLINE", EptsQuerysUtils.loadQuery(TERAPEUTIC_LINE)),
            mappings));

    composition.setCompositionString("TERAPEUTICLINE");

    return composition;
  }

  public CohortDefinition getTApss0AfterCV2() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "APSS0AFTERCV2",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "APSS0AFTERCV2", EptsQuerysUtils.loadQuery(APSS0_AFTER_CV2)),
            mappings));

    composition.setCompositionString("APSS0AFTERCV2");

    return composition;
  }

  public CohortDefinition getTApss1AfterCV2() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "APSS1AFTERCV2",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "APSS1AFTERCV2", EptsQuerysUtils.loadQuery(APSS1_AFTER_CV2)),
            mappings));

    composition.setCompositionString("APSS1AFTERCV2");

    return composition;
  }

  public CohortDefinition getTApss2AfterCV2() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "APSS2AFTERCV2",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "APSS2AFTERCV2", EptsQuerysUtils.loadQuery(APSS2_AFTER_CV2)),
            mappings));

    composition.setCompositionString("APSS2AFTERCV2");

    return composition;
  }

  public CohortDefinition getTApss3AfterCV2() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "APSS3AFTERCV2",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "APSS3AFTERCV2", EptsQuerysUtils.loadQuery(APSS3_AFTER_CV2)),
            mappings));

    composition.setCompositionString("APSS3AFTERCV2");

    return composition;
  }

  public CohortDefinition getThirdVlRequest() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "THIRDVLREQUEST",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "THIRDVLREQUEST", EptsQuerysUtils.loadQuery(THIRD_VL_REQUEST)),
            mappings));

    composition.setCompositionString("THIRDVLREQUEST");

    return composition;
  }

  public CohortDefinition getThirdVlRepeat() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "THIRDVLREPEAT",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "THIRDVLREPEAT", EptsQuerysUtils.loadQuery(THIRD_VL_REPEAT)),
            mappings));

    composition.setCompositionString("THIRDVLREPEAT");

    return composition;
  }

  public CohortDefinition getThirdVlLabElab() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "THIRDVLRESULTLABELAB",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "THIRDVLRESULTLABELAB", EptsQuerysUtils.loadQuery(THIRD_VL_RESULT_LAB_ELAB)),
            mappings));

    composition.setCompositionString("THIRDVLRESULTLABELAB");

    return composition;
  }

  public CohortDefinition getThirdHighVl() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "THIRDHIGHVL",
        EptsReportUtils.map(
            this.genericCohorts.generalSql("THIRDHIGHVL", EptsQuerysUtils.loadQuery(THIRD_HIGH_VL)),
            mappings));

    composition.setCompositionString("THIRDHIGHVL");

    return composition;
  }

  public CohortDefinition getThirdHighVlForNewTerapeuticLine() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "THIRDVLNEWTERAPEUTICLINE",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "THIRDVLNEWTERAPEUTICLINE",
                EptsQuerysUtils.loadQuery(THIRD_VL_NEW_TERAPEUTIC_LINE)),
            mappings));

    composition.setCompositionString("THIRDVLNEWTERAPEUTICLINE");

    return composition;
  }

  public CohortDefinition getThirdHighVlForTerapeuticLine() {
    final CompositionCohortDefinition composition = new CompositionCohortDefinition();

    composition.setName("VL");
    composition.addParameter(new Parameter("startDate", "Start Date", Date.class));
    composition.addParameter(new Parameter("endDate", "End Date", Date.class));
    composition.addParameter(new Parameter("location", "location", Location.class));

    final String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

    composition.addSearch(
        "THIRDVLTERAPEUTICLINE",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "THIRDVLTERAPEUTICLINE", EptsQuerysUtils.loadQuery(THIRD_VL_TERAPEUTIC_LINE)),
            mappings));

    composition.setCompositionString("THIRDVLTERAPEUTICLINE");

    return composition;
  }
}
