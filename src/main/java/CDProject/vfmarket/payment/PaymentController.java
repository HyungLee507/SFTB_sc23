package CDProject.vfmarket.payment;

import static CDProject.vfmarket.global.AuthenticationUserId.getAuthenticatedUser;

import CDProject.vfmarket.domain.dto.OrderDTO.OrderSaveDto;
import CDProject.vfmarket.global.jwt.TokenValueProvider;
import CDProject.vfmarket.service.NotificationService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@PreAuthorize("isAuthenticated()")
public class PaymentController {

    private final RefundService refundService;

    private final PaymentService paymentService;

    private final NotificationService notificationService;

    private final TokenValueProvider tokenValueProvider;

    private IamportClient iamportClient;

    @Value("${portone.api.key}")
    private String apiKey;

    @Value("${portone.api.secretKey}")
    private String secretKey;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }

    @PostMapping("/payment")
    public ResponseEntity<String> paymentComplete(@RequestBody OrderSaveDto orderSaveDto) throws IOException {
        Long userId = getAuthenticatedUser();
        String orderNumber = orderSaveDto.getMerchant_uid();
        try {
            paymentService.saveOrder(userId, orderSaveDto);
            log.info("결제 성공 : 주문 번호 {}", orderNumber);
        } catch (RuntimeException e) {
            log.info("주문 상품 환불 진행 : 주문 번호 {}", orderNumber);
            String impToken = refundService.getToken(apiKey, secretKey);
            refundService.refundRequest(impToken, orderNumber, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
//        notificationService.makeNotification(userId, orderSaveDto.getId(),
//                orderSaveDto.getName() + " 상품 결제를 하셨습니다.");
//        notificationService.makeNotificationBySellerName(orderSaveDto.getSeller_name(), orderSaveDto.getId(),
//                orderSaveDto.getName() + " 상품에 대한 거래가 체결되었습니다.");
        return ResponseEntity.ok().build();
    }

    //portOne 결제 내역 조회
    @PostMapping("/payment/validation/{imp_uid}")
    public IamportResponse<Payment> validateIamport(@PathVariable String imp_uid) {
        IamportResponse<Payment> payment = iamportClient.paymentByImpUid(imp_uid);
        log.info("결제 요청 응답. 결제 내역 - 주문 번호: {}", payment.getResponse().getMerchantUid());
        log.info("결제 요청 응답. 결제 내역 - 주문 금액: {}", payment.getResponse().getAmount());
        log.info("결제 요청 응답. 결제 내역 - 주문 금액: {}", payment.getResponse().getBuyerTel());
        return payment;
    }
}
