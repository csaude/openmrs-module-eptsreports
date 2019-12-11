package org.openmrs.module.eptsreports.reporting.calculation.processor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang3.time.DateUtils;
import org.openmrs.module.reporting.common.DateUtil;

public class CalculationProcessorUtils {

  @SuppressWarnings("unchecked")
  public static Map<Integer, Date> getMaxMapDateByPatient(Map<Integer, Date>... maps) {

    Map<Integer, Date> resutls = new HashMap<>();

    Set<Integer> ids = new TreeSet<>();
    for (Map<Integer, Date> map : maps) {
      ids.addAll(map.keySet());
    }
    Date finalComparisonDate = DateUtil.getDateTime(Integer.MAX_VALUE, 1, 1);
    for (Integer patientId : ids) {
      Date maxDate = DateUtil.getDateTime(Integer.MAX_VALUE, 1, 1);
      for (int i = 0; i < maps.length; i++) {
        Date dateMap = getDate(maps[i], patientId);

        if (dateMap != null && dateMap.compareTo(maxDate) > 0) {
          maxDate = dateMap;
        }
      }
      if (!DateUtils.isSameDay(maxDate, finalComparisonDate)) {
        resutls.put(patientId, maxDate);
      }
    }
    return resutls;
  }

  @SuppressWarnings("unchecked")
  public static Map<Integer, Date> getMinMapDateByPatient(Map<Integer, Date>... maps) {

    Map<Integer, Date> resutls = new HashMap<>();
    Set<Integer> ids = new TreeSet<>();
    for (Map<Integer, Date> map : maps) {
      ids.addAll(map.keySet());
    }
    Date finalComparisonDate = DateUtil.getDateTime(Integer.MIN_VALUE, 1, 1);
    for (Integer patientId : ids) {

      Date minDate = DateUtil.getDateTime(Integer.MIN_VALUE, 1, 1);
      for (int i = 0; i < maps.length; i++) {
        Date dateMap = getDate(maps[i], patientId);

        if (dateMap != null && dateMap.compareTo(minDate) < 0) {
          minDate = dateMap;
        }
      }
      if (!DateUtils.isSameDay(minDate, finalComparisonDate)) {
        resutls.put(patientId, minDate);
      }
    }
    return resutls;
  }

  private static Date getDate(Map<Integer, Date> map, Integer patientId) {
    return map != null ? map.get(patientId) : null;
  }
}
