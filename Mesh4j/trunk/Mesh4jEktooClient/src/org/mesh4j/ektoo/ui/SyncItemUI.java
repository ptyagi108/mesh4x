package org.mesh4j.ektoo.ui;

import static org.mesh4j.ektoo.ui.settings.prop.AppPropertiesProvider.getFilePath;
import static org.mesh4j.ektoo.ui.settings.prop.AppPropertiesProvider.getProperty;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.mesh4j.ektoo.IUIController;
import org.mesh4j.ektoo.controller.AbstractUIController;
import org.mesh4j.ektoo.controller.CloudUIController;
import org.mesh4j.ektoo.controller.FeedUIController;
import org.mesh4j.ektoo.controller.FolderUIController;
import org.mesh4j.ektoo.controller.GSSheetUIController;
import org.mesh4j.ektoo.controller.KmlUIController;
import org.mesh4j.ektoo.controller.MsAccessUIController;
import org.mesh4j.ektoo.controller.MsExcelUIController;
import org.mesh4j.ektoo.controller.MySQLUIController;
import org.mesh4j.ektoo.controller.PFIFUIController;
import org.mesh4j.ektoo.controller.ZipFeedUIController;
import org.mesh4j.ektoo.model.CloudModel;
import org.mesh4j.ektoo.model.FeedModel;
import org.mesh4j.ektoo.model.FolderModel;
import org.mesh4j.ektoo.model.GSSheetModel;
import org.mesh4j.ektoo.model.KmlModel;
import org.mesh4j.ektoo.model.MsAccessModel;
import org.mesh4j.ektoo.model.MsExcelModel;
import org.mesh4j.ektoo.model.MySQLAdapterModel;
import org.mesh4j.ektoo.model.PFIFModel;
import org.mesh4j.ektoo.model.ZipFeedModel;
import org.mesh4j.ektoo.ui.component.RoundBorder;
import org.mesh4j.ektoo.ui.component.messagedialog.MessageDialog;
import org.mesh4j.ektoo.ui.settings.prop.AppProperties;
import org.mesh4j.ektoo.ui.translator.EktooUITranslator;
import org.mesh4j.sync.ISyncAdapter;
import org.mesh4j.sync.adapters.feed.ISyndicationFormat;
import org.mesh4j.sync.adapters.feed.atom.AtomSyndicationFormat;
import org.mesh4j.sync.adapters.feed.pfif.PfifUtil;
import org.mesh4j.sync.adapters.feed.rss.RssSyndicationFormat;
import org.mesh4j.sync.payload.schema.rdf.IRDFSchema;

public class SyncItemUI extends JPanel implements IUIController {

	private static final long serialVersionUID = 8681801062827267140L;
	
	private final static String DYMMY_PANEL = "DUMMY_PANEL";
	public final static String KML_PANEL = "KML";
	public final static String MS_EXCEL_PANEL = "MS Excel";
	public final static String GOOGLE_SPREADSHEET_PANEL = "Google Spreadsheet";
	public final static String MS_ACCESS_PANEL = "MS Access";
	public final static String ZIP_FILE_PANEL = "Zip feeds";
	public final static String CLOUD_PANEL = "Cloud";
	public final static String MYSQL_PANEL = "MySQL";
	public final static String RSS_FILE_PANEL = "Rss 2.0";
	public final static String ATOM_FILE_PANEL = "Atom 1.0";
	public final static String FOLDER_PANEL = "Folder";
	public final static String PFIF_PANEL = "Pfif 1.2";

	public static final String UI_AS_SOURCE = "source";
	public static final String UI_AS_TARGET = "target";
	
	// MODEL VARIABLES
	

	private JPanel body = null;
	private JPanel head = null;
	private JPanel firstPanel = new JPanel();

	private MsExcelUI excelUI = null;
	private MsExcelUIController excelUIController = null;

	private MsAccessUI accessUI = null;
	private MsAccessUIController accessUIController = null;
	
	private GSSheetUI googleUI = null;
	private GSSheetUIController googleUIControler = null;

