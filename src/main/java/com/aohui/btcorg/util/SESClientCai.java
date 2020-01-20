package com.aohui.btcorg.util;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import org.springframework.stereotype.Component;

//@Component
public class SESClientCai {

	private AmazonSimpleEmailServiceClient ses;

	public SESClientCai() {
		
			
		String aws_SES_SNS_key2 = "AKIAXUXTCHMHA7KSD5F5";
		String aws_SES_SNS_secret2 = "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";
		
		ses = new AmazonSimpleEmailServiceClient(new BasicAWSCredentials(aws_SES_SNS_key2, aws_SES_SNS_secret2));
	
		ses.setEndpoint("email.us-east-1.amazonaws.com");

	}
	
	

	public AmazonSimpleEmailServiceClient getSes() {
		return ses;
	}



	public String sendMail(String fromNickName, String toEmail, String subject, String text,
			String html) {

		SendEmailRequest request = new SendEmailRequest().withSource(fromNickName + " <" + "noreply@newsletterz.com.au" + ">");

		Destination dest = new Destination().withToAddresses(toEmail);

		request.setDestination(dest);

		Content subjectPart = new Content().withData(subject);
		Content textPart = new Content().withData(text);
		Content htmlPart = new Content().withData(html);

		Body body = new Body().withText(textPart).withHtml(htmlPart);

		Message msg = new Message().withSubject(subjectPart).withBody(body);

		request.setMessage(msg);

		SendEmailResult result = ses.sendEmail(request);

		return result.getMessageId();
	}
}
