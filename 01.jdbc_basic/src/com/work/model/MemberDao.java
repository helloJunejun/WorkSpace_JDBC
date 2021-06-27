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
	// jdbc resource property
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@127.0.0.1:1521:XE";
	private String user = "scott";
	private String password = "tiger";
	
	/**
	 * 1. jdbc driver 로딩
	 */
	public MemberDao() {
		try {
			Class.forName(driver);
			System.out.println("[성공] 드라이버 로딩");
		} catch (ClassNotFoundException e) {
			System.out.println("[오류] 드라이버 로딩");
			e.printStackTrace();
		}
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
			// 2번째 과정
			conn = DriverManager.getConnection(url, user, password);
			// 3번째 과정
			stmt = conn.createStatement();
		
			// 4. 로그인을 위한 SQL 구문
			//String sql = "select grade from member where member_id = 'user01' and member_pw = 'password01'";
			String sql = "select grade from member where member_id ='" + memberId+ "' and member_pw ='" + memberPw + "'";
			// 4.
			rs = stmt.executeQuery(sql);
			// 5.
			if(rs.next()) {
				String grade = rs.getString("grade");
				return grade;
			}
//			// 6.
//			rs.close();
//			stmt.close();
//			conn.close();
			
		} catch (SQLException e) {
			System.out.println("[오류] 로그인");
			e.printStackTrace();
		} finally {
			// 6. 자원해제 : finally 구문으로 변경 수정
			try {
				if(rs != null) {
				rs.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(stmt != null) {
					stmt.close();
				}				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(conn != null) {
					conn.close();
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			// 2번째 과정
			conn = DriverManager.getConnection(url, user, password);
			// 3. 주의사항 : sql 구문뒤에 ;(세미콜론)이 와서는 안됨
			String sql = "select grade from member where member_id =? and member_pw =?";
			stmt = conn.prepareStatement(sql);
			
			
			//3. ?에 매핑되는 값을 설정
			stmt.setString(1, memberId);
			stmt.setString(2, memberPw);
		
			// 4. 실행시에 이미 전용통로로 개설되었으므로 sql 구문을 지정해서는 안됨
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
			// 6. 자원해제 : finally 구문으로 변경 수정
			try {
				if(rs != null) {
				rs.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(stmt != null) {
					stmt.close();
				}				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(conn != null) {
					conn.close();
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	public Member selectOne(String memberId) {
	//	String sql = "select * from member where member_id = ''"; // 
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;	
		try {
			// 2번째 과정
			conn = DriverManager.getConnection(url, user, password);
			
			// 3. 주의사항 : sql 구문뒤에 ;(세미콜론)이 와서는 안됨
			String sql = "select * from member where member_id = ?"; // 
			stmt = conn.prepareStatement(sql);
			
			
			//3. ?에 매핑되는 값을 설정
			stmt.setString(1, memberId);
					
			// 4. 실행시에 이미 전용통로로 개설되었으므로 sql 구문을 지정해서는 안됨
			rs = stmt.executeQuery();
			
			// 5.
			if(rs.next()) {
				//String memberId = rs.getString("memberId");
				// 나머지 구현 비밀번호, 이름, 휴대폰, 이메일, 가입일, 등급, 마일리지, 담당자 : 데이터 가져오기
				String memberPw = rs.getString("member_pw");
				String name = rs.getString("name");
				String mobile = rs.getString("mobile");
				String email = rs.getString("email");
				String entryDate = rs.getString("entry_date");
				String grade = rs.getString("grade");
				int mileage = rs.getInt("mileage");
				String manager = rs.getString("manager");
				
				// select 결과로 가져온 회원의 정보로 Member 객체 생성자 이용해서 Member 객체 생성
				Member dto = new Member(memberId, memberPw, name, mobile, email, entryDate, grade, mileage, manager);
				return dto; // 생성한 Member객체 반환
			}
		
			
		} catch (SQLException e) {
			System.out.println("[오류] 회원상세조회");
			e.printStackTrace();
		} finally {
			// 6. 자원해제 : finally 구문으로 변경 수정
			try {
				if(rs != null) {
				rs.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(stmt != null) {
					stmt.close();
				}				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(conn != null) {
					conn.close();
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public ArrayList<Member> selectList() {
		
		ArrayList<Member> list = new ArrayList<Member>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
			try {
				conn = DriverManager.getConnection(url, user, password);
				
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
					
					// select 결과로 가져온 회원의 정보로 Member 객체 생성자 이용해서 Member 객체 생성
					Member dto = new Member(memberId, memberPw, name, mobile, email, entryDate, grade, mileage, manager);
					list.add(dto);
				}
				
				
							
				//return list;

				
			} catch (SQLException e) {
				System.out.println("[오류] 회원상세조회");
				e.printStackTrace();
			} finally {
				// 6. 자원해제 : finally 구문으로 변경 수정
				try {
					if(rs != null) {
					rs.close();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(stmt != null) {
						stmt.close();
					}				
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(conn != null) {
						conn.close();
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return list;
		}
	
	public ArrayList<Member> selectListGrade() {
		return null;
	}
	
//	## 회원등급별 전체조회 	
//	=> MemberService#getMemberListByGrade(등급) : ArrayList<회원>
//	=> MemberDao#selectListByGrade(등급): ArrayList<회원>
		
	public ArrayList<Member> selectListByGrade(String grade) {
		ArrayList<Member> list = new ArrayList<Member>();
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = DriverManager.getConnection(url, user, password);
			
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
//				String grade = rs.getString("grade");  // 아규먼트로 회원의 조회할 등급받았으므로 가져올필요없음
				int mileage = rs.getInt("mileage");
				String manager = rs.getString("manager");
				
				Member dto = new Member(memberId, memberPw, name, mobile, email, entryDate, grade, mileage, manager);
				list.add(dto);
			}
	
			//return list;
			
		} catch (SQLException e) {
			System.out.println("[오류] 등급별 회원전체조회");
			e.printStackTrace();
		} finally {
			// 6. 자원해제 : finally 구문으로 변경 수정
			try {
				if(rs != null) {
				rs.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(stmt != null) {
					stmt.close();
				}				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(conn != null) {
					conn.close();
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			conn = DriverManager.getConnection(url, user, password);
			
			String sql = "select member_pw from member where member_id = ? and name = ? and email = ?"; 
			stmt = conn.prepareStatement(sql);
			
			
			stmt.setString(1, memberId);
			stmt.setString(2, name);
			stmt.setString(3, email);
					
			rs = stmt.executeQuery();
			
			if(rs.next()) { //rs.next()는 다음 줄에 정보가 있다는 뜻이니까.
							
				return true; 
			}
					
		} catch (SQLException e) {
			System.out.println("[오류] 비밀번호찾기 정보 검증");
			e.printStackTrace();
		} finally {
			// 6. 자원해제 : finally 구문으로 변경 수정
			try {
				if(rs != null) {
				rs.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(stmt != null) {
					stmt.close();
				}				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(conn != null) {
					conn.close();
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			conn = DriverManager.getConnection(url, user, password);
			
			String sql = "update member set member_pw = ? where member_id = ?"; 
			stmt = conn.prepareStatement(sql);
			
			
			stmt.setString(1, memberNewPw);
			stmt.setString(2, memberId);
								
//			ResultSet rs = stmt.executeQuery();
			int rows = stmt.executeUpdate(); // CUD 할땐 executeUpdate 반환타입은 int이다.
			
					
			if(rows > 0) { //rows는 한행이 업데이트 됐으니까
							
				return true; 
			}
					
		} catch (SQLException e) {
			System.out.println("[오류] 비밀번호(임시발급)변경");
			e.printStackTrace();
		} finally {
			// 6. 자원해제 : finally 구문으로 변경 수정
//			try {
//				if(rs != null) {
//				rs.close();
//				}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			try {
				if(stmt != null) {
					stmt.close();
				}				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(conn != null) {
					conn.close();
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

}
