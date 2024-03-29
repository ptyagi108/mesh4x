package org.mesh4j.sync.message.channel.sms.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mesh4j.sync.id.generator.IdGenerator;
import org.mesh4j.sync.message.MockMessageEncoding;
import org.mesh4j.sync.message.MockSmsConnection;
import org.mesh4j.sync.message.channel.sms.SmsEndpoint;
import org.mesh4j.sync.message.channel.sms.batch.SmsMessage;
import org.mesh4j.sync.message.channel.sms.batch.SmsMessageBatch;
import org.mesh4j.sync.message.protocol.IProtocolConstants;
import org.mesh4j.sync.test.utils.TestHelper;


public class SmsSenderTests {

	@Test(expected=IllegalArgumentException.class)
	public void shouldCreateSMSSenderFailsWhenSMSConnectionIsNull(){
		new SmsSender(null);
	}
	
	@Test
	public void shouldCreateSMSSender(){
		SmsSender smsSender = new SmsSender(new MockSmsConnection("sms:154022344", new MockMessageEncoding()));
		Assert.assertEquals(0, smsSender.getOngoingBatchesCount());
		Assert.assertNotNull(smsSender.getOngoingBatches());
		Assert.assertEquals(0, smsSender.getOngoingBatches().size());
	}
	
	@Test
	public void shouldSendBatchAddToOngoingListIfAckIsRequired(){
		SmsMessageBatch batch = createBatch(3, "123");
		
		SmsSender smsSender = new SmsSender(new MockSmsConnection("sms:154022344", new MockMessageEncoding()));
		smsSender.send(batch, true);
		
		Assert.assertEquals(1, smsSender.getOngoingBatchesCount());
		Assert.assertEquals(batch, smsSender.getOngoingBatches().get(0));
		Assert.assertEquals(smsSender.getOngoingBatchesCount(), smsSender.getOngoingBatches().size());
	}
	
	@Test
	public void shouldSendBatchDontAddToOngoingListIfAckIsNotRequired(){
		SmsMessageBatch batch = createBatch(3, "123");
		
		SmsSender smsSender = new SmsSender(new MockSmsConnection("sms:154022344", new MockMessageEncoding()));
		smsSender.send(batch, false);
		
		Assert.assertEquals(0, smsSender.getOngoingBatchesCount());
		Assert.assertEquals(0, smsSender.getOngoingBatches().size());
	}	

	@Test
	public void shouldSendBatchSendAllSmsMessagesToSMSConnection(){

		MockSmsReceiver smsReceiver = new MockSmsReceiver();
		
		MockSmsConnection connA = new MockSmsConnection("sms:154022344", new MockMessageEncoding());
		
		MockSmsConnection connB = new MockSmsConnection("sms:330083232", new MockMessageEncoding());
		connB.registerMessageReceiver(null, smsReceiver);
		
		connA.setEndPoint(connB);
		
		SmsMessageBatch batch = createBatch(3, "123");
		
		SmsSender smsSender = new SmsSender(connA);
		smsSender.send(batch, true);
		
		Assert.assertEquals(batch.getExpectedMessageCount(), smsReceiver.getReceivedMessages().size());
		Assert.assertEquals(batch.getMessage(0).getText(), smsReceiver.getReceivedMessages().get(0).getText());
		Assert.assertEquals(batch.getMessage(1).getText(), smsReceiver.getReceivedMessages().get(1).getText());
		Assert.assertEquals(batch.getMessage(2).getText(), smsReceiver.getReceivedMessages().get(2).getText());
	}	

	@Test
	public void shouldReceiveACKIsDiscartedWhenBathIsNotOngoing(){
		SmsMessageBatch batch = createBatch(3, "123");

		SmsSender smsSender = new SmsSender(new MockSmsConnection("sms:154022344", new MockMessageEncoding()));
		smsSender.send(batch, false);
		
		Assert.assertNull(smsSender.getOngoingBatch(batch.getId()));
		smsSender.receiveACK(batch.getId());
		Assert.assertNull(smsSender.getOngoingBatch(batch.getId()));		
	}
	
	@Test
	public void shouldReceiveACKIsDiscartedWhenBathIDIsNull(){
		SmsSender smsSender = new SmsSender(new MockSmsConnection("sms:154022344", new MockMessageEncoding()));
		smsSender.receiveACK(null);
	}	

