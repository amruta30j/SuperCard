package com.gateway.creditcard.repository;

import com.gateway.creditcard.repository.model.CreditCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** CreditCardRepository is a repository for persisting/retrieving CreditCard information.
 *  Extending JpaRepository interface allows to create a repository implementation automatically at run time
 *  for a given CreditCardEntity class. Since this component is not implementing its own behaviour there is no
 *  need to test this class as the springframework.data.jpa.repository is a well tested one.
 */

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCardEntity, String> {

}
