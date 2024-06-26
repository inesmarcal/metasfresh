package de.metas.security.process;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import de.metas.process.IProcessPrecondition;
import de.metas.process.IProcessPreconditionsContext;
import de.metas.process.JavaProcess;
import de.metas.process.Param;
import de.metas.process.ProcessPreconditionsResolution;
import de.metas.security.Principal;
import de.metas.security.permissions.Access;
import de.metas.security.permissions.record_access.PermissionIssuer;
import de.metas.security.permissions.record_access.RecordAccessGrantRequest;
import de.metas.security.permissions.record_access.RecordAccessRevokeRequest;
import de.metas.security.permissions.record_access.RecordAccessService;
import de.metas.user.UserGroupId;
import de.metas.user.UserId;
import de.metas.util.Check;
import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.FillMandatoryException;
import org.compiere.SpringContextHolder;

import java.util.List;
import java.util.Set;

/*
 * #%L
 * de.metas.adempiere.adempiere.base
 * %%
 * Copyright (C) 2019 metas GmbH
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

abstract class UserGroupRecordAccess_Base extends JavaProcess implements IProcessPrecondition
{
	private final RecordAccessService userGroupRecordAccessService = SpringContextHolder.instance.getBean(RecordAccessService.class);

	@Param(parameterName = "PrincipalType", mandatory = true)
	private String principalTypeCode;

	@Param(parameterName = "AD_User_ID", mandatory = false)
	private UserId userId;

	@Param(parameterName = "AD_UserGroup_ID", mandatory = false)
	private UserGroupId userGroupId;

	private static final String PARAM_PermissionCode = "Access";
	@Param(parameterName = PARAM_PermissionCode, mandatory = false)
	private String permissionCode;

	@Override
	public final ProcessPreconditionsResolution checkPreconditionsApplicable(final IProcessPreconditionsContext context)
	{
		if (context.isNoSelection())
		{
			return ProcessPreconditionsResolution.rejectBecauseNoSelection().toInternal();
		}

		return ProcessPreconditionsResolution.accept();
	}

	protected final void grantAccessToRecord()
	{
		userGroupRecordAccessService.grantAccess(RecordAccessGrantRequest.builder()
				.recordRef(getRecordRef())
				.principal(getPrincipal())
				.permissions(getPermissionsToGrant())
				.issuer(PermissionIssuer.MANUAL)
				.requestedBy(getUserId())
				.build());
	}

	protected final void revokeAccessFromRecord()
	{
		final boolean revokeAllPermissions;
		final List<Access> permissionsToRevoke;
		final Access permission = getPermissionOrNull();
		if (permission == null)
		{
			revokeAllPermissions = true;
			permissionsToRevoke = ImmutableList.of();
		}
		else
		{
			revokeAllPermissions = false;
			permissionsToRevoke = ImmutableList.of(permission);
		}

		userGroupRecordAccessService.revokeAccess(RecordAccessRevokeRequest.builder()
				.recordRef(getRecordRef())
				.principal(getPrincipal())
				.revokeAllPermissions(revokeAllPermissions)
				.permissions(permissionsToRevoke)
				.issuer(PermissionIssuer.MANUAL)
				.requestedBy(getUserId())
				.build());
	}

	private Principal getPrincipal()
	{
		final PrincipalType principalType = PrincipalType.ofCode(principalTypeCode);
		if (PrincipalType.USER.equals(principalType))
		{
			return Principal.userId(userId);
		}
		else if (PrincipalType.USER_GROUP.equals(principalType))
		{
			return Principal.userGroupId(userGroupId);
		}
		else
		{
			throw new AdempiereException("@Unknown@ @PrincipalType@: " + principalType);
		}
	}

	private Set<Access> getPermissionsToGrant()
	{
		final Access permission = getPermissionOrNull();
		if (permission == null)
		{
			throw new FillMandatoryException(PARAM_PermissionCode);
		}

		if (Access.WRITE.equals(permission))
		{
			return ImmutableSet.of(Access.READ, Access.WRITE);
		}
		else
		{
			return ImmutableSet.of(permission);
		}
	}

	private Access getPermissionOrNull()
	{
		if (Check.isEmpty(permissionCode))
		{
			return null;
		}
		else
		{
			return Access.ofCode(permissionCode);
		}
	}
}
