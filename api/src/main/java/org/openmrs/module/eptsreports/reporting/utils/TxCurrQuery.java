package org.openmrs.module.eptsreports.reporting.utils;

public class TxCurrQuery {

  private static final String TXCURR = "TX_CURR/PATIENTS_WHO_ARE_CURRENTLY_ENROLLED_ON_ART.sql";

  public static String findPatientsInTxCurr(TxCurrColumnsQuantity columnsQuantity) {

    String query = EptsQuerysUtils.loadQuery(TXCURR);

    switch (columnsQuantity) {
      case PATIENT_ID:
        query = String.format(query, " patient_id ");

        break;

      case ALL:
        query = String.format(query, " * ");

        break;
    }

    return query;
  }
}
