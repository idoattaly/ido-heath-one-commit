package com.dchealth.VO;

/**
 * Created by Administrator on 2017/8/25.
 */
public class MailProperties {
    private String  mailSmtpHost;
    private String mailTransportProtocal;
    private int mailSmtpPort = 25;
    private String userName;
    private String loginPassword;

    public String getMailSmtpHost() {
        return mailSmtpHost;
    }

    public void setMailSmtpHost(String mailSmtpHost) {
        this.mailSmtpHost = mailSmtpHost;
    }

    public String getMailTransportProtocal() {
        return mailTransportProtocal;
    }

    public void setMailTransportProtocal(String mailTransportProtocal) {
        this.mailTransportProtocal = mailTransportProtocal;
    }

    public int getMailSmtpPort() {
        return mailSmtpPort;
    }

    public void setMailSmtpPort(int mailSmtpPort) {
        this.mailSmtpPort = mailSmtpPort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
}
