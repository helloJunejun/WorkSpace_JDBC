package com.concert.view;

import java.util.ArrayList;

import com.concert.exception.CommonException;
import com.concert.exception.DuplicateException;
import com.concert.exception.RecordNotFoundException;
import com.concert.model.Member;
import com.concert.service.MemberService;

public class ConcertTest {

	public static void main(String[] args) throws RecordNotFoundException, CommonException, DuplicateException {
		MemberService service = new MemberService();
		ArrayList<Member> list = service.getMemberList();
//		service.initInfo();
		
		service.mainMenu();
	
			
		while(true) {	
			int menuNo = service.inputNumber();
			switch(menuNo) {
			case 1:
				service.loginMenu();
				break;
			
			case 2:
				service.addMemberMenu();
				break;
				
			case 0:
				System.out.println("회원관리 프로그램이 종료됩니다.");
				System.exit(0);
				break;
			default:
				System.out.println("메뉴번호 오류");
				break;
			}
		}
		
	}
	
}
