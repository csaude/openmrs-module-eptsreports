package org.openmrs.module.eptsreports.reporting.utils;

import org.openmrs.module.eptsreports.reporting.reports.SetupIntensiveMonitoringReport;
import org.openmrs.module.eptsreports.reporting.reports.SetupMisauResumoMensalPrepReport;
import org.openmrs.module.eptsreports.reporting.reports.SetupQualityImprovementReport;
import org.openmrs.module.eptsreports.reporting.reports.SetupResumoMensalReport;
import org.openmrs.module.eptsreports.reporting.reports.SetupResumoTrimestralApssReport;

public enum MISAUIndicatorReports {
  RESUMO_MENSAL_FICHA_MESTRA(new SetupResumoMensalReport(null).getUuid()),

  RESUMO_TRUMESTRAL_APSS(new SetupResumoTrimestralApssReport(null, null).getUuid()),

  RESUMO_MENSAL_PREP(new SetupMisauResumoMensalPrepReport().getUuid()),

  MQ(new SetupQualityImprovementReport().getUuid()),

  MI(new SetupIntensiveMonitoringReport().getUuid());

  private String reportUUID;

  private MISAUIndicatorReports(String reportUUID) {
    this.reportUUID = reportUUID;
  }

  public static Boolean containsReportUUID(String reportUUID) {
    for (MISAUIndicatorReports e : values()) {
      if (e.reportUUID.equals(reportUUID)) {
        return true;
      }
    }
    return false;
  }
}
