package org.mesh4j.grameen.training.intro.adapter.googlespreadsheet;

import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mesh4j.grameen.training.intro.adapter.googlespreadsheet.model.GSCell;
import org.mesh4j.grameen.training.intro.adapter.googlespreadsheet.model.GSRow;
import org.mesh4j.grameen.training.intro.adapter.googlespreadsheet.model.GSSpreadsheet;
import org.mesh4j.grameen.training.intro.adapter.googlespreadsheet.model.GSWorksheet;
import org.mesh4j.grameen.training.intro.adapter.googlespreadsheet.model.IGSElement;


import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

public class GoogleSpreadsheetUtilsTests {
	
	private GoogleSpreadsheet gss;
	
	@Before
	public void setUp() throws Exception {
		String spreadsheetFileId = "pvQrTFmc5F8tXD89WRNiBVw";
		
		String username = "sharif.uddin.ku@gmail.com";
		String password = "sharif123";
		this.gss = new GoogleSpreadsheet(spreadsheetFileId,
				username, password);
	}
	

	public void shouldLoadSpreadsheetWhenFileExist()
			throws FileNotFoundException, IOException {		
		SpreadsheetEntry sse = getSampleGoogleSpreadsheet().getSpreadsheet();
		// GoogleSpreadsheetUtils.flush(sse, spreadsheetFileId);
		Assert.assertNotNull(sse);
	}
	
