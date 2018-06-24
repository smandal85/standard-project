package com.backbase.recruitment;

import com.backbase.recruitment.openbank.v2.api.Transactions;
import com.backbase.recruitment.openbank.v2.api.Value;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import javax.security.auth.Subject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andresc on 04/07/2017.
 */
public class TransactionRoutesTest extends CamelSpringTestSupport {

    protected static int portNum;

    @BeforeClass
    public static void initializePortNum() {
        portNum = AvailablePortFinder.getNextAvailable();
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-test-context.xml");
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // configure to use jetty on localhost with the given port
                restConfiguration().component("jetty").host("localhost").port(portNum);

                // use the rest DSL to define the rest services
                rest("/transactions")
                        .get()
                        .to("direct:openbanktransactions");

                rest("/filter")
                        .get()
                        .to("direct:processTransactions").outType(Transactions.class);

                rest("/totalAmount")
                        .get()
                        .to("direct:processAmountTransactions").outType(Value.class);
            }
        };
    }

    @Test
    public void testInvokeTransactions() {
        String response = template.requestBody("http://localhost:" + portNum + "/transactions", null, String.class);

        assertNotNull(response);
    }

    @Test
    public void testParsingTransactions() {
        ObjectMapper mapper = new ObjectMapper();
        Transactions transactions = null;
        String auth = "Basic dXNlcjp1c2Vy";
        String response = template.requestBodyAndHeader("http://localhost:" + portNum + "/transactions", null, "CamelAuthentication", auth, String.class);

        try {
            transactions = mapper.readValue(response,Transactions.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(transactions.getTransactions().get(0).getThisAccount().getId(), "savings-kids-john");
    }

    @Test
    public void testConsumerTransactions() throws IOException {
        Client client = new Client(Protocol.HTTP);
        Response response = client.handle(new Request(Method.GET,
                "http://localhost:" + portNum + "/transactions"));

        assertEquals(response.getStatus().getCode(), 200);
    }
    @Test
    @Ignore
    @WithMockUser(username = "user", authorities = { "USER" })
    public void testConsumerFilterTransactions() throws IOException {
        Client client = new Client(Protocol.HTTP);
        Response response = client.handle(new Request(Method.GET,
                "http://localhost:" + portNum + "/filter?transactionType=sandbox-payment"));

        assertEquals(response.getStatus().getCode(), 200);
    }
    @Test
    @Ignore
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void testConsumerAmountTransactions() throws IOException {
        Client client = new Client(Protocol.HTTP);
        Response response = client.handle(new Request(Method.GET,
                "http://localhost:" + portNum + "/totalAmount?transactionType=sandbox-payment"));

        assertEquals(response.getStatus().getCode(), 200);
    }

    @Test
    @Ignore
//    @WithMockUser(username = "user", authorities = { "USER" })
    public void testInvokeFilterTransactions() {
        Authentication authToken = createAuthenticationToken("user", "user", "USER");
        SecurityContextHolder.getContext().setAuthentication(authToken);
        Subject subject = new Subject();
        subject.getPrincipals().add(authToken);
        String response = template.requestBodyAndHeader("http://localhost:" + portNum + "/filter?transactionType=sandbox-payment", null, Exchange.AUTHENTICATION, subject, String.class);
        assertNotNull(response);
    }

    @Test
    @Ignore
//    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void testInvokeAmountTransactions() {
        Authentication authToken = createAuthenticationToken("admin", "admin", "ADMIN", "USER");
        Subject subject = new Subject();
        subject.getPrincipals().add(authToken);

        String response = (String) template.requestBodyAndHeader("http://localhost:" + portNum + "/totalAmount?transactionType=sandbox-payment", "", Exchange.AUTHENTICATION, subject);
        assertNotNull(response);
    }

    private Authentication createAuthenticationToken(String username, String password, String... roles) {
        Authentication authToken;
        if (roles != null && roles.length > 0) {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(roles.length);
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
            authToken = new UsernamePasswordAuthenticationToken(username, password, authorities);
        } else {
            authToken = new UsernamePasswordAuthenticationToken(username, password);
        }
        return authToken;
    }


}
