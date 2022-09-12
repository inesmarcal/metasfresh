package org.compiere.model;

import de.metas.ad_reference.ADRefList;
import de.metas.ad_reference.ADReferenceService;
import de.metas.ad_reference.ReferenceId;
import de.metas.cache.model.IModelCacheInvalidationService;
import de.metas.cache.model.ModelCacheInvalidationTiming;
import de.metas.cache.model.POCacheSourceModel;
import de.metas.document.sequence.IDocumentNoBL;
import de.metas.document.sequence.IDocumentNoBuilderFactory;
import de.metas.util.Services;
import lombok.NonNull;
import org.adempiere.ad.migration.logger.IMigrationLogger;
import org.adempiere.ad.service.IDeveloperModeBL;
import org.adempiere.ad.session.ChangeLogRecord;
import org.adempiere.ad.session.ISessionBL;
import org.adempiere.ad.session.ISessionDAO;
import org.adempiere.ad.session.MFSession;
import org.adempiere.ad.trx.api.ITrxManager;
import org.adempiere.service.ISysConfigBL;

import java.util.List;

final class POServicesFacade
{
	private IDeveloperModeBL _developerModeBL;
	private ISysConfigBL _sysConfigBL;
	private ISessionBL _sessionBL;
	private ISessionDAO _sessionDAO;
	private IMigrationLogger _migrationLogger;
	private IModelCacheInvalidationService _cacheInvalidationService;
	private IDocumentNoBuilderFactory _documentNoBuilderFactory;
	private IDocumentNoBL _documentNoBL;
	private ITrxManager _trxManager;
	private ADReferenceService _adReferenceService;

	private IDeveloperModeBL developerModeBL()
	{
		IDeveloperModeBL developerModeBL = this._developerModeBL;
		if (developerModeBL == null)
		{
			developerModeBL = this._developerModeBL = Services.get(IDeveloperModeBL.class);
		}
		return developerModeBL;
	}

	private ISysConfigBL sysConfigBL()
	{
		ISysConfigBL sysConfigBL = this._sysConfigBL;
		if (sysConfigBL == null)
		{
			sysConfigBL = this._sysConfigBL = Services.get(ISysConfigBL.class);
		}
		return sysConfigBL;
	}

	public ISessionBL sessionBL()
	{
		ISessionBL sessionBL = this._sessionBL;
		if (sessionBL == null)
		{
			sessionBL = this._sessionBL = Services.get(ISessionBL.class);
		}
		return sessionBL;
	}

	private ISessionDAO sessionDAO()
	{
		ISessionDAO sessionDAO = this._sessionDAO;
		if (sessionDAO == null)
		{
			sessionDAO = this._sessionDAO = Services.get(ISessionDAO.class);
		}
		return sessionDAO;
	}

	private IMigrationLogger migrationLogger()
	{
		IMigrationLogger migrationLogger = this._migrationLogger;
		if (migrationLogger == null)
		{
			migrationLogger = this._migrationLogger = Services.get(IMigrationLogger.class);
		}
		return migrationLogger;
	}

	public IModelCacheInvalidationService cacheInvalidationService()
	{
		IModelCacheInvalidationService cacheInvalidationService = this._cacheInvalidationService;
		if (cacheInvalidationService == null)
		{
			cacheInvalidationService = this._cacheInvalidationService = Services.get(IModelCacheInvalidationService.class);
		}
		return cacheInvalidationService;
	}

	public IDocumentNoBuilderFactory documentNoBuilderFactory()
	{
		IDocumentNoBuilderFactory documentNoBuilderFactory = this._documentNoBuilderFactory;
		if (documentNoBuilderFactory == null)
		{
			documentNoBuilderFactory = this._documentNoBuilderFactory = Services.get(IDocumentNoBuilderFactory.class);
		}
		return documentNoBuilderFactory;
	}

	private IDocumentNoBL documentNoBL()
	{
		IDocumentNoBL documentNoBL = this._documentNoBL;
		if (documentNoBL == null)
		{
			documentNoBL = this._documentNoBL = Services.get(IDocumentNoBL.class);
		}
		return documentNoBL;
	}

	public ITrxManager trxManager()
	{
		ITrxManager trxManager = this._trxManager;
		if (trxManager == null)
		{
			trxManager = this._trxManager = Services.get(ITrxManager.class);
		}
		return trxManager;
	}

	private ADReferenceService adReferenceService()
	{
		ADReferenceService adReferenceService = this._adReferenceService;
		if (adReferenceService == null)
		{
			adReferenceService = this._adReferenceService = ADReferenceService.get();
		}
		return adReferenceService;
	}

	//
	//
	//

	public boolean isDeveloperMode()
	{
		return developerModeBL().isEnabled();
	}

	public boolean getSysConfigBooleanValue(final String sysConfigName, final boolean defaultValue, final int ad_client_id, final int ad_org_id)
	{
		return sysConfigBL().getBooleanValue(sysConfigName, defaultValue, ad_client_id, ad_org_id);
	}

	public boolean isChangeLogEnabled()
	{
		return sessionBL().isChangeLogEnabled();
	}

	public String getInsertChangeLogType(final int adClientId)
	{
		return sysConfigBL().getValue("SYSTEM_INSERT_CHANGELOG", "N", adClientId);
	}

	public void saveChangeLogs(final List<ChangeLogRecord> changeLogRecords)
	{
		sessionDAO().saveChangeLogs(changeLogRecords);
	}

	public void logMigration(final MFSession session, final PO po, final POInfo poInfo, final String actionType)
	{
		migrationLogger().logMigration(session, po, poInfo, actionType);
	}

	public void invalidateForModel(final POCacheSourceModel model, final ModelCacheInvalidationTiming timing)
	{
		cacheInvalidationService().invalidateForModel(model, timing);
	}

	public void fireDocumentNoChange(final PO po, final String value)
	{
		documentNoBL().fireDocumentNoChange(po, value); // task 09776
	}

	public ADRefList getRefListById(@NonNull final ReferenceId referenceId)
	{
		return adReferenceService().getRefListById(referenceId);
	}

}
