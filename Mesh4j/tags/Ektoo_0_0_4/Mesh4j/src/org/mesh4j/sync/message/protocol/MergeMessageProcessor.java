package org.mesh4j.sync.message.protocol;

import java.util.ArrayList;
import java.util.List;

import org.mesh4j.sync.message.IMessage;
import org.mesh4j.sync.message.IMessageSyncProtocol;
import org.mesh4j.sync.message.ISyncSession;
import org.mesh4j.sync.message.MessageSyncEngine;
import org.mesh4j.sync.message.core.IMessageProcessor;
import org.mesh4j.sync.message.core.Message;
import org.mesh4j.sync.model.Item;
import org.mesh4j.sync.validations.Guard;


public class MergeMessageProcessor implements IMessageProcessor {

	public static final String MESSAGE_TYPE = "5";
	
	// MODEL VARIABLES
	private IItemEncoding itemEncoding;
	private EndSyncMessageProcessor endMessage;
	
	// METHODS
	public MergeMessageProcessor(IItemEncoding itemEncoding, EndSyncMessageProcessor endMessage) {
		super();
		this.endMessage = endMessage;
		this.itemEncoding = itemEncoding;
	}

	@Override
	public String getMessageType() {
		return MESSAGE_TYPE;
	}
	
	public IMessage createMessage(ISyncSession syncSession, String syncId, int[] diffHashCodes) {
		Guard.argumentNotNull(syncSession, "syncSession");
		Guard.argumentNotNullOrEmptyString(syncId, "syncId");
		Guard.argumentNotNull(diffHashCodes, "diffHashCodes");
		
		Item item = syncSession.get(syncId);
		return createMessage(syncSession, item, diffHashCodes);
	}
	
	public IMessage createMessage(ISyncSession syncSession, Item item, int[] diffHashCodes) {
		Guard.argumentNotNull(syncSession, "syncSession");
		Guard.argumentNotNull(item, "item");
		Guard.argumentNotNull(diffHashCodes, "diffHashCodes");
		
		String data = this.itemEncoding.encode(syncSession, item, diffHashCodes);
		return new Message(
				IProtocolConstants.PROTOCOL,
				getMessageType(),
				syncSession.getSessionId(),
				syncSession.getVersion(),
				data,
				syncSession.getTarget());
	}
	
	@Override
	public List<IMessage> process(ISyncSession syncSession, IMessage message) {
		if(syncSession.isOpen()  && syncSession.getVersion() == message.getSessionVersion() && this.getMessageType().equals(message.getMessageType())){
			Item incomingItem = this.itemEncoding.decode(syncSession, message.getData());
			MessageSyncEngine.merge(syncSession, incomingItem);
			
			syncSession.notifyAck(incomingItem.getSyncId());
			if(syncSession.isCompleteSync()){
				ArrayList<IMessage> response = new ArrayList<IMessage>();
				response.add(this.endMessage.createMessage(syncSession));
				return response;
			}
		}
		return IMessageSyncProtocol.NO_RESPONSE;
	}

	public String getSyncID(String data) {
		return this.itemEncoding.getSyncID(data);
	}
}
