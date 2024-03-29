package org.mesh4j.sync.message.core;

import java.util.List;

import org.mesh4j.sync.message.IEndpoint;
import org.mesh4j.sync.message.IMessageSyncAdapter;
import org.mesh4j.sync.message.ISyncSession;

public interface ISyncSessionRepository {

	ISyncSession createSession(String sessionID, int version, String sourceId, IEndpoint endpoint, boolean fullProtocol, boolean shouldSendChanges, boolean shouldReceiveChanges);

	ISyncSession getSession(String sessionId);
	
	ISyncSession getSession(String sourceId, String endpointId);
	
	void flush(ISyncSession syncSession);

	void snapshot(ISyncSession syncSession);

	void cancel(ISyncSession syncSession);

	void registerSource(IMessageSyncAdapter adapter);
	
	void registerSourceIfAbsent(IMessageSyncAdapter adapter);
	
	IMessageSyncAdapter getSource(String sourceId);

	IMessageSyncAdapter getSourceOrCreateIfAbsent(String sourceId);

	List<ISyncSession> getAllSyncSessions();

	void removeSourceId(String sourceId);

}
