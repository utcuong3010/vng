/**
 * AdsRMT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.vng.adsnew.ws;

public interface AdsRMTNEW extends java.rmi.Remote {
	public int mtReceiver(java.lang.String requestID, java.lang.String userID,
			java.lang.String serviceID, java.lang.String commandCode,
			java.lang.String message, java.lang.String sig)
			throws java.rmi.RemoteException;
}
