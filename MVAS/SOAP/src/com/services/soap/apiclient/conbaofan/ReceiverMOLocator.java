/**
 * ReceiverMOLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.services.soap.apiclient.conbaofan;

public class ReceiverMOLocator extends org.apache.axis.client.Service implements com.services.soap.apiclient.conbaofan.ReceiverMO {

    public ReceiverMOLocator() {
    }


    public ReceiverMOLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ReceiverMOLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ReceiverMOPort
    private java.lang.String ReceiverMOPort_address = "http://127.0.0.1/conbaofan/ReceiverMO.php";

    public java.lang.String getReceiverMOPortAddress() {
        return ReceiverMOPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ReceiverMOPortWSDDServiceName = "ReceiverMOPort";

    public java.lang.String getReceiverMOPortWSDDServiceName() {
        return ReceiverMOPortWSDDServiceName;
    }

    public void setReceiverMOPortWSDDServiceName(java.lang.String name) {
        ReceiverMOPortWSDDServiceName = name;
    }

    public com.services.soap.apiclient.conbaofan.ReceiverMOPortType getReceiverMOPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ReceiverMOPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getReceiverMOPort(endpoint);
    }

    public com.services.soap.apiclient.conbaofan.ReceiverMOPortType getReceiverMOPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.services.soap.apiclient.conbaofan.ReceiverMOBindingStub _stub = new com.services.soap.apiclient.conbaofan.ReceiverMOBindingStub(portAddress, this);
            _stub.setPortName(getReceiverMOPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setReceiverMOPortEndpointAddress(java.lang.String address) {
        ReceiverMOPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.services.soap.apiclient.conbaofan.ReceiverMOPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.services.soap.apiclient.conbaofan.ReceiverMOBindingStub _stub = new com.services.soap.apiclient.conbaofan.ReceiverMOBindingStub(new java.net.URL(ReceiverMOPort_address), this);
                _stub.setPortName(getReceiverMOPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ReceiverMOPort".equals(inputPortName)) {
            return getReceiverMOPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:receivermo", "ReceiverMO");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:receivermo", "ReceiverMOPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ReceiverMOPort".equals(portName)) {
            setReceiverMOPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
