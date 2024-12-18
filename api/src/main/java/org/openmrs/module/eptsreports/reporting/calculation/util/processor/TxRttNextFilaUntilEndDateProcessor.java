package org.openmrs.module.eptsreports.reporting.calculation.util.processor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.openmrs.api.context.Context;
import org.openmrs.module.eptsreports.reporting.library.queries.TxRttQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsListUtils;
import org.openmrs.module.reporting.common.ListMap;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.stereotype.Component;

@Component
public class TxRttNextFilaUntilEndDateProcessor {

  private static final int CHUNK_SIZE = 1000;

  public ListMap<Integer, Date[]> getResutls(List<Integer> cohort, EvaluationContext context) {

    if (cohort == null || cohort.isEmpty()) {
      return new ListMap<>();
    }
    return getNextFilaScheduled(context, cohort);
  }

  private ListMap<Integer, Date[]> getNextFilaScheduled(
      EvaluationContext context, List<Integer> cohort) {

    ListMap<Integer, Date[]> listPatientDates = new ListMap<>();
    final int slices = EptsListUtils.listSlices(cohort, CHUNK_SIZE);
    for (int position = 0; position < slices; position++) {

      final List<Integer> subList =
          cohort.subList(
              position * CHUNK_SIZE,
              (((position * CHUNK_SIZE) + CHUNK_SIZE) < cohort.size()
                  ? (position * CHUNK_SIZE) + CHUNK_SIZE
                  : cohort.size()));
      final SqlQueryBuilder queryBuilder =
          new SqlQueryBuilder(
              TxRttQueries.QUERY.findNextScheduledFilaEncountersByPatientsIdAndLocation,
              context.getParameterValues());
      queryBuilder.addParameter("patientIds", subList);

      List<Object[]> queryResult =
          Context.getRegisteredComponents(EvaluationService.class)
              .get(0)
              .evaluateToList(queryBuilder, context);

      for (Object[] row : queryResult) {

        Object row1 = row[1];
        Object row2 = row[2];

        LocalDateTime rowDate1 = (LocalDateTime) row1;
        LocalDateTime rowDate2 = (LocalDateTime) row2;

        Date date1 = Date.from(rowDate1.atZone(ZoneId.systemDefault()).toInstant());
        Date date2 = Date.from(rowDate2.atZone(ZoneId.systemDefault()).toInstant());

        listPatientDates.putInList((Integer) row[0], new Date[] {date1, date2});
      }
    }
    return listPatientDates;
  }
}
