

/**
 * ObopayExternalWebServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.0  Built on : May 17, 2011 (04:19:43 IST)
 */

    package com.ebridge.services.payment.telecash.ws;

    /*
     *  ObopayExternalWebServiceService java interface
     */

    public interface ObopayExternalWebServiceService {
          

        /**
          * Auto generated method signature
          * 
                    * @param customerCashIn
                
         */

         
                     public com.obopay.www.xml.oews.v1.CustomerCashInResponse customerCashIn(

                        com.obopay.www.xml.oews.v1.CustomerCashIn customerCashIn)
                        throws java.rmi.RemoteException
             ;

        

        /**
          * Auto generated method signature
          * 
                    * @param customerCashOut
                
         */

         
                     public com.obopay.www.xml.oews.v1.CustomerCashOutResponse customerCashOut(

                        com.obopay.www.xml.oews.v1.CustomerCashOut customerCashOut)
                        throws java.rmi.RemoteException
             ;

        

        /**
          * Auto generated method signature
          * 
                    * @param customerViralCashOut
                
         */

         
                     public com.obopay.www.xml.oews.v1.CustomerViralCashOutResponse customerViralCashOut(

                        com.obopay.www.xml.oews.v1.CustomerViralCashOut customerViralCashOut)
                        throws java.rmi.RemoteException
             ;

        

        /**
          * Auto generated method signature
          * 
                    * @param customerRegistration
                
         */

         
                     public com.obopay.www.xml.oews.v1.CustomerRegistrationResponse customerRegistration(

                        com.obopay.www.xml.oews.v1.CustomerRegistration customerRegistration)
                        throws java.rmi.RemoteException
             ;

        

        /**
          * Auto generated method signature
          * 
                    * @param authorize
                
         */

         
                     public com.obopay.www.xml.oews.v1.AuthorizeResponse authorize(

                        com.obopay.www.xml.oews.v1.Authorize authorize)
                        throws java.rmi.RemoteException
             ;

        

        /**
          * Auto generated method signature
          * 
                    * @param process
                
         */

         
                     public com.obopay.www.xml.oews.v1.ProcessResponse process(

                        com.obopay.www.xml.oews.v1.Process process)
                        throws java.rmi.RemoteException
             ;

        

        
       //
       }
    