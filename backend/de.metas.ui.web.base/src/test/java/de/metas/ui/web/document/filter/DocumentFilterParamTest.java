package de.metas.ui.web.document.filter;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import de.metas.document.engine.DocStatus;
import de.metas.ui.web.document.filter.DocumentFilterParam.Operator;
import de.metas.ui.web.window.datatypes.LookupValue.IntegerLookupValue;
import de.metas.ui.web.window.datatypes.LookupValue.StringLookupValue;
import de.metas.ui.web.window.datatypes.LookupValuesList;
import org.adempiere.exceptions.AdempiereException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/*
 * #%L
 * metasfresh-webui-api
 * %%
 * Copyright (C) 2020 metas GmbH
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

public class DocumentFilterParamTest
{
	private static DocumentFilterParam newParam(final Object value)
	{
		return DocumentFilterParam.builder()
				.setFieldName("param")
				.setOperator(Operator.EQUAL)
				.setValue(value)
				.build();
	}

	@Nested
	public class equalsTest
	{
		@Test
		public void testEquals()
		{
			final DocumentFilterParam param1 = DocumentFilterParam.builder()
					.setFieldName("param1")
					.setOperator(Operator.BETWEEN)
					.setValue("value1")
					.setValueTo("value2")
					.build();
			final DocumentFilterParam param2 = DocumentFilterParam.builder()
					.setFieldName("param1")
					.setOperator(Operator.BETWEEN)
					.setValue("value1")
					.setValueTo("value2")
					.build();

			assertThat(param1).isEqualTo(param2);
		}

		@Test
		public void testNotEquals()
		{
			final DocumentFilterParam param1 = DocumentFilterParam.builder()
					.setFieldName("param1")
					.setOperator(Operator.BETWEEN)
					.setValue("value1")
					.setValueTo("value2")
					.build();
			final DocumentFilterParam param2 = DocumentFilterParam.builder()
					.setFieldName("param1")
					.setOperator(Operator.BETWEEN)
					.setValue("value1")
					.setValueTo("value3")
					.build();

			assertThat(param1).isNotEqualTo(param2);
		}
	}

	@Nested
	public class getValueAsCollection
	{
		@Test
		public void fromNull()
		{
			assertThatThrownBy(() -> newParam(null).getValueAsCollection())
					.isInstanceOf(AdempiereException.class)
					.hasMessageStartingWith("Cannot convert null value");
		}

		@Test
		public void fromList()
		{
			final ImmutableList<String> value = ImmutableList.of("1", "2");
			assertThat(newParam(value).getValueAsCollection()).isSameAs(value);
		}

		@Test
		public void fromSet()
		{
			final ImmutableSet<String> value = ImmutableSet.of("1", "2");
			assertThat(newParam(value).getValueAsCollection()).isSameAs(value);
		}

		/**
		 * Needed for widget types like MultiListValue.
		 */
		@Test
		public void fromLookupValuesList()
		{
			final DocumentFilterParam param = newParam(LookupValuesList.fromCollection(ImmutableList.of(
					StringLookupValue.of("id1", "displayName1"),
					StringLookupValue.of("id2", "displayName2"))));

			assertThat(param.getValueAsCollection().toArray()).containsExactly(
					StringLookupValue.of("id1", "displayName1"),
					StringLookupValue.of("id2", "displayName2"));
		}

		@Nested
		public class fromSomethingWhichCannotBeConvertedToCollection
		{
			private void test(final Object value)
			{
				assertThat(newParam(value).getValueAsCollection())
						.isEqualTo(ImmutableList.of(value));
			}

			@Test
			public void fromString()
			{
				test("just a string");
			}

			@Test
			public void fromStringLookupValue()
			{
				test(StringLookupValue.of("id", "displayName"));
			}
		}
	}

	@Nested
	public class getValueAsString
	{
		@Test
		void from_null() {assertThat(newParam(null).getValueAsString()).isNull();}

		@Test
		void from_StringLookupValue() {assertThat(newParam(StringLookupValue.of("key", "name")).getValueAsString()).isEqualTo("key");}

		@Test
		void from_IntegerLookupValue() {assertThat(newParam(IntegerLookupValue.of(123, "name")).getValueAsString()).isEqualTo("123");}

		@Test
		void from_ReferenceListAwareEnum() {assertThat(newParam(DocStatus.Completed).getValueAsString()).isEqualTo(DocStatus.Completed.getCode());}
	}

	@Nested
	public class getValueAsRefListOrNull
	{
		void assertDocStatusCompleted(final Object value)
		{
			assertThat(newParam(value).getValueAsRefListOrNull(DocStatus::ofCode)).isEqualTo(DocStatus.Completed);
		}

		@Test
		void from_null() {assertThat(newParam(null).getValueAsRefListOrNull(DocStatus::ofCode)).isNull();}

		@Test
		void from_EmptyString() {assertThat(newParam("").getValueAsRefListOrNull(DocStatus::ofCode)).isNull();}

		@Test
		void from_String() {assertDocStatusCompleted(DocStatus.Completed.getCode());}

		@Test
		void from_StringLookupValue() {assertDocStatusCompleted(StringLookupValue.of(DocStatus.Completed.getCode(), "name"));}

		@Test
		void from_ReferenceListAwareEnum() {assertDocStatusCompleted(DocStatus.Completed);}
	}
}