	@Test
	public void shouldReceiveACKIsDiscartedWhenBathIDIsEmpty(){
		SmsSender smsSender = new SmsSender(new MockSmsConnection("sms:154022344", new MockMessageEncoding()));
		smsSender.receiveACK("");
	}	

	@Test
	public void shouldReceiveACKRemoveBathInOngoingList(){
		SmsMessageBatch batch = createBatch(3, "123");

		SmsSender smsSender = new SmsSender(new MockSmsConnection("sms:154022344", new MockMessageEncoding()));
		smsSender.send(batch, true);
		
		Assert.assertEquals(batch, smsSender.getOngoingBatch(batch.getId()));
		smsSender.receiveACK(batch.getId());
		Assert.assertNull(smsSender.getOngoingBatch(batch.getId()));		

	}	
	
	@Test
	public void shouldGetOngoingBatchReturnsNullIfBatchIdIsNull(){
		SmsSender smsSender = new SmsSender(new MockSmsConnection("sms:154022344", new MockMessageEncoding()));
		Assert.assertNull(smsSender.getOngoingBatch(null));
	}
	
	@Test
	public void shouldGetOngoingBatchReturnsNullIfBatchIdIsEmpty(){
		SmsSender smsSender = new SmsSender(new MockSmsConnection("sms:154022344", new MockMessageEncoding()));
		Assert.assertNull(smsSender.getOngoingBatch(""));
	}	
	
	@Test
	public void shouldGetOngoingBatchReturnsNullIfBatchIsNotOngoing(){
		SmsMessageBatch batch = createBatch(3, "123");

		SmsSender smsSender = new SmsSender(new MockSmsConnection("sms:154022344", new MockMessageEncoding()));
		smsSender.send(batch, false);
		
		Assert.assertNull(smsSender.getOngoingBatch(batch.getId()));
	}	

	@Test
	public void shouldGetOngoingBatchReturnsBatchOngoing(){
		SmsMessageBatch batch = createBatch(3, "123");
		
		SmsSender smsSender = new SmsSender(new MockSmsConnection("sms:154022344", new MockMessageEncoding()));
		smsSender.send(batch, true);
		
		Assert.assertEquals(batch, smsSender.getOngoingBatch(batch.getId()));
	}	
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldSendSMSMessageFailsIfSmsMessageListIsNull(){
		SmsSender smsSender = new SmsSender(new MockSmsConnection("sms:154022344", new MockMessageEncoding()));
		List<SmsMessage> messages = null;
		smsSender.send(messages, new SmsEndpoint("123"));
	}	
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldSendSMSMessageFailsIfSmsMessageTextIsNull(){
		SmsSender smsSender = new SmsSender(new MockSmsConnection("sms:154022344", new MockMessageEncoding()));
		
		ArrayList<SmsMessage> msgs = new ArrayList<SmsMessage>();
		msgs.add(new SmsMessage(null));
		
		smsSender.send(msgs, new SmsEndpoint("123"));
	}	

	@Test(expected=IllegalArgumentException.class)
	public void shouldSendSMSMessageFailsIfSmsMessageTextIsEmpty(){
		SmsSender smsSender = new SmsSender(new MockSmsConnection("sms:154022344", new MockMessageEncoding()));
		
		ArrayList<SmsMessage> msgs = new ArrayList<SmsMessage>();
		msgs.add(new SmsMessage(""));
		
		smsSender.send(msgs, new SmsEndpoint("123"));
	}	

	@Test(expected=IllegalArgumentException.class)
	public void shouldSendSMSMessageFailsIfEndpointIsNull(){
		SmsSender smsSender = new SmsSender(new MockSmsConnection("sms:154022344", new MockMessageEncoding()));
		
		ArrayList<SmsMessage> msgs = new ArrayList<SmsMessage>();
		msgs.add(new SmsMessage("dffef"));
		
		smsSender.send(msgs, null);
	}
	
