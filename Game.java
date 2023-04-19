import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;


public class Game implements ActionListener
{

    private JFrame frame;
    private JPanel panelText;
    private JPanel panelBtn;
    private JLabel label;

    private JLabel labelPoints;
    private int[] points;

    private JPanel panelMenu;
    private JButton btnUndo;
    private JButton btnNew;
    private JButton btnExit;

    private Color base;

    private JButton[] buttons;
    private boolean xTurn;
    private boolean win;

    private Stack<Integer> stack;


    public Game()
    {
        this.frame = new JFrame();
        this.panelText = new JPanel();
        this.panelBtn = new JPanel();
        this.label = new JLabel();
        this.buttons = new JButton[9];

        this.panelMenu = new JPanel();
        this.btnUndo = new JButton();
        this.btnNew = new JButton();
        this.btnExit = new JButton();

        this.labelPoints = new JLabel();
        this.points = new int[2];//points[0] = x points[1] = o

        init();
        start();
    }

    private void start()
    {
        JOptionPane.showMessageDialog(this.frame, "Welcome to Tic Tac Toe", "Tic Tac Toe" +
                "", JOptionPane.WARNING_MESSAGE);

        this.stack = new Stack<Integer>();
        this.win = false;
        try
        {
            Thread.sleep(500);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        try
        {
            this.label.setText("Loading....");
            Thread.sleep(500);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        double turn = Math.random();
        if (turn > 0.5)
        {
            this.xTurn = true;
            this.label.setText("X turn");
        } else
        {
            this.xTurn = false;
            this.label.setText("O turn");
        }
    }

    private void init()
    {
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(600, 600);
        this.frame.getContentPane().setBackground(new Color(50, 50, 50));
        this.frame.setTitle("Tic Tac Toe");
        this.frame.setLayout(new BorderLayout());
        this.frame.setVisible(true);

        this.btnUndo.addActionListener(this);
        this.btnNew.addActionListener(this);
        this.btnExit.addActionListener(this);

        this.btnUndo.setFont(new Font("Ink Free", Font.BOLD, 20));
        this.btnNew.setFont(new Font("Ink Free", Font.BOLD, 20));
        this.btnExit.setFont(new Font("Ink Free", Font.BOLD, 20));

        this.btnUndo.setText("Undo");
        this.btnNew.setText("New game");
        this.btnExit.setText("Exit");

        this.btnUndo.setFocusable(false);
        this.btnNew.setFocusable(false);
        this.btnExit.setFocusable(false);

        this.panelMenu.add(this.btnUndo);
        this.panelMenu.add(this.btnNew);
        this.panelMenu.add(this.btnExit);

        // this.labelPoints.setBackground(Color.cyan);
        this.labelPoints.setForeground(Color.BLACK);
        this.labelPoints.setFont(new Font("Ink Free", Font.BOLD, 20));
        this.labelPoints.setText("<html>X wins " + points[0] + "<br/>O wins " + points[1] + "</html>");
        this.labelPoints.setHorizontalAlignment(JLabel.CENTER);
        this.labelPoints.setOpaque(true);
        this.panelMenu.add(this.labelPoints);
        //this.panelMenu.setLayout(new BorderLayout());
        // this.panelMenu.setBounds(0, 0, 600, 100);

        this.label.setBackground(new Color(120, 20, 124));
        this.label.setForeground(new Color(25, 255, 0));
        this.label.setFont(new Font("Ink Free", Font.BOLD, 50));
        this.label.setText("Welcome to Tic Tac Toe");
        this.label.setHorizontalAlignment(JLabel.CENTER);
        this.label.setOpaque(true);


        this.panelText.setLayout(new BorderLayout());
        this.panelText.setBounds(0, 0, 600, 100);

        this.panelText.add(this.label);

        this.panelBtn.setLayout(new GridLayout(3, 3));
        this.panelBtn.setBackground(new Color(150, 150, 150));

        for (int i = 0; i < this.buttons.length; i++)
        {
            this.buttons[i] = new JButton();
            this.buttons[i].addActionListener(this);

            this.buttons[i].setFont(new Font("Ink Free", Font.BOLD, 120));
            this.buttons[i].setText("");
            this.buttons[i].setFocusable(false);
            this.panelBtn.add(this.buttons[i]);
        }

        this.base = this.buttons[0].getBackground();

        this.frame.add(this.panelText, BorderLayout.NORTH);
        this.frame.add(this.panelBtn, BorderLayout.CENTER);
        this.frame.add(this.panelMenu, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (!this.win)
        {
            for (int i = 0; i < this.buttons.length; i++)
            {
                if (e.getSource() == this.buttons[i])
                {
                    if (this.buttons[i].getText().equals(""))
                    {
                        if (this.xTurn)
                            this.buttons[i].setText("X");
                        else
                            this.buttons[i].setText("O");

                        this.stack.push(i);
                        check();
                        if (!this.win)
                            nextTurn();
                        if (tie()&&(!this.win))
                            this.label.setText("tie");
                    }
                }
            }
        }
        if (e.getSource() == this.btnNew)
        {
            for (int i = 0; i < this.buttons.length; i++)
            {
                this.buttons[i].setText("");
                this.buttons[i].setBackground(base);
            }
            JOptionPane.showMessageDialog(this.frame, "New game", "New game", JOptionPane.WARNING_MESSAGE);
            start();
        }
        if (e.getSource() == this.btnExit)
        {
            int a = JOptionPane.showConfirmDialog(this.frame, "Are you sure?");
            if (a == JOptionPane.YES_OPTION)
            {
                this.frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }
        if (e.getSource() == this.btnUndo && this.win == false)
        {
            if (!stack.empty())
            {
                this.buttons[stack.pop()].setText("");
                nextTurn();
            }
        }
    }

    private void check()
    {
        if ((buttons[0].getText() == "X") && (buttons[1].getText() == "X") && (buttons[2].getText() == "X"))
        {
            wins(0, 1, 2);
        } else if ((buttons[0].getText() == "X") && (buttons[4].getText() == "X") && (buttons[8].getText() == "X"))
        {
            wins(0, 4, 8);
        } else if ((buttons[0].getText() == "X") && (buttons[3].getText() == "X") && (buttons[6].getText() == "X"))
        {
            wins(0, 3, 6);
        } else if ((buttons[1].getText() == "X") && (buttons[4].getText() == "X") && (buttons[7].getText() == "X"))
        {
            wins(1, 4, 7);
        } else if ((buttons[2].getText() == "X") && (buttons[4].getText() == "X") && (buttons[6].getText() == "X"))
        {
            wins(2, 4, 6);
        } else if ((buttons[2].getText() == "X") && (buttons[5].getText() == "X") && (buttons[8].getText() == "X"))
        {
            wins(2, 5, 8);
        } else if ((buttons[3].getText() == "X") && (buttons[4].getText() == "X") && (buttons[5].getText() == "X"))
        {
            wins(3, 4, 5);
        } else if ((buttons[6].getText() == "X") && (buttons[7].getText() == "X") && (buttons[8].getText() == "X"))
        {
            wins(6, 7, 8);
        } else if ((buttons[0].getText() == "O") && (buttons[1].getText() == "O") && (buttons[2].getText() == "O"))
        {
            wins(0, 1, 2);
        } else if ((buttons[0].getText() == "O") && (buttons[3].getText() == "O") && (buttons[6].getText() == "O"))
        {
            wins(0, 3, 6);
        } else if ((buttons[0].getText() == "O") && (buttons[4].getText() == "O") && (buttons[8].getText() == "O"))
        {
            wins(0, 4, 8);
        } else if ((buttons[1].getText() == "O") && (buttons[4].getText() == "O") && (buttons[7].getText() == "O"))
        {
            wins(1, 4, 7);
        } else if ((buttons[2].getText() == "O") && (buttons[4].getText() == "O") && (buttons[6].getText() == "O"))
        {
            wins(2, 4, 6);
        } else if ((buttons[2].getText() == "O") && (buttons[5].getText() == "O") && (buttons[8].getText() == "O"))
        {
            wins(2, 5, 8);
        } else if ((buttons[3].getText() == "O") && (buttons[4].getText() == "O") && (buttons[5].getText() == "O"))
        {
            wins(3, 4, 5);
        } else if ((buttons[6].getText() == "O") && (buttons[7].getText() == "O") && (buttons[8].getText() == "O"))
        {
            wins(6, 7, 8);
        }
    }
    private boolean tie()
    {
        int num=0;
        for (int i=0;i<this.buttons.length;i++)
        {
            if (!this.buttons[i].getText().equals(""))
            {
                num++;
            }
        }
        return num==9;
    }

    private void wins(int x1, int x2, int x3)
    {
        this.win = true;
        buttons[x1].setBackground(Color.GREEN);
        buttons[x2].setBackground(Color.GREEN);
        buttons[x3].setBackground(Color.GREEN);
        if (xTurn)
        {
            this.label.setText("X win");
            points[0]++;
        } else
        {
            this.label.setText("O win");
            points[1]++;
        }
        this.labelPoints.setText("<html>X wins " + points[0] + "<br/>O wins " + points[1] + "</html>");
    }

    private void nextTurn()
    {
        if (this.xTurn)
            this.xTurn = false;
        else
            this.xTurn = true;

        if (xTurn)
            this.label.setText("X turn");
        else
            this.label.setText("O turn");
    }

}
