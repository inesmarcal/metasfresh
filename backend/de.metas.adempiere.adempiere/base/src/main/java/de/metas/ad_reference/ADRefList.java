package de.metas.ad_reference;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@EqualsAndHashCode
@ToString
public class ADRefList
{
	@Getter @NonNull private final ReferenceId referenceId;
	@Getter @NonNull final String name;
	@Getter boolean isOrderByValue;
	private final ImmutableMap<String, ADRefListItem> itemsByValue;

	@Builder
	private ADRefList(
			final @NonNull ReferenceId referenceId,
			final @NonNull String name,
			final boolean isOrderByValue,
			final @NonNull List<ADRefListItem> items)
	{

		this.referenceId = referenceId;
		this.name = name;
		this.itemsByValue = Maps.uniqueIndex(items, ADRefListItem::getValue);
		this.isOrderByValue = isOrderByValue;
	}

	public Set<String> getValues() {return itemsByValue.keySet();}

	public Collection<ADRefListItem> getItems() {return itemsByValue.values();}

	public Optional<ADRefListItem> getItemByValue(final String value) {return Optional.ofNullable(itemsByValue.get(value));}

	public boolean containsValue(String value) {return itemsByValue.get(value) != null;}
}
