package com.work.model;
/*
	 ## JDBC 관련 클래스 의존관계
		1. Test 
		=> MemberService#login(아이디, 비밀번호) : 등급
		=> MemberDao#login(아이디, 비밀번호) : 등급 : 메서드 형식 구현
*/

import java.util.ArrayList;

import com.work.util.Utility;

public class MemberService {
	/** MemberDao 객체 생성 */
	private MemberDao dao = new MemberDao();
	
	/**
	 * 로그인
	 * @param memeberId 아이디
	 * @param memberPw 비밀번호
	 * @return 회원등급, 미존재시 null
	 */
	public String login(String memberId, String memberPw) {
		String grade = dao.login(memberId,  memberPw);
		if(grade != null) {
			return grade;
		}
		
		return null;
	}
	
	public Member getMember(String memberId) {
		
		return dao.selectOne(memberId);
	}
//	## 전체회원조회
//	=> MemberService#getMemberList() : ArrayList<회원>
//	=> MemberDao#selectList(): ArrayList<회원>
	public ArrayList<Member> getMemberList() {
		
		return dao.selectList();
	}
	
//	## 회원등급별 전체조회 	
//	=> MemberService#getMemberListByGrade(등급) : ArrayList<회원>
//	=> MemberDao#selectListByGrade(등급): ArrayList<회원>
	public ArrayList<Member> getMemberListByGrade(String grade) {
		return dao.selectListByGrade(grade);
	}
	
	public String findMemberPwByEmail(String memberId, String name, String email) {
		// 1. dao 해당회원 존재여부 체킹
		Utility util = new Utility();
		// 2.
//		if(존재여부 체킹) {
//			2. 임시암호 발급 : java utility 구현
//			3. dao(아이디[, 이름, 이메일,] 임시암호변경암호) 변경 요청
//			4. 비밀번호찾기(임시암호변경) 성공 : 임시암호 반환
//		}
		if(dao.selectMemberPwByEmail(memberId, name, email)) {
			dao.updateMemberPw(memberId, util.getSecureNumberString());
			
			return util.getSecureNumberString();
			
			
		}
		
		return null;

	}
	
}
