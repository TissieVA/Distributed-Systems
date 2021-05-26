package be.ua.fti.ei;

import be.ua.fti.ei.http.HttpRequester;
import be.ua.fti.ei.http.PublishBody;
import be.ua.fti.ei.sockets.NextPreviousBody;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.util.ArrayList;


//This class will implement a graphical interface where we can add new nodes,removing nodes HTTPrequester
//showing the list of all nodes
//showing the list of all files on the selected node (local and replicated in two groups)
//showing the configuration data of the selected node (i.e., previous and next ID)

public class Graphics
{
    JLayeredPane layeredPane = new JLayeredPane();

    private static final Gson gson = new Gson();

    public static void main(String args[]) throws Exception
    {
        NSConfig nsConfig = NSConfig.load("config.json");
        JFrame frame = new JFrame("Distributed Systems");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,600);
        JPanel panelMenu = new JPanel(); // the panel is not visible in output


        JLabel labeltext = new JLabel("Choose what you would like to do:");


        JButton addNewNode = new JButton("add new Node");
        addNewNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelMenu.removeAll();
                addNewNode(frame,nsConfig);
            }
        });

        panelMenu.add(addNewNode); // Components Added using Flow Layout
        panelMenu.add(labeltext);

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.NORTH, panelMenu);

        frame.setVisible(true);

    }

    private static void addNewNode(JFrame frame,NSConfig nsConfig)
    {

        JPanel panelName = new JPanel(); // the panel is not visible in output
        JPanel panelSend = new JPanel(); // the panel is not visible in output
        JPanel panelIp = new JPanel();

        JLabel labelName = new JLabel("Give the name of the node:");
        JTextField tfName = new JTextField(20);

        JLabel labelIp = new JLabel("Give the IP of the node:");
        JTextField tfIP = new JTextField(20);

        JLabel labelport = new JLabel("Give the multicastport of the node:");
        JTextField tfport = new JTextField(20);

        JLabel labelportunicast = new JLabel("Give the unicastport of the node:");
        JTextField unitf = new JTextField(20);

        JButton send = new JButton("Send");
        send.addActionListener(new ActionListener()
        {
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
                    ArrayList<String> files = new ArrayList<String >();
                    PublishBody publishBody = new PublishBody(textfieldName,files,textfieldIP,tfPort,tfunicastPort);
                    String json = gson.toJson(publishBody);
                    System.out.println("what we are sending to the post:"+nsConfig.getIpAddress() + ":" + nsConfig.getHttpPort() + "/publish");
                    HttpRequester.POST( "http://"+nsConfig.getIpAddress()+ ":" + nsConfig.getHttpPort() + "/publish", json, NextPreviousBody.class);//need to add the ip of the nameserver and port
                }
            }
        });

        panelName.add(labelName); // Components Added using Flow Layout
        panelName.add(tfName);
        panelSend.add(send);
        panelIp.add(labelIp);
        panelIp.add(tfIP);
        panelIp.add(labelport);
        panelIp.add(tfport);
        panelIp.add(labelportunicast);
        panelIp.add(unitf);

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.NORTH, panelName);
        frame.getContentPane().add(BorderLayout.CENTER,panelIp);
        frame.getContentPane().add(BorderLayout.SOUTH,panelSend);
        frame.setVisible(true);
    }



}

