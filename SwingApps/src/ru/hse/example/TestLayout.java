package ru.hse.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

public class TestLayout {

	public static void main(String[] args) {
		JFrame frame = new JFrame();

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().setBackground(Color.white);
		createContentsGroup(frame.getContentPane());


		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private static void createContentsFlow(Container pane) {
		pane.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

		pane.add(new JButton("Button1"));
		pane.add(new JButton("Button2"));
		pane.add(new JButton("Button3"));
	}

	private static void createContentsBox(Container pane) {
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

		JButton b1 = new JButton("Button1");
		JButton b2 = new JButton("Button2");
		JButton b3 = new JButton("Button3");

		b1.setAlignmentX(Component.LEFT_ALIGNMENT);
		b2.setAlignmentX(Component.CENTER_ALIGNMENT);
		b3.setAlignmentX(Component.RIGHT_ALIGNMENT);
		pane.add(b1);
		pane.add(Box.createRigidArea(new Dimension(10,10)));
		pane.add(b2);
		pane.add(Box.createVerticalGlue());
		pane.add(b3);

	}

	private static void createContentsGrid(Container pane) {
		pane.setLayout(new GridLayout(2,2, 10, 5));

		pane.add(new JButton("Button1"));
		pane.add(new JButton("Button2"));
		pane.add(new JButton("Button3"));
	}

	private static void createContentsBorder(Container pane) {
		pane.setLayout(new BorderLayout(10, 5));

		pane.add(new JButton("Button1"), BorderLayout.CENTER);
		pane.add(new JButton("Button2"), BorderLayout.LINE_START);
		pane.add(new JButton("Button3"), BorderLayout.PAGE_END);
	}

	private static void createContentsGridBag(Container pane) {
		pane.setLayout(new GridBagLayout());

		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 0;
		c1.gridy = 0;
		c1.gridwidth  = 3;
		c1.fill = GridBagConstraints.BOTH;

		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 1;
		c2.gridy = 1;

		GridBagConstraints c3 = new GridBagConstraints();
		c3.gridx = 2;
		c3.gridy = 2;

		pane.add(new JButton("Button1"), c1);
		pane.add(new JButton("Button2"), c2);
		pane.add(new JButton("Button3"), c3);
	}

	private static void createContentsGroup(Container pane) {
		GroupLayout layout = new GroupLayout(pane);
		pane.setLayout(layout);

		JButton b1 = new JButton("Button1");
		JButton b2 = new JButton("Button2");
		JButton b3 = new JButton("Button3");

		pane.add(b1);
		pane.add(b2);
		pane.add(b3);

		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(b1)
						.addComponent(b2))
				.addComponent(b3));

		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addComponent(b1)
				.addGroup(
					layout.createParallelGroup()
						.addComponent(b2)
						.addComponent(b3)));
	}
}
