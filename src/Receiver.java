import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by desenvolverdor on 04/09/17.
 */
public class Receiver {

    final static String subject = "soma";
    public static void main(String[] args) throws JMSException {
        // Getting JMS connection from the server

        initializa();

        // connection.close();
    }

    public static void initializa() throws JMSException {
        System.out.println("...");

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        // Creating session for seding messages
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        // Getting the queue 'JCG_QUEUE'
        Destination destination = session.createQueue(subject);

        // MessageConsumer is used for receiving (consuming) messages
        MessageConsumer consumer = session.createConsumer(destination);

        // Here we receive the message.
        Message message = consumer.receive();

        // We will be using TestMessage in our example. MessageProducer sent us a TextMessage
        // so we must cast to it to get access to its .getText() method.
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            System.out.println("Received message '" + textMessage.getText() + "'");
            connection.close();
            calculo(textMessage.getText());
            // connection.start();
        }
        connection.close();
    }

   private static void calculo(String text) throws JMSException {

        //Sender s = new Sender();
        int qtd = text.length();
        String[] pala = new String[qtd];
        int i,j=0;
        String[] v = new String[2];
        v[0]="";
        v[1]="";
        String oper="";
        for (i = 0; i < qtd; i++) {
            pala[i] = Character.toString(text.charAt(i));
            System.out.println(pala[i]);
            if(!pala[i].equals("+") && !pala[i].equals("-")) {
                v[j] += pala[i];
            }
            else{
                oper = pala[i];
                j++;
            }
        }
        int valor1, valor2, result;
        valor1 = Integer.parseInt(v[0]);
        valor2 = Integer.parseInt(v[1]);
        if(oper.equals("+")){
            result = valor1 + valor2;
            System.out.println("Soma="+result);
            new Thread(new Sender("" + result, "resultsoma")).start();
            //s.sender(""+result,"resultsoma");
        }
        else{
            result = valor1 - valor2;
            System.out.println("Subtracao="+result);

            //TODO
            new Thread(new Sender("" + result, "resultsubt")).start();
            //s.sender(""+result,"resultsubt");
        }

       // int a = Integer.parseInt(textMessage.getText());
        //sender(""+a);
    }


}
