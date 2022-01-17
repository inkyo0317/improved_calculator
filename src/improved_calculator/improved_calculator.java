package improved_calculator;

import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;

/**
 * @author inkyo 
 * < GUI�� �̿��� ����>
 * 'JAVA ���α׷���' ������ ���� �� '����'�� ���� (+ ���� ��� ���, ������ �켱 ��� ���, ����� ȭ�� ����ȭ)
 *  1. [���� ������ ����]
 *   - ū �гξȿ� �Է��� ���ڰ� ǥ�õǴ� 'screen'�� ���� �� ��� ��ư�� 4*4 ����
 *  2. [�� ��ư�� ���ȭ]
 *   - 'C', '+', '-' ,'*' ,'/' �� ���ȭ
 *  3. [��� ��� ����]
 *  - ���� ��� ��� : �� ȭ�鿡 -��ȣ�� ���� ���� ���ڸ� ���� -����� �ٿ� ������ �Ǵ�
 *  - ������ �켱���� : �ϼ��� ������ ó������ ���Ǹ� ���ϱ�, �����⸦ ���̿� �� �� ���ڸ� ���� �����ϰ�
 *                 ��갪�� ���ϱ�, ������ ������ ���� ���� ��ġ�� ��ġ
 *  4. [��� ó��] 
 *  */

public class improved_calculator extends JFrame {

	// ���� Ʋ ����
	private JTextField screen_input;                                   // ���� ȭ�� �Է� â
	private JTextField screen_output;                                  // ���� ȭ�� ��� â
	private String num = "";                                           // ������ ���ڸ� ��� ���� 
	private String final_input = "";                                   // ���������� �Էµ� ��
	private ArrayList<String> equation = new ArrayList<String>();      // �������� �����ϴ� �迭
	
