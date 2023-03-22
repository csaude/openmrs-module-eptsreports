package org.openmrs.module.eptsreports.reporting.calculation.txml;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.eptsreports.reporting.calculation.util.processor.TxmlLostFollowUpProcessor;
import org.openmrs.module.eptsreports.reporting.utils.EptsDateUtil;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.springframework.stereotype.Component;

@Component
public class TxMLPatientsWhoAreIITLessThan3MonthsCalculation extends TxMLPatientCalculation {

  private static int LESS_THAN_3_MONTHS = 90;

  @Override
  protected CalculationResultMap evaluateUsingCalculationRules(
      EvaluationContext context, CalculationResultMap txmlResults) {

    CalculationResultMap resultMap = new CalculationResultMap();

    for (Entry<Integer, CalculationResult> pair : txmlResults.entrySet()) {

      Integer patientID = pair.getKey();
      @SuppressWarnings("unchecked")
      Map<String, Date> value = (Map<String, Date>) pair.getValue().getValue();

      Date dataInicio = value.get(TxmlLostFollowUpProcessor.DATA_INICIO);
      Date dataProximoAgendamento = value.get(TxmlLostFollowUpProcessor.DATA_PROXIMO_AGENDAMENTO);

      if (EptsDateUtil.getDaysBetween(dataInicio, dataProximoAgendamento) < LESS_THAN_3_MONTHS) {
        resultMap.put(patientID, new SimpleResult(dataProximoAgendamento, this));
      }
    }
    return resultMap;
  }
}
