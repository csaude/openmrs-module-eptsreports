package org.openmrs.module.eptsreports.reporting.library.datasets;

import java.util.List;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

@Component
public class ListPatientWithReminderCallPrepDataSet extends BaseDataSet {

  private static final String PREP1_FIND_PATIENTS_WITH_REMINDER_PREP_CALL =
      "PREP1/PREP1_LIST_PATIENTS_WITH_REMINDER_PREP_PATIENTS.sql";

  public DataSetDefinition constructDataset(List<Parameter> list) {

    SqlDataSetDefinition dsd = new SqlDataSetDefinition();
    dsd.setName("Lista de Chamadas de Lembrete para Utentes em PrEP");
    dsd.addParameters(list);
    dsd.setSqlQuery(EptsQuerysUtils.loadQuery(PREP1_FIND_PATIENTS_WITH_REMINDER_PREP_CALL));
    return dsd;
  }
}
