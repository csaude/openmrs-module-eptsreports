package org.openmrs.module.eptsreports.reporting.library.datasets.resumo.ceta;

import java.util.Arrays;
import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.datasets.BaseDataSet;
import org.springframework.stereotype.Component;

@Component
public class ResumoMensalCETADataSetDefinition extends BaseDataSet {

  protected List<ColumnParameters> getColumns(String dimensionName) {

    ColumnParameters a1 =
        new ColumnParameters(
            "15-19",
            "15-19 male - 2a consulta TARV",
            "gender=M|age=15-19|" + dimensionName + "=secondARVConsultation",
            "01");

    ColumnParameters a2 =
        new ColumnParameters(
            "15-19",
            "15-19 female - 2a consulta TARV",
            "gender=F|age=15-19|" + dimensionName + "=secondARVConsultation",
            "02");

    ColumnParameters a3 =
        new ColumnParameters(
            "20-24",
            "20-24 male - 2a consulta TARV",
            "gender=M|age=20-24|" + dimensionName + "=secondARVConsultation",
            "03");

    ColumnParameters a4 =
        new ColumnParameters(
            "20-24",
            "20-24 female - 2a consulta TARV",
            "gender=F|age=20-24|" + dimensionName + "=secondARVConsultation",
            "04");

    ColumnParameters a5 =
        new ColumnParameters(
            "25+",
            "25+ male - 2a consulta TARV",
            "gender=M|age=25+|" + dimensionName + "=secondARVConsultation",
            "05");

    ColumnParameters a6 =
        new ColumnParameters(
            "25+",
            "25+ female- 2a consulta TARV",
            "gender=F|age=25+|" + dimensionName + "=secondARVConsultation",
            "06");

    ColumnParameters a7 =
        new ColumnParameters(
            "totalM",
            "Total male - 2a consulta TARV",
            "gender=M|age=15+|" + dimensionName + "=secondARVConsultation",
            "07");

    ColumnParameters a8 =
        new ColumnParameters(
            "totalF",
            "Total female - 2a consulta TARV",
            "gender=F|age=15+|" + dimensionName + "=secondARVConsultation",
            "08");

    ColumnParameters a9 =
        new ColumnParameters(
            "15-19",
            "15-19 male - CV>1000 cp/ml",
            "gender=M|age=15-19|" + dimensionName + "=VLGreaterThan1000",
            "09");

    ColumnParameters a10 =
        new ColumnParameters(
            "15-19",
            "15-19 female - CV>1000 cp/ml",
            "gender=F|age=15-19|" + dimensionName + "=VLGreaterThan1000",
            "10");

    ColumnParameters a11 =
        new ColumnParameters(
            "20-24",
            "20-24 male - CV>1000 cp/ml",
            "gender=M|age=20-24|" + dimensionName + "=VLGreaterThan1000",
            "11");

    ColumnParameters a12 =
        new ColumnParameters(
            "20-24",
            "20-24 female - CV>1000 cp/ml",
            "gender=F|age=20-24|" + dimensionName + "=VLGreaterThan1000",
            "12");

    ColumnParameters a13 =
        new ColumnParameters(
            "25+",
            "25+ male - CV>1000 cp/ml",
            "gender=M|age=25+|" + dimensionName + "=VLGreaterThan1000",
            "13");

    ColumnParameters a14 =
        new ColumnParameters(
            "25+",
            "25+ female - CV>1000 cp/ml",
            "gender=F|age=25+|" + dimensionName + "=VLGreaterThan1000",
            "14");

    ColumnParameters a15 =
        new ColumnParameters(
            "totalM",
            "Total male - CV>1000 cp/ml",
            "gender=M|age=15+|" + dimensionName + "=VLGreaterThan1000",
            "15");

    ColumnParameters a16 =
        new ColumnParameters(
            "totalF",
            "Total female - CV>1000 cp/ml",
            "gender=F|age=15+|" + dimensionName + "=VLGreaterThan1000",
            "16");

    ColumnParameters a17 =
        new ColumnParameters(
            "15-19",
            "15-19 male - Reintegrado",
            "gender=M|age=15-19|" + dimensionName + "=reintegreted",
            "17");

    ColumnParameters a18 =
        new ColumnParameters(
            "15-19",
            "15-19 female - Reintegrado",
            "gender=F|age=15-19|" + dimensionName + "=reintegreted",
            "18");

    ColumnParameters a19 =
        new ColumnParameters(
            "20-24",
            "20-24 male - Reintegrado",
            "gender=M|age=20-24|" + dimensionName + "=reintegreted",
            "19");

    ColumnParameters a20 =
        new ColumnParameters(
            "20-24",
            "20-24 female - Reintegrado",
            "gender=F|age=20-24|" + dimensionName + "=reintegreted",
            "20");

    ColumnParameters a21 =
        new ColumnParameters(
            "25+",
            "25+ male - Reintegrado",
            "gender=M|age=25+|" + dimensionName + "=reintegreted",
            "21");

    ColumnParameters a22 =
        new ColumnParameters(
            "25+",
            "25+ female - Reintegrado",
            "gender=F|age=25+|" + dimensionName + "=reintegreted",
            "22");

    ColumnParameters a23 =
        new ColumnParameters(
            "totalM",
            "Total male - Reintegrado",
            "gender=M|age=15+|" + dimensionName + "=reintegreted",
            "23");

    ColumnParameters a24 =
        new ColumnParameters(
            "totalF",
            "Total female - Reintegrado",
            "gender=F|age=15+|" + dimensionName + "=reintegreted",
            "24");

    ColumnParameters a25 =
        new ColumnParameters(
            "15-19",
            "15-19 male - Factores Psicossociais",
            "gender=M|age=15-19|" + dimensionName + "=psychosocialFactors",
            "25");

    ColumnParameters a26 =
        new ColumnParameters(
            "15-19",
            "15-19 female - Factores Psicossociais",
            "gender=F|age=15-19|" + dimensionName + "=psychosocialFactors",
            "26");

    ColumnParameters a27 =
        new ColumnParameters(
            "20-28",
            "20-24 male - Factores Psicossociais",
            "gender=M|age=20-24|" + dimensionName + "=psychosocialFactors",
            "27");

    ColumnParameters a28 =
        new ColumnParameters(
            "20-24",
            "20-24 female - Factores Psicossociais",
            "gender=F|age=20-24|" + dimensionName + "=psychosocialFactors",
            "28");

    ColumnParameters a29 =
        new ColumnParameters(
            "25+",
            "25+ male - Factores Psicossociais",
            "gender=M|age=25+|" + dimensionName + "=psychosocialFactors",
            "29");

    ColumnParameters a30 =
        new ColumnParameters(
            "25+",
            "25+ female - Factores Psicossociais",
            "gender=F|age=25+|" + dimensionName + "=psychosocialFactors",
            "30");

    ColumnParameters a31 =
        new ColumnParameters(
            "totalM",
            "Total male - Factores Psicossociais",
            "gender=M|age=15+|" + dimensionName + "=psychosocialFactors",
            "31");

    ColumnParameters a32 =
        new ColumnParameters(
            "totalF",
            "Total female - Factores Psicossociais",
            "gender=F|age=15+|" + dimensionName + "=psychosocialFactors",
            "32");

    ColumnParameters a33 =
        new ColumnParameters(
            "15-19",
            "15-19 male - Sem Informacao",
            "gender=M|age=15-19|" + dimensionName + "=whithoutInformation",
            "33");

    ColumnParameters a34 =
        new ColumnParameters(
            "15-19",
            "15-19 female - Sem Informacao",
            "gender=F|age=15-19|" + dimensionName + "=whithoutInformation",
            "34");

    ColumnParameters a35 =
        new ColumnParameters(
            "20-28",
            "20-24 male - Sem Informacao",
            "gender=M|age=20-24|" + dimensionName + "=whithoutInformation",
            "35");

    ColumnParameters a36 =
        new ColumnParameters(
            "20-24",
            "20-24 female - Sem Informacao",
            "gender=F|age=20-24|" + dimensionName + "=whithoutInformation",
            "36");

    ColumnParameters a37 =
        new ColumnParameters(
            "25+",
            "25+ male - Sem Informacao",
            "gender=M|age=25+|" + dimensionName + "=whithoutInformation",
            "37");

    ColumnParameters a38 =
        new ColumnParameters(
            "25+",
            "25+ female - Sem Informacao",
            "gender=F|age=25+|" + dimensionName + "=whithoutInformation",
            "38");

    ColumnParameters a39 =
        new ColumnParameters(
            "totalM",
            "Total male - Sem Informacao",
            "gender=M|age=15+|" + dimensionName + "=whithoutInformation",
            "39");

    ColumnParameters a40 =
        new ColumnParameters(
            "totalF",
            "Total female - Sem Informacao",
            "gender=F|age=15+|" + dimensionName + "=whithoutInformation",
            "40");

    ColumnParameters a41 =
        new ColumnParameters("15-19", "15-19 male - TOTAL", "gender=M|age=15-19", "41");
    ColumnParameters a42 =
        new ColumnParameters("15-19", "15-19 female - TOTAL", "gender=F|age=15-19", "42");
    ColumnParameters a43 =
        new ColumnParameters("20-24", "20-24 male - TOTAL", "gender=M|age=20-24", "43");
    ColumnParameters a44 =
        new ColumnParameters("20-24", "20-24 female - TOTAL", "gender=F|age=20-24", "44");
    ColumnParameters a45 =
        new ColumnParameters("25+", "25+ male - TOTAL", "gender=M|age=25+", "45");
    ColumnParameters a46 =
        new ColumnParameters("25+", "25+ female - TOTAL", "gender=F|age=25+", "46");
    ColumnParameters a47 =
        new ColumnParameters("totalM", "Total male - TOTAL", "gender=M|age=15+", "47");
    ColumnParameters a48 =
        new ColumnParameters("totalF", "Total female - TOTAL", "gender=F|age=15+", "48");

    return Arrays.asList(
        a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20,
        a21, a22, a23, a24, a25, a26, a27, a28, a29, a30, a31, a32, a33, a34, a35, a36, a37, a38,
        a39, a40, a41, a42, a43, a44, a45, a46, a47, a48);
  }
}
