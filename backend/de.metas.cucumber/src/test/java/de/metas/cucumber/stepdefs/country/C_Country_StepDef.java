/*
 * #%L
 * de.metas.cucumber
 * %%
 * Copyright (C) 2023 metas GmbH
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

package de.metas.cucumber.stepdefs.country;

import de.metas.cucumber.stepdefs.DataTableUtil;
import de.metas.cucumber.stepdefs.StepDefConstants;
import de.metas.util.Services;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import lombok.NonNull;
import org.adempiere.ad.dao.IQueryBL;
import org.compiere.model.I_C_Country;

import java.util.Map;

import static org.compiere.model.I_C_Country.COLUMNNAME_C_Country_ID;
import static org.compiere.model.I_C_Country.COLUMNNAME_CountryCode;

public class C_Country_StepDef
{
	final IQueryBL queryBL = Services.get(IQueryBL.class);

	final C_Country_StepDefData countryTable;

	public C_Country_StepDef(@NonNull final C_Country_StepDefData countryTable)
	{
		this.countryTable = countryTable;
	}

	@And("load C_Country by country code:")
	public void locate_invoice_candidates_for_invoice(@NonNull final DataTable dataTable)
	{
		for (final Map<String, String> row : dataTable.asMaps())
		{
			final String countryCode = DataTableUtil.extractStringForColumnName(row, COLUMNNAME_CountryCode);

			final I_C_Country country = queryBL.createQueryBuilder(I_C_Country.class)
					.addEqualsFilter(COLUMNNAME_CountryCode, countryCode)
					.create()
					.firstOnlyNotNull(I_C_Country.class);

			final String countryIdentifier = DataTableUtil.extractStringForColumnName(row, COLUMNNAME_C_Country_ID + "." + StepDefConstants.TABLECOLUMN_IDENTIFIER);
			countryTable.put(countryIdentifier, country);
		}
	}
}
