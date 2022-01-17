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
 * < GUI를 이용한 계산기>
 * 'JAVA 프로그래밍' 과목의 과제 중 '계산기'를 개선 (+ 음수 계산 기능, 연산자 우선 계산 기능, 입출력 화면 차별화)
 *  1. [계산기 프레임 제작]
 *   - 큰 패널안에 입력한 숫자가 표시되는 'screen'과 숫자 및 계산 버튼을 4*4 제작
 *  2. [각 버튼의 기능화]
 *   - 'C', '+', '-' ,'*' ,'/' 의 기능화
 *  3. [계산 방식 설정]
 *  - 음수 계산 기능 : 빈 화면에 -부호를 먼저 쓰고 숫자를 쓰면 -기능을 붙여 음수로 판단
 *  - 연산자 우선순위 : 완성된 계산식을 처음부터 살피며 곱하기, 나누기를 사이에 둔 두 숫자를 먼저 연산하고
 *                 계산값을 곱하기, 나누기 연산자 이전 숫자 위치에 배치
 *  4. [계산 처리] 
 *  */

public class improved_calculator extends JFrame {

	// 계산기 틀 제작
	private JTextField screen_input;                                   // 계산기 화면 입력 창
	private JTextField screen_output;                                  // 계산기 화면 출력 창
	private String num = "";                                           // 계산식의 숫자를 담는 변수 
	private String final_input = "";                                   // 마지막으로 입력된 값
	private ArrayList<String> equation = new ArrayList<String>();      // 계산과정을 저장하는 배열
	
