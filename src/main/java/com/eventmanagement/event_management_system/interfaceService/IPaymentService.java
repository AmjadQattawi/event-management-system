package com.eventmanagement.event_management_system.interfaceService;

import com.eventmanagement.event_management_system.dto.PaymentDTO;
import com.eventmanagement.event_management_system.searchCriteria.PaymentSearchCriteria;
import com.eventmanagement.event_management_system.service.BaseService;
import jakarta.servlet.http.PushBuilder;
import org.springframework.data.domain.Page;

public interface IPaymentService extends BaseService<PaymentDTO,Long> {

    public PaymentDTO create(PaymentDTO paymentDTO);

    public PaymentDTO update(Long id, PaymentDTO paymentDTO) ;

    public Page<PaymentDTO> search(PaymentSearchCriteria paymentSearchCriteria,int page,int size);

}
