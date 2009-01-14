package org.mesh4j.sync.ui.tasks;

import java.awt.Cursor;

import javax.swing.SwingWorker;

import org.mesh4j.sync.epiinfo.ui.EpiinfoCompactUI;
import org.mesh4j.sync.mappings.DataSourceMapping;
import org.mesh4j.sync.mappings.EndpointMapping;
import org.mesh4j.sync.utils.SyncEngineUtil;

public class ReadyToSyncTask extends SwingWorker<Void, Void> {

	// MODEL VARIABLEs
	private EpiinfoCompactUI ui;
	
	// BUSINESS METHODS
	public ReadyToSyncTask(EpiinfoCompactUI ui){
		super();
		this.ui = ui;
	}
	
    public Void doInBackground() {
		ui.getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		EndpointMapping endpoint = (EndpointMapping)ui.getComboBoxEndpoint().getSelectedItem();
		DataSourceMapping dataSource = (DataSourceMapping)ui.getComboBoxMappingDataSource().getSelectedItem();
	
		ui.notifyStartReadyToSync(endpoint, dataSource);
		
		String message = makeQuestion(dataSource.getAlias());
		SyncEngineUtil.sendSms(ui.getSyncEngine(), endpoint.getEndpoint(), message);
		return null;
    }

	@Override
    public void done() {
		super.done();
		ui.getFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

	public static String makeAnswer(String dataSourceAlias, boolean isDataSourceAvailable) {
		if(isDataSourceAvailable){
			return "I am ready to sync "+ dataSourceAlias;
		} else {
			return "I am not ready to sync "+ dataSourceAlias;
		}
	}
	
	public static boolean isAnswerOk(String message, String dataSourceAlias) {
		return makeAnswer(dataSourceAlias, true).equals(message);
	}
	
	public static boolean isAnswerNotOk(String message, String dataSourceAlias) {
		return makeAnswer(dataSourceAlias, false).equals(message);
	}
	
	public static String makeQuestion(String dataSourceAlias) {
		return  "Are you ready to sync " + dataSourceAlias + " ?";
	}

	public static boolean isQuestion(String message) {
		return message.startsWith("Are you ready to sync ") &&
			message.endsWith(" ?");
	}
	
	public static String getDataSourceAlias(String message) {
		int start = "Are you ready to sync ".length();
		int end = message.length() - 2;
		return message.substring(start, end);
	}
}
