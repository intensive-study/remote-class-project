package org.server.remoteclass.controller;

import io.swagger.annotations.ApiOperation;
import org.server.remoteclass.dto.coupon.ResponseCouponDto;
import org.server.remoteclass.dto.event.RequestEventDto;
import org.server.remoteclass.dto.event.RequestUpdateEventDto;
import org.server.remoteclass.dto.fixDiscountCoupon.RequestFixDiscountCouponDto;
import org.server.remoteclass.dto.fixDiscountCoupon.RequestUpdateFixDiscountCouponDto;
import org.server.remoteclass.dto.fixDiscountCoupon.ResponseFixDiscountCouponDto;
import org.server.remoteclass.dto.lecture.ResponseLectureDto;
import org.server.remoteclass.dto.lecture.ResponseLectureFromStudentDto;
import org.server.remoteclass.dto.order.ResponseOrderByAdminDto;import org.server.remoteclass.dto.purchase.ResponsePurchaseByAdminDto;
import org.server.remoteclass.dto.rateDiscountCoupon.RequestRateDiscountCouponDto;
import org.server.remoteclass.dto.rateDiscountCoupon.RequestUpdateRateDiscountCouponDto;
import org.server.remoteclass.dto.rateDiscountCoupon.ResponseRateDiscountCouponDto;
import org.server.remoteclass.dto.student.ResponseStudentByLecturerDto;
import org.server.remoteclass.dto.user.ResponseUserByAdminDto;
import org.server.remoteclass.service.admin.AdminService;
import org.server.remoteclass.service.coupon.CouponService;
import org.server.remoteclass.service.event.EventService;
import org.server.remoteclass.service.fixDiscountCoupon.FixDiscountCouponService;
import org.server.remoteclass.service.lecture.LectureService;
import org.server.remoteclass.service.order.OrderService;
import org.server.remoteclass.service.purchase.PurchaseService;
import org.server.remoteclass.service.rateDiscountCoupon.RateDiscountCouponService;
import org.server.remoteclass.service.student.StudentService;
import org.server.remoteclass.service.user.UserService;
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
    private final UserService userService;
    private final LectureService lectureService;
    private final StudentService studentService;
    private final OrderService orderService;
    private final PurchaseService purchaseService;
    private final CouponService couponService;
    private final FixDiscountCouponService fixDiscountCouponService;
    private final RateDiscountCouponService rateDiscountCouponService;
    private final EventService eventService;

    @Autowired
    public AdminController(AdminService adminService,
                           LectureService lectureService,
                           StudentService studentService,
                           OrderService orderService,
                           PurchaseService purchaseService,
                           CouponService couponService,
                           FixDiscountCouponService fixDiscountCouponService,
                           RateDiscountCouponService rateDiscountCouponService,
                           EventService eventService){

        this.adminService = adminService;
        this.userService = userService;
        this.lectureService = lectureService;
        this.studentService = studentService;
        this.orderService = orderService;
        this.purchaseService = purchaseService;
        this.couponService = couponService;
        this.fixDiscountCouponService = fixDiscountCouponService;
        this.rateDiscountCouponService = rateDiscountCouponService;
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

    //수강생 -> 강의자 변경 신청
    @ApiOperation(value = "수강생에서 강의자로 역할 변경")
    @PutMapping("/student/lecturer/{userId}")
    public ResponseEntity fromStudentToLecturer(@PathVariable("userId") Long userId){
        userService.fromStudentToLecturer(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //강의자 -> 수강생 변경 신청
    @ApiOperation(value = "강의자에서 수강생으로 역할 변경")
    @PutMapping("/lecturer/student/{userId}")
    public ResponseEntity fromLecturerToStudent(@PathVariable("userId") Long userId){
        userService.fromLecturerToStudent(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * LECTURE
     */

    @ApiOperation(value = "전체 강의 조회", notes = "현재까지 생성된 모든 강의를 조회할 수 있다.")
    @GetMapping("/lectures")
    public ResponseEntity<List<ResponseLectureDto>> getAllLecture(){
        return ResponseEntity.status(HttpStatus.OK).body(lectureService.getLectureByAll());
    }

    @ApiOperation(value = "강의 조회", notes = "원하는 강의 번호로 강의를 조회할 수 있다.")
    @GetMapping("/lectures/{lectureId}")

    public ResponseEntity<ResponseLectureDto> getLecture(@PathVariable("lectureId") Long lectureId){
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
    public ResponseEntity<List<ResponseLectureDto>> getLectureByCategory(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(lectureService.getLectureByCategoryId(categoryId));
    }

    /**
     * STUDENT
     */

    @ApiOperation(value = "수강 강좌 조회", notes = "특정 학생이 수강하는 모든 강의를 조회할 수 있다.")
    @GetMapping("/students/lectures/{userId}")
    public ResponseEntity<List<ResponseLectureFromStudentDto>> getLecturesByUserId(@PathVariable("userId") Long userId)  {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getLecturesByUserIdByAdmin(userId));
    }

    //수강생 전체 조회
    @ApiOperation(value = "수강생 조회", notes = "특정 강의의 모든 수강생을 조회할 수 있다.")
    @GetMapping("/students/lecture/{lectureId}")
    public ResponseEntity<List<ResponseStudentByLecturerDto>> getStudentsByLectureIdByAdmin(@PathVariable("lectureId") Long lectureId) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudentsByLectureIdByAdmin(lectureId));
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
     * PURCHASE
     */

    @ApiOperation(value = "구매 내역 전체 조회", notes = "생성된 전체 구매 내역 조회함.")
    @GetMapping("/purchases/")
    public ResponseEntity<List<ResponsePurchaseByAdminDto>> getMyAllPurchasesByAdmin() {
        return ResponseEntity.status(HttpStatus.OK).body(purchaseService.getAllPurchasesByUserIdByAdmin());
    }

    @ApiOperation(value = "특정 사용자의 구매 내역 조회", notes = "특정 사용자의 생성된 전체 구매 내역 조회함.")
    @GetMapping("/purchases/user/{userId}")
    public ResponseEntity<List<ResponsePurchaseByAdminDto>> getOnePurchaseByUserIdByAdmin(@PathVariable("userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(purchaseService.getPurchaseByUserIdByAdmin(userId));
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

    // 정액 할인 부분

    // 정액 할인 쿠폰 생성(관리자 권한)
    @ApiOperation(value = "정액 할인 쿠폰 생성", notes = "새로운 쿠폰을 생성할 수 있다.")
    @PostMapping("/coupons/fix-discount")
    public ResponseEntity createFixDiscountCoupon(@RequestBody @Valid RequestFixDiscountCouponDto fixDiscountDto){
        fixDiscountCouponService.createFixDiscountCoupon(fixDiscountDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 정액 할인 쿠폰 전체 조회
    @ApiOperation(value = "정액 할인 쿠폰 전체 조회")
    @GetMapping("/coupons/fix-discount")
    public ResponseEntity<List<ResponseFixDiscountCouponDto>> getAllFixDiscountCoupons(){
        return ResponseEntity.status(HttpStatus.OK).body(fixDiscountCouponService.getAllFixDiscountCoupons());
    }

    // 정액 할인 쿠폰 개별 조회
    @ApiOperation(value = "정액 할인 쿠폰을 쿠폰 번호로 조회")
    @GetMapping("/coupons/fix-discount/{couponId}")
    public ResponseEntity<ResponseFixDiscountCouponDto> getFixDiscountCoupon(@PathVariable("couponId") Long couponId){
        return ResponseEntity.status(HttpStatus.OK).body(fixDiscountCouponService.getFixDiscountCoupon(couponId));
    }

    // 정액 할인 쿠폰 수정
    @ApiOperation(value = "정액 할인 쿠폰 수정")
    @PutMapping("/coupons/fix-discount")
    public ResponseEntity updateFixDiscountCoupon(@Valid @RequestBody RequestUpdateFixDiscountCouponDto requestUpdateFixDiscountCouponDto){
        fixDiscountCouponService.updateFixDiscountCoupon(requestUpdateFixDiscountCouponDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 정률 할인 부분

    // 정률 할인 쿠폰 생성(관리자 권한)
    @ApiOperation(value = "정률 할인 쿠폰 생성", notes = "새로운 쿠폰을 생성할 수 있다.")
    @PostMapping("/coupons/rate-discount")
    public ResponseEntity createRateDiscountCoupon(@RequestBody @Valid RequestRateDiscountCouponDto requestRateDiscountCouponDto){
        rateDiscountCouponService.createRateDiscountCoupon(requestRateDiscountCouponDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 정률 할인 쿠폰 전체 조회
    @ApiOperation(value = "정률 할인 쿠폰 전체 조회")
    @GetMapping("/coupons/rate-discount")
    public ResponseEntity<List<ResponseRateDiscountCouponDto>> getAllRateDiscountCoupons(){
        return ResponseEntity.status(HttpStatus.OK).body(rateDiscountCouponService.getAllRateDiscountCoupons());
    }

    // 정률 할인 쿠폰 개별 조회
    @ApiOperation(value = "정률 할인 쿠폰을 쿠폰 번호로 조회")
    @GetMapping("/coupons/rate-discount/{couponId}")
    public ResponseEntity<ResponseRateDiscountCouponDto> getRateDiscountCoupon(@PathVariable("couponId") Long couponId){
        return ResponseEntity.status(HttpStatus.CREATED).body(rateDiscountCouponService.getRateDiscountCoupon(couponId));
    }

    // 정액 할인 쿠폰 수정
    @ApiOperation(value = "정률 할인 쿠폰 수정")
    @PutMapping("/coupons/rate-discount")
    public ResponseEntity updateRateDiscountCoupon(@Valid @RequestBody RequestUpdateRateDiscountCouponDto requestUpdateRateDiscountCouponDto){
        rateDiscountCouponService.updateRateDiscountCoupon(requestUpdateRateDiscountCouponDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //쿠폰 활성화(관리자 권한)
    @ApiOperation(value = "쿠폰 활성화")
    @PutMapping("/coupons/activate/{couponId}")
    public ResponseEntity activateCoupon(@PathVariable("couponId") Long couponId) {
        couponService.activateCoupon(couponId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //쿠폰 비활성화(관리자 권한)
    @ApiOperation(value = "쿠폰 비활성화", notes = "더 이상 쿠폰을 발급받을 수 없게 쿠폰을 비활성화 한다.")
    @PutMapping("/coupons/deactivate/{couponId}")
    public ResponseEntity deactivateCoupon(@PathVariable("couponId") Long couponId) {
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
     * EVENT
     */
    @ApiOperation(value = "이벤트 생성", notes = "이벤트 생성과 동시에 이벤트와 연계된 쿠폰을 생성한다.")
    @PostMapping("/events")
    public ResponseEntity createEvent(@RequestBody @Valid RequestEventDto requestEventDto){
        eventService.createEvent(requestEventDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "이벤트 수정")
    @PutMapping("/events")
    public ResponseEntity updateEvent(@Valid @RequestBody RequestUpdateEventDto requestUpdateEventDto){
        eventService.updateEvent(requestUpdateEventDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "이벤트 종료하기(수동)", notes = "이벤트와 연관된 쿠폰을 정지시킨다.")
    @PutMapping("/events/{eventId}")
    public ResponseEntity quitEvent(@PathVariable("eventId") Long eventId){
        eventService.quitEvent(eventId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "이벤트 삭제", notes = "이벤트 번호를 파라미터로 넘겨 해당하는 이벤트를 삭제한다.")
    @DeleteMapping("/events/{eventId}")
    public ResponseEntity deleteEvent(@PathVariable("eventId") Long eventId){
        eventService.deleteEvent(eventId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
