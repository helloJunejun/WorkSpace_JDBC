package com.concert.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.concert.model.Info;
import com.concert.model.Member;
import com.concert.model.Reservation;
import com.concert.service.MemberService2.Book1;

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
	private String[][] seats;
	
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
			String sql = "select * from member where member_id =? and member_pw =?";
			stmt = conn.prepareStatement(sql);
			
			
			stmt.setString(1, memberId);
			stmt.setString(2, memberPw);
		
			rs = stmt.executeQuery();
			
			// 5.
			if(rs.next()) {
				memberId = rs.getString("member_id");
				return memberId;
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
				String grade = rs.getString("grade");
				int money = rs.getInt("money");				
				
				Member dto = new Member(memberId, memberPw, name, mobile, grade, money);
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
					String grade = rs.getString("grade");
					int money = rs.getInt("money");		
					
					Member dto = new Member(memberId, memberPw, name, mobile, grade, money);
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
	
	public ArrayList<Reservation> reservationList() {
		
		ArrayList<Reservation> list = new ArrayList<Reservation>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
			try {
				conn = factory.getConnection();
				
				String sql = "select * from reservation"; // 
				stmt = conn.prepareStatement(sql);
				
				rs = stmt.executeQuery();
				
				
				
				while(rs.next()) {
					int infoCode = rs.getInt("infocode");
					String cusName = rs.getString("cusname");
					int stGrade = rs.getInt("stgrade");
					int stNum = rs.getInt("stnum");
					
					Reservation dto = new Reservation(infoCode, cusName, stGrade, stNum);
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
	
	public ArrayList<Member> addList(String memberId, String memberPw, String name, String mobile) {
		
		ArrayList<Member> list = new ArrayList<Member>();
		Connection conn = null;
		PreparedStatement stmt = null;
		int rs = 0;
		
			try {
				conn = factory.getConnection();
				
				String sql = "insert into member (member_id, member_pw, name, mobile, grade, money) values(?, ?, ?, ?, 'a', '0')";
				stmt = conn.prepareStatement(sql);
				
			
				
				stmt.setString(1, memberId);
				stmt.setString(2, memberPw);
				stmt.setString(3, name);
				stmt.setString(4, mobile);
				
				
				rs = stmt.executeUpdate();
		
				
			} catch (SQLException e) {
				System.out.println("[오류] 회원 가입");
				e.printStackTrace();
			} finally {
				factory.close(conn,  stmt, rs);
			}
			return list;
		}
	
	public ArrayList<Info> infoSelectList() {
		
		ArrayList<Info> infoList = new ArrayList<Info>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
			try {
				conn = factory.getConnection();
				
				String sql = "select * from info"; // 
				stmt = conn.prepareStatement(sql);
				
				rs = stmt.executeQuery();
				
				
				
				while(rs.next()) {
					String infoCode = rs.getString("infocode");
					String infoName = rs.getString("infoname");
					String infoDate = rs.getString("infodate");
					String infoTime = rs.getString("infotime");
					String infoFemaleActor = rs.getString("infofemaleactor");
					String infoMaleActor = rs.getString("infomaleactor");
					int ticketPrice = rs.getInt("ticketprice");		
					
					Info dto = new Info(infoCode, infoName, infoDate, infoTime, infoFemaleActor, infoMaleActor, ticketPrice);
					infoList.add(dto);
				}
				
			} catch (SQLException e) {
				System.out.println("[오류] 공연 전체 조회");
				e.printStackTrace();
			} finally {
				factory.close(conn,  stmt, rs);
			}
			return infoList;
		}
	
	public ArrayList<Member> selectListGrade() {
		return null;
	}
	
	
	/** 좌석 추가 */
	/** 좌석 추가 */
	/** 좌석 추가 */
	/** 좌석 추가 */
	public ArrayList<Member> bookSeat(int infoCode, String cusName, int stGrade, int stNum) {
//		
		ArrayList<Member> list = new ArrayList<Member>();
		Connection conn = null;
		PreparedStatement stmt = null;
		int rs = 0;
		
			try {
				conn = factory.getConnection();
				String sql = "insert into reservation values(?, ?, ?, ?)"; // 
				stmt = conn.prepareStatement(sql);
				
//				
				stmt.setInt(1, infoCode);
				stmt.setString(2, cusName);
				stmt.setInt(3, stGrade);
				stmt.setInt(4, stNum);
				
				rs = stmt.executeUpdate();
												
			} catch (SQLException e) {
				System.out.println("[오류] bookInsert");
				e.printStackTrace();
			} finally {
				factory.close(conn,  stmt, rs);
			}
			return list;
		}
	
	/** 실험중 */
	/** 실험중 */
	/** 실험중 */
	/** 실험중 */
	/** 실험중 */
	/** 실험중 */
public ArrayList<Reservation> bookSearch(int infoCode, String cusName) {
		
		ArrayList<Reservation> list = new ArrayList<Reservation>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String[][] seats = new String[3][10];
		String[] gradeList = {"VIP","S","R"};
		
		for(int i = 0 ; i < 3 ; i++) {
			for(int j = 0; j < 10 ; j++) {
				seats[i][j] = "[]";
			}
		}
		
		int stGrade = 0;
		int stNum = 0;
	
	
			try {				
				conn = factory.getConnection();
				
				String sql = "select * from reservation where infocode = ? and cusname = ?"; 
				stmt = conn.prepareStatement(sql);
				
				stmt.setInt(1, infoCode);
				stmt.setString(2, cusName);
							
				rs = stmt.executeQuery(); // rs로 결과값이 들어오고 rs.getString("")으로 컬럼값 받는다
				
								
					if(rs.next()) {	
						if(rs.getString(2).equals(cusName)) {
						
						cusName = rs.getString(2);
						stGrade = rs.getInt(3)-1;
						stNum = rs.getInt(4)-1;
						
						seats[stGrade][stNum] = ("[" + cusName + "]");
						
						for (int i = 0; i < 3; i++) {	
							System.out.print("\n" + gradeList[i] + "석 ");
							for (int j = 0; j < 10; j++) {
								System.out.print(" "+ seats[i][j]);
							}
						}
				   				
						} else {
							System.out.println("[오류] 예약이 존재하지않습니다.");
						}
					}
//				bookList(infoCode).get(i).getStGrade()][bookList(infoCode).get(j).getStNum()
//				bookList(infoCode);
			
			} catch (SQLException e) {
				System.out.println("[오류] 회원상세조회");
				e.printStackTrace();
			} finally {
				factory.close(conn,  stmt, rs);
			}
			return list;
		}

/** reservation 전체조회 */
public ArrayList<Reservation> bookList(int infoCode) {
	ArrayList<Reservation> list = new ArrayList<Reservation>();
	
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	
	String[][] seats = new String[3][10];
	String[] gradeList = {"VIP","S","R"};
	
	for(int i = 0 ; i < 3 ; i++) {
		for(int j = 0; j < 10 ; j++) {
			seats[i][j] = "[]";
		}
	}
	
	try {
		conn = factory.getConnection();
		
		String sql = "select * from Reservation where infoCode = ?";
		stmt = conn.prepareStatement(sql);
		
		stmt.setInt(1, infoCode);
		
		rs = stmt.executeQuery();
		
		while(rs.next()) {
//			int infoCode = rs.getInt(1);
			String cusName = rs.getString(2);
			int stGrade = rs.getInt(3)-1;
			int stNum = rs.getInt(4)-1;
			
			seats[stGrade][stNum] = ("[" + cusName + "]");
		}
		
		for (int i = 0; i < 3; i++) {	
			System.out.print("\n" + gradeList[i] + "석 ");
			for (int j = 0; j < 10; j++) {
				System.out.print(" "+ seats[i][j]);
			}
		} 

		
	} catch (SQLException e) {
		System.out.println("[오류] 등급별 회원전체조회");
		e.printStackTrace();
	} finally {
		factory.close(conn,  stmt, rs);
	}
	return list;
}

public ArrayList<Reservation> bookCancle(int infoCode, String cusName) {
//	
	ArrayList<Reservation> list = new ArrayList<Reservation>();
	Connection conn = null;
	PreparedStatement stmt = null;
	int rs = 0;
	
		try {
			conn = factory.getConnection();
			String sql = "delete from reservation where infocode = ? and cusname = ?"; // 
			stmt = conn.prepareStatement(sql);
			
//			
			stmt.setInt(1, infoCode);
			stmt.setString(2, cusName);
			
			rs = stmt.executeUpdate();
			
											
		} catch (SQLException e) {
			System.out.println("[오류] bookCancle");
			e.printStackTrace();
		} finally {
			factory.close(conn,  stmt, rs);
		}
		return list;
	}


	public static String inputString() {
		String data = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			data = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public static int inputNumber() {
		String data = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			data = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Integer.parseInt(data);
	}

}
