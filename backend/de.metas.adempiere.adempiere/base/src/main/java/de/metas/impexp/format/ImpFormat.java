package de.metas.impexp.format;

import com.google.common.collect.ImmutableList;
import de.metas.common.util.time.SystemTime;
import de.metas.report.ReportResultData;
import de.metas.util.Check;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import org.springframework.core.io.ByteArrayResource;

import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Import Format Definition
 */
public final class ImpFormat
{
	@Getter
	@NonNull
	private final ImpFormatId id;

	@Getter
	@NonNull
	private final String name;
	@Getter
	@NonNull
	private final ImpFormatType formatType;
	@Getter
	private final boolean multiLine;
	@Getter
	private final boolean manualImport;
	@Getter
	@NonNull
	private final Charset charset;
	@Getter
	private final int skipFirstNRows;

	/**
	 * The Table to be imported
	 */
	@Getter
	@NonNull
	private final ImportTableDescriptor importTableDescriptor;

	@Getter
	@NonNull
	private final ImmutableList<ImpFormatColumn> columns;

	@Builder
	private ImpFormat(
			@NonNull final ImpFormatId id,
			@NonNull final String name,
			@NonNull final ImpFormatType formatType,
			final boolean multiLine,
			final boolean manualImport,
			@NonNull final ImportTableDescriptor importTableDescriptor,
			@NonNull @Singular final List<ImpFormatColumn> columns,
			@NonNull final Charset charset,
			final int skipFirstNRows)
	{
		Check.assumeNotEmpty(name, "name is not empty");
		Check.assumeNotEmpty(columns, "columns is not empty");

		this.id = id;
		this.name = name;
		this.formatType = formatType;
		this.multiLine = multiLine;
		this.manualImport = manualImport;
		this.importTableDescriptor = importTableDescriptor;
		this.columns = ImmutableList.copyOf(columns);
		this.charset = charset;
		this.skipFirstNRows = Math.max(skipFirstNRows, 0);
	}

	public String getImportTableName()
	{
		return getImportTableDescriptor().getTableName();
	}

	public char getCellDelimiterChar()
	{
		return formatType.getCellDelimiterChar();
	}

	@NonNull
	public String getContentType()
	{
		return formatType.getContentType();
	}

	public ReportResultData generateTabularTemplate()
	{
		final String delimiter = String.valueOf(getCellDelimiterChar());

		final String content = columns
				.stream()
				.sorted(Comparator.comparing(ImpFormatColumn::getStartNo))
				.map(ImpFormatColumn::getName)
				.collect(Collectors.joining(delimiter));

		return ReportResultData.builder()
				.reportData(new ByteArrayResource(content.getBytes(charset)))
				.reportFilename("Template_" + name + "_" + SystemTime.asInstant() + formatType.getFileExtensionIncludingDot())
				.reportContentType(getContentType())
				.build();

	}
}
