package org.openmrs.module.eptsreports.reporting.library.cohorts.mq;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.eptsreports.reporting.calculation.dsd.DSDPatientsWhoExperiencedIITCalculation;
import org.openmrs.module.eptsreports.reporting.cohort.definition.BaseFghCalculationCohortDefinition;
import org.openmrs.module.eptsreports.reporting.library.cohorts.GenericCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.ResumoMensalCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.mi.MICategory15CohortQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.DSDQueriesInterface;
import org.openmrs.module.eptsreports.reporting.library.queries.ResumoMensalQueries;
import org.openmrs.module.eptsreports.reporting.library.queries.mq.MQCategory15QueriesInterface;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.eptsreports.reporting.utils.WomanState;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.definition.library.DocumentedDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQCategory15CohortQueries {

  @Autowired private MQCohortQueries mQCohortQueries;

  @Autowired private ResumoMensalCohortQueries resumoMensalCohortQueries;

  @Autowired private MICategory15CohortQueries mICategory15CohortQueries;

  @Autowired private GenericCohortQueries genericCohorts;

  private static final int CONCEPT_DT_23730 = 23730;

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_1")
  public CohortDefinition getNumeratorCategory15_Indicator_1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_1");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "G2",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(),
            "endDate=${endRevisionDate},location=${location}"));
    definition.addSearch(
        "DENOMINATOR", EptsReportUtils.map(getDenominatorCategory15_Indicator_1(), mappings));

    definition.setCompositionString("G2 AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithDTandGaacWithRequestForVLCategory15H1")
  public CohortDefinition findPatientsWithDTandGaacWithRequestForVLCategory15H1() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWithDTandGaacWithRequestForVLCategory15H1;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithDTWithRequestForVLCategory15_7_FR20")
  public CohortDefinition findPatientsWithDTWithRequestForVLCategory15_7_FR20() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWithDTWithRequestForVLCategory15_7_FR20;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsOnDMCWhoHaveTwoVLRequestAndOneVLResultWhoOnDT")
  public CohortDefinition findPatientsOnDMCWhoHaveTwoVLRequestAndOneVLResultWhoOnDT() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsOnDMCWhoHaveTwoVLRequestAndOneVLResultWhoOnDT");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY
            .findPatientsOnDMCWhoHaveTwoVLRequestAndOneVLResultWhoOnDT;

    definition.setQuery(String.format(query, CONCEPT_DT_23730));

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsOnDMCWhoOnDTAndHaveTwoRequestVLAndVLOnLabForm")
  public CohortDefinition findPatientsOnDMCWhoOnDTAndHaveTwoRequestVLAndVLOnLabForm() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsOnDMCWhoHaveTwoVLRequestAndOneVLResultWhoOnDT");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY
            .findPatientsOnDMCWhoOnDTAndHaveTwoRequestVLAndVLOnLabForm;

    definition.setQuery(String.format(query, CONCEPT_DT_23730));

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsOnDMCWhoOnDTAndHaveTwoRequestVLAndVLOnLabFormOverThan1000")
  public CohortDefinition findPatientsOnDMCWhoOnDTAndHaveTwoRequestVLAndVLOnLabFormOverThan1000() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsOnDMCWhoHaveTwoVLRequestAndOneVLResultWhoOnDT");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY
            .findPatientsOnDMCWhoOnDTAndHaveTwoRequestVLAndVLOnLabFormOverThan1000;

    definition.setQuery(String.format(query, CONCEPT_DT_23730));

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithDTandGaacWithRequestForVLCategory15H2")
  public CohortDefinition findPatientsWithDTandGaacWithRequestForVLCategory15H2() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWithDTandGaacWithRequestForVLCategory15H2;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithRequestOnLabFormCategory15")
  public CohortDefinition findPatientsWithRequestOnLabFormCategory15() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQCategory15QueriesInterface.QUERY.findPatientsWithRequestOnLabFormCategory15;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithRequestOnLabFormCategory15OverThan1000")
  public CohortDefinition findPatientsWithRequestOnLabFormCategory15OverThan1000() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWithRequestOnLabFormCategory15OverThan1000;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithDTWithRequestForVLAndVLResultCategory15")
  public CohortDefinition findPatientsWithDTWithRequestForVLAndVLResultCategory15() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWithDTWithRequestForVLAndVLResultCategory15;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithDTWithCV1000Category15I")
  public CohortDefinition findPatientsWithDTWithCV1000Category15I() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQCategory15QueriesInterface.QUERY.findPatientsWithDTWithCV1000Category15I;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithDTandGaacWithCV1000Category15I")
  public CohortDefinition findPatientsWithDTandGaacWithCV1000Category15I() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWithDTandGaacWithCV1000Category15I;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWithMDSWhoHaveTwoVLRequestAndOneVLResult")
  public CohortDefinition findPatientsWithMDSWhoHaveTwoVLRequestAndOneVLResult() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("patientsPregnantEnrolledOnART");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWithMDSWhoHaveTwoVLRequestAndOneVLResult;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_2")
  public CohortDefinition getNumeratorCategory15_Indicator_2() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_2");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "VL",
        EptsReportUtils.map(this.findPatientsWithMDSWhoHaveTwoVLRequestAndOneVLResult(), mappings));

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(getDenominatorCategory15_Indicator_2_and_3_And_4(), mappings));

    definition.setCompositionString("VL AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_3")
  public CohortDefinition getNumeratorCategory15_Indicator_3() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_3");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "H2", EptsReportUtils.map(this.findPatientsWithRequestOnLabFormCategory15(), mappings));

    definition.addSearch(
        "DENOMINATOR", EptsReportUtils.map(getDenominatorCategory15_Indicator_1_3(), mappings));

    definition.setCompositionString("H2 AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_4")
  public CohortDefinition getNumeratorCategory15_Indicator_4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_4");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "I",
        EptsReportUtils.map(
            this.findPatientsWithRequestOnLabFormCategory15OverThan1000(), mappings));

    definition.addSearch(
        "DENOMINATOR", EptsReportUtils.map(getDenominatorCategory15_Indicator15_4(), mappings));

    definition.setCompositionString("I AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_5")
  public CohortDefinition getNumeratorCategory15_Indicator_5() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_5");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "G2",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "DENOMINATOR", EptsReportUtils.map(getDenominatorCategory15_Indicator_5(), mappings));

    definition.setCompositionString("G2 AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_6")
  public CohortDefinition getNumeratorCategory15_Indicator_6() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_6");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "G2",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "DENOMINATOR", EptsReportUtils.map(getDenominatorCategory15_Indicator_6(), mappings));

    definition.setCompositionString("G2 AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_7")
  public CohortDefinition getNumeratorCategory15_Indicator_7() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_7");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "H1",
        EptsReportUtils.map(
            this.findPatientsOnDMCWhoHaveTwoVLRequestAndOneVLResultWhoOnDT(), mappings));

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(getDenominatorCategory15_Indicator_7_And_9_And_11(), mappings));

    definition.setCompositionString("H1 AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_8")
  public CohortDefinition getNumeratorCategory15_Indicator_8() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_8");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "H1",
        EptsReportUtils.map(
            this.findPatientsOnDMCWhoHaveTwoVLRequestAndOneVLResultWhoOnDT(), mappings));

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(getDenominatorCategory15_Indicator_8_And_10_And_12(), mappings));

    definition.setCompositionString("H1 AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_9")
  public CohortDefinition getNumeratorCategory15_Indicator_9() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_9");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "H2",
        EptsReportUtils.map(
            this.findPatientsOnDMCWhoOnDTAndHaveTwoRequestVLAndVLOnLabForm(), mappings));

    definition.addSearch(
        "DENOMINATOR", EptsReportUtils.map(getDenominatorCategory15_Indicator_15_9(), mappings));

    definition.setCompositionString("H2 AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_10")
  public CohortDefinition getNumeratorCategory15_Indicator_10() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_10");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "H2",
        EptsReportUtils.map(
            this.findPatientsOnDMCWhoOnDTAndHaveTwoRequestVLAndVLOnLabForm(), mappings));

    definition.addSearch(
        "DENOMINATOR", EptsReportUtils.map(getDenominatorCategory15_Indicator_15_10(), mappings));

    definition.setCompositionString("H2 AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_11")
  public CohortDefinition getNumeratorCategory15_Indicator_11() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_11");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "I",
        EptsReportUtils.map(
            this.findPatientsOnDMCWhoOnDTAndHaveTwoRequestVLAndVLOnLabFormOverThan1000(),
            mappings));

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(getDenominatorCategory15_Indicator_7_And_9_And_11(), mappings));

    definition.setCompositionString("I AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "NumeratorCategory15_Indicator_12")
  public CohortDefinition getNumeratorCategory15_Indicator_12() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - NumeratorCategory15_Indicator_12");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "I",
        EptsReportUtils.map(
            this.findPatientsOnDMCWhoOnDTAndHaveTwoRequestVLAndVLOnLabFormOverThan1000(),
            mappings));

    definition.addSearch(
        "DENOMINATOR",
        EptsReportUtils.map(getDenominatorCategory15_Indicator_8_And_10_And_12(), mappings));

    definition.setCompositionString("I AND DENOMINATOR");

    return definition;
  }

  @DocumentedDefinition(value = "DenominatorCategory15_Indicator_1")
  public CohortDefinition getDenominatorCategory15_Indicator_1() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - DenominatorCategory15_Indicator_1");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingEndRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15ARF5(),
            mappingEndRevisionDate));

    definition.addSearch(
        "C", EptsReportUtils.map(findPatientsWhoArePregnantSpecificForCategory15(), mappings));

    definition.addSearch(
        "D", EptsReportUtils.map(findPatientsWhoAreBreastfeedingSpecificForCategory15(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.addSearch(
        "E", EptsReportUtils.map(mQCohortQueries.getPatientsWhoDiedEndRevisioDate(), mappings));

    definition.setCompositionString("A NOT (C OR D OR F OR E)");

    return definition;
  }

  @DocumentedDefinition(value = "DenominatorCategory15_Indicator_2_and_3_And_4")
  public CohortDefinition getDenominatorCategory15_Indicator_2_and_3_And_4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - DenominatorCategory15_Indicator_2_and_3_And_4");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingEndRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15ARF5(),
            mappingEndRevisionDate));

    definition.addSearch(
        "G2",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "C", EptsReportUtils.map(findPatientsWhoArePregnantSpecificForCategory15(), mappings));

    definition.addSearch(
        "D", EptsReportUtils.map(findPatientsWhoAreBreastfeedingSpecificForCategory15(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.addSearch(
        "CV", EptsReportUtils.map(this.findPatientsWhoHaveCVOverThan10000Category15(), mappings));

    definition.setCompositionString("(A AND G2) NOT (C OR D OR F OR CV)");

    return definition;
  }

  @DocumentedDefinition(value = "getDenominatorCategory15_Indicator15_4")
  public CohortDefinition getDenominatorCategory15_Indicator15_4() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - DenominatorCategory15_Indicator_2_and_3_And_4");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingEndRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15ARF5(),
            mappingEndRevisionDate));

    definition.addSearch(
        "G2",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "CVP",
        EptsReportUtils.map(
            findPatientsWithMDSWhoHaveTwoVLRequestAndOneVLResult(), mappingEndRevisionDate));

    definition.addSearch(
        "CVR",
        EptsReportUtils.map(findPatientsWithRequestOnLabFormCategory15(), mappingEndRevisionDate));

    definition.addSearch(
        "C", EptsReportUtils.map(findPatientsWhoArePregnantSpecificForCategory15(), mappings));

    definition.addSearch(
        "D", EptsReportUtils.map(findPatientsWhoAreBreastfeedingSpecificForCategory15(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.addSearch(
        "CV", EptsReportUtils.map(this.findPatientsWhoHaveCVOverThan10000Category15(), mappings));

    definition.setCompositionString("(A AND G2 AND CVP AND CVR) NOT (C OR D OR F OR CV)");

    return definition;
  }

  @DocumentedDefinition(value = "getDenominatorCategory15_Indicator_1_3")
  public CohortDefinition getDenominatorCategory15_Indicator_1_3() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - DenominatorCategory15_Indicator_2_and_3_And_4");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingEndRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15ARF5(),
            mappingEndRevisionDate));

    definition.addSearch(
        "G2",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "CVR",
        EptsReportUtils.map(this.findPatientsWithMDSWhoHaveTwoVLRequestAndOneVLResult(), mappings));

    definition.addSearch(
        "C", EptsReportUtils.map(findPatientsWhoArePregnantSpecificForCategory15(), mappings));

    definition.addSearch(
        "D", EptsReportUtils.map(findPatientsWhoAreBreastfeedingSpecificForCategory15(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.addSearch(
        "CV", EptsReportUtils.map(this.findPatientsWhoHaveCVOverThan10000Category15(), mappings));

    definition.setCompositionString("(A AND G2 AND CVR) NOT (C OR D OR F OR CV)");

    return definition;
  }

  @DocumentedDefinition(value = "DenominatorCategory15_Indicator_5")
  public CohortDefinition getDenominatorCategory15_Indicator_5() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - DenominatorCategory15_Indicator_5");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingEndRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15ARF6(),
            mappingEndRevisionDate));

    definition.addSearch(
        "C", EptsReportUtils.map(findPatientsWhoArePregnantSpecificForCategory15(), mappings));

    definition.addSearch(
        "D", EptsReportUtils.map(findPatientsWhoAreBreastfeedingSpecificForCategory15(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.addSearch(
        "E", EptsReportUtils.map(mQCohortQueries.getPatientsWhoDiedEndRevisioDate(), mappings));

    definition.setCompositionString("A NOT (C OR D OR F OR E)");

    return definition;
  }

  @DocumentedDefinition(value = "DenominatorCategory15_Indicator_7_And_9_And_11")
  public CohortDefinition getDenominatorCategory15_Indicator_7_And_9_And_11() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - DenominatorCategory15_Indicator_7_And_9_And_11");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingEndRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15ARF6(),
            mappingEndRevisionDate));

    definition.addSearch(
        "G2",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "C", EptsReportUtils.map(findPatientsWhoArePregnantSpecificForCategory15(), mappings));

    definition.addSearch(
        "D", EptsReportUtils.map(findPatientsWhoAreBreastfeedingSpecificForCategory15(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.addSearch(
        "CV", EptsReportUtils.map(this.findPatientsWhoHaveCVOverThan10000Category15(), mappings));

    definition.setCompositionString("(A AND G2) NOT (C OR D OR F OR CV)");

    return definition;
  }

  @DocumentedDefinition(value = "getDenominatorCategory15_Indicator_15_11")
  public CohortDefinition getDenominatorCategory15_Indicator_15_11() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - DenominatorCategory15_Indicator_7_And_9_And_11");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingEndRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15ARF6(),
            mappingEndRevisionDate));

    definition.addSearch(
        "CVP",
        EptsReportUtils.map(
            findPatientsOnDMCWhoHaveTwoVLRequestAndOneVLResultWhoOnDT(), mappingEndRevisionDate));

    definition.addSearch(
        "CVR",
        EptsReportUtils.map(
            findPatientsOnDMCWhoOnDTAndHaveTwoRequestVLAndVLOnLabForm(), mappingEndRevisionDate));

    definition.addSearch(
        "G2",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "C", EptsReportUtils.map(findPatientsWhoArePregnantSpecificForCategory15(), mappings));

    definition.addSearch(
        "D", EptsReportUtils.map(findPatientsWhoAreBreastfeedingSpecificForCategory15(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.addSearch(
        "CV", EptsReportUtils.map(this.findPatientsWhoHaveCVOverThan10000Category15(), mappings));

    definition.setCompositionString("(A AND G2 AND CVP OR CVR) NOT (C OR D OR F OR CV)");

    return definition;
  }

  @DocumentedDefinition(value = "getDenominatorCategory15_Indicator_15")
  public CohortDefinition getDenominatorCategory15_Indicator_15_9() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - DenominatorCategory15_Indicator_7_And_9_And_11");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingEndRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15ARF6(),
            mappingEndRevisionDate));

    definition.addSearch(
        "CVR",
        EptsReportUtils.map(
            findPatientsOnDMCWhoHaveTwoVLRequestAndOneVLResultWhoOnDT(), mappingEndRevisionDate));

    definition.addSearch(
        "G2",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "C", EptsReportUtils.map(findPatientsWhoArePregnantSpecificForCategory15(), mappings));

    definition.addSearch(
        "D", EptsReportUtils.map(findPatientsWhoAreBreastfeedingSpecificForCategory15(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.addSearch(
        "CV", EptsReportUtils.map(this.findPatientsWhoHaveCVOverThan10000Category15(), mappings));

    definition.setCompositionString("(A AND G2 AND CVR) NOT (C OR D OR F OR CV)");

    return definition;
  }

  @DocumentedDefinition(value = "DenominatorCategory15_Indicator_6")
  public CohortDefinition getDenominatorCategory15_Indicator_6() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - DenominatorCategory15_Indicator_6");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingEndRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15ARF6(),
            mappingEndRevisionDate));

    definition.addSearch(
        "C", EptsReportUtils.map(findPatientsWhoArePregnantSpecificForCategory15(), mappings));

    definition.addSearch(
        "D", EptsReportUtils.map(findPatientsWhoAreBreastfeedingSpecificForCategory15(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.addSearch(
        "E", EptsReportUtils.map(mQCohortQueries.getPatientsWhoDiedEndRevisioDate(), mappings));

    definition.setCompositionString("A NOT (C OR D OR F OR E)");

    return definition;
  }

  @DocumentedDefinition(value = "DenominatorCategory15_Indicator_8_And_10_And_12")
  public CohortDefinition getDenominatorCategory15_Indicator_8_And_10_And_12() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - DenominatorCategory15_Indicator_8_And_10_And_12");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingEndRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15ARF6(),
            mappingEndRevisionDate));

    definition.addSearch(
        "G2",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "C", EptsReportUtils.map(findPatientsWhoArePregnantSpecificForCategory15(), mappings));

    definition.addSearch(
        "D", EptsReportUtils.map(findPatientsWhoAreBreastfeedingSpecificForCategory15(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.addSearch(
        "CV", EptsReportUtils.map(this.findPatientsWhoHaveCVOverThan10000Category15(), mappings));

    definition.setCompositionString("(A AND G2) NOT (C OR D OR F OR CV)");

    return definition;
  }

  @DocumentedDefinition(value = "getDenominatorCategory15_Indicator_15_12")
  public CohortDefinition getDenominatorCategory15_Indicator_15_12() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - DenominatorCategory15_Indicator_8_And_10_And_12");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingEndRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15ARF6(),
            mappingEndRevisionDate));

    definition.addSearch(
        "G2",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "CVP",
        EptsReportUtils.map(
            findPatientsOnDMCWhoHaveTwoVLRequestAndOneVLResultWhoOnDT(), mappingEndRevisionDate));

    definition.addSearch(
        "CVR",
        EptsReportUtils.map(
            findPatientsOnDMCWhoOnDTAndHaveTwoRequestVLAndVLOnLabForm(), mappingEndRevisionDate));

    definition.addSearch(
        "C", EptsReportUtils.map(findPatientsWhoArePregnantSpecificForCategory15(), mappings));

    definition.addSearch(
        "D", EptsReportUtils.map(findPatientsWhoAreBreastfeedingSpecificForCategory15(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.addSearch(
        "CV", EptsReportUtils.map(this.findPatientsWhoHaveCVOverThan10000Category15(), mappings));

    definition.setCompositionString("(A AND G2 AND CVP OR CVR) NOT (C OR D OR F OR CV)");

    return definition;
  }

  @DocumentedDefinition(value = "getDenominatorCategory15_Indicator_15_10")
  public CohortDefinition getDenominatorCategory15_Indicator_15_10() {

    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("MQ - DenominatorCategory15_Indicator_8_And_10_And_12");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    final String mappings =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingEndRevisionDate = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15ARF6(),
            mappingEndRevisionDate));

    definition.addSearch(
        "CVP",
        EptsReportUtils.map(
            findPatientsOnDMCWhoHaveTwoVLRequestAndOneVLResultWhoOnDT(), mappingEndRevisionDate));

    definition.addSearch(
        "G2",
        EptsReportUtils.map(
            resumoMensalCohortQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "C", EptsReportUtils.map(findPatientsWhoArePregnantSpecificForCategory15(), mappings));

    definition.addSearch(
        "D", EptsReportUtils.map(findPatientsWhoAreBreastfeedingSpecificForCategory15(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(mQCohortQueries.findPatientsWhoTransferedOutRF07(), mappings));

    definition.addSearch(
        "CV", EptsReportUtils.map(this.findPatientsWhoHaveCVOverThan10000Category15(), mappings));

    definition.setCompositionString("(A AND G2 AND CVP) NOT (C OR D OR F OR CV)");

    return definition;
  }

  @DocumentedDefinition(value = "PatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15A")
  private CohortDefinition findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15A() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("MQ - PatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15A");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    String query =
        MQCategory15QueriesInterface.QUERY
            .findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15A;
    definition.setQuery(String.format(query));
    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15ARF6")
  private CohortDefinition
      findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15ARF6() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("MQ - PatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15A");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    String query =
        MQCategory15QueriesInterface.QUERY
            .findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15ARF6;
    definition.setQuery(String.format(query, CONCEPT_DT_23730));
    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15ARF5")
  private CohortDefinition
      findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15ARF5() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("MQ - PatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15A");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    String query =
        MQCategory15QueriesInterface.QUERY
            .findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15ARF5;
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoHaveCVOverThan10000Category15")
  private CohortDefinition findPatientsWhoHaveCVOverThan10000Category15() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("MQ - CV Over Than 1000");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    String query = MQCategory15QueriesInterface.QUERY.findPatientsWhoHaveCVOverThan10000Category15;
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(
      value =
          "PatientsWithLastGaacOrDispensaTrimestralInClinicaForGivenConceptsDenominadorCategoria15B1")
  private CohortDefinition
      findPatientsWithLastGaacOrDispensaTrimestralInClinicaForGivenConceptsDenominadorCategoria15B1() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MQ - PatientsWithLastGaacOrDispensaTrimestralInClinicaForGivenConceptsDenominadorCategoria15B1");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));
    String query =
        MQCategory15QueriesInterface.QUERY
            .findPatientsWithLastGaacOrLastDispensaTrimestralRegisteredInFichaClinicaWithinRevisionPeriodB1;
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoArePregnantSpecificForCategory15")
  private CohortDefinition findPatientsWhoArePregnantSpecificForCategory15() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoArePregnantSpecificForCategory15");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWhoArePregnantSpecificForCategory15;
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoArePregnantSpecificForCategory15")
  private CohortDefinition getPatientsWhoDiedCategory15RF17() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoArePregnantSpecificForCategory15");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWhoArePregnantSpecificForCategory15;
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoArePregnant9MonthsSpecificForCategory15")
  private CohortDefinition findPatientsWhoArePregnant14MonthsSpecificForCategory15() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoArePregnantSpecificForCategory15");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientWhoArePregnantOrBreastfeeding14Months(
            WomanState.PREGNANT);
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreBreastfeeding18MonthsSpecificForCategory15")
  private CohortDefinition findPatientsWhoAreBreastfeeding14MonthsSpecificForCategory15() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoArePregnantSpecificForCategory15");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientWhoArePregnantOrBreastfeeding14Months(
            WomanState.BREASTFEEDING);
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreBreastfeedingSpecificForCategory15")
  private CohortDefinition findPatientsWhoAreBreastfeedingSpecificForCategory15() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("findPatientsWhoAreBreastfeedingSpecificForCategory15");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWhoAreBreastfeedingSpecificForCategory15;
    definition.setQuery(query);
    return definition;
  }

  @DocumentedDefinition(value = "PatientsWhoAreNewlyEnrolledOnARTByAgeUsingAgeRange")
  private CohortDefinition findPatientsWhoAreNewlyEnrolledOnARTByAgeUsingAgeRange(
      int startAge, int endAge) {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MQ - PatientsWhoAreNewlyEnrolledOnARTByAgeUsingAgeRange "
            + String.valueOf(startAge)
            + " to "
            + String.valueOf(endAge));
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWhoAreNewlyEnrolledOnARTByAgeBetweenAgeRange(
            startAge, endAge);

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoHasRegisteredAsIniciarInAtLeastOneMDS")
  private CohortDefinition findPatientsWhoHasRegisteredAsIniciarInAtLeastOneMDS() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(String.format("MQ - findPatientsWhoHasRegisteredAsIniciarInAtLeastOneMDS "));
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWhoHasRegisteredAsIniciarInAtLeastOneMDS;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoHasRegisteredAsFimInAtLeastOneMDS")
  private CohortDefinition findPatientsWhoHasRegisteredAsFimInAtLeastOneMDS() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(String.format("MQ - findPatientsWhoHasRegisteredAsIniciarInAtLeastOneMDS "));
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY.findPatientsWhoHasRegisteredAsFimInAtLeastOneMDS;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoHaveCD4OverThan1000")
  private CohortDefinition findPatientsWhoHaveCD4OverThan1000() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(String.format("MQ - findPatientsWhoHasRegisteredAsIniciarInAtLeastOneMDS "));
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQCategory15QueriesInterface.QUERY.findPatientsWhoHaveCD4OverThan1000;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoHaveCVBiggerThan1000")
  private CohortDefinition findPatientsWhoHaveCVBiggerThan1000() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(String.format("MQ - findPatientsWhoHasRegisteredAsIniciarInAtLeastOneMDS "));
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query = MQCategory15QueriesInterface.QUERY.findPatientsWhoHaveCVBiggerThan1000;

    definition.setQuery(query);

    return definition;
  }

  private CohortDefinition findPatientsWhoHaveLastConsultationOnFichaClinicaAndWhoOnMDCARF44() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        String.format("MQ - findPatientsWhoHaveLastConsultationOnFichaClinicaAndWhoOnMDCARF44 "));
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY
            .findPatientsWhoHaveLastConsultationOnFichaClinicaAndWhoOnMDCARF44;

    definition.setQuery(query);

    return definition;
  }

  private CohortDefinition findPatientsWhoHaveLastConsultationOnFichaClinicaAndWhoOnMDCRF36() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        String.format("MQ - findPatientsWhoHaveLastConsultationOnFichaClinicaAndWhoOnMDCARF44 "));
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY
            .findPatientsWhoHaveLastConsultationOnFichaClinicaAndWhoOnMDCRF36;

    definition.setQuery(query);

    return definition;
  }

  private CohortDefinition findPatientsWhoOnARTAndHaveCVBiggerThan1000AndWhoSuspendTratment() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        String.format("MQ - findPatientsWhoOnARTAndHaveCVBiggerThan1000AndWhoSuspendTratment "));
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY
            .findPatientsWhoOnARTAndHaveCVBiggerThan1000AndWhoSuspendTratment;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsRegisteredInOneMDSForStablePatientsAndHadCVBetween18And24nMonthsAfterCVLessThan1000")
  public CohortDefinition
      findPatientsRegisteredInOneMDSForStablePatientsAndHadCVBetween18And24nMonthsAfterCVLessThan1000() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MI Category 15 - Get patients registered in one MDS stable patients and had CV between 11 and 24 after CV<1000");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY
            .findPatientsRegisteredInOneMDSForStablePatientsAndHadCVBetween18And24nMonthsAfterCVLessThan1000;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsElegibleForMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriod_Denominator_15_13")
  public CohortDefinition
      findPatientsElegibleForMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriod_Denominator_15_13() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "MQ-MDS-eligible patients for stable patients who had a consultation during the evaluation period - Denominator");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-12m+1d},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            mICategory15CohortQueries.findPatientsWithClinicalConsultationDuringRevisionPeriod(),
            mappings));

    definition.addSearch(
        "B1",
        EptsReportUtils.map(
            this.findPatientsWithClinicalConsultationAndARTStartDateGreaterThanThreeMonths(),
            mappings));

    definition.addSearch(
        "C",
        EptsReportUtils.map(
            this.findPatientsWhoArePregnant14MonthsSpecificForCategory15(), mappings));

    definition.addSearch(
        "D",
        EptsReportUtils.map(
            this.findPatientsWhoAreBreastfeeding14MonthsSpecificForCategory15(), mappings));

    definition.addSearch(
        "F", EptsReportUtils.map(this.findPatientsWhoHaveCD4OverThan1000(), mappings));

    definition.addSearch(
        "G", EptsReportUtils.map(this.findPatientsWhoHaveCVBiggerThan1000(), mappings));

    definition.addSearch(
        "J",
        EptsReportUtils.map(
            this.findPatientsWhoHaveLastConsultationOnFichaClinicaAndWhoOnMDCARF44(), mappings));

    definition.addSearch(
        "TB",
        EptsReportUtils.map(
            this.findPatientsWhoAreInTbTreatmentFor7MonthsPriorEndRevisionPeriod(), mappings));

    definition.addSearch(
        "ADVERSASE-REACTIONS",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "ADVERSASE-REACTIONS",
                DSDQueriesInterface.QUERY
                    .findPatientsWithAdverseDrugReactionsRequiringRegularMonitoringNotifiedInLast6Months),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "SARCOMA-KAPOSI",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "SARCOMA-KAPOSI",
                DSDQueriesInterface.QUERY.findPatientsWhoHaveBeenNotifiedOfKaposiSarcoma),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "IIT",
        EptsReportUtils.map(
            this.findPatientsWhoReinitiatedTreatmentInTheLastThreeMonths(), mappings));

    definition.setCompositionString(
        "(A AND B1) NOT (C OR D OR F OR G OR J OR TB OR ADVERSASE-REACTIONS OR SARCOMA-KAPOSI OR IIT)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsElegibleForMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodAndRegisteredAtLeastInOneMDS_Numerator_15_13")
  public CohortDefinition
      findPatientsElegibleForMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodAndRegisteredAtLeastInOneMDS_Numerator_15_13() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "MQ-of patients eligible for MDS for stable patients who had a visit during the "
            + "evaluation period and who were enrolled in at least one MDS for a stable patient "
            + "(GAAC, DT, DS, FR) at the same visit - Numerator");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-12m+1d},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsDen =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-15-13",
        EptsReportUtils.map(
            this
                .findPatientsElegibleForMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriod_Denominator_15_13(),
            mappingsDen));

    definition.addSearch(
        "K",
        EptsReportUtils.map(
            this.findPatientsWhoHaveLastConsultationOnFichaClinicaAndWhoOnMDCRF36(), mappings));

    definition.setCompositionString("(DENOMINATOR-15-13 AND K)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodWhoReceivedCargaViralGreaterThan1000_Denominator_15_14")
  public CohortDefinition
      findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodWhoReceivedCargaViralGreaterThan1000_Denominator_15_14() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "MQ-patients on ART enrolled in MDS for stable patients, seen in the review period, "
            + "who have received a CV result greater than or equal to 1000 copies - Denominator");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-12m+1d},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    final String mappingsMQ = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            mICategory15CohortQueries.findPatientsWithClinicalConsultationDuringRevisionPeriod(),
            mappings));

    definition.addSearch(
        "J",
        EptsReportUtils.map(
            this.findPatientsWhoHaveLastConsultationOnFichaClinicaAndWhoOnMDCARF44(), mappingsMQ));

    definition.addSearch(
        "H",
        EptsReportUtils.map(
            mICategory15CohortQueries
                .findPatientsWithTheLastCargaViralGreaterOrEqualThan1000RegisteredInTheLastClinicalConsultation(),
            mappings));

    definition.setCompositionString("(A AND J AND H)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodWhoReceivedCargaViralGreaterThan1000AndSuspendedInTheMDS_Numerator_15_14")
  public CohortDefinition
      findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodWhoReceivedCargaViralGreaterThan1000AndSuspendedInTheMDS_Numerator_15_14() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "Patients on ART enrolled in MDS for stable patients, seen in the review period, "
            + "who received a CV result greater than or equal to 1000 copies, "
            + "and who were suspended from the MDS at that same appointment - Numerator");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-12m+1d},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    final String mappingsDen =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-15-14",
        EptsReportUtils.map(
            this
                .findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodWhoReceivedCargaViralGreaterThan1000_Denominator_15_14(),
            mappingsDen));

    definition.addSearch(
        "L",
        EptsReportUtils.map(
            this.findPatientsWhoOnARTAndHaveCVBiggerThan1000AndWhoSuspendTratment(), mappings));

    definition.setCompositionString("(DENOMINATOR-15-14 AND L)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodInARTMoreThan21Months_Denominator_15_15")
  public CohortDefinition
      findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodInARTMoreThan21Months_Denominator_15_15() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "patients enrolled in MDS for stable patients who had a consultation in the evaluation period and in ART for more than 24 months - Denominator ");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-12m+1d},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";

    final String mappingsMQ = "endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "A",
        EptsReportUtils.map(
            mICategory15CohortQueries.findPatientsWithClinicalConsultationDuringRevisionPeriod(),
            mappings));

    definition.addSearch(
        "J",
        EptsReportUtils.map(
            this.findPatientsWhoHaveLastConsultationOnFichaClinicaAndWhoOnMDCARF44(), mappingsMQ));

    definition.addSearch(
        "B2",
        EptsReportUtils.map(
            mICategory15CohortQueries
                .findPatientsWithClinicalConsultationAndARTStartDateGreaterThanTwentyFourMonths(),
            mappings));

    definition.setCompositionString("(A AND J AND B2)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findPatientsRegisteredInOneMDSForStablePatientsAndHadCVBetweenTwelveAndEigtheenMonthsAfterCVLessThan1000_Numerator_15_15")
  public CohortDefinition
      findPatientsRegisteredInOneMDSForStablePatientsAndHadCVBetweenTwelveAndEigtheenMonthsAfterCVLessThan1000_Numerator_15_15() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName(
        "of patients enrolled in MDS to stable patients who had a consultation in the evaluation period (review period)"
            + " and who have been on ART for more than 24 months and who have a VC result 18 to 24 months "
            + " after the last VC result < 1000 copies - Numerator ");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    final String mappings =
        "startInclusionDate=${endRevisionDate-12m+1d},endInclusionDate=${endRevisionDate},endRevisionDate=${endRevisionDate},location=${location}";
    final String mappingsDen =
        "startInclusionDate=${startInclusionDate},endInclusionDate=${endInclusionDate},endRevisionDate=${endRevisionDate},location=${location}";

    definition.addSearch(
        "DENOMINATOR-15-15",
        EptsReportUtils.map(
            this
                .findPatientsRegisteredInMDSForStablePatientsWithClinicalConsultationInTheRevisionPeriodInARTMoreThan21Months_Denominator_15_15(),
            mappingsDen));

    definition.addSearch(
        "I",
        EptsReportUtils.map(
            this
                .findPatientsRegisteredInOneMDSForStablePatientsAndHadCVBetween18And24nMonthsAfterCVLessThan1000(),
            mappings));

    definition.setCompositionString("(DENOMINATOR-15-15 AND I)");

    return definition;
  }

  @DocumentedDefinition(
      value =
          "findAllPatientsWhoHaveLaboratoryInvestigationsRequestsAndViralChargeInLastConsultationDuringLastThreeMonths")
  public CohortDefinition
      findAllPatientsWhoHaveLaboratoryInvestigationsRequestsAndViralChargeInLastConsultationDuringLastThreeMonths() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MQ Category 15 - Get Patients with Pedido de Investigacoes Laboratoriais in the Last 3 Months of Last Consultation");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY
            .findAllPatientsWhoHaveLaboratoryInvestigationsRequestsAndViralChargeInLastConsultationDuringLastThreeMonths;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(
      value = "findPatientsWithClinicalConsultationAndARTStartDateGreaterThanThreeMonths")
  public CohortDefinition
      findPatientsWithClinicalConsultationAndARTStartDateGreaterThanThreeMonths() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName("MQ Category 15 - Get Patients in ART in 3 Months");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY
            .findPatientsWithClinicalConsultationAndARTStartDateGreaterThanThreeMonths;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoAreInTbTreatmentFor7MonthsPriorEndRevisionPeriod")
  public CohortDefinition findPatientsWhoAreInTbTreatmentFor7MonthsPriorEndRevisionPeriod() {

    final SqlCohortDefinition definition = new SqlCohortDefinition();

    definition.setName(
        "MQ Category 15 - Get Patients in TB Treatment 7 Months Prior the End Revision Period");
    definition.addParameter(new Parameter("startInclusionDate", "Start Date", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "End Date", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "End Revision Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));

    String query =
        MQCategory15QueriesInterface.QUERY
            .findPatientsWhoAreInTbTreatmentFor7MonthsPriorEndRevisionPeriod;

    definition.setQuery(query);

    return definition;
  }

  @DocumentedDefinition(value = "findPatientsWhoReinitiatedTreatmentInTheLastThreeMonths")
  public CohortDefinition findPatientsWhoReinitiatedTreatmentInTheLastThreeMonths() {
    final CompositionCohortDefinition definition = new CompositionCohortDefinition();

    definition.setName("Patients who reinitiated the tratment in the last 3 months");
    definition.addParameter(
        new Parameter("startInclusionDate", "Data Inicio Inclusão", Date.class));
    definition.addParameter(new Parameter("endInclusionDate", "Data Fim Inclusão", Date.class));
    definition.addParameter(new Parameter("endRevisionDate", "Data Fim Revisão", Date.class));
    definition.addParameter(new Parameter("location", "location", Date.class));

    definition.addSearch(
        "B13",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "Finding DSD- Denominator 3 by Reporting Period",
                ResumoMensalQueries.findPatientsWhoAreCurrentlyEnrolledOnArtMOHB13()),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "IIT-PREVIOUS-PERIOD",
        EptsReportUtils.map(
            this.getPatientsWhoExperiencedIITCalculation(),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "IIT-PREVIOUS-PERIOD-2",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "IIT-PREVIOUS-PERIOD-2", DSDQueriesInterface.QUERY.findPatientsAreDefaultIIT),
            "endDate=${endRevisionDate},location=${location}"));

    definition.addSearch(
        "TRF-IN",
        EptsReportUtils.map(
            this.genericCohorts.generalSql(
                "patientsWhoAreInTbTreatment",
                DSDQueriesInterface.QUERY.findPatientsWhoWhereTransferredInPriorReportingPeriod),
            "startDate=${endRevisionDate-3m},endDate=${endRevisionDate},location=${location}"));

    definition.setCompositionString(
        "(B13 AND (IIT-PREVIOUS-PERIOD OR IIT-PREVIOUS-PERIOD-2)) NOT TRF-IN");

    return definition;
  }

  @DocumentedDefinition(value = "DSDPatientsWhoExperiencedIITCalculation")
  private CohortDefinition getPatientsWhoExperiencedIITCalculation() {
    BaseFghCalculationCohortDefinition definition =
        new BaseFghCalculationCohortDefinition(
            "DSDPatientsWhoExperiencedIITCalculation",
            Context.getRegisteredComponents(DSDPatientsWhoExperiencedIITCalculation.class).get(0));
    definition.addParameter(new Parameter("endDate", "End Date", Date.class));
    definition.addParameter(new Parameter("location", "Location", Location.class));
    return definition;
  }
}
