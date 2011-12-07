package ru.hse.example;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TestCalc {

	public static void main(String[] args) {
		JFrame frame = new JFrame();

		createContents(frame.getContentPane());

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private static void createContents(Container pane) {
		JPanel inputPanel = new JPanel();
		final JTextField firstInput = new JTextField();
		final JTextField secondInput = new JTextField();

		inputPanel.setLayout(new GridLayout(2, 2));
		inputPanel.add(new JLabel("First operand: "));
		inputPanel.add(firstInput);
		inputPanel.add(new JLabel("Second operand: "));
		inputPanel.add(secondInput);

		JPanel operationsPanel = new JPanel();
		JButton addButton = new JButton("+");
		JButton subButton = new JButton("-");
		JButton mulButton = new JButton("*");
		JButton divButton = new JButton("/");

		operationsPanel.setLayout(new FlowLayout());
		operationsPanel.add(addButton);
		operationsPanel.add(subButton);
		operationsPanel.add(mulButton);
		operationsPanel.add(divButton);

		final JTextField resultField = new JTextField("Result");

		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		pane.add(inputPanel);
		pane.add(operationsPanel);
		pane.add(resultField);

		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resultField.setText(String.valueOf(Integer.valueOf(firstInput
						.getText()) + Integer.valueOf(secondInput.getText())));
			}
		});

		subButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resultField.setText(String.valueOf(Integer.valueOf(firstInput
						.getText()) - Integer.valueOf(secondInput.getText())));
			}
		});

		mulButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resultField.setText(String.valueOf(Integer.valueOf(firstInput
						.getText()) * Integer.valueOf(secondInput.getText())));
			}
		});
		divButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resultField.setText(String.valueOf(Integer.valueOf(firstInput
						.getText()) / Integer.valueOf(secondInput.getText())));
			}
		});
	}
}
