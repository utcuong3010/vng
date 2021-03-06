// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Preference.java

package com.vasc.smpp.gateway;

import java.io.*;
import java.util.*;
import org.smpp.pdu.*;

public class Preference
{

    public Preference()
    {
    }

    public static Address buildDestAddress(String destAddr)
    {
        Address address;
        address = new Address();
        address.setTon(dest_addr_ton);
        address.setNpi(dest_addr_npi);
        address.setAddress(destAddr);
        return address;
        WrongLengthOfStringException e;
        e;
        System.out.println("The length of dest_addr parameter is wrong: " + destAddr);
        return null;
    }

    public static Address buildSrcAddress(String srcAddr)
    {
        Address address;
        address = new Address();
        address.setTon(src_addr_ton);
        address.setNpi(src_addr_npi);
        address.setAddress(srcAddr);
        return address;
        WrongLengthOfStringException e;
        e;
        System.out.println("The length of dest_addr parameter is wrong: " + srcAddr);
        return null;
    }

    static byte getByteProperty(String propName, byte defaultValue)
    {
        return Byte.parseByte(properties.getProperty(propName, Byte.toString(defaultValue)).trim());
    }

    static int getIntProperty(String propName, int defaultValue)
    {
        return Integer.parseInt(properties.getProperty(propName, Integer.toString(defaultValue)).trim());
    }

    public static boolean isValidServiceId(String serviceId)
    {
        if(serviceId == null)
            return false;
        if(serviceId.startsWith("+"))
            serviceId = serviceId.substring(1);
        return serviceId.length() > 2;
    }

    public static void loadProperties(String fileName)
        throws IOException
    {
        String addr;
        System.out.println("Reading configuration file " + fileName + "...");
        FileInputStream propsFile = new FileInputStream(fileName);
        properties.load(propsFile);
        propsFile.close();
        System.out.println("Setting default parameters...");
        ipAddress = properties.getProperty("ip_address");
        port = getIntProperty("port", port);
        systemId = properties.getProperty("system_id");
        password = properties.getProperty("password");
        byte ton = getByteProperty("addr_ton", addressRange.getTon());
        byte npi = getByteProperty("addr_npi", addressRange.getNpi());
        addr = properties.getProperty("addr_range", addressRange.getAddressRange());
        addressRange.setTon(ton);
        addressRange.setNpi(npi);
        addressRange.setAddressRange(addr);
        break MISSING_BLOCK_LABEL_182;
        WrongLengthOfStringException e;
        e;
        System.out.println("The length of address_range parameter is wrong.");
        src_addr_ton = getByteProperty("source_addr_ton", src_addr_ton);
        src_addr_npi = getByteProperty("source_addr_npi", src_addr_ton);
        sourceAddressList = parseString(properties.getProperty("source_addr", ""));
        if(sourceAddressList != null && sourceAddressList.size() > 0)
            System.out.println("This gateway will process: " + sourceAddressList);
        else
            System.out.println("Warning: No source address is specified!");
        dest_addr_ton = getByteProperty("dest_addr_ton", dest_addr_ton);
        dest_addr_npi = getByteProperty("dest_addr_npi", dest_addr_npi);
        addr = properties.getProperty("destination_addr", "");
        serviceType = properties.getProperty("service_type", serviceType);
        systemType = properties.getProperty("system_type", systemType);
        String strTemp = properties.getProperty("bind_mode", bindMode);
        if(!strTemp.equalsIgnoreCase("t") && !strTemp.equalsIgnoreCase("r") && !strTemp.equalsIgnoreCase("tr"))
        {
            System.out.println("Wrong value of bind_mode parameter in the configuration file " + fileName + "--> Setting the default: t");
            strTemp = "t";
        }
        bindMode = strTemp;
        String syncMode = properties.getProperty("sync_mode", asynchronous ? "async" : "sync");
        if(syncMode.equalsIgnoreCase("sync"))
            asynchronous = false;
        else
        if(syncMode.equalsIgnoreCase("async"))
            asynchronous = true;
        else
            asynchronous = false;
        int rcvTimeout = 0;
        rcvTimeout = getIntProperty("receive_timeout", rcvTimeout);
        if(rcvTimeout == -1)
            receiveTimeout = -1L;
        else
            receiveTimeout = rcvTimeout * 1000;
        db_DriverClassName = properties.getProperty("db_driver", db_DriverClassName);
        db_URL = properties.getProperty("db_url", db_URL);
        db_user = properties.getProperty("db_user", db_user);
        db_password = properties.getProperty("db_password", db_password);
        db_MaxConnections = getIntProperty("db_max_connections", db_MaxConnections);
        maxNumOfSend = getIntProperty("max_num_of_send", maxNumOfSend);
        logToFile = getIntProperty("log_to_file", logToFile);
        fileToLog = properties.getProperty("file_to_log", fileToLog);
        timeRebind = getIntProperty("time_rebind", timeRebind / 1000) * 1000;
        timeResend = getIntProperty("time_resend", timeResend / 1000) * 1000;
        timeEnquireLink = getIntProperty("time_enquire_link", timeEnquireLink / 1000) * 1000;
        maxSMPerSecond = getIntProperty("max_sm_per_sec", maxSMPerSecond);
        mobileOperator = properties.getProperty("mobile_operator", mobileOperator).toUpperCase();
        sendSPAM = properties.getProperty("spam", sendSPAM);
        contentSPAM = properties.getProperty("content-spam", sendSPAM);
        String sTemp = properties.getProperty("report_required", "0");
        if("1".equals(sTemp))
            reportRequired = true;
        sTemp = properties.getProperty("cdr_enabled", "0");
        if("1".equals(sTemp))
            cdrEnabled = true;
        cdroutFolder = properties.getProperty("cdrout_folder", cdroutFolder);
        cdrsentFolder = properties.getProperty("cdrsent_folder", cdrsentFolder);
        cdrfileExtension = properties.getProperty("cdrfile_extension", cdrfileExtension);
        receiveLogFolder = properties.getProperty("receive-log-folder", receiveLogFolder);
        sendLogFolder = properties.getProperty("send-log-folder", sendLogFolder);
        VIETTEL_MODE = properties.getProperty("vt-mode", VIETTEL_MODE).trim();
        SEND_MODE = properties.getProperty("send-mode", SEND_MODE).trim();
        if(sourceAddressList != null && sourceAddressList.size() > 0)
        {
            for(Iterator it = sourceAddressList.iterator(); it.hasNext();)
            {
                String sNumber = (String)it.next();
                Collection cPrefixes = parseString(properties.getProperty("prefix_" + sNumber));
                if(cPrefixes != null && cPrefixes.size() > 0)
                    prefixMap.put(sNumber, cPrefixes);
                else
                    System.out.println("Warning: No prefix is specified for " + sNumber);
                String sMessage = properties.getProperty("message_" + sNumber);
                if(sMessage != null)
                    messageMap.put(sNumber, sMessage);
                else
                    System.out.println("Warning: No message is specified for " + sNumber);
            }

        }
        return;
    }

