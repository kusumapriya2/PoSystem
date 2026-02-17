package com.example.repository;

import com.example.entity.Courier;
import com.example.entity.CourierServiceArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierServiceRepo extends JpaRepository<CourierServiceArea, Long> {
    List<CourierServiceArea> findByCourier_CourierId(Long courierId);

    boolean existsByCourier_CourierIdAndPincode(Long courierId, Long pincode);

    @Query("""
        select csa.courier
        from CourierServiceArea csa
        where csa.pincode = :pincode
          and csa.courier.status = :status
    """)
    List<Courier> findCouriersByPincodeAndStatus(@Param("pincode") Long pincode,
                                                 @Param("status") Courier.Status status);

    default List<Courier> findActiveCouriersByPincode(Long pincode) {
        return findCouriersByPincodeAndStatus(pincode, Courier.Status.ACTIVE);
    }
}
