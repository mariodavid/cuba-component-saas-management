package de.diedavids.cuba.ccsm.service.steps.create_subscription;

import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import de.diedavids.cuba.ccsm.CreateCustomerWithSubscriptionRequest;
import de.diedavids.cuba.ccsm.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CreateCustomerStepTest {


  @Mock
  private DataManager dataManager;


  @Test
  public void given_customerIdFromRequest_when_execution_then_aCustomerWithCorrectExternalIdIsCommitted() {

    // given:
    CreateCustomerWithSubscriptionRequest request = new CreateCustomerWithSubscriptionRequest();

    // and:
    request.setCustomerId("123");

    CreateCustomerStep sut = new CreateCustomerStep(dataManager, request);

    // and:
    Customer customer = new Customer();

    Mockito
        .when(dataManager.create(Customer.class))
        .thenReturn(customer);

    // and:
    CommitContext commitContext = new CommitContext();

    // when:
    sut.accept(commitContext);

    // then:
    assertThat(commitContext.getCommitInstances())
        .hasSize(1);

    // but:
    assertThat(customer.getExternalId())
            .isEqualTo("123");

  }

}