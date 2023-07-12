package de.metas.acct.interceptor;

import de.metas.acct.api.IFactAcctDAO;
import de.metas.acct.doc.AcctDocRegistry;
import de.metas.logging.LogManager;
import de.metas.util.Services;
import lombok.NonNull;
import lombok.ToString;
import org.adempiere.ad.modelvalidator.AbstractModelInterceptor;
import org.adempiere.ad.modelvalidator.IModelInterceptorRegistry;
import org.adempiere.ad.modelvalidator.IModelValidationEngine;
import org.adempiere.ad.modelvalidator.ModelChangeType;
import org.adempiere.model.InterfaceWrapperHelper;
import org.adempiere.util.lang.impl.TableRecordReference;
import org.compiere.model.I_AD_Client;
import org.compiere.model.I_Fact_Acct;
import org.compiere.model.POInfo;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedHashSet;

@Component
public class POReference
{
	// services
	private static final Logger logger = LogManager.getLogger(POReference.class);
	private final IModelInterceptorRegistry modelInterceptorRegistry = Services.get(IModelInterceptorRegistry.class);
	private final IFactAcctDAO factAcctDAO = Services.get(IFactAcctDAO.class);
	private final AcctDocRegistry acctDocRegistry;

	private static final String COLUMNNAME_POReference = I_Fact_Acct.COLUMNNAME_POReference;

	public POReference(final AcctDocRegistry acctDocRegistry) {this.acctDocRegistry = acctDocRegistry;}

	@PostConstruct
	public void postConstruct()
	{
		final LinkedHashSet<String> registeredOnTableNames = new LinkedHashSet<>();
		for (final String tableName : acctDocRegistry.getDocTableNames())
		{
			if (POInfo.getPOInfoNotNull(tableName).hasColumnName(COLUMNNAME_POReference))
			{
				modelInterceptorRegistry.addModelInterceptor(new POReferenceInterceptor(factAcctDAO, tableName));
				registeredOnTableNames.add(tableName);
			}
		}

		logger.info("Watching POReference changes on {}", registeredOnTableNames);
	}

	//
	//
	//

	@ToString(of = "tableName")
	private static class POReferenceInterceptor extends AbstractModelInterceptor
	{
		private final IFactAcctDAO factAcctDAO;
		private final String tableName;

		private POReferenceInterceptor(
				@NonNull final IFactAcctDAO factAcctDAO,
				@NonNull final String tableName)
		{
			this.factAcctDAO = factAcctDAO;
			this.tableName = tableName;
		}

		@Override
		protected void onInit(final IModelValidationEngine engine, final I_AD_Client client)
		{
			engine.addModelChange(tableName, this);
		}

		@Override
		public void onModelChange(final Object model, final ModelChangeType changeType)
		{
			if (changeType.isAfter() && changeType.isChange()
					&& InterfaceWrapperHelper.isValueChanged(model, COLUMNNAME_POReference))
			{
				final TableRecordReference recordRef = TableRecordReference.of(model);
				final String poReference = InterfaceWrapperHelper.getValueOptional(model, COLUMNNAME_POReference)
						.map(Object::toString)
						.orElse(null);

				factAcctDAO.updatePOReference(recordRef, poReference);
			}
		}
	}
}
