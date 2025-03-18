package work.dduo.ans.qxy;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class StudentFrame extends JFrame implements ActionListener {
    JTextArea showArea;
    JTextField inputName, inputScore;
    JButton button;
    TreeSet<Student> treeSet;

    StudentFrame() {
        treeSet = new TreeSet<Student>(); // 使用无参数构造方法创建treeSet
        showArea = new JTextArea();
        showArea.setFont(new Font("", Font.BOLD, 20));
        inputName = new JTextField(5);
        inputScore = new JTextField(5);
        button = new JButton("确定");
        button.addActionListener(this);

        JPanel pNorth = new JPanel();
        pNorth.add(new JLabel("Name:"));
        pNorth.add(inputName);
        pNorth.add(new JLabel("Score:"));
        pNorth.add(inputScore);
        pNorth.add(button);

        add(pNorth, BorderLayout.NORTH);
        add(new JScrollPane(showArea), BorderLayout.CENTER);

        setSize(300, 320);
        setVisible(true);
        validate();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        String name = inputName.getText();
        int score = 0;
        try {
            score = Integer.parseInt(inputScore.getText().trim());
            if (name.length() > 0) {
                Student stu = new Student(name, score);
                treeSet.add(stu); // treeSet添加stu
                show(treeSet);
            }
        } catch (NumberFormatException exp) {
            inputScore.setText("请输入数字字符");
        }
    }

    public void show(TreeSet<Student> tree) {
        showArea.setText(null);
        Iterator<Student> te = treeSet.iterator();
        while (te.hasNext()) {
            Student stu = te.next(); // te返回其中的下一个元素
            showArea.append("Name:" + stu.getName() + " Score: " + stu.getScore() + "\n");
        }
    }

    public static void main(String args[]) {
        new StudentFrame();
    }
}

class Student implements Comparable<Student> {
    String name;
    int score;

    Student(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public int compareTo(Student b) {
        int m = this.score - b.score;
        if (m != 0)
            return m;
        else
            return 1;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }
}