	private KmlUI kmlUI = null;
	private KmlUIController kmlUIControler = null;

	private CloudUI cloudUI = null;
	private CloudUIController cloudUIControler = null;

	private MySQLUI mysqlUI = null;
	private MySQLUIController mysqlUIControler = null;

	private FeedUI rssUI = null;
	private FeedUIController rssUIControler = null;
	
	private ZipFeedUI zipRssUI = null;
	private ZipFeedUIController zipRssUIControler = null;

	private FeedUI atomUI = null;
	private FeedUIController atomUIControler = null;
	
	
	private PfifFeedUI pfifUI = null;
	private PFIFUIController pfifUIControler = null;

	private FolderUI folderUI = null;
	private FolderUIController folderUIController = null;
	
	private String SourceOrTargetType = null;

	private JComboBox listType = null;
	private JLabel labelType = null;
	
	private String title = null;
	private boolean acceptsCreateDataset;
	private String uiType = null;
	

	// BUSINESS MODEL
	public SyncItemUI(String title, boolean acceptsCreateDataset, String uiType) {
		this.title = title;
		this.acceptsCreateDataset = acceptsCreateDataset;
		this.uiType = uiType;
		initialize();
	}

	private void initialize() {
	
		SourceOrTargetType = EktooUITranslator.getDataSourceType();
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder( new RoundBorder(Color.LIGHT_GRAY), this.title));

