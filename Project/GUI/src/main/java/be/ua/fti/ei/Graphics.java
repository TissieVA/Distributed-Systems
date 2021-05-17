package be.ua.fti.ei;

import be.ua.fti.ei.http.HttpRequester;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



//This class will implement a graphical interface where we can add new nodes,removing nodes HTTPrequester
//showing the list of all nodes
//showing the list of all files on the selected node (local and replicated in two groups)
//showing the configuration data of the selected node (i.e., previous and next ID)
public class Graphics
{


    public static void main(String args[])
    {
        JFrame frame = new JFrame("Distributed Systems");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        JPanel panelMenu = new JPanel(); // the panel is not visible in output


        JLabel labeltext = new JLabel("Choose what you would like to do:");


        JButton addNewNode = new JButton("add new Node");
        addNewNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelMenu.removeAll();
                addNewNode(frame);
            }
        });

        panelMenu.add(addNewNode); // Components Added using Flow Layout
        panelMenu.add(labeltext);

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.NORTH, panelMenu);

        frame.setVisible(true);

    }

    private static void addNewNode(JFrame frame)
    {

        JPanel panelName = new JPanel(); // the panel is not visible in output
        JPanel panelSend = new JPanel(); // the panel is not visible in output
        JPanel panelIp = new JPanel();

        JLabel labelName = new JLabel("Give the name of the node:");
        JTextField tfName = new JTextField(16);

        JLabel labelIp = new JLabel("Give the IP of the node:");
        JTextField tfIP = new JTextField(16);

        JButton send = new JButton("Send");
        send.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //do something if the send button is clicked
                String textfieldName = tfName.getText();
                String textfieldIP = tfIP.getText();

                if(textfieldName.length()<2|| textfieldIP.length()<10)
                {
                    System.out.println("no values where added");
                }
                else
                {
                    HttpRequester.POST("localhost:8080/publish","\"ipAddress\":"+textfieldIP);//do there have to be quotation marks around the ip??
                }
            }
        });

        panelName.add(labelName); // Components Added using Flow Layout
        panelName.add(tfName);
        panelSend.add(send);
        panelIp.add(labelIp);
        panelIp.add(tfIP);

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.NORTH, panelName);
        frame.getContentPane().add(BorderLayout.CENTER,panelIp);
        frame.getContentPane().add(BorderLayout.SOUTH,panelSend);
        frame.setVisible(true);
    }
}

