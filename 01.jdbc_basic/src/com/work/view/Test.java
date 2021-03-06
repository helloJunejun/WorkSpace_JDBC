package com.work.view;

import java.util.ArrayList;

import com.work.model.Member;
import com.work.model.MemberDao;
import com.work.model.MemberService;

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
		String grade = service.login("user01", "password01");
		if(grade != null) {
			System.out.println("로그인성공 등급 : " + grade);
		} else {
			System.out.println("로그인 실패 : 회원정보를 확인하시기 바랍니다.");
		}
		
		print("로그인");
		grade = service.login("user05", "password05");
		if(grade != null) {
			System.out.println("로그인성공 등급 : " + grade);
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
		print("등급별 전체회원조회 : 일반회원");
		list = service.getMemberListByGrade("G");
		for (Member member : list) {
			System.out.println(member);
		}
		
		print("등급별 전체회원조회 : 우수회원");
		list = service.getMemberListByGrade("S");
		for (Member member : list) {
			System.out.println(member);
		}

		print("등급별 전체회원조회 : 관리자");
		list = service.getMemberListByGrade("A");
		for (Member member : list) {
			System.out.println(member);
		}
		
		print("임시암호 발급");
		if(service.findMemberPwByEmail("user01", "홍길동", "user01@work.com") != null) {
			System.out.println("[성공] 임시 비밀번호로 변경이 완료되었습니다.");
			dto = service.getMember("user01");
			System.out.println(dto);
		} else {
			System.out.println("[오류] 입력한 회원정보가 잘못되었습니다.");
		}
	
	}
	
	public static void print(String message) {
		System.out.println("\n###" + message);
	}
	
}


