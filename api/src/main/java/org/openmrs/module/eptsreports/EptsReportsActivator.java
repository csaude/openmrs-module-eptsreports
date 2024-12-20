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

package org.openmrs.module.eptsreports;

import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.eptsreports.metadata.ConfigurableMetadataLookupException;
import org.openmrs.module.eptsreports.reporting.EptsReportInitializer;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class EptsReportsActivator extends BaseModuleActivator {

  private Log log = LogFactory.getLog(this.getClass());

  private EptsReportInitializer reportsInitializer = new EptsReportInitializer();

  @Override
  public void contextRefreshed() {
    log.debug("EPTS Reports Module refreshed");
  }

  @Override
  public void willRefreshContext() {
    log.debug("Refreshing EPTS Reports Module");
  }

  @Override
  public void willStart() {
    log.debug("Starting EPTS Reports Module");
  }

  @Override
  public void willStop() {
    log.debug("Stopping EPTS Reports Module");
    try {
      reportsInitializer.purgeReports();
      log.debug("EPTS Reports purged");
      Context.getAdministrationService()
          .purgeGlobalProperty(new GlobalProperty(EptsReportsConfig.SESP_VERSION));
    } catch (Exception e) {
      log.error("An error occured trying to purge EPTS reports", e);
    }
  }

  /** @see #started() */
  public void started() {

    try {
      reportsInitializer.initializeReports();
      log.info("Started EPTS Reports Module");
    } catch (ConfigurableMetadataLookupException e) {
      Context.getAlertService()
          .notifySuperUsers("eptsreports.startuperror.globalproperties", null, e.getMessage());
      throw e;
    } catch (Exception e) {
      Context.getAlertService().notifySuperUsers("eptsreports.startuperror.general", null);
      throw e;
    }
    try {
      log.debug("Deleting old reports...");
      reportsInitializer.purgOldReports();
    } catch (Exception e) {
      throw e;
    }

    log.debug("updating SESP version...");
    Context.getAdministrationService()
        .setGlobalProperty(EptsReportsConfig.SESP_VERSION, getSESPVersion());
    log.debug("Finished Deleting old reports...");
  }

  /** @see #stopped() */
  public void stopped() {
    log.info("Stopped EPTS Reports Module");
  }

  private synchronized String getSESPVersion() {
    String version = null;

    try {
      Properties p = new Properties();
      InputStream is =
          getClass()
              .getResourceAsStream(
                  "/META-INF/maven/org.openmrs.module/eptsreports-omod/pom.properties");

      if (is != null) {
        p.load(is);
        version = p.getProperty("version", "");
      }
    } catch (Exception e) {
    }

    if (version == null) {
      Package aPackage = getClass().getPackage();
      if (aPackage != null) {
        version = aPackage.getImplementationVersion();
        if (version == null) {
          version = aPackage.getSpecificationVersion();
        }
      }
    }

    if (version == null) {
      version = "";
    }

    return version;
  }
}
