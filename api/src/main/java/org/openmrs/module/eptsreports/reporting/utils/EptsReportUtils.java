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

package org.openmrs.module.eptsreports.reporting.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.Program;
import org.openmrs.ProgramWorkflowState;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.SerializedObject;
import org.openmrs.api.db.SerializedObjectDAO;
import org.openmrs.module.eptsreports.reporting.reports.manager.EptsPeriodIndicatorReportManager;
import org.openmrs.module.reporting.ReportingException;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.evaluation.parameter.Parameterizable;
import org.openmrs.module.reporting.evaluation.parameter.ParameterizableUtil;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.ReportRequest;
import org.openmrs.module.reporting.report.definition.PeriodIndicatorReportDefinition;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.openmrs.module.reporting.report.manager.ReportManager;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
import org.openmrs.module.reporting.report.service.ReportService;

/** Epts Reports module utilities */
public class EptsReportUtils {

  protected static Log log = LogFactory.getLog(EptsReportUtils.class);

  /**
   * Purges a Report Definition from the database
   *
   * @param reportManager the Report Definition
   */
  public static void purgeReportDefinition(final ReportManager reportManager) {
    final ReportDefinition findDefinition = findReportDefinition(reportManager.getUuid());
    final ReportDefinitionService reportService = Context.getService(ReportDefinitionService.class);
    if (findDefinition != null) {
      reportService.purgeDefinition(findDefinition);

      // Purge Global property used to track version of report definition
      final String gpName = "reporting.reportManager." + reportManager.getUuid() + ".version";
      final GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject(gpName);
      if (gp != null) {
        Context.getAdministrationService().purgeGlobalProperty(gp);
      }
    }
  }

  /**
   * Returns the Report Definition matching the provided uuid.
   *
   * @param uuid Report Uuid
   * @throws RuntimeException a RuntimeException if the Report Definition can't be found
   * @return Report Definition object
   */
  public static ReportDefinition findReportDefinition(final String uuid) {
    final ReportDefinitionService reportService = Context.getService(ReportDefinitionService.class);

    return reportService.getDefinitionByUuid(uuid);
  }

  /**
   * Setup a Report Definition in a database
   *
   * @param reportManager the Report Definition
   */
  public static void setupReportDefinition(final ReportManager reportManager) {

    if (!MISAUIndicatorReports.containsReportUUID(reportManager.getUuid())) {
      ReportManagerUtil.setupReport(reportManager);
    } else {

      EptsPeriodIndicatorReportManager eptsPeriodIndicatorReportManager =
          (EptsPeriodIndicatorReportManager) reportManager;

      String gpName = "reporting.reportManager." + reportManager.getUuid() + ".version";
      GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject(gpName);
      if (gp == null) {
        gp = new GlobalProperty(gpName, "");
      }

      if (reportManager.getVersion().contains("-SNAPSHOT")
          || !gp.getPropertyValue().equals(reportManager.getVersion())) {
        PeriodIndicatorReportDefinition reportDefinition =
            eptsPeriodIndicatorReportManager.constructReportDefinition();
        List<ReportDesign> reportDesigns = reportManager.constructReportDesigns(reportDefinition);
        List<ReportRequest> scheduledRequests =
            reportManager.constructScheduledRequests(reportDefinition);
        log.info(
            "Updating " + reportDefinition.getName() + " to version " + reportManager.getVersion());
        setupReportDefinition(reportDefinition, reportDesigns, scheduledRequests);
        gp.setPropertyValue(reportManager.getVersion());
        Context.getAdministrationService().saveGlobalProperty(gp);
      }
    }
  }

