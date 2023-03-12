package com.juaracoding.DBLaundry.utils;

import com.juaracoding.DBLaundry.configuration.OtherConfig;
import com.juaracoding.DBLaundry.core.SMTPCore;

public class ExecuteSMTP {

    private String [] strException = new String[2];

    public ExecuteSMTP() {
        strException[0] = "ExecuteSMTP";
    }

    public Boolean sendSMTPToken(String mailAddress, String subject, String purpose, String token)
    {
        try
        {
            if(OtherConfig.getFlagSMTPActive().equalsIgnoreCase("y") && !mailAddress.equals(""))
            {
                String strContent = new ReadTextFileSB("\\data\\template-BCAF.html").getContentFile();
                strContent = strContent.replace("#$%^$%",purpose);
                strContent = strContent.replace("7tr54j",token);
                String [] strEmail = {mailAddress};
                SMTPCore sc = new SMTPCore();
                return  sc.sendSimpleMail(strEmail,
                        subject,
                        strContent,
                        "SSL");
            }
        }
        catch (Exception e)
        {
            strException[1]="sendToken(String mailAddress, String subject, String purpose, String token) -- LINE 35";
            LoggingFile.exceptionStringz(strException,e,OtherConfig.getFlagLogging());
            return false;
        }
        return true;
    }
}