		setBackground(Color.WHITE);
		add(getHeadPane(), BorderLayout.NORTH);
		add(getBodyPane(), BorderLayout.CENTER);
		updateLayout((String) getListType().getSelectedItem());
	}
	
	private JPanel getHeadPane() {
		if (head == null) {
			head = new JPanel();
			head.setLayout(null);
			head.setPreferredSize(new Dimension(390, 32));
			head.add(getTypeLabel());
			head.add(getDataSourceType());
		}
		return head;
	}

	private JPanel getBodyPane() {
		if (body == null) {
			body = new JPanel();
			body.setLayout(new CardLayout());

			firstPanel.setBackground(Color.WHITE);
			body.add(firstPanel, DYMMY_PANEL);

			// add cards here
			body.add(getMsExcelUI(), MS_EXCEL_PANEL);
			body.add(getMsAccessUI(), MS_ACCESS_PANEL);
			body.add(getGSSheetUI(), GOOGLE_SPREADSHEET_PANEL);
			body.add(getKmlUI(), KML_PANEL);
			body.add(getCloudUI(), CLOUD_PANEL);
			body.add(getMySQLUI(), MYSQL_PANEL);
			body.add(getRSSFileUI(), RSS_FILE_PANEL);
			body.add(getZipRSSFileUI(), ZIP_FILE_PANEL);
			body.add(getAtomFileUI(), ATOM_FILE_PANEL);
			body.add(getFolderUI(), FOLDER_PANEL);
			body.add(getPfifUI(), PFIF_PANEL);
		}
		return body;
	}
	
	private JComboBox getDataSourceType() {
		if (getListType() == null) {
			setListType(new JComboBox());
			getListType().setBounds(new Rectangle(100, 5, 230, 22));
			getListType().setPreferredSize(new Dimension(200, 22));

			if (SourceOrTargetType != null) {
				StringTokenizer st = new StringTokenizer(SourceOrTargetType,
						"|");
				String type = null;
				while (st.hasMoreTokens()) {
					type = st.nextToken();
					if (type != null && type.length() != 0)
						getListType().addItem(type);
				}
			}
			getListType().addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					if (evt.getStateChange() == ItemEvent.SELECTED) {
						int index = getListType().getSelectedIndex();
						if (index != -1)
							updateLayout((String) evt.getItem());
					}
				}
			});

		}
		return getListType();
	}

	private JLabel getTypeLabel() {
		if (labelType == null) {
			labelType = new JLabel();
			labelType.setText(EktooUITranslator.getSyncDataSourceType());
			labelType.setBounds(new Rectangle(5, 11, 116, 16));
		}
		return labelType;
	}

	private MsExcelUI getMsExcelUI() {
		if (excelUI == null) {
			excelUIController = new MsExcelUIController(this.acceptsCreateDataset);
			String fileName = getFileName(MS_EXCEL_PANEL);
			excelUIController.addModel(new MsExcelModel(fileName));
			excelUI = new MsExcelUI(fileName, excelUIController);
		}
		return excelUI;
	}

	private MsAccessUI getMsAccessUI() {
		if (accessUI == null) {
			String fileName = getFileName(MS_ACCESS_PANEL);
			accessUI = new MsAccessUI(fileName, getMsAccessUIController());
		}
		return accessUI;
	}

	private MsAccessUIController getMsAccessUIController() {
		if(accessUIController == null){
			accessUIController = new MsAccessUIController(this.acceptsCreateDataset);
			String fileName = getFileName(MS_ACCESS_PANEL);
			accessUIController.addModel(new MsAccessModel(fileName));
		}
		return accessUIController;
	}

	private GSSheetUI getGSSheetUI() {
		if (googleUI == null) {
			googleUIControler = new GSSheetUIController(this.acceptsCreateDataset);
			googleUIControler.addModel(new GSSheetModel());

			googleUI = new GSSheetUI(googleUIControler, getProperty(AppProperties.URL_GOOGLE_DOCS));
			googleUI.setUserLabel(EktooUITranslator.getGoogleUserLabel());
			googleUI.setPasswordLabel(EktooUITranslator.getGooglePasswordLabel());
			googleUI.setNameLabel(EktooUITranslator.getGoogleSpreadsheetNameLabel());
			googleUI.WorksheetLabel(EktooUITranslator.getGoogleWorksheetLabel());
			googleUI.setUniqueColumnLabel(EktooUITranslator
					.getUniqueColumnNameLabel());
		}
		return googleUI;
	}

	private KmlUI getKmlUI() {
		if (kmlUI == null) {
			kmlUIControler = new KmlUIController(this.acceptsCreateDataset);
			String fileName = getFileName(KML_PANEL);
			kmlUIControler.addModel(new KmlModel(fileName));
			kmlUI = new KmlUI(fileName, kmlUIControler);
		}
		return kmlUI;
	}

	private CloudUI getCloudUI() {
		if (cloudUI == null) {
			cloudUIControler = new CloudUIController(this.acceptsCreateDataset);
			cloudUIControler.addModel(new CloudModel(getProperty(AppProperties.CLOUD_ROOT_URI)));
			cloudUI = new CloudUI(getProperty(AppProperties.CLOUD_ROOT_URI), cloudUIControler);
		}
		return cloudUI;
	}

	private MySQLUI getMySQLUI() {
		if (mysqlUI == null) {
			mysqlUIControler = new MySQLUIController(this.acceptsCreateDataset);
			mysqlUIControler.addModel(new MySQLAdapterModel());
			mysqlUI = new MySQLUI(mysqlUIControler);
		}
		return mysqlUI;
	}

	private FeedUI getRSSFileUI() {
		if (rssUI == null) {
			rssUIControler = new FeedUIController(this.acceptsCreateDataset);
			String fileName = getFileName(RSS_FILE_PANEL);
			rssUIControler.addModel(
				new FeedModel(
					fileName,
					RssSyndicationFormat.INSTANCE,
					getProperty(AppProperties.CLOUD_ROOT_URI)));
			rssUI = new FeedUI(fileName, rssUIControler);
		}
		return rssUI;
	}

	private ZipFeedUI getZipRSSFileUI() {
		if (zipRssUI == null) {
			zipRssUIControler = new ZipFeedUIController(this.acceptsCreateDataset);
			String fileName = getFilePath(AppProperties.PATH_SOURCE_ZIP);
			zipRssUIControler.addModel(new ZipFeedModel(fileName));
			zipRssUI = new ZipFeedUI(fileName, zipRssUIControler);
		}
		return zipRssUI;
	}
	
	private FeedUI getAtomFileUI() {
		if (atomUI == null) {
			atomUIControler = new FeedUIController(this.acceptsCreateDataset);
			String fileName = getFileName(ATOM_FILE_PANEL);
			atomUIControler.addModel(
				new FeedModel(
						fileName,
						AtomSyndicationFormat.INSTANCE,
						getProperty(AppProperties.CLOUD_ROOT_URI)));
			atomUI = new FeedUI(fileName, atomUIControler);
		}
		return atomUI;
	}
	
	private FolderUI getFolderUI() {
		if (folderUI == null) {
			folderUIController = new FolderUIController(this.acceptsCreateDataset);
			String fileName = getFileName(FOLDER_PANEL);
			folderUIController.addModel(new FolderModel(fileName));
			folderUI = new FolderUI(fileName, folderUIController);
		}
		return folderUI;
	}

	private PfifFeedUI getPfifUI(){
		if (pfifUI == null) {
			pfifUIControler = new PFIFUIController(this.acceptsCreateDataset);
			String fileName = getFileName(PFIF_PANEL);
			ISyndicationFormat syndicationFormat = null;
			File file = new File(fileName);
			if(file.exists()){
				syndicationFormat = PfifUtil.getPfifSyndicationFormat(file);
			} else {
				syndicationFormat = RssSyndicationFormat.INSTANCE;
			}
			
			pfifUIControler.addModel(
				new PFIFModel(
						fileName,
						syndicationFormat,
						getProperty(AppProperties.CLOUD_ROOT_URI)));
			pfifUI = new PfifFeedUI(fileName, pfifUIControler);
		}
		return pfifUI;
	}
	
	
	private String getFileName(String dbSrcName){
		 
		 String fileName = "";
			if(this.acceptsCreateDataset){
				if(dbSrcName.equals(MS_EXCEL_PANEL)){
					fileName = getFilePath(AppProperties.PATH_TARGET_EXCEL);
					
				} else if(dbSrcName.equals(MS_ACCESS_PANEL)){
					fileName = getFilePath(AppProperties.PATH_TARGET_ACCESS);
					
				} else if(dbSrcName.equals(RSS_FILE_PANEL)){
					fileName = getFilePath(AppProperties.PATH_TARGET_RSS);
					
				} else if(dbSrcName.equals(ATOM_FILE_PANEL)){
					fileName = getFilePath(AppProperties.PATH_TARGET_ATOM);
					
				} else if(dbSrcName.equals(KML_PANEL)){
					fileName = getFilePath(AppProperties.PATH_TARGET_KML);
					
				} else if(dbSrcName.equals(FOLDER_PANEL)){
					fileName = getFilePath(AppProperties.PATH_TARGET_FOLDER);
					
				} else if(dbSrcName.equals(PFIF_PANEL)){
					fileName = getFilePath(AppProperties.PATH_TARGET_PFIF);
				} 
			} else {
				if(dbSrcName.equals(MS_EXCEL_PANEL)){
					fileName = getFilePath(AppProperties.PATH_SOURCE_EXCEL);
					
				} else if(dbSrcName.equals(MS_ACCESS_PANEL)){
					fileName = getFilePath(AppProperties.PATH_SOURCE_ACCESS);
					
				} else if(dbSrcName.equals(RSS_FILE_PANEL)){
					fileName = getFilePath(AppProperties.PATH_SOURCE_RSS);
					
				} else if(dbSrcName.equals(ATOM_FILE_PANEL)){
					fileName = getFilePath(AppProperties.PATH_SOURCE_ATOM);
					
				} else if(dbSrcName.equals(KML_PANEL)){
					fileName = getFilePath(AppProperties.PATH_SOURCE_KML);
					
				} else if(dbSrcName.equals(FOLDER_PANEL)){
					fileName = getFilePath(AppProperties.PATH_SOURCE_FOLDER);
					
				} else if(dbSrcName.equals(PFIF_PANEL)){
					fileName = getFilePath(AppProperties.PATH_SOURCE_PFIF);
				} 
			}
			return fileName;
	 }
	
	
	private void updateLayout(String item) {
		CardLayout cl = (CardLayout) (body.getLayout());

		if (item.equals(MS_EXCEL_PANEL)) {
			if(EktooFrame.multiModeSync){				
				getMsExcelUI().getlabelTable().setVisible(false);
				getMsExcelUI().getTableList().setVisible(false);
				getMsExcelUI().getLabelColumn().setVisible(false);
				getMsExcelUI().getColumnList().setVisible(false);
			}else{
				getMsExcelUI().getlabelTable().setVisible(true);
				getMsExcelUI().getTableList().setVisible(true);
				getMsExcelUI().getLabelColumn().setVisible(true);
				getMsExcelUI().getColumnList().setVisible(true);
			}
			cl.show(body, MS_EXCEL_PANEL);
		} else if (item.equals(MS_ACCESS_PANEL)) {
			cl.show(body, MS_ACCESS_PANEL);
		} else if (item.equals(GOOGLE_SPREADSHEET_PANEL)) {
			cl.show(body, GOOGLE_SPREADSHEET_PANEL);
		} else if (item.equals(KML_PANEL)) {
			cl.show(body, KML_PANEL);
		} else if (item.equals(CLOUD_PANEL)) {
			if(EktooFrame.multiModeSync){				
				getCloudUI().getDataSetLabel().setVisible(false);
				getCloudUI().getDataSetText().setVisible(false);
				getCloudUI().getSchemaViewButton().setVisible(false);
			}else{
				getCloudUI().getDataSetLabel().setVisible(true);
				getCloudUI().getDataSetText().setVisible(true);
				getCloudUI().getSchemaViewButton().setVisible(true);
			}			
			cl.show(body, CLOUD_PANEL);
		} else if (item.equals(MYSQL_PANEL)) {
			cl.show(body, MYSQL_PANEL);
		} else if (item.equals(RSS_FILE_PANEL)) {
			cl.show(body, RSS_FILE_PANEL);
		} else if (item.equals(ATOM_FILE_PANEL)) {
			cl.show(body, ATOM_FILE_PANEL);
		} else if (item.equals(FOLDER_PANEL)) {
			cl.show(body, FOLDER_PANEL);	
		} else if (item.equals(ZIP_FILE_PANEL)) {
			cl.show(body, ZIP_FILE_PANEL);
		} else if (item.equals(PFIF_PANEL)) {
			cl.show(body, PFIF_PANEL);
		}else {
			cl.show(body, DYMMY_PANEL);
		}
	}

	public void showInitCard() {
		CardLayout cl = (CardLayout) (body.getLayout());
		cl.show(body, DYMMY_PANEL);
	}

	public void setListType(JComboBox listType) {
		this.listType = listType;
	}

	public JComboBox getListType() {
		return listType;
	}

	@Override
	public ISyncAdapter createAdapter() {
		AbstractUIController uiController = getCurrentController();
		return uiController.createAdapter();
	}

	@Override
	public List<IRDFSchema> fetchSchema(ISyncAdapter adapter) {
		AbstractUIController uiController = getCurrentController();
		return uiController.fetchSchema(adapter);
	}
	
	@Override
	public ISyncAdapter createAdapter(List<IRDFSchema> schemas) {
		AbstractUIController uiController = getCurrentController();
		return uiController.createAdapter(schemas);
	}
	
	public String toString() {
		return getCurrentController().toString();
	}

	// TODO (NBL)improve this section
	public AbstractUIController getCurrentController() {
		AbstractUIController currrentController = null;

		String item = (String) getDataSourceType().getSelectedItem();

		if (item.equals(KML_PANEL)) {
			currrentController = kmlUI.getController();
		} else if (item.equals(MS_EXCEL_PANEL)) {
			currrentController = excelUI.getController();
		} else if (item.equals(MS_ACCESS_PANEL)) {
			currrentController = accessUI.getController();
		} else if (item.equals(GOOGLE_SPREADSHEET_PANEL)) {
			currrentController = googleUI.getController();
		} else if (item.equals(CLOUD_PANEL)) {
			currrentController = cloudUI.getController();
		} else if (item.equals(MYSQL_PANEL)) {
			currrentController = mysqlUI.getController();
		} else if (item.equals(RSS_FILE_PANEL)) {
			currrentController = rssUI.getController();
		} else if (item.equals(ATOM_FILE_PANEL)) {
			currrentController = atomUI.getController();
		} else if (item.equals(FOLDER_PANEL)) {
			currrentController = folderUI.getController();
		} else if (item.equals(ZIP_FILE_PANEL)) {
			currrentController = zipRssUI.getController();
		} else if (item.equals(PFIF_PANEL)) {
			currrentController = pfifUI.getController();
		}

		return currrentController;
	}
	
	public Set<AbstractUIController> getAllController(){
		Set<AbstractUIController> listOfAll = new LinkedHashSet<AbstractUIController>();
		listOfAll.add(kmlUI.getController());
		listOfAll.add(excelUI.getController());
		listOfAll.add(accessUI.getController());
		listOfAll.add(googleUI.getController());
		listOfAll.add(cloudUI.getController());
		listOfAll.add(mysqlUI.getController());
		listOfAll.add(rssUI.getController());
		listOfAll.add(atomUI.getController());
		listOfAll.add(folderUI.getController());
		listOfAll.add(zipRssUI.getController());
		return listOfAll;
	}
	
	public AbstractUI getCurrentView(){
		AbstractUI currentUI = null; 
		String type = (String) getDataSourceType().getSelectedItem();
		if (type.equals(KML_PANEL)) {
			currentUI = kmlUI;
		} else if (type.equals(MS_EXCEL_PANEL)) {
			currentUI = excelUI;
		} else if (type.equals(MS_ACCESS_PANEL)) {
			currentUI = accessUI;
		} else if (type.equals(GOOGLE_SPREADSHEET_PANEL)) {
			currentUI = googleUI;
		} else if (type.equals(CLOUD_PANEL)) {
			currentUI = cloudUI;
		} else if (type.equals(MYSQL_PANEL)) {
			currentUI = mysqlUI;
		} else if (type.equals(RSS_FILE_PANEL)) {
			currentUI = rssUI;
		} else if (type.equals(ATOM_FILE_PANEL)) {
			currentUI = atomUI;
		} else if (type.equals(FOLDER_PANEL)) {
			currentUI = folderUI;
		} else if (type.equals(ZIP_FILE_PANEL)) {
			currentUI = zipRssUI;
		} else if (type.equals(PFIF_PANEL)) {
			currentUI = pfifUI;
		}
		return currentUI;
	}
	
	public String getTargetFilePath(){
		return this.excelUI.getFilePath();
	}
	public boolean verify(){
		return getCurrentView().verify();
	}

	public void openErrorPopUp(Hashtable<Object, String> errorTable) {
		Object key = null;
		StringBuffer err = new StringBuffer();
		Enumeration<Object> keys = errorTable.keys();
		while (keys.hasMoreElements()) {
			key = keys.nextElement(); 
			err.append(errorTable.get(key) + "\n");
		}		
		this.openErrorPopUp(err.toString());
	}
	
	public void openErrorPopUp(String error) {
		MessageDialog.showErrorMessage(JOptionPane.getRootFrame(), this.title, error);
	}

	public void cleanMessaged() {
		getCurrentView().cleanMessages();		
	}

	/**
	 * return whether UI Items available here for source or target
	 * @return
	 */
	public String getUiType() {
		return uiType;
	}

	public boolean isCloudUI() {
		String type = (String) getDataSourceType().getSelectedItem();
		return type.equals(CLOUD_PANEL);
	}
	
	public boolean isMsAccessUI() {
		String type = (String) getDataSourceType().getSelectedItem();
		return type.equals(MS_ACCESS_PANEL);
	}
	
	
}