  private static void setupReportDefinition(
      PeriodIndicatorReportDefinition reportDefinition,
      List<ReportDesign> designs,
      List<ReportRequest> scheduledRequests) {
    ReportDefinitionService rds = Context.getService(ReportDefinitionService.class);
    ReportDefinition existing = rds.getDefinitionByUuid(reportDefinition.getUuid());
    if (existing != null) {
      // we need to overwrite the existing, rather than purge-and-recreate, to avoid
      // deleting old ReportRequests
      // log.debug("Overwriting existing ReportDefinition");
      reportDefinition.setId(existing.getId());
      try {
        Context.evictFromSession(existing);
      } catch (IllegalArgumentException ex) {
        // intentionally ignored as per REPORT-802
      }
    } else {
      // incompatible class changes for a serialized object could mean that getting
      // the definition returns null
      // and some serialization error gets logged. In that case we want to overwrite
      // the invalid serialized definition
      SerializedObjectDAO serializedObjectDAO =
          Context.getRegisteredComponents(SerializedObjectDAO.class).get(0);
      SerializedObject invalidSerializedObject =
          serializedObjectDAO.getSerializedObjectByUuid(reportDefinition.getUuid());
      if (invalidSerializedObject != null) {
        reportDefinition.setId(invalidSerializedObject.getId());
        try {
          Context.evictFromSession(invalidSerializedObject);
        } catch (IllegalArgumentException ex) {
          // intentionally ignored as per REPORT-802
        }
      }
    }
    rds.saveDefinition(reportDefinition);

    // purging a ReportDesign doesn't trigger any extra logic, so we can just
    // purge-and-recreate here
    ReportService reportService = Context.getService(ReportService.class);
    List<ReportDesign> existingDesigns =
        reportService.getReportDesigns(reportDefinition, null, true);
    if (existingDesigns.size() > 0) {
      log.debug(
          "Deleting " + existingDesigns.size() + " old designs for " + reportDefinition.getName());
      for (ReportDesign design : existingDesigns) {
        reportService.purgeReportDesign(design);
      }
    }

    if (designs != null) {
      for (ReportDesign design : designs) {
        reportService.saveReportDesign(design);
      }
    }

    // Update scheduled report requests
    if (scheduledRequests != null) {
      for (ReportRequest rrTemplate : scheduledRequests) {
        ReportRequest existingRequest = reportService.getReportRequestByUuid(rrTemplate.getUuid());
        if (existingRequest == null) {
          reportService.queueReport(rrTemplate);
        } else {
          existingRequest.setReportDefinition(rrTemplate.getReportDefinition());
          existingRequest.setPriority(rrTemplate.getPriority());
          existingRequest.setProcessAutomatically(rrTemplate.isProcessAutomatically());
          existingRequest.setRenderingMode(rrTemplate.getRenderingMode());
          existingRequest.setSchedule(rrTemplate.getSchedule());
          existingRequest.setMinimumDaysToPreserve(rrTemplate.getMinimumDaysToPreserve());
          reportService.saveReportRequest(existingRequest);
        }
      }
    }
  }

  /**
   * @param parameterizable
   * @param mappings
   * @param <T>
   * @return
   */
  public static <T extends Parameterizable> Mapped<T> map(
      final T parameterizable, final String mappings) {
    if (parameterizable == null) {
      throw new IllegalArgumentException("Parameterizable cannot be null");
    }
    final String m = mappings != null ? mappings : ""; // probably not
    // necessary,
    // just to be safe
    return new Mapped<T>(parameterizable, ParameterizableUtil.createParameterMappings(m));
  }

  public static String mergeParameterMappings(final String... parameters) {
    if ((parameters == null) || (parameters.length == 0)) {
      throw new ReportingException("parameters are required");
    }
    final LinkedHashSet<String> params = new LinkedHashSet<>();
    for (final String p : parameters) {
      params.addAll(new LinkedHashSet<String>(Arrays.asList(p.split(","))));
    }
    return StringUtils.join(params, ",");
  }

  public static String removeMissingParameterMappingsFromCohortDefintion(
      final CohortDefinition definition, final String mappings) {
    if ((definition == null) || StringUtils.isEmpty(mappings)) {
      return mappings;
    }
    final Iterator<String> mappingsIterator =
        new LinkedHashSet<String>(Arrays.asList(mappings.split(","))).iterator();
    final LinkedHashSet<String> existingMappingsSet = new LinkedHashSet<String>();
    while (mappingsIterator.hasNext()) {
      final String mapping = mappingsIterator.next();
      for (final Parameter p : definition.getParameters()) {
        final String paramMap = "${" + p.getName() + "}";
        if (mapping.trim().endsWith(paramMap)) {
          existingMappingsSet.add(mapping);
        }
      }
    }
    return StringUtils.join(existingMappingsSet, ",");
  }

  /**
   * Get the configurable widget parameter to be passed on the reporting UI TODO: redesign this to
   * be more configurable
   *
   * @return
   */
  public static Parameter getProgramConfigurableParameter(final Program program) {
    final List<ProgramWorkflowState> defaultStates = new ArrayList<>();
    for (final ProgramWorkflowState p : program.getAllWorkflows().iterator().next().getStates()) {
      defaultStates.add(p);
    }

    final Parameter parameter = new Parameter();
    parameter.setName("state");
    parameter.setLabel("States");
    parameter.setType(ProgramWorkflowState.class);
    parameter.setCollectionType(List.class);
    parameter.setWidgetConfiguration(getProgramProperties(program));
    parameter.setDefaultValue(defaultStates);
    return parameter;
  }

  private static Properties getProgramProperties(final Program program) {
    final Properties properties = new Properties();
    properties.put("Program", program.getName());
    return properties;
  }

  public static String formatDateWithTime(final Date date) {

    final Format formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    return formatter.format(date);
  }

  public static String formatDate(final Date date) {

    final Format formatter = new SimpleDateFormat("dd-MM-yyyy");

    return formatter.format(date);
  }

  public static long getDifferenceInDaysBetweenDates(final Date first, final Date last) {
    final long diffInMillies = first.getTime() - last.getTime();
    final long days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

    return days;
  }
}