	public improved_calculator() {
		setLayout(null);                                               // 레이아웃 관리자 사용X
		
		// 입력 화면 제작
		screen_input = new JTextField();                                                
		screen_input.setEditable(false);                               // 화면 편집 불가
		screen_input.setBounds(8, 10, 270, 50);                        // 화면 위치 및 크기
		screen_input.setBackground(Color.WHITE);                       // 배경 색 지정
		screen_input.setHorizontalAlignment(JTextField.RIGHT);         // 오른쪽 정렬
		screen_input.setFont(new Font("Arial", Font.BOLD, 50));        // 글자 폰트 설정
		
		// 화면 아래에 버튼 제작
		JPanel button_panel = new JPanel();                             // 버튼 생성
		button_panel.setLayout(new GridLayout(4, 4, 10, 10));           // 버튼 수, 크기
		button_panel.setBounds(8, 70, 270, 235);                        // 버튼 위치
		
		// 출력 화면 제작
		screen_output = new JTextField();                                                
		screen_output.setEditable(false);                               // 화면 편집 불가
		screen_output.setBounds(8, 310, 270, 50);                       // 화면 위치 및 크기
		screen_output.setBackground(Color.WHITE);                       // 배경 색 지정
		screen_output.setHorizontalAlignment(JTextField.RIGHT);         // 오른쪽 정렬
		screen_output.setFont(new Font("Arial", Font.BOLD, 50));        // 글자 폰트 설정

		// 16개의 버튼에 들어갈 문자(숫자, 기호) 리스트화
		// buttons[] 요소 16개로 선언
		String button_names[] = { "C", "×", "÷", "=", "7", "8", "9", "+", "4", "5", "6", "-", "1", "2", "3", "0" };
		JButton buttons[] = new JButton[button_names.length];
		
		// 왼쪽 위의 버튼부터 0 시작, 오른쪽 아래 버튼이 15로 끝
		for (int i = 0; i < button_names.length; i++) {
			buttons[i] = new JButton(button_names[i]);
			buttons[i].setFont(new Font("Sepir", Font.BOLD, 20));
		// 'C'버튼 - 빨간색, 1~19 버튼 - 회색(숫자는 흰색)
			if (button_names[i] == "C") buttons[i].setBackground(Color.RED);
			else if ((i >= 4 && i <= 6) || (i >= 8 && i <= 10) || (i >= 12 && i <= 15)) buttons[i].setBackground(Color.GRAY);
			else buttons[i].setBackground(Color.ORANGE);
			buttons[i].setForeground(Color.WHITE);
			buttons[i].setBorderPainted(false);
			buttons[i].addActionListener(new PadActionListener());
			button_panel.add(buttons[i]);
		}
		// 패널에 화면과 16개의 버튼 추가 
		add(screen_input);
		add(screen_output);
		add(button_panel);
		
		// 전체 패널 편집
		setTitle("계산기");
		setVisible(true);
		setSize(300, 400);
		setBackground(Color.BLACK);
		setLocationRelativeTo(null);                                    // 화면 가운데 고정
		setResizable(false);                                            // 크기 조절X
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	// 계산과정 분석
	private void analysing(String input) {
		equation.clear();
	
		// 계산식 처리과정 원리
		for (int i = 0; i < input.length(); i++) {
			char figure = input.charAt(i);
			// 계산과정에 있는 문자들이 연산 기호 -> num을 초기화하고 연산 기호를 ArrayList에 추가
			if (figure == '+' || figure == '-' || figure == '×' || figure == '÷') {
				equation.add(num);
				num = "";
				equation.add(figure + "");
			} 
			// 계산과정에 있는 문자들이 숫자 -> num에 숫자가 추가되며 연산기호를 입력시 뒤에 연산기호가 붙음
			else {
				num = num + figure;
			}
		}
		equation.add(num);  
		equation.remove(""); 
	}
	// 위의 계산처리 방식으로 연산 기능 설정
	public double calculate(String input) {
		analysing(input);     // 
		
		double prev = 0;      // 
		double current = 0;   // 
		String mode = "";     // 
		
		// 계산
		for (int i = 0; i < equation.size(); i++) {
			String s = equation.get(i);
			// 사칙연산 모드 설정
			if (s.equals("+")) {
				mode = "add";
			} 
			else if (s.equals("-")) {
				mode = "sub";
			} 
			else if (s.equals("×")) {
				mode = "mul";
			} 
			else if (s.equals("÷")) {
				mode = "div";
			} 
			// 연산자 우선순위 적용 : 곱셈 나눗셈을 먼저 계산한다
			else {
				if ((mode.equals("mul") || mode.equals("div")) && !s.equals("+") && !s.equals("-") && !s.equals("×") && !s.equals("÷")) {
					Double num1 = Double.parseDouble(equation.get(i - 2));
					Double num2 = Double.parseDouble(equation.get(i));
					Double result = 0.0;
					
					if (mode.equals("mul")) {
						result = num1 * num2;
					} 
					else if (mode.equals("div")) {
						result = num1 / num2;
					}
					
					// 계산값을 ArrayList에 추가
					equation.add(i + 1, Double.toString(result));
					
					for (int j = 0; j < 3; j++) {
						equation.remove(i - 2);
					}
					
					i -= 2;	// 결과값이 생긴 인덱스로 이동
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
			prev = Math.round(prev * 1000) / 1000.0;  // 화면상에 보이기 위해 소수 넷째자리에서 반올림하는 메소드
		}
		return prev;
	}
	// 계산 기능 이벤트 처리
		class PadActionListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
					
				String operation = e.getActionCommand();  // getActionCommand() : 어떤 버튼이 눌러졌는지 확인
				
				// "C"버튼 누를 시, 빈칸 되도록 설정
				// "="버튼 누를 시, 결과값 도출되도록 설정
				// "+,-,*,/"버튼 누를 시, 
				//    만약 초기에 빈 화면이거나 -부호가 존재하면, 계산식이 뒤에 추가되도록 설정				
				
				if (operation.equals("C")) {
					screen_input.setText("");
				} 
				// 입력화면과 출력화면 차별화
				else if (operation.equals("=")) {
					String result = Double.toString(calculate(screen_input.getText()));
					screen_output.setText("" + result);
					num = "";
				}
				else if (operation.equals("+") || operation.equals("-") || operation.equals("×") || operation.equals("÷")) {
					if (screen_input.getText().equals("") && operation.equals("-")) {
						screen_input.setText(screen_input.getText() + e.getActionCommand());
					} 
					else if (!screen_input.getText().equals("") && !final_input.equals("+") && !final_input.equals("-") && !final_input.equals("×") && !final_input.equals("÷")) {
						screen_input.setText(screen_input.getText() + e.getActionCommand());
					}
				} 
				else {
					screen_input.setText(screen_input.getText() + e.getActionCommand());
				}
				// 마지막으로 누른 버튼 기억
				final_input = e.getActionCommand();
			}
		}
	
	public static void main(String[] args) {
		new improved_calculator();
	}

}