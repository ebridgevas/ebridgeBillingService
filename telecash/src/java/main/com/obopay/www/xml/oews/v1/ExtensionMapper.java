
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.0  Built on : May 17, 2011 (04:21:18 IST)
 */

        
            package com.obopay.www.xml.oews.v1;
        
            /**
            *  ExtensionMapper class
            */
            @SuppressWarnings({"unchecked","unused"})
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "java:com.obopay.ws.data".equals(namespaceURI) &&
                  "CashInResponse".equals(typeName)){
                   
                            return  data.ws.obopay.com.CashInResponse.Factory.parse(reader);
                        

                  }

              
                  if (
                  "java:com.obopay.ws.data".equals(namespaceURI) &&
                  "AbstractRequest".equals(typeName)){
                   
                            return  data.ws.obopay.com.AbstractRequest.Factory.parse(reader);
                        

                  }

              
                  if (
                  "java:com.obopay.ws.data".equals(namespaceURI) &&
                  "ObopayWebServiceData".equals(typeName)){
                   
                            return  data.ws.obopay.com.ObopayWebServiceData.Factory.parse(reader);
                        

                  }

              
                  if (
                  "java:com.obopay.ws.data".equals(namespaceURI) &&
                  "RegistrationResponse".equals(typeName)){
                   
                            return  data.ws.obopay.com.RegistrationResponse.Factory.parse(reader);
                        

                  }

              
                  if (
                  "java:com.obopay.ws.data".equals(namespaceURI) &&
                  "WhiteList".equals(typeName)){
                   
                            return  data.ws.obopay.com.WhiteList.Factory.parse(reader);
                        

                  }

              
                  if (
                  "java:com.obopay.ws.data".equals(namespaceURI) &&
                  "AbstractResponse".equals(typeName)){
                   
                            return  data.ws.obopay.com.AbstractResponse.Factory.parse(reader);
                        

                  }

              
                  if (
                  "java:com.obopay.ws.data".equals(namespaceURI) &&
                  "AddressDetails".equals(typeName)){
                   
                            return  data.ws.obopay.com.AddressDetails.Factory.parse(reader);
                        

                  }

              
                  if (
                  "java:com.obopay.ws.data".equals(namespaceURI) &&
                  "CashOutRequest".equals(typeName)){
                   
                            return  data.ws.obopay.com.CashOutRequest.Factory.parse(reader);
                        

                  }

              
                  if (
                  "java:com.obopay.ws.data".equals(namespaceURI) &&
                  "AuthorizationRequest".equals(typeName)){
                   
                            return  data.ws.obopay.com.AuthorizationRequest.Factory.parse(reader);
                        

                  }

              
                  if (
                  "java:com.obopay.core.transferobject".equals(namespaceURI) &&
                  "BaseTransferObject".equals(typeName)){
                   
                            return  transferobject.core.obopay.com.BaseTransferObject.Factory.parse(reader);
                        

                  }

              
                  if (
                  "java:com.obopay.ws.data".equals(namespaceURI) &&
                  "VerificationRequest".equals(typeName)){
                   
                            return  data.ws.obopay.com.VerificationRequest.Factory.parse(reader);
                        

                  }

              
                  if (
                  "java:com.obopay.ws.data".equals(namespaceURI) &&
                  "RegistrationRequest".equals(typeName)){
                   
                            return  data.ws.obopay.com.RegistrationRequest.Factory.parse(reader);
                        

                  }

              
                  if (
                  "java:com.obopay.ws.data".equals(namespaceURI) &&
                  "AuthorizationResponse".equals(typeName)){
                   
                            return  data.ws.obopay.com.AuthorizationResponse.Factory.parse(reader);
                        

                  }

              
                  if (
                  "java:com.obopay.ws.data".equals(namespaceURI) &&
                  "CashOutResponse".equals(typeName)){
                   
                            return  data.ws.obopay.com.CashOutResponse.Factory.parse(reader);
                        

                  }

              
                  if (
                  "java:com.obopay.ws.data".equals(namespaceURI) &&
                  "ViralCashOutRequest".equals(typeName)){
                   
                            return  data.ws.obopay.com.ViralCashOutRequest.Factory.parse(reader);
                        

                  }

              
                  if (
                  "java:com.obopay.ws.data".equals(namespaceURI) &&
                  "CashInRequest".equals(typeName)){
                   
                            return  data.ws.obopay.com.CashInRequest.Factory.parse(reader);
                        

                  }

              
                  if (
                  "java:com.obopay.ws.data".equals(namespaceURI) &&
                  "ViralCashOutResponse".equals(typeName)){
                   
                            return  data.ws.obopay.com.ViralCashOutResponse.Factory.parse(reader);
                        

                  }

              
                  if (
                  "java:com.obopay.ws.data".equals(namespaceURI) &&
                  "NonFinancialTransactionRequest".equals(typeName)){
                   
                            return  data.ws.obopay.com.NonFinancialTransactionRequest.Factory.parse(reader);
                        

                  }

              
                  if (
                  "java:com.obopay.ws.data".equals(namespaceURI) &&
                  "FinancialTransactionRequest".equals(typeName)){
                   
                            return  data.ws.obopay.com.FinancialTransactionRequest.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    