    public static void main(String args[])
    {
        Preference pre = new Preference();
        pre;
        loadProperties("gateway.cfg");
        break MISSING_BLOCK_LABEL_23;
        IOException ex;
        ex;
        return;
    }

    static Vector parseString(String text)
    {
        Vector prefixes = new Vector();
        if(text == null || "".equals(text))
            return prefixes;
        String tempStr = text.toUpperCase();
        String currentLabel = null;
        for(int index = tempStr.indexOf(","); index != -1; index = tempStr.indexOf(","))
        {
            currentLabel = tempStr.substring(0, index).trim();
            if(!"".equals(currentLabel))
                prefixes.addElement(currentLabel);
            tempStr = tempStr.substring(index + 1);
        }

        currentLabel = tempStr.trim();
        if(!"".equals(currentLabel))
            prefixes.addElement(currentLabel);
        return prefixes;
    }

    public static String SEND_MODE = "0";
    public static String VIETTEL_MODE = "tr";
    public static AddressRange addressRange = new AddressRange();
    public static boolean asynchronous = false;
    public static String bindMode = "tr";
    public static boolean cdrEnabled = false;
    public static String cdrfileExtension = ".bil";
    public static String cdroutFolder = ".\\CDROUT";
    public static String cdrsentFolder = ".\\CDRSENT";
    public static String contentSPAM = "Hay tham du tuan le sinh nhat VASC tai The gioi dieu ky bat dau tu 01/08  den het 07/08. Website: http://18001255.vietnamnet.vn, DT ho tro 18001255";
    public static byte dataCoding = 0;
    static String db_DriverClassName = "oracle.jdbc.driver.OracleDriver";
    static int db_MaxConnections = 3;
    static String db_URL = "jdbc:oracle:oci8:@ORA";
    static String db_password = "smpp2003";
    static String db_user = "smpp";
    private static byte dest_addr_npi = 0;
    private static byte dest_addr_ton = 0;
    public static byte esmClass = 0;
    public static String fileToLog = null;
    public static String ipAddress = null;
    public static int logToFile = 0;
    public static int maxNumOfSend = 0;
    public static int maxSMPerSecond = 1;
    public static String messageId = "";
    public static Map messageMap = new Hashtable();
    public static String mobileOperator = "";
    public static String password = null;
    public static int port = 0;
    public static Map prefixMap = new Hashtable();
    public static byte priorityFlag = 0;
    private static Properties properties = new Properties();
    public static byte protocolId = 0;
    public static String receiveLogFolder = ".\\LOG-R";
    public static long receiveTimeout = 0L;
    public static byte registeredDelivery = 0;
    public static byte replaceIfPresentFlag = 0;
    public static boolean reportRequired = false;
    public static String scheduleDeliveryTime = "";
    public static String sendLogFolder = ".\\LOG-S";
    public static String sendSPAM = "0";
    public static String serviceType = "";
    public static String shortMessage = "";
    public static byte smDefaultMsgId = 0;
    public static Collection sourceAddressList = null;
    private static byte src_addr_npi = 0;
    private static byte src_addr_ton = 0;
    public static String systemId = null;
    public static String systemType = "";
    public static int timeEnquireLink = 0x11170;
    public static int timeRebind = 1000;
    public static int timeResend = 0x493e0;
    public static String validServiceIds[] = {
        "996", "997", "998", "19001255", "19001522", "19001799", "84996", "84997", "84998", "8419001255", 
        "8419001522", "8419001799", "04996", "04997", "04998", "0419001255", "0419001522", "0419001799"
    };
    public static String validityPeriod = "";

}