	public improved_calculator() {
		setLayout(null);                                               // ���̾ƿ� ������ ���X
		
		// �Է� ȭ�� ����
		screen_input = new JTextField();                                                
		screen_input.setEditable(false);                               // ȭ�� ���� �Ұ�
		screen_input.setBounds(8, 10, 270, 50);                        // ȭ�� ��ġ �� ũ��
		screen_input.setBackground(Color.WHITE);                       // ��� �� ����
		screen_input.setHorizontalAlignment(JTextField.RIGHT);         // ������ ����
		screen_input.setFont(new Font("Arial", Font.BOLD, 50));        // ���� ��Ʈ ����
		
		// ȭ�� �Ʒ��� ��ư ����
		JPanel button_panel = new JPanel();                             // ��ư ����
		button_panel.setLayout(new GridLayout(4, 4, 10, 10));           // ��ư ��, ũ��
		button_panel.setBounds(8, 70, 270, 235);                        // ��ư ��ġ
		
		// ��� ȭ�� ����
		screen_output = new JTextField();                                                
		screen_output.setEditable(false);                               // ȭ�� ���� �Ұ�
		screen_output.setBounds(8, 310, 270, 50);                       // ȭ�� ��ġ �� ũ��
		screen_output.setBackground(Color.WHITE);                       // ��� �� ����
		screen_output.setHorizontalAlignment(JTextField.RIGHT);         // ������ ����
		screen_output.setFont(new Font("Arial", Font.BOLD, 50));        // ���� ��Ʈ ����

		// 16���� ��ư�� �� ����(����, ��ȣ) ����Ʈȭ
		// buttons[] ��� 16���� ����
		String button_names[] = { "C", "��", "��", "=", "7", "8", "9", "+", "4", "5", "6", "-", "1", "2", "3", "0" };
		JButton buttons[] = new JButton[button_names.length];
		
		// ���� ���� ��ư���� 0 ����, ������ �Ʒ� ��ư�� 15�� ��
		for (int i = 0; i < button_names.length; i++) {
			buttons[i] = new JButton(button_names[i]);
			buttons[i].setFont(new Font("Sepir", Font.BOLD, 20));
		// 'C'��ư - ������, 1~19 ��ư - ȸ��(���ڴ� ���)
			if (button_names[i] == "C") buttons[i].setBackground(Color.RED);
			else if ((i >= 4 && i <= 6) || (i >= 8 && i <= 10) || (i >= 12 && i <= 15)) buttons[i].setBackground(Color.GRAY);
			else buttons[i].setBackground(Color.ORANGE);
			buttons[i].setForeground(Color.WHITE);
			buttons[i].setBorderPainted(false);
			buttons[i].addActionListener(new PadActionListener());
			button_panel.add(buttons[i]);
		}
		// �гο� ȭ��� 16���� ��ư �߰� 
		add(screen_input);
		add(screen_output);
		add(button_panel);
		
		// ��ü �г� ����
		setTitle("����");
		setVisible(true);
		setSize(300, 400);
		setBackground(Color.BLACK);
		setLocationRelativeTo(null);                                    // ȭ�� ��� ����
		setResizable(false);                                            // ũ�� ����X
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	// ������ �м�
	private void analysing(String input) {
		equation.clear();
	
		// ���� ó������ ����
		for (int i = 0; i < input.length(); i++) {
			char figure = input.charAt(i);
			// �������� �ִ� ���ڵ��� ���� ��ȣ -> num�� �ʱ�ȭ�ϰ� ���� ��ȣ�� ArrayList�� �߰�
			if (figure == '+' || figure == '-' || figure == '��' || figure == '��') {
				equation.add(num);
				num = "";
				equation.add(figure + "");
			} 
			// �������� �ִ� ���ڵ��� ���� -> num�� ���ڰ� �߰��Ǹ� �����ȣ�� �Է½� �ڿ� �����ȣ�� ����
			else {
				num = num + figure;
			}
		}
		equation.add(num);  
		equation.remove(""); 
	}
	// ���� ���ó�� ������� ���� ��� ����
	public double calculate(String input) {
		analysing(input);     // 
		
		double prev = 0;      // 
		double current = 0;   // 
		String mode = "";     // 
		
		// ���
		for (int i = 0; i < equation.size(); i++) {
			String s = equation.get(i);
			// ��Ģ���� ��� ����
			if (s.equals("+")) {
				mode = "add";
			} 
			else if (s.equals("-")) {
				mode = "sub";
			} 
			else if (s.equals("��")) {
				mode = "mul";
			} 
			else if (s.equals("��")) {
				mode = "div";
			} 
			// ������ �켱���� ���� : ���� �������� ���� ����Ѵ�
			else {
				if ((mode.equals("mul") || mode.equals("div")) && !s.equals("+") && !s.equals("-") && !s.equals("��") && !s.equals("��")) {
					Double num1 = Double.parseDouble(equation.get(i - 2));
					Double num2 = Double.parseDouble(equation.get(i));
					Double result = 0.0;
					
					if (mode.equals("mul")) {
						result = num1 * num2;
					} 
					else if (mode.equals("div")) {
						result = num1 / num2;
					}
					
					// ��갪�� ArrayList�� �߰�
					equation.add(i + 1, Double.toString(result));
					
					for (int j = 0; j < 3; j++) {
						equation.remove(i - 2);
					}
					
					i -= 2;	// ������� ���� �ε����� �̵�
				}
			}
		}
		
		for (String s : equation) {
			if (s.equals("+")) {
				mode = "add";
			} 
			else if (s.equals("-")) {
				mode = "sub";
			}  
			else {
				current = Double.parseDouble(s);
				if (mode.equals("add")) {
					prev += current;
				} 
				else if (mode.equals("sub")) {
					prev -= current;
				} 
				else {
					prev = current;
				}
			}
			prev = Math.round(prev * 1000) / 1000.0;  // ȭ��� ���̱� ���� �Ҽ� ��°�ڸ����� �ݿø��ϴ� �޼ҵ�
		}
		return prev;
	}
	// ��� ��� �̺�Ʈ ó��
		class PadActionListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
					
				String operation = e.getActionCommand();  // getActionCommand() : � ��ư�� ���������� Ȯ��
				
				// "C"��ư ���� ��, ��ĭ �ǵ��� ����
				// "="��ư ���� ��, ����� ����ǵ��� ����
				// "+,-,*,/"��ư ���� ��, 
				//    ���� �ʱ⿡ �� ȭ���̰ų� -��ȣ�� �����ϸ�, ������ �ڿ� �߰��ǵ��� ����				
				
				if (operation.equals("C")) {
					screen_input.setText("");
				} 
				// �Է�ȭ��� ���ȭ�� ����ȭ
				else if (operation.equals("=")) {
					String result = Double.toString(calculate(screen_input.getText()));
					screen_output.setText("" + result);
					num = "";
				}
				else if (operation.equals("+") || operation.equals("-") || operation.equals("��") || operation.equals("��")) {
					if (screen_input.getText().equals("") && operation.equals("-")) {
						screen_input.setText(screen_input.getText() + e.getActionCommand());
					} 
					else if (!screen_input.getText().equals("") && !final_input.equals("+") && !final_input.equals("-") && !final_input.equals("��") && !final_input.equals("��")) {
						screen_input.setText(screen_input.getText() + e.getActionCommand());
					}
				} 
				else {
					screen_input.setText(screen_input.getText() + e.getActionCommand());
				}
				// ���������� ���� ��ư ���
				final_input = e.getActionCommand();
			}
		}
	
	public static void main(String[] args) {
		new improved_calculator();
	}

}