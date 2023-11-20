package CDProject.vfmarket.payment;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PaymentController {

    private final RefundService refundService;

    private final PaymentService paymentService;

    private IamportClient iamportClient;

    @Value("${portone.code}")
    private String apiKey;

    @Value("${portone.api.secretKey}")
    private String secretKey;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }

    //해당 경로로 요청을 받으면 요청으로 받은 주문 상품들을 저장한다.
//    @PostMapping("/payment")
//    public ResponseEntity<String> paymentComplete(@RequestHeader("Authorization") String token,
//                                                  @RequestBody List<OrderSaveDto> orderSaveDtos) throws IOException {
//        String orderNumber = orderSaveDtos.get(0).getOrderNumber();
//        try {
//            Long userId = sessionUser.getUserIdNo();
//            paymentService.saveOrder(userId, orderSaveDtos);
//            log.info("결제 성공 : 주문 번호 {}", orderNumber);
//            return ResponseEntity.ok().build();
//        } catch (RuntimeException e) {
//            log.info("주문 상품 환불 진행 : 주문 번호 {}", orderNumber);
//            String impToken = refundService.getToken(apiKey, secretKey);
//            refundService.refundRequest(impToken, orderNumber, e.getMessage());
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }

    //이게 결제 내역 조회인거네
    @PostMapping("/payment/validation/{imp_uid}")
    public IamportResponse<Payment> validateIamport(@PathVariable String imp_uid) {
        IamportResponse<Payment> payment = iamportClient.paymentByImpUid(imp_uid);
        log.info("결제 요청 응답. 결제 내역 - 주문 번호: {}", payment.getResponse().getMerchantUid());
        return payment;
    }
}
