package org.mesh4j.sync.utils;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.mesh4j.sync.adapters.kml.KmlNames;
import org.mesh4j.sync.adapters.kml.timespan.decorator.IKMLGenerator;
import org.mesh4j.sync.model.Item;
import org.mesh4j.sync.payload.mappings.IMappingResolver;
import org.mesh4j.sync.validations.Guard;
import org.mesh4j.sync.validations.MeshException;

public class KmlGenerator implements IKMLGenerator{

	final static Log LOGGER = LogFactory.getLog(KmlGenerator.class);
	final static SimpleDateFormat DATEONSET_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	// MODEL VARIABLE
	private IMappingResolver mappingResolver;
	private String templateFileName;
	
	// BUSINESS METHODS

	public KmlGenerator(String templateFileName, IMappingResolver mappingResolver){
		Guard.argumentNotNull(mappingResolver, "mappingResolver");
		Guard.argumentNotNullOrEmptyString(templateFileName, "templateFileName");
		
		this.mappingResolver = mappingResolver;
		this.templateFileName = templateFileName;
	}
	
	protected static String makePlacemark(String id, String name, String description, String location, String styleUrl, Date start) {

		return MessageFormat.format(
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
			"<kml xmlns=\"http://earth.google.com/kml/2.2\">"+
			"<Document>"+
			"<Placemark xml:id={0}>"+
			"	<name>{1}</name>"+
			"	<description><![CDATA[{2}]]></description>"+
			"	<styleUrl>{3}</styleUrl>"+
			"	<Point>"+
			"		<coordinates>{4}</coordinates>"+
			"	</Point>"+
			"   <TimeSpan>"+
			"		<begin>{5}</begin>"+
			"	</TimeSpan>"+
			"</Placemark>"+
			"</Document>"+
			"</kml>", id, name, description, styleUrl, location, DateHelper.formatW3CDateTime(start));
	}


	
	// IKMLGenerator methods
	@Override
	public void addElement(Document document, Item item) {
		try{
			if(!item.isDeleted()){
				
				Element payload = item.getContent().getPayload();
				
				//String longitude = mappingResolver.getValue(payload, "//geo.longitude");
				//String latitude = mappingResolver.getValue(payload, "//geo.latitude");
				String location = mappingResolver.getValue(payload, "//geo.location");
				
				if(location != null && location.trim().length() > 0){
					String name = mappingResolver.getValue(payload, "//item.title");
					String description = mappingResolver.getValue(payload, "//item.description");
					
					String ill = mappingResolver.getValue(payload, "//patient.ill");
					String style = "0".equals(ill) ? "#msn_ylw-pushpin0" : "#msn_ylw-pushpin";
					
					Date start = getDateOnSet(item);
					if(start == null){
						start = item.getLastUpdate().getWhen();
					} 
					
					String xml = makePlacemark("'"+item.getSyncId()+"'", name, description, location, style, start);
	
					Element itemElement = DocumentHelper.parseText(xml).getRootElement().element(KmlNames.KML_ELEMENT_DOCUMENT).element(KmlNames.KML_ELEMENT_PLACEMARK); 
					document.getRootElement().element(KmlNames.KML_ELEMENT_DOCUMENT).add(itemElement.createCopy());
				}
			}
		} catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
	}

	@Override
	public Element getElement(Document document, Item item) {
		HashMap<String, String> namespaces = new HashMap<String, String>();
		namespaces.put(KmlNames.KML_PREFIX, KmlNames.KML_URI);
		
		List<Element> items = XMLHelper.selectElements("//*[@xml:id='"+item.getSyncId()+"']", document.getRootElement(), namespaces);
		if(items.isEmpty()){
			return null;
		} else {
			return items.get(items.size()-1);
		}
	}

	@Override
	public boolean hasItemChanged(Document document, Element itemElement, Item item) {
		try{
			String itemIll = mappingResolver.getValue(item.getContent().getPayload(), "//patient.ill");
			//String actualXML = itemElement.element(KmlNames.KML_ELEMENT_DESCRIPTION).getText();
			//String actualItemIll = schemaResolver.getValue(DocumentHelper.parseText(actualXML).getRootElement(), "//patient.ill");
			Element styleUrl = itemElement.element(KmlNames.KML_ELEMENT_STYLE_URL);
			String actualItemIll = "#msn_ylw-pushpin0".equals(styleUrl.getText()) ?  "0" : "1";			
			return !itemIll.equals(actualItemIll);
		} catch(Exception e){
			throw new MeshException(e);
		}
	}

	@Override
	public Document makeDocument(String documentName) {
		try{
			return DocumentHelper.parseText(readTemplateXML(this.templateFileName, documentName));
		} catch(Exception e){
			throw new MeshException(e);
		}
	}
	
	protected static String readTemplateXML(String templateFileName, String documentName) throws Exception {		
		byte[] templateBytes = FileUtils.read(templateFileName);
		String template = new String(templateBytes, "UTF-8");		
		return MessageFormat.format(template, documentName, "");
	}

	@Override
	public String getEndTimeSpan(Item item) throws Exception{
		Date dateOnSet = getDateOnSet(item);
		if(dateOnSet == null){
			return DateHelper.formatW3CDateTime(item.getLastUpdate().getWhen());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateOnSet);
			cal.add(Calendar.SECOND, (-1 * 1));
			dateOnSet = cal.getTime();
			return DateHelper.formatW3CDateTime(dateOnSet);
		}
	}

	private Date getDateOnSet(Item item) throws ParseException {
		Element payload = item.getContent().getPayload();
		String dateOnSet = mappingResolver.getValue(payload, "//patient.dateOnset");
		if(dateOnSet == null || dateOnSet.trim().length() == 0){
			return null;
		} else {
			Date date = DATEONSET_FORMAT.parse(dateOnSet);
			return date;
		}
	}
}
