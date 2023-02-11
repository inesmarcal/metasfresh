/*
 * #%L
 * de.metas.externalsystem
 * %%
 * Copyright (C) 2022 metas GmbH
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

package de.metas.externalsystem.sap.source;

import com.google.common.base.Strings;
import de.metas.externalsystem.ExternalSystemParentConfigId;
import de.metas.externalsystem.sap.SAPExternalRequest;
import de.metas.i18n.AdMessageKey;
import de.metas.i18n.BooleanWithReason;
import de.metas.i18n.IMsgBL;
import de.metas.i18n.ITranslatableString;
import de.metas.user.UserId;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.adempiere.exceptions.AdempiereException;

import javax.annotation.Nullable;
import java.time.Duration;

@Value
public class SAPContentSourceSFTP
{
	private static final AdMessageKey MSG_DUPLICATE_FILE_LOOKUP_DETAILS = AdMessageKey.of("ExternalSystemConfigSAPDuplicateFileLookupDetails");

	@NonNull
	String hostName;

	@NonNull
	String port;

	@NonNull
	String username;

	@NonNull
	String password;

	@NonNull
	String processedDirectory;

	@NonNull
	String erroredDirectory;

	@NonNull
	Duration pollingFrequency;

	@Nullable
	String targetDirectoryProduct;

	@Nullable
	String fileNamePatternProduct;

	@Nullable
	String targetDirectoryBPartner;

	@Nullable
	String fileNamePatternBPartner;

	@Nullable
	String targetDirectoryCreditLimit;

	@Nullable
	String fileNamePatternCreditLimit;

	@Nullable
	UserId approvedBy;

	@Nullable
	String targetDirectoryConversionRate;

	@Nullable
	String fileNamePatternConversionRate;

	@Builder
	public SAPContentSourceSFTP(
			@NonNull final String hostName,
			@NonNull final String port,
			@NonNull final String username,
			@NonNull final String password,
			@NonNull final String processedDirectory,
			@NonNull final String erroredDirectory,
			@NonNull final Duration pollingFrequency,
			@Nullable final String targetDirectoryProduct,
			@Nullable final String fileNamePatternProduct,
			@Nullable final String targetDirectoryBPartner,
			@Nullable final String fileNamePatternBPartner,
			@Nullable final String targetDirectoryCreditLimit,
			@Nullable final String fileNamePatternCreditLimit,
			@Nullable final UserId approvedBy,
			@Nullable final String targetDirectoryConversionRate,
			@Nullable final String fileNamePatternConversionRate)
	{
		this.hostName = hostName;
		this.port = port;
		this.username = username;
		this.password = password;
		this.processedDirectory = processedDirectory;
		this.erroredDirectory = erroredDirectory;
		this.pollingFrequency = pollingFrequency;
		this.targetDirectoryProduct = targetDirectoryProduct;
		this.fileNamePatternProduct = fileNamePatternProduct;
		this.targetDirectoryBPartner = targetDirectoryBPartner;
		this.fileNamePatternBPartner = fileNamePatternBPartner;
		this.targetDirectoryCreditLimit = targetDirectoryCreditLimit;
		this.fileNamePatternCreditLimit = fileNamePatternCreditLimit;
		this.approvedBy = approvedBy;
		this.targetDirectoryConversionRate = targetDirectoryConversionRate;
		this.fileNamePatternConversionRate = fileNamePatternConversionRate;
	}

	@NonNull
	public BooleanWithReason isStartServicePossible(
			@NonNull final SAPExternalRequest sapExternalRequest,
			@NonNull final ExternalSystemParentConfigId parentId,
			@NonNull final IMsgBL msgBL)
	{
		if (!sapExternalRequest.isStartService())
		{
			return BooleanWithReason.TRUE;
		}

		final String productFileLookupInfo = Strings.nullToEmpty(targetDirectoryProduct)
				.concat(Strings.nullToEmpty(fileNamePatternProduct));

		final String bpartnerFileLookupInfo = Strings.nullToEmpty(targetDirectoryBPartner)
				.concat(Strings.nullToEmpty(fileNamePatternBPartner));

		final String creditLimitFileLookupInfo = Strings.nullToEmpty(targetDirectoryCreditLimit)
				.concat(Strings.nullToEmpty(fileNamePatternCreditLimit));

		final String conversionRateFileLookupInfo = Strings.nullToEmpty(targetDirectoryConversionRate)
				.concat(Strings.nullToEmpty(fileNamePatternConversionRate));

		final boolean isFileLookupInfoDuplicated;
		switch (sapExternalRequest)
		{
			case START_BPARTNER_SYNC_SFTP:
				isFileLookupInfoDuplicated = bpartnerFileLookupInfo.equals(productFileLookupInfo) || bpartnerFileLookupInfo.equals(creditLimitFileLookupInfo) || bpartnerFileLookupInfo.equals(conversionRateFileLookupInfo);
				break;
			case START_PRODUCT_SYNC_SFTP:
				isFileLookupInfoDuplicated = productFileLookupInfo.equals(bpartnerFileLookupInfo) || productFileLookupInfo.equals(creditLimitFileLookupInfo) || productFileLookupInfo.equals(conversionRateFileLookupInfo);
				break;
			case START_CREDIT_LIMIT_SYNC_SFTP:
				isFileLookupInfoDuplicated = creditLimitFileLookupInfo.equals(productFileLookupInfo) || creditLimitFileLookupInfo.equals(bpartnerFileLookupInfo) || creditLimitFileLookupInfo.equals(conversionRateFileLookupInfo);
				break;
			case START_CONVERSION_RATE_SYNC_SFTP:
				isFileLookupInfoDuplicated = conversionRateFileLookupInfo.equals(productFileLookupInfo) || conversionRateFileLookupInfo.equals(bpartnerFileLookupInfo) || conversionRateFileLookupInfo.equals(creditLimitFileLookupInfo);
				break;
			default:
				throw new AdempiereException("Unexpected sapExternalRequest=" + sapExternalRequest.getCode());
		}

		if (isFileLookupInfoDuplicated)
		{
			final ITranslatableString duplicateFileLookupInfoErrorMsg = msgBL.getTranslatableMsgText(MSG_DUPLICATE_FILE_LOOKUP_DETAILS,
																									 sapExternalRequest.getCode(),
																									 parentId.getRepoId());

			return BooleanWithReason.falseBecause(duplicateFileLookupInfoErrorMsg);
		}

		return BooleanWithReason.TRUE;
	}
}
