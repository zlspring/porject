package com.aohui.btcorg.entity;

public class EmailReq {
    private String domainName;
    private String fromEmail;
    private String fromNickName;
    private String toEmail;
    private String subject;
    private String text;
    private String html;

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getFromNickName() {
        return fromNickName;
    }

    public void setFromNickName(String fromNickName) {
        this.fromNickName = fromNickName;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    @Override
    public String toString() {
        return "EmailReq{" +
                "domainName='" + domainName + '\'' +
                ", fromEmail='" + fromEmail + '\'' +
                ", fromNickName='" + fromNickName + '\'' +
                ", toEmail='" + toEmail + '\'' +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", html='" + html + '\'' +
                '}';
    }
}
