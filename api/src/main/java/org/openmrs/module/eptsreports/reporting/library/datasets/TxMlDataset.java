package org.openmrs.module.eptsreports.reporting.library.datasets;

import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.ABOVE_FIFTY;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.FIFTEEN_TO_NINETEEN;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.FIVE_TO_NINE;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.FORTY_FIVE_TO_FORTY_NINE;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.FORTY_TO_FORTY_FOUR;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.ONE_TO_FOUR;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.TEN_TO_FOURTEEN;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.THIRTY_FIVE_TO_THIRTY_NINE;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.THIRTY_TO_THRITY_FOUR;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.TWENTY_FIVE_TO_TWENTY_NINE;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.TWENTY_TO_TWENTY_FOUR;
import static org.openmrs.module.eptsreports.reporting.utils.AgeRange.UNDER_ONE;

import java.util.Arrays;
import java.util.List;
import org.openmrs.module.eptsreports.reporting.library.cohorts.TxCurrCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.cohorts.TxMlCohortQueries;
import org.openmrs.module.eptsreports.reporting.library.dimensions.AgeDimensionCohortInterface;
import org.openmrs.module.eptsreports.reporting.library.dimensions.EptsCommonDimension;
import org.openmrs.module.eptsreports.reporting.library.indicators.EptsGeneralIndicator;
import org.openmrs.module.eptsreports.reporting.utils.AgeRange;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.eptsreports.reporting.utils.Gender;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TxMlDataset extends BaseDataSet {

	@Autowired
	private EptsCommonDimension eptsCommonDimension;

	@Autowired
	@Qualifier("commonAgeDimensionCohort")
	private AgeDimensionCohortInterface ageDimensionCohort;

	@Autowired
	private EptsGeneralIndicator eptsGeneralIndicator;

	@Autowired
	private TxMlCohortQueries txMlCohortQueries;

	@Autowired
	private TxCurrCohortQueries txCurrCohortQueries;

	public DataSetDefinition constructtxMlDataset() {
		CohortIndicatorDataSetDefinition dsd = new CohortIndicatorDataSetDefinition();

		String mappings = "startDate=${startDate},endDate=${endDate},location=${location}";

		CohortDefinition txMLDefinition = txCurrCohortQueries.getTXMLPatientsWhoMissedNextApointmentCalculation();
		final CohortIndicator patientsWhoMissedNextApointmentIndicator = this.eptsGeneralIndicator
				.getIndicator("findPatientsWhoAreActiveOnART", EptsReportUtils.map(txMLDefinition, mappings));

		dsd.setName("Tx_Ml Data Set");
		dsd.addParameters(getParameters());
		// tie dimensions to this data definition
		dsd.addDimension("gender", EptsReportUtils.map(eptsCommonDimension.gender(), ""));
		dsd.addDimension("age",
				EptsReportUtils.map(eptsCommonDimension.age(ageDimensionCohort), "effectiveDate=${endDate}"));

		dsd.addDimension("dead",
				EptsReportUtils.map(this.txCurrCohortQueries.findPatientsWhoAreDeadDimension(), mappings));

		// start building the datasets
		// get the column for the totals
		dsd.addColumn("M1", "Total missed appointments",
				EptsReportUtils.map(
						eptsGeneralIndicator.getIndicator("totals missed", EptsReportUtils.map(
								txCurrCohortQueries.getTXMLPatientsWhoMissedNextApointmentCalculation(), mappings)),
						mappings),
				"");

		this.addColums(dsd, mappings, patientsWhoMissedNextApointmentIndicator, UNDER_ONE, ONE_TO_FOUR, FIVE_TO_NINE,
				TEN_TO_FOURTEEN, FIFTEEN_TO_NINETEEN, TWENTY_TO_TWENTY_FOUR, TWENTY_FIVE_TO_TWENTY_NINE,
				THIRTY_TO_THRITY_FOUR, THIRTY_FIVE_TO_THIRTY_NINE, FORTY_TO_FORTY_FOUR, FORTY_FIVE_TO_FORTY_NINE,
				ABOVE_FIFTY);

		// txMlCohortQueries.getPatientsWhoMissedNextAppointmentAndNotTransferredOut()

		// get totals disaggregated by gender and age
		addRow(dsd, "M2", "Age and Gender",
				EptsReportUtils.map(
						eptsGeneralIndicator.getIndicator("Age and Gender", EptsReportUtils.map(
								txMlCohortQueries.getPatientsWhoMissedNextAppointmentAndNotTransferredOut(), mappings)),
						mappings),
				getColumnsForAgeAndGender());

		dsd.addColumn("ANC", "TX_NEW: Breastfeeding Started ART",
				EptsReportUtils.map(patientsWhoMissedNextApointmentIndicator, mappings), "dead=dead");

		// txMlCohortQueries
		// .getPatientsWhoMissedNextAppointmentAndNotTransferredOutButDiedDuringReportingPeriod()

		// Missed appointment and dead
		addRow(dsd, "M3", "Dead",
				EptsReportUtils.map(eptsGeneralIndicator.getIndicator("missed and dead",
						EptsReportUtils.map(txCurrCohortQueries.getPatientsMarkedDead(), mappings)), mappings),
				getColumnsForAgeAndGender());

		// Not Consented
		addRow(dsd, "M4", "Not Consented", EptsReportUtils.map(eptsGeneralIndicator.getIndicator("Not Consented",
				EptsReportUtils.map(txMlCohortQueries
						.getPatientsWhoMissedNextAppointmentAndNotTransferredOutAndNotConsentedDuringReportingPeriod(),
						mappings)),
				mappings), getColumnsForAgeAndGender());

		// Traced (Unable to locate)
		addRow(dsd, "M5", "Traced (Unable to locate)",
				EptsReportUtils.map(eptsGeneralIndicator.getIndicator("Traced (Unable to locate)",
						EptsReportUtils.map(
								txMlCohortQueries.getPatientsWhoMissedNextAppointmentAndNotTransferredOutAndTraced(),
								mappings)),
						mappings),
				getColumnsForAgeAndGender());

		// Untraced Patients
		addRow(dsd, "M6", "Untraced patients",
				EptsReportUtils.map(eptsGeneralIndicator.getIndicator("Untraced Patients",
						EptsReportUtils.map(
								txMlCohortQueries.getPatientsWhoMissedNextAppointmentAndNotTransferredOutAndUntraced(),
								mappings)),
						mappings),
				getColumnsForAgeAndGender());
		return dsd;
	}

	private void addColums(final CohortIndicatorDataSetDefinition dataSetDefinition, final String mappings,
			final CohortIndicator cohortIndicator, final AgeRange... rannges) {

		for (final AgeRange range : rannges) {

			final String maleName = this.getName(Gender.MALE, range);
			final String femaleName = this.getName(Gender.FEMALE, range);

			dataSetDefinition.addColumn(maleName, maleName.replace("-", " "),
					EptsReportUtils.map(cohortIndicator, mappings), maleName + "=" + maleName);

			dataSetDefinition.addColumn(femaleName, femaleName.replace("-", " "),
					EptsReportUtils.map(cohortIndicator, mappings), femaleName + "=" + femaleName);
		}
	}

	private void addDimensions(final CohortIndicatorDataSetDefinition cohortIndicatorDataSetDefinition,
			final String mappings, final AgeRange... ranges) {

		for (final AgeRange range : ranges) {

			cohortIndicatorDataSetDefinition.addDimension(this.getName(Gender.MALE, range),
					EptsReportUtils.map(this.eptsCommonDimension.findPatientsWhoAreNewlyEnrolledOnArtByAgeAndGender(
							this.getName(Gender.MALE, range), range, Gender.MALE.getName()), mappings));

			cohortIndicatorDataSetDefinition
					.addDimension(this.getName(Gender.FEMALE, range),
							EptsReportUtils.map(
									this.eptsCommonDimension.findPatientsWhoAreNewlyEnrolledOnArtByAgeAndGender(
											this.getName(Gender.FEMALE, range), range, Gender.FEMALE.getName()),
									mappings));
		}
	}

	private String getName(final Gender gender, final AgeRange ageRange) {
		String name = "males-" + ageRange.getName() + "" + gender.getName();

		if (gender.equals(Gender.FEMALE)) {
			name = "females-" + ageRange.getName() + "" + gender.getName();
		}

		return name;
	}

	private List<ColumnParameters> getColumnsForAgeAndGender() {
		ColumnParameters under1M = new ColumnParameters("under1M", "under 1 year male", "gender=M|age=<1", "01");
		ColumnParameters oneTo4M = new ColumnParameters("oneTo4M", "1 - 4 years male", "gender=M|age=1-4", "02");
		ColumnParameters fiveTo9M = new ColumnParameters("fiveTo9M", "5 - 9 years male", "gender=M|age=5-9", "03");
		ColumnParameters tenTo14M = new ColumnParameters("tenTo14M", "10 - 14 male", "gender=M|age=10-14", "04");
		ColumnParameters fifteenTo19M = new ColumnParameters("fifteenTo19M", "15 - 19 male", "gender=M|age=15-19",
				"05");
		ColumnParameters twentyTo24M = new ColumnParameters("twentyTo24M", "20 - 24 male", "gender=M|age=20-24", "06");
		ColumnParameters twenty5To29M = new ColumnParameters("twenty4To29M", "25 - 29 male", "gender=M|age=25-29",
				"07");
		ColumnParameters thirtyTo34M = new ColumnParameters("thirtyTo34M", "30 - 34 male", "gender=M|age=30-34", "08");
		ColumnParameters thirty5To39M = new ColumnParameters("thirty5To39M", "35 - 39 male", "gender=M|age=35-39",
				"09");
		ColumnParameters foutyTo44M = new ColumnParameters("foutyTo44M", "40 - 44 male", "gender=M|age=40-44", "10");
		ColumnParameters fouty5To49M = new ColumnParameters("fouty5To49M", "45 - 49 male", "gender=M|age=45-49", "11");
		ColumnParameters above50M = new ColumnParameters("above50M", "50+ male", "gender=M|age=50+", "12");
		ColumnParameters unknownM = new ColumnParameters("unknownM", "Unknown age male", "gender=M|age=UK", "13");

		ColumnParameters under1F = new ColumnParameters("under1F", "under 1 year female", "gender=F|age=<1", "14");
		ColumnParameters oneTo4F = new ColumnParameters("oneTo4F", "1 - 4 years female", "gender=F|age=1-4", "15");
		ColumnParameters fiveTo9F = new ColumnParameters("fiveTo9F", "5 - 9 years female", "gender=F|age=5-9", "16");
		ColumnParameters tenTo14F = new ColumnParameters("tenTo14F", "10 - 14 female", "gender=F|age=10-14", "17");
		ColumnParameters fifteenTo19F = new ColumnParameters("fifteenTo19F", "15 - 19 female", "gender=F|age=15-19",
				"18");
		ColumnParameters twentyTo24F = new ColumnParameters("twentyTo24F", "20 - 24 female", "gender=F|age=20-24",
				"19");
		ColumnParameters twenty5To29F = new ColumnParameters("twenty4To29F", "25 - 29 female", "gender=F|age=25-29",
				"20");
		ColumnParameters thirtyTo34F = new ColumnParameters("thirtyTo34F", "30 - 34 female", "gender=F|age=30-34",
				"21");
		ColumnParameters thirty5To39F = new ColumnParameters("thirty5To39F", "35 - 39 female", "gender=F|age=35-39",
				"22");
		ColumnParameters foutyTo44F = new ColumnParameters("foutyTo44F", "40 - 44 female", "gender=F|age=40-44", "23");
		ColumnParameters fouty5To49F = new ColumnParameters("fouty5To49F", "45 - 49 female", "gender=F|age=45-49",
				"24");
		ColumnParameters above50F = new ColumnParameters("above50F", "50+ female", "gender=F|age=50+", "25");
		ColumnParameters unknownF = new ColumnParameters("unknownF", "Unknown age female", "gender=F|age=UK", "26");
		ColumnParameters total = new ColumnParameters("totals", "Totals", "", "27");

		return Arrays.asList(under1M, oneTo4M, fiveTo9M, tenTo14M, fifteenTo19M, twentyTo24M, twenty5To29M, thirtyTo34M,
				thirty5To39M, foutyTo44M, fouty5To49M, above50M, unknownM, under1F, oneTo4F, fiveTo9F, tenTo14F,
				fifteenTo19F, twentyTo24F, twenty5To29F, thirtyTo34F, thirty5To39F, foutyTo44F, fouty5To49F, above50F,
				unknownF, total);
	}
}
