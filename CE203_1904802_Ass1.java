package Assignment1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CE203_1904802_Ass1 {
    public static void main(String[] args) {
        UserIDs frame = new UserIDs();
        frame.setVisible(true);
    }
}

class UserIDs extends JFrame {
    int size = 400;

    JTextArea list_area;
    JTextField input_id;
    JTextField red;
    JTextField green;
    JTextField blue;

    private JPanel input_panel;
    private JPanel display_panel;
    private JPanel action_panel;

    public UserIDs() {
        //SETTING UP WINDOW
        setSize(size + 400, size + 100);
        setVisible(true);
        setTitle("IDs");
        setResizable(false); //I set this to false to avoid weird stretches of the frame which creates big empty spaces
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //CREATING PANELS
        input_panel = new JPanel();
        display_panel = new JPanel();
        action_panel = new JPanel();

        //CREATING PANEL ELEMENTS (Buttons, text input, text area)
        JButton add_id = new JButton("Add");
        JButton display_id = new JButton("Show IDs");
        JButton remove_matching_id = new JButton("Remove");
        JButton sort_list = new JButton("Sort");
        JButton clear = new JButton("Clear");

        input_id = new JTextField(10);

        red = new JTextField(2);
        red.setText("0");
        red.setBackground(new Color(255, 84, 0, 255));

        green = new JTextField(2);
        green.setText("0");
        green.setBackground(new Color(154, 255,0));

        blue = new JTextField(2);
        blue.setText("0");
        blue.setBackground(new Color(0, 218, 255));

        list_area = new JTextArea();
        list_area.setFont(list_area.getFont().deriveFont(20f)); //increase size of font
        list_area.setEditable(false); //the user should not be able to change the content of this JTextArea
        list_area.setLineWrap(true);
        list_area.setRows(size/27);
        list_area.setColumns(size/10);

        //ADDING GUI ELEMENTS TO PANELS
        input_panel.add(red);
        input_panel.add(green);
        input_panel.add(blue);
        input_panel.add(input_id, BorderLayout.NORTH);
        input_panel.add(add_id, BorderLayout.SOUTH);
        input_panel.add(remove_matching_id, BorderLayout.SOUTH);

        //adding the list_area to a scrollable panel
        JScrollPane scroll = new JScrollPane(list_area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.createVerticalScrollBar();
        display_panel.add(scroll);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0x555555), 5)); //customizing the look of the border

        action_panel.add(display_id);
        action_panel.add(sort_list);
        action_panel.add(clear);

        //DECORATING THE FRAME
        Color background = new Color(108, 168, 158); //changing the color of the three panels
        input_panel.setBackground(background);
        display_panel.setBackground(background);
        action_panel.setBackground(background);

        Color background2 = new Color(226, 226, 226);
        list_area.setBackground(background2); //changing the color of the list_area

        //ADDING LISTENERS TO BUTTONS
        add_id.addActionListener(new ButtonHandler(this,1));
        display_id.addActionListener(new ButtonHandler(this,2));
        remove_matching_id.addActionListener(new ButtonHandler(this,3));
        sort_list.addActionListener(new ButtonHandler(this,4));
        clear.addActionListener(new ButtonHandler(this,5));

        //ADDING PANELS TO FRAME
        add(input_panel, BorderLayout.NORTH);
        add(display_panel, BorderLayout.CENTER);
        add(action_panel, BorderLayout.SOUTH);

        setVisible(true);
    }

    //This method is used to print the ids in the list_area. Used a couple of times throughout the program
    public void printIDs(){

        Color color;

        if(ID.listID.size() != 0){//checking if the list is not empty

            try{
                color = new Color(Integer.parseInt(this.red.getText()), Integer.parseInt(this.green.getText()), Integer.parseInt(this.blue.getText()));
                this.list_area.append("IDs will be printed with color " + "[r=" + color.getRed() + ", g=" + color.getGreen() + ", b=" + color.getBlue() + "]");
                this.list_area.append("\n");

            }catch (IllegalArgumentException e){ //Error handling for the RGB values

                color = new Color(0);
                this.list_area.append("RGB values are only in 0-255 range. Black color was chosen");
                this.list_area.append("\n");
            }

            this.list_area.setForeground(color);
            this.list_area.append("\n");

            for(ID id : ID.listID){
                list_area.append(id+"\n");
            }

        }else{
            this.list_area.append("There are no IDs to show");
        }

        this.red.setText("0");
        this.green.setText("0");
        this.blue.setText("0");


    }
}

class ButtonHandler implements ActionListener {

    UserIDs theApp;
    int action;

