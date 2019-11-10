package org.openmrs.module.eptsreports.reporting.utils;

public enum Erin2MonthsRenge {
  IART("Initiated ART"),
  DNPUD("Alive & Not Transferred Out and Did not have consultation or pick-up drugs 2x"),
  PUD("Alive & Not Transferred Out and Had Consultation or Picked-up Drugs 2x"),
  DP("Dead"),
  TOP("Transferred Out"),
  STP("Stopped Treatment");
  private final String name;

  private Erin2MonthsRenge(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
