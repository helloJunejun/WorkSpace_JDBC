package com.concert.service;
/*
	 ## JDBC 관련 클래스 의존관계
		1. Test 
		=> MemberService#login(아이디, 비밀번호) : 등급
		=> MemberDao#login(아이디, 비밀번호) : 등급 : 메서드 형식 구현
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.concert.exception.CommonException;
import com.concert.exception.DuplicateException;
import com.concert.exception.RecordNotFoundException;
import com.concert.model.Info;
import com.concert.model.Member;
import com.concert.service.MemberService2.Book1;
//import com.concert.service.MemberService.Book1;
//import com.concert.service.MemberService.Book2;
//import com.concert.service.MemberService.Book3;
//import com.concert.service.MemberService.Book4;
//import com.concert.service.MemberService.Book5;
import com.concert.util.Utility;

public class MemberService {
	/** MemberDao 객체 생성 */
	private static MemberDao dao = MemberDao.getInstance();
	
	/**
	 * 로그인
	 * @param memeberId 아이디
	 * @param memberPw 비밀번호
	 * @return 회원등급, 미존재시 null
	 */
	public static String login(String memberId, String memberPw) {
		String suc = dao.login(memberId,  memberPw);
		if(suc != null) {
			return suc;
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
	
	// 전체공연조회
	public static ArrayList<Info> getInfoList() {
		for(int index = 0; index < dao.infoSelectList().size();index++) {
			System.out.println("[" + (index + 1) + "번]" + dao.infoSelectList().get(index));
		}
			return dao.infoSelectList();
	}
	
	public static void mainMenu() {
		printTitle("회원관리시스템 메인메뉴");
		
		System.out.println("1. 로그인");
		System.out.println("2. 회원가입");
		System.out.println("0. 프로그램 종료");
		printLine();
		System.out.print("메뉴번호 입력 : ");
	}
	

		
		public static void loginMenu() throws RecordNotFoundException, CommonException, DuplicateException {
						
			printTitle("로그인 메뉴");
			System.out.print("아이디 : ");
			String memberId = inputString();

			System.out.print("비밀번호 : ");
			String memberPw = inputString();

			// service login 요청 처리
			String dto = login(memberId, memberPw);
			
			if(dto != null && (memberId.equals("administrator"))) {
				System.out.println("관리자 로그인에 성공하였습니다.");
				adminMenu();
				
			} else if(dto != null) {
				System.out.println("로그인에 성공하였습니다.");
				memberMenu(memberId);
				
			} else {
				
				System.out.println("로그인에 실패하였습니다.");
				mainMenu();
			}
		}
		
		public static void addMemberMenu() throws DuplicateException {
						
			printTitle("회원 가입 메뉴");
			
			System.out.print("아이디 : ");
			String memberId = inputString();

			System.out.print("비밀번호 : ");
			String memberPw = inputString();


			System.out.print("이름 : ");
			String name = inputString();

			
			System.out.print("휴대폰 : ");
			String mobile = inputString();

			if(exist(memberId) >= 0) {
				System.out.println("---------중복된 ID입니다.---------");
			} else {
			dao.addList(memberId, memberPw, name, mobile);
			}
			
//			System.out.println("현재 등록회원수 : " + service.getCount());
			
			mainMenu();
			
		}
		
		public static void addReservation() throws DuplicateException {
			System.out.print("공연 번호 : ");
			int infoCode = inputNumber();
			if(infoCode > 5){
     			System.out.println("잘못된 공연번호 입니다. 다시 입력해주세요.");
     			System.out.print("공연 번호 : ");
     			infoCode = inputNumber();
     		}
			printLine();
			dao.bookList(infoCode);
			System.out.println();
			printLine();
			
			System.out.println();
			System.out.print("예약자 이름 : ");
			String cusName = inputString();
			
			printLine();
			if(bookExist(cusName, infoCode) >= 0) {
				System.out.println("해당 공연에 이미 예약된 고객 입니다.");
				addReservation();
				
			} 
			printLine();
			System.out.println("원하시는 좌석등급을 선택하세요. <VIP석 = 1> <S석 = 2> <R석 = 3>");
			System.out.print("좌석등급(숫자입력) : ");
			int stGrade = inputNumber();
			if(stGrade > 3){
	     		System.out.println("잘못된 좌석등급 입니다. 다시 입력해주세요.");
	     		System.out.print("좌석등급(숫자입력) : ");
	     		stGrade = inputNumber();
	     	}
				
			printLine();
			System.out.println("원하시는 좌석번호을 선택하세요. 1번 ~ 10번");
			System.out.print("좌석번호 : ");
			int stNum = inputNumber();
			if(stNum > 10){
	     		System.out.println("잘못된 좌석번호 입니다. 다시 입력해주세요.");
	     		System.out.print("좌석번호 : ");
	     		stNum = inputNumber();
			} 
			
			if(bookExist2(infoCode, stGrade, stNum) >= 0) {
				System.out.println();
				System.out.println("이미 예약된 좌석입니다.");
			 } else { 	
				 
				System.out.println();
				System.out.println("예약 완료");
				
			
			dao.bookSeat(infoCode, cusName, stGrade, stNum);	
			 }
			printLine();
			dao.bookList(infoCode);
			System.out.println();
			printLine();
		}		
		
			
		
					
		
		
		public static void reservationSearch() throws DuplicateException {
			System.out.print("공연 번호 : ");
			int infoCode = inputNumber();
			
			System.out.print("예약자 이름 : ");
			String cusName = inputString();
			
			dao.bookSearch(infoCode, cusName);
				
		}
		
		public static void bookListSelect() throws DuplicateException {
			System.out.print("공연 번호 : ");
			int infoCode = inputNumber();
			
			dao.bookList(infoCode);
				
		}
		
		public static void bookCancle() throws DuplicateException {
			printLine();
			System.out.print("취소할 공연 번호 : ");
			int infoCode = inputNumber();
			
			System.out.print("취소할 예약자 이름 : ");
			String cusName = inputString();
			
			if(bookExist(cusName, infoCode) >= 0) {
				dao.bookCancle(infoCode, cusName);
			} else {
				printLine();
				System.out.println("해당 예약자가 존재하지 않습니다.");
				
			}
			
									
			dao.bookList(infoCode);
				
		}
		/**
		 * 회원 존재 유무 조회
		 * @param memberId 아이디
		 * @return 존재시 저장위치 인덱스번호, 미존재시 -1
		 */
		public static int exist(String memberId) {
			for (int index = 0; index < dao.selectList().size(); index++) {
				if (dao.selectList().get(index).getMemberId().equals(memberId)) {
					return index;
				}
			}
			return -1;
		}
		
		/**
		 * 예약 고객 존재 유무 조회
		 * @param memberId 아이디
		 * @return 존재시 저장위치 인덱스번호, 미존재시 -1
		 */
		public static int bookExist(String cusName, int infoCode) {
			for (int index = 0; index < dao.reservationList().size(); index++) {
				if (dao.reservationList().get(index).getCusName().equals(cusName)) {
					if(dao.reservationList().get(index).getInfoCode() == infoCode) {
						return index;
					}
				}
			}
			return -1;
		}
		
		/**
		 * 좌석 중복
		 * @param memberId 아이디
		 * @return 존재시 저장위치 인덱스번호, 미존재시 -1
		 */
		public static int bookExist2(int infoCode, int stGrade, int stNum) {
			for (int index = 0; index < dao.reservationList().size(); index++) {
				if (dao.reservationList().get(index).getStGrade() == stGrade) {
					if(dao.reservationList().get(index).getInfoCode() == infoCode) {
						if(dao.reservationList().get(index).getStNum() == stNum) {
						return index;
						}
					}
				}
			}
			return -1;
		}
		
		
		
		
		
		
		public static void memberMenu(String memberId) throws RecordNotFoundException {
				
			loop : while(true) {
				printTitle(memberId + "님 환영합니다.");
				
				System.out.println("1. 공연 정보 조회");
				System.out.println("2. 공연 예약");
				System.out.println("3. 공연 예약 조회");
				System.out.println("4. 전체 예약 조회");
				System.out.println("5. 공연 예약 취소");
				System.out.println("6. 메인 메뉴");
				printLine();
				System.out.print("메뉴번호 입력 : ");
				int menuNo = inputNumber();
				
				switch(menuNo) {
				case 1:
					getInfoList();
					break;
				case 2:
								
					getInfoList();
					printLine();
					System.out.println("공연을 선택하세요");
					try {
						addReservation();
					} catch (DuplicateException e1) {
						e1.printStackTrace();
						}
					
					break;
				
				case 3:
					getInfoList();		
					printLine();
					System.out.println("공연을 선택하세요");
					try {
						reservationSearch();
					} catch (DuplicateException e) {
						e.printStackTrace();
						}
										
					break;
				case 4:
					getInfoList();		
					printLine();
					System.out.println("공연을 선택하세요");
					try {
						bookListSelect();
					} catch (DuplicateException e) {
						e.printStackTrace();
					}
					
					break;
				case 5:
					getInfoList();		
					printLine();
					System.out.println("공연을 선택하세요");
					try {
						bookCancle();
					} catch (DuplicateException e) {
						e.printStackTrace();
					}
					
					break;
					
				case 6:
					mainMenu();
					break loop;
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
		
		public static void adminMenu() {

			while(true) {
				
				printTitle("관리자님 환영합니다.");
				
				System.out.println("1. 회원 전체 조회");
				System.out.println("2. 공연 추가");
				System.out.println("3. 공연 수정");
				System.out.println("4. 공연 삭제");
				printLine();
				System.out.print("메뉴번호 입력 : ");
				
				int menuNo = inputNumber();
				switch(menuNo) {
				case 1:
//					service.getMember2();
					break;
				case 2:
//					ss.searchSeat();
					break;
				case 3:
					System.out.println("3. 공연 취소");
					break;
				case 4:
					System.out.println("4. 캐쉬 조회 및 충전 및 결제 내역");
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
		
		
		public static void printLine() {
			System.out.println("-------------------------");
		}
		
		public static void printTitle(String title) {
			System.out.println();
			printLine();
			System.out.println(title);
			printLine();
		}	
		
	
}
