package net.ddns.sbapiserver.service.helper;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.ddns.sbapiserver.domain.entity.client.Clients;
import net.ddns.sbapiserver.domain.entity.client.QClients;
import net.ddns.sbapiserver.domain.entity.common.Manufacturers;
import net.ddns.sbapiserver.domain.entity.common.Products;
import net.ddns.sbapiserver.domain.entity.order.Orders;
import net.ddns.sbapiserver.domain.entity.staff.Notice;
import net.ddns.sbapiserver.domain.entity.staff.QStaffs;
import net.ddns.sbapiserver.domain.entity.staff.Staffs;
import net.ddns.sbapiserver.exception.custom.BusinessException;
import net.ddns.sbapiserver.common.code.ErrorCode;
import net.ddns.sbapiserver.repository.client.ClientRepository;
import net.ddns.sbapiserver.repository.common.ManufacturersRepository;
import net.ddns.sbapiserver.repository.common.OrderRepository;
import net.ddns.sbapiserver.repository.common.ProductsRepository;
import net.ddns.sbapiserver.repository.staff.NoticeRepository;
import net.ddns.sbapiserver.repository.staff.StaffRepository;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ServiceErrorHelper {
    private final ClientRepository clientRepository;
    private final ProductsRepository productsRepository;
    private final StaffRepository staffRepository;
    private final NoticeRepository noticeRepository;
    private final OrderRepository orderRepository;
    private final ManufacturersRepository manufacturersRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public Clients findClientsOrElseThrow404(int clientId){
        ErrorCode clientNotFoundError = ErrorCode.CLIENT_NOT_FOUND_ERROR;
        return clientRepository.findById(clientId).orElseThrow(
                            () -> new BusinessException(clientNotFoundError, clientNotFoundError.getReason())
        );
    }

    public Products findProductsOrElseThrow404(int productId){
        ErrorCode productNotFoundError = ErrorCode.PRODUCT_NOT_FOUND_ERROR;
        return productsRepository.findById(productId).orElseThrow(
                () -> new BusinessException(productNotFoundError, productNotFoundError.getReason())
        );
    }

    public Staffs findStaffOrElseThrow404(int staffId){
        ErrorCode staffNotFoundError = ErrorCode.STAFF_NOT_FOUND_ERROR;
        return staffRepository.findById(staffId).orElseThrow(
                () -> new BusinessException(staffNotFoundError, staffNotFoundError.getReason())
        );
    }

    public Notice findNoticeOrElseThrow404(int noticeId){
        ErrorCode noticeNotFoundError = ErrorCode.NOTICE_NOT_FOUND_ERROR;
        return noticeRepository.findById(noticeId).orElseThrow(
                () -> new BusinessException(noticeNotFoundError, noticeNotFoundError.getReason())
        );
    }

    public Orders findOrderOrElseThrow404(int orderId){
        ErrorCode orderNotFoundError = ErrorCode.ORDER_NOT_FOUND_ERROR;
        return orderRepository.findById(orderId).orElseThrow(
                () -> new BusinessException(orderNotFoundError, orderNotFoundError.getReason())
        );
    }

    public Manufacturers findManufacturerOrElseThrow404(int manufacturerId){
        ErrorCode manufacturerNotFoundError = ErrorCode.MANUFACTURER_NOT_FOUND_ERROR;
        return manufacturersRepository.findById(manufacturerId).orElseThrow(
                () -> new BusinessException(manufacturerNotFoundError, manufacturerNotFoundError.getReason())
        );
    }

    public boolean isUserIdDuplicated(String userId){
        QClients clients = QClients.clients;
        QStaffs staffs = QStaffs.staffs;

        Clients findClient = jpaQueryFactory.selectFrom(clients)
                .where(clients.clientName.eq(userId))
                .fetchOne();

        Staffs findStaff = jpaQueryFactory.selectFrom(staffs)
                .where(staffs.staffUserId.eq(userId))
                .fetchOne();

        return findClient != null || findStaff != null;
    }

}
