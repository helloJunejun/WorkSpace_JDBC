package com.concert.view;

import java.util.ArrayList;

import com.concert.model.Member;
import com.concert.service.MemberService;

public class Test {
	/*
	 * ## JDBC 관련 클래스 의존관계
		1. Test 
		=> MemberService#login(아이디, 비밀번호) : 등급
		=> MemberDao#login(아이디, 비밀번호) : 등급 : 메서드 형식 구현
	 */
	public static void main(String[] args) {
		// MemberService 객체 생성
		MemberService service = new MemberService();
		
		print("로그인");
		String mobile = service.login("user01", "password01");
		if(mobile != null) {
			System.out.println("로그인성공 등급 : " + mobile);
		} else {
			System.out.println("test 로그인 실패 : 회원정보를 확인하시기 바랍니다.");
		}
		
		print("로그인");
		mobile = service.login("user04", "password04");
		if(mobile != null) {
			System.out.println("로그인성공 등급 : " + mobile);
		} else {
			System.out.println("로그인 실패 : 회원정보를 확인하시기 바랍니다.");
		}
		
		print("회원상세조회 : user01");
		Member dto = service.getMember("user01");
		if(dto != null) {
			System.out.println(dto);
		} else {
			System.out.println("조회 실패 : 회원정보가 존재하지 않습니다.");
		}
		
		print("회원전체조회");
		ArrayList<Member> list = service.getMemberList();
		for(Member member : list) {	
			System.out.println(member);
		}
	}
	
	public static void print(String message) {
		System.out.println("\n###" + message);
	}
	
}


