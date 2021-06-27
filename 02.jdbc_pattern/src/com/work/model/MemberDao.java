package com.work.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * ## dAO pattern
 * -- C
 * -- R
 * -- U
 * -- D
 * 
 * ## jdbc 프로그래밍 기본절차 (순서)
1. jdbc driver 로딩 : 생성자 수행
2. db 서버연결 : url, user, password  => Connection
3. 연결된 서버와 통로 개설 => Statement, PreparedStatement, CallableStatement
4. 통로이용 sql 실행 요청
5. 실행결과 처리
6. 자원해제
 *
 */
public class MemberDao {
	// FactoryDao 객체 멤버변수 선언 및 할당 : 구현
	private FactoryDao factory = FactoryDao.getInstance();
	
	private MemberDao() { //1. singleton pattern 첫번째 규칙 private 생성자
	}
	
	private static MemberDao instance = new MemberDao(); // 3. private static 클래스이름 instance = new 클래스이름(); 
	
	public static MemberDao getInstance() { // 2. instance 반환 메서드
		return instance;
	}
	
	
	/**
	 * 로그인
	 * 
	2. db 서버연결 : url, user, password  => Connection
	3. 연결된 서버와 통로 개설 => Statement, PreparedStatement, CallableStatement
	4. 통로이용 sql 실행 요청
		// C U D => 레코드 추가, 변경, 삭제 => 수행결과 적용된 레코드 수 반환
		int rows = stmt.executeUpdate(sql);

		// R => 조회 => 결과 여러개(0~n) 반환
		ResultSet rs = stmt.executeQuery(sql);
	
	5. 실행결과 처리
	6. 자원해제
	 * @param memeberId 아이디
	 * @param memberPw 비밀번호
	 * @return 회원등급, 미존재시 null
	 */
	public String login2(String memberId, String memberPw) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = factory.getConnection();
			stmt = conn.createStatement();
		
