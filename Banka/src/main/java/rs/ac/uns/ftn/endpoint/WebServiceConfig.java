package rs.ac.uns.ftn.endpoint;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import rs.ac.uns.ftn.exception.DetailSoapFaultDefinitionExceptionResolver;
import rs.ac.uns.ftn.exception.ServiceFaultException;

import java.util.Properties;

/**
 * Created by Jasmina on 23/06/2017.
 */
@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
    @Bean
    public SoapFaultMappingExceptionResolver exceptionResolver(){

        final SoapFaultMappingExceptionResolver exceptionResolver = new DetailSoapFaultDefinitionExceptionResolver();

        final SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
        faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
        exceptionResolver.setDefaultFault(faultDefinition);

        final Properties errorMappings = new Properties();
        errorMappings.setProperty(Exception.class.getName(), SoapFaultDefinition.SERVER.toString());
        errorMappings.setProperty(ServiceFaultException.class.getName(), SoapFaultDefinition.SERVER.toString());
        exceptionResolver.setExceptionMappings(errorMappings);
        exceptionResolver.setOrder(1);
        return exceptionResolver;
    }

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/ws/*");
    }


    @Bean(name = "mt102")
    public DefaultWsdl11Definition Mt102SchemaDefinition(XsdSchema mt102schema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("Mt102Port");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://www.ftn.uns.ac.rs/mt102");
        wsdl11Definition.setSchema(mt102schema);
        return wsdl11Definition;
    }


    @Bean
    public XsdSchema mt102schema() {
        return new SimpleXsdSchema(new ClassPathResource("mt102.xsd"));
    }

    @Bean(name = "mt103")
    public DefaultWsdl11Definition Mt103SchemaDefinition(XsdSchema mt103schema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("Mt103Port");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://www.ftn.uns.ac.rs/mt103");
        wsdl11Definition.setSchema(mt103schema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema mt103schema() {
        return new SimpleXsdSchema(new ClassPathResource("mt103.xsd"));
    }

    @Bean(name = "mt900")
    public DefaultWsdl11Definition Mt900SchemaDefinition(XsdSchema mt900schema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("Mt900Port");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://www.ftn.uns.ac.rs/mt900");
        wsdl11Definition.setSchema(mt900schema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema mt900schema() {
        return new SimpleXsdSchema(new ClassPathResource("mt900.xsd"));
    }

    @Bean(name = "mt910")
    public DefaultWsdl11Definition Mt910SchemaDefinition(XsdSchema mt910schema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("Mt910Port");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://www.ftn.uns.ac.rs/mt910");
        wsdl11Definition.setSchema(mt910schema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema mt910schema() {
        return new SimpleXsdSchema(new ClassPathResource("mt910.xsd"));
    }


}