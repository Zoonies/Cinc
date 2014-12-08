package com.zoonies.cinc.core;

/**
 * @author <a href="mailto:thomas.boyles@gmail.com">Thomas Boyles</a>
 * Dec 6, 2014
 */

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendSms {

  private static final String ACCOUNT_SID = "...";
  private static final String AUTH_TOKEN = "...";
  private static final String FROM_NUMBER = "...";
  
    final static Logger logger = LoggerFactory.getLogger(SendSms.class);

    public SendSms() {
        super();
    }

    public static void sendSms(String toNumber) throws TwilioRestException { 
        logger.info("Sending SMS to " + toNumber);
        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Body", "Click through the following link to update your Cinc data and view your personal data: http://localhost:8080/index.html?4156065083"));
        params.add(new BasicNameValuePair("To", "+" + toNumber));
        params.add(new BasicNameValuePair("From", "+1" + FROM_NUMBER));
        try {
            MessageFactory messageFactory = client.getAccount().getMessageFactory();
            Message message = messageFactory.create(params);
            logger.info(message.getSid());
        } catch (TwilioRestException tre) {
            logger.info(tre.toString());
            throw new TwilioRestException (tre.toString(), 1);
        }
    }

    public static void main(String[] args) {
        try {
            sendSms(args[0]);
        } catch (TwilioRestException twe) {
            System.out.println(twe.toString());
        }
    }

}
