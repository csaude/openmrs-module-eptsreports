package org.openmrs.module.eptsreports.reporting.calculation.util.processor;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;
import org.openmrs.api.context.Context;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.stereotype.Component;

@Component
public class TxmlLostFollowUpProcessor {

  public static final String DATA_INICIO = "data_inicio";
  public static final String DATA_PROXIMO_AGENDAMENTO = "data_proximo_agendamento";

  private static final String FIND_PATIENTS_WHO_LOST_FOLLOWP_DATA =
      "TX_ML/PATIENTS_WHO_LOST_FOLLOWP_DATA.sql";

  @SuppressWarnings("unchecked")
  public Map<Integer, Map<String, Date>> getResutls(EvaluationContext context) {

    SqlQueryBuilder qb =
        new SqlQueryBuilder(
            EptsQuerysUtils.loadQuery(FIND_PATIENTS_WHO_LOST_FOLLOWP_DATA),
            context.getParameterValues());

    List<Object[]> queryResult =
        Context.getRegisteredComponents(EvaluationService.class).get(0).evaluateToList(qb, context);

    Map<Integer, Map<String, Date>> obsForPatients = new HashedMap();
    for (Object[] row : queryResult) {

      Map<String, Date> value = new HashedMap();

      value.put(DATA_INICIO, (Date) row[1]);
      value.put(DATA_PROXIMO_AGENDAMENTO, (Date) row[2]);

      obsForPatients.put((Integer) row[0], value);
    }

    return obsForPatients;
  }
}
