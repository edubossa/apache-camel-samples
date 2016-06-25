/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.argonavis.cursocamel;

/**
 * OK!
 *
 * @author helderdarocha
 */
public interface Configuration {

    /** WORKDIR **/
    public static String WORKDIR = "lab";

    /** file:lab/inbox **/
    public static String INBOX   = "file:" + WORKDIR + "/inbox";

    /** file:lab/outbox **/
    public static String OUTBOX  = "file:" + WORKDIR + "/outbox";

    /** file:lab/wiretap **/
    public static String WIRETAP  = "file:" + WORKDIR + "/wiretap";

    /** localhost **/
    public static String FTP_SERVER = "localhost";

    /** Username xxxxx **/
    public static String FTP_USER = "wallace";

    /** Password xxxx **/
    public static String FTP_PASS = "XXXX9098987";

    /** /Users/wallace/Projetos/EIP-Course/camel-course/lab/ftpdata **/
    public static String FTP_PATH = "/Users/wallace/Projetos/EIP-Course/camel-course/lab/ftpdata";

    /** tcp://localhost:61616 **/
    public static String ACTIVEMQ_URL = "tcp://localhost:61616";

    /** ftp://localhost?username=XXXXXX&password=******* **/
    public static String FTP_URL      = "ftp://" + FTP_SERVER + FTP_PATH + "?username=" +  FTP_USER + "&password=" + FTP_PASS;

}