	public void shouldGetRow(){
		SpreadsheetEntry sse = getSampleGoogleSpreadsheet().getSpreadsheet();		
		WorksheetEntry wse = null;
		ListEntry row = null;

		try {
			wse = GoogleSpreadsheetUtils.getWorksheet(gss.getService(), sse, 0);
			row = GoogleSpreadsheetUtils.getRow(gss.getService(), wse, "firstname", "Sharif");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertNotNull(row);
	}
	
	//@Test
	public void shouldGetMJCell() throws IOException, ServiceException {
		SpreadsheetEntry sse = getSampleGoogleSpreadsheet().getSpreadsheet();
		WorksheetEntry wse = GoogleSpreadsheetUtils.getWorksheet(gss
				.getService(), sse, 0);
		Assert.assertNotNull(wse);
		
		int cellRowIndex = 2;
		int cellColIndex = 2;
		
		GSCell gsCell = GoogleSpreadsheetUtils.getGSCell(gss.getService(),
				wse, cellRowIndex, cellColIndex);
		
		Assert.assertNotNull(gsCell);
		Assert.assertEquals(cellColIndex, gsCell.getCellEntry().getCell().getCol());
		Assert.assertEquals(cellRowIndex, gsCell.getCellEntry().getCell().getRow());

		Assert.assertEquals("Sharif", gsCell.getCellEntry().getCell().getValue());
		
		
		Assert.assertNotNull(gsCell.getParentRow());		
		
		Assert.assertEquals(cellRowIndex - 1, gsCell.getParentRow().getRowIndex());
		Assert.assertEquals(4, gsCell.getParentRow().getGsCells().size());
		
		//this cell should be the same as the one contained in the child cell list of its parent at position colIndex  
		Assert.assertEquals(gsCell.getId(), gsCell.getParentRow().getGsCell(cellColIndex).getId());
		
		//get the parent row, pick 2 different child/cell, parent row ID of those two child should be same 
		Assert.assertEquals(gsCell.getParentRow().getGsCell(cellColIndex + 1).getParentRow().getId(),
				gsCell.getParentRow().getGsCell(cellColIndex - 1).getParentRow().getId());
		
		//get the parent row, pick 2 different child/cell, parent row index of those two child should be same 
		Assert.assertEquals(gsCell.getParentRow().getGsCell(cellColIndex+1).getParentRow().getRowIndex(),
				gsCell.getParentRow().getGsCell(cellColIndex - 1).getParentRow().getRowIndex());		
	}		

	//@Test
	public void shouldGetMJRow() throws IOException, ServiceException {
		SpreadsheetEntry sse = getSampleGoogleSpreadsheet().getSpreadsheet();
		WorksheetEntry wse = GoogleSpreadsheetUtils.getWorksheet(gss
				.getService(), sse, 0);
		Assert.assertNotNull(wse);
		
		int rowIndex = 1;
		
		GSRow gsRow = GoogleSpreadsheetUtils.getGSRow(gss.getService(),
				wse, rowIndex);
		
		Assert.assertNotNull(gsRow);
		Assert.assertEquals(rowIndex, gsRow.getRowIndex());
		
		Assert.assertNotNull(gsRow.getGsCells());
		Assert.assertTrue(gsRow.getGsCells().size()>0);
		
/*		for (String tag : mjRow.getRowEntry().getCustomElements().getTags()) {
		      //out.print(entry.getCustomElements().getValue(tag)+"\t");
		      System.out.print(mjRow.getRowEntry().getCustomElements().getValue(tag)+" \t");
		}    		    
		System.out.println("");
*/		    
		/*//row/list's rowIndex will be 1 less than cells rowIndex!
		Assert.assertEquals(rowIndex - 1, mjCell.getParentRow().getRowIndex());
		Assert.assertEquals(4, mjCell.getParentRow().getMjCells().size());
		
		//this cell should be the same as the one contained in the child cell list of its parent at position colIndex  
		Assert.assertEquals(mjCell.getId(), mjCell.getParentRow().getMjCell(colIndex).getId());
		
		//get the parent row, pick 2 different child/cell, parent row ID of those two child should be same 
		Assert.assertEquals(mjCell.getParentRow().getMjCell(colIndex+1).getParentRow().getId(),
				mjCell.getParentRow().getMjCell(colIndex - 1).getParentRow().getId());
		
		//get the parent row, pick 2 different child/cell, parent row index of those two child should be same 
		Assert.assertEquals(mjCell.getParentRow().getMjCell(colIndex+1).getParentRow().getRowIndex(),
				mjCell.getParentRow().getMjCell(colIndex - 1).getParentRow().getRowIndex());		*/
	}		

	@Test
	public void shouldBatchUpdateCells() throws IOException, ServiceException {
		GSSpreadsheet ss = getSampleGoogleSpreadsheet();
		GSWorksheet ws = ss.getGSWorksheet(1); //get the first sheet
		
		Assert.assertNotNull(ws);		
		Assert.assertEquals(ws.getId(), ss.getChildEntry("1").getId()); //get the first sheet from another method and check if they are equal
		
		GSCell gsCell_1 = ws.getGSCell(2, 1);
		gsCell_1.getCellEntry().changeInputValueLocal("GSL-A21");		
		GoogleSpreadsheetUtils.prepareCellForBatchUpdate(gsCell_1);
		
		GSCell gsCell_2 = ws.getGSCell(3, 1);
		gsCell_2.getCellEntry().changeInputValueLocal("GSL-A21");
		GoogleSpreadsheetUtils.prepareCellForBatchUpdate(gsCell_2);
		
		GoogleSpreadsheetUtils.flush(gss.getService(), ss);		
	}		
	
	
/*	
	@Test //TODO: Need to review the code for dirty checking, avoid adding duplicate cell entry   
	public void shouldBatchUpdateRows() throws IOException, ServiceException {
		SpreadsheetEntry sse = getSampleGoogleSpreadsheet();
		WorksheetEntry wse = GoogleSpreadsheetUtils.getWorksheet(gss
				.getService(), sse, 0);
		Assert.assertNotNull(wse);
		
		GSRow mjRow_1 = GoogleSpreadsheetUtils.getGSRow(gss.getService(),
				wse, 1);

		mjRow_1.getGsCell(1).getCellEntry().changeInputValueLocal("GSL-A219");
		mjRow_1.getGsCell(1).setDirty();
		
		BatchUtils.setBatchId(mjRow_1.getGsCell(1).getCellEntry(), mjRow_1.getGsCell(1).getCellEntry().getId());
		BatchUtils.setBatchOperationType(mjRow_1.getGsCell(1).getCellEntry(),
				BatchOperationType.UPDATE);
		
		mjRow_1.setDirty();
		gss.addEntryToUpdate(mjRow_1);
	
		GSRow gsRow_2 = GoogleSpreadsheetUtils.getGSRow(gss.getService(),
				wse, 2);
		gsRow_2.getGsCell(1).getCellEntry().changeInputValueLocal("GSL-A218");
		gsRow_2.getGsCell(1).setDirty();
		
		BatchUtils.setBatchId(gsRow_2.getGsCell(1).getCellEntry(), gsRow_2.getGsCell(1).getCellEntry().getId());
		BatchUtils.setBatchOperationType(gsRow_2.getGsCell(1).getCellEntry(),
				BatchOperationType.UPDATE);
		gss.addEntryToUpdate(gsRow_2);


		GSRow gsRow_3 = GoogleSpreadsheetUtils.getGSRow(gss.getService(),
				wse, 3);
		gsRow_3.getGsCell(1).getCellEntry().changeInputValueLocal("GSL-A217");
		gsRow_3.getGsCell(1).setDirty();
		
		BatchUtils.setBatchId(gsRow_3.getGsCell(1).getCellEntry(), gsRow_3.getGsCell(1).getCellEntry().getId());
		BatchUtils.setBatchOperationType(gsRow_3.getGsCell(1).getCellEntry(),
				BatchOperationType.UPDATE);
		
		gss.addEntryToUpdate(gsRow_3);

		GSRow mjRow_4 = GoogleSpreadsheetUtils.getGSRow(gss.getService(),
				wse, 4);
		mjRow_4.getGsCell(1).getCellEntry().changeInputValueLocal("GSL-A216");
		mjRow_4.getGsCell(1).setDirty();
		
		BatchUtils.setBatchId(mjRow_4.getGsCell(1).getCellEntry(), mjRow_4.getGsCell(1).getCellEntry().getId());
		BatchUtils.setBatchOperationType(mjRow_4.getGsCell(1).getCellEntry(),
				BatchOperationType.UPDATE);
		gss.addEntryToUpdate(mjRow_4);
		
		GoogleSpreadsheetUtils.flush(gss.getService(), wse, gss.getBatchFeed());
	}		

	
	@Test //TODO: need to resolve phantom row issue
	public void shouldBatchDeleteRow() throws IOException, ServiceException {
		SpreadsheetEntry sse = getSampleGoogleSpreadsheet();
		WorksheetEntry wse = GoogleSpreadsheetUtils.getWorksheet(gss
				.getService(), sse, 0);
		Assert.assertNotNull(wse);
		
		GSRow gsRow_4 = GoogleSpreadsheetUtils.getGSRow(gss.getService(),
				wse, 4);
		for(IGSElement gsCell: gsRow_4.getGsCells()) {
			((GSCell)gsCell).getCellEntry().changeInputValueLocal("");
			gsCell.setDirty();
		
			BatchUtils.setBatchId(((GSCell)gsCell).getCellEntry(), ((GSCell)gsCell).getCellEntry().getId());
			BatchUtils.setBatchOperationType(((GSCell)gsCell).getCellEntry(),
					BatchOperationType.UPDATE);
		}
		
		gsRow_4.setDirty();
		gss.addEntryToUpdate(gsRow_4);
	
		GoogleSpreadsheetUtils.flush(gss.getService(), wse);
	}		
	
*/	
	private GSSpreadsheet getSampleGoogleSpreadsheet() {
		return gss.getGSSpreadsheet();
	}

}