    ButtonHandler(UserIDs app, int action){
        this.theApp = app;
        this.action = action;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if(action == 1){ //action 1 = add id to list

            theApp.list_area.setForeground(new Color(0));
            theApp.list_area.setText("");
            try{

                ID id =  new ID(theApp.input_id.getText());
                ID.listID.add(id); //add new ID object to list
                theApp.list_area.append(id + " added successfully");

                theApp.input_id.setText("");

            }catch (NumberFormatException e){

                theApp.list_area.setText("");
                theApp.list_area.append(theApp.input_id.getText() + " is not valid. ID must contain ONLY 6 digits");
                //JOptionPane.showMessageDialog(null, input_id.getText() + " is not valid. ID must contain ONLY 6 digits");

            }catch (InvalidIDFormat inID){ //Custom made Error for when the ID has more than 6 digits or it is negative

                theApp.list_area.setText("");
                theApp.list_area.append(theApp.input_id.getText() + " is not valid. ID must contain ONLY 6 digits");

            }

        }else if(action == 2){ // action 2 = display list inside text area
            theApp.list_area.setText("");
            theApp.printIDs();

        }else if(action == 3){ // action 3 = delete all all ids that match the user given id
            boolean control = false;
            theApp.list_area.setForeground(new Color(0));

            if(ID.listID.size()>0){

                try{

                    ID remove = new ID(theApp.input_id.getText());
                    for(int i=0; i<ID.listID.size(); i++){
                        if(remove.equals(ID.listID.get(i))) {
                            ID.listID.remove(ID.listID.get(i));
                            control = true;
                            i--;
                        }
                    }
                    if(!control){
                        theApp.list_area.setText("");
                        theApp.list_area.append("No match found");
                    }else{
                        theApp.input_id.setText("");
                        theApp.list_area.setText("");
                        theApp.list_area.append("All occurrences of " + remove + " have been removed");
                    }

                }catch (InvalidIDFormat inID){

                    theApp.list_area.setText("");
                    theApp.list_area.append(inID.getMessage());

                }catch (NumberFormatException e){

                    theApp.list_area.setText("");
                    theApp.list_area.append(theApp.input_id.getText() + " is not valid. ID must contain ONLY 6 digits");

                }

            }else{

                theApp.list_area.setText("");
                theApp.list_area.append("No match found");

            }

        }else if(action == 4){ // action 4 = sort list
            theApp.list_area.setForeground(new Color(0));
            theApp.list_area.setText("");

            if(ID.listID.size() == 0){
                theApp.list_area.append("The list is empty, no element to sort");
            }else{
                theApp.list_area.append("The list has been sorted in ascending order\n\n");
                //TODO print the sorted list after clicking sort
                Collections.sort(ID.listID);
                theApp.printIDs();
            }

        }else if(action == 5){ // action 5 = delete all ids from list

            theApp.list_area.setForeground(new Color(0));
            theApp.list_area.setText("");
            ID.listID.removeAll(ID.listID);
            theApp.list_area.append("All IDs have been removed");

        }
    }
}

// Incomplete ID class for CE203 Assignment 1
// Date: 12/10/2020
// Author: f.Doctor
class ID implements Comparable<ID>{

    // id attribute
    int id = 000000;
    Color color = new Color(0,0,0);
    static List<ID> listID = new ArrayList<ID>(); //creating a static List<ID> so that it is accessible from anywhere in the program

    // constructor should input an ID as a String or int and set it to the attribute id - to be modified
    public ID(String ID) throws InvalidIDFormat, NumberFormatException
    {

        if(Integer.parseInt(ID) == Integer.parseInt(ID)){
            this.id = Integer.parseInt(ID);

            if( (ID.length() != 6) || ( this.id < 0 ) ){
                throw new InvalidIDFormat("ID must contain 6 digits"); //throwing custom made error for when the ID has more than 6 digits or it is negative
            }

        }else{
            String msg = ID + " is not valid. ID must contain ONLY 6 digits";
            throw new NumberFormatException(msg); //this takes care of string inputs
        }
    }

    // gets a stored ID
    public int getID() {
        return id;
    }

    // sets the input parameter to an ID this can be modified to input a string in which case you will need to convert
    // the parameter to an int
    public void setID(int inputID) {
        id = inputID;
    }


    @Override
    // method used for comparing ID objects based on stored ids, you need to complete the method
    public int compareTo(ID o) {
        if(this.id < o.id){
            return -1;
        }else if(this.id == o.id){
            //System.out.println("ID1: " + this.id + " and ID2: " + o.id + " are equal");
            return 0;
        }else{
            return 1;
        }
    }

    public boolean equals(ID o){
        if(this.id == o.id){
            return true;
        }
        return false;
    }

    // outputs a string representation of the object
    public String toString()
    {
        return String.format("ID = " + "%06d", id); // With this String.format every ID will be 6 digits long. i.e. 0 will be 000000
        //return ("ID = %08d%id" + id);
    }
}

class InvalidIDFormat extends Exception { //custom made exception

    public InvalidIDFormat(String error){
        super(error);
    }

}
