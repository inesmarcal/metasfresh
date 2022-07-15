/*
 * #%L
 * de.metas.business
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

package de.metas.project.budget;

import de.metas.product.ResourceId;
import de.metas.project.ProjectId;
import de.metas.resource.ResourceGroupAndResourceId;
import de.metas.resource.ResourceGroupId;
import de.metas.resource.ResourceService;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class BudgetProjectService
{
	private final ResourceService resourceService;
	private final BudgetProjectRepository projectRepository;
	private final BudgetProjectResourceRepository resourceBudgetRepository;

	public BudgetProjectService(
			final ResourceService resourceService,
			final BudgetProjectRepository projectRepository,
			final BudgetProjectResourceRepository resourceBudgetRepository)
	{
		this.resourceService = resourceService;
		this.projectRepository = projectRepository;
		this.resourceBudgetRepository = resourceBudgetRepository;
	}

	public List<BudgetProject> getAllActiveProjects()
	{
		return projectRepository.getAllActive();
	}

	public Map<ProjectId, BudgetProjectResources> getBudgetsByProjectIds(@NonNull final Set<ProjectId> projectIds)
	{
		return resourceBudgetRepository.getByProjectIds(projectIds);
	}

	public BudgetProjectResource getBudgetsById(@NonNull final ProjectId projectId, @NonNull final BudgetProjectResourceId id)
	{
		return resourceBudgetRepository.getByProjectId(projectId).getBudgetById(id);
	}

	public BudgetProjectResource getBudgetsById(@NonNull final BudgetProjectAndResourceId id)
	{
		return resourceBudgetRepository.getByProjectId(id.getProjectId()).getBudgetById(id.getProjectResourceId());
	}

	public Optional<BudgetProjectResource> findBudgetForResource(
			@NonNull final ProjectId budgetProjectId,
			@NonNull final ResourceId resourceId)
	{
		final ResourceGroupId resourceGroupId = resourceService.getResourceById(resourceId).getResourceGroupId();
		if (resourceGroupId == null)
		{
			return Optional.empty();
		}

		return resourceBudgetRepository.getByProjectId(budgetProjectId)
				.findBudgetForResource(ResourceGroupAndResourceId.of(resourceGroupId, resourceId));
	}

	public Optional<BudgetProject> getById(@NonNull final ProjectId projectId)
	{
		return projectRepository.getById(projectId);
	}
}
