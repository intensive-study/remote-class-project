package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.coupon.RequestCouponDto;
import org.server.remoteclass.dto.coupon.ResponseCouponDto;
import org.server.remoteclass.dto.event.RequestEventDto;
import org.server.remoteclass.dto.lecture.LectureDto;
import org.server.remoteclass.dto.lecture.ResponseLectureDto;
import org.server.remoteclass.dto.order.ResponseOrderByAdminDto;
import org.server.remoteclass.dto.student.ResponseStudentByLecturerDto;
import org.server.remoteclass.dto.user.ResponseUserByAdminDto;
import org.server.remoteclass.service.admin.AdminService;
import org.server.remoteclass.service.coupon.CouponService;
import org.server.remoteclass.service.event.EventService;
import org.server.remoteclass.service.lecture.LectureService;
import org.server.remoteclass.service.order.OrderService;
import org.server.remoteclass.service.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final LectureService lectureService;
    private final StudentService studentService;
    private final OrderService orderService;
    private final CouponService couponService;
    private final EventService eventService;

    @Autowired
    public AdminController(AdminService adminService, LectureService lectureService,
                           StudentService studentService,OrderService orderService,
                           CouponService couponService, EventService eventService){
        this.adminService = adminService;
        this.lectureService = lectureService;
        this.studentService = studentService;
        this.orderService = orderService;
        this.couponService = couponService;
        this.eventService = eventService;
    }

    @ApiOperation(value = "테스트용")
    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome test";
    }

    /**
     * USER
     */

    // 관리자가 전체 유저 조회
    @ApiOperation(value = "관리자가 모든 사용자 조회", notes = "모든 사용자의 상세한 정보를 알 수 있다.")
    @GetMapping("/users")
    public ResponseEntity<List<ResponseUserByAdminDto>> getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getAllUsers());
    }

    // 관리자가 사용자 조회
    @ApiOperation(value = "관리자가 사용자 조회", notes = "사용자의 모든 정보를 조회할 수 있다.")
    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUserByAdminDto> getUser(@PathVariable("userId") Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(adminService.getUser(userId));
    }

    /**
     * LECTURE
     */

    @ApiOperation(value = "전체 강의 조회", notes = "현재까지 생성된 모든 강의를 조회할 수 있다.")
    @GetMapping("/lectures")
    public ResponseEntity<List<LectureDto>> getAllLecture(){
        return ResponseEntity.status(HttpStatus.OK).body(lectureService.getLectureByAll());
    }

    @ApiOperation(value = "강의 조회", notes = "원하는 강의 번호로 강의를 조회할 수 있다.")
    @GetMapping("/lectures/{lectureId}")
    public ResponseEntity<LectureDto> getLecture(@PathVariable("lectureId") Long lectureId){
        return ResponseEntity.status(HttpStatus.OK).body(lectureService.getLectureByLectureId(lectureId));
    }
    @ApiOperation(value = "강의 삭제", notes = "강의를 삭제할 수 있다.")
    @DeleteMapping("/lectures/{lectureId}")
    public ResponseEntity deleteLecture(@PathVariable("lectureId") Long lectureId) {
        lectureService.deleteLecture(lectureId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "카테고리별 강의 조회", notes = "현재까지 생성된 강의를 카테고리별로 조회할 수 있다.")
    @GetMapping("/lectures/category/{categoryId}")
    public ResponseEntity<List<LectureDto>> getLectureByCategory(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(lectureService.getLectureByCategoryId(categoryId));
    }

    /**
     * STUDENT
     */

    @ApiOperation(value = "수강 철회", notes = "학생이 신청했던 강의를 철회할 수 있다.")
    @DeleteMapping("/students/{lectureId}")
    public ResponseEntity deleteStudent(@PathVariable("lectureId") Long lectureId) {
        studentService.cancel(lectureId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // URL 고민해 봐야 할 것 같습니다.
    @ApiOperation(value = "수강 강좌 조회", notes = "특정 학생이 수강하는 모든 강의를 조회할 수 있다.")
    @GetMapping("/students/lectures/{userId}")
    public ResponseEntity<List<ResponseLectureDto>> getLecturesByUserId(@PathVariable("userId") Long userId)  {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getLecturesByUserIdByAdmin(userId));
    }

    //수강생 전체 조회
    @ApiOperation(value = "수강생 조회", notes = "특정 강의의 모든 수강생을 조회할 수 있다.")
    @GetMapping("/students/lecture/{lectureId}")
    public ResponseEntity<List<ResponseStudentByLecturerDto>> getStudentsByLectureId(@PathVariable("lectureId") Long lectureId) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudentsByLectureId(lectureId));
    }

    /**
     * ORDER
     */

    @ApiOperation(value = "전체 주문 조회", notes = "모든 주문 내역을 조회할 수 있다.")
    @GetMapping("/orders")
    public ResponseEntity<List<ResponseOrderByAdminDto>> getAllByAdmin() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrdersByAdmin());
    }

    @ApiOperation(value = "사용자별 주문 조회", notes = "특정 사용자의 모든 주문내역을 조회할 수 있다.")
    @GetMapping("/orders/user/{userId}")
    public ResponseEntity<List<ResponseOrderByAdminDto>> getByUserIdByAdmin(@PathVariable("userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderByUserIdByAdmin(userId));
    }

    @ApiOperation(value = "주문 내역 조회", notes = "특정 주문내역을 조회할 수 있다.")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ResponseOrderByAdminDto> getByOrderIdByAdmin(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderByOrderIdByAdmin(orderId));
    }

    /**
     * COUPON
     */
    //관리자 권한이므로 CouponDto로 모든 정보를 보여주게끔 한다.
    @ApiOperation(value = "전체 쿠폰 조회", notes = "현재까지 생성된 모든 쿠폰을 조회할 수 있다.")
    @GetMapping("/coupons")
    public ResponseEntity<List<ResponseCouponDto>> getAllCoupons(){
        return ResponseEntity.status(HttpStatus.OK).body(couponService.getAllCoupons());
    }

    //쿠폰 번호로 쿠폰 검색(관리자 권한)
    @ApiOperation(value = "쿠폰 번호로 쿠폰 조회", notes = "쿠폰 번호에 해당하는 쿠폰을 조회한다.")
    @GetMapping("/coupons/{couponId}")
    public ResponseEntity<ResponseCouponDto> getCoupon(@PathVariable("couponId") Long couponId){
        return ResponseEntity.status(HttpStatus.OK).body(couponService.getCouponByCouponId(couponId));
    }

    //쿠폰 생성(관리자 권한)
    @ApiOperation(value = "쿠폰 생성", notes = "새로운 쿠폰을 생성할 수 있다.")
    @PostMapping("/coupons")
    public ResponseEntity createCoupon(@RequestBody @Valid RequestCouponDto requestCouponDto){
        couponService.createCoupon(requestCouponDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //쿠폰 비활성화(관리자 권한)
    @ApiOperation(value = "쿠폰 비활성화", notes = "더 이상 쿠폰을 발급받을 수 없게 쿠폰을 비활성화 한다.")
    @PutMapping("/coupons/deactivate/{couponId}")
    public ResponseEntity createCoupon(@PathVariable("couponId") Long couponId) {
        couponService.deactivateCoupon(couponId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "쿠폰 삭제", notes = "쿠폰 목록에서 쿠폰을 삭제한다.")
    @DeleteMapping("/coupons/{couponId}")
    public ResponseEntity deleteCoupon(@PathVariable("couponId") Long couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * ISSUEDCOUPON
     */

    /**
     * EVENT
     */
    @ApiOperation(value = "이벤트 생성", notes = "이벤트 생성과 동시에 이벤트와 연계된 쿠폰을 생성한다.")
    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid RequestEventDto requestEventDto){
        eventService.createEvent(requestEventDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "이벤트 삭제", notes = "이벤트 번호를 파라미터로 넘겨 해당하는 이벤트를 삭제한다.")
    @DeleteMapping("/{eventId}")
    public ResponseEntity deleteEvent(@PathVariable("eventId") Long eventId){
        eventService.deleteEvent(eventId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
