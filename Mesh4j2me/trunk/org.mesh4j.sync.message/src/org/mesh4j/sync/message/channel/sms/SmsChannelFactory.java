package org.mesh4j.sync.message.channel.sms;

import org.mesh4j.sync.message.channel.sms.core.ISmsReceiverRepository;
import org.mesh4j.sync.message.channel.sms.core.ISmsSenderRepository;
import org.mesh4j.sync.message.channel.sms.core.SmsChannel;
import org.mesh4j.sync.message.channel.sms.core.SmsReceiver;
import org.mesh4j.sync.message.channel.sms.core.SmsSender;
import org.mesh4j.sync.message.channel.sms.core.rms.storage.SmsReceiverRepository;
import org.mesh4j.sync.message.channel.sms.core.rms.storage.SmsSenderRepository;
import org.mesh4j.sync.message.channel.sms.schedule.AskLossMessagesScheduleTask;
import org.mesh4j.sync.message.channel.sms.schedule.ResendBatchWithoutACKScheduleTask;
import org.mesh4j.sync.message.schedule.timer.TimerScheduler;
import org.mesh4j.sync.validations.Guard;

public class SmsChannelFactory {

	public static ISmsChannel createChannel(ISmsConnection smsConnection, int senderRetryTimeOut, int receiverRetryTimeOut, ISmsRetiesNotification retriesNotification){
		return createChannel(smsConnection, senderRetryTimeOut, receiverRetryTimeOut, new SmsSenderRepository(), new SmsReceiverRepository(), retriesNotification);
	}
	
	public static ISmsChannel createChannel(ISmsConnection smsConnection, int senderRetryTimeOut, int receiverRetryTimeOut, ISmsSenderRepository senderRepository, ISmsReceiverRepository receiverRepository, ISmsRetiesNotification retriesNotification){
		Guard.argumentNotNull(smsConnection, "smsConnection");
		
		SmsSender sender = new SmsSender(smsConnection, senderRepository);
		SmsReceiver receiver = new SmsReceiver(receiverRepository);
		smsConnection.setMessageReceiver(receiver);
		
		SmsChannel channel = new SmsChannel(sender, receiver, smsConnection.getMessageEncoding(), smsConnection.getMaxMessageLenght(), retriesNotification);
		
		if(senderRetryTimeOut > 0){
			TimerScheduler.INSTANCE.schedule(new ResendBatchWithoutACKScheduleTask(channel, senderRetryTimeOut), senderRetryTimeOut);
		}
		
		if(receiverRetryTimeOut > 0){
			TimerScheduler.INSTANCE.schedule( new AskLossMessagesScheduleTask(channel, receiverRetryTimeOut), receiverRetryTimeOut);
		}
		return channel;
	}
	
	public static void restart(int senderRetryTimeOut, int receiverRetryTimeOut){
		ResendBatchWithoutACKScheduleTask resendTask = (ResendBatchWithoutACKScheduleTask) TimerScheduler.INSTANCE.getTask(ResendBatchWithoutACKScheduleTask.TASK_ID);
		TimerScheduler.INSTANCE.cancelTask(ResendBatchWithoutACKScheduleTask.TASK_ID);
		
		AskLossMessagesScheduleTask askLossTask = (AskLossMessagesScheduleTask) TimerScheduler.INSTANCE.getTask(AskLossMessagesScheduleTask.TASK_ID);
		TimerScheduler.INSTANCE.cancelTask(AskLossMessagesScheduleTask.TASK_ID);
		
		if(senderRetryTimeOut > 0){
			TimerScheduler.INSTANCE.schedule(new ResendBatchWithoutACKScheduleTask(resendTask.getChannel(), senderRetryTimeOut), senderRetryTimeOut);
		}
		
		if(receiverRetryTimeOut > 0){
			TimerScheduler.INSTANCE.schedule( new AskLossMessagesScheduleTask(askLossTask.getChannel(), receiverRetryTimeOut), receiverRetryTimeOut);
		}
	}
}