			String sql = "select grade from member where member_id ='" + memberId+ "' and member_pw ='" + memberPw + "'";
			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				String grade = rs.getString("grade");
				return grade;
			}
			
		} catch (SQLException e) {
			System.out.println("[오류] 로그인");
			e.printStackTrace();
		} finally {
			factory.close(conn,  stmt, rs);
		}
		return null;
	}
	/**
	 * ## PreparedStatement를 이용한 로그인 처리
	 * @param memberId
	 * @param memberPw
	 * @return
	 */
	public String login(String memberId, String memberPw) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;	
		try {
			conn = factory.getConnection();
			String sql = "select grade from member where member_id =? and member_pw =?";
			stmt = conn.prepareStatement(sql);
			
			
			stmt.setString(1, memberId);
			stmt.setString(2, memberPw);
		
			rs = stmt.executeQuery();
			
			// 5.
			if(rs.next()) {
				String grade = rs.getString("grade");
				return grade;
			}
	
		} catch (SQLException e) {
			System.out.println("[오류] 로그인");
			e.printStackTrace();
		} finally {
			factory.close(conn,  stmt, rs);
		}
		return null;
	}
	public Member selectOne(String memberId) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;	
		try {
			conn = factory.getConnection();
			
			String sql = "select * from member where member_id = ?"; // 
			stmt = conn.prepareStatement(sql);
			
			
			stmt.setString(1, memberId);
					
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				String memberPw = rs.getString("member_pw");
				String name = rs.getString("name");
				String mobile = rs.getString("mobile");
				String email = rs.getString("email");
				String entryDate = rs.getString("entry_date");
				String grade = rs.getString("grade");
				int mileage = rs.getInt("mileage");
				String manager = rs.getString("manager");
				
				Member dto = new Member(memberId, memberPw, name, mobile, email, entryDate, grade, mileage, manager);
				return dto; 
			}
		
			
		} catch (SQLException e) {
			System.out.println("[오류] 회원상세조회");
			e.printStackTrace();
		} finally {
			factory.close(conn,  stmt, rs);
		}
		return null;
	}
	
	public ArrayList<Member> selectList() {
		
		ArrayList<Member> list = new ArrayList<Member>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
			try {
				conn = factory.getConnection();
				
				String sql = "select * from member"; // 
				stmt = conn.prepareStatement(sql);
				
				rs = stmt.executeQuery();
				
				
				
				while(rs.next()) {
					String memberId = rs.getString("member_id");
					String memberPw = rs.getString("member_pw");
					String name = rs.getString("name");
					String mobile = rs.getString("mobile");
					String email = rs.getString("email");
					String entryDate = rs.getString("entry_date");
					String grade = rs.getString("grade");
					int mileage = rs.getInt("mileage");
					String manager = rs.getString("manager");
					
					Member dto = new Member(memberId, memberPw, name, mobile, email, entryDate, grade, mileage, manager);
					list.add(dto);
				}
				
			} catch (SQLException e) {
				System.out.println("[오류] 회원상세조회");
				e.printStackTrace();
			} finally {
				factory.close(conn,  stmt, rs);
			}
			return list;
		}
	
	public ArrayList<Member> selectListGrade() {
		return null;
	}
	
		
	public ArrayList<Member> selectListByGrade(String grade) {
		ArrayList<Member> list = new ArrayList<Member>();
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = factory.getConnection();
			
			String sql = "select * from member where grade=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, grade);
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				String memberId = rs.getString("member_Id"); 
				String memberPw = rs.getString("member_pw");
				String name = rs.getString("name");
				String mobile = rs.getString("mobile");
				String email = rs.getString("email");
				String entryDate = rs.getString("entry_date");
				int mileage = rs.getInt("mileage");
				String manager = rs.getString("manager");
				
				Member dto = new Member(memberId, memberPw, name, mobile, email, entryDate, grade, mileage, manager);
				list.add(dto);
			}
	
			
		} catch (SQLException e) {
			System.out.println("[오류] 등급별 회원전체조회");
			e.printStackTrace();
		} finally {
			factory.close(conn,  stmt, rs);
		}
		return list;
	}
	
	/**
	 * 1단계 : 비밀번호 찾기 : 해당 회원 존재 유무 체킹
	 * 
	 * String sql = "select member_pw from member where member_id = ? and name = ? and email = ?"
	 * @param memberId
	 * @param name
	 * @param email
	 * @return 성공 true, 실패 false
	 */
	public boolean selectMemberPwByEmail(String memberId, String name, String email) {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = factory.getConnection();
			
			String sql = "select member_pw from member where member_id = ? and name = ? and email = ?"; 
			stmt = conn.prepareStatement(sql);
			
			
			stmt.setString(1, memberId);
			stmt.setString(2, name);
			stmt.setString(3, email);
					
			rs = stmt.executeQuery();
			
			if(rs.next()) {
							
				return true; 
			}
					
		} catch (SQLException e) {
			System.out.println("[오류] 비밀번호찾기 정보 검증");
			e.printStackTrace();
		} finally {
			factory.close(conn,  stmt, rs);
		}
		return false;
	}
	
	/**
	 * 2단계 : 비밀번호(임시발급) 변경
	 * @param memberId
	 * @param memberNewPw
	 * @return 성공 true, 실패 false
	 */
	public boolean updateMemberPw(String memberId, String memberNewPw) {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = factory.getConnection();
			
			String sql = "update member set member_pw = ? where member_id = ?"; 
			stmt = conn.prepareStatement(sql);
			
			
			stmt.setString(1, memberNewPw);
			stmt.setString(2, memberId);
								
			int rows = stmt.executeUpdate(); // CUD 할땐 executeUpdate 반환타입은 int이다.
			
					
			if(rows > 0) { //rows는 한행이 업데이트 됐으니까
							
				return true; 
			}
					
		} catch (SQLException e) {
			System.out.println("[오류] 비밀번호(임시발급)변경");
			e.printStackTrace();
		} finally {
			factory.close(conn,  stmt, rs);
		}
		return false;
	}
	
	

}
