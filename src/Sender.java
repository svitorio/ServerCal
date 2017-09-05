import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by zero on 04/09/17.
 */
public class Sender implements Runnable {

    private String result, subject;

    public Sender(String result, String subject) {
        this.result = result;
        this.subject = subject;
    }

    @Override
    public void run() {
        try {
            sender(result, subject);
        } catch (JMSException e) { /*e.printStackTrace(); */}
    }

    private static void sender(String result, String subject) throws JMSException {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
        Connection connection = connectionFactory.createConnection();
        try {
            connection.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        //Creating a non transactional session to send/receive JMS message.
        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

        //Destination represents here our queue 'JCG_QUEUE' on the JMS server.
        //The queue will be created automatically on the server.
        Destination destination = session.createQueue(subject);

        // MessageProducer is used for sending messages to the queue.
        MessageProducer producer = session.createProducer(destination);


        // We will send a small text message saying 'Hello World!!!'
        TextMessage message = session
                .createTextMessage(result);

        // Here we are sending our message!
        producer.send(message);

        System.out.println("JCG printing@@ '" + message.getText() + "'");
        connection.close();
        Receiver receiver = new Receiver();
        receiver.initializa();
    };;;;;;;;;;;;


}