	@Test
	public void shouldSendSMSMessage(){
		
		MockSmsReceiver smsReceiver = new MockSmsReceiver();
		
		MockSmsConnection connA = new MockSmsConnection("sms:154022344", new MockMessageEncoding());
		
		MockSmsConnection connB = new MockSmsConnection("sms:330083232", new MockMessageEncoding());
		connB.registerMessageReceiver(null, smsReceiver);
		
		connA.setEndPoint(connB);
		
		SmsSender smsSender = new SmsSender(connA);
		
		Date date = TestHelper.makeDate(2008, 1, 1, 1, 1, 1, 1);
		SmsMessage smsMessage = new SmsMessage("message 1123", date); 
				
		ArrayList<SmsMessage> msgs = new ArrayList<SmsMessage>();
		msgs.add(smsMessage);
		
		smsSender.send(msgs, new SmsEndpoint("123"));
		
		Assert.assertEquals(1, smsReceiver.getReceivedMessages().size());
		Assert.assertEquals(smsMessage.getText(), smsReceiver.getReceivedMessages().get(0).getText());
		Assert.assertTrue(date.before(smsMessage.getLastModificationDate()));
	}

	@Test
	public void shouldGetCompletedBatchesForSyncSession(){
		
		String sessionId = "1";
		int version = 1;
		
		SmsSender smsSender = new SmsSender(new MockSmsConnection("sms:154022344", new MockMessageEncoding()));
		
		SmsMessageBatch batch = createBatch(10, "2");
		smsSender.send(batch, false);
		batch = createBatch(10, "3");
		smsSender.send(batch, false);
		batch = createBatch(10, "4");
		smsSender.send(batch, true);
		batch = createBatch(10, "5");
		smsSender.send(batch, true);
		
		batch = createBatch(10, sessionId);
		smsSender.send(batch, false);
		
		SmsMessageBatch batch2 = createBatch(10, sessionId);
		smsSender.send(batch2, false);

		List<SmsMessageBatch> result = smsSender.getCompletedBatches(sessionId, version);
		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		Assert.assertFalse(result.get(1).equals(result.get(0)));
		
		Assert.assertTrue(batch.equals(result.get(1)) || batch.equals(result.get(0)));
		Assert.assertTrue(batch2.equals(result.get(1)) || batch2.equals(result.get(0)));
	}
	
	@Test
	public void shouldGetOngoingBatchesForSyncSession(){
		
		String sessionId = "1";
		int version = 1;
		
		SmsSender smsSender = new SmsSender(new MockSmsConnection("sms:154022344", new MockMessageEncoding()));
		
		SmsMessageBatch batch = createBatch(10, "2");
		smsSender.send(batch, false);
		batch = createBatch(10, "3");
		smsSender.send(batch, false);
		batch = createBatch(10, "4");
		smsSender.send(batch, true);
		batch = createBatch(10, "5");
		smsSender.send(batch, true);
			
		batch = createBatch(10, sessionId);
		smsSender.send(batch, true);
		SmsMessageBatch batch2 = createBatch(10, sessionId);
		smsSender.send(batch2, true);
		
		List<SmsMessageBatch> result = smsSender.getOngoingBatches(sessionId, version);
		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		Assert.assertFalse(result.get(1).equals(result.get(0)));
		
		Assert.assertTrue(batch.equals(result.get(1)) || batch.equals(result.get(0)));
		Assert.assertTrue(batch2.equals(result.get(1)) || batch2.equals(result.get(0)));
	}

	
	@Test
	public void shouldPurgeBatches(){
		String sessionId = "1";
		int version = 1;
		
		SmsSender smsSender = new SmsSender(new MockSmsConnection("sms:154022344", new MockMessageEncoding()));
		
		SmsMessageBatch batch = createBatch(10, sessionId);
		smsSender.send(batch, false);
		batch = createBatch(10, sessionId);
		smsSender.send(batch, false);
		batch = createBatch(10,sessionId);
		smsSender.send(batch, true);
		batch = createBatch(10, sessionId);
		smsSender.send(batch, true);			
		
		List<SmsMessageBatch> result = smsSender.getOngoingBatches(sessionId, version);
		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		result = smsSender.getCompletedBatches(sessionId, version);
		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		
		smsSender.purgeBatches(sessionId, version);
		
		result = smsSender.getOngoingBatches(sessionId, version);
		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());
		result = smsSender.getCompletedBatches(sessionId, version);
		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());
	}
	
	private SmsMessageBatch createBatch(int numberOfMessages, String sessionId) {
		SmsMessageBatch batch = new SmsMessageBatch(sessionId, new SmsEndpoint("123"), IProtocolConstants.PROTOCOL, IdGenerator.INSTANCE.newID(), numberOfMessages);
		for (int i = 0; i < numberOfMessages; i++) {
			batch.addMessage(i, new SmsMessage("message"+i));
		}
		return batch;
	}
}
