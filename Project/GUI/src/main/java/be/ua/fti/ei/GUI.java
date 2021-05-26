package be.ua.fti.ei;

import be.ua.fti.ei.http.HttpRequester;
import be.ua.fti.ei.http.PublishBody;
import be.ua.fti.ei.sockets.NextPreviousBody;
import com.google.gson.Gson;
import org.w3c.dom.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUI extends JFrame{
    private JTabbedPane removeFrame;
    private JPanel panel1;
    private JPanel panel1frame;
    private JPanel panel2Frame;
    private JLabel textpanel2;
    private JTextField tfName;
    private JLabel nodeNameText;
    private JTextField tfIP;
    private JLabel iPNodeText;
    private JTextField tfport;
    private JLabel labelport;
    private JLabel labelportunicast;
    private JTextField unitf;
    private JButton send;
    private JLabel removenodeText;
    private JButton sendButtonRemove;
    private JTextField tfNodeNameRemove;
    private JLabel textNameNodeRemove;
    private JPanel AllNodesPanel;
    private JButton GiveAllNodes;
    private static final Gson gson = new Gson();


    public GUI() throws Exception {
        super("distributed");

        NSConfig nsConfig = NSConfig.load("GUI\\config.json");


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel1);
        this.pack();
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //do something if the send button is clicked
                String textfieldName = tfName.getText();
                String textfieldIP = tfIP.getText();
                int tfPort = Integer.parseInt(tfport.getText());
                int tfunicastPort =Integer.parseInt(unitf.getText());

                if(textfieldName.length()<2|| textfieldIP.length()<10)
                {
                    System.out.println("no values where added");
                }
                else
                {
                    ArrayList<String> files = new ArrayList<String>();
                    PublishBody publishBody = new PublishBody(textfieldName,files,textfieldIP,tfPort,tfunicastPort);
                    String json = gson.toJson(publishBody);
                    System.out.println("what we are sending to the post:"+nsConfig.getIpAddress() + ":" + nsConfig.getHttpPort() + "/publish");
                    HttpRequester.POST( "http://"+nsConfig.getIpAddress()+ ":" + nsConfig.getHttpPort() + "/publish", json, NextPreviousBody.class);//need to add the ip of the nameserver and port
                }
            }
        });
        sendButtonRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String textfieldNameToRemove = tfNodeNameRemove.getText();
                if(textfieldNameToRemove.length()<2)
                {
                    System.out.println("Name to short");
                }
                else
                {
                    System.out.println("http://" + nsConfig.getIpAddress() + ":" + nsConfig.getHttpPort() + "/remove");
                    HttpRequester.GET("http://" + nsConfig.getIpAddress() + ":" + nsConfig.getHttpPort() + "/remove/"+ textfieldNameToRemove);//
                }
            }

        });
        GiveAllNodes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(HttpRequester.GETList("http://" + nsConfig.getIpAddress() + ":" + nsConfig.getHttpPort() + "/nodes",String[].class));

            }
        });
    }
    public static void main(String[] args) throws Exception {
            JFrame frame = new GUI();
            frame.setVisible(true);
    }
}
